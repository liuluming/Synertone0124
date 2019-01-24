package com.my51c.see51.app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.my51c.see51.BaseActivity;
import com.my51c.see51.Logger.LoggerSave;
import com.my51c.see51.adapter.MySpinnerAdapter;
import com.my51c.see51.app.OneKeyStarActivity;
import com.my51c.see51.app.bean.ResultBean;
import com.my51c.see51.app.bean.StatusBean;
import com.my51c.see51.app.http.XTHttpUtil;
import com.my51c.see51.app.utils.GsonUtils;
import com.my51c.see51.common.AppData;
import com.my51c.see51.widget.NiceDialog;
import com.my51c.see51.widget.SharedPreferenceManager;
import com.synertone.commonutil.view.BaseNiceDialog;
import com.synertone.commonutil.view.ConfirmDialog;
import com.synertone.commonutil.view.ViewConvertListener;
import com.synertone.commonutil.view.ViewHolder;
import com.synertone.netAssistant.R;

import org.afinal.simplecache.ACache;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import static com.my51c.see51.app.http.XTHttpUtil.GET_TIAN_STYLE;


/*
 * 高级设置
 */
public class SuperSetActivity extends BaseActivity implements OnClickListener {
    private LinearLayout mOnestartLL, mRefLL, ll_query_param,ll_modem_set;// 卫星设置下面的 （一键对星
    // ，参数设置，调试控制）
    private LinearLayout mLink_State, mRouteset, mBandwidth;// 链路状态
    // （链路状态，路由设置，带宽叠加）
    private LinearLayout mSystem_Update;// 系统升级 密码修改
    //private ToggleButton mBtn_kuandaidiejia;//宽带叠加按钮
    private ACache mACache;
    private String SET_REF_TAG="setreftag";
    private TextView super_weixingshezhi,super_shoudongduixing,super_canshushezhi,tvGateway;
    private Spinner mTianxianStyle;
    private String mOduType,mToken;
    private HashMap<String, Toast> toaHashMap=new HashMap<>();
    private RelativeLayout rl_top_bar;
    private TextView mTittle;
    private boolean switchStatus;
    private boolean isSet=false;
    private TextView tv_modem_set,tv_query_param;
    private String mModemIp="10.192.0.1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_set_activity);
        mACache=ACache.get(this);
        mACache.put(SET_REF_TAG,"1");
        if(AppData.accountModel!=null) {
            mToken = AppData.accountModel.getSessionToken();
        }
        initView();
        initToasts();
        initEvent();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        try {
            setContentView(R.layout.super_set_activity);
        } catch (Exception ex) {
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        switchStatus=SharedPreferenceManager.getBoolean(mContext,"switchStatus");
    }

    private void initEvent() {
        rl_top_bar.setOnTouchListener(new ComBackTouchListener());
    }
    @SuppressLint("ShowToast")
    private void initToasts() {
        Toast toast=Toast.makeText(SuperSetActivity.this,"正在查询天线类型，请稍等！", Toast.LENGTH_LONG);
        toaHashMap.put("正在查询天线类型，请稍等！", toast);
    }
    // 查找控件
    private void initView() {
        rl_top_bar= (RelativeLayout) findViewById(R.id.rl_top_bar);
        mTittle= (TextView)findViewById(R.id.tv_bar_title);
        mTittle.setText(R.string.high_set);
        super_weixingshezhi=(TextView) findViewById(R.id.super_weixingshezhi);
        super_weixingshezhi.setTypeface(AppData.fontPutu);
        tvGateway=(TextView) findViewById(R.id.tv_gateway);
        tvGateway.setTypeface(AppData.fontPutu);
        super_canshushezhi=(TextView) findViewById(R.id.super_canshushezhi);
        super_canshushezhi.setTypeface(AppData.fontXiti);
        super_shoudongduixing=(TextView) findViewById(R.id.super_shoudongduixing);
        super_shoudongduixing.setTypeface(AppData.fontXiti);
        tv_modem_set=(TextView) findViewById(R.id.tv_modem_set);
        tv_modem_set.setTypeface(AppData.fontXiti);
        tv_query_param=(TextView) findViewById(R.id.tv_query_param);
        tv_query_param.setTypeface(AppData.fontXiti);
        mOnestartLL = (LinearLayout) findViewById(R.id.superset_onestart);
        mRefLL = (LinearLayout) findViewById(R.id.superset_ref);
        ll_modem_set = (LinearLayout) findViewById(R.id.ll_modem_set);
        ll_query_param = (LinearLayout) findViewById(R.id.ll_query_param);
        mOnestartLL.setOnClickListener(this);
        mRefLL.setOnClickListener(this);
        ll_modem_set.setOnClickListener(this);
        ll_query_param.setOnClickListener(this);
        querySessionStatus(new OnSessionStatusListener() {
            @Override
            public void sessionSuccess() {
                queryTianStyle();
            }

            @Override
            public void sessionErrorResponse() {
                queryTianStyle();
            }
        });
    }

    Intent mIntent;

    // 点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.superset_onestart:
                if(!switchStatus){
                    mIntent = new Intent(SuperSetActivity.this, AutoStarActivity.class);
                    startActivity(mIntent);
                }else {
                    mIntent = new Intent(SuperSetActivity.this, OneKeyStarActivity.class);
                    startActivity(mIntent);
                }
                break;

            case R.id.superset_ref:
               //tianStyleDialog();
                if(mOduType==null){
                    if (toaHashMap.get("正在查询天线类型，请稍等！")!=null){
                        toaHashMap.get("正在查询天线类型，请稍等！").show();
                    }
                }else{
                    if("0".equals(mOduType)){
                        tianStyleDialog();
                    }else if("1".equals(mOduType)){
                        Intent mIntent = new Intent(SuperSetActivity.this,SuperSetRefActivity.class);
                        startActivity(mIntent);
                    }else if("2".equals(mOduType)){
                        Intent mIntent = new Intent(SuperSetActivity.this,SuperSetRefActivity.class);
                        startActivity(mIntent);
                    }else if("3".equals(mOduType)){
                        Intent mIntent = new Intent(SuperSetActivity.this,SuperSetRefActivity.class);
                        startActivity(mIntent);
                    }else if("4".equals(mOduType)){
                        Intent mIntent = new Intent(SuperSetActivity.this,SuperSetRefActivity.class);
                        mIntent.putExtra("compass", "S(三轴)");
                        startActivity(mIntent);
                    }else if("5".equals(mOduType)){
                        Intent mIntent = new Intent(SuperSetActivity.this,SuperSetRefActivity.class);
                        mIntent.putExtra("compass", "S(三轴)");
                        startActivity(mIntent);
                    }else if("6".equals(mOduType)){
                        Intent mIntent = new Intent(SuperSetActivity.this,SuperSetRefActivity.class);
                        mIntent.putExtra("compass", "S(三轴)");
                        startActivity(mIntent);
                    }else if("7".equals(mOduType)){
                        Intent mIntent = new Intent(SuperSetActivity.this,SuperSetRefActivity.class);
                        mIntent.putExtra("compass", "C系列");
                        startActivity(mIntent);
                    }else if("8".equals(mOduType)){
                        Intent mIntent = new Intent(SuperSetActivity.this,SuperSetRefActivity.class);
                        mIntent.putExtra("compass", "C系列");
                        startActivity(mIntent);
                    }
                }
                break;
            case R.id.ll_modem_set:
                showDia();
                isSet=true;
                getModemIp();
                break;
            case R.id.ll_query_param:
                isSet=false;
                getModemIp();
                break;
        }
    }
    protected void tianStyleDialog() {
        NiceDialog.init().setLayoutId(R.layout.star_type_device).setConvertListener(new ViewConvertListener() {
            @Override
            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                View convertView = holder.getConvertView();
                mTianxianStyle =(Spinner) convertView.findViewById(R.id.spinner_tianxianstyle);// 天线类型
                mTianxianStyle.setAdapter(new MySpinnerAdapter(SuperSetActivity.this,
                        R.layout.spinner_stytle, getResources().getStringArray(R.array.tianxian_style)));
                mTianxianStyle.setSelection(0);
                holder.setOnClickListener(R.id.bt_cancel, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                holder.setOnClickListener(R.id.bt_ok, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToActivityByType();
                        dialog.dismiss();
                    }
                });
            }

            private void goToActivityByType() {
                if(mTianxianStyle.getSelectedItem().toString().equals("S(两轴)")){
                    Intent mIntent = new Intent(SuperSetActivity.this,SuperSetRefActivity.class);
                    startActivity(mIntent);

                }else if(mTianxianStyle.getSelectedItem().toString().equals("S(三轴)")){
                    Intent mIntent = new Intent(SuperSetActivity.this,SuperSetRefActivity.class);
                    mIntent.putExtra("compass", "S(三轴)");
                    startActivity(mIntent);

                }else if(mTianxianStyle.getSelectedItem().toString().equals("V系列")){
                    Intent mIntent = new Intent(SuperSetActivity.this,SuperSetRefActivity.class);
                    startActivity(mIntent);

                }else if(mTianxianStyle.getSelectedItem().toString().equals("N系列")){
                    Intent mIntent = new Intent(SuperSetActivity.this,ManualControlActivity.class);
                    startActivity(mIntent);

                }else if(mTianxianStyle.getSelectedItem().toString().equals("C系列")){
                    Intent mIntent = new Intent(SuperSetActivity.this,SuperSetRefActivity.class);
                    mIntent.putExtra("compass", "C系列");
                    startActivity(mIntent);

                }else if(mTianxianStyle.getSelectedItem().toString().equals("P(自动)")){
                    Intent mIntent = new Intent(SuperSetActivity.this,SuperSetRefActivity.class);
                    startActivity(mIntent);

                }else if(mTianxianStyle.getSelectedItem().toString().equals("P(手动)")){
                    Intent mIntent = new Intent(SuperSetActivity.this,ManualControlActivity.class);
                    startActivity(mIntent);

                }
            }
        }).show(getSupportFragmentManager());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void queryTianStyle() {
        String queryTianStyleUrl= GET_TIAN_STYLE;
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("sessionToken", mToken);
            LoggerSave.requestLog(GET_TIAN_STYLE,jsonObject.toString());
            JsonObjectRequest request = new JsonObjectRequest(Method.POST,
                    queryTianStyleUrl, jsonObject,
                    new Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                LoggerSave.responseLog(GET_TIAN_STYLE,response.toString());
                                JSONObject jsonObject = new JSONObject(response.toString());
                                String code = jsonObject.getString("code");
                                String msg = jsonObject.optString("msg");
                                if("0".equals(code)) {
                                    //Toast.makeText(SuperSetActivity.this, "天线类型查询成功",0).show();
                                }else if("-1".equals(code)){
                                    if("acu_occupy".equals(msg)){
                                        showMutualDialog();
                                    }
                                }else if("-2".equals(code)){
                                    showLoginDialog();
                                }
                                mOduType  = jsonObject.getString("odutype");
                                if("0".equals(mOduType)){
                                    System.out.println("未知天线类型");
                                }else if("1".equals(mOduType)){
                                    System.out.println("V4");
                                }else if("2".equals(mOduType)){
                                    System.out.println("V6");
                                }else if("3".equals(mOduType)){
                                    System.out.println("S6");
                                }else if("4".equals(mOduType)){
                                    System.out.println("S6A");//三轴
                                }else if("5".equals(mOduType)){
                                    System.out.println("S8");
                                }else if("6".equals(mOduType)){
                                    System.out.println("S9");
                                }else if("7".equals(mOduType)){
                                    System.out.println("C6");
                                }else if("8".equals(mOduType)){
                                    System.out.println("C9");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mOduType="0";
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(10*1000,0,0f));
            AppData.mRequestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            Iterator<Entry<String, Toast>> iter = toaHashMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Toast> entry = (Entry<String, Toast>) iter.next();
                Toast toast=entry.getValue();
                Field field = toast.getClass().getDeclaredField("mTN");
                field.setAccessible(true);
                Object obj = field.get(toast);
                java.lang.reflect.Method m=obj.getClass().getDeclaredMethod("hide", new Class[0]);
                m.invoke(obj, new Object[]{});
                iter.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getModemIp() {
        String getModemUrl = XTHttpUtil.GET_MODEM_IP;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getModemUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(mContext,response,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if (code.equals("0")) {
                        mModemIp = jsonObject.getString("ip");
                        //Toast.makeText(mContext,mModemIp,Toast.LENGTH_LONG).show();
                        if("".equals(mModemIp)||mModemIp==null){
                            dismissDia();
                            getNoIpDiaLog();
                        }else if("192.168.1.1".equals(mModemIp)){
                            if(isSet) {
                                doStatus();
                            }else{
                                getNoConfigParam();
                            }
                        }else{
                            if(isSet) {
                                dismissDia();
                                getAlreadySetDiaLog();
                            }else{
                                Intent intent=new Intent(mContext,QueryModemParamActivity.class);
                                intent.putExtra("ip",mModemIp);
                                startActivity(intent);
                            }
                        }
                    }else if("-1".equals(code)){
                        dismissDia();
                        getNoIpDiaLog();
                    }else{
                        doStatus();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dismissDia();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //doStatus();
                dismissDia();
                Toast.makeText(mContext,"连接网元服务器失败！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0, 0f));
        AppData.mRequestQueue.add(stringRequest);
    }
    private void getNoIpDiaLog() {
        ConfirmDialog.init().setConvertListener(new ViewConvertListener() {
            @Override
            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                holder.setText(R.id.tv_tip,getString(R.string.get_no_ip));
                holder.setOnClickListener(R.id.bt_ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        }).show(getSupportFragmentManager());

    }
    private void getAlreadySetDiaLog() {
        ConfirmDialog.init().setConvertListener(new ViewConvertListener() {
            @Override
            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                holder.setText(R.id.tv_tip,getString(R.string.already_set));
                holder.setOnClickListener(R.id.bt_ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent=new Intent(mContext,QueryModemParamActivity.class);
                        intent.putExtra("ip",mModemIp);
                        startActivity(intent);
                    }
                });
            }
        }).show(getSupportFragmentManager());

    }
    private void getNoConfigParam() {
        ConfirmDialog.init().setConvertListener(new ViewConvertListener() {
            @Override
            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                holder.setText(R.id.tv_tip,getString(R.string.get_no_config_param));
                holder.setOnClickListener(R.id.bt_ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        }).show(getSupportFragmentManager());

    }
    private void doStatus() {
        String getCatParameterUrl = "http://" +  mModemIp + "/cgi-bin/status/";
        RequestParams params=new RequestParams();
        params.setHeader("Cookie", "loc=en");
        AppData.http.send(HttpRequest.HttpMethod.GET, getCatParameterUrl, params,
                new RequestCallBack<JSONObject>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        dismissDia();
                        Toast.makeText(mContext,"请求失败，请重试！",Toast.LENGTH_SHORT).show();
                        /*if(arg0!=null){
                            Intent intent=new Intent(mContext,InstallModemActivity.class);
                            startActivity(intent);
                        }*/
                    }
                    @SuppressWarnings("rawtypes")
                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        dismissDia();
                        try {
                            if(responseInfo!=null){
                                StatusBean statusBean= GsonUtils.fromJson(responseInfo.result.toString(),StatusBean.class);
                                String install = statusBean.getInstall();
                                if("1".equals(statusBean.getActiveCode())){
                                    switch (install){
                                        case "3":
                                            //step5
                                          Intent  intent=new Intent(mContext,InstallModemActivity.class);
                                            startActivity(intent);
                                            break;
                                        default:
                                            Intent intent1 = new Intent(mContext, ConfigParamActivity.class);
                                            intent1.putExtra("ip",mModemIp);
                                            startActivity(intent1);
                                            break;
                                    }
                                }else  if("0".equals(statusBean.getActiveCode())){
                                    if("3".equals(install)){
                                        Intent  intent=new Intent(mContext,InstallModemActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(mContext, ConfigParamActivity.class);
                                        intent.putExtra("ip",mModemIp);
                                        startActivity(intent);

                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
