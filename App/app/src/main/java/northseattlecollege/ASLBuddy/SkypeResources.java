package northseattlecollege.ASLBuddy;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Skype resources including three methods: check if Skype is installed,
 * go to market to install skpye, and initiate a Skype video call to a specific username.
 *
 * These public methods will be called from within other activities in the app in order to perform
 * these functions as necessary.
 *
 * Added 11/2/16 by James Orrell
 */

public class SkypeResources {

    /*
    Call this method to determine if Skype is installed on the client device.
    Return True if installed
     */
    public static boolean isSkypeClientInstalled(Context myContext) {
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
    This method will automatically redirect the user to the Skype page on the google play store
   to install client through market uri scheme

    */
    public static void goToMarket(Context myContext) {
        Uri marketUri = Uri.parse("market://details?id=com.skype.raider");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myContext.startActivity(myIntent);
    }

    /**
     * initiate a Skype URI request which will attempt to switch focus to the Skype application and
     * initiate a call to the specified USERNAME. Username must be correct or call will fail and
     * return to our app activity.
     */
    public static void initiateSkypeCall(Context myContext, String username) {

        // Create the Intent from our Skype URI.
        Uri skypeUri = Uri.parse("skype:" + username + "?call&video=true");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, skypeUri);

        // Restrict the Intent to being handled by the Skype for Android client only.
        myIntent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Initiate the Intent. It should never fail because you've already established the
        // presence of its handler (although there is an extremely minute window where that
        // handler can go away).
        myContext.startActivity(myIntent);
    }

    /**
     * //TODO: connect this function with server api to get username
     * Placeholder method should get username from the server once an interpreter has accepted the
     * request, and this function can be used to pass the username to the SkypeURI for placing
     * the call.
     * @return String username,  the valid Skype name of the interpreter
     */
    public static String getUsernameFromServer()
    {
        String username = "username";
        return username;
    }

}
