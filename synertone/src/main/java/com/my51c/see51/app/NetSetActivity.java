package com.my51c.see51.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.came.viewbguilib.ButtonBgUi;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.my51c.see51.BaseActivity;
import com.my51c.see51.adapter.CommonAdapter;
import com.my51c.see51.adapter.NetSetAdapter;
import com.my51c.see51.app.bean.StarCodeModel;
import com.my51c.see51.app.domian.ResWifi;
import com.my51c.see51.app.http.SntAsyncHttpGet;
import com.my51c.see51.app.http.SntAsyncHttpGet.HpOverListener;
import com.my51c.see51.app.http.SntAsyncPost;
import com.my51c.see51.app.http.SntAsyncPost.PostOverHandle;
import com.my51c.see51.app.http.XTHttpUtil;
import com.my51c.see51.app.utils.SpinnerAdapter;
import com.my51c.see51.app.utils.XTHttpJSON;
import com.my51c.see51.common.AppData;
import com.my51c.see51.common.TextUtils;
import com.synertone.netAssistant.R;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class NetSetActivity extends BaseActivity implements
        OnCheckedChangeListener, OnClickListener, OnItemClickListener, OnItemSelectedListener  {

    protected static final String TAG = "NetSetActivity";
    private LinearLayout mNetwork_satellite, mNetwork_set_34g,
            mNetwork_set_wifi;// 分别是卫星网络、3g/4g公网、WiFi的线性布局
    private LinearLayout mNetwork_ll_setway_wifi;// 分别是卫星网络、3g/4g公网、WiFi隐藏的线性布局
    private LinearLayout mNetwork_set_liantong, mNetwork_set_yidong,
            mNetwork_set_dianxin;// 分别是3g/4g公网下的中国联通、移动、电信的线性布局
    private LinearLayout mNetwork_set_getchoose_weixing,mNetwork_set_getchoose_wifi;
    private List<ResWifi> mResWifiList;
    private ResWifi mResWifi=new ResWifi();
    private ToggleButton network_Satellite_OnOff, network_Sim_OnOff,
            network_Wifi_OnOff;
    private Spinner mNetwork_sp_choosestar, mNetset_sp_chooseway;// 分别为卫星网络中是sp和优先选择中的sp
    private Button mNetset_btn_duixing;// 卫星网络中的一键対星按钮
    private TextView mNetwork_tv_getstarname;// 卫星网络中sp中所选择的卫星名字
    private ImageView mMetwork_iv_getstarimage;// 卫星网络中sp中所选择的卫星图标
    private TextView mNetset_tv_titlewifi;
    private TextView mNetwork_tv_getsimname;// 3G、4G公网中选择的sim网络运营商的名字
    private ImageView mNetwork_iv_getsimimage;// 3G、4G公网中选择的sim网络运营商的图标

    private TextView mNetwork_tv_getwifiname;// 在WiFi选择的wifi名字
    private ImageView mNetwork_iv_getwifiimage;// 在WiFi选择的wifi对应的图标
    private ImageView mLiantong,mYidong,mDianxin;
    private SntAsyncHttpGet gettask;
    private SntAsyncPost postask;
    private String satestu="",simstu="",satename="";
    private JSONObject jsstate,jssim,jswifi;
    private int sateTag = 10,simTag = 10,wifiTag = 10,count = 0;
    private String recv="";
    private TextView mWeixingwangluo,mYingdongwnag,mWifi,mYouxian;
    private TextView mNetwork_tv_choosesatellite;
    private ButtonBgUi netset_btn_duixing;
    private ButtonBgUi netset_satestop;
    private TextView xuanzeyun,xz_liantong,xz_yidong,xz_dianxin,xz_wifi;
    private ListView mListview;//表示wifi存放的容器
    private NetSetAdapter adapter;
    private String simEnable, simNum, simType,wifiLink,simLink,wifiEnable,wifiSSID,satelliteNum;
    private String[] stingArray_sp2;
    protected List<Toast> toasts=new ArrayList<>();
    private HashMap<String, Toast> toaHashMap=new HashMap<>();
    private CommonAdapter<StarCodeModel> starAdapter;
    private EditText network_spinner_choosestar;
    protected StarCodeModel currentStar;
    private RelativeLayout rl_top_bar;
    private TextView mTittle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_set);
        initView();
        initToasts();
        initMyEvent();

    }
    @SuppressLint("ShowToast") @SuppressWarnings("unused")
    private void initToasts() {
        Toast toast1=Toast.makeText(NetSetActivity.this, "卫星链路连接成功", Toast.LENGTH_LONG);
        Toast toast2=Toast.makeText(NetSetActivity.this, "卫星链路连接失败", Toast.LENGTH_LONG);
        Toast toast3=Toast.makeText(NetSetActivity.this, "3G/4G公网连接成功", Toast.LENGTH_LONG);
        Toast toast4=Toast.makeText(NetSetActivity.this, "3G/4G公网连接失败", Toast.LENGTH_LONG);
        Toast toast5=Toast.makeText(NetSetActivity.this,  "对星成功", 0);
        Toast toast6=Toast.makeText(NetSetActivity.this,  "连接网元服务器故障", 0);
        Toast toast7=Toast.makeText(NetSetActivity.this,  "正在捕获", 0);
        Toast toast8=Toast.makeText(NetSetActivity.this,  "正在跟踪", 0);
        Toast toast9=Toast.makeText(NetSetActivity.this,  "查询对星 状态故障", 0);
        Toast toast10=Toast.makeText(NetSetActivity.this,   "开启3G/4G公网失败", 0);
        Toast toast11=Toast.makeText(NetSetActivity.this,    "开启3G/4G公网成功", 0);
        Toast toast12=Toast.makeText(NetSetActivity.this,   "关闭3G/4G公网失败", 0);
        Toast toast13=Toast.makeText(NetSetActivity.this,   "关闭3G/4G公网成功", 0);
        Toast toast14=Toast.makeText(NetSetActivity.this,   "关闭卫星链路失败", 0);
        Toast toast15=Toast.makeText(NetSetActivity.this,   "关闭卫星链路成功", 0);
        Toast toast16=Toast.makeText(NetSetActivity.this,   "开启卫星链路成功", 0);
        Toast toast17=Toast.makeText(NetSetActivity.this,   "开启卫星链路失败", 0);
        Toast toast18=Toast.makeText(NetSetActivity.this,   "关闭卫星链路失败", 0);
        toaHashMap.put("卫星链路连接成功", toast1);
        toaHashMap.put("卫星链路连接失败", toast2);
        toaHashMap.put("3G/4G公网连接成功", toast3);
        toaHashMap.put("3G/4G公网连接失败", toast4);
        toaHashMap.put("对星成功", toast5);
        toaHashMap.put("连接网元服务器故障", toast6);
        toaHashMap.put("正在捕获", toast7);
        toaHashMap.put("正在跟踪", toast8);
        toaHashMap.put("查询对星 状态故障", toast9);
        toaHashMap.put("开启3G/4G公网失败", toast10);
        toaHashMap.put("开启3G/4G公网成功", toast11);
        toaHashMap.put("关闭3G/4G公网失败", toast12);
        toaHashMap.put("关闭3G/4G公网成功", toast13);
        toaHashMap.put("关闭卫星链路失败", toast14);
        toaHashMap.put("关闭卫星链路成功", toast15);
        toaHashMap.put("开启卫星链路成功", toast16);
        toaHashMap.put("开启卫星链路失败", toast17);
        toaHashMap.put("关闭卫星链路失败", toast18);


    }

    private void initView() {
        rl_top_bar= (RelativeLayout) findViewById(R.id.rl_top_bar);
        mTittle= (TextView)findViewById(R.id.tv_bar_title);
        mTittle.setText(R.string.net_set);
        mWeixingwangluo=(TextView) findViewById(R.id.weixingwangluo_net);
        mWeixingwangluo.setTypeface(AppData.fontXiti);
        mYingdongwnag=(TextView) findViewById(R.id.yidongwang_net);
        mYingdongwnag.setTypeface(AppData.fontXiti);
        mWifi=(TextView) findViewById(R.id.wifi_net);
        mWifi.setTypeface(AppData.fontXiti);
        mYouxian=(TextView) findViewById(R.id.youxian_net);
        mYouxian.setTypeface(AppData.fontXiti);
        xuanzeyun=(TextView) findViewById(R.id.xuanzeyun);
        xuanzeyun.setTypeface(AppData.fontXiti);
        xz_liantong=(TextView) findViewById(R.id.xz_liantong);
        xz_liantong.setTypeface(AppData.fontXiti);
        xz_yidong=(TextView) findViewById(R.id.xz_yidong);
        xz_yidong.setTypeface(AppData.fontXiti);
        xz_dianxin=(TextView) findViewById(R.id.xz_dianxin);
        xz_dianxin.setTypeface(AppData.fontXiti);
        xz_wifi=(TextView) findViewById(R.id.netset_tv_titlewifi);
        xz_wifi.setTypeface(AppData.fontXiti);
        mListview=(ListView) findViewById(R.id.wifiListView);
        mNetwork_iv_getsimimage = (ImageView) findViewById(R.id.network_iv_getsimimage);// 得到的运营商的图标
        mNetwork_iv_getwifiimage = (ImageView) findViewById(R.id.network_iv_getwifiimage);// 得到的wifi的图标
        mLiantong=(ImageView) findViewById(R.id.liantong_iv);
        mYidong=(ImageView) findViewById(R.id.yidong_iv);
        mDianxin=(ImageView) findViewById(R.id.dianxin_iv);
        mNetset_tv_titlewifi=(TextView) findViewById(R.id.netset_tv_titlewifi);
        mNetwork_tv_getsimname = (TextView) findViewById(R.id.network_tv_getsimname);// 得到sim网络运营商的名字
        mNetwork_tv_getsimname.setTypeface(AppData.fontXiti);
        mNetwork_tv_getwifiname = (TextView) findViewById(R.id.network_tv_getwifiname);// 得到wifi网络的名字
        mNetset_sp_chooseway = (Spinner) findViewById(R.id.netset_spinner_chooseway);// 优先选择中的sp
        SpinnerAdapter mNetset_sp_chooseway_adapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.network_firstchoose));  //numbers
        mNetset_sp_chooseway.setAdapter(mNetset_sp_chooseway_adapter);

        network_Satellite_OnOff = (ToggleButton) findViewById(R.id.network_satellite_onoff);// 卫星网络
        network_Sim_OnOff = (ToggleButton) findViewById(R.id.network_sim_onoff);// 3G4G公网
        network_Wifi_OnOff = (ToggleButton) findViewById(R.id.network_wifi_onoff);// wifi

        mNetwork_satellite = (LinearLayout) findViewById(R.id.network_satellite);// 选择卫星线性布局
        mNetwork_set_34g = (LinearLayout) findViewById(R.id.network_set_34g);// 选择卫星线性布局
        mNetwork_set_wifi = (LinearLayout) findViewById(R.id.network_set_wifi);// 选择卫星线性布局

        mNetwork_ll_setway_wifi = (LinearLayout) findViewById(R.id.network_ll_setway_wifi);// 隐藏的选择WiFi线性布局
        mNetwork_set_liantong = (LinearLayout) findViewById(R.id.network_set_liantong);// 中国联通
        mNetwork_set_yidong = (LinearLayout) findViewById(R.id.network_set_yidong);// 中国移动
        mNetwork_set_dianxin = (LinearLayout) findViewById(R.id.network_set_dianxin);// 中国电信
        //mNetwork_set_getchoose_3g4g = (LinearLayout) findViewById(R.id.network_set_getchoose);// 隐藏选择运营商完成后显示线性布局
        mNetwork_set_getchoose_wifi = (LinearLayout) findViewById(R.id.network_get_wifi1);// 隐藏选择的wifi完成后显示线性布局

        network_Satellite_OnOff.setOnCheckedChangeListener(this);
        network_Sim_OnOff.setOnCheckedChangeListener(this);
        network_Wifi_OnOff.setOnCheckedChangeListener(this);

        //mNetwork_ll_setway_3g4g.setOnClickListener(this);
        mNetwork_ll_setway_wifi.setOnClickListener(this);

        //	mNetset_sp_chooseway.setOnItemClickListener(this);
        mNetset_sp_chooseway.setOnItemSelectedListener(this);
//		mNetwork_satellite.setOnClickListener(this);
//		mNetwork_set_34g.setOnClickListener(this);
//		mNetwork_set_wifi.setOnClickListener(this);


        mNetwork_set_liantong.setOnClickListener(this);
        mNetwork_set_yidong.setOnClickListener(this);
        mNetwork_set_dianxin.setOnClickListener(this);
	/*	mNetwork_set_getchoose_3g4g.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mNetwork_ll_choose_yunyingshang.setVisibility(View.GONE);
				showListDia();
			}
		});*/
        mResWifiList = new ArrayList<ResWifi>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 查询 卫星及3g通断
        NetSetUpUi();

    }

    private void initMyEvent() {
        rl_top_bar.setOnTouchListener(new ComBackTouchListener());
    }



    // ToggleButton的改变事件
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.network_satellite_onoff:
                if(10 == sateTag){
                    if (isChecked) {
                        SateOn();
//							mNetwork_ll_setway_weixing.setVisibility(View.VISIBLE);
                    }else {
                        SateOff();//关闭卫星网络
                    }
                }else{
                    //后台操作只改变标记的值
                    sateTag = 10;
                }

                break;
            case R.id.network_sim_onoff:
                if(10 == simTag){
                    if (isChecked) {
                        SimOn();
                        //mNetwork_ll_setway_3g4g.setVisibility(View.VISIBLE);
                        //mNetwork_ll_choose_yunyingshang.setVisibility(View.VISIBLE);
                        //simQuery();
                    }else {
                        SimOff();
                        //mNetwork_ll_setway_3g4g.setVisibility(View.GONE);
                    }
                }else{
                    simTag = 10;
                }
                break;
            case R.id.network_wifi_onoff:
                if(10 == wifiTag){
                    if (isChecked) {

                        //查询可用wifi
                        mNetwork_ll_setway_wifi.setVisibility(View.VISIBLE);
                        wifiQuery();

                    }else {
                        mNetwork_ll_setway_wifi.setVisibility(View.GONE);
                        wifiClose();
                        mResWifiList.clear();
                        //mListview=null;
                    }

                }else{
                    wifiTag = 10;
                }
                break;
            default:
                break;
        }
    }


    //关闭wifi请求
    private void wifiClose() {
        progresshow = true;
        showDia();
        postask = new SntAsyncPost();
        postask.SetListener(new PostOverHandle() {
            public void HandleData(JSONObject data) {
                try {
                    if(data.getString("code").equals("0")){
                        mNetwork_ll_setway_wifi.setVisibility(View.GONE);
                        mNetwork_set_getchoose_wifi.setVisibility(View.GONE);
                        if (progresshow) {
                            pd.dismiss();
                        }
                    }else{
                        //simTag = 10;
                        mNetwork_set_getchoose_wifi.setVisibility(View.VISIBLE);
                        network_Wifi_OnOff.setChecked(true);
                        if (progresshow) {
                            pd.dismiss();
                        }
                        Toast.makeText(NetSetActivity.this, "关闭wifi链路失败", 0).show();//,错误码:"+data.getString("code")
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        JSONObject wifi = new JSONObject();
        try {
            wifi.put("enable", "-1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postask.execute(XTHttpUtil.POST_NETSET_WIFICONNECT,wifi.toString());
    }

    //查询wifi
    private void wifiQuery() {


        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET, XTHttpUtil.GET_NETSET_WIFIQUERY,
                null, new RequestCallBack<JSONObject>() {

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        progresshow = true;
                        showDia();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        Log.i(TAG, "接收到数据-->" + arg0.reasonPhrase);
                        if (arg0.result.toString()==null) {
                            mNetset_tv_titlewifi.setText("无可用WiFi");
                        }
                        //	loginDataQuery(arg0);
                        pdDismiss(arg0.result+"");
                        JSONObject object;
                        try {
                            object = new JSONObject((String)arg0.result);
                            loginQuery(object);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private void loginQuery(JSONObject response) {
        if (XTHttpJSON.getJSONString(response.toString()).equals("0")) {


            try {
                int num = response.getInt("num");
                for (int i = 1; i <=num; i++) {
                    JSONObject wifiObject = response.getJSONObject("wifi" + i);
                    String name = wifiObject.getString("name");
                    String encryption = wifiObject.getString("encryption");
                    String strength = wifiObject.getString("strength");

                    mResWifi = new ResWifi();
                    mResWifi.setName(name);
                    mResWifi.setEncryption(encryption);
                    mResWifi.setStrength(strength);

                    mResWifiList.add(mResWifi);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            adapter=new NetSetAdapter(mResWifiList, getApplicationContext());
            mListview.setAdapter(adapter);//设置适配器
            mListview.setDivider(null);
            mListview.setOnItemClickListener(this);
        } else if (XTHttpJSON.getJSONString(response.toString()).equals("-1")) {
            Toast.makeText(NetSetActivity.this, "访问失败", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    // 34G打开之后的查询
    private void simQuery() {
        progresshow = true;
        showDia();
        JsonObjectRequest request = new JsonObjectRequest(Method.GET,
                XTHttpUtil.GET_NETSET_SIMQUERY, null,
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "接收到数据-->" + response.toString());
                        pdDismiss(response);

                        loginGetSim(response);
                        showListDia();
                    }
                }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.toString());
                Toast.makeText(NetSetActivity.this, "网络错误",
                        Toast.LENGTH_SHORT).show();
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                //THREE_FOUR_G_TAG = 2;
            }
        });
        AppData.mRequestQueue.add(request);
    }

    private String sim1type, sim2type, simEnadlePost;

    private String n_code, n_enable,n_simlink, n_simnum;//edit by hyw 20161212

    // 34G点击开始查询
    private void loginGetSim(JSONObject response) {
        if (XTHttpJSON.getJSONString(response.toString()).equals("0")) {
            try {
                sim1type = response.getString("sim1type");
                sim2type = response.getString("sim2type");

					/*//added by hyw 20161212
					n_code = response.getString("code");// // 0 成功   1 正在开启   -1 失败
					n_enable = response.getString("enable");//0 表示软件使能 -1表示软件关闭
					n_simlink = response.getString("simlink"); //0 移动网络连接  -1 移动网为连接
					n_simnum = response.getString("simnum"); //当前连接的sim卡槽  "simlink":"0"时有效
					Toast.makeText(NetSetActivity.this, "code:"+n_code+"--enable:"+n_enable+"--simlink:"+n_simlink+"--simnum:"+n_simnum, Toast.LENGTH_SHORT)
					.show();	*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (XTHttpJSON.getJSONString(response.toString()).equals("1")) {
            Toast.makeText(NetSetActivity.this, "正在开启", Toast.LENGTH_SHORT)
                    .show();
        } else if (XTHttpJSON.getJSONString(response.toString()).equals("-1")) {
            Toast.makeText(NetSetActivity.this, "访问失败", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private boolean isChecked=true;

    // 点击显示与隐藏控件

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.network_satellite:
//			if (mNetwork_ll_setway_weixing.getVisibility() == 0) {
//				mNetwork_ll_setway_weixing.setVisibility(View.GONE);
//				mNetwork_set_getchoose_weixing.setVisibility(View.GONE);
//			} else {
//				mNetwork_ll_setway_weixing.setVisibility(View.VISIBLE);
//			}

                break;
            case R.id.network_set_34g:
//			if (mNetwork_ll_setway_3g4g.getVisibility() == 0) {
//				mNetwork_ll_setway_3g4g.setVisibility(View.GONE);
//			} else {
//				mNetwork_ll_setway_3g4g.setVisibility(View.VISIBLE);
//			}

                break;

            case R.id.network_set_wifi:
//			if (mNetwork_ll_setway_wifi.getVisibility() == 0) {
//				mNetwork_ll_setway_wifi.setVisibility(View.GONE);
//			} else {
//				mNetwork_ll_setway_wifi.setVisibility(View.VISIBLE);
//			}
                //	break;
                //case R.id.netset_btn_duixing:


                //		ns_catch();



                break;
            default:
                break;
        }
    }
    // 有数据的时候ProgressDialog消失
    private void pdDismiss(Object object) {
        if (object != null) {
            pd.dismiss();
        }
    }
    //  页面聚焦 更新ui
    private void NetSetUpUi() {

        progresshow = true;
        showDia();
        gettask = new SntAsyncHttpGet();
        gettask.setFinishListener(new HpOverListener() {
            private JSONObject zifei;

            public void HpRecvOk(JSONObject data) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                try {

                    if (data.getString("code").equals("0")) {

                        //查询卫星
                        jsstate = new JSONObject(data.getString("satellite"));
                        if(jsstate.getString("enable").equals("0")){

                            //表示在此进入可点击下拉
                            satelliteNum = jsstate.getString("num");
                            Log.i("LYJ", "更新ui前 sateTag："+sateTag);
                            sateTag = 30;
                            network_Satellite_OnOff.setChecked(true);
                            //	mNetwork_satellite.setOnClickListener(NetSetActivity.this);
                           // mNetwork_ll_setway_weixing.setVisibility(View.VISIBLE);  //显示对星控件
                            //String s=jsstate.getString("satename");

                        }else if(jsstate.getString("enable").equals("-1")){
                            //   因为默认布局文件用的是关着的  所以不需要修改布局文件
                            //mNetwork_ll_setway_weixing.setVisibility(View.GONE);
                            //network_Satellite_OnOff.setChecked(false);
                        }
                        if(jsstate.getString("satelink").equals("0")){
                            if (toaHashMap.get("卫星链路连接成功")!=null){
                                toaHashMap.get("卫星链路连接成功").show();
                            }
                            //	UnableClick();

                            //  }else if(jsstate.getString("satelink").equals("1")) {
                            //	Toast.makeText(NetSetActivity.this, "正在捕获", 0).show();
                            // UnableClick();

                        }else {
                            if (toaHashMap.get("卫星链路连接失败")!=null){
                                toaHashMap.get("卫星链路连接失败").show();
                            }

                        }
                        //查询3,4g
                        //jssim = new JSONObject(data.getString("sim"));
                        JSONObject simObject = data.getJSONObject("sim");
                        simEnable = simObject.getString("enable");
                        simLink = simObject.getString("simlink");
                        if(simEnable.equals("0")){
                            simTag = 30;
                            network_Sim_OnOff.setChecked(true);
                            //	mNetwork_set_34g.setOnClickListener(NetSetActivity.this);
                            //mNetwork_ll_setway_3g4g.setVisibility(View.VISIBLE);
                        }else if(simEnable.equals("-1")){
                            //布局文件 默认创建页面时 就是关着的 所以不需要更新控件 也不需要更新 开关按钮的标签值
                            //mNetwork_ll_setway_3g4g.setVisibility(View.GONE);
                            //network_Sim_OnOff.setChecked(false);
                        }
                        if (simLink.equals("0")) {
                            if (toaHashMap.get("3G/4G公网连接成功")!=null){
                                toaHashMap.get("3G/4G公网连接成功").show();
                            }
                            simNum = simObject.getString("simnum");
                            simType = simObject.getString("simtype");
                        } else if (simLink.equals("-1")) {
                            if (toaHashMap.get("3G/4G公网连接失败")!=null){
                                toaHashMap.get("3G/4G公网连接失败").show();
                            }
                        }
                        //查询wifi
                        //jswifi=new JSONObject("wifi");
                        JSONObject wifiObject = data.getJSONObject("wifi");
                        wifiEnable = wifiObject.getString("enable");
                        wifiLink = wifiObject.optString("wifilink");
                        if (wifiEnable.equals("0")) {
                            wifiTag=30;
                            network_Wifi_OnOff.setChecked(true);
                            //mNetwork_set_wifi.setOnClickListener(NetSetActivity.this);
                            mNetwork_ll_setway_wifi.setVisibility(View.VISIBLE);
                        }
                        if (wifiEnable.equals("-1")) {
                            wifiTag=30;
                            //	network_Wifi_OnOff.setChecked(false);
                            mNetwork_ll_setway_wifi.setVisibility(View.GONE);
                        }
                        if (wifiLink.equals("0")) {
                            wifiSSID = wifiObject.getString("SSID");
                            Toast.makeText(NetSetActivity.this, "wifi连接成功", 0).show();
                        } else if (wifiLink.equals("-1")) {
                            Toast.makeText(NetSetActivity.this, "wifi连接失败", 0).show();
                        }else if(wifiLink.equals("1")){
                            Toast.makeText(NetSetActivity.this, "正在连接wifi", 0).show();
                        }
                        //查询资费
                        if(data.getString("policy").equals("0")) {
                            mNetset_sp_chooseway.setSelection(0);
                            //mNetset_sp_chooseway.getSelectedItem();
                            mNetset_sp_chooseway.getSelectedItemPosition();
                        }
                        if (data.getString("policy").equals("2")) {
                            mNetset_sp_chooseway.setSelection(1);
                            mNetset_sp_chooseway.getSelectedItem();
                        }


                    } else if(data.getString("code").equals("-100")){
                        if (toaHashMap.get("连接网元服务器故障")!=null){
                            toaHashMap.get("连接网元服务器故障").show();
                        }
                        //	EnavleClick();
                    } else{
                        Toast.makeText(NetSetActivity.this, "查询链路状态失败", 0).show();//错误码"+data.getString("code")
                        //	EnavleClick();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        gettask.execute(XTHttpUtil.GET_NETSET_STATUS);
    }

    //开启卫星链路接口
    private void SateOn(){
        progresshow = true;
        showDia();
        postask = new SntAsyncPost();
        postask.SetListener(new PostOverHandle() {
            @Override
            public void HandleData(JSONObject data) {
                // TODO Auto-generated method stub
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                try {
                    if(data.getString("code").equals(XTHttpUtil.NORESPONES)){
                        Toast.makeText(getApplicationContext(), "网元服务器无响应", 0).show();
                        return;
                    }
                    if(data.getString("code").equals("0")){
                        if (toaHashMap.get("开启卫星链路成功")!=null){
                            toaHashMap.get("开启卫星链路成功").show();
                        }
                       // mNetwork_ll_setway_weixing.setVisibility(View.VISIBLE);
                    }else {
                        if (toaHashMap.get("开启 卫星链路失败")!=null){
                            toaHashMap.get("开启 卫星链路失败").show();
                        }
                    }
                    return;
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        JSONObject sateon = new JSONObject();
        try {
            sateon.put("enable", "0");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        postask.execute(XTHttpUtil.POST_NETSET_SATEENABLE,sateon.toString());
    }



    //关闭卫星链路接口
    private void SateOff(){
        progresshow = true;
        showDia();
        postask = new SntAsyncPost();
        postask.SetListener(new PostOverHandle() {
            public void HandleData(JSONObject data) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                try {
                    if(data.getString("code").equals("0")){
                        if (toaHashMap.get("关闭卫星链路成功")!=null){
                            toaHashMap.get("关闭卫星链路成功").show();
                        }
                        //mNetwork_set_getchoose_weixing.setVisibility(View.GONE);
                        //mNetwork_ll_setway_weixing.setVisibility(View.GONE);

                    }else{
                        sateTag = 30;
                        if (toaHashMap.get("关闭卫星链路失败")!=null){
                            toaHashMap.get("关闭卫星链路失败").show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        JSONObject sate = new JSONObject();
        try {
            sate.put("enable", "-1");
            //sate.put("satenum", ""+(mNetwork_sp_choosestar.getSelectedItemPosition()+1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postask.execute(XTHttpUtil.POST_NETSET_SATEENABLE,sate.toString());
    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            Iterator<Entry<String, Toast>> iter = toaHashMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Toast> entry = iter.next();
                Toast toast=entry.getValue();
                Field field = toast.getClass().getDeclaredField("mTN");
                field.setAccessible(true);
                Object obj = field.get(toast);
                java.lang.reflect.Method m=obj.getClass().getDeclaredMethod("hide");
                m.invoke(obj);
                iter.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void SimOn(){
        progresshow = true;
        showDia();
        postask = new SntAsyncPost();
        postask.SetListener(new PostOverHandle() {
            public void HandleData(JSONObject data) {
                if (progresshow) {
                    pd.dismiss();
                }
                try {
                    if(data.getString("code").equals("0")){
                        if (toaHashMap.get("开启3G/4G公网成功")!=null){
                            toaHashMap.get("开启3G/4G公网成功").show();
                        }
                        //mNetwork_set_getchoose_3g4g.setVisibility(View.VISIBLE);
                        //mNetwork_ll_setway_3g4g.setVisibility(View.VISIBLE);
                        mYidong.setBackgroundResource(R.drawable.yidong);
                        mLiantong.setBackgroundResource(R.drawable.liantong);
                        mDianxin.setBackgroundResource(R.drawable.dianxin_checked);
                        mNetwork_tv_getsimname.setText("中国电信");
                        mNetwork_iv_getsimimage.setBackgroundResource(R.drawable.dianxin_checked);
                    }else{
                        simTag = 10;
                        if (toaHashMap.get("开启3G/4G公网失败")!=null){
                            toaHashMap.get("开启3G/4G公网失败").show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        JSONObject sim = new JSONObject();
        try {
            sim.put("enable", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postask.execute(XTHttpUtil.POST_NETSET_SIMENABLE,sim.toString());
    }

    private void SimOff(){
        progresshow = true;
        showDia();
        postask = new SntAsyncPost();
        postask.SetListener(new PostOverHandle() {
            public void HandleData(JSONObject data) {
                if (progresshow) {
                    pd.dismiss();
                }
                try {
                    if(data.getString("code").equals("0")){
                        if (toaHashMap.get("关闭3G/4G公网成功")!=null){
                            toaHashMap.get("关闭3G/4G公网成功").show();
                        }
                    }else{

                        simTag = 30;
                        if (toaHashMap.get("关闭3G/4G公网失败")!=null){
                            toaHashMap.get("关闭3G/4G公网失败").show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        JSONObject sim = new JSONObject();
        try {
            sim.put("enable", "-1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postask.execute(XTHttpUtil.POST_NETSET_SIMENABLE,sim.toString());
    }




    public void netSetOnFinish(View view) {
        finish();
    }


    // 加载数据的 ProgressDialog
    private ProgressDialog pd;
    private boolean progresshow;

    private void showDia() {
        if(!isVisible){
            return;
        }
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progresshow = false;
            }
        });
        pd.setMessage("正在加载数据。。。。。");
        pd.show();
    }
    /* 列表对话框 */
    private void showListDia() {
        if (sim1type != null || sim2type != null) {
            final String[] mList = { sim1type, sim2type };
            AlertDialog.Builder listDia = new AlertDialog.Builder(this);
            listDia.setItems(mList, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
					/* 下标是从0开始的 */
					/*
					 * if (mList[which].equals("CDMA")) { simNum = "1"; } if
					 * (mList[which].equals("TD-SCDMA")) { simNum = "2"; }
					 * if(mList[which].equals("WCDMA")){ simNum="3"; }
					 */
                    JSONObject object = XTHttpJSON.postSimEnable(simEnadlePost,
                            which + 1 + "");
                    //postSimEnable(object);
                    httpPostRequest(XTHttpUtil.POST_NETSET_SIMENABLE,object.toString());
                    showClickMessage(mList[which]);
                }
            });
            listDia.create().show();
        } else {
            showClickMessage("加载的数据为空");
            // return;
        }
    }
    /* 显示点击然后显示更新UI */
    private void showClickMessage(String message) {

        //判断是哪种运营商，进行UI设置(目前只有判断3G网络类型模式)
        if (message.equals("TDS-CDMA")||message.equals("TD-SCDMA")) {
            //	Toast.makeText(getApplicationContext(), message+">>>>", 0).show();
            //mNetwork_set_getchoose_3g4g.setVisibility(View.VISIBLE);
            mYidong.setBackgroundResource(R.drawable.yidong_checked);
            mLiantong.setBackgroundResource(R.drawable.liantong);
            mDianxin.setBackgroundResource(R.drawable.dianxin);
            mNetwork_tv_getsimname.setText("中国移动");
            mNetwork_iv_getsimimage.setBackgroundResource(R.drawable.yidong_checked);
        }
        //||message.equals("TD-LTE")||message.equals("FDD-LTE")
        if (message.equals("CDMA2000")) {
            //mNetwork_set_getchoose_3g4g.setVisibility(View.VISIBLE);
            mYidong.setBackgroundResource(R.drawable.yidong);
            mLiantong.setBackgroundResource(R.drawable.liantong);
            mDianxin.setBackgroundResource(R.drawable.dianxin_checked);
            mNetwork_tv_getsimname.setText("中国电信");
            mNetwork_iv_getsimimage.setBackgroundResource(R.drawable.dianxin_checked);
        }
        if (message.equals("WCDMA")) {
            //mNetwork_set_getchoose_3g4g.setVisibility(View.VISIBLE);
            mYidong.setBackgroundResource(R.drawable.yidong);
            mLiantong.setBackgroundResource(R.drawable.liantong_checked);
            mDianxin.setBackgroundResource(R.drawable.dianxin);
            mNetwork_tv_getsimname.setText("中国联通");
            mNetwork_iv_getsimimage.setBackgroundResource(R.drawable.liantong_checked);
        }
    }
    private void httpPostRequest(String url, String jsonObjet) {
        RequestParams params = new RequestParams("UTF-8");
        try {
            params.setBodyEntity(new StringEntity(jsonObjet, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.setContentType("applicatin/json");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, params,
                new RequestCallBack<JSONObject>() {

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        progresshow = true;
                        showDia();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                    }


                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        Log.i(TAG, "接收到数据-->" + arg0.reasonPhrase);
//						Toast.makeText(getApplicationContext(),
//								arg0.result + "11111111111111111", 0).show();
                        pdDismiss(arg0);
                        // loginDataQuery(arg0);
                        String object;
                        object= arg0.result.toString();
                        //Toast.makeText(getApplicationContext(), "这里的数据为："+object, 1).show();
                        UpDataUI(object);

                    }
                });
    }


    protected void UpDataUI(String object) {

        if (XTHttpJSON.getJSONString(object)
                .equals("0")) {
            Toast.makeText(NetSetActivity.this, "选择成功", 0)
                    .show();
            //mNetwork_ll_choose_yunyingshang.setVisibility(View.GONE);//选择成功，隐藏运营商选择布局
        }

    }



    protected void loginDataQuery(String string) {

    }

    private String ssidpass;//表示输入框输入的字符串
    private String wifiName;//表示wifi名字
    private String strength;//表示wifi信号强度
    @Override
    public void onItemClick(AdapterView<?> parent, View view,  int position,
                            long id) {
        //这里是item点击相关事件
        //Toast.makeText(getApplicationContext(), mResWifiList.get(position).getName()+".....", 0).show();
        wifiName= mResWifiList.get(position).getName().toString();
        strength=mResWifiList.get(position).getStrength().toString();
        //Toast.makeText(getApplicationContext(), "点击了第"+position+"条", 0).show();
        AlertDialog.Builder customDia = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater=getLayoutInflater();
        final View viewDia = layoutInflater.inflate(
                R.layout.base_custom_dialog, null);
        final ResWifi wifi = mResWifiList.get(position);
        customDia.setTitle(wifi.getName());
        customDia.setView(viewDia);
        customDia.setPositiveButton("连接",
                new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText diaInput = (EditText) viewDia
                                .findViewById(R.id.txt_cusDiaInput);
                        if (!TextUtils.isEmpty(diaInput.getText().toString())) {

                            ssidpass = diaInput.getText().toString().trim();
                            if (ssidpass.length()<8) {
                                Toast.makeText(getApplicationContext(), "密码长度至少为8", 0).show();
                                return;
                            }
                        }
                        final JSONObject jsonObject = XTHttpJSON.postWifiConnect(
                                wifi.getName().toString(), ssidpass);
                        wifiDataListPost(jsonObject);
                    }
                });
        customDia.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
        customDia.create().show();

    }
    //连接wifi
    private void wifiDataListPost(JSONObject object) {

        RequestParams params = new RequestParams("UTF-8");
        try {
            params.setBodyEntity(new StringEntity(object.toString(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.setContentType("applicatin/json");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, XTHttpUtil.POST_NETSET_WIFICONNECT,
                params, new RequestCallBack<JSONObject>() {



                    @Override
                    public void onLoading(long total,  long current,
                                          boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        progresshow = true;
                        showDia();
                    }
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        if (pd.isShowing()) {
                            pd.dismiss();
                            return;
                        }
                    }
                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        Log.i(TAG, "接收到数据-->" + arg0.reasonPhrase);
                        //Toast.makeText(getApplicationContext(),arg0.result+"" , 0).show();
                        //	loginDataQuery(arg0);
                        pdDismiss(arg0.result+"");
	                       /* JSONObject object;
							try {
								object = new JSONObject((String)arg0.result);
								loginQuery(object);
							} catch (JSONException e) {
								e.printStackTrace();
							}*/
                        //这里显示隐藏wifi的布局
                        mNetwork_set_getchoose_wifi.setVisibility(View.VISIBLE);
                        mNetwork_tv_getwifiname.setText(wifiName);
                        //mNetwork_iv_getwifiimage.setBackgroundResource();
                        if (strength.equals("1")||strength.equals("2")) {

                            mNetwork_iv_getwifiimage.setBackgroundResource(R.drawable.netset_wifi1);
                        }else if (strength.equals("3")) {
                            mNetwork_iv_getwifiimage.setBackgroundResource(R.drawable.netset_wifi2);
                        }
                        else if (strength.equals("4")) {
                            mNetwork_iv_getwifiimage.setBackgroundResource(R.drawable.netset_wifi3);
                        }
                        else if (strength.equals("5")) {
                            mNetwork_iv_getwifiimage.setBackgroundResource(R.drawable.netset_wifi4);
                        }
                    }
                });
    }
    private void postSimEnable(JSONObject object) {
        progresshow = true;
        showDia();
        JsonObjectRequest request = new JsonObjectRequest(Method.POST,
                XTHttpUtil.POST_NETSET_SIMENABLE, object,
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "接收到数据-->" + response.toString());
                        pdDismiss(response);
                        if (XTHttpJSON.getJSONString(response.toString())
                                .equals("0")) {
                            Toast.makeText(NetSetActivity.this, "访问成功", 0)
                                    .show();
                            //	UpDataUI(response);
                        } else if (XTHttpJSON
                                .getJSONString(response.toString())
                                .equals("-1")) {
                            Toast.makeText(NetSetActivity.this, "访问失败", 0)
                                    .show();
                        }
                    }
                }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.toString());
                Toast.makeText(NetSetActivity.this, "网络错误",
                        Toast.LENGTH_SHORT).show();
            }
        });
        AppData.mRequestQueue.add(request);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mResWifi!=null) {
            mResWifi=null;
        }
    }
    String policyVal;//选择方式资费
    String policyEnStr;//表示开启或者关闭资费方式
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
		/*
		TextView v=(TextView) view;
		v.setGravity(Gravity.CENTER_HORIZONTAL);
		policyEnStr="0";

		policyVal=mNetset_sp_chooseway.getSelectedItemPosition()+"";
		//Toast.makeText(getApplicationContext(), ">>>>"+mNetset_sp_chooseway.getSelectedItemPosition(), 0).show();
		JSONObject object = XTHttpJSON.postPolicyslc(
				policyEnStr, policyVal);
		postPolicyEnable(object);
		*/
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    // Post Policyslc 带宽资费选择
    private void postPolicyEnable(JSONObject object) {
        progresshow = true;
        showDia();
        JsonObjectRequest request = new JsonObjectRequest(Method.POST,
                XTHttpUtil.POST_NETSET_POLICYSLC, object,
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "接收到数据-->" + response.toString());
                        pdDismiss(response);
                        if (XTHttpJSON.getJSONString(response.toString())
                                .equals("0")) {
                            //Toast.makeText(NetSetActivity.this, "设置成功", 0).show();  //0815-del 屏蔽不必要的toast
                        } else if (XTHttpJSON
                                .getJSONString(response.toString())
                                .equals("-1")) {
                            //Toast.makeText(NetSetActivity.this, "设置失败", 0).show();
                        }
                    }
                }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.toString());
                Toast.makeText(NetSetActivity.this, "网络错误",
                        Toast.LENGTH_SHORT).show();
            }
        });
        AppData.mRequestQueue.add(request);
    }
    private void EnableClick() {
        //Toast.makeText(getApplicationContext(), "対星失败，请重新尝试！", 1).show();
        netset_btn_duixing.setClickable(true);
        //mNetwork_sp_choosestar.setEnabled(true);
    }
    private void UnableClick() {//
        //Toast.makeText(getApplicationContext(), "正在对星，请重新尝试！", 1).show();
        netset_btn_duixing.setClickable(false);
        //mNetwork_sp_choosestar.setEnabled(false);
    }

}

