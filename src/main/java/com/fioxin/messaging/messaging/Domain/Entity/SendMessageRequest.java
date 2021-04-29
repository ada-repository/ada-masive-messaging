package com.fioxin.messaging.messaging.Domain.Entity;

import java.util.List;

public class SendMessageRequest {
    private User user;
    private List<NotficationMessage> messages;



    public SendMessageRequest() {
    }


    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<NotficationMessage> getMessages() {
        return this.messages;
    }

    public void setMessages(List<NotficationMessage> messages) {
        this.messages = messages;
    }

}
