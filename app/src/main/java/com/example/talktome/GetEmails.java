package com.example.talktome;

import android.os.AsyncTask;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetEmails extends AsyncTask<String,String,String> {

    protected GetEmails() {}

    private String resStr;

    @Override
    protected String doInBackground(String... params) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String postBody = "{\"userMail\":\"emailclientproject1@gmail.com\",\"userPassword\":\"computerproject1\"}";

        String myParams = params[0];
        String url = "";

        /*try {
            JSONObject obj = new JSONObject(postBody);
        } catch (Throwable t) {

        }*/

        OkHttpClient client = new OkHttpClient();

        if(myParams.equals("inbox")){
            url = "https://seemailbackendserver.appspot.com/getSent";
        }
        else if(myParams.equals("sent")){
            url = "https://seemailbackendserver.appspot.com/getInbox";
        }
        else if(myParams.equals("compose")){
            url = "https://seemailbackendserver.appspot.com/sendEmail";
            String receiver = params[1];
            String subject = params[2];
            String content = params[3];

            postBody = "{\"userMail\":\"emailclientproject1@gmail.com\",\"userPassword\":\"computerproject1\"," +
                    "\"receiver\":\"" + receiver + "\",\"subject\":\"" + subject + "\",\"text\":\"" + content +"\"}";
        }

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