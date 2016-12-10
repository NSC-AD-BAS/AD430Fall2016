package northseattlecollege.ASLBuddy;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
        implements SetRadiusFragment.OnSetRadiusFragmentInteractionListener {

    public final static String REQUEST_TYPE = "northseattlecollege.ASLBuddy.REQUEST_TYPE";
    public final static String REQUEST_TYPE_VIDEO = "northseattlecollege.ASLBuddy.REQUEST_TYPE_VIDEO";
    public final static String REQUEST_TYPE_PHYSICAL = "northseattlecollege.ASLBuddy.REQUEST_TYPE_PHYSICAL";

    public final static String REQUEST_RADIUS = "northseattlecollege.ASLBuddy.REQUEST_RADIUS";

    private String requestType;

    private SetRadiusFragment setRadiusFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        Intent intent = getIntent();
        requestType = intent.getStringExtra(REQUEST_TYPE);

        InitializeUI();

        Button submitButton = (Button) findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitRequest();
            }
        });

        // ToDo: remove back button once system back button is working
        // Back button for easy navigation
        Button backButton = (Button) findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent navigationIntent = new Intent(CreateRequest.this, MenuHOH.class);
                CreateRequest.this.startActivity(navigationIntent);
            }
        });

        final Spinner requestTypeSpinner = (Spinner) findViewById(R.id.spinner_request_type);
        requestTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ((TextView) view).getText().toString();
                UpdateOtherEditView(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupVideoRequest() {
        TextView title = (TextView) findViewById(R.id.label_create_request);
        title.setText(R.string.label_create_video_request);
        if (!SkypeResources.isSkypeClientInstalled(getApplicationContext())) {
            LinearLayout requestLayout = (LinearLayout) findViewById(R.id.layout_create_request);
            requestLayout.setVisibility(View.GONE);
            LinearLayout installSkypeLayout = (LinearLayout) findViewById(R.id.layout_install_skype);
            installSkypeLayout.setVisibility(View.VISIBLE);

            Button installSkype = (Button) findViewById(R.id.button_install_skype);
            installSkype.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SkypeResources.goToMarket(getApplicationContext());
                }
            });

            Button cancel = (Button) findViewById(R.id.button_cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Intent navigationIntent = new Intent(CreateRequest.this, MenuHOH.class);
                    CreateRequest.this.startActivity(navigationIntent);
                }
            });
        }
    }

    private void SubmitRequest() {
        Intent navigationIntent = new Intent(CreateRequest.this, RequestPending.class);

        if (requestType.compareTo(REQUEST_TYPE_PHYSICAL) == 0) {
            // ToDo: how are physical requests handled?
            navigationIntent.putExtra(CreateRequest.REQUEST_TYPE, CreateRequest.REQUEST_TYPE_PHYSICAL);
            navigationIntent.putExtra(CreateRequest.REQUEST_RADIUS, setRadiusFragment.getRadius());
        } else {
            navigationIntent.putExtra(CreateRequest.REQUEST_TYPE, CreateRequest.REQUEST_TYPE_VIDEO);
        }
        finish();
        CreateRequest.this.startActivity(navigationIntent);
    }

    private void InitializeUI() {
        if (requestType.compareTo(REQUEST_TYPE_PHYSICAL) == 0) {
            setupPhysicalRequest();
        } else {
            setupVideoRequest();
        }
    }

    private void setupPhysicalRequest() {
        TextView title = (TextView) findViewById(R.id.label_create_request);
        title.setText(R.string.label_create_physical_request);
        // ToDo: Get default/saved radius from Settings
        setRadiusFragment = SetRadiusFragment.newInstance(.25f);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.set_radius_fragment_container, setRadiusFragment);
        transaction.commit();
    }

    private void UpdateOtherEditView(String selectedItem) {
        EditText otherDescEditText = (EditText) findViewById(R.id.edit_description);
        // ToDo: Replace hard-coded other with value from strings.xml
        if (selectedItem.compareTo("Other") == 0) {
            otherDescEditText.setEnabled(true);
        } else {
            otherDescEditText.setEnabled(false);
        }
    }

    @Override
    public void onFragmentInteraction(float radius) {

    }

    
    public String callString(String user1, String user2) {
    	return "skype:" + user1 + ";" + user2 + "?call&video=true";
    }
}
