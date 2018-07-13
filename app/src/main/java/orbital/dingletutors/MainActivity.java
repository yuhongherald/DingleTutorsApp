package orbital.dingletutors;

import android.Manifest;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Telephony;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mindorks.placeholderview.PlaceHolderView;

import java.io.File;
import java.util.HashMap;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    public static boolean active = false;
    public static final String intent = "MainActivity";

    private Popup popup;
    public static TextView notificationCount;
    public static TextView oldNotificationCount;

    public String[] categoryTitles = new String[]{"Home", "Lesson History", "Notifications", "Students", "Finances"};
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private DrawerCallBack mCallBack;
    private String tutorNumber;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // do nothing here defined later in createVerification
        }
    };

    // hash table of tutor phone number as key and tutor name as val
    // will use a proper verification after trial period
    private static HashMap<String, String> tutorPhoneNumbers;
    static {
        tutorPhoneNumbers = new HashMap<>(20, 1);
        tutorPhoneNumbers.put("92342198", "Winnie Chan");
        tutorPhoneNumbers.put("81833345", "Poh Wei Ming");
        tutorPhoneNumbers.put("85335202", "Sandra Tan");
        tutorPhoneNumbers.put("90610061", "Celine Chian");
        tutorPhoneNumbers.put("96476491", "Randall Wee");
        tutorPhoneNumbers.put("96538226", "Michelle Tan");
        tutorPhoneNumbers.put("98630227", "Tan Zhi Hao");
        tutorPhoneNumbers.put("91786869", "Pinkett Teo");
        tutorPhoneNumbers.put("81494675", "Krisna Dwipayan");
        tutorPhoneNumbers.put("86855725", "Ng Kai Yan");
        tutorPhoneNumbers.put("81815627", "K Muruges");
        tutorPhoneNumbers.put("97481738", "Yu Hong Herald");
        tutorPhoneNumbers.put("97881548", "Gao Fei");

    }

    @Override
    protected void onResume() {
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        mNotifyMgr.cancel(MinuteUpdater.lessonCode);

//        IntentFilter filter = new IntentFilter();
//        filter.addAction("orbital.dingletutors.UPDATE_MAIN");
//        registerReceiver(receiver, filter);
        super.onResume();
        MinuteUpdater.mainAppRunning = true;
    }

    @Override
    protected void onPause() {
//        unregisterReceiver(receiver);
        super.onPause();
        MinuteUpdater.mainAppRunning = false;
    }

    @Override
    public void onStop() {
        try {
            MinuteUpdater.mainAppRunning = false;
            MinuteUpdater.recurringLessonMap.save();
            MinuteUpdater.lessonPresetMap.save();
            MinuteUpdater.studentPresetMap.save();
            MinuteUpdater.saveMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
        active = false;
    }

    protected void createVerification(){
        setContentView(R.layout.verification_fragment);
        final TextInputEditText number = (TextInputEditText) findViewById(R.id.number);
        final TextInputLayout numberWrapper = ((TextInputLayout) findViewById(R.id.numberWrapper));
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().toString().trim().length() == 0){
                    numberWrapper.setError("This field cannot be empty");
                    return;
                } else if (number.getText().toString().trim().length() != 8) {
                    numberWrapper.setError("Ensure phone number has 8 digits");
                    return;
                }
//                final ProgressDialog progressdialog = ProgressDialog.show(getApplicationContext(),
//                        "Waiting for SMS", "Please hold on");
                numberWrapper.setError(null);
                final String phoneNum = number.getText().toString().trim();
                if (!checkNumber(phoneNum)){
                    Toast.makeText(getApplicationContext(), "Phone number of this device not in our database.",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Toast.makeText(getApplicationContext(), "Phone verification will be done through SMS and may take up to 2 minutes",
                            Toast.LENGTH_LONG).show();
                }

                Random random = new Random();
                final String verificationCode = String.valueOf(100000 + random.nextInt(900000));
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNum, null, "Verification code for Dingletutors app: " + verificationCode, null, null);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error sending verification message",
                            Toast.LENGTH_LONG).show();
//                    progressdialog.dismiss();
                    return;
                }
                final CountDownTimer timer = new CountDownTimer(120000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
//                        progressdialog.setMessage("Waiting for message. " + millisUntilFinished / 1000 + " seconds left.");
                    }

                    @Override
                    public void onFinish() {
                        unregisterReceiver(receiver);
//                        progressdialog.dismiss();
                    }
                }.start();
                receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Bundle bundle = intent.getExtras();
                        if (bundle != null){
                            if (readSMS(intent, verificationCode)){
                                timer.cancel();
                                try {
                                    unregisterReceiver(receiver);
                                    MinuteUpdater.studentPresetMap.tutorVerified = true;
                                    String tutorName = tutorPhoneNumbers.get(phoneNum);
                                    Toast.makeText(getApplicationContext(), "Phone verified. Welcome " + tutorName + "!",
                                            Toast.LENGTH_LONG).show();
                                    createMain();
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                };
                registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

                // do the verification here right now I'm just changing to true onclick
            }
        });
    }

    protected void createMain(){
        setContentView(R.layout.activity_main);
        // test();

        NotificationFragment.img = ContextCompat.getDrawable(this, R.drawable.notification);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        final View view = getSupportActionBar().getCustomView();

        oldNotificationCount = (TextView) view.findViewById(R.id.notificationCount);
        // tabs for each page
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        tabLayout.addTab(tabLayout.newTab().setText("Calendar"));
//        tabLayout.addTab(tabLayout.newTab().setText("Lesson History"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

//        drawerLayout =  (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawerList = (ListView) findViewById(R.id.left_drawer);
////
//        drawerList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.drawer_list, categoryTitles));
//        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        MinuteUpdater.getLessons(); // force a load
        mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView)findViewById(R.id.drawerView);

        setupDrawer();
        mCallBack = new DrawerCallBack();
        DrawerMenuItem.setDrawerCallBack(mCallBack);

        ImageButton notificationBtn = (ImageButton) view.findViewById(R.id.notificationBtn);
        notificationBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCallBack.onNotificationsSelected();

//                popup = new PopupNotification(getApplicationContext(), view, view.getWidth() * 3 / 4, view.getWidth() * 3 / 4, 0);
//                popup.show();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.get(intent) != null) {
            String msg = bundle.getString(intent);
            mCallBack.fragment = NotificationFragment.newInstance();
            mCallBack.fragment.setArguments(bundle);
            mCallBack.doTransaction(mCallBack.fragment);

            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            if (msg.equals(MinuteUpdater.lessonIntent)) {
//                mNotifyMgr.cancel(MinuteUpdater.lessonCode);
//            } else if (msg.equals(MinuteUpdater.reportIntent)) {
//                mNotifyMgr.cancel(MinuteUpdater.reportCode);
//            } else {
//                Log.v("Bundle", msg);
//            }

        } else {
            mCallBack.onHomeMenuSelected();
        }


//        final ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
//        pager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));
//
//        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
//            @Override
//            public void onTabSelected(TabLayout.Tab tab){
//                pager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        // mark activity is running
        active = true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // request permission for sms
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        load();
        if (MinuteUpdater.studentPresetMap.tutorVerified) {
            createMain();
        } else {
            createVerification();
        }

    }

    /**
     * For reading verificaition message sent to a number
     */
    private boolean readSMS(Intent intent, String verificationNum) {
        try{
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                if (Build.VERSION.SDK_INT >= 19) { //KITKAT
                    SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                    for (SmsMessage smsMessage:msgs){
                        if (smsMessage.getDisplayMessageBody().contains(verificationNum)){
                            return true;
                        }
                    }
                } else {
                    Object[] smsObjects = (Object[]) bundle.get("pdus");
                    for (int i =0; i < smsObjects.length; i++){
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) smsObjects[i]);
                        if (smsMessage.getDisplayMessageBody().contains(verificationNum)){
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * check input tutor number against database of tutors
     * @param phoneNum
     * @return
     */
    private boolean checkNumber(String phoneNum){
        // check against database of tutor numbers here
        return tutorPhoneNumbers.get(phoneNum) != null ;
    }

    private void setupDrawer(){
        mDrawerView
//                .addView(new DrawerHeader())
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_HOME))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_LESSON_HISTORY))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_NOTIFICATIONS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_STUDENTS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_FINANCES));


    }

    private class DrawerCallBack implements DrawerMenuItem.IDrawerCallBack{

        Fragment fragment;

        public void doTransaction(Fragment fragment){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.drawer_container, fragment)
                .commit();
        }

        @Override
        public void onHomeMenuSelected() {
            fragment = CalendarFragment.newInstance();
            doTransaction(fragment);
        }

        @Override
        public void onLessonHistoryMenuSelected() {
            fragment = LessonHistoryFragment.newInstance();
            doTransaction(fragment);
        }

        @Override
        public void onNotificationsSelected() {
            fragment = NotificationFragment.newInstance();
            doTransaction(fragment);
        }

        @Override
        public void onStudentsSelected() {
            fragment = ViewStudentFragment.newInstance();
            doTransaction(fragment);
        }

        @Override
        public void onFinancesSelected() {
            // add stuff
            fragment = FinanceFragment.newInstance();
            doTransaction(fragment);
        }
    }

//    private class CustomPagerAdapter extends FragmentPagerAdapter {
//
//        private CustomPagerAdapter(FragmentManager manager){ super(manager); }
//
//        @Override
//        public Fragment getItem(int position) {
//            switch(position) {
//                case 0:
//                    return CalendarFragment.newInstance();
//                case 1:
//                    return LessonHistoryFragment.newInstance();
//                default:
//                    return LessonHistoryFragment.newInstance();
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return 2;
//        }
//    }

    public void load() {
        // init class and students presets
        try {
            if (MinuteUpdater.mapDir == null) {
                MinuteUpdater.mapDir = new File(getFilesDir(), "/map");
                MinuteUpdater.mapDir.mkdirs();
            }
            MinuteUpdater.recurringLessonMap = RecurringLessonMap.init("recurringLessons.map");
            MinuteUpdater.lessonPresetMap = LessonPresetMap.init("lessons.map");
            MinuteUpdater.studentPresetMap = StudentPresetMap.init("students.map");

            if (!BackgroundNotification.initialized) {
                Log.v("BackgroundNotification", "not initialized");
                (new BackgroundNotification()).onReceive(this, new Intent().setAction("android.intent.action.BOOT_COMPLETED"));
            }
            while (MinuteUpdater.isInitializing || MinuteUpdater.calendarMap == null) {
                Thread.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test() {
        try {
            // for testing
            if (MinuteUpdater.calendarMap.get("6-2017") != null && !MinuteUpdater.calendarMap.isEmpty()) {
                Log.v("CalendarMap", "Retrieved stored month");
            }
            // to remove all the stuff we have been adding
//            CalendarMap.map.clear();
//            MonthMap testMonth = new MonthMap("6-2017", CalendarMap.map);
//            final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
//            DayMap testDay = new DayMap(formatter.format(Calendar.getInstance().getTime()), testMonth);
//            Lesson temp = new Lesson("01", "00", "test", 0, testDay);
            // this is for test save only
            MinuteUpdater.calendarMap.save();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
