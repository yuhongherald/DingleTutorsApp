package orbital.dingletutors;

import android.widget.ImageView;
import android.widget.TextView;

import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

/**
 * Created by Muruges on 2/7/2017.
 */


@NonReusable
@Layout(R.layout.drawer_header)
public class DrawerHeader {

//    @View(R.id.profileImageView)
//    private ImageView profileImage;

    @View(R.id.nameTxt)
    private TextView nameTxt;

//    @View(R.id.emailTxt)
//    private TextView emailTxt;

    @Resolve
    private void onResolved() {
        nameTxt.setText("Navigation Drawer");
//        emailTxt.setText("teachers_email@gmail.com");
    }
}
