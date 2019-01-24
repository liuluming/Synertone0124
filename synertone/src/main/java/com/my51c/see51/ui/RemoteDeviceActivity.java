package com.my51c.see51.ui;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.my51c.see51.BaseActivity;
import com.my51c.see51.adapter.DeviceListAdapter;
import com.my51c.see51.common.AppData;
import com.my51c.see51.common.MyDensityUtil;
import com.my51c.see51.data.Device;
import com.my51c.see51.data.DeviceList;
import com.my51c.see51.data.Group;
import com.my51c.see51.data.SelectionDevice;
import com.my51c.see51.listener.DeviceListListener;
import com.my51c.see51.map.CameraLocation;
import com.my51c.see51.service.GVAPService;
import com.my51c.see51.widget.DeviceListView;
import com.synertone.netAssistant.R;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;


public class RemoteDeviceActivity extends BaseActivity implements DeviceListListener, View.OnClickListener, DeviceListView.OnRefreshListener,AdapterView.OnItemClickListener {

    @BindView(R.id.tv_bar_title)
    TextView tvBarTitle;
    @BindView(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @BindView(R.id.refreshImg)
    ImageView refreshImg;
    @BindView(R.id.dlv_content)
    DeviceListView listView;
    @BindView(R.id.loading)
    TextView waitTextView;
    @BindView(R.id.progress_get_devices_image)
    LinearLayout progressView;
    @BindView(R.id.emptyView)
    LinearLayout emptyView;
    @BindView(R.id.emptydevice)
    TextView emptyDevice;
    @BindView(R.id.devListLayout)
    LinearLayout devListLayout;
    @BindView(R.id.mapBtn)
    ImageView mapBtn;
    private boolean canRefresh = true;
    private AppData appData;
    static final int MSG_UPDATE = 0;
    static final int MSG_ClEAR_PROGRESSBAR = 1;
    private MyHandler mHandler = new MyHandler(this);
    private Timer timer;
    private TimerTask timerTask;
    private DeviceList myList;
    static DeviceListAdapter adapter;
    private static AlertDialog nonetworkAlertDialog;
    private Group parent_group = null;
    private GVAPService gvapService;
    private final String DEVICE="device";
    private final String GROUP="group";
    private boolean isShowSubList=false;


    private class MyHandler extends Handler {
        private WeakReference<RemoteDeviceActivity> mRef;

        public MyHandler(RemoteDeviceActivity mAct) {
            mRef = new WeakReference<>(mAct);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mRef == null||!isVisible)
                return;

            RemoteDeviceActivity mFragment = mRef.get();
            if (mFragment != null) {
                switch (msg.what) {
                    case MSG_UPDATE:
                        if (adapter != null) {
                            adapter.updateDeviceData();
                            adapter.notifyDataSetChanged();
                        }
                        progressView.setVisibility(View.INVISIBLE);
                        waitTextView.setVisibility(View.INVISIBLE);
                        emptyView.setVisibility(View.INVISIBLE);
                        synchronized (myList) {
                            if (myList.getDeviceCount() == 0) {
                                emptyDevice.setVisibility(View.VISIBLE);
                            } else {
                                emptyDevice.setVisibility(View.INVISIBLE);
                            }
                        }


                        break;
                    case MSG_ClEAR_PROGRESSBAR:
                        progressView.setVisibility(View.INVISIBLE);
                        waitTextView.setVisibility(View.INVISIBLE);
                        emptyView.setVisibility(View.INVISIBLE);
                        synchronized (myList) {
                            if (myList.getDeviceCount() == 0) {
                                emptyDevice.setVisibility(View.VISIBLE);
                            } else {
                                emptyDevice.setVisibility(View.INVISIBLE);
                            }
                        }

                        break;
                    case 7:
                        try {
                            if (appData != null &&
                                    appData.getAccountInfo() != null &&
                                    appData.getAccountInfo().getCurrentList() != null &&
                                    appData.getAccountInfo().getCurrentList().getDeviceCount() == 0) {
                                refreshDevice();
                            }
                        } catch (NullPointerException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
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
        setContentView(R.layout.activity_remote_device);
        initView();
        initData();
        initEvent();
        gvapService = new GVAPService();
        gvapService.bNetStatus = true;
        if (MainActivity.isLoginClicked) {
            mHandler.sendEmptyMessageDelayed(7, 5000);
            MainActivity.isLoginClicked = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        appData = (AppData)getApplication();

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                mHandler.sendEmptyMessage(MSG_ClEAR_PROGRESSBAR);
            }
        };


        myList = appData.getAccountInfo().getCurrentList();
        synchronized (myList) {
            if (myList.getDeviceCount() == 0) {
                progressView.setVisibility(View.VISIBLE);
                waitTextView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.VISIBLE);
                timer.schedule(timerTask, 10000);
            } else {
                progressView.setVisibility(View.INVISIBLE);
                waitTextView.setVisibility(View.INVISIBLE);
                emptyView.setVisibility(View.INVISIBLE);
            }

        }

        emptyDevice.setVisibility(View.INVISIBLE);

        try {
            adapter = new DeviceListAdapter(mContext, myList, this, false);
        } catch (OutOfMemoryError e) {
            // TODO Auto-generated catch block
        }

        nonetworkAlertDialog = new AlertDialog.Builder(mContext)
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
        myList.addListListener(this);
        listView.setAdapter(adapter);
        //setListAdapter(adapter);
    }

    private void initData() {

    }

    private void initEvent() {
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CameraLocation.class));
            }
        });
        rlTopBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float downX=event.getX();
                float downY = event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    int maxX= MyDensityUtil.dip2px(130);
                    int maxY=MyDensityUtil.dip2px(getResources().getDimension(R.dimen.bar_height));
                    if(downX<=maxX &&downY<maxY){
                        backParent();
                        /*application.removeAct((Activity) mContext);
                        finish();*/
                    }
                }
                return false;
            }
        });
        listView.setonRefreshListener(this);
        listView.setOnItemClickListener(this);
    }

    private void initView() {
        listView.setItemsCanFocus(true);
        tvBarTitle.setText("远程设备");
        emptyDevice.setVisibility(View.INVISIBLE);
        mapBtn.setVisibility(View.VISIBLE);

    }
    public void backParent() {
        if (appData.getAccountInfo() != null) {
            DeviceList currentList = appData.getAccountInfo().getCurrentList();
            String grandParent_group = currentList.getGrandParent_group();
            DeviceList devList;
            if (grandParent_group != null) {
                devList = appData.getAccountInfo().getDevList(grandParent_group);
                appData.getAccountInfo().setCurrentList(devList);
            } else {
                /*menuBtn.setVisibility(View.VISIBLE);
                menuBack.setVisibility(View.GONE);*/
                devList = appData.getAccountInfo().getDevList(); // ������
                appData.getAccountInfo().setCurrentList(devList);
                //displayHomeButton(false);
            }
            if(adapter!=null){
                adapter.setDeviceList(devList);
            }
            devList.listUpdated();
            if(!isShowSubList){
                  application.removeAct((Activity) mContext);
                        finish();
            }else{
                isShowSubList=false;
            }
        }
    }
    public void refreshDevice() {
        emptyDevice.setVisibility(View.INVISIBLE);
        MainActivity.fromFav = true;

        if (!appData.getAccountInfo().isLogined()
                || appData.getAccountInfo().isGuest() || !appData.getNetStatus()) {
            nonetworkAlertDialog.show();
            return;
        }
        mHandler.sendEmptyMessageDelayed(MSG_ClEAR_PROGRESSBAR, 15000);

        /*update start*/
        DeviceList currentList = appData.getAccountInfo().getCurrentList();
        parent_group = currentList.getParent_group();
        if (parent_group != null)                            //��ȡ������Ϣ
        {
            String groupId = parent_group.getGroupID();
//				appData.getAccountInfo().getDevList(groupId).clear();
            appData.getGVAPService().getDeviceList(groupId);
        } else                                                //��ȡ��Ŀ¼��Ϣ
        {
//				appData.getAccountInfo().getDevList().clear();
            appData.getGVAPService().getDeviceList();            //���ͻ�ȡ�豸�б�ָ��
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        // ��Ӧinfo����
        String Gro_Dev = ((DeviceListAdapter.ViewHolder) v.getTag()).Gro_Dev;
        if (Gro_Dev.equals(DEVICE)) {
            Device dev = (Device) ((DeviceListAdapter.ViewHolder) v.getTag()).info.getTag();
            Intent intent = new Intent(mContext, DeviceInfoActivity.class);
            intent.putExtra("id", dev.getID());
            intent.putExtra("version", dev.getSee51Info().getHwVersion() + " / " + dev.getSee51Info().getSwVersion());
            intent.putExtra("name", dev.getSee51Info().getDeviceName());
            intent.putExtra("isLocal", false);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);

        } else if (Gro_Dev.equals(GROUP)) {
            showSubList(v);
        }
    }

    @Override
    public void onListUpdate() {
        // TODO Auto-generated method stub
        mHandler.sendEmptyMessage(MSG_UPDATE);
        synchronized (myList) {
            if (myList.getDeviceCount() > 0) {
                if (timer != null)
                    timer.cancel();
            } else {
                if (timerTask != null)
                    timerTask.cancel();

                if (timer != null)
                    timer.cancel();

                timer = new Timer();
                timerTask = new TimerTask() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mHandler.sendEmptyMessage(1);
                    }
                };
                timer.schedule(timerTask, 1000);
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        //Log.i("FragmentList", "Item clicked: id " + id);
        try {
            String Gro_Dev = ((DeviceListAdapter.ViewHolder) v.getTag()).Gro_Dev;
            if (Gro_Dev.equals(DEVICE)) {
                Device dev = (Device) ((DeviceListAdapter.ViewHolder) v.getTag()).info.getTag();

                Pattern pattern = Pattern.compile("^d[a-fA-F0-9]{11}$", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(dev.getID());
                if (matcher.matches()) {
                    Toast.makeText(mContext, getString(R.string.canseenvr), Toast.LENGTH_LONG).show();
                    return;
                }

                if (dev != null) {
                    SelectionDevice selDev = new SelectionDevice();
                    selDev.setDeviceid(dev.getID());
                    selDev.setDevicename(dev.getSee51Info().getDeviceName());
                    selDev.setUrl(dev.getPlayURL());
                    selDev.setLocal(false);

                    if (dev.isbRemoteSelected()) {
                        dev.setbRemoteSelected(false);
                        appData.removeSelectDev(selDev);
                        adapter.notifyDataSetChanged();
                    } else if (!dev.isbRemoteSelected() && dev.getPlayURL() != null) {
                        if (!appData.addSelectionDev(selDev)) {
                            Toast.makeText(mContext, getString(R.string.enoughselectiondevice), Toast.LENGTH_LONG).show();
                        } else {
                            dev.setbRemoteSelected(true);
                            adapter.notifyDataSetChanged();
                        }
                    }


                }
            } else if (Gro_Dev.equals(GROUP)) {
                showSubList(v);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    public void showSubList(View v) {
        isShowSubList=true;
      /*  mainActivity.menuBtn.setVisibility(View.GONE);
        mainActivity.menuBack.setVisibility(View.VISIBLE);
*/
        Group parent_group = ((DeviceListAdapter.ViewHolder) v.getTag()).group;
        String groupId = parent_group.getGroupID();
        String grandParent_group = ((DeviceListAdapter.ViewHolder) v.getTag()).grandParent_group;
        if (appData.getAccountInfo().getDevList(groupId) == null) {
            appData.getAccountInfo().addList(parent_group,
                    grandParent_group);
            appData.getGVAPService().getDeviceList(groupId);
        }
        DeviceList devList = appData.getAccountInfo().getDevList(groupId);
        appData.getAccountInfo().setCurrentList(devList);
        myList = appData.getAccountInfo().getCurrentList();
        myList.addListListener(this);
        if(adapter==null){
            try {
                adapter = new DeviceListAdapter(mContext, myList, this, false);
            } catch (OutOfMemoryError e) {
                // TODO Auto-generated catch block
            }
        }
        adapter.setDeviceList(myList);
        myList.listUpdated();
        listView.setAdapter(adapter);
        //setListAdapter(adapter);
       /* MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.displayHomeButton(true);*/
    }
    @Override
    public void onRefresh() {
        emptyDevice.setVisibility(View.INVISIBLE);
        if (canRefresh) {
            emptyView.setVisibility(View.GONE);
            refreshDevice();
            canRefresh = false;
            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    canRefresh = true;
                }
            }, 2000);
        } else {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerTask.cancel();
        timer.cancel();
        myList.removeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter = null;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        if (nonetworkAlertDialog != null)
            nonetworkAlertDialog = null;

        if (adapter != null)
            adapter = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
         // 判断按下的键是否是“返回”键
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            backParent();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

