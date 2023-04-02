package com.sortscript.serfix.modela;

public class ChatModel {
    private String msg;
    private String to;
    private String from;
    public boolean isFromYou;

    public ChatModel() {
    }

    public ChatModel(String msg, String to, String from, boolean isFromYou) {
        this.msg = msg;
        this.to = to;
        this.from = from;
        this.isFromYou = isFromYou;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isFromYou() {
        return isFromYou;
    }

    public void setFromYou(boolean fromYou) {
        isFromYou = fromYou;
    }
}
