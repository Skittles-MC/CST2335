package com.example.androidlabs;

public class message {

    private String msg;
    private boolean isSend;

    public message(String msg, boolean isSend) {
        this.msg = msg;
        this.isSend = isSend;
    }

    public message() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }
}
