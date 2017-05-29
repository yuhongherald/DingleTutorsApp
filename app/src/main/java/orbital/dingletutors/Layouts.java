package orbital.dingletutors;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

/**
 * Created by Herald on 27/5/2017. Not used anymore.
 */

public class Layouts {
    public static RelativeLayout viewLesson(Context context) {
        RelativeLayout layout = (RelativeLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.view_lesson, null);
        return layout;
    }
}
