package com.my51c.see51.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.my51c.see51.BaseActivity;
import com.my51c.see51.app.bean.ResultBean;
import com.my51c.see51.app.bean.StarParamBean;
import com.my51c.see51.app.utils.GsonUtils;
import com.my51c.see51.common.AppData;
import com.synertone.netAssistant.R;

import org.json.JSONObject;

public class SatelliteParamActivity extends BaseActivity {
    private ImageView mBack;
    private TextView mTittle;
    private RelativeLayout rl_top_bar;
    private TextView tv_next;
    private TextView maxLevel;
    private TextView mCurrentLevel;
    private TextView mPrompt;
    private ImageView mChooseStatus;
    private String mStarCatIp;
    private StarParamBean starParamBean;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                getCatParameter();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satellite_param);
        initView();
        initEvent();
        startPointParameter();
    }

    private void initView() {
        rl_top_bar= (RelativeLayout) findViewById(R.id.rl_top_bar_right);
        mBack= (ImageView)findViewById(R.id.iv_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTittle= (TextView)findViewById(R.id.tv_bar_title);
        mTittle.setText(R.string.modem_set);
        mStarCatIp=getIntent().getStringExtra("ip");
        tv_next= (TextView)findViewById(R.id.tv_next);
        tv_next.setText(R.string.tv_next);
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(starParamBean!=null&&"1".equals(starParamBean.getLock())) {
                    setCatParameter();
                }else {
                    Toast.makeText(mContext,"参数设置有误，请检查！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mChooseStatus= (ImageView)findViewById(R.id.iv_choose_status);
        maxLevel= (TextView)findViewById(R.id.tv_max_level);
        mCurrentLevel= (TextView)findViewById(R.id.tv_current_level);
        mPrompt= (TextView)findViewById(R.id.tv_prompt);
    }
    private void startPointParameter() {
        String getCatParameterUrl = "http://" + mStarCatIp + "/cgi-bin/startpoining/";
        RequestParams params=new RequestParams();
        params.setHeader("Cookie", "loc=en");
        AppData.http.send(HttpRequest.HttpMethod.GET, getCatParameterUrl, params,
                new RequestCallBack<JSONObject>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }
                    @SuppressWarnings("rawtypes")
                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        getCatParameter();
                    }
                });

    }
    private void setCatParameter() {
        showDia();
        String getCatParameterUrl = "http://" + mStarCatIp + "/cgi-bin/inststep/";
        RequestParams params=new RequestParams();
        params.setHeader("Cookie", "loc=en");
        params.addBodyParameter("step","3");
       AppData.http.send(HttpRequest.HttpMethod.POST, getCatParameterUrl, params,
                new RequestCallBack<JSONObject>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        dismissDia();
                    }
                    @SuppressWarnings("rawtypes")
                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        dismissDia();
                        try {
                            if(responseInfo!=null){
                                ResultBean resultBean= GsonUtils.fromJson(responseInfo.result.toString(),ResultBean.class);
                                if("1".equals(resultBean.getRes())){
                                    Intent intent=new Intent(mContext,InstallModemActivity.class);
                                    startActivity(intent);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(handler!=null){
            handler.removeMessages(1);
        }
    }

    private void initEvent() {
        rl_top_bar.setOnTouchListener(new ComBackTouchListener());
    }
    private void getCatParameter() {
        String getCatParameterUrl = "http://" + mStarCatIp + "/cgi-bin/pointingstatus/";
        RequestParams params=new RequestParams();
        params.setHeader("Cookie", "loc=en");
        AppData.http.send(HttpRequest.HttpMethod.GET, getCatParameterUrl, params,
                new RequestCallBack<JSONObject>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }
                    @SuppressWarnings("rawtypes")
                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        //Toast.makeText(mContext,responseInfo.result.toString(),Toast.LENGTH_LONG).show();
                         starParamBean= GsonUtils.fromJson(responseInfo.result.toString(),StarParamBean.class);
                         if("1".equals(starParamBean.getLock())){
                            mChooseStatus.setImageDrawable(getResources().getDrawable(R.drawable.iv_right));
                            maxLevel.setText(starParamBean.getMax()+"dB");
                            mCurrentLevel.setText(starParamBean.getCur()+"dB");
                            mPrompt.setText("注意：对星成功，单击“下一步”启动调制解调器安装");
                            tv_next.setVisibility(View.VISIBLE);
                            handler.sendEmptyMessageDelayed(1, 900);
                        }else{
                            mChooseStatus.setImageDrawable(getResources().getDrawable(R.drawable.iv_error));
                            mPrompt.setText("注意：对星失败，请确认配置参数重新对星");
                            tv_next.setVisibility(View.GONE);
                            handler.sendEmptyMessageDelayed(1, 900);
                        }
                    }
                });

    }

}
