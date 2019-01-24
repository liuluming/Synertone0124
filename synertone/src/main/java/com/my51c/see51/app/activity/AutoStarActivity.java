package com.my51c.see51.app.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.my51c.see51.BaseActivity;
import com.my51c.see51.Logger.LoggerSave;
import com.my51c.see51.app.bean.DataModel;
import com.my51c.see51.app.bean.StarCodeModel;
import com.my51c.see51.app.http.XTHttpUtil;
import com.my51c.see51.common.AppData;
import com.synertone.netAssistant.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.my51c.see51.app.http.XTHttpUtil.GET_ONESTAR_STATE_ADDRESS;
import static com.my51c.see51.app.http.XTHttpUtil.POST_SATE_CATCH;

public class AutoStarActivity extends BaseActivity {

    private RelativeLayout rl_top_bar;
    private TextView mTittle;
    private AnimationDrawable rocketAnimation;
    private ImageView iv_star_connection_loading;
    private int messageDelay = 900;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                getCatParameter();
            }
        }
    };
    private boolean mSateFlag = false;
    private String mModemIp;
    private String mToken;
    private String mRx, maxRx, mTx, maxTx;
    private TextView capability,receice_level,mReceiveLevel, mCapability;
    private ProgressBar pb_progressbar,pb_progressbar1;
    protected StarCodeModel currentStar;
    private Gson gson;
    private HashMap<String, Toast> toaHashMap=new HashMap<>();
    private String mSatename;
    private TextView onekeystar_num;
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_star);
        gson=new Gson();
        if(AppData.accountModel!=null) {
            mToken = AppData.accountModel.getSessionToken();
        }
        currentStar = (StarCodeModel) getIntent().getSerializableExtra("currentStar");
        if(currentStar!=null) {
            mSatename = currentStar.getSatename();
        }else{
            mSatename ="协同一号";
        }
        initView();
        initToasts();
        initEvent();
    }

    private void initView() {
        rl_top_bar= (RelativeLayout) findViewById(R.id.rl_top_bar);
        mBack= (ImageView)findViewById(R.id.iv_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSateFlag = false;
                finish();
            }
        });
        mTittle= (TextView)findViewById(R.id.tv_bar_title);
        mTittle.setText(R.string.one_key_star);
        onekeystar_num= (TextView) findViewById(R.id.onekeystar_num);
        onekeystar_num.setText(mSatename);
        iv_star_connection_loading=(ImageView)findViewById(R.id.iv_star_connection_loading);
        capability = (TextView) findViewById(R.id.capability);
        receice_level = (TextView) findViewById(R.id.receice_level);
        mCapability = (TextView) findViewById(R.id.tv_capability);
        mCapability = (TextView) findViewById(R.id.tv_capability);
        mReceiveLevel = (TextView) findViewById(R.id.tv_receive_level);
        pb_progressbar = (ProgressBar) findViewById(R.id.pb_progressbar);
        pb_progressbar1 = (ProgressBar) findViewById(R.id.pb_progressbar1);
        getModemIp();
        String stringExtra = getIntent().getStringExtra("key");
        if("sateCatchPost".equals(stringExtra)) {
            sateCatchPost();
        }else{
            sntSateStatus();
        }
    }
    private void initEvent() {
        rl_top_bar.setOnTouchListener(new ComBackTouchListener());
    }
    @SuppressLint("ShowToast")
    private void initToasts() {
        Toast toast1=Toast.makeText(getApplicationContext(),"查询对星状态故障", 0);
        Toast toast2=Toast.makeText(getApplicationContext(),"正在跟踪", 0);
        Toast toast3=Toast.makeText(getApplicationContext(),"捕获确认中", 0);
        Toast toast4=Toast.makeText(getApplicationContext(),"正在捕获", 0);
        Toast toast5=Toast.makeText(getApplicationContext(),"对星成功", 0);
        Toast toast6=Toast.makeText(getApplicationContext(),"正在确认", 0);
        Toast toast7=Toast.makeText(getApplicationContext(),"对星命令发送成功", 0);
        Toast toast8=Toast.makeText(getApplicationContext(),"对星命令发送失败", 0);
        Toast toast9=Toast.makeText(getApplicationContext(),"正在对星！", 0);
        Toast toast10=Toast.makeText(getApplicationContext(),"对星故障", 0);
        Toast toast11=Toast.makeText(getApplicationContext(),"对星失败，请重新尝试！", 0);
        toaHashMap.put("查询对星状态故障", toast1);
        toaHashMap.put("正在跟踪", toast2);
        toaHashMap.put("捕获确认中", toast3);
        toaHashMap.put("正在捕获", toast4);
        toaHashMap.put("对星成功", toast5);
        toaHashMap.put("正在确认", toast6);
        toaHashMap.put("对星命令发送成功", toast7);
        toaHashMap.put("对星命令发送失败", toast8);
        toaHashMap.put("正在对星！", toast9);
        toaHashMap.put("对星故障", toast10);
        toaHashMap.put("对星失败，请重新尝试！", toast11);
    }
    private void sateCatchPost() {
        if (toaHashMap.get("正在对星！") != null) {
            toaHashMap.get("正在对星！").show();
        }
        animationShow();
        String sateCatchUrl = POST_SATE_CATCH;
        if(mToken!=null) {
            if(currentStar!=null){
                currentStar.setSessionToken(mToken);
            }else{
                currentStar=new StarCodeModel();
                currentStar.setSessionToken(mToken);
                currentStar.setSatename(mSatename);
                currentStar.setMode("3");
                currentStar.setAmipSwitch("1");
                currentStar.setCenterFreq("12369");
                currentStar.setFreq("960.0");
                currentStar.setBw("0");
                currentStar.setZfreq("12236");
                currentStar.setSignRate("60000");
                currentStar.setType("0");
                currentStar.setSatelon("119.5");
            }
        }
        String mCurrentStar = gson.toJson(currentStar);
        try {
            JSONObject jsonObject = new JSONObject(mCurrentStar);
            LoggerSave.requestLog(POST_SATE_CATCH, jsonObject.toString());
            //Toast.makeText(mContext, jsonObject.toString(), Toast.LENGTH_SHORT).show();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    sateCatchUrl, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                LoggerSave.responseLog(POST_SATE_CATCH, response.toString());
                                String code = response.getString("code");
                                String msg = response.optString("msg");
                                if (code.equals("0")) {
                                    sntSateStatus();
                                    if (toaHashMap.get("对星命令发送成功") != null) {
                                        toaHashMap.get("对星命令发送成功").show();
                                    }
                                } else if (code.equals("-1")) {
                                    if (msg.equals("acu_occupy")) {
                                        showMutualDialog();
                                    } else {
                                        if (toaHashMap.get("对星命令发送失败") != null) {
                                            toaHashMap.get("对星命令发送失败").show();
                                        }
                                        failDuixing();
                                    }
                                } else if (code.equals("-2")) {
                                    showLoginDialog();
                                    failDuixing();
                                } else {
                                    if (toaHashMap.get("对星故障") != null) {
                                        toaHashMap.get("对星故障").show();
                                    }
                                    failDuixing();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (toaHashMap.get("连接网元服务器失败") != null) {
                        toaHashMap.get("连接网元服务器失败").show();
                    }
                    failDuixing();
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0, 0f));
            AppData.mRequestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sntSateStatus() {
        mSateFlag = true;
        SntSateStatusQuery satequerytask = new SntSateStatusQuery();
        satequerytask.execute(GET_ONESTAR_STATE_ADDRESS);
        LoggerSave.requestLog(GET_ONESTAR_STATE_ADDRESS, GET_ONESTAR_STATE_ADDRESS);
    }
    private class SntSateStatusQuery extends AsyncTask<String, String, String> {
        private String recv = "";
        private int count;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            count = 0;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            HttpGet hget = new HttpGet(params[0]);
            HttpClient hclient = new DefaultHttpClient();
            HttpResponse respone;
            while (mSateFlag) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    respone = hclient.execute(hget);
                    recv = EntityUtils.toString(respone.getEntity());
                    LoggerSave.responseLog(params[0], recv);
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                publishProgress(recv);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            if (recv.equals("")) {
                if (toaHashMap.get("查询对星状态故障") != null) {
                    toaHashMap.get("查询对星状态故障").show();
                }
                mSateFlag = false;
            } else {
                try {
                    JSONObject satetusjs = new JSONObject(recv);
                    //Toast.makeText(getApplicationContext(),satetusjs.toString(), 0).show();
                    //LoggerSave.responseLog("TT",satetusjs.toString()+"count="+count);
                    if (satetusjs.getString("code").equals("0")) {
                            if (toaHashMap.get("正在确认") != null && mSateFlag) {
                                toaHashMap.get("正在确认").show();

                        }
                        //onekeystar_spinner_choose.setEnabled(false);
                        //onekeystar_btn_duixing_progress.setClickable(false);
                        if (count > 3) {
                            mSateFlag = false;
                            successDuixing();
                            if (toaHashMap.get("对星成功") != null) {
                                toaHashMap.get("对星成功").show();
                            }
                            //onekeystar_spinner_choose.setClickable(true);//spinner 能点
                            //onekeystar_spinner_choose.setEnabled(true);
                            // onekeystar_btn_duixing_progress.setClickable(true);
                        } else {
                            count++;
                        }
                    } else if (satetusjs.getString("code").equals("1")) {

                           if (toaHashMap.get("正在捕获") != null) {
                                toaHashMap.get("正在捕获").show();

                        }
                        //onekeystar_spinner_choose.setEnabled(false);
                        // onekeystar_btn_duixing_progress.setClickable(false);
                        //onekeystar_spinner_choose.setClickable(false);//spinner 能点
                        count = 0;
                    } else if (satetusjs.getString("code").equals("2")) {
                        Toast.makeText(getApplicationContext(), "卫星未找到", 0).show();
                        mSateFlag = false;//对星失败 不需要继续查询了
                        failDuixing();
                    } else if (satetusjs.getString("code").equals("3")) {
                        if (toaHashMap.get("正在跟踪") != null) {
                            toaHashMap.get("正在跟踪").show();
                        }
                        //onekeystar_spinner_choose.setEnabled(false);
                        //onekeystar_btn_duixing_progress.setClickable(false);
                        //onekeystar_spinner_choose.setClickable(false);//spinner 不能点
                    } else if (satetusjs.getString("code").equals("-1")) {
                        if (satetusjs.optString("msg").equals("acu_occupy")) {
                            showMutualDialog();
                        } else {
                            if (toaHashMap.get("查询对星状态故障") != null) {
                                toaHashMap.get("查询对星状态故障").show();
                            }
                            mSateFlag = false;//查询状态失败 不需要继续查询了
                            failDuixing();
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }

    }
    /**
     * 表示対星成功动画停止，得到相关数据
     */
    private void successDuixing() {
        // 这里表示対星状态变化
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if(rocketAnimation!=null){
                    rocketAnimation.stop();
                }

            }
        }, 3000);
    }
    /**
     * 対星过程中的动画
     */
    private void animationShow() {
        // 开始対星
        iv_star_connection_loading
                .setBackgroundResource(R.drawable.rocket_thrust);
        rocketAnimation = (AnimationDrawable) iv_star_connection_loading
                .getBackground();
        rocketAnimation.start();
    }

    /**
     * 表示対星失败
     */
    private void failDuixing() {
        if(rocketAnimation!=null){
            rocketAnimation.stop();
        }
        iv_star_connection_loading
                .setBackgroundResource(R.drawable.onekeystar_iv_beforeduixing);
        if (toaHashMap.get("对星失败，请重新尝试！") != null) {
            toaHashMap.get("对星失败，请重新尝试！").show();
        }
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
    private void getCatParameter() {
        //String sendUrl = "http://10.192.0.177/cgi-bin/modemstatus/";
        String getCatParameterUrl = "http://" + mModemIp + "/cgi-bin/modemstatus/";
        AppData.http.send(HttpRequest.HttpMethod.GET, getCatParameterUrl, null,
                new RequestCallBack<JSONObject>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        handler.sendEmptyMessageDelayed(1, messageDelay);
                    }

                    @SuppressWarnings("rawtypes")
                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        //Toast.makeText(AutoStarActivity.this, responseInfo.result.toString(), 0).show();
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
    @Override
    protected void onPause() {
        super.onPause();
        mSateFlag = false;
        try {
            Iterator<Map.Entry<String, Toast>> iter = toaHashMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Toast> entry = iter.next();
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
    @Override
    protected void onDestroy() {
        mSateFlag=false;
        super.onDestroy();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mSateFlag = false;
        return super.onKeyDown(keyCode, event);
    }

}
