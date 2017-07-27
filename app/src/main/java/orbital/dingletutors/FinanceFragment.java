package orbital.dingletutors;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Muruges on 28/7/2017.
 */

public class FinanceFragment extends Fragment {
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.finance_fragment, container, false);
        return v;

    }

    public static FinanceFragment newInstance() {
        FinanceFragment f = new FinanceFragment();
        return f;
    }
}
