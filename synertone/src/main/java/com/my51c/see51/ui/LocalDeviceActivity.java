package com.my51c.see51.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.my51c.see51.data.Device;
import com.my51c.see51.data.DeviceList;
import com.my51c.see51.data.DeviceLocalInfo;
import com.my51c.see51.data.SelectionDevice;
import com.my51c.see51.listener.DeviceListListener;
import com.my51c.see51.map.CameraLocation;
import com.my51c.see51.widget.DeviceListView;
import com.synertone.netAssistant.R;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;


public class LocalDeviceActivity extends BaseActivity implements DeviceListListener,
        View.OnClickListener, DeviceListView.OnRefreshListener,AdapterView.OnItemClickListener {

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
    static final int MSG_UPDATE = 0;
    static final int MSG_ClEAR_PROGRESSBAR = 1;
    static final int MSG_CHECKTIME = 2;
    private AppData appData;
    private Timer timer;
    private TimerTask timerTask;
    private MyHandler mHandler = new MyHandler(this);
    private DeviceList localList;
    private DeviceListAdapter adapter;
    private boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_device);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();

        appData = (AppData) getApplication();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                mHandler.sendEmptyMessage(MSG_ClEAR_PROGRESSBAR);
            }
        };
        timer.schedule(timerTask, 10000);

        localList = appData.getLocalList();
        if (localList.getDeviceCount() == 0) {
            progressView.setVisibility(View.VISIBLE);
            waitTextView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            progressView.setVisibility(View.INVISIBLE);
            waitTextView.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.INVISIBLE);
        }

        emptyDevice.setVisibility(View.INVISIBLE);

        adapter = new DeviceListAdapter(mContext, localList, this, true);
        localList.addListListener(this);
        listView.setAdapter(adapter);
        //setListAdapter(adapter);
        checkDevTime();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerTask.cancel();
        timer.cancel();
        localList.removeListener(this);
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
    }

    private void initView() {
        listView.setItemsCanFocus(true);
        tvBarTitle.setText("本地设备");
        //mapBtn.setVisibility(View.VISIBLE);

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
        listView.setonRefreshListener(this);
        listView.setOnItemClickListener(this);
        rlTopBar.setOnTouchListener(new ComBackTouchListener());
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        // ��Ӧinfo����
        String Gro_Dev = ((DeviceListAdapter.ViewHolder) v.getTag()).Gro_Dev;
        if (Gro_Dev.equals("device")) {
            Device dev = (Device) ((DeviceListAdapter.ViewHolder) v.getTag()).info.getTag();
            Intent intent = new Intent(mContext, DeviceInfoActivity.class);
            intent.putExtra("id", dev.getID());
            intent.putExtra("version", " " + " / " + dev.getLocalInfo().getCameraVer());
            intent.putExtra("name", dev.getLocalInfo().getDeviceName());
            intent.putExtra("isLocal", true);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
        }
    }

    @Override
    public void onListUpdate() {
        // TODO Auto-generated method stub

        mHandler.sendEmptyMessage(MSG_UPDATE);
        if (localList.getDeviceCount() > 0) {
            emptyView.setVisibility(View.INVISIBLE);
            timer.cancel();
        } else {
            timerTask.cancel();
            timer.cancel();
            timer = new Timer();
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    mHandler.sendEmptyMessage(MSG_ClEAR_PROGRESSBAR);
                }
            };
            timer.schedule(timerTask, 1000);
        }


    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        isChecked = false;
        emptyDevice.setVisibility(View.INVISIBLE);
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo == null) {
            return;
        }
        if (netinfo.isConnected()) {
            int t = cm.getActiveNetworkInfo().getType();
            //Log.d("LocalFragment", "Network type = " + t);
            if (t == ConnectivityManager.TYPE_WIFI) {
                // ����wifi����ethernet�����������������豸
                // TODO Auto-generated method stub
                appData.getLocalList().clear();
                appData.getLocalService().search();
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        try {
            String Gro_Dev = ((DeviceListAdapter.ViewHolder) v.getTag()).Gro_Dev;
            if (Gro_Dev.equals("device")) {
                Device dev = (Device) ((DeviceListAdapter.ViewHolder) v.getTag()).info.getTag();
                Pattern pattern = Pattern.compile("^d[a-fA-F0-9]{11}$", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(dev.getID());
                if (matcher.matches()) {
                    Toast.makeText(mContext, getString(R.string.canseenvr), Toast.LENGTH_LONG).show();
                    return;
                }


                if (dev != null && dev.getPlayURL() != null) {
                    SelectionDevice selDev = new SelectionDevice();
                    selDev.setDeviceid(dev.getID());
                    selDev.setDevicename(dev.getLocalInfo().getDeviceName());
                    selDev.setUrl(dev.getPlayURL());
                    selDev.setLocal(true);

                    if (!dev.isbLocalSelected()) {
                        if (!appData.addSelectionDev(selDev)) {
                            Toast.makeText(mContext, getString(R.string.enoughselectiondevice), Toast.LENGTH_LONG).show();
                        } else {
                            dev.setbLocalSelected(true);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        dev.setbLocalSelected(false);
                        appData.removeSelectDev(selDev);
                        adapter.notifyDataSetChanged();
                    }


                }
            }
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private  class MyHandler extends Handler {
        private WeakReference<LocalDeviceActivity> mRef;

        public MyHandler(LocalDeviceActivity mFragment) {
            mRef = new WeakReference<>(mFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mRef == null)
                return;

            LocalDeviceActivity mFrag = mRef.get();
            if (mFrag != null) {
                switch (msg.what) {
                    case MSG_UPDATE:
                        adapter.updateDeviceData();
                        appData.checkLocalDeviceList();
                        adapter.notifyDataSetChanged();
                        progressView.setVisibility(View.INVISIBLE);
                        waitTextView.setVisibility(View.INVISIBLE);
                        emptyView.setVisibility(View.INVISIBLE);
                        mHandler.sendEmptyMessageDelayed(MSG_CHECKTIME, 3000);
                        break;
                    case MSG_ClEAR_PROGRESSBAR:
                        progressView.setVisibility(View.INVISIBLE);
                       waitTextView.setVisibility(View.INVISIBLE);
                        emptyView.setVisibility(View.INVISIBLE);
                        if (localList.getDeviceCount() == 0) {
                          emptyDevice.setVisibility(View.VISIBLE);
                        }
                    case MSG_CHECKTIME:
                        checkDevTime();
                       emptyView.setVisibility(View.INVISIBLE);
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        }
    }
    public void checkDevTime() {
        try {
            if (!isChecked) {
                if (localList.getDeviceCount() != 0) {
                    Calendar calendar = Calendar.getInstance();
                    isChecked = true;
                    for (Device device : localList) {
                        int year = device.getLocalInfo().getnYear();
                        if (year < 2015) {
                            DeviceLocalInfo localDeviceInfo = device.getLocalInfo();
                            localDeviceInfo.setnYear(calendar.get(Calendar.YEAR));
                            localDeviceInfo.setnMon((byte) (calendar.get(Calendar.MONTH) + 1));
                            localDeviceInfo.setnDay((byte) calendar.get(Calendar.DATE));
                            localDeviceInfo.setnHour((byte) calendar.get(Calendar.HOUR_OF_DAY));
                            localDeviceInfo.setnMin((byte) calendar.get(Calendar.MINUTE));
                            localDeviceInfo.setnSec((byte) calendar.get(Calendar.SECOND));
                            appData.getLocalService().setDeviceParam(localDeviceInfo);
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ConcurrentModificationException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
