package northseattlecollege.ASLBuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import northseattlecollege.ASLBuddy.fragments.CreateRequestFragment;
import northseattlecollege.ASLBuddy.fragments.InstallSkypeFragment;
import northseattlecollege.ASLBuddy.fragments.RequestErrorFragment;
import northseattlecollege.ASLBuddy.fragments.SetRadiusFragment;

/**
 * Author: Jesse Bernoudy
 * Created 10/10/2016
 * <p>
 * NOTE: Will use the same XML layout as the ViewRequest activity, but
 * instead display the text boxes as "Edit Texts" using the Java Android library
 * <p>
 * Starts new video interpreter request through Skype.
 */

public class CreateRequest extends AppCompatActivity
        implements CreateRequestFragment.OnCreateRequestFragmentInteractionListener,
        InstallSkypeFragment.OnInstallSkypeFragmentInteractionListener,
        RequestErrorFragment.OnRequestErrorFragmentInteractionListener,
        SetRadiusFragment.OnSetRadiusFragmentInteractionListener {

    public final static String REQUEST_TYPE = "northseattlecollege.ASLBuddy.REQUEST_TYPE";
    public final static String REQUEST_TYPE_VIDEO = "northseattlecollege.ASLBuddy.REQUEST_TYPE_VIDEO";
    public final static String REQUEST_TYPE_PHYSICAL = "northseattlecollege.ASLBuddy.REQUEST_TYPE_PHYSICAL";
    public final static String REQUEST_TYPE_HEARING_TOOL = "northseattlecollege.ASLBuddy.REQUEST_TYPE_HEARING_TOOL";

    public final static String REQUEST_RADIUS = "northseattlecollege.ASLBuddy.REQUEST_RADIUS";

    /**
     * WHAT DOES THIS DO?
     * @param error
     */
    public static void setError(boolean error) {
        mIsError = error;
    }

    private static boolean mIsError = false;

    private String mRequestType;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        Intent intent = getIntent();
        mRequestType = intent.getStringExtra(REQUEST_TYPE);
        userId = getIntent().getIntExtra("userId", userId);

        initialize();

        Button cancelButton = (Button) findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsError = false;
                finish();
                Intent navigationIntent = new Intent(CreateRequest.this, MenuHOH.class);
                CreateRequest.this.startActivity(navigationIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
    }

    private void initialize() {

        if (mRequestType.compareTo(REQUEST_TYPE_VIDEO) == 0) {
            TextView title = (TextView) findViewById(R.id.label_create_request);
            title.setText(R.string.label_create_video_request);
        } else {
            TextView title = (TextView) findViewById(R.id.label_create_request);
            title.setText(R.string.label_create_physical_request);
        }

        if (mIsError) {
            Fragment requestErrorFragment = RequestErrorFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.create_request_fragment_container, requestErrorFragment);
            transaction.commit();
        } else if (mRequestType.compareTo(REQUEST_TYPE_VIDEO) == 0 && !SkypeResources.isSkypeClientInstalled(getApplicationContext())) {
            Fragment installSkypeFragment = InstallSkypeFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.create_request_fragment_container, installSkypeFragment);
            transaction.commit();
        } else {
            Fragment createRequestFragment = CreateRequestFragment.newInstance(mRequestType);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.create_request_fragment_container, createRequestFragment);
            transaction.commit();
        }
    }

    private void submitRequest(float radius) {

        Intent navigationIntent = new Intent(CreateRequest.this, RequestPending.class);

        if (mRequestType.compareTo(REQUEST_TYPE_PHYSICAL) == 0) {
            // ToDo: how are physical requests handled?
            navigationIntent.putExtra(REQUEST_TYPE, CreateRequest.REQUEST_TYPE_PHYSICAL);
            navigationIntent.putExtra(REQUEST_RADIUS, radius);
        } else {
            navigationIntent.putExtra(CreateRequest.REQUEST_TYPE, CreateRequest.REQUEST_TYPE_VIDEO);
        }

        finish();
        CreateRequest.this.startActivity(navigationIntent);
    }

    @Override
    public void onRequestErrorFragmentInteraction(String requestType) {
        mIsError = false;
        if (requestType.compareTo(REQUEST_TYPE_HEARING_TOOL) == 0) {
            finish();
            Intent navigationIntent = new Intent(this, HearingTool.class);
            navigationIntent.putExtra(CreateRequest.REQUEST_TYPE, CreateRequest.REQUEST_TYPE_VIDEO);
            startActivity(navigationIntent);
        } else {
            mRequestType = requestType;
            initialize();
        }
    }

    @Override
    public void onCreateRequestFragmentInteraction(float radius) {
        submitRequest(radius);
    }

    @Override
    public void onInstallSkypeFragmentInteraction() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_redirect_message)
                .setTitle(R.string.dialog_title_warning);

        // Add the buttons
        builder.setPositiveButton(R.string.label_continue, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SkypeResources.goToMarket(getApplicationContext());
            }
        });
        builder.setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();

    }

    @Override
    public void onSetRadiusFragmentInteraction(float radius) {

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
                Intent navigationIntent = new Intent(CreateRequest.this, Settings.class);
                // pass userId to the following activity
                navigationIntent.putExtra("userId", userId);
                CreateRequest.this.startActivity(navigationIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
