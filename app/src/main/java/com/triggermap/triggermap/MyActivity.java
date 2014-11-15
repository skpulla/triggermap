package com.triggermap.triggermap;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import io.oauth.OAuth;
import io.oauth.OAuthCallback;
import io.oauth.OAuthData;
import io.oauth.OAuthRequest;

public class MyActivity extends Activity implements OAuthCallback {

    Button facebookButton;
    Button fitbitButton;
    TextView facebookText;
    TextView fitbitText;

    boolean isFB = false;
    boolean isFitbit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // TODO: remove hardcoded value from public repo
        final OAuth oauth = new OAuth(this);
        oauth.initialize("DHQZjSu606skjSklp-Btsgrv1QY");

        facebookButton = (Button) findViewById(R.id.facebook);
        fitbitButton = (Button) findViewById(R.id.fitbit);
        facebookText = (TextView) findViewById(R.id.facebookText);
        fitbitText = (TextView) findViewById(R.id.fitbitText);

        facebookButton.setOnClickListener(new View.OnClickListener() { // Listen the on click event
            @Override
            public void onClick(View v)
            {
                isFB = true;
                isFitbit = false;
                oauth.popup("facebook", MyActivity.this); // Launch the pop up with the right provider & callback
            }
        });

        fitbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                isFB = false;
                isFitbit = true;
                oauth.popup("fitbit", MyActivity.this); // Launch the pop up with the right provider & callback
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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

    @Override
    public void onFinished(OAuthData data) {
        final TextView textview = data.provider.equals("fitbit") ? fitbitText : facebookText;
        if ( ! data.status.equals("success")) {
            textview.setTextColor(Color.parseColor("#FF0000"));
            textview.setText("error, " + data.error);
        }

        // You can access the tokens through data.token and data.secret

        textview.setText("loading...");
        textview.setTextColor(Color.parseColor("#00FF00"));

        // Let's skip the NetworkOnMainThreadException for the purpose of this sample.
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        // To make an authenticated request, you can implement OAuthRequest with your prefered way.
        // Here, we use an URLConnection (HttpURLConnection) but you can use any library.
        data.http(data.provider.equals("facebook") ? "/me" : "/1/user/-/profile.json", new OAuthRequest() {
            private URL url;
            private URLConnection con;

            @Override
            public void onSetURL(String _url) {
                try {
                    url = new URL(_url);
                    con = url.openConnection();
                } catch (Exception e) { e.printStackTrace(); }
            }

            @Override
            public void onSetHeader(String header, String value) {
                con.addRequestProperty(header, value);
            }

            @Override
            public void onReady() {
                try {
                    BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder total = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        total.append(line);
                    }
                    JSONObject result = new JSONObject(total.toString());

                    if(isFB) {
                        String name = result.getString("name");
                        textview.setText("hello, " + name);
                    } else if(isFitbit) {
                        JSONObject userInfo = result.getJSONObject("user");
                        textview.setText("hello, " + userInfo.getString("fullName"));
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }

            @Override
            public void onError(String message) {
                textview.setText("error: " + message);
            }
        });
    }
}
