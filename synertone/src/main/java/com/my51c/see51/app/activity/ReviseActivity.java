package com.my51c.see51.app.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.came.viewbguilib.ButtonBgUi;
import com.my51c.see51.BaseActivity;
import com.my51c.see51.app.LoginActivity;
import com.my51c.see51.app.PersonalActivity;
import com.my51c.see51.app.http.XTHttpUtil;
import com.my51c.see51.app.utils.XTHttpJSON;
import com.my51c.see51.common.AppData;
import com.my51c.see51.listener.NoDoubleClickListener;
import com.synertone.netAssistant.R;

import org.json.JSONException;
import org.json.JSONObject;

/*个人中心密码修改*/
public class ReviseActivity extends BaseActivity implements OnClickListener {
    protected static final String TAG = "ReviseActivity";
    private ButtonBgUi mReviseSavePasswork,revise_password_cancel;
    private EditText mReviseOldPasswork, mReviseNewPasswork, mReviseRetSetPass;
    private TextView textView1, querenmima, xinmima_tv, yuanshimima_tv;
    private String mCode, mPwdToken;
    //获取文本的方法
    private String oldPassWorkStr, newPassWorkStr, retSetPassStr;
    // 加载数据的 ProgressDialog
    private ProgressDialog pd;
    private boolean progresshow;
    private RelativeLayout rl_top_bar;
    private TextView mTittle;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.revise_password_activity);
        if (AppData.accountModel != null) {
            mPwdToken = AppData.accountModel.getSessionToken();
        }
        initView();
        initEvent();
    }

    private void initEvent() {
        mReviseSavePasswork.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
               System.out.print("------------------------onNoDoubleClick--------------------------");
                getTextPassStr();
                if (!"".equals(oldPassWorkStr)) {
                    if (newPassWorkStr.length() >= 6 && newPassWorkStr.length() <= 8 && retSetPassStr.length() >= 6 && retSetPassStr.length() <= 8
                            && oldPassWorkStr.length() >= 6 && oldPassWorkStr.length() <= 8) {
                    /*if (newPassWorkStr.length()== 8 && retSetPassStr.length()== 8 && oldPassWorkStr.length()== 8) {*/
                        if (newPassWorkStr.equals(oldPassWorkStr)) {
                            Toast.makeText(mContext, "原密码与新密码不能相同", 0).show();
                        } else if (newPassWorkStr.equals(retSetPassStr)) {
                            postPassWork(oldPassWorkStr, newPassWorkStr, retSetPassStr);
                        } else {
                            Toast.makeText(mContext, "新密码与确认密码不一致,请重新输入", 0).show();

                            return;
                        }
                    } else {
                        /*Toast.makeText(mContext, "密码为8位数字,不能使用其他字符!", 0).show();*/
                        Toast.makeText(mContext, "密码为6-8位的数字、字母或组合!", 0).show();
                        return;
                    }
                } else {
                    Toast.makeText(mContext, "原密码不能为空", 0).show();
                    return;
                }
            }
        });
            rl_top_bar.setOnTouchListener(new ComBackTouchListener());
    }

    private void initView() {
        rl_top_bar= (RelativeLayout) findViewById(R.id.rl_top_bar);
        mTittle= (TextView)findViewById(R.id.tv_bar_title);
        iv_back= (ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        mTittle.setText(R.string.modify_pwd);
        querenmima = (TextView) findViewById(R.id.querenmima);
        querenmima.setTypeface(AppData.fontXiti);
        SpannableStringBuilder style = new SpannableStringBuilder("*确认密码:");
        style.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        querenmima.setText(style);
        xinmima_tv = (TextView) findViewById(R.id.xinmima_tv);
        xinmima_tv.setTypeface(AppData.fontXiti);
        SpannableStringBuilder style1 = new SpannableStringBuilder("*新密码:");
        style1.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        xinmima_tv.setText(style1);
        yuanshimima_tv = (TextView) findViewById(R.id.yuanshimima_tv);
        yuanshimima_tv.setTypeface(AppData.fontXiti);
        SpannableStringBuilder style2 = new SpannableStringBuilder("*原密码:");
        style2.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        yuanshimima_tv.setText(style2);
        mReviseSavePasswork = (ButtonBgUi) findViewById(R.id.revise_password_btnsave);
        revise_password_cancel = (ButtonBgUi) findViewById(R.id.revise_password_cancel);
        revise_password_cancel.setOnClickListener(this);
        mReviseOldPasswork = (EditText) findViewById(R.id.revise_oldpassword);
        mReviseNewPasswork = (EditText) findViewById(R.id.revise_newpassword);
        mReviseRetSetPass = (EditText) findViewById(R.id.revise_resetpassword);
        if (AppData.accountModel != null) {
            mPwdToken = AppData.accountModel.getSessionToken();
        }
    }

    private void getTextPassStr() {
        oldPassWorkStr = mReviseOldPasswork.getText().toString();
        newPassWorkStr = mReviseNewPasswork.getText().toString();
        retSetPassStr = mReviseRetSetPass.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.revise_password_cancel:
                Intent mIntent = new Intent(ReviseActivity.this, PersonalActivity.class);
                startActivity(mIntent);
                finish();
                break;
            case R.id.iv_back:
                revisePWOnFinish();
                break;
        }
    }

    //post密码修改
    private void postPassWork(final String oldPassWorkStr, final String newPassWorkStr, final String retSetPassStr) {
        progresshow = true;
        showDia();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oldPasswd", XTHttpJSON.string2MD5(oldPassWorkStr));
            jsonObject.put("newPasswd1", XTHttpJSON.string2MD5(newPassWorkStr));
            jsonObject.put("newPasswd2", XTHttpJSON.string2MD5(retSetPassStr));
            jsonObject.put("sessionToken", mPwdToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Method.POST,
                XTHttpUtil.POST_MODIFYPASS_ADDRESS, jsonObject,
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pdDismiss(response);
                        try {
                            //使用JSONObject给response转换编码
                            JSONObject jsonObject = new JSONObject(response.toString());
                            mCode = jsonObject.getString("code");
                            String msg = jsonObject.optString("msg");
                            if (mCode.equals("0")) {
                                Intent intent = new Intent(ReviseActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(getApplicationContext(), "修改成功", 0).show();
                            } else if (mCode.equals("2")) {
                                Toast.makeText(getApplicationContext(), "密码错误", 0).show();
                            } else if (mCode.equals("-1")) {
                                Toast.makeText(getApplicationContext(), "修改失败", 0).show();
                            } else if (mCode.equals("-2")) {
                                showLoginDialog();
                           }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "连接网元服务器失败", Toast.LENGTH_SHORT).show();
                Log.i(TAG, error.toString());
                if (pd.isShowing()) {
                    pd.dismiss();
                }
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(20*1000,0,0f));
        AppData.mRequestQueue.add(request);
    }

    private void showDia() {
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

    // 有数据的时候ProgressDialog消失
    private void pdDismiss(JSONObject object) {
        if (object != null) {
            pd.dismiss();
        }
    }

    public void revisePWOnFinish() {
        finish();
    }

}
