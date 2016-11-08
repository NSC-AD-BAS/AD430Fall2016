package northseattlecollege.ASLBuddy;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

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

    private String requestType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        Button submitButton = (Button) findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent navigationIntent = new Intent(CreateRequest.this, RequestPending.class);
                CreateRequest.this.startActivity(navigationIntent);
            }
        });
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
                String test = ((TextView) view).getText().toString();
                EditText otherDesc = (EditText) findViewById(R.id.edit_description);
                // ToDo: Replace hard-coded other with value from strings.xml
                if (test.compareTo("Other") == 0) {
                    otherDesc.setEnabled(true);
                } else {
                    otherDesc.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // ToDo: Determine if this is a Video or Physical request
        Intent intent = getIntent();
        requestType = intent.getStringExtra(REQUEST_TYPE);

        if (requestType.compareTo(REQUEST_TYPE_PHYSICAL) == 0) {
            // ToDo: Get default/saved radius from Settings
            SetRadiusFragment newFragment = SetRadiusFragment.newInstance(.25f);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.set_radius_fragment_container, newFragment);
            transaction.commit();
        }
    }

    @Override
    public void onFragmentInteraction(float radius) {

    }
}
