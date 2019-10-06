package com.example.talktome;

import android.os.AsyncTask;
import java.io.*;
import java.util.*;
import javax.mail.*;

public class GetEmails extends AsyncTask<String,Void,Message[]> {

    protected GetEmails() {}

    private Message[] messages;
    //
    // inspired by :
    // http://www.mikedesjardins.net/content/2008/03/using-javamail-to-read-and-extract/
    //

    @Override
    protected Message[] doInBackground(String... urls) {
        Folder folder = null;

        //create properties field
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.imap.socketFactory.fallback", "false");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); //TLS
        props.put("simple-content", "true");

        try {
            // open session
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", "yakuphanbilgic@gmail.com", "kosvkeonriszprry");

            String yakup = "alueda";

            // get inbox folder
            folder = store.getFolder("inbox");
            folder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            messages = folder.getMessages();

            return messages;
        }
        catch (MessagingException m){
            m.printStackTrace();
        }

        return messages;
    }

    @Override
    protected void onPostExecute(Message[] result) {

    }

    public Message[] returnEmails(){
        return messages;
    }

}