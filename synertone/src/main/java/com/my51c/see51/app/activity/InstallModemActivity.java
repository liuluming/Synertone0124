package com.my51c.see51.app.activity;

import android.content.Intent;
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
import com.my51c.see51.app.bean.InstallBean;
import com.my51c.see51.app.http.XTHttpUtil;
import com.my51c.see51.app.utils.GsonUtils;
import com.my51c.see51.common.AppData;
import com.my51c.see51.widget.SharedPreferenceManager;
import com.synertone.commonutil.view.BaseNiceDialog;
import com.synertone.commonutil.view.ConfirmCancelDialog;
import com.synertone.commonutil.view.ConfirmDialog;
import com.synertone.commonutil.view.ViewConvertListener;
import com.synertone.commonutil.view.ViewHolder;
import com.synertone.netAssistant.R;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class InstallModemActivity extends BaseActivity {
    private ImageView mBack;
    private TextView mTittle;
    private RelativeLayout rl_top_bar;
    private ProgressBar mInstallProgressbar;
    private TextView mProgress;
    private TextView tv_next;
    private TextView tvInstep;
    private int messageDelay = 5*1000;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                getModemIp();
            }else if(msg.what==2){
                setCatParameter();
            }
        }
    };
    private String mCurrentIp="192.168.1.1";
    private String mModemIp="192.168.1.1";
    private int lastProgress=0;
    private String lastStep="1";
    private InstallBean installBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_modem);
        initView();
        getModemIp();
        setCatParameter();
        String step = SharedPreferenceManager.getString(mContext, "step");
        tvInstep.setText("步骤"+step+"/4");
    }

    private void initView() {
        rl_top_bar= (RelativeLayout)findViewById(R.id.rl_top_bar);
        mBack= (ImageView) findViewById(R.id.iv_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCurrentIp.equals(mModemIp)){
                    noInstallCompleteDialog();
                }
            }
        });
        mTittle= (TextView) findViewById(R.id.tv_bar_title);
        mTittle.setText(R.string.modem_set);
        tv_next= (TextView) findViewById(R.id.tv_next);
        tv_next.setVisibility(View.GONE);
        mInstallProgressbar=(ProgressBar) findViewById(R.id.pb_install_progressbar);
        tvInstep= (TextView) findViewById(R.id.tv_instep);
        mProgress= (TextView) findViewById(R.id.tv_progress);
    }
    private void installSuccessDiaLog() {
        ConfirmDialog.init().setConvertListener(new ViewConvertListener() {
            @Override
            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                holder.setText(R.id.tv_tip,getString(R.string.install_success));
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

    private void doRestToStep1() {
        showDia();
        String getCatParameterUrl = "http://" + mModemIp + "/cgi-bin/inststep/";
        RequestParams params=new RequestParams();
        params.setHeader("Cookie", "loc=en");
        params.addBodyParameter("step", "1");
        AppData.http.send(HttpRequest.HttpMethod.POST, getCatParameterUrl, params,
                new RequestCallBack<JSONObject>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        dismissDia();
                        Intent intent=new Intent(mContext,SuperSetActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    @SuppressWarnings("rawtypes")
                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        dismissDia();
                        Intent intent=new Intent(mContext,SuperSetActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    private void installFailDiaLog() {
        ConfirmDialog.init().setConvertListener(new ViewConvertListener() {
            @Override
            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                holder.setText(R.id.tv_tip,getString(R.string.install_fail));
                holder.setOnClickListener(R.id.bt_ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                       doRestToStep1();
                    }
                });
            }
        }).show(getSupportFragmentManager());

    }
    private void noInstallCompleteDialog() {
        ConfirmCancelDialog.init()
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        holder.setText(R.id.tv_tip,getResources().getString(R.string.no_install_complete));
                        holder.setOnClickListener(R.id.bt_cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        holder.setOnClickListener(R.id.bt_ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, SuperSetActivity.class);
                                startActivity(intent);
                                dialog.dismiss();
                                finish();
                            }
                        });
                    }
                })
                .show(getSupportFragmentManager());

    }
    private void getModemIp() {
        String getModemUrl = XTHttpUtil.GET_MODEM_IP;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getModemUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                 //   Toast.makeText(mContext,jsonObject.optString("ip")+"======="+code,Toast.LENGTH_SHORT).show();
                    if (code.equals("0")) {
                        if(!StringUtils.isEmpty(jsonObject.getString("ip"))){
                            mModemIp = jsonObject.getString("ip");
                        }
                        if(!mCurrentIp.equals(mModemIp)){
                            handler.removeMessages(1);
                            handler=null;
                            mProgress.setText(100+"%");
                            mInstallProgressbar.setProgress(100);
                            installSuccessDiaLog();
                        }
                    }
                    if(handler!=null){
                        handler.sendEmptyMessageDelayed(1, messageDelay);
                    }
                } catch (JSONException e) {
                    //installFailDiaLog();
                    e.printStackTrace();
                    if(handler!=null){
                        handler.sendEmptyMessageDelayed(1, messageDelay);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //installFailDiaLog();
                if(handler!=null){
                    handler.sendEmptyMessageDelayed(1, messageDelay);
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
    private void setCatParameter() {
       /* if(StringUtils.isEmpty(mModemIp)){
            if(handler!=null){
                handler.sendEmptyMessageDelayed(2,900);
            }
            return;
        }*/
       //Toast.makeText(mContext,mModemIp,Toast.LENGTH_SHORT).show();
        String getCatParameterUrl = "http://" + mModemIp + "/cgi-bin/installstatus/";
        RequestParams params=new RequestParams();
        params.setHeader("Cookie", "loc=en");
        AppData.http.send(HttpRequest.HttpMethod.GET, getCatParameterUrl, params,
                new RequestCallBack<JSONObject>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        if(handler!=null){
                            handler.sendEmptyMessageDelayed(2,900);
                        }
                    }
                    @SuppressWarnings("rawtypes")
                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        try{
                             installBean= GsonUtils.fromJson(responseInfo.result.toString(),InstallBean.class);
                            String err = installBean.getErr();
                            if(installBean!=null&&("4".equals(err)||"3".equals(err))){
                                 installFailDiaLog();
                                 handler=null;
                                 return;
                             }
                            int progress= (int) Double.parseDouble(installBean.getProgress());
                            if(!lastStep.equals(installBean.getCurstep())&&!"0".equals(installBean.getCurstep())){
                                       lastProgress=0;
                            }
                           /* if(!"2".equals(installBean.getCurstep())){
                                SharedPreferenceManager.saveString(mContext,"step",null);
                            }*/
                            if("0".equals(installBean.getCurstep())){
                                tvInstep.setText("步骤4/4");
                            }else{
                                tvInstep.setText("步骤"+installBean.getCurstep()+"/4");
                            }
                            if(lastProgress<=progress){
                                mProgress.setText(progress+"%");
                                mInstallProgressbar.setProgress(progress);
                            }
                            if(handler!=null){
                                handler.sendEmptyMessageDelayed(2,900);
                            }

                            lastStep=installBean.getCurstep();
                            if(!"0".equals(installBean.getCurstep())){
                                lastProgress=progress;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler=null;
        if(installBean!=null){
            SharedPreferenceManager.saveString(mContext,"step",installBean.getCurstep());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount() == 0){
            if(mCurrentIp.equals(mModemIp)){
                noInstallCompleteDialog();
                return false;
            }

        }
        return super.onKeyDown(keyCode, event);


    }
}
