package com.example.talktome;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginRequest extends AsyncTask<String,String,String> {

    protected LoginRequest() {}

    private String resStr;

    @Override
    protected String doInBackground(String... params) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String postBody = "{\"email\":\"" + params[0] + "\",\"password\":\"" + params[1] + "\"}";

        String myParams = params[0];
        String url = "";

        /*try {
            JSONObject obj = new JSONObject(postBody);
        } catch (Throwable t) {

        }*/

        OkHttpClient client = new OkHttpClient();

        url = "https://seemailbackendserver.appspot.com/login";

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, postBody))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            resStr = response.body().string().toString();
        }
        catch (IOException io){
            io.printStackTrace();
        }
        return resStr;
    }

    @Override
    protected void onPostExecute(String result) {
    }
}