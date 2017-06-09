package orbital.dingletutors;

import android.content.Context;
import android.view.View;

/**
 * Created by user on 9/6/2017.
 */

public class PopupCheckIn extends Popup {
    public static Lesson checkedInLesson;

    PopupCheckIn(Context context, View anchor, int width, int x, int y) {
        super(context, anchor, width, x, y);
    }

    @Override
    public void updateList() {

    }
}
