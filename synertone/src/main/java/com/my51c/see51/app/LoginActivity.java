package com.my51c.see51.app;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.my51c.see51.BaseActivity;
import com.my51c.see51.app.bean.AccountModel;
import com.my51c.see51.app.bean.StarCodeModel;
import com.my51c.see51.app.utils.GsonUtils;
import com.my51c.see51.app.utils.XTHttpJSON;
import com.my51c.see51.common.AppData;
import com.my51c.see51.widget.SharedPreferenceManager;
import com.synertone.netAssistant.R;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

import static com.my51c.see51.app.http.XTHttpUtil.POST_LOGIN_ADDRESS;


public class LoginActivity extends BaseActivity {
    private EditText mEdUser, mEdPassW;
    private String mStrUser, mStrPassW;
    private Button mLoginButton;
    private String mToken, mGetUser, mGetPassWord,mCode;
    private CheckBox mCheckButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initView();
        initData();
    }

    private void initData() {
        mGetUser = SharedPreferenceManager.getString(LoginActivity.this, "subscriberId");
        mGetPassWord = SharedPreferenceManager.getString(LoginActivity.this, "passwd");
        boolean isNotChecked = SharedPreferenceManager.getBoolean(mContext, "isNotChecked");
        if(isNotChecked){
            mEdUser.setText("");
            mEdPassW.setText("");
            mCheckButton.setChecked(false);
        }else{
            mEdUser.setText(mGetUser);
            mEdPassW.setText(mGetPassWord);
            mCheckButton.setChecked(true);
        }
    }

    private void initView() {
        mCheckButton = (CheckBox) findViewById(R.id.remember);
        mEdUser = (EditText) findViewById(R.id.login_ed_user);
        mEdPassW = (EditText) findViewById(R.id.login_ed_password);
        mLoginButton = (Button) findViewById(R.id.btn_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });
        // 用户名改变的时候清空密码输入框
        mEdUser.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mEdPassW.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    // 点击登录
    public void onLogin() {
        mStrUser = mEdUser.getText().toString();
        mStrPassW = mEdPassW.getText().toString();
        SharedPreferenceManager.saveString(LoginActivity.this, "subscriberId", mStrUser);
        SharedPreferenceManager.saveString(LoginActivity.this, "passwd", mStrPassW);
        if(mCheckButton.isChecked()){
            SharedPreferenceManager.saveBoolean(LoginActivity.this, "isNotChecked", false);
        }else{
            SharedPreferenceManager.saveBoolean(LoginActivity.this, "isNotChecked", true);
        }
        if (!TextUtils.isEmpty(mStrUser)) {
            if (!TextUtils.isEmpty(mStrPassW)) {
              //handlerSuccessLogin(mStrUser, mStrPassW);
              loginHttp(mStrUser, mStrPassW);
            } else {
                Toast.makeText(getApplicationContext(), "密码不能为空，请输入密码", 0).show();
                mEdPassW.requestFocus();
            }
        } else {
            Toast.makeText(getApplicationContext(), "用户名不能为空，请输入用户名", 0).show();
            mEdUser.requestFocus();
        }
    }

    /* 登录的请求 */
    private void loginHttp(final String mStrUser, final String mStrPassW) {
        showDia();
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", mStrUser);
            jsonObject.put("passwd", XTHttpJSON.string2MD5(mStrPassW));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    POST_LOGIN_ADDRESS, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dismissDia();
                            try {
                                JSONObject jsonObject = null;
                                jsonObject = new JSONObject(response.toString());
                                mCode = jsonObject.optString("code");
                                mToken = jsonObject.optString("sessionToken", null);
                                if (mCode.equals("0")) {
                                    initAccountData(mStrUser, mStrPassW, mToken);
                                    Toast.makeText(getApplicationContext(), "登录成功", 0).show();
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (mCode.equals("1")) {
                                    Toast.makeText(getApplicationContext(), "用户不存在", 0).show();
                                    mLoginButton.setEnabled(true);
                                } else if (mCode.equals("2")) {
                                    Toast.makeText(getApplicationContext(), "用户名或密码错误", 0).show();
                                    mLoginButton.setEnabled(true);
                                } else {
                                    Toast.makeText(getApplicationContext(), "登录失败", 0).show();
                                    mLoginButton.setEnabled(true);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "连接网元服务器失败", Toast.LENGTH_SHORT).show();
                    mLoginButton.setEnabled(true);
                    dismissDia();
                  /*Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);*/
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0, 0f));
            AppData.mRequestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void handlerSuccessLogin(String mStrUser, String mStrPassW) {
        initAccountData(mStrUser, mStrPassW, mToken);
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    private void initAccountData(String mStrUser, String mStrPassW, String mToken) {
        AccountModel accountModel = new AccountModel();
        if (mToken == null) {
            mToken = SharedPreferenceManager.getString(mContext, "mToken");
        } else {
            SharedPreferenceManager.saveString(mContext, "mToken", mToken);
        }
        accountModel.setSessionToken(mToken);
        accountModel.setUser(mStrUser);
        accountModel.setPasswd(mStrPassW);
        AppData.accountModel = accountModel;
        reSetCurrentSession(mToken);
        upDateDataBaseSession(mToken);
    }

    private void upDateDataBaseSession(String mToken) {
        List<StarCodeModel> all = DataSupport.findAll(StarCodeModel.class);
        for(StarCodeModel codeModel:all){
            if(codeModel.getSessionToken()!=null){
                codeModel.setSessionToken(mToken);
                codeModel.update(codeModel.getId());
            }
        }
    }

    private void reSetCurrentSession(String mToken) {
        String savedStar = SharedPreferenceManager.getString(mContext,"currentStar");
        if(savedStar!=null){
            StarCodeModel currentStar = GsonUtils.fromJson(savedStar, StarCodeModel.class);
            currentStar.setSessionToken(mToken);
            SharedPreferenceManager.saveString(mContext,"currentStar",GsonUtils.toJson(currentStar));
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();

    }

    @Override
    protected void onResume () {
        super.onResume();
    }

    @Override
    protected void onStop () {
        super.onStop();

    }

    @Override
    protected void onPause () {
        super.onPause();
    }

}
