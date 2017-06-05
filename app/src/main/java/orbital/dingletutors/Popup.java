package orbital.dingletutors;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Herald on 27/5/2017.
 *
 * Note: when putting custom objects into bundles, they must implement Parcelable interface
 * Bundle.putParcelable/Bundle.getParcelable
 * currently only using getCharSequence
 */

public class Popup<Key> {

    TreeMap<Key, Bundle> map;
    String title;
    int listMember;

    View popupView;
    PopupWindow popupWindow;

    Context context;
    View anchorView;
    int[] location;
    boolean initialized;

    Popup(Context context, View anchorView, TreeMap<Key, Bundle> map,
          String title, int listMember, View.OnClickListener buttonListener) {
        this.map = map;
        this.title = title;
        this.listMember = listMember;
        this.context = context;
        this.anchorView = anchorView;
        
        // use container as a viewgroup or null
        //View popupView = getActivity().getLayoutInflater().inflate(R.layout.popup, null);
        popupView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.linear_scrollable, null);
        popupView.setBackgroundColor(ContextCompat.getColor(context, R.color.popup));
        popupView.findViewById(R.id.add_button).setOnClickListener(buttonListener);

        popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // popupWindow.setContentView(popupView);
        TextView tv = (TextView) popupView.findViewById(R.id.title);
        tv.setText(title);

        popupWindow.setFocusable(true);
        ColorDrawable background = new ColorDrawable();
        background.setColor(ContextCompat.getColor(context, R.color.popup));
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        location = new int[2];
        anchorView.getLocationOnScreen(location);
        initialized = true;
        Log.v("Popup", "created");
    }

    public void updateList() {
        if (!initialized) {
            Log.v("Popup", "Not initialized");
            return;
        }
        // the inefficient method: clear and put lessons again
        // assuming there's not many lessons on 1 day
        ListView list = (ListView) popupView;
        list.removeAllViewsInLayout();
        for (Map.Entry<Key, Bundle> pair : map.entrySet()) {
            Bundle b = pair.getValue();
            RelativeLayout layout = (RelativeLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(listMember, null);
            // use set tag and get tag to attach objects to a view

            for (int i = 0; i < layout.getChildCount(); i++) {
                View v = layout.getChildAt(i);
                if (v instanceof TextView) {
                    // currently we only use textviews to display information
                    TextView t = (TextView) v;
                    CharSequence cs = b.getCharSequence(t.getText().toString());
                    if (cs != null) {
                        t.setText(cs);
                    } else {
                        // no value associated means use default
                    }
                }
            }
            list.addView(layout);
        }
        Log.v("Popup", "updated");
    }

    // can access treemap directly to add/remove elements so not bothered

    public void showPopup() {
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                location[0], location[1]);
        Log.v("Popup", "shown");
    }
}
