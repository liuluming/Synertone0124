package com.my51c.see51.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.my51c.see51.app.bean.ModemBean;
import com.my51c.see51.app.bean.ResultBean;
import com.my51c.see51.app.utils.ChechIpMask;
import com.my51c.see51.app.utils.GsonUtils;
import com.my51c.see51.common.AppData;
import com.synertone.commonutil.view.BaseNiceDialog;
import com.synertone.commonutil.view.ConfirmDialog;
import com.synertone.commonutil.view.ViewConvertListener;
import com.synertone.commonutil.view.ViewHolder;
import com.synertone.netAssistant.R;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigParamActivity extends BaseActivity {

    private EditText mGroupId;
    private EditText mTerminalId;
    private ImageView mBack;
    private TextView mTittle;
    private RelativeLayout rl_top_bar;
    private TextView tv_next;
    private String mStarCatIp;
    private ModemBean modemBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_param);
        initView();
        initEvent();
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
        mTittle.setText(R.string.modem_set);
        mStarCatIp = getIntent().getStringExtra("ip");
        mGroupId = (EditText) findViewById(R.id.et_group_id);
        mTerminalId = (EditText) findViewById(R.id.et_terminal_id);
        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_next.setText(R.string.tv_next);
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyInput()) {
                    return;
                }
                List<ModemBean> modemBeans = DataSupport.where("mgid = ?", mGroupId.getText().toString()).find(ModemBean.class);
                if (modemBeans != null && modemBeans.size() > 0) {
                    modemBean = modemBeans.get(0);
                }
                if(modemBean!=null) {
                    setCatFirstParameter();
                }
            }
        });
        initData();
    }

    private void initEvent() {
        rl_top_bar.setOnTouchListener(new ComBackTouchListener());
    }

    private void initData() {
        mGroupId.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) {
                    if ("--".equals(mGroupId.getText().toString())) {
                        mGroupId.setText("");
                    }
                }

            }
        });
        mTerminalId.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) {
                    if ("--".equals(mTerminalId.getText().toString())) {
                        mTerminalId.setText("");
                    }
                }

            }
        });
    }

    private boolean verifyInput() {
        if ("--".equals(mGroupId.getText().toString())) {
            Toast.makeText(mContext, "请输入管理组ID", Toast.LENGTH_LONG).show();
            return true;
        }
        if (StringUtils.isEmpty(mGroupId.getText().toString())) {
            Toast.makeText(mContext, "请输入管理组ID", Toast.LENGTH_LONG).show();
            return true;
        }
        String regex = "^0+";
        Pattern p=Pattern.compile(regex);
        Matcher m=p.matcher(mGroupId.getText().toString());
        boolean matches = m.lookingAt();
        if (!ChechIpMask.a2b(this.mGroupId.getText().toString(), 1, 250)|| matches) {
            Toast.makeText(mContext, "输入的管理组ID不合法！", Toast.LENGTH_SHORT).show();
           return  true;

        }
        if(!AppData.migIdList.contains(this.mGroupId.getText().toString())){
            isNoGroupIdDialog();
            return true;
        }
        if ("--".equals(mTerminalId.getText().toString())) {
            Toast.makeText(mContext, "请输入终端ID", Toast.LENGTH_LONG).show();
            return true;
        }

        if (StringUtils.isEmpty(mTerminalId.getText().toString())) {
            Toast.makeText(mContext, "请输入终端ID", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
    private void isNoGroupIdDialog() {
        ConfirmDialog.init().setConvertListener(new ViewConvertListener() {
            @Override
            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                holder.setText(R.id.tv_tip,getString(R.string.no_group_id));
                holder.setOnClickListener(R.id.bt_ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        }).show(getSupportFragmentManager());

    }
    private void setCatFirstParameter() {
        showDia();
        String getCatParameterUrl = "http://" +  mStarCatIp + "/cgi-bin/instlocation/";
        RequestParams params=new RequestParams();
        params.setHeader("Cookie", "loc=en");
        params.addBodyParameter("loc","000M");
        params.addBodyParameter("cluster",modemBean.getCluster());
        AppData.http.send(HttpRequest.HttpMethod.POST, getCatParameterUrl, params,
                new RequestCallBack<JSONObject>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        dismissDia();
                    }
                    @SuppressWarnings("rawtypes")
                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        try {
                            if(responseInfo!=null){
                                ResultBean resultBean= GsonUtils.fromJson(responseInfo.result.toString(),ResultBean.class);
                                if("1".equals(resultBean.getRes())){
                                    setCatParameter();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
    private void setCatParameter() {
        String getCatParameterUrl = "http://" + mStarCatIp + "/cgi-bin/instpro/";
        RequestParams params = new RequestParams();
        params.setHeader("Cookie", "loc=en");
        params.addBodyParameter("pro_mgid", modemBean.getMgid());
        params.addBodyParameter("pro_terminal", mTerminalId.getText().toString());
        params.addBodyParameter("pro_ob_freq", modemBean.getFreq());
        params.addBodyParameter("pro_ob_symbolrate", modemBean.getSymbolrate());
        params.addBodyParameter("pro_adv_lnblo", modemBean.getLnblo());
        params.addBodyParameter("pro_adv_clustergroup", modemBean.getClustergroup());
        params.addBodyParameter("pro_buclo", modemBean.getBuclo());
        params.addBodyParameter("pro_10Mhz", "1");
        params.addBodyParameter("pro_audio", "0");
        params.addBodyParameter("pro_opmode", "1");
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
                        //Toast.makeText(mContext, responseInfo.result.toString(), Toast.LENGTH_LONG).show();
                        try {
                            if (responseInfo != null) {
                                ResultBean resultBean = GsonUtils.fromJson(responseInfo.result.toString(), ResultBean.class);
                                if ("1".equals(resultBean.getRes())) {
                                    Intent intent = new Intent(mContext, SatelliteParamActivity.class);
                                    intent.putExtra("ip", mStarCatIp);
                                    startActivity(intent);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}
