package com.my51c.see51.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.my51c.see51.BaseActivity;
import com.my51c.see51.common.AppData;
import com.my51c.see51.common.SystemConfigSp;
import com.my51c.see51.config.ServerConfig;
import com.my51c.see51.data.AccountInfo;
import com.my51c.see51.data.AlarmInfo;
import com.my51c.see51.data.Device;
import com.my51c.see51.data.DeviceList;
import com.my51c.see51.data.DeviceSee51Info;
import com.my51c.see51.data.Group;
import com.my51c.see51.data.SelectionDevice;
import com.my51c.see51.guide.DeviceIdActivity;
import com.my51c.see51.guide.GuidSmartId;
import com.my51c.see51.guide.Guide;
import com.my51c.see51.protocal.GvapCommand;
import com.my51c.see51.protocal.GvapPackage;
import com.my51c.see51.protocal.GvapXmlParser;
import com.my51c.see51.service.GvapEvent;
import com.my51c.see51.widget.NewSwitch;
import com.my51c.see51.widget.ReboundScrollView;
import com.synertone.netAssistant.R;

import java.lang.ref.WeakReference;
import java.util.List;
import butterknife.BindView;
public class MainActivityV1_5 extends BaseActivity implements GvapEvent.GvapEventListener {

    @BindView(R.id.tv_bar_title)
    TextView tvBarTitle;
    @BindView(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @BindView(R.id.btnNewCamera)
    LinearLayout btnNewCamera;
    @BindView(R.id.btnFourViews)
    LinearLayout btnFourViews;
    @BindView(R.id.btnMyCamera)
    LinearLayout btnMyCamera;
    @BindView(R.id.txLocalList)
    TextView txLocalList;
    @BindView(R.id.btnLocalCamera)
    LinearLayout btnLocalCamera;
    @BindView(R.id.showWarnSwitch)
    NewSwitch showWarnSwitch;
    @BindView(R.id.showWarn)
    LinearLayout showWarn;
    @BindView(R.id.warnMsg)
    LinearLayout warnMsg;
    @BindView(R.id.local_video)
    LinearLayout localVideo;
 /*   @BindView(R.id.about)
    LinearLayout about;*/
    @BindView(R.id.reboundscrollview)
    ReboundScrollView reboundscrollview;
    @BindView(R.id.setListView)
    ListView setListView;
    @BindView(R.id.btnProgramExit)
    Button btnProgramExit;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    private AppData appData;
    public final static String GUIDE_FINISH_ACTION = "guide_finish_action";
    RefreshDevListReceiver devlistreceiver = new RefreshDevListReceiver();
    public static int openNum = 0;
    public static AccountInfo account;
    public static Dialog mDialog;
    public static Boolean isLogin = false;
    public static boolean isLocalList = false;
    public static boolean isLoginClicked = false;
    public static final int DIALOG_ID_LOGIN = 0;
    private int tryLoginTimes = 0;
    public static boolean FromPlatAcy = false;
    public static boolean fromFav = false;
    private String errorString;
    private Thread reLoginThread = null;
    private boolean bNetConnnect = false;
    private Boolean isAlarmNotification;
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String ALARM_NOTIFICATION = "alarmNotification";
    private static AlertDialog nonetworkAlertDialog;
    private NotificationManager notificationManager;
    public static final String FIRST_RUN = "first";
    IntentFilter intentFilter = new IntentFilter();
    private MyHandler mHandler = new MyHandler(this);

    private  class MyHandler extends Handler {
        private WeakReference<MainActivityV1_5> mRef;

        public MyHandler(MainActivityV1_5 mAct) {
            mRef = new WeakReference<>(mAct);
        }

        @Override
        public void handleMessage(Message msg) {
            GvapCommand cmd = (GvapCommand) msg.obj;
            if(!isVisible){
                return;
            }

            switch (msg.what) {
                case 0:
                    handleSucess(cmd);
                    break;
                case 1:
                    handleFailed(cmd);
                    break;
                case 2:
                    //没啥用
                    //FavoriteFragment.refreshImg.performClick();
                    break;
                case 3:
                    handleTimeOut(cmd);
                    break;
                default:
                    break;
            }
        }

        @SuppressWarnings("deprecation")
        private void handleSucess(GvapCommand commandID) {
            if (mRef == null)
                return;

            MainActivityV1_5 activity = mRef.get();

            if (activity != null) {
                switch (commandID) {
                    case CMD_LOGIN:
                        activity.appData.setAccountInfo(account);
                        activity.appData.getAccountInfo().setLogined(true);
                        activity.appData.getGVAPService().setUserServerLoginStatus(true);
                        if (!activity.appData.getAccountInfo().isGuest()) {
                            if (mDialog != null)
                                mDialog.dismiss();
                            isLogin = true;
                            //activity.setFragment.setIsLoginBtnName(isLogin);

                            // activity.getSupportFragmentManager().beginTransaction()
                            // .replace(R.id.content_frame, new FavoriteFragment()).commitAllowingStateLoss();

                            // activity.setFragment.setLoginState(account.getUsername());
                            isLocalList = false;
                            //activity.mapBtn.setVisibility(View.VISIBLE);
                            //activity.menuSet.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        @SuppressWarnings("deprecation")
        private void handleFailed(GvapCommand commandID) {
            // TODO Auto-generated method stub

            if (commandID == null) {
                return;
            }

            if (mRef == null)
                return;

            MainActivityV1_5 activity = mRef.get();

            if (activity != null) {
                switch (commandID) {
                    case CMD_LOGIN:

                        activity.appData.getGVAPService().setUserServerLoginStatus(false);
                        if (!account.getUsername().equals("guest")) {
                            try {
                                if (mDialog != null)
                                    mDialog.dismiss();

                            } catch (IllegalArgumentException e) {
                            }
                            try {
                                if (!FromPlatAcy) {
                                    activity.showLoginErroDialog();
                                }
                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }


        private void handleTimeOut(GvapCommand commandID) {
            if (mRef == null)
                return;

            MainActivityV1_5 activity = mRef.get();

            if (activity != null) {

                switch (commandID) {
                    case CMD_LOGIN:
                        activity.appData.getGVAPService().setUserServerLoginStatus(false);
                        if (!account.getUsername().equals("guest")) {
                            try {
                                if (mDialog != null)
                                    mDialog.dismiss();

                            } catch (IllegalArgumentException e) {
                            }

                            Toast.makeText(activity.getApplicationContext(), activity.errorString,
                                    Toast.LENGTH_LONG).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_v1_5_layout);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        isAlarmNotification = settings.getBoolean(ALARM_NOTIFICATION, false);
        initVideoServer();
        initView();
        initEvent();
        nonetworkAlertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.sure)
                .setMessage(R.string.nonetwork)
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        }).create();
        appData = (AppData) getApplication();
        if (!appData.getGVAPService().isRunning()) {
            if (!appData.init()) {
            }
        }
        appData.getGVAPService().addGvapEventListener(this);
        setDevListReceiver();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, intentFilter);
        notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        doAutoLogin();
    }

    private void initEvent() {
        rlTopBar.setOnTouchListener(new ComBackTouchListener());
        //安装摄像头
        btnNewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    if (appData.getAccountInfo() == null
                            || !appData.getAccountInfo().isLogined()
                            || appData.getAccountInfo().isGuest()) {
                       /* getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new LoginFragment()).commit();
                        getSlidingMenu().showContent();*/
                        Toast.makeText(mContext, getString(R.string.pleaseLoginFirst), Toast.LENGTH_LONG).show();


                    } else {
                        showAddDialog();
                    }

                }
            }
        });
        //视频预览
        btnFourViews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<SelectionDevice> m_selectdevice = appData.getM_selectdevice();

                if (m_selectdevice == null || m_selectdevice.size() <= 0) {
                    Toast.makeText(mContext, getString(R.string.checkdevicefist), Toast.LENGTH_SHORT).show();
                   return;
                }
                Intent intent = new Intent(mContext, FourPlayerActivity.class);
                startActivity(intent);
            }
        });
        //远程设备
        btnMyCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {
                    Intent intent = new Intent(mContext, RemoteDeviceActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, getString(R.string.pleaseLoginFirst), Toast.LENGTH_LONG).show();
                }
                isLocalList = false;
            }
        });
        //本地设备
        btnLocalCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLocalList = true;
               Intent intent=new Intent(mContext, LocalDeviceActivity.class);
               startActivity(intent);

            }
        });
        //报警通知
        showWarnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                if (isChecked) {
                    editor.putBoolean(ALARM_NOTIFICATION, true);
                    isAlarmNotification = true;
                } else {
                    editor.putBoolean(ALARM_NOTIFICATION, false);
                    isAlarmNotification = false;
                }
                editor.commit();
            }
        });
        //报警历史
        warnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActionAlarmActivity.class);
                startActivity(intent);
            }
        });
        //本地文件
        localVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentabout = new Intent(mContext, LocalFileAcy.class);
                startActivity(intentabout);
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
            }
        });

    }

    private void initView() {
        tvBarTitle.setText("视频监控");
        showWarnSwitch.setChecked(isAlarmNotification);
    }
    public void showAddDialog() {
        final Dialog dialog = new Dialog(mContext, R.style.Erro_Dialog);
        dialog.setContentView(R.layout.add_dev_dialog);
        LinearLayout addInstall = (LinearLayout) dialog.findViewById(R.id.add_install);
        LinearLayout idAdd = (LinearLayout) dialog.findViewById(R.id.addbyid);
        LinearLayout voiceAdd = (LinearLayout) dialog.findViewById(R.id.addbyvoice);
        LinearLayout cancel = (LinearLayout) dialog.findViewById(R.id.cancel);
        addInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onJumpGuideActivity("complex");
            }
        });
        idAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onJumpGuideActivity("simple");
            }
        });
        voiceAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(mContext, GuidSmartId.class);
                startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    public void onJumpGuideActivity(String strType) {
        String inBindType = "complex";
        inBindType = strType;

        Intent intent;
        if (appData.getAccountInfo() == null
                || !appData.getAccountInfo().isLogined()
                || appData.getAccountInfo().isGuest()) {
            intent = new Intent(mContext, Guide.class);
            intent.putExtra("BindStyle", inBindType);
        } else {
            intent = new Intent(mContext, DeviceIdActivity.class);
            intent.putExtra("BindStyle", inBindType);
        }

        startActivity(intent);
    }
    public void showLoginErroDialog() {
        final Dialog dialog = new Dialog(this, R.style.Erro_Dialog);
        dialog.setContentView(R.layout.login_erro_dialog);
        TextView erroTx = (TextView) dialog.findViewById(R.id.erroTx);
        Button reTry = (Button) dialog.findViewById(R.id.erro_reTry);
        Button cancel = (Button) dialog.findViewById(R.id.erro_cancel);
        erroTx.setText(getString(R.string.invalidPassword));
        reTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                doAutoLogin();
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }



    private void initVideoServer() {
        SystemConfigSp.instance().init(getApplicationContext());
        if (TextUtils.isEmpty(SystemConfigSp.instance().getStrConfig(SystemConfigSp.SysCfgDimension.LOGINSERVERIP))) {
            SystemConfigSp.instance().setStrConfig(SystemConfigSp.SysCfgDimension.LOGINSERVERIP, ServerConfig.loginServerAddr);
        }

        if (SystemConfigSp.instance().getIntConfig(SystemConfigSp.SysCfgDimension.LOGINSERVERPORT) == 0) {
            SystemConfigSp.instance().setIntConfig(SystemConfigSp.SysCfgDimension.LOGINSERVERPORT, ServerConfig.loginServerPort);
        }

        if (TextUtils.isEmpty(SystemConfigSp.instance().getStrConfig(SystemConfigSp.SysCfgDimension.REGISTERSERVERIP))) {
            SystemConfigSp.instance().setStrConfig(SystemConfigSp.SysCfgDimension.REGISTERSERVERIP, ServerConfig.regServerAddr);
        }

        if (SystemConfigSp.instance().getIntConfig(SystemConfigSp.SysCfgDimension.REGISTERSERVERPORT) == 0) {
            SystemConfigSp.instance().setIntConfig(SystemConfigSp.SysCfgDimension.REGISTERSERVERPORT, ServerConfig.regServerPort);
        }
    }

    private void doAutoLogin() {
        isLoginClicked = true;
        String userName = "snt1073";
        String password = "111111";
        boolean isInputOK = userName.length() > 0 && password.length() > 0;
        if (isInputOK) {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            if (isConnected) {
                login(userName, password);
            } else {
                toastMessage(R.string.timeout);
            }

        } else {
            toastMessage(R.string.inputIncomplete);
        }
    }

    private void login(String userName, String password) {
        account = new AccountInfo(userName, password);
        appData.getGVAPService().logout();
        appData.clearSelectDev();
        if (appData.getGVAPService().getUsrAccount() != null &&
                appData.getGVAPService().getUsrAccount().getDevList() != null)
            appData.getGVAPService().getUsrAccount().getDevList().clear();
        appData.getGVAPService().login(account);
        // appData.setAccountInfo(account);
        //MainActivity.account = account;
        tryLoginTimes = 0;
        showLoginDialog(DIALOG_ID_LOGIN);
    }

    public void showLoginDialog(int id) {
        mDialog = new Dialog(this, R.style.Login_Dialog);
        mDialog.setContentView(R.layout.login_dialog);
        TextView tx = (TextView) mDialog.findViewById(R.id.tx);
        if (id == DIALOG_ID_LOGIN)
            tx.setText(getString(R.string.userlogining));
        mDialog.show();
    }

    private void toastMessage(int resId) {
        Toast toast;
        toast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void setDevListReceiver() {
        IntentFilter filter = new IntentFilter(GUIDE_FINISH_ACTION);
        registerReceiver(devlistreceiver, filter);
    }

    @Override
    public void onGvapEvent(GvapEvent event) {
        switch (event) {
            case OPERATION_SUCCESS:
                onOperationSuccess(event.getCommandID(),
                        (GvapPackage) event.attach(),
                        (GvapPackage) event.getRequest());
                break;
            case OPERATION_FAILED:
                onOperationFailed(event.getCommandID(),
                        (GvapPackage) event.attach());
                break;
            case OPERATION_TIMEOUT:
                onOperationTimeout(event.getCommandID(), (GvapPackage) event.attach());
                break;
            case CONNECTION_RESET:
                onConnectReset();
                break;
            case CONNECT_TIMEOUT:
                break;
            case CONNECT_FAILED:
                onConnectFailed();
                break;
            case NETWORK_ERROR:
                onNetworkError();
                break;
            case SERVER_REQUEST:
                onGetServerRequest((GvapPackage) event.attach());
                break;
            case OPEN_FAILED:
                openNum++;
                appData.getGVAPService().stop();
                appData.getGVAPService().start();
                break;

        }
    }

    private void onGetServerRequest(GvapPackage response) {
        String alarmtype = response.getResourceName();

        if (alarmtype.equals("alarm")) {
            String deviceId = response.getParam("device-id");
            Device device = appData.getAccountInfo().getDevList().getDevice(deviceId);
            String devName = "ipcamera";
            if (device != null) {
                devName = device.getSee51Info().getDeviceName();
            }
            AlarmInfo alarmInfo = new AlarmInfo(deviceId, devName,
                    response.getParam("message"), response.getParam("title"));
            //Log.d(TAG, "onGetServerRequest");
            appData.getAccountInfo().addAlarmInfo(alarmInfo);
            addNotificaction();
        }
    }

    public void addNotificaction() {
        if (isAlarmNotification) {
            Intent intent = new Intent(this, ActionAlarmActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    intent, PendingIntent.FLAG_ONE_SHOT);
            Notification notification = new Notification.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle(getString(R.string.alarm))
                    .setContentText(getString(R.string.strangers_illegal_instrusion))
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .build();
            notification.defaults = Notification.DEFAULT_SOUND;
            notificationManager.notify(1, notification);
        }
    }

    private void onNetworkError() {
        appData.getGVAPService().setUserServerLoginStatus(false);
        if (appData.getAccountInfo() != null
                && appData.getAccountInfo().isLogined()) {
            appData.setReLogin(true);
            appData.getGVAPService().logout();
            appData.clearSelectDev();
            if (appData.getGVAPService().getUsrAccount() != null &&
                    appData.getGVAPService().getUsrAccount().getDevList() != null)
                appData.getGVAPService().getUsrAccount().getDevList().clear();

            appData.getAccountInfo().setLogined(false);
            appData.getGVAPService().login(account);
        }
    }

    public void onConnectFailed() {
        if (appData.getAccountInfo() != null
                && appData.getAccountInfo().isLogined()) {
            appData.setReLogin(true);
            appData.getGVAPService().logout();
            appData.clearSelectDev();
            if (appData.getGVAPService().getUsrAccount() != null &&
                    appData.getGVAPService().getUsrAccount().getDevList() != null)
                appData.getGVAPService().getUsrAccount().getDevList().clear();

            appData.getAccountInfo().setLogined(false);
            appData.getGVAPService().login(account);
        }
    }

    public void onConnectReset() {
        if (reLoginThread == null) {
            reLoginThread = new Thread(reLoginRunnable);
            reLoginThread.start();
        }
    }

    Runnable reLoginRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (isLogin)    //always runing check if need reloagin
            {
                if (!bNetConnnect) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    continue;

                }

                if (!appData.getAccountInfo().isLogined()) {
                    ConnectReset();
                }
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    };

    public void ConnectReset() {
        if (appData.getAccountInfo() != null) {
            appData.setReLogin(true);
            appData.getGVAPService().logout();
            appData.clearSelectDev();
            if (appData.getGVAPService().getUsrAccount() != null &&
                    appData.getGVAPService().getUsrAccount().getDevList() != null)
                appData.getGVAPService().getUsrAccount().getDevList().clear();

            appData.getAccountInfo().setLogined(false);
            appData.getGVAPService().login(account);

        }
    }

    public void onOperationTimeout(GvapCommand cmd, GvapPackage response) {
        Message msg;
        if (cmd == null) {
            return;
        }
        //Log.d(TAG, "onOperationTimeout  cmd == " + cmd);
        switch (cmd) {
            case CMD_LOGIN:
                if (appData.isReLogin()) {
                    break;
                } else {
                    tryLoginTimes++;
                    if (tryLoginTimes < 2) {
                        appData.getGVAPService().logout();
                        appData.clearSelectDev();
                        if (appData.getGVAPService().getUsrAccount() != null &&
                                appData.getGVAPService().getUsrAccount().getDevList() != null)
                            appData.getGVAPService().getUsrAccount().getDevList().clear();

                        appData.getGVAPService().login(account);

                        break;
                    } else {
                        errorString = getString(R.string.timeout);
                        msg = mHandler.obtainMessage(3, GvapCommand.CMD_LOGIN);
                        mHandler.sendMessage(msg);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void onOperationFailed(GvapCommand cmd, GvapPackage response) {
        Message msg;
        if (cmd == null) {
            return;
        }
        int retcode = response.getStatusCode();
        switch (cmd) {
            case CMD_LOGIN:

                switch (retcode) {
                    default:
                    case 403:
                        appData.setReLogin(true);
                        appData.getGVAPService().logout();
                        appData.clearSelectDev();
                        if (appData.getGVAPService().getUsrAccount() != null &&
                                appData.getGVAPService().getUsrAccount().getDevList() != null)
                            appData.getGVAPService().getUsrAccount().getDevList().clear();
                        appData.getGVAPService().login(account);
                        break;

                    case 405:
                    case 406:
                        msg = mHandler.obtainMessage(1, GvapCommand.CMD_LOGIN);
                        mHandler.sendMessage(msg);
                        break;
                }
                break;
            default:
                appData.setReLogin(true);
                appData.getGVAPService().logout();
                appData.clearSelectDev();
                if (appData.getGVAPService().getUsrAccount() != null &&
                        appData.getGVAPService().getUsrAccount().getDevList() != null)
                    appData.getGVAPService().getUsrAccount().getDevList().clear();
                appData.getGVAPService().login(account);

                break;
        }
    }

    private void onOperationSuccess(GvapCommand cmd, GvapPackage response, GvapPackage request) {
        Message msg;
        switch (cmd) {
            case CMD_LOGIN: {
                if (account != null) {
                    account.setLogined(true);
                    appData.setAccountInfo(account);
                }
                appData.getAccountInfo().setLogined(true);
                appData.getGVAPService().getVersions();
                if (appData.isReLogin()) {
                    appData.setReLogin(false);
                } else {
                    msg = mHandler.obtainMessage(0, GvapCommand.CMD_LOGIN);
                    mHandler.sendMessage(msg);
                }

                appData.getGVAPService().setHeartBeat_expire(
                        Integer.parseInt(response.getParam("expire")));
                break;
            }
            case CMD_GET_VERSIONS: {
                onGetVersionsSuccess(response);
                break;
            }
            case CMD_GET_DEVLIST: {
                String parent_group = request.getParent_group();
                if (parent_group != null)//�����б�
                {

                    if (appData != null && !"".equals(appData)) {
                        onGetDeviListSuccess(response, appData.getAccountInfo()
                                .getDevList(parent_group));
                    }

                } else                    //��Ŀ¼�б�
                {
                    onGetDeviListSuccess(response, appData.getAccountInfo()
                            .getDevList());
                }
                break;
            }
            case CMD_GET_PUBLIST: {
                String parent_group = request.getParent_group();
                if (parent_group != null) {
                    onGetDeviListSuccess(response, appData.getDevList(parent_group));
                } else
                    onGetDeviListSuccess(response, appData.getPublicList());
                break;
            }
            case CMD_GET_DEVSTATUS:
                String groupId = request.getParent_group();
                updateDeviceInfo(request, response);
                if (!appData.getAccountInfo().isGuest()) {
                    if (groupId != null) {
                        appData.getGVAPService().getDeviceInfo(
                                appData.getAccountInfo().getDevList(groupId));
                    } else {
                        appData.getGVAPService().getDeviceInfo(//��ȡ�б����豸��ϸ��Ϣ��CMD_GET_DEVINFO��
                                appData.getAccountInfo().getDevList());
                    }
                }
                break;
            case CMD_GET_DEVINFO: {
                updateDeviceInfo(request, response);
                if (!appData.getAccountInfo().isGuest()) {
                    // dbAdapter.saveDeviceList(appData.getPublicList(),
                    // appData.getAccountInfo().getUsername());
                }
                // dbAdapter.saveDeviceList(appData.getPublicList(), null);
                break;
            }
            case CMD_UNBIND:
                msg = mHandler.obtainMessage(0, GvapCommand.CMD_UNBIND);
                mHandler.sendMessage(msg);
                break;
        }
    }

    private void updateDeviceInfo(GvapPackage request, GvapPackage response) {
        if (!appData.getAccountInfo().isGuest()) {
            String groupId = request.getParent_group();
            if (groupId == null) {
                GvapXmlParser.parseDevInfo(new String(response.getContent()),
                        appData.getAccountInfo().getDevList());
                appData.getAccountInfo().getDevList().listUpdated();
            } else {
                GvapXmlParser.parseDevInfo(new String(response.getContent()),
                        appData.getAccountInfo().getDevList(groupId));
                appData.getAccountInfo().getDevList(groupId).listUpdated();
            }

        }
        GvapXmlParser.parseDevInfo(new String(response.getContent()),
                appData.getPublicList());
        appData.getPublicList().listUpdated();
    }

    private void onGetDeviListSuccess(GvapPackage response, DeviceList devList) {
        if (fromFav) {
            devList.clear();
            fromFav = false;
        }
        List<String> list = response.getParamList("device-id");
        List<String> listGro = response.getParamList("group-id");
        if (listGro != null) {
            for (String gro : listGro) {
                Group group = new Group(gro);
                devList.addGroup(group);
            }
            devList.updateSuccess();// �����б���³ɹ���־
            appData.getGVAPService().getGroupInfo(devList);
        }
        if (list != null) {
            //Log.d("GVAPService", "CMD_GET_DEVLIST: list != null ");
            Group group = devList.getParent_group();
            if (group != null)
                group.setDevCount(list.size());
            for (String dev : list) {
                DeviceSee51Info info = new DeviceSee51Info(dev);

                List<SelectionDevice> mylist = appData.getM_selectdevice();
                boolean bFound = false;
                if (mylist != null) {
                    for (int i = 0; i < mylist.size(); i++) {
                        SelectionDevice seldev = mylist.get(i);

                        if (seldev.getDeviceid().equals(info.getDiviceID()) && seldev.isLocal() == false) {
                            bFound = true;
                            break;
                        }
                    }
                }

                devList.addBySee51Info(info, bFound);    // �����µ��豸
            }

            devList.updateSuccess();            // �����б���³ɹ���־
            appData.getGVAPService().getDeviceStatus(devList);
        }


    }

    private void onGetVersionsSuccess(GvapPackage response) {
        if (!appData.getAccountInfo().isGuest()) {
            int ver = response.getIntegerParamWithDefault("dev-version", -1);
            if (ver != appData.getAccountInfo().getDevList().getVersion()) {
                appData.getGVAPService().getDeviceList();
                appData.getAccountInfo().getDevList().setServeVersion(ver); // �����б�ķ������汾
            } else
            // ���ذ汾��������汾��ͬ
            {
                appData.getGVAPService().getDeviceStatus(
                        appData.getAccountInfo().getDevList());
            }
        }
    }

    private class RefreshDevListReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            appData.getGVAPService().getDeviceList();
        }
    }

    private final BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if ((mobNetInfo == null || !mobNetInfo.isConnected())
                    && (wifiNetInfo == null || !wifiNetInfo.isConnected())) {
                setNetStatus(false);

                nonetworkAlertDialog.show();

            } else {
                setNetStatus(true);
            }
        }
    };

    private void setNetStatus(boolean bNet) {
        bNetConnnect = bNet;
        if (appData != null) {
            appData.setNetStatus(bNet);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseResource();
        mHandler.removeCallbacksAndMessages(null);
        if (nonetworkAlertDialog != null) {
            nonetworkAlertDialog = null;
        }

        if (account != null) {
            account = null;
        }
        //bm-add-1101 end

        if (connectionReceiver != null) {
            unregisterReceiver(connectionReceiver);
        }

        if (devlistreceiver != null) {
            unregisterReceiver(devlistreceiver);
            devlistreceiver = null;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        Boolean first = settings.getBoolean(FIRST_RUN, true);
        if (first) {
            editor.putBoolean(FIRST_RUN, false);
        }
        editor.commit();
    }

   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || event.getRepeatCount() == 0) {
            releaseResource();
            this.finish();
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }*/

    private void releaseResource() {
        try {
            if (isLogin&&appData!=null) {
                isLogin = false;
                //bReLoginFlag = false;
                reLoginThread = null;
                //setFragment.setIsLoginBtnName(isLogin);
                appData.getGVAPService().logout();
                appData.clearSelectDev();
                if (appData.getGVAPService().getUsrAccount() != null &&
                        appData.getGVAPService().getUsrAccount().getDevList() != null)
                    appData.getGVAPService().getUsrAccount().getDevList().clear();

                appData.getAccountInfo().setLogined(false);
                appData.setAccountInfo(null);
                appData.release();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

