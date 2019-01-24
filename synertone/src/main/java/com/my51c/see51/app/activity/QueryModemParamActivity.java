package com.my51c.see51.app.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.my51c.see51.BaseActivity;
import com.my51c.see51.app.HomeActivity;
import com.my51c.see51.app.bean.InstproBean;
import com.my51c.see51.app.bean.ModemBean;
import com.my51c.see51.app.bean.ResultBean;
import com.my51c.see51.app.bean.StatusBean;
import com.my51c.see51.app.http.XTHttpUtil;
import com.my51c.see51.app.utils.GsonUtils;
import com.my51c.see51.common.AppData;
import com.synertone.commonutil.view.BaseNiceDialog;
import com.synertone.commonutil.view.ConfirmCancelDialog;
import com.synertone.commonutil.view.ConfirmDialog;
import com.synertone.commonutil.view.ViewConvertListener;
import com.synertone.commonutil.view.ViewHolder;
import com.synertone.netAssistant.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class QueryModemParamActivity extends BaseActivity {
    private TextView mGroupId;
    private TextView mTerminalId;
    private TextView mDownFrequency;
    private TextView mDownRate;
    private TextView mLnb;
    private TextView mBuc;
    private TextView mFrequencyGroup;
    private ImageView mBack;
    private TextView mTittle;
    private RelativeLayout rl_top_bar;
    private TextView tv_next;
    private InstproBean instproBean;
    private String mModemIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_modem_param);
        initView();
        initEvent();
        doGetInstpro();
    }

    private void doGetInstpro() {
        showDia();
        String getCatParameterUrl = "http://" +  mModemIp + "/cgi-bin/getinstpro/";
        RequestParams params=new RequestParams();
        params.setHeader("Cookie", "loc=en");
        AppData.http.send(HttpRequest.HttpMethod.GET, getCatParameterUrl, params,
                new RequestCallBack<JSONObject>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        dismissDia();
                        Toast.makeText(mContext,"查询失败，请重试！",Toast.LENGTH_SHORT).show();

                    }
                    @SuppressWarnings("rawtypes")
                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        dismissDia();
                        try {
                            if(responseInfo!=null){
                                InstproBean instproBean= GsonUtils.fromJson(responseInfo.result.toString(),InstproBean.class);
                                initDataView(instproBean);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initDataView(InstproBean instproBean) {
        if(instproBean==null){
            return;
        }
        mGroupId.setText(instproBean.getMgid()+"");
        mTerminalId.setText(instproBean.getTerminal()+"");
        mDownFrequency.setText(new BigDecimal(instproBean.getOb().getFreq()).toString());
        mDownRate.setText((int)instproBean.getOb().getSr()+"");
        mLnb.setText(new BigDecimal(instproBean.getAdv().getLnblo()).divide(new BigDecimal(Math.pow(10, 6))).toString());
        mBuc.setText(new BigDecimal(instproBean.getBuclo()).divide(new BigDecimal(Math.pow(10, 6))).toString());
        mFrequencyGroup.setText(instproBean.getAdv().getClustergroup()+"");
    }

    private void initView() {
        rl_top_bar = (RelativeLayout) findViewById(R.id.rl_top_bar_right);
        mBack = (ImageView) findViewById(R.id.iv_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTittle = (TextView) findViewById(R.id.tv_bar_title);
        mTittle.setText(R.string.query_param);
        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_next.setText("重置");
        mModemIp = getIntent().getStringExtra("ip");
        mGroupId = (TextView) findViewById(R.id.et_group_id);
        mTerminalId = (TextView) findViewById(R.id.et_terminal_id);
        mDownFrequency = (TextView) findViewById(R.id.et_down_frequency);
        mDownRate = (TextView) findViewById(R.id.et_down_rate);
        mLnb = (TextView) findViewById(R.id.et_lnb);
        mBuc = (TextView) findViewById(R.id.et_buc);
        mFrequencyGroup = (TextView) findViewById(R.id.et_frequency_group);
    }

    private void initEvent() {
        rl_top_bar.setOnTouchListener(new BaseActivity.ComBackTouchListener());
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSetDialog();
            }
        });
    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                doPing();
            }
        }
    };
    private void doLogin() {
        String url = "http://" +  mModemIp + "/cgi-bin/techlogin/";
        RequestParams params=new RequestParams();
        params.setHeader("Cookie", "loc=en; auth=XSCJCEISKW");
        params.addBodyParameter("pswd","2598");
        AppData.http.send(HttpRequest.HttpMethod.POST, url, params,
                new RequestCallBack<JSONObject>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        doReset();
                    }
                    @SuppressWarnings("rawtypes")
                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        doReset();
                    }
                });

    }

    private void doReset() {
        String getCatParameterUrl = "http://" +  mModemIp + "/cgi-bin/inststep/";
        RequestParams params=new RequestParams();
        params.setHeader("Cookie", "loc=en;auth=UVCAQEVWUI");
        params.addBodyParameter("step","1");
        AppData.http.send(HttpRequest.HttpMethod.POST, getCatParameterUrl, params,
                new RequestCallBack<JSONObject>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        dismissDia();
                        Toast.makeText(mContext,"请求失败，请重试！",Toast.LENGTH_LONG).show();
                    }
                    @SuppressWarnings("rawtypes")
                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        ResultBean resultBean= GsonUtils.fromJson(responseInfo.result.toString(),ResultBean.class);
                        if(resultBean.getRes().equals("1")){
                            doPing();
                        }else{
                            resetFailureDiaLog();
                        }
                    }
                });

    }
    private void doPing() {
        String getModemUrl = XTHttpUtil.GET_MODEM_IP;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getModemUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(mContext,response,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if (code.equals("0")) {
                        String mModemIp  = jsonObject.getString("ip");
                        if("192.168.1.1".equals(mModemIp)){
                            mHandler=null;
                            dismissDia();
                            resetSuccessDiaLog();
                        }
                    }
                    if(mHandler!=null){
                        mHandler.sendEmptyMessageDelayed(1,5*1000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(mHandler!=null){
                    mHandler.sendEmptyMessageDelayed(1,5*1000);
                }
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
    private void reSetDialog() {
        ConfirmCancelDialog.init()
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        holder.setText(R.id.tv_tip,getResources().getString(R.string.reset_modem));
                        holder.setOnClickListener(R.id.bt_cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        holder.setOnClickListener(R.id.bt_ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showDia("重置过程大约需要一分钟，请耐心等待");
                                doLogin();
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .show(getSupportFragmentManager());

    }
    private void resetSuccessDiaLog() {
        ConfirmDialog.init().setConvertListener(new ViewConvertListener() {
            @Override
            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                holder.setText(R.id.tv_tip,getString(R.string.reset_success));
                holder.setOnClickListener(R.id.bt_ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent=new Intent(mContext,SuperSetActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).show(getSupportFragmentManager());

    }
    private void resetFailureDiaLog() {
        ConfirmDialog.init().setConvertListener(new ViewConvertListener() {
            @Override
            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                holder.setText(R.id.tv_tip,getString(R.string.reset_fail));
                holder.setOnClickListener(R.id.bt_ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        }).show(getSupportFragmentManager());

    }
}
