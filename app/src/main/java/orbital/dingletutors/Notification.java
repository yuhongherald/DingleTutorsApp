package orbital.dingletutors;

import java.util.Date;

/**
 * Created by user on 9/6/2017.
 */

public class Notification {
    public boolean read;
    public Date lastUpdated;
    public String message;
    public int switchCase;

    Notification(Date lastUpdated, String message, int switchCase) {
        this.read = false;
        this.lastUpdated = lastUpdated;
        this.message = message;
        this.switchCase = switchCase;
    }

    public void switches() {
        // to be placed in the onclicklistener
        switch(switchCase) {
            case 0:
                return;
            default:
                return;
        }
    }
}
