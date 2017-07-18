package orbital.dingletutors;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import static android.view.View.VISIBLE;

/**
 * Created by Muruges on 2/7/2017.
 */
@Layout(R.layout.drawer_item)

public class DrawerMenuItem {
    public static final int DRAWER_MENU_ITEM_HOME = 1;
    public static final int DRAWER_MENU_ITEM_LESSON_HISTORY = 2;
    public static final int DRAWER_MENU_ITEM_NOTIFICATIONS = 3;
    public static final int DRAWER_MENU_ITEM_STUDENTS = 4;
    public static final int DRAWER_MENU_ITEM_FINANCES = 5;

    private int mMenuPosition;
    private Context mContext;
    private static IDrawerCallBack mCallBack;

    @View(R.id.itemNameTxt)
    private TextView itemNameTxt;

    @View(R.id.itemIcon)
    private ImageView itemIcon;

    @View(R.id.notification_count)
    public TextView notificationCount;

    public DrawerMenuItem(Context context, int menuPosition) {
        mContext = context;
        mMenuPosition = menuPosition;
    }

    @Resolve
    private void onResolved() {
        switch (mMenuPosition) {
            case DRAWER_MENU_ITEM_HOME:
                itemNameTxt.setText("Home");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_home_black_20dp));
                break;
            case DRAWER_MENU_ITEM_LESSON_HISTORY:
                itemNameTxt.setText("Lesson History");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_assignment_black_20dp));
                break;
            case DRAWER_MENU_ITEM_NOTIFICATIONS:
                Log.v("notificationCount", "set up");
                MainActivity.notificationCount = notificationCount; // passing over to MainActivity
                itemNameTxt.setText("Notifications");
                notificationCount.setVisibility(VISIBLE);
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_priority_high_black_20dp));
                break;
            case DRAWER_MENU_ITEM_STUDENTS:
                itemNameTxt.setText("Students");
                break;
            case DRAWER_MENU_ITEM_FINANCES:
                itemNameTxt.setText("Finances");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_attach_money_black_24dp));
                break;
        }
    }

    @Click(R.id.mainView)
    private void onMenuItemClick(){
        switch (mMenuPosition) {
            case DRAWER_MENU_ITEM_HOME:
                Toast.makeText(mContext, "Home", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onHomeMenuSelected();
                break;
            case DRAWER_MENU_ITEM_LESSON_HISTORY:
                Toast.makeText(mContext, "Lesson History", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onLessonHistoryMenuSelected();
                break;
            case DRAWER_MENU_ITEM_NOTIFICATIONS:
                Toast.makeText(mContext, "Notifications", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onNotificationsSelected();
                break;
            case DRAWER_MENU_ITEM_STUDENTS:
                Toast.makeText(mContext, "Students", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onStudentsSelected();
                break;
            case DRAWER_MENU_ITEM_FINANCES:
                Toast.makeText(mContext, "Finances", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onFinancesSelected();
                break;
            default:
                mCallBack.onHomeMenuSelected();
                break;
        }
    }

    public static void setDrawerCallBack(IDrawerCallBack callBack) {
        mCallBack = callBack;
    }

    public interface IDrawerCallBack{
        void onHomeMenuSelected();
        void onLessonHistoryMenuSelected();
        void onNotificationsSelected();
        void onStudentsSelected();
        void onFinancesSelected();
    }
}
