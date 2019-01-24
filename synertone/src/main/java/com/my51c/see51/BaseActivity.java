package com.my51c.see51;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.my51c.see51.app.LoginActivity;
import com.my51c.see51.app.bean.DevStatusBean;
import com.my51c.see51.app.fragment.WarnDialogFragment;
import com.my51c.see51.app.http.XTHttpUtil;
import com.my51c.see51.app.utils.GsonUtils;
import com.my51c.see51.common.AppData;
import com.my51c.see51.common.MyDensityUtil;
import com.my51c.see51.widget.SharedPreferenceManager;
import com.synertone.commonutil.view.BaseNiceDialog;
import com.synertone.commonutil.view.ConfirmDialog;
import com.synertone.commonutil.view.ViewConvertListener;
import com.synertone.commonutil.view.ViewHolder;
import com.synertone.netAssistant.R;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

public class BaseActivity extends SupportActivity {
    protected Context mContext;
    private WarnDialogFragment warnDialogFragment;
    private FragmentManager supportFragmentManager;
    protected boolean isVisible;
    private UpdateUIBroadcastReceiver broadcastReceiver;
    private ProgressDialog pd;
    private static ArrayList<String> lastWarnList;
    protected AppData application;
    private Unbinder bind;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        bind= ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        bind= ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        bind= ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bind!=null){
            bind.unbind();
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        LayoutInflater.from(this).setFactory2(new LayoutInflater.Factory2() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                if("EditText".equals(name)){
                    EditText editText=new EditText(context,attrs);
                    editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
                    return  editText;
                }
                return null;
            }

            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                if("EditText".equals(name)){
                    EditText editText=new EditText(context,attrs);
                    editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
                    return  editText;
                }
                return null;
            }
        });
        super.onCreate(arg0);
        mContext = this;
        application = (AppData) getApplication();
        application.addActivity(new WeakReference<Activity>(this));
        supportFragmentManager = getSupportFragmentManager();
        warnDialogFragment = new WarnDialogFragment(this);
        pd = new ProgressDialog(mContext);
        pd.setCanceledOnTouchOutside(false);

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
                    application.removeAct((Activity) mContext);
                    finish();
                }
            }
            return false;
        }
    }
    public void showMutualDialog() {
        /*if(isFinishing()||!isVisible){
			return;
		}*/
        ConfirmDialog.init().setConvertListener(new ViewConvertListener() {
            @Override
            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                holder.setText(R.id.tv_tip,getString(R.string.acu_hold_tip));
                holder.setOnClickListener(R.id.bt_ok, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        }).show(getSupportFragmentManager());
    }

    public void showLoginDialog() {
        if(isVisible){
            ConfirmDialog.init().setConvertListener(new ViewConvertListener() {
                @Override
                public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                    holder.setText(R.id.tv_tip,getString(R.string.relogin_tip));
                    holder.setOnClickListener(R.id.bt_ok, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            application.exit();
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }).show(getSupportFragmentManager()).setOutCancel(false);
        }

    }
    public void showDia(String... msg) {
        if (msg != null && msg.length > 0) {
            pd.setMessage(msg[0] + ".....");
        } else {
            pd.setMessage("正在加载数据.....");
        }
        if(isVisible){
            pd.show();
        }

    }

    public void dismissDia() {
        if (pd != null&&isVisible) {
            pd.dismiss();
        }
    }
   protected boolean isShowingDia(){
       if(pd!=null&&isVisible){
           return  pd.isShowing();
       }else{
           return  false;
       }

   }
    private void showWarnDialogFragment(ArrayList<String> warnList) {
        if (!isFinishing() && isVisible) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("value", warnList);
            if (!warnDialogFragment.isAdded()) {
                warnDialogFragment.setArguments(bundle);
            }
            if (!warnDialogFragment.isVisible()) {
                warnDialogFragment.show(supportFragmentManager, "");
            } else {
                warnDialogFragment.reFreshData(bundle);
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
        // 动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("ServiceUpdateUI");
        broadcastReceiver = new UpdateUIBroadcastReceiver();
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isVisible = false;
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver=null;
        }
    }
    private void doDevstatu() {
        try {
            RequestParams params = new RequestParams("UTF-8");
            JSONObject jsonObjet = new JSONObject();
            jsonObjet.put("sessionToken", AppData.accountModel.getSessionToken());
            params.setBodyEntity(new StringEntity(jsonObjet.toString(), "UTF-8"));
            params.setContentType("applicatin/json");
            AppData.http.send(HttpMethod.POST,
                    XTHttpUtil.devstatu, params, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            //Toast.makeText(mContext, responseInfo.result, 1).show();
                            DevStatusBean devStatusBean = GsonUtils.fromJson(responseInfo.result, DevStatusBean.class);
                            if (devStatusBean != null) {
                                String code = devStatusBean.getCode();
                                if ("0".equals(code)) {
                                    ArrayList<String> warnList = new ArrayList<>();
                                    initWarnList(devStatusBean, warnList);
                                    if (warnList.size() > 0) {
                                        if (!isFinishing() && isVisible) {
                                            if(lastWarnList!=null&&lastWarnList.equals(warnList)){
                                                boolean isNoRead = SharedPreferenceManager.getBoolean(mContext, "isNoRead");
                                                  if(isNoRead){
                                                      showWarnDialogFragment(warnList);
                                                  }
                                            }else{
                                                SharedPreferenceManager.saveBoolean(mContext,"isNoRead",true);
                                                showWarnDialogFragment(warnList);
                                            }
                                            lastWarnList=warnList;
                                        }
                                    }
                                } else if("-2".equals(code)){
                                    if (!isFinishing() && isVisible) {
                                            //showLoginDialog();
                                    }
                                    //if (!isFinishing() && isVisible) {
                                        //Toast.makeText(mContext, devStatusBean.getMsg(), Toast.LENGTH_LONG).show();

                                }
                            }


                        }

                        @Override
                        public void onFailure(HttpException arg0, String arg1) { //
                            //TODO Auto-generated method stub

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化 错误信息
     *
     * @param devStatusBean
     * @param warnList
     */
    private void initWarnList(DevStatusBean devStatusBean, List<String> warnList) {
        String gl = devStatusBean.getGeneral();
        if ("-1".equals(gl)) {
            warnList.add(getString(R.string.warn_gl));
        }
        String current = devStatusBean.getCurrent();
        if ("-1".equals(current)) {
            warnList.add(getString(R.string.wran_current));
        }
        String hot = devStatusBean.getHot();
        if ("-1".equals(hot)) {
            warnList.add(getString(R.string.warn_hot));
        }
        String voltage = devStatusBean.getVoltage();
        if ("-1".equals(voltage)) {
            warnList.add(getString(R.string.warn_voltage));
        }
        String search = devStatusBean.getSearch();
        if ("-1".equals(search)) {
            warnList.add(getString(R.string.warn_search));
        }
        String oriemotor = devStatusBean.getOrimotor();
        if ("-1".equals(oriemotor)) {
            warnList.add(getString(R.string.warn_oriemotor));
        }
        String sendzero = devStatusBean.getSendzero();
        if ("-1".equals(sendzero)) {
            warnList.add(getString(R.string.warn_send_zero));
        }
        String pitchmotor = devStatusBean.getPitchmotor();
        if ("-1".equals(pitchmotor)) {
            warnList.add(getString(R.string.warn_pitchmotor));
        }
        String rollzero = devStatusBean.getRollzero();
        if ("-1".equals(rollzero)) {
            warnList.add(getString(R.string.warn_rollzero));
        }
        String oriezero = devStatusBean.getOrizero();
        if ("-1".equals(oriezero)) {
            warnList.add(getString(R.string.wran_oriezero));
        }
        String gps = devStatusBean.getGps();
        if ("-1".equals(gps)) {
            warnList.add(getString(R.string.warn_gps));
        }
        String rf = devStatusBean.getRf();
        if ("-1".equals(rf)) {
            warnList.add(getString(R.string.warn_rf));
        }
        String pitchzero = devStatusBean.getPitchzero();
        if ("-1".equals(pitchzero)) {
            warnList.add(getString(R.string.warn_pitchzero));
        }
        String sensor = devStatusBean.getSensor();
        if ("-1".equals(sensor)) {
            String odType = devStatusBean.getOdutype();
            if (odType.contains("7") || odType.equals("8")) {
                warnList.add(getString(R.string.warn_dzlp));
            } else {
                warnList.add(getString(R.string.warn_tly));
            }
        }
    }

    class UpdateUIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(mContext, "onReceive", 0).show();
            if (AppData.accountModel != null) {
                doDevstatu();
            }

        }


    }

    public void querySessionStatus(final OnSessionStatusListener onSessionStatusListener) {
        final String mToken = SharedPreferenceManager.getString(mContext, "mToken");
        String queryStatusUrl = XTHttpUtil.QUERY_STATUS;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sessionToken", mToken);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    queryStatusUrl, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                String code = jsonObject.getString("code");
                                if ("0".equals(code)) {
                                    //Session未失效
                                    onSessionStatusListener.sessionSuccess();
                                } else if ("-1".equals(code)) {
                                    //Session失效
                                   showLoginDialog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    onSessionStatusListener.sessionErrorResponse();
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0, 0f));
            AppData.mRequestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public abstract static class OnSessionStatusListener{
        public abstract void sessionSuccess();
        public void sessionErrorResponse(){

        }
    }
}


