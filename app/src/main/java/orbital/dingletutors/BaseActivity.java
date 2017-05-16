package orbital.dingletutors;

/**
 * Created by Herald on 16/5/2017.
 */

public interface BaseActivity {
    // constructor takes a context

    // used before switching away from activity
    public void save();

    // used before switching to activity
    public void load();

    public android.support.v4.app.Fragment getFragment();

}
