package com.example.talktome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SentList extends AppCompatActivity {
    private ListView sentEmails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_list);

        sentEmails = findViewById(R.id.sentMails);

        String[] emails = new String[] {"email1", "email2", "alueda"};

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, emails);

        sentEmails.setAdapter(adapter);

    }
}
