package northseattlecollege.ASLBuddy;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.location.LocationListener;

/**
 * Created by nathanflint on 12/11/16.
 */

public class MenuInterpreterViewModel implements LocationListener {
    private MenuInterpreterViewable viewable;
    private InterpreterStatus status;
    private String skypeName;
    private boolean isVideoStatusEnabled;
    private boolean isLocationStatusEnabled;

    public MenuInterpreterViewModel(MenuInterpreterViewable viewable, int userId) {
        this.viewable = viewable;
        status = new InterpreterStatus(userId);

        skypeName = status.getSkypeName();
        if (!isSkypeProperlyConfigured())
            status.setVideoStatus(false);

        isLocationStatusEnabled = status.getLocationStatus();
        isVideoStatusEnabled = status.getVideoStatus();
    }

    public void setSkypeName(String name) {
        status.setSkypeName(name);
        skypeName = name;
        if (!isSkypeProperlyConfigured()) {
            setVideoStatus(false);
            return;
        }
        viewable.onViewModelUpdated();
    }

    public String getSkypeName() {
        return skypeName;
    }

    public boolean hasSkypeName() {
        return skypeName != null && !skypeName.isEmpty();
    }

    public void setVideoStatus(boolean isVideoEnabled) {
        status.setVideoStatus(isVideoEnabled);
        isVideoStatusEnabled = isVideoEnabled;
        viewable.onViewModelUpdated();
    }

    public boolean isVideoStatusEnabled() {
        return isVideoStatusEnabled;
    }

    public boolean isSkypeProperlyConfigured() {
        return hasSkypeName() && isSkypeInstalled();
    }

    public void setLocationStatus(boolean isLocationEnabled) {
        status.setLocationStatus(isLocationEnabled);
        isLocationStatusEnabled = isLocationEnabled;
        viewable.onViewModelUpdated();
    }

    public boolean isLocationStatusEnabled() {
        return isLocationStatusEnabled;
    }

    public boolean isSkypeInstalled() {
        return SkypeResources.isSkypeClientInstalled(viewable.getAppContext());
    }

    @Override
    public void onLocationChanged(Location location) {
        final Location coordinates = location;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Log.i("LocationUpdate", "Updated location!");
                status.setInterpreterLocation(coordinates.getLatitude(), coordinates.getLongitude());
            }
        });
    }
}
