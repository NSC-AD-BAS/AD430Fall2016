package northseattlecollege.ASLBuddy;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class that updates the status by querying database using get and post data to update the
 * Interpreter status
 * Created by samueliox on 10/31/2016.
 */

public class InterpreterStatus {
    int userId;

    public InterpreterStatus(int userId) {
        //figuring out who is currently logged in
        //pass in the userid as a parameter of the constructor
        this.userId = userId;
    }

    //send video status to the server
    public void setVideoStatus(boolean videoTrue) {
        //call api here
        final boolean vid = videoTrue;
        try {
            //call API to update the video status here, if true set true else false
            URL url = new URL("http://54.69.18.19:8081/setVideoStatus?userId=" +
                    userId + "&status=" + (vid ? 1 : 0));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //method that sets the video
    public boolean getVideoStatus() {
        ObjectMapper mapper = new ObjectMapper();
        boolean status = false;
        try {
            //call API to update the video status here
            JsonObj obj = mapper.readValue(new URL("http://54.69.18.19/getVideoStatus?userId=" +
                    userId), JsonObj.class);
            //set status here from object
            status = obj.getOk_to_chat();
        } catch (Exception e) {
            System.out.println(e);
        }
        //return the result from the query (video status)
        return status;
    }

    //method that sends the location status to the server
    public void setLocationStatus(boolean locationTrue) {
        //call api here
        final boolean loc = locationTrue;
        try {
            URL url = new URL("http://54.69.18.19/setLocationStatus?userId=" +
                    userId + "&status=" + (loc ? 1 : 0));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //method that sets the location status in the activity
    public boolean getLocationStatus() {
        ObjectMapper mapper = new ObjectMapper();
        boolean status = false;
        try {
            //call API to update the video status here
            JsonObj obj = mapper.readValue(new URL("http://54.69.18.19/getLocationStatus?userId=" +
                    userId), JsonObj.class);
            status = obj.getOk_to_show_location();
        } catch (Exception e) {
            System.out.println(e);
        }
        return status;
    }

    //method that sends the skype user name to the server
    public void setSkypeName(String skypeName) {
        //call api here


        try {
            URL url = new URL("http://54.69.18.19/setSkypeName?userId=" + userId + "&skypeName=" + skypeName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
        } catch (Exception e) {
            System.out.println(e);
        }


    }

    //method that sets the Skype user name
    public String getSkypeName() {
        //call api here


        ObjectMapper mapper = new ObjectMapper();
        String name = "";
        try {
            //call API to update the video status here
            JsonObj[] obj = mapper.readValue(new URL("http://54.69.18.19/getSkypeName?userId=" +
                    userId), JsonObj[].class);
            name = obj[0].getSkype_user_name();

        } catch (Exception e) {
            System.out.println(e);
        }
        return name;


    }

    public void setInterpreterLocation(double lat, double lng) {
        try {
            //call API to update the video status here, if true set true else false
            URL url = new URL("http://54.69.18.19/updateUserLocation?userId=" + userId + "&userLocLat=" +
                    lat + "&userLocLong=+" + lng);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

