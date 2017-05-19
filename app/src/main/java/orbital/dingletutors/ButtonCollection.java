package orbital.dingletutors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by Herald on 16/5/2017.
 */

public class ButtonCollection {

    LinearLayout layout;
    HashMap<String, Button> buttonTable;
    ArrayList<Button> buttons;
    Context context;
    GradientDrawable normal;
    GradientDrawable pressed;

    public ButtonCollection(LinearLayout newLayout, Context newContext) {
        layout = newLayout;
        buttonTable = new HashMap<String, Button>();
        buttons = new ArrayList<Button>();
        context = newContext;

        normal = new GradientDrawable();
        normal.setColor(ContextCompat.getColor(newContext, R.color.lightYellow));
        normal.setCornerRadius(10);
        normal.setStroke(10, ContextCompat.getColor(newContext, R.color.darkYellow));

        pressed = new GradientDrawable();
        pressed.setColor(ContextCompat.getColor(newContext, R.color.darkYellow));
        pressed.setCornerRadius(10);
        pressed.setStroke(10, ContextCompat.getColor(newContext, R.color.darkYellow));
    }

    @SuppressLint("NewApi")
    public Button addButton(final ViewPager pager,
                            String name,
                            final int caseNo) {

        Button.OnClickListener click = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(caseNo, true);
            }
        };

        final Button button = new Button(context);
        button.setGravity(Gravity.CENTER_HORIZONTAL);
        button.setText(name);
        button.setTextSize(16);

        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            button.setBackgroundDrawable(normal);
        } else {
            button.setBackground(normal);
        }

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(caseNo, true);
            }
        });

        button.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            button.setBackgroundDrawable(pressed);
                        } else {
                            button.setBackground(pressed);
                        }
                    }
                    case MotionEvent.ACTION_UP: {
                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            button.setBackgroundDrawable(normal);
                        } else {
                            button.setBackground(normal);
                        }
                    }
                }
                return false;
            }
        });

        buttonTable.put(name, button);
        buttons.add(button);
        return button;
    }

    public Button getButton(String name) {
        return buttonTable.get(name);
    }

    public void clearButtons() {
        buttonTable = new HashMap<String, Button>();
        buttons = new ArrayList<Button>();
    }

    public void finalizeButtons() {
        layout.removeAllViewsInLayout();
        for (Button button : buttons) {
            button.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    layout.getWeightSum() / (float) buttons.size())
            );
            layout.addView(button);
        }
    }
}
