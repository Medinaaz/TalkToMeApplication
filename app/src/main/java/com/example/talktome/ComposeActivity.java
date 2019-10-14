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
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ComposeActivity extends AppCompatActivity {
    private TextToSpeech mTTS;
    private String speechText;
    private EditText receiverEmailEditText;
    private EditText subjectEditText;
    private EditText contentEmailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        receiverEmailEditText = findViewById(R.id.editTextReceiver);
        subjectEditText = findViewById(R.id.editTextSubject);
        contentEmailEditText = findViewById(R.id.editTextContent);

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.UK);

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

        speechText = "ComposeActivity page is opened, say email address of the receiver";

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

                    /*SendEmailTLS sendEmailTLS = new SendEmailTLS();
                    Toast.makeText(getApplicationContext(), sendEmailTLS.sendEmail("mySubject", "myContent", "emailclientproject1@gmail.com", "emailclientproject1@gmail.com"), Toast.LENGTH_LONG).show();
                    */

                    switch (speechText) {
                        case "ComposeActivity page is opened, say email address of the receiver":
                            //receiverEmailEditText.setText("computerproject714@gmail.com");
                            speechText = "Say subject of the email";
                            mTTS.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "messageID1");
                            break;
                        case "Say subject of the email":
                            subjectEditText.setText(result.get(0));
                            speechText = "Say content of the email";
                            mTTS.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "messageID1");
                            break;
                        case "Say content of the email":
                            contentEmailEditText.setText(result.get(0));
                            speechText = "Say send in order to send the email and back to go back";
                            mTTS.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "messageID1");
                            break;
                        case "Say send in order to send the email and back to go back":
                            if(result.get(0).equals("send")){
                                try{
                                    GetEmails getEmails = new GetEmails();

                                    String[] parameters = {"compose",
                                            receiverEmailEditText.getText().toString(),
                                            subjectEditText.getText().toString(),
                                            contentEmailEditText.getText().toString()
                                    };

                                    String sendEmail = getEmails.execute(parameters).get();

                                    Toast.makeText(getApplicationContext(), sendEmail, Toast.LENGTH_LONG).show();

                                } catch (Exception e){
                                    e.printStackTrace();
                                }

                                Intent intentToMain = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intentToMain);
                            }
                            else if(result.get(0).equals("back")){
                                Intent intentToMain = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intentToMain);
                            }
                            else if(result.get(0).equals("exit")){
                                finishAndRemoveTask();
                            }
                            else{
                                speechText = "Say send in order to send the email and back to go back";
                                mTTS.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "messageID1");
                            }
                    }
                }
                break;
            }
        }
    }
}
