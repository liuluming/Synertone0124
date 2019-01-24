package com.my51c.see51.app.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.came.viewbguilib.ButtonBgUi;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.my51c.see51.Logger.LoggerSave;
import com.my51c.see51.adapter.CommonAdapter;
import com.my51c.see51.adapter.CommonViewHolder;
import com.my51c.see51.adapter.MySpinnerAdapter;
import com.my51c.see51.app.LoginActivity;
import com.my51c.see51.app.OneKeyStarActivity;
import com.my51c.see51.app.bean.AntennaParameterBean;
import com.my51c.see51.app.bean.BucDataModel;
import com.my51c.see51.app.bean.DevStatusBean;
import com.my51c.see51.app.bean.LnbDataModel;
import com.my51c.see51.app.bean.StarCodeModel;
import com.my51c.see51.app.fragment.WarnDialogFragment;
import com.my51c.see51.app.http.SntAsyncHttpGet;
import com.my51c.see51.app.http.SntAsyncHttpGet.HpOverListener;
import com.my51c.see51.app.http.SntAsyncPost;
import com.my51c.see51.app.http.SntAsyncPost.PostOverHandle;
import com.my51c.see51.app.http.XTHttpUtil;
import com.my51c.see51.app.utils.ChechIpMask;
import com.my51c.see51.app.utils.SntSpUtils;
import com.my51c.see51.app.utils.ViewUtil;
import com.my51c.see51.common.AppData;
import com.my51c.see51.common.MyDensityUtil;
import com.my51c.see51.widget.NiceDialog;
import com.my51c.see51.widget.ReSpinner;
import com.my51c.see51.widget.SharedPreferenceManager;
import com.synertone.commonutil.util.GsonUtils;
import com.synertone.commonutil.util.MathUtil;
import com.synertone.commonutil.util.ScreenUtil;
import com.synertone.commonutil.view.BaseNiceDialog;
import com.synertone.commonutil.view.ConfirmCancelDialog;
import com.synertone.commonutil.view.ConfirmDialog;
import com.synertone.commonutil.view.ViewConvertListener;
import com.synertone.commonutil.view.ViewHolder;
import com.synertone.netAssistant.R;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.my51c.see51.app.http.XTHttpUtil.GET_COMPASS;
import static com.my51c.see51.app.http.XTHttpUtil.GET_SATEADV_ARG;
import static com.my51c.see51.app.http.XTHttpUtil.GET_STOP;
import static com.my51c.see51.app.http.XTHttpUtil.POST_ARGSET_ADV;
import static com.my51c.see51.app.http.XTHttpUtil.QUERY_COMPASS_RESULT;

/*参数设置
 * */
public class SuperSetRefActivity extends FragmentActivity implements OnClickListener, OnCheckedChangeListener {
    @BindView(R.id.tv_bar_title)
    TextView tv_bar_title;
    @BindView(R.id.rl_top_bar)
    RelativeLayout rl_top_bar;
    @BindView(R.id.tv_compass)
    TextView tv_compass;
    @BindView(R.id.bt_compass)
    ButtonBgUi bt_compass;
    @BindView(R.id.ll_compass)
    LinearLayout ll_compass;
    @BindView(R.id.bt_save_default)
    ButtonBgUi bt_save_default;
    @BindView(R.id.tv_satellite_number)
    TextView tv_satellite_number;
    @BindView(R.id.et_satellite_number)
    EditText et_satellite_number;
    @BindView(R.id.tv_satellite_longitude)
    TextView tv_satellite_longitude;
    @BindView(R.id.sp_longitude_type)
    Spinner sp_longitude_type;
    @BindView(R.id.et_satellite_longitude)
    EditText et_satellite_longitude;
    @BindView(R.id.tv_mobility_support)
    TextView tv_mobility_support;
    @BindView(R.id.tb_mobility_support_switch)
    ToggleButton tb_mobility_support_switch;
    @BindView(R.id.tv_aim_satellite_mode)
    TextView tv_aim_satellite_mode;
    @BindView(R.id.sp_aim_satellite_mode)
    ReSpinner sp_aim_satellite_mode;
    @BindView(R.id.tv_aim_satellite_frequency)
    TextView tv_aim_satellite_frequency;
    @BindView(R.id.et_aim_satellite_frequency)
    EditText et_aim_satellite_frequency;
    @BindView(R.id.ll_aim_satellite_frequency)
    LinearLayout ll_aim_satellite_frequency;
    @BindView(R.id.tv_zai_bo)
    TextView tv_zai_bo;
    @BindView(R.id.et_zai_bo_frequency)
    EditText et_zai_bo_frequency;
    @BindView(R.id.ll_zai_bo_rate)
    LinearLayout ll_zai_bo_rate;
    @BindView(R.id.tv_zai_bo_bandwidth)
    TextView tv_zai_bo_bandwidth;
    @BindView(R.id.sp_zai_bo_bandwidth)
    Spinner sp_zai_bo_bandwidth;
    @BindView(R.id.ll_zai_bo_bandwidth)
    LinearLayout ll_zai_bo_bandwidth;
    @BindView(R.id.tv_center_frequency)
    TextView tv_center_frequency;
    @BindView(R.id.et_center_frequency)
    EditText et_center_frequency;
    @BindView(R.id.ll_center_frequency)
    LinearLayout ll_center_frequency;
    @BindView(R.id.tv_symbol_rate)
    TextView tv_symbol_rate;
    @BindView(R.id.et_symbol_rate)
    EditText et_symbol_rate;
    @BindView(R.id.ll_symbol_rate)
    LinearLayout ll_symbol_rate;
    @BindView(R.id.tv_polarization_type)
    TextView tv_polarization_type;
    @BindView(R.id.sp_polarization_type)
    Spinner sp_polarization_type;
    @BindView(R.id.bt_antenna_stop)
    ButtonBgUi bt_antenna_stop;
    @BindView(R.id.tv_location_type)
    TextView tv_location_type;
    @BindView(R.id.sp_location_type)
    Spinner sp_location_type;
    @BindView(R.id.tv_current_lon)
    TextView tv_current_lon;
    @BindView(R.id.sp_current_lon_type)
    Spinner sp_current_lon_type;
    @BindView(R.id.et_current_lon)
    EditText et_current_lon;
    @BindView(R.id.tv_current_lat)
    TextView tv_current_lat;
    @BindView(R.id.sp_current_lat_type)
    Spinner sp_current_lat_type;
    @BindView(R.id.et_current_lat)
    EditText et_current_lat;
    @BindView(R.id.tv_rth)
    TextView tv_rth;
    @BindView(R.id.et_rth)
    EditText et_rth;
    @BindView(R.id.tv_rsd)
    TextView tv_rsd;
    @BindView(R.id.et_rsd)
    EditText et_rsd;
    @BindView(R.id.tv_pitch_compensation_angle)
    TextView tv_pitch_compensation_angle;
    @BindView(R.id.et_pitch_compensation_angle)
    EditText et_pitch_compensation_angle;
    @BindView(R.id.tv_lnb_local_oscillator)
    TextView tv_lnb_local_oscillator;
    @BindView(R.id.et_lnb_local_oscillator)
    EditText et_lnb_local_oscillator;
    @BindView(R.id.tv_buc)
    TextView tv_buc;
    @BindView(R.id.sp_buc_type)
    Spinner sp_buc_type;
    @BindView(R.id.ll_buc_type)
    LinearLayout ll_buc_type;
    @BindView(R.id.tv_buc_switch)
    TextView tv_buc_switch;
    @BindView(R.id.tb_buc_switch)
    ToggleButton tb_buc_switch;
    @BindView(R.id.ll_buc_switch)
    LinearLayout ll_buc_switch;
    @BindView(R.id.tv_buc_local_oscillator)
    TextView tv_buc_local_oscillator;
    @BindView(R.id.et_buc_local_oscillator)
    EditText et_buc_local_oscillator;
    @BindView(R.id.ll_buc_local_oscillator)
    LinearLayout ll_buc_local_oscillator;
    @BindView(R.id.tv_buc_gain_attenuation)
    TextView tv_buc_gain_attenuation;
    @BindView(R.id.et_buc_gain_attenuation)
    EditText et_buc_gain_attenuation;
    @BindView(R.id.ll_buc_gain_attenuation)
    LinearLayout ll_buc_gain_attenuation;
    @BindView(R.id.tv_network_detection)
    TextView tv_network_detection;
    @BindView(R.id.tb_network_detection_switch)
    ToggleButton tb_network_detection_switch;
    @BindView(R.id.ll_network_detection)
    LinearLayout ll_network_detection;
    @BindView(R.id.tv_debug_model)
    TextView tv_debug_model;
    @BindView(R.id.iv_right_arrow)
    ImageView iv_right_arrow;
    @BindView(R.id.ll_debug_model)
    LinearLayout ll_debug_model;
    @BindView(R.id.scrollview_compass)
    ScrollView scrollview_compass;
    @BindView(R.id.bt_save)
    ButtonBgUi bt_save;
    @BindView(R.id.bt_aim_satellite)
    ButtonBgUi bt_aim_satellite;
    private final static String TAG = "Activity---->";
    public static final String ZAIBO_AUTO = "3";
    private static final String XING_BIAO = "0";
    private static final String ZAIBO_MANUAL = "2";
    private static final String DVB = "1";
    private RequestQueue mRequestQueue;
    private SntAsyncHttpGet advgettask;
    private SntAsyncPost agrtask;
    private String mSate = "";
    private String starGetMode, starMode, mPing;
    private String
            strSatelliteNum,
            strBucSwitch,
            strAmipSwitch;
    private HashMap<String, Toast> toaHashMap = new HashMap<>(89);
    private int messageDelay = 5000;
    private PopupWindow pWindow;
    private ListView starListView;
    private List<StarCodeModel> starCodeModels = new ArrayList<>();
    private List<StarCodeModel> searchStarModels = new ArrayList<>();
    private List<LnbDataModel> lnbModels = new ArrayList<>();
    private List<BucDataModel> bucModels = new ArrayList<>();
    private CommonAdapter<StarCodeModel> starAdapter;
    private CommonAdapter<LnbDataModel> lnbAdapter;
    private CommonAdapter<BucDataModel> bucAdapter;
    private MyTextWatcher myTextWatcher;
    protected StarCodeModel currentStar;
    protected LnbDataModel lnbDataModel;
    protected BucDataModel bucDataModel;
    private String mType, mToken;
    private MyStarModeOnItemSelectedListener myStarModeOnItemSelectedListener;
    private MyOnTouchListener myOnTouchListener;
    private MyLnbOnTouchListener myLnbOnTouchListener;
    private MyBucOnTouchListener myBucOnTouchListener;
    private String amipStatus;
    private SuperSetRefActivity mContext;
    private AppData application;
    private PopupWindow pWindowLNB;
    private PopupWindow pWindowBUC;
    private ListView listViewLNB;
    private ListView listViewBUC;
    private UpdateUIBroadcastReceiver broadcastReceiver;
    private HttpUtils http;
    private boolean isVisible;
    private ArrayList<String> lastWarnList;
    private WarnDialogFragment warnDialogFragment;
    private FragmentManager supportFragmentManager;
    private BaseNiceDialog addStarDialog;
    private BaseNiceDialog addBUCDialog;
    private BaseNiceDialog addLNBDialog;
    private BaseNiceDialog bUCDelDialog;
    private BaseNiceDialog starDelDialog;
    private BaseNiceDialog lNBDelDialog;
    private BucDataModel bUCDelItem;
    private StarCodeModel startItem;
    private LnbDataModel lnbDataItem;
    private int bUCDelItemPosition;
    private int startItemPosition;
    private int lnbDataItemPosition;
    private Unbinder bind;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                queryCompassResult();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.superset_ref_activity);
        bind = ButterKnife.bind(this);
        mContext = this;
        application = (AppData) getApplication();
        if (AppData.accountModel != null) {
            mToken = AppData.accountModel.getSessionToken();
        }
        mType = getIntent().getStringExtra("compass");
        initNetWork();
        initView();
        initData();
        initEvent();
        initSatelliteData();
        initLnbList();
        initBucList();
        querySessionStatus(new OnSessionStatusListener() {
            @Override
            public void sessionSuccess() {
                doGetAntennaParameters();//从服务器获取下面参数
                doCheckPing();
            }
        });
        doCheckOpenamip();

    }

    /**
     * 初始化卫星相关的数据
     */
    private void initSatelliteData() {
        String savedStar = SharedPreferenceManager.getString(mContext,
                "currentStar");
        if (savedStar != null) {
            currentStar = GsonUtils.fromJson(savedStar, StarCodeModel.class);
            if (currentStar != null) {
                try {
                    if (("1").equals(currentStar.getAmipSwitch())) {
                        settingView(false);
                    }
                    initDataView();
                    initStarList();
                } catch (Exception e) {
                    initDataView();
                    initStarList();
                    e.printStackTrace();
                }
            }
        } else {
            initStarList();
        }
    }

    private void initNetWork() {
        http = new HttpUtils();
        http.configTimeout(15000);
        http.configSoTimeout(15000);
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    private void initData() {
        //卫星经度增加下拉框  20170105 hyw added
        sp_longitude_type.setAdapter(new MySpinnerAdapter(this,
                R.layout.spinner_stytle, getResources().getStringArray(R.array.e_w)));
        sp_aim_satellite_mode.setAdapter(new MySpinnerAdapter(this,
                R.layout.spinner_stytle, getResources().getStringArray(R.array.vsat_mode)));
        sp_zai_bo_bandwidth.setAdapter(new MySpinnerAdapter(this,
                R.layout.spinner_stytle, getResources().getStringArray(R.array.ref_bandwidth)));

        sp_polarization_type.setAdapter(new MySpinnerAdapter(this,
                R.layout.spinner_stytle, getResources().getStringArray(R.array.receive_polarization)));
        sp_location_type.setAdapter(new MySpinnerAdapter(this,
                R.layout.spinner_stytle, getResources().getStringArray(R.array.advpar_locatemode)));
        sp_buc_type.setAdapter(new MySpinnerAdapter(this,
                R.layout.spinner_stytle, getResources().getStringArray(R.array.buc_type_mode)));
        sp_buc_type.setEnabled(false);
        //当前经度  20170105hyw added
        sp_current_lon_type.setAdapter(new MySpinnerAdapter(this,
                R.layout.spinner_stytle, getResources().getStringArray(R.array.e_w)));
        //当前维度  20170105hyw added
        sp_current_lat_type.setAdapter(new MySpinnerAdapter(this,
                R.layout.spinner_stytle, getResources().getStringArray(R.array.n_s)));
        if ("C系列".equals(mType)) {
            ll_compass.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
        initToasts();
        // 动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("ServiceUpdateUI");
        broadcastReceiver = new UpdateUIBroadcastReceiver();
        registerReceiver(broadcastReceiver, filter);
    }

    class UpdateUIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(mContext, "onReceive", 0).show();
            if (AppData.accountModel != null) {
                doDevstatu();
            }

        }


    }

    private void doDevstatu() {
        try {
            RequestParams params = new RequestParams("UTF-8");
            JSONObject jsonObjet = new JSONObject();
            jsonObjet.put("sessionToken", AppData.accountModel.getSessionToken());
            params.setBodyEntity(new StringEntity(jsonObjet.toString(), "UTF-8"));
            params.setContentType("applicatin/json");
            http.send(HttpRequest.HttpMethod.POST,
                    XTHttpUtil.devstatu, params, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            //Toast.makeText(mContext, responseInfo.result, 1).show();
                            if(!isVisible){
                                return;
                            }
                            DevStatusBean devStatusBean = GsonUtils.fromJson(responseInfo.result, DevStatusBean.class);
                            if (devStatusBean != null) {
                                String code = devStatusBean.getCode();
                                if ("0".equals(code)) {
                                    ArrayList<String> warnList = new ArrayList<>();
                                    initWarnList(devStatusBean, warnList);
                                    if (warnList.size() > 0) {
                                        if (!isFinishing() && isVisible) {
                                            if (lastWarnList != null && lastWarnList.equals(warnList)) {
                                                boolean isNoRead = SharedPreferenceManager.getBoolean(mContext, "isNoRead");
                                                if (isNoRead) {
                                                    showWarnDialogFragment(warnList);
                                                }
                                            } else {
                                                SharedPreferenceManager.saveBoolean(mContext, "isNoRead", true);
                                                showWarnDialogFragment(warnList);
                                            }
                                            lastWarnList = warnList;
                                        }
                                    }
                                } else if ("-2".equals(code)) {
                                    //if (!isFinishing() && isVisible) {
                                    //Toast.makeText(mContext, devStatusBean.getMsg(), Toast.LENGTH_LONG).show();

                                }
                            }


                        }

                        @Override
                        public void onFailure(HttpException arg0, String arg1) { //
                            //TODO Auto-generated method stub

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showWarnDialogFragment(ArrayList<String> warnList) {
        if (!isFinishing() && isVisible) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("value", warnList);
            if (!warnDialogFragment.isAdded()) {
                warnDialogFragment.setArguments(bundle);
            }
            if (!warnDialogFragment.isVisible()) {
                warnDialogFragment.show(supportFragmentManager, "");
            } else {
                warnDialogFragment.reFreshData(bundle);
            }

        }

    }

    /**
     * 初始化 错误信息
     *
     * @param devStatusBean
     * @param warnList
     */
    private void initWarnList(DevStatusBean devStatusBean, List<String> warnList) {
        String gl = devStatusBean.getGeneral();
        if ("-1".equals(gl)) {
            warnList.add(getString(R.string.warn_gl));
        }
        String current = devStatusBean.getCurrent();
        if ("-1".equals(current)) {
            warnList.add(getString(R.string.wran_current));
        }
        String hot = devStatusBean.getHot();
        if ("-1".equals(hot)) {
            warnList.add(getString(R.string.warn_hot));
        }
        String voltage = devStatusBean.getVoltage();
        if ("-1".equals(voltage)) {
            warnList.add(getString(R.string.warn_voltage));
        }
        String search = devStatusBean.getSearch();
        if ("-1".equals(search)) {
            warnList.add(getString(R.string.warn_search));
        }
        String oriemotor = devStatusBean.getOrimotor();
        if ("-1".equals(oriemotor)) {
            warnList.add(getString(R.string.warn_oriemotor));
        }
        String sendzero = devStatusBean.getSendzero();
        if ("-1".equals(sendzero)) {
            warnList.add(getString(R.string.warn_send_zero));
        }
        String pitchmotor = devStatusBean.getPitchmotor();
        if ("-1".equals(pitchmotor)) {
            warnList.add(getString(R.string.warn_pitchmotor));
        }
        String rollzero = devStatusBean.getRollzero();
        if ("-1".equals(rollzero)) {
            warnList.add(getString(R.string.warn_rollzero));
        }
        String oriezero = devStatusBean.getOrizero();
        if ("-1".equals(oriezero)) {
            warnList.add(getString(R.string.wran_oriezero));
        }
        String gps = devStatusBean.getGps();
        if ("-1".equals(gps)) {
            warnList.add(getString(R.string.warn_gps));
        }
        String rf = devStatusBean.getRf();
        if ("-1".equals(rf)) {
            warnList.add(getString(R.string.warn_rf));
        }
        String pitchzero = devStatusBean.getPitchzero();
        if ("-1".equals(pitchzero)) {
            warnList.add(getString(R.string.warn_pitchzero));
        }
        String sensor = devStatusBean.getSensor();
        if ("-1".equals(sensor)) {
            String odType = devStatusBean.getOdutype();
            if (odType.contains("7") || odType.equals("8")) {
                warnList.add(getString(R.string.warn_dzlp));
            } else {
                warnList.add(getString(R.string.warn_tly));
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int[] location = new int[2];
        int[] lnbLocation = new int[2];
        int[] bucLocation = new int[2];
        et_satellite_number.getLocationOnScreen(location);
        et_lnb_local_oscillator.getLocationOnScreen(lnbLocation);
        et_buc_local_oscillator.getLocationOnScreen(bucLocation);
        int x = location[0];
        int y = location[1];
        int lnbX = lnbLocation[0];
        int lnbY = lnbLocation[1];
        int bucX = lnbLocation[0];
        int bucY = lnbLocation[1];
        if (event.getX() < x || event.getX() > (x + et_satellite_number.getWidth()) || event.getY() < y || event.getY() > (y + et_satellite_number.getHeight())) {
            pWindow.dismiss();

        }
        if (event.getX() < lnbX || event.getX() > (lnbX + et_lnb_local_oscillator.getWidth()) || event.getY() < lnbY || event.getY() > (lnbY + et_lnb_local_oscillator.getHeight())) {
            pWindowLNB.dismiss();

        }
        if (event.getX() < bucX || event.getX() > (bucX + et_buc_local_oscillator.getWidth()) || event.getY() < bucY || event.getY() > (bucY + et_buc_local_oscillator.getHeight())) {
            pWindowBUC.dismiss();

        }
        return super.dispatchTouchEvent(event);
    }

    private void initEvent() {
        tb_network_detection_switch.setOnCheckedChangeListener(this);
        tb_buc_switch.setOnCheckedChangeListener(this);
        tb_mobility_support_switch.setOnCheckedChangeListener(this);
        bt_aim_satellite.setOnClickListener(this);
        bt_save.setOnClickListener(this);
        bt_save_default.setOnClickListener(this);
        ll_debug_model.setOnClickListener(this);
        bt_antenna_stop.setOnClickListener(this);
        bt_compass.setOnClickListener(this);
        myOnTouchListener = new MyOnTouchListener();
        myLnbOnTouchListener = new MyLnbOnTouchListener();
        myBucOnTouchListener = new MyBucOnTouchListener();
        et_satellite_number.setOnTouchListener(myOnTouchListener);
        et_lnb_local_oscillator.setOnTouchListener(myLnbOnTouchListener);
        et_buc_local_oscillator.setOnTouchListener(myBucOnTouchListener);
        myTextWatcher = new MyTextWatcher();
        et_satellite_number.addTextChangedListener(myTextWatcher);
        myStarModeOnItemSelectedListener = new MyStarModeOnItemSelectedListener();
        sp_aim_satellite_mode.setOnItemSelectedListener(myStarModeOnItemSelectedListener);
        rl_top_bar.setOnTouchListener(new ComBackTouchListener());
        et_current_lat.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) {
                    if ("--".equals(et_current_lat.getText().toString())) {
                        et_current_lat.setText("");
                    }
                }

            }
        });
        et_current_lat.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                    Toast.makeText(SuperSetRefActivity.this, "对不起，您只能输入两位小数！", Toast.LENGTH_LONG).show();
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
        et_current_lon.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) {
                    if ("--".equals(et_current_lon.getText().toString())) {
                        et_current_lon.setText("");
                    }
                }

            }
        });
        et_current_lon.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                    Toast.makeText(SuperSetRefActivity.this, "对不起，您只能输入两位小数！", Toast.LENGTH_LONG).show();
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
        et_rth.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) {
                    if ("--".equals(et_rth.getText().toString())) {
                        et_rth.setText("");
                    }
                }

            }
        });
        et_rsd.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) {
                    if ("--".equals(et_rsd.getText().toString())) {
                        et_rsd.setText("");
                    }
                }

            }
        });
        et_buc_gain_attenuation.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) {
                    if ("--".equals(et_buc_gain_attenuation.getText().toString())) {
                        et_buc_gain_attenuation.setText("");
                    }
                }

            }
        });
        et_aim_satellite_frequency.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                    Toast.makeText(SuperSetRefActivity.this, "对不起，您只能输入两位小数！", Toast.LENGTH_LONG).show();
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
        et_zai_bo_frequency.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                    Toast.makeText(SuperSetRefActivity.this, "对不起，您只能输入两位小数！", Toast.LENGTH_LONG).show();
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
        et_pitch_compensation_angle.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1) {
                    if ("--".equals(et_pitch_compensation_angle.getText().toString())) {
                        et_pitch_compensation_angle.setText("");
                    }
                }

            }
        });
        et_pitch_compensation_angle.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                    Toast.makeText(SuperSetRefActivity.this, "对不起，您只能输入两位小数！", Toast.LENGTH_LONG).show();
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
        et_aim_satellite_frequency.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) {
                    if ("--".equals(et_aim_satellite_frequency.getText().toString())) {
                        et_aim_satellite_frequency.setText("");
                    }
                }

            }
        });

        et_zai_bo_frequency.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) {
                    if ("--".equals(et_zai_bo_frequency.getText().toString())) {
                        et_zai_bo_frequency.setText("");
                    }
                }

            }
        });
        et_center_frequency.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) {
                    if ("--".equals(et_center_frequency.getText().toString())) {
                        et_center_frequency.setText("");
                    }
                }

            }
        });
        et_symbol_rate.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) {
                    if ("--".equals(et_symbol_rate.getText().toString())) {
                        et_symbol_rate.setText("");
                    }
                }

            }
        });
    }

    public class ComBackTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float downX = event.getX();
            float downY = event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int maxX = MyDensityUtil.dip2px(130);
                int maxY = MyDensityUtil.dip2px(getResources().getDimension(R.dimen.bar_height));
                if (downX <= maxX && downY < maxY) {
                    application.removeAct(mContext);
                    finish();
                }
            }
            return false;
        }
    }

    protected void showDelDialog(final StarCodeModel starCodeModel) {
        starDelDialog = ConfirmCancelDialog.init()
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        holder.setText(R.id.tv_tip, "确定删除卫星");
                        holder.setOnClickListener(R.id.bt_ok, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                starCodeModel.delete();
                                boolean remove = searchStarModels.remove(starCodeModel);
                                if (!remove) {
                                    searchStarModels.remove(startItemPosition);
                                }
                                boolean remove1 = starCodeModels.remove(starCodeModel);
                                if (!remove1) {
                                    starCodeModels.remove(startItemPosition);
                                }
                                starAdapter.notifyDataSetChanged();
                            }
                        });
                        holder.setOnClickListener(R.id.bt_cancel, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .show(getSupportFragmentManager());
    }

    protected void showLBNDelDialog(final LnbDataModel lnbDataModel) {
        lNBDelDialog = ConfirmCancelDialog.init()
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        holder.setText(R.id.tv_tip, "确定删除" + lnbDataModel.getLnb() + "值");
                        holder.setOnClickListener(R.id.bt_ok, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                lnbDataModel.delete();
                                boolean remove = lnbModels.remove(lnbDataModel);
                                if (!remove) {
                                    lnbModels.remove(lnbDataItemPosition);
                                }
                                lnbAdapter.notifyDataSetChanged();
                                setListViewHeight(listViewLNB, lnbAdapter);
                            }
                        });
                        holder.setOnClickListener(R.id.bt_cancel, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .show(getSupportFragmentManager());
    }

    protected void showBUCDelDialog(final BucDataModel bucDataModel) {
        bUCDelDialog = ConfirmCancelDialog.init()
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        holder.setText(R.id.tv_tip, "确定删除" + bucDataModel.getBucOscillator() + "值");
                        holder.setOnClickListener(R.id.bt_ok, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                bucDataModel.delete();
                                boolean remove = bucModels.remove(bucDataModel);
                                if (!remove) {
                                    bucModels.remove(bUCDelItemPosition);
                                }
                                bucAdapter.notifyDataSetChanged();
                                setListViewHeight(listViewBUC, bucAdapter);
                            }
                        });
                        holder.setOnClickListener(R.id.bt_cancel, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .show(getSupportFragmentManager());
    }

    class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            searchStarModels.clear();
            if ("".equals(s.toString())) {
                searchStarModels.addAll(starCodeModels);
            } else {
                for (StarCodeModel temp : starCodeModels) {
                    if (temp.getSatename().contains(s.toString())) {
                        searchStarModels.add(temp);
                    }
                }
            }
          //  getRemoveText();
            setListViewHeight(starListView, starAdapter);
            starAdapter.notifyDataSetChanged();
            if (pWindow != null && !pWindow.isShowing()) {
                pWindow.showAsDropDown(et_satellite_number);
            }

        }



        @Override
        public void afterTextChanged(Editable s) {

        }

    }
    private void getRemoveText() {
        et_satellite_number.removeTextChangedListener(myTextWatcher);
        if (searchStarModels.size() == 0||"--".equals(et_satellite_number.getText().toString())) {
            et_satellite_number.setText("");
        }
        et_satellite_number.addTextChangedListener(myTextWatcher);
    }
    private void setDuixingType() {
        String mode = currentStar.getMode();
        if (StringUtils.isEmpty(mode)) {
            sp_aim_satellite_mode.setSelection(0);
            initXinBiaoView();
            return;
        }
        if (Integer.parseInt(mode) <= 2) {
            sp_aim_satellite_mode.setSelection(Integer.parseInt(mode));
        }
        switch (mode) {
            case XING_BIAO:
                initXinBiaoView();
                break;
            case ZAIBO_MANUAL:
                initZaiBoView();
                break;
            case DVB:
                initDVBView();
                break;

        }
    }


    private void setSateLon() {
        String _satelon = ChechIpMask.numDigite(currentStar.getSatelon(), 1);//保留一位有效数字。
        //SharedPreferenceManager.saveString(mContext, "satelon", _satelon);
        //如果带负号
        if (_satelon.substring(0, 1).equals("-") || _satelon.substring(0, 1).equals("﹣") || _satelon.substring(0, 1).equals("－") || _satelon.substring(0, 1).equals("﹣")) {

            _satelon = _satelon.substring(1);
            sp_longitude_type.setSelection(1);
        } else {
            sp_longitude_type.setSelection(0);
        }
        et_satellite_longitude.setText(_satelon);
    }

    protected void showAddDialog() {
        addStarDialog = NiceDialog.init().setLayoutId(R.layout.dialog_add_satellite_layout)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        final Spinner super_ref_mdqjd1 = holder.getView(R.id.super_ref_mdqjd1);// 天线类型
                        super_ref_mdqjd1.setAdapter(new MySpinnerAdapter(mContext,
                                R.layout.spinner_stytle, getResources().getStringArray(R.array.e_w)));
                        super_ref_mdqjd1.setSelection(0);
                        final EditText et_star_name = holder.getView(R.id.et_star_name);
                        ViewUtil.setEditTextInhibitInputSpeChat(et_star_name, 16);
                        et_star_name.setOnFocusChangeListener(new OnFocusChangeListener() {

                            @Override
                            public void onFocusChange(View arg0, boolean arg1) {
                                // TODO Auto-generated method stub
                                if (arg1) {
                                    if ("--".equals(et_star_name.getText().toString())) {
                                        et_star_name.setText("");
                                    }
                                }

                            }
                        });
                        final EditText mCurrentLatitude1 = holder.getView(R.id.et_star_lat_lng);
                        mCurrentLatitude1.setOnFocusChangeListener(new OnFocusChangeListener() {

                            @Override
                            public void onFocusChange(View arg0, boolean arg1) {
                                if (arg1) {
                                    if ("--".equals(mCurrentLatitude1.getText().toString())) {
                                        mCurrentLatitude1.setText("");
                                    }
                                }

                            }
                        });
                        holder.setOnClickListener(R.id.bt_cancel, new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        holder.setOnClickListener(R.id.bt_ok, new OnClickListener() {
                            public String strCurrentLongItude1;

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                String sateName = et_star_name.getText().toString();
                                if ((!"".equals(sateName)) && (!"--".equals(sateName))) {
                                    if (ChechIpMask.a2b(mCurrentLatitude1.getText().toString(), 0, 180)) {
                                        String _strCurrentLongItude1 = ChechIpMask.numDigite(mCurrentLatitude1.getText().toString(), 2);
                                        if (super_ref_mdqjd1.getSelectedItemPosition() == 1) {
                                            _strCurrentLongItude1 = "-" + _strCurrentLongItude1;
                                            strCurrentLongItude1 = _strCurrentLongItude1;
                                        } else if (super_ref_mdqjd1.getSelectedItemPosition() == 0) {
                                            strCurrentLongItude1 = _strCurrentLongItude1;
                                        }
                                        StarCodeModel s1 = new StarCodeModel();
                                        s1.setSatename(sateName);
                                        s1.setSatelon(strCurrentLongItude1);
                                        s1.setAdd(true);
                                        s1.save();
                                        searchStarModels.add(searchStarModels.size(), s1);
                                        starCodeModels.add(starCodeModels.size(), s1);
                                        starAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(SuperSetRefActivity.this, "对不起，您输入的经度不合法！", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(SuperSetRefActivity.this, "卫星编号不能为空", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                })
                .show(getSupportFragmentManager());
    }

    private void initStarList() {
        pWindow = new PopupWindow();
        pWindow.setWidth(LayoutParams.WRAP_CONTENT);
        pWindow.setHeight(LayoutParams.WRAP_CONTENT);
        pWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        pWindow.setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_PAN);
        starListView = new ListView(this);
        setSearchStarModels();
        starAdapter = new CommonAdapter<StarCodeModel>(this, R.layout.item_star_info, searchStarModels) {

            @Override
            protected void fillItemData(CommonViewHolder viewHolder,
                                        int position, StarCodeModel item) {
                viewHolder.setTextForTextView(R.id.tv_star_name, item.getSatename());
				/*if(searchStarModels.size()>12){
					isAddStar=true;
				}*/

            }
        };
        starListView.setAdapter(starAdapter);
        pWindow.setBackgroundDrawable(new ColorDrawable());
        //pWindow.setOutsideTouchable(true);
        final LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setBackgroundResource(R.drawable.shape_bottom_corner__gray_bg);
        ll.addView(starListView);
        View adView = getLayoutInflater().inflate(R.layout.item_star_info, null);
        adView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });
        TextView tv_star_name = (TextView) adView.findViewById(R.id.tv_star_name);
        tv_star_name.setText("添加更多");
		/*View lineView=new View(mContext);
		lineView.setBackgroundColor(getResources().getColor(R.color.white));
		ll.addView(lineView);
		LinearLayout.LayoutParams lineParms=(android.widget.LinearLayout.LayoutParams) lineView.getLayoutParams();
		lineParms.height=ScreenUtil.dip2px(mContext, 1);
		lineParms.width=LinearLayout.LayoutParams.MATCH_PARENT;*/
        ll.addView(adView);
        adView.getLayoutParams().height = ScreenUtil.dip2px(mContext, 42);
        pWindow.setContentView(ll);
        et_satellite_number.post(new Runnable() {
            @Override
            public void run() {
                setListViewHeight(starListView, starAdapter);
                pWindow.setWidth(et_satellite_number.getWidth());
            }
        });

        starListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                CommonAdapter<StarCodeModel> adapter = (CommonAdapter) parent.getAdapter();
                startItem = adapter.getItem(position);
                startItemPosition = position;
                if (startItem.isAdd() && (position > 43)) {
                    showDelDialog(startItem);
                }
                return false;
            }
        });
        starListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                strSatelliteNum = String.valueOf(position + 1);
                CommonAdapter<StarCodeModel> adapter = (CommonAdapter) parent.getAdapter();
                currentStar = adapter.getItem(position);
                setSateLon();
                setJiHuaType();
                setDuixingType();
                pWindow.dismiss();
                setSatelliteNumberText(currentStar.getSatename());
            }
        });

    }

    private void initLnbList() {
        pWindowLNB = new PopupWindow();
        pWindowLNB.setWidth(LayoutParams.WRAP_CONTENT);
        pWindowLNB.setHeight(LayoutParams.WRAP_CONTENT);
        pWindowLNB.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        pWindowLNB.setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_PAN);
        listViewLNB = new ListView(this);
        initLnbData();
        lnbAdapter = new CommonAdapter<LnbDataModel>(this, R.layout.item_star_info, lnbModels) {

            @Override
            protected void fillItemData(CommonViewHolder viewHolder,
                                        int position, LnbDataModel item) {
                viewHolder.setTextForTextView(R.id.tv_star_name, item.getLnb());

            }
        };
        listViewLNB.setAdapter(lnbAdapter);
        pWindowLNB.setBackgroundDrawable(new ColorDrawable());
        final LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setBackgroundResource(R.drawable.shape_bottom_corner__gray_bg);
        ll.addView(listViewLNB);
        View adView = getLayoutInflater().inflate(R.layout.item_star_info, null);
        adView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showAddLNBDialog();
            }
        });
        TextView tv_star_name = (TextView) adView.findViewById(R.id.tv_star_name);
        tv_star_name.setText("添加更多");
        ll.addView(adView);
        adView.getLayoutParams().height = ScreenUtil.dip2px(mContext, 42);
        pWindowLNB.setContentView(ll);
        et_lnb_local_oscillator.post(new Runnable() {
            @Override
            public void run() {
                setListViewHeight(listViewLNB, lnbAdapter);
                pWindowLNB.setWidth(et_lnb_local_oscillator.getWidth());
            }
        });

        listViewLNB.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                CommonAdapter<LnbDataModel> adapter = (CommonAdapter) parent.getAdapter();
                lnbDataItem = adapter.getItem(position);
                lnbDataItemPosition = position;
                if (lnbDataItem.isAdd()) {
                    showLBNDelDialog(lnbDataItem);
                }
                return false;
            }
        });
        listViewLNB.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // strSatelliteNum=String.valueOf(position+1);
                //et_lnb_local_oscillator.removeTextChangedListener(myLnbTextWatcher);
                CommonAdapter<LnbDataModel> adapter = (CommonAdapter) parent.getAdapter();
                lnbDataModel = adapter.getItem(position);
                pWindowLNB.dismiss();
                et_lnb_local_oscillator.setText(lnbDataModel.getLnb());
                et_lnb_local_oscillator.setSelection(lnbDataModel.getLnb().length());
                //et_lnb_local_oscillator.addTextChangedListener(myLnbTextWatcher);


            }
        });

    }

    private void showAddLNBDialog() {
        addLNBDialog = NiceDialog.init().setLayoutId(R.layout.dialog_add_local_oscillator_layout)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        final EditText et_lnb_local_oscillator = holder.getView(R.id.et_lnb_local_oscillator);
                        et_lnb_local_oscillator.setOnFocusChangeListener(new OnFocusChangeListener() {

                            @Override
                            public void onFocusChange(View arg0, boolean arg1) {
                                // TODO Auto-generated method stub
                                if (arg1) {
                                    if ("--".equals(et_lnb_local_oscillator.getText().toString())) {
                                        et_lnb_local_oscillator.setText("");
                                    }
                                }

                            }
                        });
                        holder.setOnClickListener(R.id.bt_cancel, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        holder.setOnClickListener(R.id.bt_ok, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (StringUtils.isEmpty(et_lnb_local_oscillator.getText().toString())) {
                                    Toast.makeText(mContext, "输入不能为空，请重新输入！", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                LnbDataModel lnbDataModel = new LnbDataModel();
                                lnbDataModel.setLnb(et_lnb_local_oscillator.getText().toString());
                                lnbDataModel.setAdd(true);
                                List<LnbDataModel> lnbDataModels = DataSupport.findAll(LnbDataModel.class);
                                if (lnbDataModels != null) {
                                    boolean contains = lnbDataModels.contains(lnbDataModel);
                                    if (!contains) {
                                        lnbDataModel.save();
                                        lnbModels.add(lnbDataModel);
                                        lnbAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    lnbDataModel.save();
                                    lnbModels.add(lnbDataModel);
                                    lnbAdapter.notifyDataSetChanged();
                                }

                            }
                        });
                    }
                })
                .show(getSupportFragmentManager());
    }

    private void initBucList() {
        pWindowBUC = new PopupWindow();
        pWindowBUC.setWidth(LayoutParams.WRAP_CONTENT);
        pWindowBUC.setHeight(LayoutParams.WRAP_CONTENT);
        pWindowBUC.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        pWindowBUC.setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_PAN);
        listViewBUC = new ListView(this);
        initBucData();
        bucAdapter = new CommonAdapter<BucDataModel>(this, R.layout.item_star_info, bucModels) {

            @Override
            protected void fillItemData(CommonViewHolder viewHolder,
                                        int position, BucDataModel item) {
                viewHolder.setTextForTextView(R.id.tv_star_name, item.getBucOscillator());

            }
        };
        listViewBUC.setAdapter(bucAdapter);
        pWindowBUC.setBackgroundDrawable(new ColorDrawable());
        final LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setBackgroundResource(R.drawable.shape_bottom_corner__gray_bg);
        ll.addView(listViewBUC);
        View adView = getLayoutInflater().inflate(R.layout.item_star_info, null);
        adView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showAddBUCDialog();
            }
        });
        TextView tv_star_name = (TextView) adView.findViewById(R.id.tv_star_name);
        tv_star_name.setText("添加更多");
        ll.addView(adView);
        adView.getLayoutParams().height = ScreenUtil.dip2px(mContext, 42);
        pWindowBUC.setContentView(ll);
        et_buc_local_oscillator.post(new Runnable() {
            @Override
            public void run() {
                setListViewHeight(listViewBUC, bucAdapter);
                pWindowBUC.setWidth(et_buc_local_oscillator.getWidth());
            }
        });

        listViewBUC.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                CommonAdapter<BucDataModel> adapter = (CommonAdapter) parent.getAdapter();
                bUCDelItem = adapter.getItem(position);
                bUCDelItemPosition = position;
                if (bUCDelItem.isAdd()) {
                    showBUCDelDialog(bUCDelItem);
                }
                return false;
            }
        });
        listViewBUC.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // strSatelliteNum=String.valueOf(position+1);
                CommonAdapter<BucDataModel> adapter = (CommonAdapter) parent.getAdapter();
                bucDataModel = adapter.getItem(position);
                pWindowBUC.dismiss();
                et_buc_local_oscillator.setText(bucDataModel.getBucOscillator());
                et_buc_local_oscillator.setSelection(bucDataModel.getBucOscillator().length());


            }
        });

    }

    private void showAddBUCDialog() {
        addBUCDialog = NiceDialog.init().setLayoutId(R.layout.dialog_add_buc_layout)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        final EditText et_buc = holder.getView(R.id.et_buc);
                        et_buc.setOnFocusChangeListener(new OnFocusChangeListener() {

                            @Override
                            public void onFocusChange(View arg0, boolean arg1) {
                                // TODO Auto-generated method stub
                                if (arg1) {
                                    if ("--".equals(et_buc.getText().toString())) {
                                        et_buc.setText("");
                                    }
                                }

                            }
                        });
                        holder.setOnClickListener(R.id.bt_cancel, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        holder.setOnClickListener(R.id.bt_ok, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (StringUtils.isEmpty(et_buc.getText().toString())) {
                                    Toast.makeText(mContext, "输入不能为空，请重新输入！", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                BucDataModel bucDataModel = new BucDataModel();
                                bucDataModel.setBucOscillator(et_buc.getText().toString());
                                bucDataModel.setAdd(true);
                                List<BucDataModel> bucDataModels = DataSupport.findAll(BucDataModel.class);
                                if (bucDataModels != null) {
                                    boolean contains = bucDataModels.contains(bucDataModel);
                                    if (!contains) {
                                        bucDataModel.save();
                                        bucModels.add(bucDataModel);
                                        bucAdapter.notifyDataSetChanged();
                                        setListViewHeight(listViewBUC, bucAdapter);
                                    }
                                } else {
                                    bucDataModel.save();
                                    bucModels.add(bucDataModel);
                                    bucAdapter.notifyDataSetChanged();
                                    setListViewHeight(listViewBUC, bucAdapter);
                                }

                            }
                        });
                    }
                })
                .show(getSupportFragmentManager());
    }

    private void initLnbData() {
        List<LnbDataModel> list = DataSupport.findAll(LnbDataModel.class);
        lnbModels.clear();
        lnbModels.addAll(list);
    }

    private void initBucData() {
        List<BucDataModel> list = DataSupport.findAll(BucDataModel.class);
        bucModels.clear();
        bucModels.addAll(list);
    }

    private void setSearchStarModels() {
        List<StarCodeModel> dbData = DataSupport.findAll(StarCodeModel.class);
        starCodeModels.clear();
        searchStarModels.clear();
        starCodeModels.addAll(dbData);
        searchStarModels.addAll(starCodeModels);

    }

    private void setJiHuaType() {
        try {
            sp_polarization_type.setSelection(Integer.parseInt(currentStar.getType()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    @SuppressLint("ShowToast")
    private void initToasts() {
        Toast toast = Toast.makeText(SuperSetRefActivity.this, "获取纬度成功！", Toast.LENGTH_LONG);
        Toast toast1 = Toast.makeText(getApplicationContext(), "获取天线参数失败", Toast.LENGTH_LONG);
        Toast toast2 = Toast.makeText(getApplicationContext(), "获取定位方式失败", Toast.LENGTH_LONG);
        Toast toast3 = Toast.makeText(getApplicationContext(), "获取定位方式成功", Toast.LENGTH_LONG);
        Toast toast4 = Toast.makeText(SuperSetRefActivity.this, "RTH门限查询成功！", Toast.LENGTH_LONG);
        Toast toast5 = Toast.makeText(SuperSetRefActivity.this, "RTH门限查询失败！", Toast.LENGTH_LONG);
        Toast toast6 = Toast.makeText(SuperSetRefActivity.this, "获取经度成功！", Toast.LENGTH_LONG);
        Toast toast7 = Toast.makeText(SuperSetRefActivity.this, "获取俯仰补偿角成功！", Toast.LENGTH_LONG);
        Toast toast8 = Toast.makeText(SuperSetRefActivity.this, "获取俯仰补偿角失败！", Toast.LENGTH_LONG);
        Toast toast9 = Toast.makeText(SuperSetRefActivity.this, "获取LNB成功！", Toast.LENGTH_LONG);
        Toast toast10 = Toast.makeText(SuperSetRefActivity.this, "获取LNB失败！", Toast.LENGTH_LONG);
        Toast toast11 = Toast.makeText(SuperSetRefActivity.this, "获取经度失败！", Toast.LENGTH_LONG);
        Toast toast12 = Toast.makeText(SuperSetRefActivity.this, "获取纬度失败！", Toast.LENGTH_LONG);
        Toast toast13 = Toast.makeText(SuperSetRefActivity.this, "网络检测开关状态查询失败", Toast.LENGTH_LONG);
        Toast toast14 = Toast.makeText(SuperSetRefActivity.this, "网络检测开关状态查询成功", Toast.LENGTH_LONG);
        Toast toast15 = Toast.makeText(SuperSetRefActivity.this, "网络检测开关状态设置成功", Toast.LENGTH_LONG);
        Toast toast16 = Toast.makeText(SuperSetRefActivity.this, "网络检测开关状态设置失败", Toast.LENGTH_LONG);
        Toast toast17 = Toast.makeText(SuperSetRefActivity.this, "俯仰补偿角设置成功", Toast.LENGTH_LONG);
        Toast toast18 = Toast.makeText(SuperSetRefActivity.this, "俯仰补偿角设置失败", Toast.LENGTH_LONG);
        Toast toast19 = Toast.makeText(SuperSetRefActivity.this, "经纬度设置失败", Toast.LENGTH_LONG);
        Toast toast20 = Toast.makeText(SuperSetRefActivity.this, "经纬度设置成功", Toast.LENGTH_LONG);
        Toast toast21 = Toast.makeText(SuperSetRefActivity.this, "定位方式设置成功", Toast.LENGTH_LONG);
        Toast toast22 = Toast.makeText(SuperSetRefActivity.this, "定位方式设置失败", Toast.LENGTH_LONG);
        Toast toast23 = Toast.makeText(SuperSetRefActivity.this, "RTH门限设置成功！", Toast.LENGTH_LONG);
        Toast toast24 = Toast.makeText(SuperSetRefActivity.this, "RTH门限设置失败！", Toast.LENGTH_LONG);
        Toast toast25 = Toast.makeText(SuperSetRefActivity.this, "LNB本振设置成功", Toast.LENGTH_LONG);
        Toast toast26 = Toast.makeText(SuperSetRefActivity.this, "LNB本振设置失败", Toast.LENGTH_LONG);
        Toast toast27 = Toast.makeText(getApplicationContext(), "连接网元服务器失败", Toast.LENGTH_LONG);
        Toast toast28 = Toast.makeText(getApplicationContext(), "保存失败,请检查设备！", Toast.LENGTH_LONG);
        Toast toast29 = Toast.makeText(getApplicationContext(), "星位参数保存成功", Toast.LENGTH_LONG);
        Toast toast30 = Toast.makeText(getApplicationContext(), "对不起，您输入的经度不合法！", Toast.LENGTH_LONG);
        Toast toast31 = Toast.makeText(getApplicationContext(), "对不起，您输入的纬度不合法！", Toast.LENGTH_LONG);
        Toast toast32 = Toast.makeText(getApplicationContext(), "对不起，您输入的俯仰补偿角不合法！", Toast.LENGTH_LONG);
        Toast toast33 = Toast.makeText(getApplicationContext(), "抱歉，接收到的RTH格式不正确！", Toast.LENGTH_LONG);
        Toast toast34 = Toast.makeText(getApplicationContext(), "抱歉，接收到的当前经度格式不正确！", Toast.LENGTH_LONG);
        Toast toast35 = Toast.makeText(getApplicationContext(), "抱歉，接收到的当前纬度格式不正确！", Toast.LENGTH_LONG);
        Toast toast36 = Toast.makeText(getApplicationContext(), "抱歉，接收到的俯仰补偿角格式不正确！", Toast.LENGTH_LONG);
        Toast toast37 = Toast.makeText(getApplicationContext(), "抱歉，接收到的经度格式不正确！", Toast.LENGTH_LONG);
        Toast toast38 = Toast.makeText(getApplicationContext(), "抱歉，接收到的频率格式不正确！", Toast.LENGTH_LONG);
        Toast toast39 = Toast.makeText(getApplicationContext(), "对不起，您输入的RTH不合法！", Toast.LENGTH_LONG);
        Toast toast40 = Toast.makeText(getApplicationContext(), "执行了保存！", Toast.LENGTH_LONG);
        Toast toast41 = Toast.makeText(getApplicationContext(), "电子罗盘初始化命令发送成功", Toast.LENGTH_LONG);
        Toast toast42 = Toast.makeText(getApplicationContext(), "电子罗盘初始化命令发送失败", Toast.LENGTH_LONG);
        Toast toast43 = Toast.makeText(getApplicationContext(), "初始化设置成功", Toast.LENGTH_LONG);
        Toast toast44 = Toast.makeText(getApplicationContext(), "正在执行电子罗盘初始化,请耐心等候！", Toast.LENGTH_LONG);
        Toast toast45 = Toast.makeText(getApplicationContext(), "任务未执行", Toast.LENGTH_LONG);
        Toast toast46 = Toast.makeText(getApplicationContext(), "初始化设置失败", Toast.LENGTH_LONG);
        Toast toast47 = Toast.makeText(getApplicationContext(), "电子罗盘校准命令发送成功", Toast.LENGTH_LONG);
        Toast toast48 = Toast.makeText(getApplicationContext(), "电子罗盘校准命令发送失败", Toast.LENGTH_LONG);
        Toast toast49 = Toast.makeText(getApplicationContext(), "电子罗盘校准成功", Toast.LENGTH_LONG);
        Toast toast50 = Toast.makeText(getApplicationContext(), "正在执行电子罗盘校准,请耐心等候！", Toast.LENGTH_LONG);
        Toast toast51 = Toast.makeText(getApplicationContext(), "电子罗盘校准失败", Toast.LENGTH_LONG);
        Toast toast52 = Toast.makeText(getApplicationContext(), "收到非正常回复", Toast.LENGTH_LONG);
        Toast toast53 = Toast.makeText(getApplicationContext(), "超时未收到回复", Toast.LENGTH_LONG);
        Toast toast54 = Toast.makeText(getApplicationContext(), "天线类型不支持", Toast.LENGTH_LONG);
        Toast toast55 = Toast.makeText(getApplicationContext(), "此天线只支持手动定位方式", Toast.LENGTH_LONG);
        Toast toast56 = Toast.makeText(getApplicationContext(), "BUC开关状态设置成功", Toast.LENGTH_LONG);
        Toast toast57 = Toast.makeText(getApplicationContext(), "BUC开关状态设置失败", Toast.LENGTH_LONG);
        Toast toast58 = Toast.makeText(getApplicationContext(), "BUC本振设置成功", Toast.LENGTH_LONG);
        Toast toast59 = Toast.makeText(getApplicationContext(), "BUC本振设置失败", Toast.LENGTH_LONG);
        Toast toast60 = Toast.makeText(getApplicationContext(), "BUC增益衰减值设置成功", Toast.LENGTH_LONG);
        Toast toast61 = Toast.makeText(getApplicationContext(), "BUC增益衰减值设置失败", Toast.LENGTH_LONG);
        Toast toast62 = Toast.makeText(getApplicationContext(), "RSD门限设置成功", Toast.LENGTH_LONG);
        Toast toast63 = Toast.makeText(getApplicationContext(), "RSD门限设置失败", Toast.LENGTH_LONG);
        Toast toast64 = Toast.makeText(getApplicationContext(), "获取BUC增益衰减值成功！", Toast.LENGTH_LONG);
        Toast toast65 = Toast.makeText(getApplicationContext(), "获取BUC增益衰减值失败！", Toast.LENGTH_LONG);
        Toast toast66 = Toast.makeText(getApplicationContext(), "获取BUC类型成功！", Toast.LENGTH_LONG);
        Toast toast67 = Toast.makeText(getApplicationContext(), "获取BUC类型失败！", Toast.LENGTH_LONG);
        Toast toast68 = Toast.makeText(getApplicationContext(), "获取BUC开关状态成功！", Toast.LENGTH_LONG);
        Toast toast69 = Toast.makeText(getApplicationContext(), "获取BUC开关状态失败！", Toast.LENGTH_LONG);
        Toast toast70 = Toast.makeText(getApplicationContext(), "抱歉，接收到的BUC增益衰减值格式不正确！", Toast.LENGTH_LONG);
        Toast toast71 = Toast.makeText(getApplicationContext(), "抱歉，接收到的RSD门限格式不正确！", Toast.LENGTH_LONG);
        Toast toast72 = Toast.makeText(getApplicationContext(), "RSD门限查询成功！", Toast.LENGTH_LONG);
        Toast toast73 = Toast.makeText(getApplicationContext(), "RSD门限查询失败！", Toast.LENGTH_LONG);
        Toast toast74 = Toast.makeText(getApplicationContext(), "查询对星状态故障", Toast.LENGTH_LONG);
        Toast toast75 = Toast.makeText(getApplicationContext(), "正在跟踪", Toast.LENGTH_LONG);
        Toast toast76 = Toast.makeText(getApplicationContext(), "捕获确认中", Toast.LENGTH_LONG);
        Toast toast77 = Toast.makeText(getApplicationContext(), "正在捕获", Toast.LENGTH_LONG);
        Toast toast78 = Toast.makeText(getApplicationContext(), "对星成功", Toast.LENGTH_LONG);
        Toast toast79 = Toast.makeText(getApplicationContext(), "正在确认", Toast.LENGTH_LONG);
        Toast toast80 = Toast.makeText(getApplicationContext(), "对星命令发送成功", Toast.LENGTH_LONG);
        Toast toast81 = Toast.makeText(getApplicationContext(), "对星命令发送失败", Toast.LENGTH_LONG);
        Toast toast82 = Toast.makeText(getApplicationContext(), "正在对星！", Toast.LENGTH_LONG);
        Toast toast83 = Toast.makeText(getApplicationContext(), "对星故障", Toast.LENGTH_LONG);
        Toast toast84 = Toast.makeText(getApplicationContext(), "对星失败，请重新尝试！", Toast.LENGTH_LONG);
        Toast toast85 = Toast.makeText(getApplicationContext(), "获取BUC本振成功！", Toast.LENGTH_LONG);
        Toast toast86 = Toast.makeText(getApplicationContext(), "获取BUC本振失败！", Toast.LENGTH_LONG);
        Toast toast87 = Toast.makeText(getApplicationContext(), "对不起，您输入的BUC增益衰减值不合法！", Toast.LENGTH_LONG);
        Toast toast88 = Toast.makeText(getApplicationContext(), "对不起，您输入的RSD不合法！", Toast.LENGTH_LONG);
        Toast toast89 = Toast.makeText(getApplicationContext(), "获取BUC状态异常！", Toast.LENGTH_LONG);
        toaHashMap.put("获取纬度成功！", toast);
        toaHashMap.put("获取天线参数失败", toast1);
        toaHashMap.put("获取定位方式失败", toast2);
        toaHashMap.put("获取定位方式成功", toast3);
        toaHashMap.put("RTH门限查询成功！", toast4);
        toaHashMap.put("RTH门限查询失败！", toast5);
        toaHashMap.put("获取经度成功！", toast6);
        toaHashMap.put("获取俯仰补偿角成功！", toast7);
        toaHashMap.put("获取俯仰补偿角失败！", toast8);
        toaHashMap.put("获取LNB成功！", toast9);
        toaHashMap.put("获取LNB失败！", toast10);
        toaHashMap.put("获取经度失败！", toast11);
        toaHashMap.put("获取纬度失败！", toast12);
        toaHashMap.put("网络检测开关状态查询失败", toast13);
        toaHashMap.put("网络检测开关状态查询成功", toast14);
        toaHashMap.put("网络检测开关状态设置成功", toast15);
        toaHashMap.put("网络检测开关状态设置失败", toast16);
        toaHashMap.put("俯仰补偿角设置成功", toast17);
        toaHashMap.put("俯仰补偿角设置失败", toast18);
        toaHashMap.put("经纬度设置失败", toast19);
        toaHashMap.put("经纬度设置成功", toast20);
        toaHashMap.put("定位方式设置成功", toast21);
        toaHashMap.put("定位方式设置失败", toast22);
        toaHashMap.put("RTH门限设置成功！", toast23);
        toaHashMap.put("RTH门限设置失败！", toast24);
        toaHashMap.put("LNB本振设置成功", toast25);
        toaHashMap.put("LNB本振设置失败", toast26);
        toaHashMap.put("连接网元服务器失败", toast27);
        toaHashMap.put("保存失败,请检查设备！", toast28);
        toaHashMap.put("星位参数保存成功", toast29);
        toaHashMap.put("对不起，您输入的经度不合法！", toast30);
        toaHashMap.put("对不起，您输入的纬度不合法！", toast31);
        toaHashMap.put("对不起，您输入的俯仰补偿角不合法！", toast32);
        toaHashMap.put("抱歉，接收到的RTH格式不正确！", toast33);
        toaHashMap.put("抱歉，接收到的当前经度格式不正确！", toast34);
        toaHashMap.put("抱歉，接收到的当前纬度格式不正确！", toast35);
        toaHashMap.put("抱歉，接收到的俯仰补偿角格式不正确！", toast36);
        toaHashMap.put("抱歉，接收到的经度格式不正确！", toast37);
        toaHashMap.put("抱歉，接收到的频率格式不正确！", toast38);
        toaHashMap.put("对不起，您输入的RTH不合法！", toast39);
        toaHashMap.put("执行了保存！", toast40);
        toaHashMap.put("电子罗盘初始化命令发送成功", toast41);
        toaHashMap.put("电子罗盘初始化命令发送失败", toast42);
        toaHashMap.put("初始化设置成功", toast43);
        toaHashMap.put("正在执行电子罗盘初始化,请耐心等候！", toast44);
        toaHashMap.put("任务未执行", toast45);
        toaHashMap.put("初始化设置失败", toast46);
        toaHashMap.put("电子罗盘校准命令发送成功", toast47);
        toaHashMap.put("电子罗盘校准命令发送失败", toast48);
        toaHashMap.put("电子罗盘校准成功", toast49);
        toaHashMap.put("正在执行电子罗盘校准,请耐心等候！", toast50);
        toaHashMap.put("电子罗盘校准失败", toast51);
        toaHashMap.put("收到非正常回复", toast52);
        toaHashMap.put("超时未收到回复", toast53);
        toaHashMap.put("天线类型不支持", toast54);
        toaHashMap.put("此天线只支持手动定位方式", toast55);
        toaHashMap.put("BUC开关状态设置成功", toast56);
        toaHashMap.put("BUC开关状态设置失败", toast57);
        toaHashMap.put("BUC本振设置成功", toast58);
        toaHashMap.put("BUC本振设置失败", toast59);
        toaHashMap.put("BUC增益衰减值设置成功", toast60);
        toaHashMap.put("BUC增益衰减值设置失败", toast61);
        toaHashMap.put("RTH门限设置成功", toast62);
        toaHashMap.put("RTH门限设置失败", toast63);
        toaHashMap.put("获取BUC增益衰减值成功！", toast64);
        toaHashMap.put("获取BUC增益衰减值失败！", toast65);
        toaHashMap.put("获取BUC类型成功！", toast66);
        toaHashMap.put("获取BUC类型失败！", toast67);
        toaHashMap.put("获取BUC开关状态成功！", toast68);
        toaHashMap.put("获取BUC开关状态失败！", toast69);
        toaHashMap.put("抱歉，接收到的BUC增益衰减值格式不正确！", toast70);
        toaHashMap.put("抱歉，接收到的RSD门限格式不正确！", toast71);
        toaHashMap.put("RSD门限查询成功！", toast72);
        toaHashMap.put("RSD门限查询失败！", toast73);
        toaHashMap.put("查询对星状态故障", toast74);
        toaHashMap.put("正在跟踪", toast75);
        toaHashMap.put("捕获确认中", toast76);
        toaHashMap.put("正在捕获", toast77);
        toaHashMap.put("对星成功", toast78);
        toaHashMap.put("正在确认", toast79);
        toaHashMap.put("对星命令发送成功", toast80);
        toaHashMap.put("对星命令发送失败", toast81);
        toaHashMap.put("正在对星！", toast82);
        toaHashMap.put("对星故障", toast83);
        toaHashMap.put("对星失败，请重新尝试！", toast84);
        toaHashMap.put("获取BUC本振成功！", toast85);
        toaHashMap.put("获取BUC本振失败！", toast86);
        toaHashMap.put("对不起，您输入的BUC增益衰减值不合法！", toast87);
        toaHashMap.put("对不起，您输入的RSD不合法！", toast88);
        toaHashMap.put("获取BUC状态异常！", toast89);

    }

    private void initView() {
        supportFragmentManager = getSupportFragmentManager();
        warnDialogFragment = new WarnDialogFragment(this);
        tv_bar_title.setText(R.string.auto_star);
        tv_center_frequency.setTypeface(AppData.fontXiti);
        tv_symbol_rate.setTypeface(AppData.fontXiti);
        tv_mobility_support.setTypeface(AppData.fontXiti);
        tv_satellite_number.setTypeface(AppData.fontXiti);
        tv_network_detection.setTypeface(AppData.fontXiti);
        tv_aim_satellite_mode.setTypeface(AppData.fontXiti);
        tv_location_type.setTypeface(AppData.fontXiti);
        tv_zai_bo_bandwidth.setTypeface(AppData.fontXiti);
        tv_polarization_type.setTypeface(AppData.fontXiti);
        tv_compass.setTypeface(AppData.fontXiti);
        tv_pitch_compensation_angle.setTypeface(AppData.fontXiti);
        tv_zai_bo.setTypeface(AppData.fontXiti);
        tv_aim_satellite_frequency.setTypeface(AppData.fontXiti);
        tv_rth.setTypeface(AppData.fontXiti);
        tv_rsd.setTypeface(AppData.fontXiti);
        tv_buc_gain_attenuation.setTypeface(AppData.fontXiti);
        tv_buc_local_oscillator.setTypeface(AppData.fontXiti);
        tv_buc_switch.setTypeface(AppData.fontXiti);
        tv_current_lon.setTypeface(AppData.fontXiti);
        tv_current_lat.setTypeface(AppData.fontXiti);
        tv_lnb_local_oscillator.setTypeface(AppData.fontXiti);
        tv_buc.setTypeface(AppData.fontXiti);
        tv_debug_model.setTypeface(AppData.fontXiti);
        tv_satellite_longitude.setTypeface(AppData.fontXiti);
    }

    // 从服务器获取卫星高级参数1
    private void doGetAntennaParameters() {
        Log.e(TAG, "下面开始从服务器获取卫星经度极频率参数并判断格式是否正确------》");
        advgettask = new SntAsyncHttpGet();
        advgettask.execute(GET_SATEADV_ARG);//	// 查看卫星参数--高级
        LoggerSave.requestLog(GET_SATEADV_ARG, GET_SATEADV_ARG);
        advgettask.setFinishListener(new HpOverListener() {
            @Override
            public void HpRecvOk(JSONObject data) {
                // TODO Auto-generated method stub
                if(!isVisible){
                    return;
                }
               AntennaParameterBean antennaParameterBean=GsonUtils.fromJson(data.toString(),AntennaParameterBean.class);
                try {
                    if ("-100".equals(antennaParameterBean.getCode())) {
                        if (toaHashMap.get("连接网元服务器失败") != null) {
                            toaHashMap.get("连接网元服务器失败").show();
                        }
                    } else if ("-1".equals(antennaParameterBean.getCode())) {
                        if (data.optString("msg").equals("acu_occupy")) {
                            showMutualDialog();
                        } else {
                            if (toaHashMap.get("获取天线参数失败") != null) {
                                toaHashMap.get("获取天线参数失败").show();
                            }
                        }
                    } else {
                        Log.e(TAG, "高级参数获取，返回码为：" + data.getString("code"));
                        initAntennaParameterUI(antennaParameterBean);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        Log.e(TAG, "---------》从服务器获取卫星经度极频率参数并判断格式是否正确完成");
    }

    //从服务器获取参数  RSSI 当前经度  当前纬度
    private void initAntennaParameterUI(AntennaParameterBean obj) {
        try {
            if (!"".equals(obj.getLocatypecode()) && obj.getLocatypecode() != null) {
                if ("0".equals(obj.getLocatypecode())) {
                    if (!StringUtils.isEmpty(obj.getLocatype())) {
                        sp_location_type.setSelection(Integer.parseInt(obj.getLocatype()));
                    }
                    if (toaHashMap.get("获取定位方式成功") != null) {
                        toaHashMap.get("获取定位方式成功").show();
                    }
                }else{
                    if (toaHashMap.get("获取定位方式失败") != null) {
                        toaHashMap.get("获取定位方式失败").show();
                    }
                }
            }
            //判断接受的数据是否符合格式
            if ("0".equals(obj.getRthCode())) {
                if (ChechIpMask.a2b(obj.getRth(), 100, 10000)) {
                    et_rth.setText(obj.getRth());//从服务器获取RSSI门限
                    if (toaHashMap.get("RTH门限查询成功！") != null) {
                        toaHashMap.get("RTH门限查询成功！").show();
                    }
                } else {
                    et_rth.setText("--");//-1
                    if (toaHashMap.get("抱歉，接收到的RTH门限格式不正确！") != null) {
                        toaHashMap.get("抱歉，接收到的RTH门限格式不正确！").show();
                    }
                }
            } else {
                if (toaHashMap.get("RTH门限查询失败！") != null) {
                    toaHashMap.get("RTH门限查询失败！").show();
                }
            }
            if ("0".equals(obj.getRsdCode())) {
                if (ChechIpMask.a2b(obj.getRsd(), 0, 2)) {
                    et_rsd.setText(obj.getRsd());//从服务器获取RSSI门限
                    if (toaHashMap.get("RSD门限查询成功！") != null) {
                        toaHashMap.get("RSD门限查询成功！").show();
                    }
                } else  {
                    et_rsd.setText("--");//-1
                    if (toaHashMap.get("抱歉，接收到的RSD门限格式不正确！") != null) {
                        toaHashMap.get("抱歉，接收到的RSD门限格式不正确！").show();
                    }
                }
            } else {
                if (toaHashMap.get("RSD门限查询失败！") != null) {
                    toaHashMap.get("RSD门限查询失败！").show();
                }
            }
            //判断接受的数据是否符合格式
            if ("0".equals(obj.getLonlatcode())) {
                if (ChechIpMask.a2b(obj.getCurlon(), 0, 180)) {//当前经度
                   /* String _curlon = ChechIpMask.numDigite(obj.getCurlon(), 2);
                    //如果带负号
                    if (_curlon.substring(0, 1).equals("-") || _curlon.substring(0, 1).equals("﹣") || _curlon.substring(0, 1).equals("－") || _curlon.substring(0, 1).equals("﹣")) {
                        _curlon = _curlon.substring(1);
                        sp_current_lon_type.setSelection(1);
                    } else {
                        sp_current_lon_type.setSelection(0);

                    }*/
                    String currlon = obj.getCurlon();
                    if (MathUtil.isNumber(currlon)) {
                        if (currlon != null && currlon.startsWith("-")) {
                            sp_current_lon_type.setSelection(1);
                        } else {
                            sp_current_lon_type.setSelection(0);
                        }
                        currlon = Math.abs(Float.parseFloat(ChechIpMask.numDigite2(currlon, 2))) + "";
                    }
                    et_current_lon.setText(currlon);
                    if (toaHashMap.get("获取经度成功！") != null) {
                        toaHashMap.get("获取经度成功！").show();
                    }
                } else{
                    et_current_lon.setText("--");//-1
                    if (toaHashMap.get("抱歉，接收到的当前经度格式不正确！") != null) {
                        toaHashMap.get("抱歉，接收到的当前经度格式不正确！").show();
                    }
                }
            } else {
                if (toaHashMap.get("获取经度失败！") != null) {
                    toaHashMap.get("获取经度失败！").show();
                }
            }
            //判断接受的数据是否符合格式
            if ("0".equals(obj.getLonlatcode())) {
                if (ChechIpMask.a2b(obj.getCurrlat(), 0, 90)) {
                    /*String _currlat = ChechIpMask.numDigite(obj.getCurrlat(), 2);
                    //如果带负号
                    if (_currlat.substring(0, 1).equals("-") || _currlat.substring(0, 1).equals("﹣") || _currlat.substring(0, 1).equals("－") || _currlat.substring(0, 1).equals("﹣")) {

                        _currlat = _currlat.substring(1);
                        sp_current_lat_type.setSelection(1);
                    } else {
                        sp_current_lat_type.setSelection(0);
                    }*/
                    String currlat = obj.getCurrlat();
                    if (MathUtil.isNumber(currlat)) {
                        if (currlat != null && currlat.startsWith("-")) {
                            sp_current_lat_type.setSelection(1);
                        } else {
                            sp_current_lat_type.setSelection(0);
                        }
                        currlat = Math.abs(Float.parseFloat(ChechIpMask.numDigite2(currlat, 2))) + "";
                    }
                    et_current_lat.setText(currlat);
                    if (toaHashMap.get("获取纬度成功！") != null) {
                        toaHashMap.get("获取纬度成功！").show();
                    }
                }else {
                        et_current_lat.setText("--");//-1
                        if (toaHashMap.get("抱歉，接收到的当前纬度格式不正确！") != null) {
                            toaHashMap.get("抱歉，接收到的当前纬度格式不正确！").show();
                        }
                    }
            } else  {
                if (toaHashMap.get("获取纬度失败！") != null) {
                    toaHashMap.get("获取纬度失败！").show();
                }
            }
            if ("0".equals(obj.getElevoffsetcode())) {
                if (ChechIpMask.abs(obj.getElevoffset(), 15)) {
                    String _elevoffset = ChechIpMask.numDigite(obj.getElevoffset(), 2);
                    et_pitch_compensation_angle.setText(_elevoffset);//从服务器获取俯仰补偿角
                    if (toaHashMap.get("获取俯仰补偿角成功！") != null) {
                        toaHashMap.get("获取俯仰补偿角成功！").show();
                    }
                } else{
                    et_pitch_compensation_angle.setText("--");//-1
                    if (toaHashMap.get("抱歉，接收到的俯仰补偿角格式不正确！") != null) {
                        toaHashMap.get("抱歉，接收到的俯仰补偿角格式不正确！").show();
                    }

                }
            } else  {
                if (toaHashMap.get("获取俯仰补偿角失败！") != null) {
                    toaHashMap.get("获取俯仰补偿角失败！").show();
                }
            }
            if ("0".equals(obj.getBucSwitchCode())) {
                if ("0".equals(obj.getBucSwitch()) ||"2".equals(obj.getBucSwitch())){
                    tb_buc_switch.setChecked(false);
                    if (toaHashMap.get("获取BUC开关状态成功！") != null) {
                        toaHashMap.get("获取BUC开关状态成功！").show();
                    }
                } else if ("1".equals(obj.getBucSwitch()) || "3".equals(obj.getBucSwitch()) ) {
                    tb_buc_switch.setChecked(true);
                    if (toaHashMap.get("获取BUC开关状态成功！") != null) {
                        toaHashMap.get("获取BUC开关状态成功！").show();
                    }
                } else if ("4".equals(obj.getBucSwitch()) ) {
                    ll_buc_type.setVisibility(View.GONE);
                    ll_buc_switch.setVisibility(View.GONE);
                    ll_buc_local_oscillator.setVisibility(View.GONE);
                    ll_buc_gain_attenuation.setVisibility(View.GONE);
                    if (toaHashMap.get("获取BUC状态异常！") != null) {
                        toaHashMap.get("获取BUC状态异常！").show();
                    }
                }

            } else{
                ll_buc_type.setVisibility(View.GONE);
                ll_buc_switch.setVisibility(View.GONE);
                ll_buc_local_oscillator.setVisibility(View.GONE);
                ll_buc_gain_attenuation.setVisibility(View.GONE);
                if (ll_buc_switch.getVisibility() == View.VISIBLE) {
                    if (toaHashMap.get("获取BUC开关状态失败！") != null) {
                        toaHashMap.get("获取BUC开关状态失败！").show();
                    }
                }
            }
            if ("0".equals(obj.getBucGainCode())) {
                if (ChechIpMask.a2b(obj.getBucGain(), 0, 300)) {
                    String _superRange = String.valueOf(ChechIpMask.a2b(obj.getBucGain(), 0, 300));
                    String superRange = String.valueOf(new BigDecimal(_superRange).divide(new BigDecimal(10)));
                    String superBucRange = ChechIpMask.numDigite(superRange, 1);
                    et_buc_gain_attenuation.setText(superBucRange);//从服务器获取俯仰补偿角
                    if (toaHashMap.get("获取BUC增益衰减值成功！") != null) {
                        toaHashMap.get("获取BUC增益衰减值成功！").show();
                    }
                } else {
                    et_buc_gain_attenuation.setText("--");//-1
                    if (toaHashMap.get("抱歉，接收到的BUC增益衰减值格式不正确！") != null) {
                        toaHashMap.get("抱歉，接收到的BUC增益衰减值格式不正确！").show();
                    }

                }
            } else  {
                if (ll_buc_switch.getVisibility() == View.VISIBLE) {
                    if (toaHashMap.get("获取BUC增益衰减值失败！") != null) {
                        toaHashMap.get("获取BUC增益衰减值失败！").show();
                    }
                }
            }
            if ("0".equals(obj.getLnbcode())) {
                et_lnb_local_oscillator.setText(obj.getLnb());
                SharedPreferenceManager.saveString(this, "lnb", obj.getLnb());
                if (toaHashMap.get("获取LNB成功！") != null) {
                    toaHashMap.get("获取LNB成功！").show();
                }

            } else {
                if (toaHashMap.get("获取LNB失败！") != null) {
                    toaHashMap.get("获取LNB失败！").show();
                }
            }
            if ("0".equals(obj.getBucOscillatorCode())){
                et_buc_local_oscillator.setText(obj.getBucOscillator());
                if (toaHashMap.get("获取BUC本振成功！") != null) {
                    toaHashMap.get("获取BUC本振成功！").show();
                }
            } else{
                if (ll_buc_switch.getVisibility() == View.VISIBLE) {
                    if (toaHashMap.get("获取BUC本振失败！") != null) {
                        toaHashMap.get("获取BUC本振失败！").show();
                    }
                }
            }
            if ("0".equals(obj.getBucTypeCode())) {
                if ("WaveStream".equals(obj.getBucType())) {
                    sp_buc_type.setSelection(0);
                } else if ("Other".equals(obj.getBucType())) {
                    sp_buc_type.setSelection(1);
                }
                if (toaHashMap.get("获取BUC类型成功！") != null) {
                    toaHashMap.get("获取BUC类型成功！").show();
                }
            } else{
                if (ll_buc_switch.getVisibility() == View.VISIBLE) {
                    if (toaHashMap.get("获取BUC类型失败！") != null) {
                        toaHashMap.get("获取BUC类型失败！").show();
                    }
                }
            }
            setListViewHeight(listViewLNB, lnbAdapter);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "获取天线参数故障", Toast.LENGTH_SHORT).show();
        }
    }

    private StarCodeModel isDataSaved() {
        List<StarCodeModel> stars = DataSupport.findAll(StarCodeModel.class);
        if (stars != null) {
            for (StarCodeModel s : stars) {
                if (currentStar.getId() == s.getId()) {
                    return s;
                }
            }
        }
        return null;
    }

    /**
     * 当用户点击保存按钮，更新searchStarModels中与id对应的Model
     *
     * @param dataSaved
     */
    private void updateSearchStarModels(StarCodeModel dataSaved) {
        for (int i = 0; i < searchStarModels.size(); i++) {
            if (searchStarModels.get(i).getId() == (dataSaved == null ? currentStar.getId() : dataSaved.getId())) {
                searchStarModels.set(i, dataSaved == null ? currentStar : dataSaved);
                break;
            }
        }
        starAdapter.notifyDataSetChanged();
    }

    /**
     * 转换天线参数
     */
    private AntennaParameterBean transformAntennaParameter() {
        String strLocatemode = sp_location_type.getSelectedItem().toString();// 定位方式
        AntennaParameterBean antennaParameterBean=new AntennaParameterBean();
        transformLocateMode(strLocatemode, antennaParameterBean);
        transformLonLat(strLocatemode, antennaParameterBean);
        antennaParameterBean.setStrLocatemode(strLocatemode);
        antennaParameterBean.setRth(ChechIpMask.numDigite(et_rth.getText().toString(), 0));
        antennaParameterBean.setRsd(ChechIpMask.numDigite(et_rsd.getText().toString(), 2));
        if (ll_buc_switch.getVisibility() == View.VISIBLE) {
            antennaParameterBean.setBucGain( new BigDecimal(ChechIpMask.numDigite(et_buc_gain_attenuation.getText().toString(), 1)).multiply(new BigDecimal(10)).toString());
        }
        antennaParameterBean.setElevoffset(ChechIpMask.numDigite(et_pitch_compensation_angle.getText().toString(), 2));
        antennaParameterBean.setLnb(et_lnb_local_oscillator.getText().toString());
        antennaParameterBean.setBucOscillator(et_buc_local_oscillator.getText().toString());
        return antennaParameterBean;
    }

    private void transformLonLat(String strLocatemode, AntennaParameterBean antennaParameterBean) {
        if (strLocatemode.equals("手动")) {
            String strCurrentLongItude = ChechIpMask.numDigite(et_current_lon.getText().toString(), 2);
            if (sp_current_lon_type.getSelectedItemPosition() == 1) {
                strCurrentLongItude = "-" + strCurrentLongItude;
            }
            antennaParameterBean.setCurlon(strCurrentLongItude);
            String strCurrentLatitude = ChechIpMask.numDigite(et_current_lat.getText().toString(), 2);
            if (sp_current_lat_type.getSelectedItemPosition() == 1) {//南纬
                strCurrentLatitude = "-" + strCurrentLatitude;
            }
            antennaParameterBean.setCurrlat(strCurrentLatitude);

        }
    }

    private void transformLocateMode(String strLocatemode, AntennaParameterBean antennaParameterBean) {
        if (strLocatemode.equals("手动")) {
            antennaParameterBean.setLocatype("0");
        } else if (strLocatemode.equals("北斗")) {
            antennaParameterBean.setLocatype("2");
        } else if (strLocatemode.equals("GPS")) {
            antennaParameterBean.setLocatype("1");
        }
    }

    private boolean judgeAntennaParameter() {
        String strLocatemode = sp_location_type.getSelectedItem().toString();// 定位方式
        if (!ChechIpMask.a2b(et_rth.getText().toString(), 100, 10000)) {
            if (toaHashMap.get("对不起，您输入的RTH不合法！") != null) {
                toaHashMap.get("对不起，您输入的RTH不合法！").show();
            }
            return true;
        }
        if (!ChechIpMask.a2b(et_rsd.getText().toString(), 0, 2)) {
            if (toaHashMap.get("对不起，您输入的RSD不合法！") != null) {
                toaHashMap.get("对不起，您输入的RSD不合法！").show();
            }
            return true;
        }
        if (!ChechIpMask.a2b(et_buc_gain_attenuation.getText().toString(), 0, 30)) {
            if(ll_buc_switch.getVisibility()==View.VISIBLE){
                if (toaHashMap.get("对不起，您输入的BUC增益衰减值不合法！") != null) {
                    toaHashMap.get("对不起，您输入的BUC增益衰减值不合法！").show();
                }
                return true;
            }

        }
        if(!ChechIpMask.abs(et_pitch_compensation_angle.getText().toString(), 15)) {
            if (toaHashMap.get("对不起，您输入的俯仰补偿角不合法！") != null) {
                toaHashMap.get("对不起，您输入的俯仰补偿角不合法！").show();
            }
            return true;
        }
        if (strLocatemode.equals("手动")) {
            if (!ChechIpMask.a2b(et_current_lon.getText().toString(), 0, 180)) {
                if (toaHashMap.get("对不起，您输入的经度不合法！") != null) {
                    toaHashMap.get("对不起，您输入的经度不合法！").show();
                }
                return true;
            }
            if (!ChechIpMask.a2b(et_current_lat.getText().toString(), 0, 90)) {
                if (toaHashMap.get("对不起，您输入的纬度不合法！") != null) {
                    toaHashMap.get("对不起，您输入的纬度不合法！").show();
                }
                return true;
            }
        }
        return false;
    }

    // 点击保存的响应事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save_default:
                querySessionStatus(new OnSessionStatusListener() {
                    @Override
                    public void sessionSuccess() {
                        //判断输入合法性
                        if (judgeAimSatelliteParameter()) return;
                        //将对星参数设为默认
                        doSaveDefault(aimStarParameterToJS());
                    }
                });
                break;
            case R.id.bt_save:
                //判断输入是否合法
                if (judgeAimSatelliteParameter()) return;
                //保存对星参数设置
                transformAndSaveAimSatelliteParameter();
                //showDia();
                querySessionStatus(new OnSessionStatusListener() {
                    @Override
                    public void sessionSuccess() {
                        if(!isVisible){
                            return;
                        }
                        //判断输入是否合法
                        if (judgeAntennaParameter()) return;
                        //保存天线参数设置
                        doAntennaParameterSetting(antennaParameterToJS());
                    }

                    @Override
                    public void sessionErrorResponse() {
                       // dismissDia();
                        super.sessionErrorResponse();
                    }
                });
                break;
            case R.id.ll_debug_model://点击了调试模式
                stopGet();
                if ("S(三轴)".equals(mType)) {
                    Intent mIntent = new Intent(SuperSetRefActivity.this, DebugControlAnotherActivity.class);
                    startActivity(mIntent);
                } else {
                    Intent mIntent = new Intent(SuperSetRefActivity.this, DebbugControlActivity.class);
                    startActivity(mIntent);
                }
                break;
            case R.id.bt_antenna_stop://点击停止保存天线参数
                querySessionStatus(new OnSessionStatusListener() {
                    @Override
                    public void sessionSuccess() {
                        stopGet();
                    }
                });
                break;
            case R.id.bt_compass://点击校准罗盘
                querySessionStatus(new OnSessionStatusListener() {
                    @Override
                    public void sessionSuccess() {
                        getCompass();
                    }
                });
                break;
            case R.id.bt_aim_satellite://点击对星按钮
                if ("查看".equals(bt_aim_satellite.getText().toString())) {
                    Intent intent = new Intent(SuperSetRefActivity.this, AutoStarActivity.class);
                    startActivity(intent);
                } else {
                    SharedPreferenceManager.saveBoolean(mContext, "switchStatus", true);
                    Intent intent = new Intent(SuperSetRefActivity.this, OneKeyStarActivity.class);
                    intent.putExtra("oneKeyStar", "oneKeyStar");
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tb_network_detection_switch:
                if (isChecked) {
                    mPing = "1";
                    PingPost(isChecked);
                } else {
                    mPing = "0";
                    PingPost(isChecked);
                }
                break;
            case R.id.tb_buc_switch:
                if (isChecked) {
                    strBucSwitch = "1";
                } else {
                    strBucSwitch = "0";
                }
                break;
            case R.id.tb_mobility_support_switch:
                if ("1".equals(amipStatus)) {
                    if (isChecked) {
                        strAmipSwitch = "1";
                        DiaSupportOn(isChecked);
                    } else {
                        strAmipSwitch = "0";
                        DiaSupportOff(isChecked);
                    }
                }
                break;
        }

    }

    private void DiaSupportOn(final boolean isChecked) {
        ConfirmCancelDialog.init()
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        holder.setText(R.id.tv_tip, getResources().getString(R.string.tip_support_on));
                        holder.setOnClickListener(R.id.bt_cancel, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                tb_mobility_support_switch.setOnCheckedChangeListener(null);
                                tb_mobility_support_switch.setChecked(!isChecked);
                                tb_mobility_support_switch.setOnCheckedChangeListener(SuperSetRefActivity.this);
                            }
                        });
                        holder.setOnClickListener(R.id.bt_ok, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                initZaiBoView();
                                settingView(false);
                                saveZaiBoAutoConfig();
                                bt_aim_satellite.setText("查看");
                                Intent intent = new Intent(SuperSetRefActivity.this, AutoStarActivity.class);
                                intent.putExtra("currentStar", currentStar);
                                intent.putExtra("key", "sateCatchPost");
                                startActivity(intent);
                                dialog.dismiss();
                                SharedPreferenceManager.saveBoolean(mContext, "switchStatus", false);
                            }
                        });
                    }
                })
                .show(getSupportFragmentManager());

    }

    private void DiaSupportOff(final boolean isChecked) {
        ConfirmCancelDialog.init()
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        holder.setText(R.id.tv_tip, getResources().getString(R.string.tip_support_off));
                        holder.setOnClickListener(R.id.bt_cancel, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                tb_mobility_support_switch.setOnCheckedChangeListener(null);
                                tb_mobility_support_switch.setChecked(!isChecked);
                                tb_mobility_support_switch.setOnCheckedChangeListener(SuperSetRefActivity.this);
                            }
                        });
                        holder.setOnClickListener(R.id.bt_ok, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                settingView(true);
                                bt_aim_satellite.setText("对星");
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .show(getSupportFragmentManager());

    }

    /**
     * 对星参数转为json格式
     *
     * @return
     */
    private JSONObject aimStarParameterToJS() {
        transformAndSaveAimSatelliteParameter();
        String strXinRate = currentStar.getFreq();
        if (!StringUtils.isEmpty(strXinRate)) {
            strXinRate = new BigDecimal(strXinRate).multiply(new BigDecimal(100)).toString();
        }
        String strZaiRate = currentStar.getZfreq();
        String strCenter = currentStar.getCenterFreq();
        String mLnb = SharedPreferenceManager.getString(this, "lnb");
        if (!StringUtils.isEmpty(strCenter) && !StringUtils.isEmpty(mLnb)) {
            BigDecimal lCenterFreq = new BigDecimal(strCenter);
            strCenter = lCenterFreq.subtract(new BigDecimal(mLnb)).multiply(new BigDecimal(100)).toString();
        }
        try {
            JSONObject genjs = new JSONObject();
            genjs.put("satenum", strSatelliteNum);
            genjs.put("satelon", currentStar.getSatelon());
            genjs.put("mode", currentStar.getMode());
            genjs.put("freq", strXinRate);
            genjs.put("zfreq", strZaiRate);
            genjs.put("centerFreq", strCenter);
            genjs.put("signRate", currentStar.getSignRate());
            genjs.put("bw", currentStar.getBw());
            genjs.put("type", currentStar.getType());
            return genjs;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将对星参数设为默认
     *
     * @param genjs
     */
    private void doSaveDefault(final JSONObject genjs) {
        if (genjs == null) {
            return;
        }
        if (judgeAimSatelliteParameter()) return;
        agrtask = new SntAsyncPost();
        agrtask.execute(XTHttpUtil.POST_ONESTAR_DEFAULT_ADDRESS, genjs.toString());
        agrtask.SetListener(new PostOverHandle() {
            @Override
            public void HandleData(JSONObject data) {
                try {
                    if(!isVisible){
                        return;
                    }
                    if (data.getString("code").equals("0")) {
                        SntSpUtils.GenJStoSp(SuperSetRefActivity.this, mSate, genjs);
                        Toast.makeText(SuperSetRefActivity.this, "保存默认对星成功", Toast.LENGTH_LONG).show();
                    } else if (data.getString("code").equals("-100")) {
                        Toast.makeText(SuperSetRefActivity.this, "连接网元服务器失败,参数未保存", Toast.LENGTH_LONG).show();
                    } else if (data.getString("code").equals("-1")) {
                        Toast.makeText(SuperSetRefActivity.this, "保存对星失败！", Toast.LENGTH_LONG).show();
                    } else if (data.getString("code").equals("-2")) {
                        Toast _toast = Toast.makeText(SuperSetRefActivity.this, "保存失败，天线类型不支持！", Toast.LENGTH_LONG);
                        _toast.setGravity(Gravity.CENTER, 0, 0);
                        _toast.show();//,错误码："+data.getString("code")
                    } else {
                        Toast.makeText(SuperSetRefActivity.this, "设为默认保存失败", Toast.LENGTH_LONG).show();//,错误码："+data.getString("code")
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 天线参数转换为json
     *
     * @return
     */
    private JSONObject antennaParameterToJS() {
        AntennaParameterBean antennaParameterBean = transformAntennaParameter();
        try {
            JSONObject advjs = new JSONObject();
            advjs.put("rth", antennaParameterBean.getRth());
            advjs.put("rsd", antennaParameterBean.getRsd());
            advjs.put("locatype", antennaParameterBean.getLocatype());
            if (antennaParameterBean.getStrLocatemode().equals("手动")) {
                advjs.put("curlon", antennaParameterBean.getCurlon());
                advjs.put("currlat", antennaParameterBean.getCurrlat());
            }
            advjs.put("elevoffset", antennaParameterBean.getElevoffset());
            advjs.put("lnb", antennaParameterBean.getLnb());
            advjs.put("bucSwitch", strBucSwitch);
            advjs.put("bucOscillator", antennaParameterBean.getBucOscillator());
            advjs.put("bucGain", antennaParameterBean.getBucGain());
            return advjs;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存天线参数设置
     *
     * @param advjs
     */
    private void doAntennaParameterSetting(final JSONObject advjs) {
        agrtask = new SntAsyncPost();
        final String strLocatemode = sp_location_type.getSelectedItem().toString();// 定位方式
        agrtask.execute(POST_ARGSET_ADV, advjs.toString());// 卫星参数设置-高级
        LoggerSave.requestLog(POST_ARGSET_ADV, advjs.toString());
        agrtask.SetListener(new PostOverHandle() {
            @Override
            public void HandleData(JSONObject data) {
                //Toast.makeText(getApplicationContext(), data.toString(), 0).show();
                //dismissDia();
                if(!isVisible){
                    return;
                }
                try {
                    if (data.getString("code").equals("0")) {
                        SntSpUtils.AdvJStoSp(SuperSetRefActivity.this, advjs);//edit by hyw 20161114
                    } else if (data.getString("code").equals("-100")) {
                        if (toaHashMap.get("连接网元服务器失败") != null) {
                            toaHashMap.get("连接网元服务器失败").show();
                        }
                    } else if (data.getString("code").equals("-1")) {
                        if (data.getString("msg").equals("acu_occupy")) {
                            showMutualDialog();
                        } else {
                            if (toaHashMap.get("保存失败,请检查设备！") != null) {
                                toaHashMap.get("保存失败,请检查设备！").show();
                            }
                        }
                    } else {
                        if (toaHashMap.get("保存失败,请检查设备！") != null) {
                            toaHashMap.get("保存失败,请检查设备！").show();
                        }
                    }

                    if (data.getString("rthcode").equals("0")) {//-1
                        if (toaHashMap.get("RTH门限设置成功！") != null) {
                            toaHashMap.get("RTH门限设置成功！").show();
                        }
                    } else if (data.getString("rthcode").equals("-1")) {
                        if (toaHashMap.get("RTH门限设置失败") != null) {
                            toaHashMap.get("RTH门限设置失败").show();
                        }
                    }
                    if (data.getString("rsdcode").equals("0")) {//-1
                        if (toaHashMap.get("RSD门限设置成功！") != null) {
                            toaHashMap.get("RSD门限设置成功！").show();
                        }
                    } else if (data.getString("rsdcode").equals("-1")) {
                        if (toaHashMap.get("RSD门限设置失败") != null) {
                            toaHashMap.get("RSD门限设置失败").show();
                        }
                    }
                    if (data.getString("locatypecode").equals("0")) {
                        if (toaHashMap.get("定位方式设置成功") != null) {
                            toaHashMap.get("定位方式设置成功").show();
                        }
                    } else if (data.getString("locatypecode").equals("-1")) {
                        if (toaHashMap.get("定位方式设置失败") != null) {
                            toaHashMap.get("定位方式设置失败").show();
                        }
                    } else if (data.getString("locatypecode").equals("1")) {
                        sp_location_type.setSelection(0);
                        if (toaHashMap.get("此天线只支持手动定位方式") != null) {
                            toaHashMap.get("此天线只支持手动定位方式").show();
                        }
						/*if (toaHashMap.get("天线类型不支持")!=null){
							 toaHashMap.get("天线类型不支持").show();
							}*/
                    } else {
                        if (toaHashMap.get("定位方式设置失败") != null) {
                            toaHashMap.get("定位方式设置失败").show();
                        }
                    }

                    if (data.getString("lonlatcode").equals("0")) {//(-1)
                        if (strLocatemode.equals("手动")) {
                            if (toaHashMap.get("经纬度设置成功") != null) {
                                toaHashMap.get("经纬度设置成功").show();
                            }
                        } else {

                        }
                    } else if (data.getString("lonlatcode").equals("-1")) {
                        if (strLocatemode.equals("手动")) {
                            if (toaHashMap.get("经纬度设置失败") != null) {
                                toaHashMap.get("经纬度设置失败").show();
                            }
                        } else {

                        }
                    } else if (data.getString("lonlatcode").equals("1")) {
                        if (strLocatemode.equals("手动")) {
                            if (toaHashMap.get("天线类型不支持") != null) {
                                toaHashMap.get("天线类型不支持").show();
                            }
                        } else {

                        }
                    } else {
                        if (strLocatemode.equals("手动")) {
                            if (toaHashMap.get("经纬度设置失败") != null) {
                                toaHashMap.get("经纬度设置失败").show();
                            }
                        } else {

                        }
                    }
                    if (data.getString("elevoffsetcode").equals("0")) {//(-1)
                        if (toaHashMap.get("俯仰补偿角设置成功") != null) {
                            toaHashMap.get("俯仰补偿角设置成功").show();
                        }
                    } else if (data.getString("elevoffsetcode").equals("-1")) {
                        if (data.optString("msg").equals("acu_occupy")) {
                            showMutualDialog();
                        } else {
                            if (toaHashMap.get("俯仰补偿角设置失败") != null) {
                                toaHashMap.get("俯仰补偿角设置失败").show();
                            }
                        }
                    }
                    if (data.getString("lnbcode").equals("0")) {//(-1)
                        if (toaHashMap.get("LNB本振设置成功") != null) {
                            toaHashMap.get("LNB本振设置成功").show();
                        }
                    } else if (data.getString("lnbcode ").equals("-1")) {
                        if (toaHashMap.get("LNB本振设置失败") != null) {
                            toaHashMap.get("LNB本振设置失败").show();
                        }
                    }
                    if (data.getString("bucSwitchCode").equals("0")) {//(-1)
                        if (toaHashMap.get("BUC开关状态设置成功") != null) {
                            toaHashMap.get("BUC开关状态设置成功").show();
                        }
                    } else if (data.getString("bucSwitchCode").equals("-1")) {
                        if (ll_buc_switch.getVisibility() == View.VISIBLE) {
                            if (toaHashMap.get("BUC开关状态设置失败") != null) {
                                toaHashMap.get("BUC开关状态设置失败").show();
                            }
                        }
                    }
                    if (data.getString("bucOscillatorCode").equals("0")) {//(-1)
                        if (toaHashMap.get("BUC本振设置成功") != null) {
                            toaHashMap.get("BUC本振设置成功").show();
                        }
                    } else if (data.getString("bucOscillatorCode").equals("-1")) {
                        if (ll_buc_switch.getVisibility() == View.VISIBLE) {
                            if (toaHashMap.get("BUC本振设置失败") != null) {
                                toaHashMap.get("BUC本振设置失败").show();
                            }
                        }
                    }
                    if (data.getString("bucGainCode").equals("0")) {//(-1)
                        if (toaHashMap.get("BUC增益衰减值设置成功") != null) {
                            toaHashMap.get("BUC增益衰减值设置成功").show();
                        }
                    } else if (data.getString("bucGainCode").equals("-1")) {
                        if (ll_buc_switch.getVisibility() == View.VISIBLE) {
                            if (toaHashMap.get("BUC增益衰减值设置失败") != null) {
                                toaHashMap.get("BUC增益衰减值设置失败").show();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isVisible=false;
        if (bind != null) {
            bind.unbind();
        }
    }

    // 点击退出
    public void refSetOnFinish(View v) {
        finish();
    }

    // 加载数据的 ProgressDialog
    private ProgressDialog pd;


    public void showDia(String... msg) {
        if(pd==null){
            pd = new ProgressDialog(mContext);
            pd.setCanceledOnTouchOutside(false);
        }
        if (msg != null && msg.length > 0) {
            pd.setMessage(msg[0] + ".....");
        } else {
            pd.setMessage("正在加载数据.....");
        }
        if(isVisible){
            pd.show();
        }

    }

    public void dismissDia() {
        if (pd != null&&isVisible) {
            pd.dismiss();
        }
    }

    private void stopGet() {
        String stopUrl = GET_STOP;
        StringRequest stringRequest = new StringRequest(Method.GET, stopUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    LoggerSave.responseLog(GET_STOP, response.toString());
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.optString("msg");
                    if (code.equals("0")) {
                        Toast.makeText(SuperSetRefActivity.this, "天线停止命令发送成功", Toast.LENGTH_LONG).show();
                    } else if (code.equals("-1")) {
                        if (msg.equals("acu_occupy")) {
                            showMutualDialog();
                        } else {
                            Toast.makeText(SuperSetRefActivity.this, "天线停止命令发送失败", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SuperSetRefActivity.this, "天线停止命令发送失败", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "连接网元服务器失败", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                return map;
            }
        };
        LoggerSave.requestLog(GET_STOP, stringRequest.toString());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0, 0f));
        mRequestQueue.add(stringRequest);
    }

    private void getCompass() {
        String getCompassUrl = GET_COMPASS;
        StringRequest stringRequest = new StringRequest(Method.GET, getCompassUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    LoggerSave.responseLog(GET_COMPASS, response.toString());
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.optString("msg");
                    if (code.equals("0")) {
                        if (toaHashMap.get("电子罗盘校准命令发送成功") != null) {
                            toaHashMap.get("电子罗盘校准命令发送成功").show();
                        }
                        queryCompassResult();
                    } else if (code.equals("-1")) {
                        if (msg.equals("acu_occupy")) {
                            showMutualDialog();
                        } else {
                            if (toaHashMap.get("电子罗盘校准命令发送失败") != null) {
                                toaHashMap.get("电子罗盘校准命令发送失败").show();
                            }
                        }
                    } else {
                        if (toaHashMap.get("电子罗盘校准命令发送失败") != null) {
                            toaHashMap.get("电子罗盘校准命令发送失败").show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (toaHashMap.get("连接网元服务器失败") != null) {
                    toaHashMap.get("连接网元服务器失败").show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                return map;
            }
        };
        LoggerSave.requestLog(GET_COMPASS, stringRequest.toString());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0, 0f));
        mRequestQueue.add(stringRequest);
    }

    private void queryCompassResult() {
        String queryCompassResultUrl = QUERY_COMPASS_RESULT;
        StringRequest stringRequest = new StringRequest(Method.GET, queryCompassResultUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    LoggerSave.responseLog(QUERY_COMPASS_RESULT, response.toString());
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.optString("msg");
                    if (code.equals("0")) {
                        if (toaHashMap.get("电子罗盘校准成功") != null) {
                            toaHashMap.get("电子罗盘校准成功").show();
                        }
                    } else if (code.equals("1")) {
                        if (toaHashMap.get("正在执行电子罗盘校准,请耐心等候！") != null) {
                            toaHashMap.get("正在执行电子罗盘校准,请耐心等候！").show();
                        }
                        handler.sendEmptyMessageDelayed(1, messageDelay);
                    } else if (code.equals("-1")) {
                        if (msg.equals("acu_occupy")) {
                            showMutualDialog();
                        } else {
                            if (toaHashMap.get("任务未执行") != null) {
                                toaHashMap.get("任务未执行").show();
                            }
                        }
                    } else if (code.equals("-2")) {
                        if (toaHashMap.get("收到非正常回复") != null) {
                            toaHashMap.get("收到非正常回复").show();
                        }
                    } else if (code.equals("-3")) {
                        if (toaHashMap.get("超时未收到回复") != null) {
                            toaHashMap.get("超时未收到回复").show();
                        }
                    } else {
                        if (toaHashMap.get("电子罗盘校准失败") != null) {
                            toaHashMap.get("电子罗盘校准失败").show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "连接网元服务器失败", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                return map;
            }
        };
        LoggerSave.requestLog(QUERY_COMPASS_RESULT, stringRequest.toString());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0, 0f));
        mRequestQueue.add(stringRequest);
    }

    protected void showMutualDialog() {
        /*if(isFinishing()||!isVisible){
			return;
		}*/
        ConfirmDialog.init().setConvertListener(new ViewConvertListener() {
            @Override
            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                holder.setText(R.id.tv_tip, getString(R.string.acu_hold_tip));
                holder.setOnClickListener(R.id.bt_ok, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        }).show(getSupportFragmentManager());
    }

    // 查看ping参数
    private void doCheckPing() {
        String getCheckPingUrl = "http://192.168.80.1:9991/api/ping/get";
        StringRequest stringRequest = new StringRequest(Method.GET, getCheckPingUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(!isVisible){
                        return;
                    }
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if (code.equals("0")) {
                        String ping = jsonObject.getString("ping");
                        if ("1".equals(ping)) {
                            setToggleState(true);
                        } else {
                            setToggleState(false);
                        }
                    } else if (code.equals("-1")) {
                        if (toaHashMap.get("网络检测开关状态查询失败") != null) {
                            toaHashMap.get("网络检测开关状态查询失败").show();
                        }
                    } else {
                        if (toaHashMap.get("网络检测开关状态查询失败") != null) {
                            toaHashMap.get("网络检测开关状态查询失败").show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0, 0f));
        mRequestQueue.add(stringRequest);
    }

    // 查看openamip状态
    private void doCheckOpenamip() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sessionToken", mToken);
            JsonObjectRequest request = new JsonObjectRequest(Method.POST,
                    XTHttpUtil.POST_OPEN_AMIP, jsonObject,
                    new Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                            try {
                                if(!isVisible){
                                    return;
                                }
                                //JSONObject jsonObject = new JSONObject(response.toString());
                                String code = response.getString("code");
                                amipStatus = response.getString("amipStatus");
                                String amipSwitch = response.getString("amipSwitch");
                                if (code.equals("0")) {
                                    if (amipStatus.equals("0")) {
                                        Toast.makeText(getApplicationContext(), "不可移动性支持！", Toast.LENGTH_LONG).show();
                                        if (amipSwitch.equals("0")) {
                                            strAmipSwitch = "0";
                                        } else if (amipSwitch.equals("1")) {
                                            strAmipSwitch = "1";
                                        }
                                        tb_mobility_support_switch.setEnabled(false);
                                    } else if (amipStatus.equals("1")) {
                                        //Toast.makeText(getApplicationContext(), "可移动性支持！", 0).show();
                                        if (amipSwitch.equals("0")) {
                                            strAmipSwitch = "0";
                                            tb_mobility_support_switch.setOnCheckedChangeListener(null);
                                            tb_mobility_support_switch.setChecked(false);
                                            tb_mobility_support_switch.setOnCheckedChangeListener(SuperSetRefActivity.this);
                                            SharedPreferenceManager.saveBoolean(mContext, "switchStatus", true);
                                        } else if (amipSwitch.equals("1")) {
                                            settingView(false);
                                            strAmipSwitch = "1";
                                            tb_mobility_support_switch.setOnCheckedChangeListener(null);
                                            tb_mobility_support_switch.setChecked(true);
                                            tb_mobility_support_switch.setOnCheckedChangeListener(SuperSetRefActivity.this);
                                            bt_aim_satellite.setText("查看");
                                            SharedPreferenceManager.saveBoolean(mContext, "switchStatus", false);
                                        }
                                    } else if (amipStatus.equals("-1")) {
                                        Toast.makeText(getApplicationContext(), "openamip未启动！", Toast.LENGTH_LONG).show();
                                        tb_mobility_support_switch.setEnabled(false);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "查询openamip状态失败", Toast.LENGTH_LONG).show();
                                    tb_mobility_support_switch.setChecked(false);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "连接网元服务器失败", Toast.LENGTH_LONG).show();
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0, 0f));
            mRequestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setToggleState(boolean bl) {
        tb_network_detection_switch.setOnCheckedChangeListener(null);
        tb_network_detection_switch.setChecked(bl);
        tb_network_detection_switch.setOnCheckedChangeListener(SuperSetRefActivity.this);
    }

    //设置ping参数并生效
    private void PingPost(final boolean isChecked) {
        String setPingUrl = "http://192.168.80.1:9991/api/ping/set";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ping", mPing);
            JsonObjectRequest request = new JsonObjectRequest(Method.POST,
                    setPingUrl, jsonObject,
                    new Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //JSONObject jsonObject = new JSONObject(response.toString());
                                String code = response.getString("code");
                                if (code.equals("0")) {
                                    if (toaHashMap.get("网络检测开关状态设置成功") != null) {
                                        toaHashMap.get("网络检测开关状态设置成功").show();
                                    }
                                } else if (code.equals("-1")) {
                                    if (toaHashMap.get("网络检测开关状态设置失败") != null) {
                                        toaHashMap.get("网络检测开关状态设置失败").show();
                                        backToggleState(isChecked);
                                    }
                                } else {
                                    if (toaHashMap.get("网络检测开关状态设置失败") != null) {
                                        toaHashMap.get("网络检测开关状态设置失败").show();
                                        backToggleState(isChecked);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                backToggleState(isChecked);
                            }
                        }
                    }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "连接网元服务器失败", Toast.LENGTH_LONG).show();
                    backToggleState(isChecked);
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0, 0f));
            mRequestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 当ping包设置失败时，回退Toggle切换按钮的状态
     *
     * @param isChecked
     */
    private void backToggleState(boolean isChecked) {
        tb_network_detection_switch.setOnCheckedChangeListener(null);
        tb_network_detection_switch.setChecked(!isChecked);
        tb_network_detection_switch.setOnCheckedChangeListener(SuperSetRefActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isVisible = false;
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            Iterator<Entry<String, Toast>> iter = toaHashMap.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, Toast> entry = iter.next();
                Toast toast = entry.getValue();
                Field field = toast.getClass().getDeclaredField("mTN");
                field.setAccessible(true);
                Object obj = field.get(toast);
                java.lang.reflect.Method m = obj.getClass().getDeclaredMethod("hide");
                m.invoke(obj);
                iter.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transformAndSaveAimSatelliteParameter() {
        String strEdItude = transformLon();
        String aim_satellite_frequency = transformFrequency();
        String intStrStatMode = transformAimSatelliteMode();
        String intBandwidth = transformBandwidth();
        String intPolarization = transformPolarization();
        if (currentStar != null) {
            StarCodeModel dataSaved = isDataSaved();
            if (dataSaved == null) {
                currentStar.setMode(intStrStatMode);
                if (!"--".equals(et_aim_satellite_frequency.getText().toString())) {
                    currentStar.setFreq(aim_satellite_frequency);
                }
                if (!"--".equals(et_zai_bo_frequency.getText().toString())) {
                    currentStar.setZfreq(aim_satellite_frequency);
                }
                if (!"--".equals(et_center_frequency.getText().toString())) {
                    currentStar.setCenterFreq(et_center_frequency.getText().toString());
                }
                if (!"--".equals(et_symbol_rate.getText().toString())) {
                    currentStar.setSignRate(et_symbol_rate.getText().toString());
                }
                if (!"--".equals(et_satellite_longitude.getText().toString())) {
                    currentStar.setSatelon(strEdItude);
                }
                currentStar.setType(intPolarization);
                currentStar.setSessionToken(mToken);
                currentStar.setBw(intBandwidth);
                currentStar.setAmipSwitch(strAmipSwitch);
                currentStar.save();
            } else {
                dataSaved.setMode(intStrStatMode);
                if (!"--".equals(et_aim_satellite_frequency.getText().toString())) {
                    dataSaved.setFreq(ChechIpMask.numDigite(et_aim_satellite_frequency.getText().toString(), 2));
                }
                if (!"--".equals(et_zai_bo_frequency.getText().toString())) {
                    dataSaved.setZfreq(ChechIpMask.numDigite(et_zai_bo_frequency.getText().toString(), 2));
                }
                if (!"--".equals(et_center_frequency.getText().toString())) {
                    dataSaved.setCenterFreq(et_center_frequency.getText().toString());
                }
                if (!"--".equals(et_symbol_rate.getText().toString())) {
                    dataSaved.setSignRate(et_symbol_rate.getText().toString());
                }
                if (!"--".equals(et_satellite_longitude.getText().toString())) {
                    dataSaved.setSatelon(strEdItude);
                }
                dataSaved.setType(intPolarization);
                dataSaved.setSessionToken(mToken);
                dataSaved.setAmipSwitch(strAmipSwitch);
                dataSaved.setBw(intBandwidth);
                dataSaved.update(dataSaved.getId());
            }
            if (toaHashMap.get("星位参数保存成功") != null) {
                toaHashMap.get("星位参数保存成功").show();
            }
            SharedPreferenceManager.saveString(mContext, "currentStar",
                    GsonUtils.toJson(dataSaved == null ? currentStar
                            : dataSaved));
            updateSearchStarModels(dataSaved);
        }
    }

    @Nullable
    private String transformPolarization() {
        String strPolarization = sp_polarization_type.getSelectedItem().toString();// 接收极化
        String intPolarization = null;
        if (strPolarization.equals("垂直")) {
            intPolarization = "0";
        } else if (strPolarization.equals("水平")) {
            intPolarization = "1";
        }
        return intPolarization;
    }

    @Nullable
    private String transformBandwidth() {
        String strBandwidth = sp_zai_bo_bandwidth.getSelectedItem().toString();// 带宽
        String intBandwidth = null;
        if (strBandwidth.equals("25K")) {
            intBandwidth = "0";
        } else if (strBandwidth.equals("50K")) {
            intBandwidth = "1";
        } else if (strBandwidth.equals("100K")) {
            intBandwidth = "2";
        } else if (strBandwidth.equals("200K")) {
            intBandwidth = "3";
        } else if (strBandwidth.equals("400K")) {
            intBandwidth = "4";
        } else if (strBandwidth.equals("800K")) {
            intBandwidth = "5";
        } else if (strBandwidth.equals("1.6M")) {
            intBandwidth = "6";
        } else if (strBandwidth.equals("2M")) {
            intBandwidth = "7";
        } else if (strBandwidth.equals("4M")) {
            intBandwidth = "8";
        } else if (strBandwidth.equals("5M")) {
            intBandwidth = "9";
        } else if (strBandwidth.equals("6M")) {
            intBandwidth = "10";
        } else if (strBandwidth.equals("7M")) {
            intBandwidth = "11";
        } else if (strBandwidth.equals("8M")) {
            intBandwidth = "12";
        } else if (strBandwidth.equals("9M")) {
            intBandwidth = "13";
        } else if (strBandwidth.equals("10M")) {
            intBandwidth = "14";
        }
        return intBandwidth;
    }

    @Nullable
    private String transformAimSatelliteMode() {
        String intStrStatMode = null;
        if (starGetMode.equals(getString(R.string.xin_biao))) {
            intStrStatMode = XING_BIAO;
        } else if (starGetMode.equals(getString(R.string.zaibo_manual))) {
            intStrStatMode = ZAIBO_MANUAL;
        } else if (starGetMode.equals(getString(R.string.dvb))) {
            intStrStatMode = DVB;
        }
        return intStrStatMode;
    }

    @Nullable
    private String transformFrequency() {
        String aim_satellite_frequency = null;
        if (starGetMode.equals(getString(R.string.xin_biao))) {
            aim_satellite_frequency = ChechIpMask.numDigite(et_aim_satellite_frequency.getText().toString(), 2);
        } else if (starGetMode.equals(getString(R.string.zaibo_manual))) {
            aim_satellite_frequency = ChechIpMask.numDigite(et_zai_bo_frequency.getText().toString(), 2);
        }
        return aim_satellite_frequency;
    }

    @Nullable
    private String transformLon() {
        String _mEdItude = ChechIpMask.numDigite(et_satellite_longitude.getText().toString(), 2);
        String strEdItude = null;
        if (sp_longitude_type.getSelectedItemPosition() == 1) {
            strEdItude = "-" + _mEdItude;
        } else if (sp_longitude_type.getSelectedItemPosition() == 0) {
            strEdItude = _mEdItude;
        }
        return strEdItude;
    }

    /**
     * 验证输入合法性
     *
     * @return true 不合法 false合法
     */
    private boolean judgeAimSatelliteParameter() {
        if (!ChechIpMask.a2b(et_satellite_longitude.getText().toString(), 0, 180)) {
            Toast.makeText(SuperSetRefActivity.this, "对不起，您输入的经度不合法！", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (starGetMode.equals(getString(R.string.xin_biao))) {
            if (!ChechIpMask.a2b(et_aim_satellite_frequency.getText().toString(), 300, 3000)) {
                Toast.makeText(SuperSetRefActivity.this, "对不起，您输入的频率不合法！", Toast.LENGTH_SHORT).show();
                return true;
            }
        } else if (starGetMode.equals(getString(R.string.zaibo_manual))) {
            if (!ChechIpMask.a2b(et_zai_bo_frequency.getText().toString(), 10700, 12750)) {
                Toast.makeText(SuperSetRefActivity.this, "对不起，您输入的频率不合法！", Toast.LENGTH_SHORT).show();
                return true;
            }
        } else if (starGetMode.equals(getString(R.string.dvb))) {
            if (!ChechIpMask.a2b(et_center_frequency.getText().toString(), 11550, 12750)) {
                Toast.makeText(SuperSetRefActivity.this, "对不起，您输入的中心频率不合法！", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (!ChechIpMask.a2b(et_symbol_rate.getText().toString(), 0, 99999)) {
                Toast.makeText(SuperSetRefActivity.this, "对不起，您输入的符号率不合法！", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    private void setListViewHeight(ListView listView, Adapter adapter) {
        int height = ScreenUtil.dip2px(mContext, 42) * adapter.getCount();
        int screenH;
        if (adapter == lnbAdapter) {
            screenH = ScreenUtil.dip2px(mContext, 42) * 5;
            if (ll_buc_switch.getVisibility() == View.GONE) {
                screenH = ScreenUtil.dip2px(mContext, 42) * 3;
            }
        } else if (adapter == bucAdapter) {
            screenH = ScreenUtil.dip2px(mContext, 42) * 5;
        } else {
            screenH = (int) (ScreenUtil.getHight(mContext) * 0.55f);
        }
        int realH = height >= screenH ? screenH : height;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) listView.getLayoutParams();
        layoutParams.height = realH;
    }

    private void initDataView() {
        setSatelliteNumberText(currentStar.getSatename());
        if (lnbDataModel != null) {
            et_lnb_local_oscillator.setText(lnbDataModel.getLnb());
            et_lnb_local_oscillator.setSelection(lnbDataModel.getLnb().length());
        }
        if (bucDataModel != null) {
            et_buc_local_oscillator.setText(bucDataModel.getBucOscillator());
            et_buc_local_oscillator.setSelection(bucDataModel.getBucOscillator().length());
        }
        String _satelon = ChechIpMask.numDigite(currentStar.getSatelon(), 1);//保留一位有效数字。
        //SharedPreferenceManager.saveString(mContext, "satelon", _satelon);
        //如果带负号
        if (_satelon.substring(0, 1).equals("-") || _satelon.substring(0, 1).equals("﹣") || _satelon.substring(0, 1).equals("－") || _satelon.substring(0, 1).equals("﹣")) {

            _satelon = _satelon.substring(1);
            sp_longitude_type.setSelection(1);
        } else {
            sp_longitude_type.setSelection(0);
        }
        et_satellite_longitude.setText(_satelon);//卫星经度
        if (StringUtils.isEmpty(currentStar.getType())) {
            sp_polarization_type.setSelection(0);
        } else {
            sp_polarization_type.setSelection(Integer.parseInt(currentStar.getType()));
        }
        if (StringUtils.isEmpty(currentStar.getMode())) {
            sp_aim_satellite_mode.setSelection(0);
            initXinBiaoView();
        } else {
            if (Integer.parseInt(currentStar.getMode()) <= 2) {
                sp_aim_satellite_mode.setSelection(Integer.parseInt(currentStar.getMode()));
            }
            if (XING_BIAO.equals(currentStar.getMode())) {
                initXinBiaoView();
            } else if (ZAIBO_MANUAL.equals(currentStar.getMode())) {
                initZaiBoView();
            } else if (DVB.equals(currentStar.getMode())) {
                initDVBView();
            }
        }
    }

    private void setSatelliteNumberText(String satelliteNumber) {
        et_satellite_number.removeTextChangedListener(myTextWatcher);
        et_satellite_number.setText(satelliteNumber);
        et_satellite_number.setSelection(satelliteNumber.length());
        et_satellite_number.addTextChangedListener(myTextWatcher);
    }

    private void initXinBiaoView() {
        ll_aim_satellite_frequency.setVisibility(View.VISIBLE);
        ll_zai_bo_rate.setVisibility(View.GONE);
        ll_zai_bo_bandwidth.setVisibility(View.GONE);
        ll_center_frequency.setVisibility(View.GONE);
        ll_symbol_rate.setVisibility(View.GONE);
        if (currentStar != null) {
            et_aim_satellite_frequency.setText(currentStar.getFreq());
        }

    }

    private void initZaiBoView() {
        ll_aim_satellite_frequency.setVisibility(View.GONE);
        ll_zai_bo_rate.setVisibility(View.VISIBLE);
        ll_zai_bo_bandwidth.setVisibility(View.VISIBLE);
        ll_center_frequency.setVisibility(View.GONE);
        ll_symbol_rate.setVisibility(View.GONE);
        if (currentStar != null) {
            et_zai_bo_frequency.setText(currentStar.getZfreq());
        }
        if (currentStar != null && !StringUtils.isEmpty(currentStar.getBw())) {
            sp_zai_bo_bandwidth.setSelection(Integer.parseInt(currentStar.getBw()));
        }
    }

    private void initDVBView() {
        ll_center_frequency.setVisibility(View.VISIBLE);
        ll_symbol_rate.setVisibility(View.VISIBLE);
        ll_aim_satellite_frequency.setVisibility(View.GONE);
        ll_zai_bo_rate.setVisibility(View.GONE);
        ll_zai_bo_bandwidth.setVisibility(View.GONE);
        if (currentStar != null) {
            et_center_frequency.setText(currentStar.getCenterFreq());
            et_symbol_rate.setText(currentStar.getSignRate());
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initStarList();
                initLnbList();
                initBucList();
                reShowAddDialog();
                reShowDelDialog();
            }
        }, 50);
        if (pWindow != null && pWindow.isShowing()) {
            pWindow.dismiss();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pWindow.showAsDropDown(et_satellite_number);
                }
            }, 200);

        }
        if (pWindowLNB != null && pWindowLNB.isShowing()) {
            pWindowLNB.dismiss();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pWindowLNB.showAsDropDown(et_lnb_local_oscillator);
                }
            }, 200);

        }
        if (pWindowBUC != null && pWindowBUC.isShowing()) {
            pWindowBUC.dismiss();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pWindowBUC.showAsDropDown(et_buc_local_oscillator);
                }
            }, 200);

        }

    }

    private void reShowDelDialog() {
        if (bUCDelDialog != null && bUCDelDialog.isVisible()) {
            bUCDelDialog.dismiss();
            showBUCDelDialog(bUCDelItem);
        }
        if (starDelDialog != null && starDelDialog.isVisible()) {
            starDelDialog.dismiss();
            showDelDialog(startItem);
        }
        if (lNBDelDialog != null && lNBDelDialog.isVisible()) {
            lNBDelDialog.dismiss();
            showLBNDelDialog(lnbDataItem);
        }
    }

    private void reShowAddDialog() {
        if (addStarDialog != null && addStarDialog.isVisible()) {
            addStarDialog.dismiss();
            showAddDialog();
        }
        if (addBUCDialog != null && addBUCDialog.isVisible()) {
            addBUCDialog.dismiss();
            showAddBUCDialog();
        }
        if (addLNBDialog != null && addLNBDialog.isVisible()) {
            addLNBDialog.dismiss();
            showAddLNBDialog();
        }
    }

    class MyStarModeOnItemSelectedListener implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //	sp_aim_satellite_mode.getSelectedItem().toString();
            //Toast.makeText(SuperSetRefActivity.this,"sp_aim_satellite_mode.getSelectedItem().toString()------->"+ sp_aim_satellite_mode.getSelectedItem().toString(), 0).show();
            //settingView(true);
            if (sp_aim_satellite_mode.getSelectedItem().toString().equals(getString(R.string.xin_biao))) {
                initXinBiaoView();
            } else if (sp_aim_satellite_mode.getSelectedItem().toString().equals(getString(R.string.zaibo_manual))) {
                initZaiBoView();
            } else if (sp_aim_satellite_mode.getSelectedItem().toString().equals(getString(R.string.dvb))) {
                initDVBView();
            }
            starMode = sp_aim_satellite_mode.getSelectedItem().toString();
            SharedPreferenceManager.saveString(getApplicationContext(), "starmode", starMode);
            starGetMode = SharedPreferenceManager.getString(getApplicationContext(), "starmode");
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void saveZaiBoAutoConfig() {
        if (currentStar != null) {
            currentStar.setMode(ZAIBO_AUTO);
            currentStar.setSessionToken(mToken);
            currentStar.setAmipSwitch(strAmipSwitch);
            StarCodeModel dataSaved = isDataSaved();
            if (dataSaved == null) {
                currentStar.save();
            } else {
                dataSaved.setMode(ZAIBO_AUTO);
                dataSaved.setSessionToken(mToken);
                currentStar.setAmipSwitch(strAmipSwitch);
                dataSaved.update(dataSaved.getId());
            }
            SharedPreferenceManager.saveString(mContext, "currentStar",
                    GsonUtils.toJson(dataSaved == null ? currentStar
                            : dataSaved));
            updateSearchStarModels(dataSaved);
        }
    }

    private void settingView(boolean enable) {
        setEditextView(enable, et_satellite_number, et_satellite_longitude, et_zai_bo_frequency, et_aim_satellite_frequency);
        ViewUtil.setSpinner(mContext, enable, sp_longitude_type, sp_polarization_type, sp_zai_bo_bandwidth, sp_aim_satellite_mode);
        ViewUtil.setToggleButton(enable, tb_network_detection_switch);
        setDebugControl(enable);
        setButton(enable, bt_save_default);
    }

    private void setButton(boolean enable, ButtonBgUi... buttons) {
        if (buttons != null && buttons.length > 0) {
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setEnabled(enable);
            }
        }
    }

    private void setDebugControl(boolean enable) {
        if (enable) {
            ll_debug_model.setOnClickListener(this);
            iv_right_arrow.setBackground(getResources().getDrawable(R.drawable.back_compass));
        } else {
            ll_debug_model.setOnClickListener(null);
            iv_right_arrow.setBackground(getResources().getDrawable(R.drawable.running_statu_icon_back));
        }
    }


    private void setEditextView(boolean enable, EditText... editTexts) {
        if (editTexts != null && editTexts.length > 0) {
            for (int i = 0; i < editTexts.length; i++) {
                EditText editText = editTexts[i];
                editText.setFocusable(enable);
                editText.setFocusableInTouchMode(enable);
                editText.setTextColor(enable == false ? getResources().getColor(R.color.gray) : getResources().getColor(R.color.black));
               /* if(!enable){
                    editText.setText("--");
                }*/
                if (editText == et_satellite_number) {
                    if (enable) {
                        et_satellite_number.setOnTouchListener(myOnTouchListener);
                    } else {
                        et_satellite_number.setOnTouchListener(null);

                    }

                }
            }
        }

    }

    class MyOnTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if ("--".equals(et_satellite_number.getText().toString())) {
                        setSatelliteNumberText("");
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    pWindowBUC.dismiss();
                    pWindowLNB.dismiss();
                    pWindow.showAsDropDown(et_satellite_number);
                    break;

                default:
                    break;
            }
            return false;
        }
    }

    class MyLnbOnTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if ("--".equals(et_lnb_local_oscillator.getText().toString())) {
                        et_lnb_local_oscillator.setText("");
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    pWindow.dismiss();
                    pWindowBUC.dismiss();
                    pWindowLNB.showAsDropDown(et_lnb_local_oscillator);
                    break;

                default:
                    break;
            }
            return false;
        }
    }

    class MyBucOnTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if ("--".equals(et_buc_local_oscillator.getText().toString())) {
                        et_buc_local_oscillator.setText("");
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    pWindow.dismiss();
                    pWindowLNB.dismiss();
                    pWindowBUC.showAsDropDown(et_buc_local_oscillator);
                    break;

                default:
                    break;
            }
            return false;
        }
    }

    public void querySessionStatus(final OnSessionStatusListener onSessionStatusListener) {
        final String mToken = SharedPreferenceManager.getString(mContext, "mToken");
        String queryStatusUrl = XTHttpUtil.QUERY_STATUS;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sessionToken", mToken);
            JsonObjectRequest request = new JsonObjectRequest(Method.POST,
                    queryStatusUrl, jsonObject,
                    new Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                String code = jsonObject.getString("code");
                                if ("0".equals(code)) {
                                    //Session未失效
                                    onSessionStatusListener.sessionSuccess();
                                } else if ("-1".equals(code)) {
                                    //Session失效
                                    showLoginDialog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    onSessionStatusListener.sessionErrorResponse();
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0, 0f));
            mRequestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract class OnSessionStatusListener {
        public abstract void sessionSuccess();

        public void sessionErrorResponse() {

        }
    }

    public void showLoginDialog() {
        if(isVisible){
            ConfirmDialog.init().setConvertListener(new ViewConvertListener() {
                @Override
                public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                    holder.setText(R.id.tv_tip, getString(R.string.relogin_tip));
                    holder.setOnClickListener(R.id.bt_ok, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            application.exit();
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }).show(getSupportFragmentManager()).setOutCancel(false);
        }
        }

}

