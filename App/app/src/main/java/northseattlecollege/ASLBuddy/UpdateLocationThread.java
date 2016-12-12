package northseattlecollege.ASLBuddy;

import android.content.Context;

/**
 * Method that calls the script to update the current location of the interpreter
 * to the server database, to be called in the activity when location services
 * are set to available by interpreter
 * @author Samuel No
 */

public class UpdateLocationThread extends Thread{
    private boolean locationStatusOn;
    private Context menuInterpreter;

    public UpdateLocationThread(boolean locationStatus, Context menu){
        this.locationStatusOn = locationStatus;
        this.menuInterpreter = menu;
    }

    public void setLocationStatusOn(boolean locationStatusOn){
        this.locationStatusOn = locationStatusOn;
    }

    @Override
    public void run(){
        while (locationStatusOn) {
            try {
                //code here for the actual database update
                ((MenuInterpreter)menuInterpreter).sendLocationToServer();
                Thread.sleep(360 * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Ends the current thread
     */
    public void kill() {
        locationStatusOn = false;
    }
}
