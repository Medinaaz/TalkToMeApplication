package com.example.talktome;

public class Email {
    private String receiver;
    private String subject;
    private String content;
    private String sender;

    public String getContent() {
        return content;
    }
    public String getReceiver(){
        return receiver;
    }
    public String getSubject(){
        return subject;
    }
    public String getSender(){
        return sender;
    }
    public void setReceiver(String r){
        this.receiver = r;
    }
    public void setSubject(String s){
        this.subject = s;
    }
    public void setContent(String c){
        this.content = c;
    }
    public void setSender(String se){
        this.sender = se;
    }
}
