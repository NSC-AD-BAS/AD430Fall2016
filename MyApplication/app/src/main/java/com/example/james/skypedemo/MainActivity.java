package com.example.james.skypedemo;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button skypeButton = (Button) findViewById(R.id.skypeButton);
        TextView usernameInput = (EditText) findViewById(R.id.username);


        skypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skypeButton(v);
            }
        });

    }

    public void skypeButton(View v){
        TextView usernameInput = (EditText) findViewById(R.id.username);
        String username = usernameInput.getText().toString();
        initiateSkypeUri(this, username);
    }
/*
    * this method is an alternate version of initiateSkypeUri based on a stackoverflow submission
    * it contains useful logging but does not prompt to open play store
    public static void skype(String username, Context ctx) {
        try {
            //Intent sky = new Intent("android.intent.action.CALL_PRIVILEGED");
            //the above line tries to create an intent for which the skype app doesn't supply public api

            Intent sky = new Intent("android.intent.action.VIEW");
            sky.setData(Uri.parse("skype:" + username + "?call"));
            Log.d("UTILS", "user:" + username);
            ctx.startActivity(sky);
        } catch (ActivityNotFoundException e) {
            Log.e("SKYPE CALL", "Skype failed", e);
        }

    }*/


    /**
     * Initiate the actions encoded in the specified URI.
     */
    public void initiateSkypeUri(Context myContext, String username) {

        // Make sure the Skype for Android client is installed.
        if (!isSkypeClientInstalled(myContext)) {
            goToMarket(myContext);
            return;
        }

        // Create the Intent from our Skype URI.
        Uri skypeUri = Uri.parse("skype:" + username + "?call");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, skypeUri);

        // Restrict the Intent to being handled by the Skype for Android client only.
        myIntent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Initiate the Intent. It should never fail because you've already established the
        // presence of its handler (although there is an extremely minute window where that
        // handler can go away).
        myContext.startActivity(myIntent);

        return;
    }

    /*
    determine if skype is installed
     */
    public boolean isSkypeClientInstalled(Context myContext) {
        PackageManager myPackageMgr = myContext.getPackageManager();
        try {
            myPackageMgr.getPackageInfo("com.skype.raider", PackageManager.GET_ACTIVITIES);
        }
        catch (PackageManager.NameNotFoundException e) {
            return (false);
        }
        return (true);
    }

    /*
    install client through market uri scheme

     */
    public void goToMarket(Context myContext) {
        Uri marketUri = Uri.parse("market://details?id=com.skype.raider");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myContext.startActivity(myIntent);

        return;
    }
}
