package com.example.talktome;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.RecognizerIntent;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import static java.lang.System.exit;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech mTTS;
    private TextView mTextTv;
    private ImageButton mVoiceBtn;
    private String speechText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        speechText = "Welcome to email client. Menu options are inbox, sent email and compose an email.";

        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID");

        mTTS.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "messageID");
    }

    private void listen(){
        //intent to show speech to text dialog
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Select one of the options");

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
                    ArrayList<String> result= data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    switch(result.get(0)){
                        case "inbox":
                            Intent intentToInbox = new Intent(getApplicationContext(), Inbox.class);
                            startActivity(intentToInbox);
                            break;
                        case "sent email":
                            Intent intentToSent = new Intent(getApplicationContext(), SentList.class);
                            startActivity(intentToSent);
                            break;
                        case "compose an email":
                            Intent intentToCompose = new Intent(getApplicationContext(), Compose.class);
                            startActivity(intentToCompose);
                            break;
                        case "exit":
                            exit(0);
                        default:
                            speechText = "Welcome to email client. Menu options are inbox, sent email and compose an email.";
                            mTTS.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "messageID");
                            break;
                    }
                }
                break;
            }
        }
    }
}