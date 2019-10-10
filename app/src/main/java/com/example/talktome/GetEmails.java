package com.example.talktome;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GetEmails extends AsyncTask<String,String,String> {

    protected GetEmails() {}

    private String mails;

    @Override
    protected String doInBackground(String... params) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        String postBody = "{\"userMail\":\"emailclientproject1@gmail.com\",\"userPassword\":\"computerproject1\"}";

        try {
            JSONObject obj = new JSONObject(postBody);
        } catch (Throwable t) {

        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://seemailbackendserver.appspot.com/getEmails")
                .post(RequestBody.create(JSON, postBody))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String resStr = response.body().string().toString();
            JSONObject json = new JSONObject(resStr);
            mails = json.get("mails").toString();
            int length = mails.length();
        }
        catch (JSONException je){
            je.printStackTrace();
        }
        catch (IOException io){
            io.printStackTrace();
        }
        return mails;
    }

    @Override
    protected void onPostExecute(String result) {
        returnEmails();
    }

    public String returnEmails(){
        return mails;
    }

}