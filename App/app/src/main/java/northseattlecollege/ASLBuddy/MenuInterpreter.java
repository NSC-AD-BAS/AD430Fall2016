package northseattlecollege.ASLBuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Author: Nathan Flint
 * Created 10/10/2016
 */

public class MenuInterpreter extends AppCompatActivity implements CompoundButton.OnClickListener {

    private final RefreshSkypeStatus refreshSkypeStatus;
    private final RefreshVideoStatus refreshVideoSwitch;
    private final RefreshLocationStatus refreshLocationSwitch;
    private final RefreshSykpeName refreshSykpeName;
    private UpdateInterpreterLocation updateInterpreterLocation;
    private UpdateLocationThread updateLocationThread;
    private SharedPreferences mPrefs;
    private Switch videoSwitch, locationSwitch;
    public InterpreterStatus status;
    public LocationService locationService;
    public Location location;
    private TextView skypeStatus;
    private EditText skypeName;
    private int userId;

    public MenuInterpreter() {
        refreshSkypeStatus = new RefreshSkypeStatus();
        refreshVideoSwitch = new RefreshVideoStatus();
        refreshLocationSwitch = new RefreshLocationStatus();
        refreshSykpeName = new RefreshSykpeName();
        updateInterpreterLocation = new UpdateInterpreterLocation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_interpreter);

        videoSwitch = (Switch)findViewById(R.id.videoSwitch);
        locationSwitch = (Switch)findViewById(R.id.locationSwitch);
        skypeStatus = (TextView) findViewById(R.id.skypeStatus);
        skypeName = (EditText)findViewById(R.id.skypeName);

        videoSwitch.setOnClickListener(this);
        locationSwitch.setOnClickListener(this);

        //get switch status from server
        userId = getIntent().getIntExtra("userId", userId);
        status = new InterpreterStatus(userId);
        refreshSkypeStatus.execute();
        refreshVideoSwitch.execute();
        refreshLocationSwitch.execute();
        refreshSykpeName.execute();

        //setting to false for debugging purposes
        locationService = new LocationService(this);
        setupUpdateLocationThread(false);

        //setting the logout button handler
        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private AsyncTask<Void, Void, Void> updateLocationStatusAsync(final boolean isChecked) {
        return new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                //kills the current thread, starts the thread again
                setupUpdateLocationThread(isChecked);
                status.setLocationStatus(isChecked);
                return null;
            }}.execute();
    }

    private AsyncTask<Void, Void, Void> updateVideoStatusAsync(final boolean isChecked) {
        return new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                status.setVideoStatus(isChecked);
                return null;
            }
        }.execute();
    }

    public void setupUpdateLocationThread(boolean status){
        killThread();
        updateLocationThread = new UpdateLocationThread(status, this);
        updateLocationThread.start();
    }

    @Override
    public void onBackPressed() {
        // do nothing, we want users to use the logout button
    }

    public void SetLocation(Location location) {
        this.location = location;
    }

    public void sendLocationToServer(){
        updateInterpreterLocation = new UpdateInterpreterLocation();
        updateInterpreterLocation.execute();
    }

    @Override
    public void onClick(View v) {
        if (!(v instanceof Switch))
            return;

        boolean checkState = ((Switch) v).isChecked();

        if (v == videoSwitch)
            updateVideoStatusAsync(checkState);
        else if (v == locationSwitch)
            updateLocationStatusAsync(checkState);
    }

    /**
     * Method for inflating settings button into options menu
     * @param menu
     * @return wasSuccessful
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return true;
    }

    /**
     * Handler for selecting settings button, will come back to this
     * activity after settings activity (using native back button)
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent navigationIntent = new Intent(MenuInterpreter.this, Settings.class);
                // pass userId to the following activity
                navigationIntent.putExtra("userId", userId);
                MenuInterpreter.this.startActivity(navigationIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("userId", userId);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userId = savedInstanceState.getInt("userId");
    }

    /**
     * Terminates this user's session and returns to the login/signup page,
     * starts activity LoginActivity.java using Android Intent.
     */
    private void logout() {
        finish();
        killThread();
        Intent navigationIntent = new Intent(MenuInterpreter.this, LoginActivity.class);
        MenuInterpreter.this.startActivity(navigationIntent);
    }

    private void killThread(){
        if(updateLocationThread != null){
            updateLocationThread.kill();
        }
    }
    private class RefreshSkypeStatus extends AsyncTask<Void, Void, Boolean> {
        protected Boolean doInBackground(Void... asdf) {

            return SkypeResources.isSkypeClientInstalled(MenuInterpreter.this);
        }

        protected void onPostExecute(Boolean isSkypeInstalled) {

            String statusText = isSkypeInstalled ? "Installed" : "Not Installed";
            int statusTextColor = isSkypeInstalled ? Color.GREEN : Color.RED;
            skypeStatus.setTextColor(statusTextColor);
            skypeStatus.setText(statusText);
        }
    }

    private class RefreshVideoStatus extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return status.getVideoStatus();
        }

        protected void onPostExecute(Boolean status) {
            videoSwitch.setChecked(status);
        }
    }

    private class RefreshLocationStatus extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return status.getLocationStatus();
        }

        protected void onPostExecute(Boolean status) {
            //kills previous thread before starting a new one
            //start the thread here on activity signin
            setupUpdateLocationThread(status);
            locationSwitch.setChecked(status);
        }
    }

    private class RefreshSykpeName extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... params) { return status.getSkypeName(); }
        protected void onPostExecute(String s) {
            skypeName.setText(s);
        }
    }

    private class UpdateInterpreterLocation extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            if(location != null){
                status.setInterpreterLocation(location.getLatitude(), location.getLongitude());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}

