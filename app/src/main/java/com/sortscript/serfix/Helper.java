package com.sortscript.serfix;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.sortscript.serfix.modela.JobModel;

public class Helper {


    public static void setUser(Context context, ModelForFirebase model) {
        SharedPreferences mPrefs = context.getSharedPreferences("APP_PREF", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(model);
        prefsEditor.putString("User", json);
        prefsEditor.apply();
    }

    public static ModelForFirebase getUser(Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences("APP_PREF", MODE_PRIVATE);
        ModelForFirebase model;
        try {
            Gson gson = new Gson();
            String json = mPrefs.getString("User", "");
            model = gson.fromJson(json, ModelForFirebase.class);
        }catch (Exception e){
            model =  new ModelForFirebase("No address","No phone number","No user");
        }
        return model;
    }

    public static void setJob(Context context, JobModel model) {
        SharedPreferences mPrefs = context.getSharedPreferences("APP_PREF", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(model);
        prefsEditor.putString("Job", json);
        prefsEditor.apply();
    }

    public static JobModel getJob(Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences("APP_PREF", MODE_PRIVATE);
        JobModel model;
        try {
            Gson gson = new Gson();
            String json = mPrefs.getString("Job", "");
            model = gson.fromJson(json, JobModel.class);
        }catch (Exception e){
            model =  new JobModel();
        }
        return model;
    }
}
