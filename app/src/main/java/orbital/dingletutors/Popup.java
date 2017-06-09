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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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

public abstract class Popup {

    Context context;
    View anchor;
    View popupView;
    PopupWindow popupWindow;
    int width;
    int x;
    int y;

    Popup(Context context, View anchor, int width, int x, int y) {
        this.context = context;
        this.anchor = anchor;
        this.width = width;
        this.x = x;
        this.y = y;
        popupView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.popup, null);
        popupView.setBackgroundColor(ContextCompat.getColor(context, R.color.popup));

        popupWindow = new PopupWindow(popupView,
                width,
                ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setTouchable(true);
//        popupWindow.setFocusable(true);
//        ColorDrawable background = new ColorDrawable();
//        background.setAlpha(0xFF);
//        popupWindow.setBackgroundDrawable(new ColorDrawable());

    }
    public abstract void updateList();
    public void show() {
        popupWindow.showAsDropDown(anchor, x, y);
    }
    public boolean isVisible() {
        return popupWindow.isShowing();
    }
    public void hide() {
        popupWindow.dismiss();
    }
}
