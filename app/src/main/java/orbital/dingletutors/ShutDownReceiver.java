package orbital.dingletutors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Herald on 9/6/2017.
 */

public class ShutDownReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Just to save the MinuteQueue
        try {
            MinuteUpdater.saveMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
