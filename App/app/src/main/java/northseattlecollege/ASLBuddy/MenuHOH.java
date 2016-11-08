package northseattlecollege.ASLBuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Author: Kellan Nealy
 * Created 10/10/2016
 *
 * Main menu for hard-of-hearing users, where they can navigate to the Hearing Tool,
 * Create a video or in-person request for an interpreter, and logout.
 */

public class MenuHOH extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_hoh);

        // Set main action button onClick attributes
        Button hearingToolButton = (Button) findViewById(R.id.hearing_tool_button);
        hearingToolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startHearingTool();
            }
        });
        Button videoRequestButton = (Button) findViewById(R.id.video_interpreter_button);
        videoRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestVideo();
            }
        });
        Button physicalRequestButton = (Button) findViewById(R.id.physical_interpreter_button);
        physicalRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPhysical();
            }
        });
        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    /**
     * Starts new activity HearingTool.java using Android Intent,
     * session information should persist while users are in the Hearing Tool.
     */
    private void startHearingTool() {
        finish();
        Intent navigationIntent = new Intent(MenuHOH.this, HearingTool.class);

        MenuHOH.this.startActivity(navigationIntent);
    }

    /**
     * Starts new activity CreateRequest.javausing Android Intent,
     * where users can specify details about their video request.
     */
    private void requestVideo() {
        finish();
        Intent navigationIntent = new Intent(MenuHOH.this, CreateRequest.class);
        navigationIntent.putExtra(CreateRequest.REQUEST_TYPE, CreateRequest.REQUEST_TYPE_VIDEO);
        MenuHOH.this.startActivity(navigationIntent);
    }

    /**
     * Starts new activity CreateRequest.java using Android Intent,
     * where users can specify details about their physical request.
     */
    private void requestPhysical() {
        finish();
        Intent navigationIntent = new Intent(MenuHOH.this, CreateRequest.class);
        navigationIntent.putExtra(CreateRequest.REQUEST_TYPE, CreateRequest.REQUEST_TYPE_PHYSICAL);
        MenuHOH.this.startActivity(navigationIntent);
    }

    /**
     * Terminates this user's session and returns to the login/signup page,
     * starts activity LoginActivity.java using Android Intent.
     */
    private void logout() {
        finish();
        Intent navigationIntent = new Intent(MenuHOH.this, LoginActivity.class);

        MenuHOH.this.startActivity(navigationIntent);
    }
}
