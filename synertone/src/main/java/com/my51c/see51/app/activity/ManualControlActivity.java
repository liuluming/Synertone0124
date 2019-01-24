package com.my51c.see51.app.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.came.viewbguilib.ButtonBgUi;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.my51c.see51.adapter.CommonAdapter;
import com.my51c.see51.adapter.CommonViewHolder;
import com.my51c.see51.adapter.MySpinnerAdapter;
import com.my51c.see51.app.bean.DataModel;
import com.my51c.see51.app.bean.StarCodeModel;
import com.my51c.see51.app.http.XTHttpUtil;
import com.my51c.see51.app.utils.ChechIpMask;
import com.my51c.see51.app.utils.GsonUtils;
import com.my51c.see51.app.utils.PackageUtil;
import com.my51c.see51.app.utils.ViewUtil;
import com.my51c.see51.app.utils.XTHttpJSON;
import com.my51c.see51.common.AppData;
import com.my51c.see51.common.MyDensityUtil;
import com.my51c.see51.widget.NiceDialog;
import com.my51c.see51.widget.SharedPreferenceManager;
import com.synertone.commonutil.util.ScreenUtil;
import com.synertone.commonutil.view.BaseNiceDialog;
import com.synertone.commonutil.view.ConfirmCancelDialog;
import com.synertone.commonutil.view.ViewConvertListener;
import com.synertone.commonutil.view.ViewHolder;
import com.synertone.netAssistant.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.synertone.netAssistant.R.id.super_ref_numner;

public class ManualControlActivity extends FragmentActivity implements OnClickListener {
    private ImageView imageButton1;
    private EditText mSatelliteNum,super_ref_itude;
    private TextView dangqiaojindu_tv, dangqiaoweidu_tv, mubiaojihua_tv, mubiaofangwei_tv, mubiaofuyang_tv;
    private LinearLayout mLinearLayoutCompass;
    private AppData application;
    private ButtonBgUi debug_btn;
    private String mModemIp,sateName,strCurrentLongItude1,strEdItude;
    private double mSatelliteLongitude;
    private double mLongitude, mLatitude;
    private Gson gson;
    private TextView super_reftv, dangqiaojin, dangqianweidu, mubiaojihua,
            mubiaofangwei, mubiaofuyang, capability, receice_level, mReceiveLevel, mCapability;
    private String mRx, maxRx, mTx, maxTx, curlon, currlat, mLongitudeS, mLatitudeS,mToken;
    private ProgressBar pb_progressbar, pb_progressbar1;
    private Spinner mDQJD, mDQWD, sp_mbjh, sp_fwmb, sp_mbfy,mDQJD1,mWXJD;
    private int messageDelay = 900;
    private HttpUtils http;
    private PopupWindow pWindow;
    private ListView starListView;
    private List<StarCodeModel> starCodeModels = new ArrayList<>();
    private List<StarCodeModel> searchStarModels = new ArrayList<>();
    private CommonAdapter<StarCodeModel> starAdapter;
    private ButtonBgUi bt_save;
    protected StarCodeModel currentStar;
    private ManualControlActivity.MyTextWatcher myTextWatcher;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                getCatParameter();
            }
        }
    };
    private String packageName;
    private RelativeLayout rl_top_bar;
    private TextView mTittle;
    private ManualControlActivity mContext;
    private BaseNiceDialog addStarDialog;
    private BaseNiceDialog starDelDialog;
    private int startItemPosition;
    private StarCodeModel startItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_control);
        mContext=this;
        application = (AppData) getApplication();
        if(AppData.accountModel!=null) {
            mToken = AppData.accountModel.getSessionToken();
        }
        initHttpData();
        initView();
        List<PackageInfo> insatalledPackages = PackageUtil.getInsatalledPackages(this);
        for (PackageInfo po : insatalledPackages) {
            String pn = po.packageName;
            if (pn.endsWith(".compass")) {
                packageName = pn;
                break;
            }

        }
        String savedStar = SharedPreferenceManager.getString(mContext,
                "currentStar");
        if (savedStar != null) {
            currentStar = GsonUtils.fromJson(savedStar, StarCodeModel.class);
            if (currentStar != null) {
                try {
                    initDataView();
                    initStarList();
                } catch (Exception e) {
                    initStarList();
                    e.printStackTrace();
                }
            }
        }else{
            initStarList();
        }
        initEvent();
    }

    private void initEvent() {
        mSatelliteNum.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if ("--".equals(mSatelliteNum.getText().toString())) {
                            mSatelliteNum.setText("");
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        pWindow.showAsDropDown(mSatelliteNum);
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
        myTextWatcher = new ManualControlActivity.MyTextWatcher();
        mSatelliteNum.addTextChangedListener(myTextWatcher);
        rl_top_bar.setOnTouchListener(new ComBackTouchListener());
    }
    public class ComBackTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float downX=event.getX();
            float downY = event.getY();
            if(event.getAction()==MotionEvent.ACTION_UP){
                int maxX= MyDensityUtil.dip2px(130);
                int maxY=MyDensityUtil.dip2px(getResources().getDimension(R.dimen.bar_height));
                if(downX<=maxX &&downY<maxY){
                    application.removeAct(mContext);
                    finish();
                }
            }
            return false;
        }
    }
    private void initView() {
        rl_top_bar= (RelativeLayout) findViewById(R.id.rl_top_bar);
        mTittle= (TextView)findViewById(R.id.tv_bar_title);
        mTittle.setText(R.string.auto_star);
        application = (AppData) getApplication();
        mDQJD = (Spinner) findViewById(R.id.super_ref_mdqjd);
        mDQJD.setEnabled(false);
        mDQJD.setAdapter(new MySpinnerAdapter(this,
                R.layout.spinner_stytle, getResources().getStringArray(R.array.e_w)));//当前经度
        mDQWD = (Spinner) findViewById(R.id.super_ref_mdqwd);
        mDQWD.setEnabled(false);
        mDQWD.setAdapter(new MySpinnerAdapter(this,
                R.layout.spinner_stytle, getResources().getStringArray(R.array.n_s)));//当前纬度
        sp_mbjh = (Spinner) findViewById(R.id.sp_mbjh);
        sp_mbjh.setEnabled(false);
        sp_mbjh.setAdapter(new MySpinnerAdapter(this,
                R.layout.spinner_stytle, getResources().getStringArray(R.array.n_jh)));//目标极化角
        sp_fwmb = (Spinner) findViewById(R.id.sp_fwmb);
        sp_fwmb.setEnabled(false);
        sp_fwmb.setAdapter(new MySpinnerAdapter(this,
                R.layout.spinner_stytle, getResources().getStringArray(R.array.n_fw)));//目标方位角
        sp_mbfy = (Spinner) findViewById(R.id.sp_mbfy);
        sp_mbfy.setEnabled(false);
        sp_mbfy.setAdapter(new MySpinnerAdapter(this,
                R.layout.spinner_stytle, getResources().getStringArray(R.array.n_fy)));//目标俯仰角
        if(String.valueOf(application.mLatLng.longitude)!=null){
            mLongitude = application.mLatLng.longitude;
        }
        curlon = (String.valueOf(mLongitude));
        if (curlon.substring(0, 1).equals("-") || curlon.substring(0, 1).equals("﹣") || curlon.substring(0, 1).equals("－") || curlon.substring(0, 1).equals("﹣")) {
            mDQJD.setSelection(1);
        } else {
            mDQJD.setSelection(0);

        }
        if(String.valueOf(application.mLatLng.latitude)!=null) {
            mLatitude = application.mLatLng.latitude;
        }
        currlat = (String.valueOf(mLatitude));
        if (currlat.substring(0, 1).equals("-") || currlat.substring(0, 1).equals("﹣") || currlat.substring(0, 1).equals("－") || currlat.substring(0, 1).equals("﹣")) {
            mDQWD.setSelection(1);
        } else {
            mDQWD.setSelection(0);

        }
        //imageButton1 = (ImageView) findViewById(R.id.imageButton1);
        dangqiaojindu_tv = (TextView) findViewById(R.id.dangqiaojindu_tv);
        mLongitudeS = String.valueOf(XTHttpJSON.decimalFormat.format(mLongitude));
        if ("0.00".equals(mLongitudeS)) {
            dangqiaojindu_tv.setText("--");
        } else {
            dangqiaojindu_tv.setText(String.valueOf(XTHttpJSON.decimalFormat.format(mLongitude)));
        }
        dangqiaoweidu_tv = (TextView) findViewById(R.id.dangqianweidu_tv);
        mLatitudeS = String.valueOf(XTHttpJSON.decimalFormat.format(mLatitude));
        if ("0.00".equals(mLatitudeS)) {
            dangqiaoweidu_tv.setText("--");
        } else {
            dangqiaoweidu_tv.setText(String.valueOf(XTHttpJSON.decimalFormat.format(mLatitude)));
        }
        mubiaojihua_tv = (TextView) findViewById(R.id.mubiaojihua_tv);
        mubiaofangwei_tv = (TextView) findViewById(R.id.mubiaofangwei_tv);
        mubiaofuyang_tv = (TextView) findViewById(R.id.mubiaofuyang_tv);
        dangqiaojin = (TextView) findViewById(R.id.dangqiaojin);
        dangqiaojin.setTypeface(AppData.fontXiti);
        dangqianweidu = (TextView) findViewById(R.id.dangqianweidu);
        dangqianweidu.setTypeface(AppData.fontXiti);
        mubiaojihua = (TextView) findViewById(R.id.mubiaojihua);
        mubiaojihua.setTypeface(AppData.fontXiti);
        mubiaofangwei = (TextView) findViewById(R.id.mubiaofangwei);
        mubiaofangwei.setTypeface(AppData.fontXiti);
        mubiaofuyang = (TextView) findViewById(R.id.mubiaofuyang);
        mubiaofuyang.setTypeface(AppData.fontXiti);
        capability = (TextView) findViewById(R.id.capability);
        receice_level = (TextView) findViewById(R.id.receice_level);
        debug_btn = (ButtonBgUi) findViewById(R.id.debug_btn);
        debug_btn.setTypeface(AppData.fontXiti);
        mCapability = (TextView) findViewById(R.id.tv_capability);
        mReceiveLevel = (TextView) findViewById(R.id.tv_receive_level);
        pb_progressbar = (ProgressBar) findViewById(R.id.pb_progressbar);
        pb_progressbar1 = (ProgressBar) findViewById(R.id.pb_progressbar1);
        mSatelliteNum = (EditText) findViewById(R.id.super_ref_numner);// 卫星编号
        bt_save = (ButtonBgUi) findViewById(R.id.bt_save);
        bt_save.setTypeface(AppData.fontXiti);
        mLinearLayoutCompass = (LinearLayout) findViewById(R.id.ll_compass);
        mLinearLayoutCompass.setOnClickListener(this);
        debug_btn.setOnClickListener(this);
       /* imageButton1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        bt_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStarPara();
            }
        });
        mWXJD=(Spinner) findViewById(R.id.super_ref_mwxjd);
        super_ref_itude = (EditText) findViewById(R.id.super_ref_itude);
        mWXJD.setAdapter(new MySpinnerAdapter(this,
                R.layout.spinner_stytle,getResources().getStringArray(R.array.e_w)));
        getModemIp();
    }
    private void initDataView() {
        mSatelliteNum.setText(currentStar.getSatename());
        mSatelliteNum.setSelection(currentStar.getSatename().length());
        String _satelon = ChechIpMask.numDigite(currentStar.getSatelon(), 1);//保留一位有效数字。
        //SharedPreferenceManager.saveString(mContext, "satelon", _satelon);
        //如果带负号
        if (_satelon.substring(0, 1).equals("-") || _satelon.substring(0, 1).equals("﹣") || _satelon.substring(0, 1).equals("－") || _satelon.substring(0, 1).equals("﹣")) {

            _satelon = _satelon.substring(1);
            mWXJD.setSelection(1);
        } else {
            mWXJD.setSelection(0);
        }
        super_ref_itude.setText(_satelon);//卫星经度
        mSatelliteLongitude=Double.parseDouble(_satelon);
    }
    private void initHttpData() {
        gson = new Gson();
        http = new HttpUtils();
        http.configTimeout(5 * 1000);
        http.configSoTimeout(5 * 1000);
        http.configCurrentHttpCacheExpiry(500);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_compass:
                if (!PackageUtil.startAppByPackageName(ManualControlActivity.this, packageName)) {
                    Intent mIntent = new Intent(ManualControlActivity.this, CompassActivity.class);
                    startActivity(mIntent);
                }
                break;
            case R.id.debug_btn:
                calculatedValue();
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getModemIp() {
        String getModemUrl = XTHttpUtil.GET_MODEM_IP;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getModemUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if (code.equals("0")) {
                        //Toast.makeText(ManualControlActivity.this, "获取ip地址成功", 0).show();
                    } else {
                        //Toast.makeText(ManualControlActivity.this, "获取ip地址失败", 0).show();
                    }
                    mModemIp = jsonObject.getString("ip");
                    if (mModemIp != null) {
                        getCatParameter();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
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
        AppData.mRequestQueue.add(stringRequest);
    }

    private void calculatedValue() {
        //注意反正切函数返回的是弧度制的值, 这里重新转成了角度值,加Math.toDegrees.
        double a = mLatitude;
        double b = mSatelliteLongitude - mLongitude;
        double A = Math.toDegrees(Math.atan(Math.tan(b * Math.PI / 180) / Math.sin(a * Math.PI / 180)));
        String mubiaofangwei_value = XTHttpJSON.decimalFormat.format(A);
        if (mubiaofangwei_value.substring(0, 1).equals("-") || mubiaofangwei_value.substring(0, 1).equals("﹣") || mubiaofangwei_value.substring(0, 1).equals("－") || mubiaofangwei_value.substring(0, 1).equals("﹣")) {
            sp_fwmb.setSelection(2);
        } else {
            sp_fwmb.setSelection(1);

        }
        if ("0.00".equals(mLongitudeS) && "0.00".equals(mLatitudeS)) {
            mubiaofangwei_tv.setText("--");
        } else {
            mubiaofangwei_tv.setText(String.valueOf(Math.abs(Double.parseDouble(mubiaofangwei_value))));
        }
        double E = Math.toDegrees(Math.atan((Math.cos(a * Math.PI / 180) * Math.cos(b * Math.PI / 180) - 0.151)
                / Math.sqrt(1 - Math.cos(a * Math.PI / 180) * Math.cos(b * Math.PI / 180) * Math.cos(a * Math.PI / 180) * Math.cos(b * Math.PI / 180))));
        String mubiaofuyang_value = XTHttpJSON.decimalFormat.format(E);
        if (mubiaofuyang_value.substring(0, 1).equals("-") || mubiaofuyang_value.substring(0, 1).equals("﹣") || mubiaofuyang_value.substring(0, 1).equals("－") || mubiaofuyang_value.substring(0, 1).equals("﹣")) {
            sp_mbfy.setSelection(2);
        } else {
            sp_mbfy.setSelection(1);

        }
        if ("0.00".equals(mLongitudeS) && "0.00".equals(mLatitudeS)) {
            mubiaofuyang_tv.setText("--");
        } else {
            mubiaofuyang_tv.setText(String.valueOf(Math.abs(Double.parseDouble(mubiaofuyang_value))));
        }
        double P = Math.toDegrees(Math.atan(Math.sin((b) * Math.PI / 180) / Math.tan(a * Math.PI / 180)));
        String mubiaojihua_value = XTHttpJSON.decimalFormat.format(P);
        if (mubiaojihua_value.substring(0, 1).equals("-") || mubiaojihua_value.substring(0, 1).equals("﹣") || mubiaojihua_value.substring(0, 1).equals("－") || mubiaojihua_value.substring(0, 1).equals("﹣")) {
            sp_mbjh.setSelection(2);
        } else {
            sp_mbjh.setSelection(1);

        }
        if ("0.00".equals(mLongitudeS) && "0.00".equals(mLatitudeS)) {
            mubiaojihua_tv.setText("--");
        } else {
            mubiaojihua_tv.setText(String.valueOf(Math.abs(Double.parseDouble(mubiaojihua_value))));
        }
    }

    private void getCatParameter() {
        //String sendUrl = "http://10.192.0.177/cgi-bin/modemstatus/";
        String getCatParameterUrl = "http://" + mModemIp + "/cgi-bin/modemstatus/";
        http.send(HttpRequest.HttpMethod.GET, getCatParameterUrl, null,
                new RequestCallBack<JSONObject>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        handler.sendEmptyMessageDelayed(1, messageDelay);
                    }

                    @SuppressWarnings("rawtypes")
                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        //Toast.makeText(ManualControlActivity.this, responseInfo.result.toString(),Toast.LENGTH_LONG).show();
                        try {
                            String resInfoStr = responseInfo.result.toString();
                            DataModel dataModel = gson.fromJson(resInfoStr, DataModel.class);
                            mRx = dataModel.getRx();
                            maxRx = dataModel.getMaxrx();
                            mTx = dataModel.getTx();
                            maxTx = dataModel.getMaxtx();
                            mReceiveLevel.setText(mRx + "/" + maxRx + " dB");
                            mCapability.setText(mTx + "/" + maxTx + " dB");
                            if (mRx.substring(0, 1).equals("-") || mRx.substring(0, 1).equals("﹣") || mRx.substring(0, 1).equals("－") || mRx.substring(0, 1).equals("﹣")) {
                                pb_progressbar.setProgress(0);
                            } else {
                                pb_progressbar.setProgress((int) (((Double.parseDouble(mRx) / Double.parseDouble(maxRx)) * 100)));

                            }
                            if (mTx.substring(0, 1).equals("-") || mTx.substring(0, 1).equals("﹣") || mTx.substring(0, 1).equals("－") || mTx.substring(0, 1).equals("﹣")) {
                                pb_progressbar1.setProgress(0);
                            } else {
                                pb_progressbar1.setProgress((int) (((Double.parseDouble(mTx) / Double.parseDouble(maxTx)) * 100)));

                            }
                            handler.sendEmptyMessageDelayed(1, messageDelay);
                        } catch (Exception e) {
                            handler.sendEmptyMessageDelayed(1, messageDelay);
                            e.printStackTrace();
                        }
                    }
                });

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
            getRemoveText();
            starAdapter.notifyDataSetChanged();
            setListViewHeight();
            if (pWindow != null && !pWindow.isShowing()) {
                pWindow.showAsDropDown(mSatelliteNum);
            }

        }

        private void getRemoveText() {
            if (searchStarModels.size() == 0) {
                mSatelliteNum.setText("");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }
    private void setListViewHeight() {
        int height = ScreenUtil.dip2px(mContext, 42)*starAdapter.getCount();
        int scrrenH=(int) (ScreenUtil.getHight(mContext)*0.55);
        int realH=height>=scrrenH?scrrenH:height;
        LinearLayout.LayoutParams layoutParams = (android.widget.LinearLayout.LayoutParams) starListView.getLayoutParams();
        layoutParams.height=realH;
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
                                if(!remove){
                                    searchStarModels.remove(startItemPosition);
                                }
                                boolean remove1 = starCodeModels.remove(starCodeModel);
                                if(!remove1){
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
    private void initStarList() {
        pWindow=new PopupWindow();
        pWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        pWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        pWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        pWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        starListView =new ListView(this);
        setSearchStarModels();
        starAdapter=new CommonAdapter<StarCodeModel>(this,R.layout.item_star_info,searchStarModels) {

            @Override
            protected void fillItemData(CommonViewHolder viewHolder,
                                        int position, StarCodeModel item) {
                viewHolder.setTextForTextView(R.id.tv_star_name,item.getSatename());
				/*if(searchStarModels.size()>12){
					isAddStar=true;
				}*/

            }
        };
        starListView.setAdapter(starAdapter);
        pWindow.setBackgroundDrawable(new ColorDrawable());
        //pWindow.setOutsideTouchable(true);
        final LinearLayout ll=new LinearLayout(mContext);
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
        TextView tv_star_name= (TextView) adView.findViewById(R.id.tv_star_name);
        tv_star_name.setText("添加更多");
		/*View lineView=new View(mContext);
		lineView.setBackgroundColor(getResources().getColor(R.color.white));
		ll.addView(lineView);
		LinearLayout.LayoutParams lineParms=(android.widget.LinearLayout.LayoutParams) lineView.getLayoutParams();
		lineParms.height=DensityUtil.dip2px(mContext, 1);
		lineParms.width=LinearLayout.LayoutParams.MATCH_PARENT;*/
        ll.addView(adView);
        pWindow.setContentView(ll);
        mSatelliteNum.post(new Runnable() {
            @Override
            public void run() {
                setListViewHeight();
                pWindow.setWidth(mSatelliteNum.getWidth());
            }
        });

        starListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                CommonAdapter<StarCodeModel> adapter = (CommonAdapter) parent.getAdapter();
                startItem = adapter.getItem(position);
                startItemPosition=position;
                if (startItem.isAdd() && (position > 43)) {
                    showDelDialog(startItem);
                }
                return false;
            }
        });
        starListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //strSatelliteNum=String.valueOf(position+1);
                mSatelliteNum.removeTextChangedListener(myTextWatcher);
                CommonAdapter<StarCodeModel> adapter=(CommonAdapter) parent.getAdapter();
                currentStar=adapter.getItem(position);
                mSatelliteLongitude= Double.parseDouble(currentStar.getSatelon());
                setSateLon();
                pWindow.dismiss();
                mSatelliteNum.setText(currentStar.getSatename());
                mSatelliteNum.setSelection(currentStar.getSatename().length());
                mSatelliteNum.addTextChangedListener(myTextWatcher);


            }
        });

    }
    private void setSateLon() {
        String _satelon=ChechIpMask.numDigite(currentStar.getSatelon(), 1);//保留一位有效数字。
        //SharedPreferenceManager.saveString(mContext, "satelon", _satelon);
        //如果带负号
        if(_satelon.substring(0,1).equals("-")||_satelon.substring(0,1).equals("﹣")||_satelon.substring(0,1).equals("－")||_satelon.substring(0,1).equals("﹣")){

            _satelon=_satelon.substring(1);
            mWXJD.setSelection(1);
        }else{
            mWXJD.setSelection(0);
        }
        super_ref_itude.setText(_satelon);
    }
    private void saveStarPara(){
        getRefText_up();
    }
    private void getRefText_up() {
        if (ChechIpMask.a2b(super_ref_itude.getText().toString(), 0, 180)) {
            String _mEdItude = ChechIpMask.numDigite(super_ref_itude.getText().toString(), 2);
            if (mWXJD.getSelectedItemPosition() == 1) {
                _mEdItude = "-" + _mEdItude;
                strEdItude = _mEdItude;
            } else if (mWXJD.getSelectedItemPosition() == 0) {
                strEdItude = _mEdItude;// mCurrentLongItude.getText().toString();// 卫星经度
            }

        } else {
            strEdItude = null;
            Toast.makeText(ManualControlActivity.this, "对不起，您输入的经度不合法！！", 0).show();
            return;
        }
        if (currentStar != null) {
            StarCodeModel dataSaved = isDataSaved();
            if (dataSaved == null) {
                if(!"--".equals(super_ref_itude.getText().toString())){
                    currentStar.setSatelon(strEdItude);
                }
                currentStar.setSessionToken(mToken);
                currentStar.save();
            } else {
                if(!"--".equals(super_ref_itude.getText().toString())){
                    dataSaved.setSatelon(strEdItude);
                }
                dataSaved.setSessionToken(mToken);
                dataSaved.update(dataSaved.getId());
            }
            Toast.makeText(ManualControlActivity.this,"卫星经度保存成功",Toast.LENGTH_SHORT).show();
                SharedPreferenceManager.saveString(mContext, "currentStar",
                        GsonUtils.toJson(dataSaved == null ? currentStar
                                : dataSaved));
            updateSearchStarModels(dataSaved);
            } else {
            Toast.makeText(ManualControlActivity.this,"卫星经度保存成功",Toast.LENGTH_SHORT).show();

            }

        }
    /**
     * 当用户点击保存按钮，更新searchStarModels中与id对应的Model
     * @param dataSaved
     */
    private void updateSearchStarModels(StarCodeModel dataSaved) {
        for(int i=0;i<searchStarModels.size();i++){
            if(searchStarModels.get(i).getId()==(dataSaved==null?currentStar.getId():dataSaved.getId())){
                searchStarModels.set(i,dataSaved==null?currentStar:dataSaved);
                break;
            }
        }
        starAdapter.notifyDataSetChanged();
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
    private void setSearchStarModels() {
        List<StarCodeModel> dbData = DataSupport.findAll(StarCodeModel.class);
        starCodeModels.clear();
        searchStarModels.clear();
        starCodeModels.addAll(dbData);
        searchStarModels.addAll(starCodeModels);

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
                        et_star_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {

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
                        mCurrentLatitude1.setOnFocusChangeListener(new View.OnFocusChangeListener() {

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
                                        Toast.makeText(ManualControlActivity.this, "对不起，您输入的经度不合法！", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(ManualControlActivity.this, "卫星编号不能为空", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                })
                .show(getSupportFragmentManager());
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int[] location = new int[2];
        mSatelliteNum.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if(event.getX() < x || event.getX() > (x + mSatelliteNum.getWidth()) || event.getY() < y || event.getY() > (y + mSatelliteNum.getHeight())){
            pWindow.dismiss();
        }

        return super.dispatchTouchEvent(event);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initStarList();
                reShowAddDialog();
                reShowDelDialog();
            }
        }, 50);
        if (pWindow != null && pWindow.isShowing()) {
            pWindow.dismiss();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pWindow.showAsDropDown(mSatelliteNum);
                }
            }, 200);

        }


    }
    private void reShowDelDialog() {
        if(starDelDialog !=null&& starDelDialog.isVisible()){
            starDelDialog.dismiss();
            showDelDialog(startItem);
        }
    }

    private void reShowAddDialog() {
        if(addStarDialog !=null&& addStarDialog.isVisible()){
            addStarDialog.dismiss();
            showAddDialog();
        }
    }
}
