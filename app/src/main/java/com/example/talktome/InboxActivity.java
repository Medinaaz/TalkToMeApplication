package com.example.talktome;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class InboxActivity extends AppCompatActivity {
    private ListView sentEmails;
    private TextToSpeech mTTS;
    private String speechText;
    private List<String> mailsList;
    private List<String> fromList;
    private List<String> subjectsList;
    private String emailSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_list);

        sentEmails = findViewById(R.id.sentMails);

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.UK);

                    //I skipped the log in part.
                    ttsInitialized();

                    if (result == TextToSpeech.LANG_MISSING_DATA|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });


        List<Map<String, String>> data = new ArrayList<>(5);

        try{
            GetEmails getEmails = new GetEmails();
            String allMails = getEmails.execute("inbox").get();

            JSONObject json = new JSONObject(allMails);
            String mails = json.get("contents").toString();
            String from = json.get("from").toString();
            String subjects = json.get("subjects").toString();

            mailsList = Arrays.asList(mails.split(",[ ]*"));
            fromList = Arrays.asList(from.split(",[ ]*"));
            subjectsList = Arrays.asList(subjects.split(",[ ]*"));
            //",[ ]*"
            fromList.set(0, fromList.get(0).replace("[", ""));
            fromList.set(fromList.size() - 1, fromList.get(fromList.size() - 1).replace("]", ""));

            subjectsList.set(0, subjectsList.get(0).replace("[", ""));
            subjectsList.set(subjectsList.size() - 1, subjectsList.get(subjectsList.size() - 1).replace("]", ""));

            for(int i = 0; i < mailsList.size(); i++){
                Map<String, String> datum = new HashMap<String, String>(2);
                datum.put("First Line", fromList.get(i));
                datum.put("Second Line", subjectsList.get(i));
                data.add(i, datum);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[] {"First Line", "Second Line" },
                new int[] {android.R.id.text1, android.R.id.text2 });

        sentEmails.setAdapter(adapter);
    }

    private void ttsInitialized(){
        mTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {}
            @Override
            public void onDone(String s) {listen(); }
            @Override
            public void onError(String s) {}
        });

        speechText = "InboxActivity is opened, say email and a number to read the email with that order and say go to go back";

        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID2");

        mTTS.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "messageID2");
    }

    private void listen(){
        //intent to show speech to text dialog
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");

        //start intent
        try{
            //at this block we do not have an error
            startActivityForResult(intent, 1000);
        }
        catch (Exception e){
            //we get the message error if it was one
            Toast.makeText(this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1000:{
                //get text array from voice intent
                if (resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    switch (speechText) {
                        case "InboxActivity is opened, say email and a number to read the email with that order and say go to go back":
                            if (result.get(0).equals("back")) {
                                Intent intentToMain = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intentToMain);
                            }

                            if (result.get(0).equals("exit")) {
                                finishAndRemoveTask();
                            }

                            if ((result.get(0)).contains("email")) {
                                int email_index = 0;

                                if(result.get(0).contains("two") || result.get(0).contains("2") || result.get(0).contains("to")) {
                                    email_index = 1;
                                }
                                else if(result.get(0).contains("three") || result.get(0).contains("3") || result.get(0).contains("tree")) {
                                    email_index = 2;
                                }
                                else if(result.get(0).contains("four") || result.get(0).contains("4") || result.get(0).contains("for")) {
                                    email_index = 3;
                                }
                                else if(result.get(0).contains("five") || result.get(0).contains("5")) {
                                    email_index = 4;
                                }
                                else if(result.get(0).contains("six") || result.get(0).contains("6")) {
                                    email_index = 5;
                                }
                                else if(result.get(0).contains("seven") || result.get(0).contains("7")) {
                                    email_index = 6;
                                }
                                else if(result.get(0).contains("eight") || result.get(0).contains("8") || result.get(0).contains("ate")) {
                                    email_index = 7;
                                }
                                else if(result.get(0).contains("nine") || result.get(0).contains("9")) {
                                    email_index = 8;
                                }
                                else if(result.get(0).contains("ten") || result.get(0).contains("10")) {
                                    email_index = 9;
                                }

                                //Read the required email
                                emailSpeech = "Email " + Integer.toString(email_index + 1) + " is " + mailsList.get(email_index);

                                HashMap<String, String> map = new HashMap<>();
                                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID3");

                                mTTS.speak(emailSpeech, TextToSpeech.QUEUE_FLUSH, null, "messageID3");
                            }

                            else {
                                speechText = "InboxActivity is opened, say email and a number to read the email with that order and say go to go back";

                                mTTS.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "messageID2");
                            }
                    }
                }
                break;
            }
        }
    }

}
