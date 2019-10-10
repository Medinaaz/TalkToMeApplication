package com.example.talktome;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class Log_In extends AppCompatActivity {

    private TextToSpeech mTTS;
    private String speechText;
    private EditText SenderEmailEditText;
    private EditText PasswordEmailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log__in);

        SenderEmailEditText = findViewById(R.id.editSenderText);
        PasswordEmailText = findViewById(R.id.editPasswordText);

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.US);

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
    private void ttsInitialized() {
        mTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
            }

            @Override
            public void onDone(String s) {
                listen();
            }

            @Override
            public void onError(String s) {
            }
        });
        speechText = "Log in page is opened, say your email address";

        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID1");

        mTTS.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "messageID1");
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
                    ArrayList<String> result= data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    switch (speechText) {
                        case "Log in page is opened, say your email address":
                            SenderEmailEditText.setText("yakuphanbilgic@gmail.com");
                            speechText = "Say your password";
                            mTTS.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "messageID1");
                            break;
                        case "Say your password":
                            PasswordEmailText.setText(result.get(0));
                            speechText = "Say login in order to log in to the app";
                            mTTS.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "messageID1");
                            break;
                        case "Say login in order to log in to the app":
                            if(result.get(0).equals("login") || result.get(0).equals("log in")){
                                Intent intentToLogIn = new Intent(getApplicationContext(), SentList.class);
                                startActivity(intentToLogIn);
                                break;
                            } else{
                                Toast.makeText(this, "you couldn't say login, you said " + result.get(0),Toast.LENGTH_LONG).show();
                            }
                        default:
                            break;
                    }
                }
                break;
            }
        }
    }
}




