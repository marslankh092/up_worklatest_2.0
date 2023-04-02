package com.sortscript.serfix.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.serfix.adapters.ChatAdapter;
import com.sortscript.serfix.modela.ChatModel;
import com.sortscript.serfix.activities.MainActivity;
import com.sortscript.serfix.ModelForFirebase;
import com.sortscript.serfix.databinding.FragmentChatBinding;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    FragmentChatBinding binding;
    ChatAdapter chatAdapter = null;
    ArrayList<ChatModel> chatModelArrayList = new ArrayList<>();

    ModelForFirebase model;

    public ChatFragment(ModelForFirebase modelForFirebase) {
        this.model = modelForFirebase;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        chatAdapter = new ChatAdapter(requireContext());
        getChat();
        binding.serviceProviderRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(true);
        binding.serviceProviderRecyclerView.setLayoutManager(linearLayoutManager);

        binding.sendBtn.setOnClickListener(view1 -> {

            sendSMS(binding.chatEditText.getText().toString());

            binding.chatEditText.setText("");
        });

        return view;
    }

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    private void sendSMSToUser(String message) {
        String UID = model.getUID();
        Log.d("sendSMSToUser", "sendSMSToUser: " + UID);
//        String Private_cart = ref.push().getKey();
        ref = FirebaseDatabase.getInstance().getReference("UpWork").child("Chat").child(UID).child("msg" + System.currentTimeMillis() + "");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref.child("msg").setValue(message);
                ref.child("to").setValue(model.getUID());
                ref.child("from").setValue("Car Tower");

                ChatModel chatModel = new ChatModel();
                chatModel.setMsg(message);
                chatModel.setFrom(UID);
                chatModel.setTo(model.getUID());
                chatModel.setFromYou(true);
                chatModelArrayList.add(chatModel);
                if (chatAdapter == null) {
                    chatAdapter.setData(chatModelArrayList);
                    binding.serviceProviderRecyclerView.setAdapter(chatAdapter);
                } else {
                    chatAdapter.notifyItemInserted(chatModelArrayList.size() - 1);
                    binding.serviceProviderRecyclerView.smoothScrollToPosition(chatModelArrayList.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendSMS(String msg) {
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("sendSMSToUser", "sendSMSToUser: " + UID);
        String Private_cart = ref.push().getKey();
        ref = FirebaseDatabase.getInstance().getReference("UpWork").child("Chat").child(UID).child("msg" + System.currentTimeMillis() + "");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref.child("msg").setValue(msg);
                ref.child("to").setValue(model.getUID());
                ref.child("from").setValue(UID);
                Log.e("SMS-", "sender->>" + UID + "receiver->>" + model.getUID() + "message->>" + msg);
                ChatModel chatModel = new ChatModel();
                chatModel.setMsg(msg);
                chatModel.setFrom(UID);
                chatModel.setTo(model.getUID());
                chatModel.setFromYou(chatModel.getFrom().equals("User"));
                chatModelArrayList.add(chatModel);

//                if (chatAdapter == null) {
                chatAdapter.setData(chatModelArrayList);
//                    binding.serviceProviderRecyclerView.setAdapter(chatAdapter);
//                } else {
//                    chatAdapter.notifyItemInserted(chatModelArrayList.size() - 1);
//                    binding.serviceProviderRecyclerView.smoothScrollToPosition(chatModelArrayList.size() - 1);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getChat() {
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("checkType", "getChat: " + model.getServiceType());
        if (!model.getServiceType().equals("Car Tower"))
            UID = model.getUID();
        Log.d("checkType", "getChat: " + UID);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UpWork").child("Chat").child(UID);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("inSingle", "onDataChange: " + snapshot);

                for (DataSnapshot ds : snapshot.getChildren()) {
                    {
                        ChatModel chatModel = ds.getValue(ChatModel.class);
                        if (model.getServiceType().equals("User")) {
                            chatModel.setFromYou(chatModel.getFrom().equals("User"));
                        } else
                            chatModel.setFromYou(!chatModel.getFrom().equals("User"));
                        chatModelArrayList.add(chatModel);
//                        if (chatAdapter == null) {
                        chatAdapter.setData(chatModelArrayList);
//                        } else
//                            chatAdapter.notifyItemInserted(chatModelArrayList.size() - 1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onBackPressed() {
        if (getParentFragmentManager().getBackStackEntryCount() > 0) {
            getParentFragmentManager().popBackStack();
        } else {
            startActivity(new Intent(getContext(), MainActivity.class));
        }
    }
}
