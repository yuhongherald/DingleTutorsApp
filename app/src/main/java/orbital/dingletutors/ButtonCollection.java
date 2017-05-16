package orbital.dingletutors;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Herald on 16/5/2017.
 */

public class ButtonCollection {

    public static LinearLayout finalizeButtons(LinearLayout layout, Button[] buttons) {
        layout.removeAllViewsInLayout();
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 1f / (float) buttons.length
            ));
            layout.addView(buttons[i]);
        }
        return layout;
    }

    public static Button createButton(Context context,
                                      final int id,
                                      final FragmentManager mgr,
                                      final BaseActivity newActivity) {

        Button.OnClickListener click = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction t = mgr.beginTransaction();

                t.replace(id, newActivity.getFragment());
                t.commit();
            }
        };

        Button button = new Button(context);
        button.setOnClickListener(click);
        button.setOnLongClickListener((Button.OnLongClickListener) click);
        return button;
    }
}
