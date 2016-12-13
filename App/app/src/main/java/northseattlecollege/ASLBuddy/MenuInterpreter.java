package northseattlecollege.ASLBuddy;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import northseattlecollege.ASLBuddy.fragments.SkypeUsernameHelpFragment;

/**
 * Author: Nathan Flint
 * Created 10/10/2016
 */

public class MenuInterpreter extends AppCompatActivity implements
        MenuInterpreterViewable,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private Switch videoSwitch, locationSwitch;
    private TextView skypeStatus;
    private TextView skypeName;
    private int userId;
    private MenuInterpreterViewModel viewModel;
    private LocationService locationService;
    private boolean isLocationServiceConnected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_interpreter);

        skypeStatus = (TextView) findViewById(R.id.skypeStatus);

        skypeName = (TextView)findViewById(R.id.skypeName);
        skypeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromptSkypeNameChange();
            }
        });

        videoSwitch = (Switch)findViewById(R.id.videoSwitch);
        videoSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean isVideoAvailable = ((Switch) v).isChecked();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        viewModel.setVideoStatus(isVideoAvailable);
                    }
                });
            }
        });

        locationSwitch = (Switch)findViewById(R.id.locationSwitch);
        locationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean isLocationAvailable = ((Switch) v).isChecked();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        viewModel.setLocationStatus(isLocationAvailable);
                    }
                });
            }
        });

        // Get User Id
        userId = getIntent().getIntExtra("userId", userId);

        //setting the logout button handler
        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        //Skype username help button handler
        Button help = (Button) findViewById(R.id.help);
        assert help != null;
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, new SkypeUsernameHelpFragment());
                fragmentTransaction.commit();
            }
        });

        createViewModel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Interpreter activity", "Destroyed!");

        if(locationService != null)
            locationService.stopLocationUpdates();
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

    private void PromptSkypeNameChange() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Skype Name");

        // Set up the input
        final EditText input = new EditText(this);
        CharSequence name = skypeName.getText() == "Not Set" ? "" : skypeName.getText();
        input.setText(name);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String newName = input.getText().toString();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        viewModel.setSkypeName(newName);
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void createViewModel() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                MenuInterpreter.this.viewModel = new MenuInterpreterViewModel(
                        MenuInterpreter.this,
                        MenuInterpreter.this.userId);
                locationService = new LocationService(getAppContext(),
                        viewModel,
                        MenuInterpreter.this,
                        MenuInterpreter.this);
                onViewModelUpdated();
            }
        });
    }


    @Override
    public void onBackPressed() {
        // do nothing, we want users to use the logout button
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

    /**
     * Terminates this user's session and returns to the login/signup page,
     * starts activity LoginActivity.java using Android Intent.
     */
    private void logout() {
        finish();
        Intent navigationIntent = new Intent(MenuInterpreter.this, LoginActivity.class);
        MenuInterpreter.this.startActivity(navigationIntent);
    }

    @Override
    public void onViewModelUpdated() {
        Context context = this.getApplicationContext();
        Handler mainHandler = new Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                videoSwitch.setChecked(viewModel.isVideoStatusEnabled());
                videoSwitch.setEnabled(viewModel.isSkypeProperlyConfigured());

                boolean isLocationStatusEnabled = viewModel.isLocationStatusEnabled();
                locationSwitch.setChecked(isLocationStatusEnabled);

                if (isLocationStatusEnabled && isLocationServiceConnected)
                    locationService.startLocationUpdates();

                if (!isLocationStatusEnabled && isLocationServiceConnected)
                    locationService.stopLocationUpdates();

                boolean hasSkypeName = viewModel.hasSkypeName();
                String nameText = hasSkypeName ? viewModel.getSkypeName() : "Not Set";
                int nameColor = hasSkypeName ? Color.GREEN : Color.RED;
                skypeName.setText(nameText);
                skypeName.setTextColor(nameColor);

                boolean isSkypeInstalled = viewModel.isSkypeInstalled();
                String installedText = isSkypeInstalled ? "Installed" : "Not Installed";
                int installedColor = isSkypeInstalled ? Color.GREEN : Color.RED;
                skypeStatus.setTextColor(installedColor);
                skypeStatus.setText(installedText);
            }
        };
        mainHandler.post(myRunnable);
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        isLocationServiceConnected = true;
        onViewModelUpdated();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

