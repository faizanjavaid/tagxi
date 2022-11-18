package com.tyt.driver.ui.homeScreen.inAppChat.chatmodel;

import java.util.List;

import javax.inject.Inject;

public class Chat {

    private String messageStatus;
    private String reqId;
    private String message;
    private boolean isSeen;
    private String time;

    private List<Chat> list;
    @Inject
    public Chat(String messageStatus, String reqId, String message, boolean isSeen, String time) {
        this.messageStatus = messageStatus;
        this.reqId = reqId;
        this.message = message;
        this.isSeen = isSeen;
        this.time = time;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Chat> getList() {
        return list;
    }

    public void setList(List<Chat> list) {
        this.list = list;
    }
}
