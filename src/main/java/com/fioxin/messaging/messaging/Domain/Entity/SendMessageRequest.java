package com.fioxin.messaging.messaging.Domain.Entity;

import java.util.List;

public class SendMessageRequest {
    private User user;
    private List<Message> messages;



    public SendMessageRequest() {
    }


    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}
