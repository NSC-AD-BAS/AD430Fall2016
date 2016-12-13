package northseattlecollege.ASLBuddy;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Author: Brandon Lorenz
 * Created 10/10/2016
 */

public class RequestPending extends AppCompatActivity {

    //initialize useful variables for holding and iterating through available interpreters
    JSONArray userArray;
    int position;

    public final static String REQUEST_TYPE = "northseattlecollege.ASLBuddy.REQUEST_TYPE";
    public final static String REQUEST_TYPE_VIDEO = "northseattlecollege.ASLBuddy.REQUEST_TYPE_VIDEO";
    public final static String REQUEST_TYPE_PHYSICAL = "northseattlecollege.ASLBuddy.REQUEST_TYPE_PHYSICAL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_pending);

        userArray = null;
        position = 0;


        // ToDo: remove back button once system back button is working
        // Back button for easy navigation
        Button backButton = (Button) findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent navigationIntent = new Intent(RequestPending.this, MenuHOH.class);
                RequestPending.this.startActivity(navigationIntent);
            }
        });


        Intent intent = getIntent();
        final String requestType = intent.getStringExtra(REQUEST_TYPE);
        //when the page is created, check if it is a pending physical or video request and
        //initialize the appropriate UI
        if (requestType.compareTo(REQUEST_TYPE_VIDEO) == 0){
            setupVideoRequest();

        }
        else if(requestType.compareTo(REQUEST_TYPE_PHYSICAL)==0){
            setupPhysicalRequest();
            System.out.println("This is a physical request");

        }
        else {
            CreateRequest.setError(true);
            finish();
            Intent navigationIntent = new Intent(this, CreateRequest.class);
            navigationIntent.putExtra(CreateRequest.REQUEST_TYPE, requestType);
            startActivity(navigationIntent);
        }

    }

    //call this method to set up UI for pending video request
    private void setupVideoRequest(){

        //set up the UI
        final TextView response = (TextView) findViewById(R.id.label_request_pending);
        Button call = (Button) findViewById(R.id.label_finish_request);
        final Button skip = (Button) findViewById(R.id.label_skip_user);
        TextView interpreterFound = (TextView)findViewById(R.id.label_interpreter_found);
        //make buttons visible


        //can't skip until there are users in the array
        skip.setClickable(false);



        //TODO: Make this button set current user ok_to_chat to false before switching to the new user
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //make sure we haven't reached the end of available users

                if (position < userArray.length()) {
                    position++;
                    try {
                        JSONObject skypeName = userArray.getJSONObject(position);
                        response.setText(skypeName.get("skype_username").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("Invalid Data from Server");
                        skip.setClickable(false);
                    }
                } else {
                    //get a new list of users
                    skip.setClickable(false);
                    finish();
                    startActivity(getIntent());
                }
            }
        });


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initiate a call by grabbing the username from the TextView after it is updated
                //this should only be available once the AsyncTask has completed and made the button visible
                SkypeResources.initiateSkypeCall(getApplicationContext(), response.getText().toString());

            }
        });
        //create a new request to contact the server and get the username of the interpreter

        ServerRequestTask usernameGet = new ServerRequestTask("http://54.69.18.19/getVideoInterpreters?userId=1");


    }

    /**
     * Setup requesting a physical interpreter
     *
     */
    private void setupPhysicalRequest(){
        //set up the UI
        final TextView listInterpreters = (TextView) findViewById(R.id.label_list_interpreters);
        Button notify = (Button) findViewById(R.id.label_finish_request);
        final Button skip = (Button) findViewById(R.id.label_skip_user);
        TextView interpreterFound = (TextView)findViewById(R.id.label_physical_interpreters_found);

        Bundle bundle = getIntent().getExtras();
        double radius = bundle.getDouble(CreateRequest.REQUEST_RADIUS);

        // Do not allow notification until we have an interpreter available
        skip.setClickable(false);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //make sure we haven't reached the end of available users

                if (position < userArray.length()) {
                    position++;
                    try {
                        JSONObject interpreterName = userArray.getJSONObject(position);
                        listInterpreters.setText(interpreterName.get("full_name").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("Invalid Data from Server");
                        skip.setClickable(false);
                    }
                } else {
                    //get a new list of users
                    skip.setClickable(false);
                    finish();
                    startActivity(getIntent());
                }
            }
        });

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                CharSequence text = "Interpreter Notified!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        });

        // Build the URL for the physical interpreters
        StringBuilder url = new StringBuilder();

        // TO-DO Get lat and long values from last known location
        double lat = 47.6062;
        double lon = -122.3321;
        int userId = 1;
        radius = 2.0; //temporarily hardcoded until value can be pulled from the slider

        url.append("http://54.69.18.19/getphysicalinterpreters?userId="+userId);
        if(lat > 0 || lat < 0 && lon > 0 || lon < 0) {
            url.append("&userLat="+lat+"&userLong="+lon);
        }
        url.append("&radius="+radius);

        // Send custom configured URL to ServerRequestTask
        ServerPhysicalRequestTask interpreterGet = new ServerPhysicalRequestTask(url.toString());
    }


    //need to define internet permission
    //user need to know that my application can use permission
    class ServerRequestTask extends AsyncTask<String, String, String> {


        //this should allow us to use this generic AsyncTask in multiple activities

        String urlString;
        String responseUsername;
        private Exception exception;

        protected ServerRequestTask(String urlString){
            //set the desired URL
            this.urlString = urlString;
            super.execute();
        }
        //TODO: use publishProgress() and onProgressUpdate() to provide helpful feedback to user
        @Override
        protected String doInBackground(String... params) {

            //initialize URLconnection object and JSON result object and BufferedReader
            HttpURLConnection connection = null;
            //JSONObject resultObject = null;
            BufferedReader reader = null;

            StringBuffer buffer = new StringBuffer();
            //try to make the connection to the provided URL
            try{
                //get the URL
                URL url = new URL(urlString);
                //open the connection
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                //create InputSteam from the connection
                InputStream stream = connection.getInputStream();
                //assign reader to stream
                reader = new BufferedReader(new InputStreamReader(stream));
                //StringBuffer to append results to

                //read inputstream line by line and append to buffer
                String line = "";
                while((line = reader.readLine()) !=null){
                    buffer.append(line);
                }

                // Old JSONArray line
                // JSONArray username = new JSONArray(buffer.toString());

                JSONArray username = new JSONArray();
                userArray = username;
                //for debugging
                System.out.println(username.toString());
                System.out.println(username.length());

                JSONObject skypeName = username.getJSONObject(position);
                return skypeName.get("skype_username").toString();

                //convert the buffered string to a JSON object
                //this may throw a JSONException if the input is bad
                //resultObject = new JSONObject(buffer.toString());

                //catch exceptions, errors and close connection
            } catch(MalformedURLException e){
                e.printStackTrace();
                System.out.println("Bad URL");
            } catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
                System.out.println("Invalid Data from Server");
            } finally{
                if(connection!=null) {
                    connection.disconnect();
                }
                try{
                    if(reader !=null){
                        reader.close();
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }


            return "No interpreters found please try again.";
        }

        //override this method again when you call the AsyncTask in another activity
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            TextView response = (TextView) findViewById(R.id.label_request_pending);
            TextView intFound = (TextView)findViewById(R.id.label_interpreter_found);
            response.setText(result);
            //make the call button appear - this step could be removed but is helpful for testing
            Button call = (Button)findViewById(R.id.label_finish_request);
            call.setVisibility(View.VISIBLE);
            Button skip = (Button)findViewById(R.id.label_skip_user);
            skip.setVisibility(View.VISIBLE);
            if(userArray.length() >0 ) {
                skip.setClickable(true);
            }
            intFound.setVisibility(View.VISIBLE);
            response.setVisibility(View.VISIBLE);


        }
    }

    //need to define internet permission
    //user need to know that my application can use permission
    class ServerPhysicalRequestTask extends AsyncTask<String, String, String> {


        //this should allow us to use this generic AsyncTask in multiple activities

        String urlString;
        String responseUsername;
        private Exception exception;

        protected ServerPhysicalRequestTask(String urlString){
            //set the desired URL
            this.urlString = urlString;
            super.execute();
        }
        //TODO: use publishProgress() and onProgressUpdate() to provide helpful feedback to user
        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            StringBuffer finalBufferedData = new StringBuffer();

            try{
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                //be able to read the data line by line by using the stream from the connection
                //set above
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";

                //this will grab the complete JSONData
                while((line = reader.readLine()) !=null){
                    buffer.append(line);
                }
                //return buffer.toString();

                //storing the final JSON into variable
                String finalJSON = buffer.toString();

                //enter the array name of the JSON object
                JSONArray parentArray = new JSONArray(finalJSON);
                userArray = parentArray;
                //need to grab all the object in the JSON Array - parent Array

                for(int i = 0; i<parentArray.length(); i++){

                    JSONObject finalObject = parentArray.getJSONObject(i);

                    //pertaining to key of JSON data to grab the value.
                    String fullName = finalObject.getString("full_name");
                    Double distance = finalObject.getDouble("distance");
//                    Double lastLat;
//                    if(finalObject.get("last_known_location_lat").equals(null)) {
//                        lastLat = 0.0;
//                    } else {
//                        lastLat = finalObject.getDouble("last_known_location_lat");
//                    }
//                    Double lastLong;
//                    if(finalObject.get("last_known_location_long").equals(null)) {
//                        lastLong = 0.0;
//                    } else {
//                        lastLong = finalObject.getDouble("last_known_location_long");
//                    }
                    finalBufferedData.append("Name: " + fullName + "\n" + "Distance: " + String.format("%.2f", distance) + " miles.\n\n");
                }

                return finalBufferedData.toString();

                //catch clause for multiple exceptions
            } catch(MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            } finally{
                if(connection!=null) {
                    connection.disconnect();
                }
                try{
                    if(reader !=null){
                        reader.close();
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }

            }

            return "No interpreters found please try again.";
        }

        //override this method again when you call the AsyncTask in another activity
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            TextView response = (TextView) findViewById(R.id.label_request_pending);
            TextView intFound = (TextView)findViewById(R.id.label_interpreter_found);
            response.setText(result);
            //make the call button appear - this step could be removed but is helpful for testing
            Button call = (Button)findViewById(R.id.label_finish_request);
            call.setVisibility(View.VISIBLE);
            Button skip = (Button)findViewById(R.id.label_skip_user);
            skip.setVisibility(View.VISIBLE);
            if(userArray.length() >0 ) {
                skip.setClickable(true);
            }
            intFound.setVisibility(View.VISIBLE);
            response.setVisibility(View.VISIBLE);


        }
    }
}

