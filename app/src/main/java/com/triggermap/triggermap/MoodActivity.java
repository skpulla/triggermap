package com.triggermap.triggermap;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MoodActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_mood);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mood, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void buttonClick(View view) {
        Intent intent = new Intent(this, HomeSpace.class);
        int moodValue = 0;
        switch (view.getId()) {
            case R.id.button1:
                moodValue = 1;
                break;
            case R.id.button2:
                moodValue = 2;
                break;
            case R.id.button3:
                moodValue = 3;
                break;
            case R.id.button4:
                moodValue = 4;
                break;
            case R.id.button5:
                moodValue = 5;
                break;
            case R.id.button6:
                moodValue = 6;
                break;
            case R.id.button7:
                moodValue = 7;
                break;
            case R.id.button8:
                moodValue = 8;
                break;
            case R.id.button9:
                moodValue = 9;
                break;
        }
//        makePostRequest(1,moodValue);
        new PostMoodTask().execute("1", String.valueOf(moodValue));
        startActivity(intent);
    }

    private void makePostRequest(int userId, int moodValue) {

    }

    class PostMoodTask extends AsyncTask<String, Void, Void> {

        private Exception exception;

        @Override
        protected Void doInBackground(String... strings) {

            String userId = strings[0];
            String moodValue = strings[1];

            HttpClient httpClient = new DefaultHttpClient();
            // replace with your url
            HttpPost httpPost = new HttpPost("http://ownmygame-api-env.elasticbeanstalk.com/api/data/" + userId);

            //Post Data
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
            nameValuePair.add(new BasicNameValuePair("type", "mood"));
            nameValuePair.add(new BasicNameValuePair("value", moodValue));

            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                // log exception
                e.printStackTrace();
            }

            //making POST request.
            try {
                HttpResponse response = httpClient.execute(httpPost);
                // write response to log
                Log.d("Http Post Response:", response.toString());
            } catch (ClientProtocolException e) {
                // Log exception
                e.printStackTrace();
            } catch (IOException e) {
                // Log exception
                e.printStackTrace();
            }

            return null;
        }
    }
}