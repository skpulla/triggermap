package com.triggermap.triggermap;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.newrelic.agent.android.NewRelic;

public class MyActivity extends Activity {

    public final static String MyMessage = "hello world!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NewRelic.withApplicationToken(
                "AAa6ff16ea8a0c215eb3a13c7f17a33d2534202373"
        ).start(this.getApplication());

        ActionBar actionBar = getActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_my);
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

    public void relaxMe(View view) {
        Intent intent = new Intent(this, MoodActivity.class);
        intent.putExtra(MyMessage, "hello world");
        startActivity(intent);
    }
}
