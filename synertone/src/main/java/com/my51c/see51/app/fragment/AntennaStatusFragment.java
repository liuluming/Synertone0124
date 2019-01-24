package com.my51c.see51.app.fragment;

import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.FloatMath;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.my51c.see51.BaseActivity;
import com.my51c.see51.Logger.LoggerSave;
import com.my51c.see51.adapter.ExpandableItemAdapter;
import com.my51c.see51.app.bean.AntennaStatusBean;
import com.my51c.see51.app.bean.Level0Item;
import com.my51c.see51.app.bean.TroubleBean;
import com.my51c.see51.app.http.XTHttpUtil;
import com.my51c.see51.app.utils.ChechIpMask;
import com.synertone.commonutil.util.GsonUtils;
import com.synertone.commonutil.util.MathUtil;
import com.synertone.netAssistant.R;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
public class AntennaStatusFragment extends BaseFragment {
    @BindView(R.id.tv_odutype)
    TextView tvOdutype;
    @BindView(R.id.tv_odunum)
    TextView tvOdunum;
    @BindView(R.id.tv_oduver)
    TextView tvOduver;
    @BindView(R.id.tv_locatype)
    TextView tvLocatype;
    @BindView(R.id.tv_lnglat)
    TextView tvLnglat;
    @BindView(R.id.tv_azi)
    TextView tvAzi;
    @BindView(R.id.tv_elevcarr)
    TextView tvElevcarr;
    @BindView(R.id.tv_roll)
    TextView tvRoll;
    @BindView(R.id.tv_rssi)
    TextView tvRssi;
    @BindView(R.id.tv_bucSwitch)
    TextView tvBucSwitch;
    @BindView(R.id.tv_temp)
    TextView tvTemp;
    @BindView(R.id.rv_antenna_trouble)
    RecyclerView rvAntennatrouble;
    @BindView(R.id.sv_content)
    ScrollView svContent;
    @BindView(R.id.tv_star_elev)
    TextView tvStarElev;
    private ExpandableItemAdapter expandableItemAdapter;
    private List<MultiItemEntity> troubleL0List;
    private boolean isExpanded=false;
    private boolean mStusFlag=false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_antenna_status_layout;
    }

    @Override
    protected void initEventAndData() {
        baseActivity.querySessionStatus(new BaseActivity.OnSessionStatusListener() {
            @Override
            public void sessionSuccess() {
                doAntennaStatus();
            }
        });
    }
    private void doAntennaStatus() {
        mStusFlag = true;
      AntennaStatusTask antennaStatusTask = new AntennaStatusTask();
        antennaStatusTask.execute(XTHttpUtil.GET_DEVSTATU_SATESATUS);
        LoggerSave.requestLog(XTHttpUtil.GET_DEVSTATU_SATESATUS,XTHttpUtil.GET_DEVSTATU_SATESATUS);
    }
    //实时查询天线状态
    private class AntennaStatusTask extends AsyncTask<String, String, String> {
        String recv = "";
        private int i;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            HttpGet hget = new HttpGet(params[0]);
            HttpClient hclient = new DefaultHttpClient();
            hclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
            HttpResponse respone;
            while (mStusFlag) {

                try {
                    respone = hclient.execute(hget);
                    recv = EntityUtils.toString(respone.getEntity());
                    LoggerSave.responseLog(params[0],recv);
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                publishProgress(recv);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            if(!isVisible){
                return;
            }
            if (recv.equals("")) {
                Toast toast = Toast.makeText(mContext, "连接网元服务器失败", Toast.LENGTH_SHORT);
                toast.show();
                if (i == 1)
                    mStusFlag = false;
                i++;
            } else {
                try {
                    AntennaStatusBean antennaStatusBean = GsonUtils.fromJson(recv, AntennaStatusBean.class);
                    if(antennaStatusBean==null){
                        return;
                    }
                    if (antennaStatusBean.getCode().equals("0")) {
                        loadData(antennaStatusBean);
                    } else if (antennaStatusBean.getCode().equals("-100")) {
                        Toast toast = Toast.makeText(mContext, "连接网元服务器失败", Toast.LENGTH_SHORT);
                        toast.show();
                        mStusFlag = false;
                    } else if (antennaStatusBean.getCode().equals("-1")) {
                        if (antennaStatusBean.getMsg().equals("acu_occupy")) {
                            baseActivity.showMutualDialog();
                            mStusFlag = false;
                        } else {
                            Toast toast = Toast.makeText(mContext, "查询天线状态失败", Toast.LENGTH_SHORT);
                            toast.show();
                            mStusFlag = false;
                        }
                    } else {
                        Toast toast = Toast.makeText(mContext, "查询天线状态失败", Toast.LENGTH_SHORT);
                        toast.show();
                        mStusFlag = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void loadData(AntennaStatusBean antennaStatusBean) {
        tvAzi.setText(antennaStatusBean.getAzi() +("--".equals(antennaStatusBean.getAzi())?"": "°"));
        tvElevcarr.setText(antennaStatusBean.getElevcarr() + ("--".equals(antennaStatusBean.getElevcarr())?"":"°"));
        String curlon=antennaStatusBean.getCurlon();
        String currlat=antennaStatusBean.getCurrlat();
        String lonType="E,";
        String latType="N";
        if(MathUtil.isNumber(curlon)){
            if(curlon!=null&&curlon.startsWith("-")){
                lonType="W,";
            }
            curlon =Math.abs(Float.parseFloat(ChechIpMask.numDigite2(curlon, 2)))+"";
        }
        if(MathUtil.isNumber(currlat)){
            if(currlat!=null&&currlat.startsWith("-")){
                latType="S";
            }
             currlat = Math.abs(Float.parseFloat(ChechIpMask.numDigite2(currlat,2)))+"";
        }
        tvLnglat.setText( curlon+ ("--".equals(curlon)?",":lonType)
                + currlat+ ("--".equals(currlat) ?"":latType));
        tvLocatype.setText(getFormat(antennaStatusBean.getLocatype()));
        tvOdunum.setText(antennaStatusBean.getOdunum());
        tvOdutype.setText(getFormatOdutype(antennaStatusBean.getOdutype()));
        tvRoll.setText(antennaStatusBean.getRoll() + ("--".equals(antennaStatusBean.getRoll())?"":"°"));
        tvOduver.setText(antennaStatusBean.getOduver());
        tvRssi.setText(antennaStatusBean.getRssi());
        tvBucSwitch.setText(getFormatBug(antennaStatusBean.getBucSwitch()));
        tvTemp.setText(antennaStatusBean.getTemp()+"°C"+getTempWarn(antennaStatusBean.getHot()));
        tvStarElev.setText(antennaStatusBean.getElev());
        initTroubleRecycleView(antennaStatusBean);
    }

    private void initTroubleRecycleView(AntennaStatusBean antennaStatusBean) {
        if(troubleL0List ==null){
            troubleL0List =new ArrayList<>();
        }

        Level0Item level0Item = new Level0Item(getTroubleNum(antennaStatusBean));
        List<TroubleBean> troubleList = getTroubleList(antennaStatusBean);
        for(TroubleBean troubleBean:troubleList){
            level0Item.addSubItem(new TroubleBean(troubleBean.getName(),troubleBean.getInfo()));
        }
        troubleL0List.clear();
        troubleL0List.add(level0Item);
         if(expandableItemAdapter==null){
             expandableItemAdapter=new ExpandableItemAdapter(troubleL0List);
             rvAntennatrouble.setAdapter(expandableItemAdapter);
             DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
             dividerItemDecoration.setDrawable(ContextCompat.getDrawable(mContext,R.drawable.shape_gray_line));
             rvAntennatrouble.addItemDecoration(dividerItemDecoration);
             rvAntennatrouble.setLayoutManager(new LinearLayoutManager(mContext));
         }else{
             expandableItemAdapter.notifyDataSetChanged();
             if(isExpanded){
                 expandableItemAdapter.expandAll();
             }

         }
        expandableItemAdapter.setOnexpandedListener(new ExpandableItemAdapter.OnexpandedListener() {
            @Override
            public void expanded() {
                isExpanded=true;
                svContent.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        svContent.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        });
        expandableItemAdapter.setOnCollapseListener(new ExpandableItemAdapter.OnCollapseListener() {
            @Override
            public void collapse() {
                isExpanded=false;
            }
        });
    }



    private String getTempWarn(String hot) {
       // 0 正常 -1 过温
       if("-1".equals(hot)){
          return "过温";
       }
        return "";
    }

    private String getFormatBug(String bucSwitch) {
        //buc 开关0 buc关闭(自动模式)，1 buc开启(自动模式),2 buc关闭(手//动模式),3 buc开启(手动模式)，4 buc异常
        String msg="未知";
        switch (bucSwitch){
            case "0":
                msg="关闭";
                break;
            case "1":
                msg="开启";
                break;
            case "2":
                msg="关闭";
                break;
            case "3":
                msg="开启";
                break;
            case "4":
                msg="异常";
                break;
        }
        return msg;
    }

    private String getTroubleNum(AntennaStatusBean antennaStatusBean) {
        List<TroubleBean> warnList = getTroubleList(antennaStatusBean);
        return warnList.size()+"";
    }

    private List<TroubleBean> getTroubleList(AntennaStatusBean antennaStatusBean) {
        List<TroubleBean> list=new ArrayList<>();
        if ("-1".equals(antennaStatusBean.getVol())) {
            TroubleBean troubleBean=new TroubleBean("电压状态","故障");
            list.add(troubleBean);
        }
        if ("-1".equals(antennaStatusBean.getAzimotor())) {
            TroubleBean troubleBean=new TroubleBean("方位电机状态","故障");
            list.add(troubleBean);
        }
        if ("-1".equals(antennaStatusBean.getElevmotor())) {
            TroubleBean troubleBean=new TroubleBean("俯仰电机状态","故障");
            list.add(troubleBean);
        }
        if ("-1".equals(antennaStatusBean.getSendzero())) {
            TroubleBean troubleBean=new TroubleBean("发射归零状态","故障");
            list.add(troubleBean);
        }
        if ("-1".equals(antennaStatusBean.getElevzero())) {
            TroubleBean troubleBean=new TroubleBean("俯仰归零状态","故障");
            list.add(troubleBean);
        }
        if ("-1".equals(antennaStatusBean.getGyroscope())) {
            if(!"7".equals(antennaStatusBean.getOdutype())&&!"8".equals(antennaStatusBean.getOdutype())){
                TroubleBean troubleBean=new TroubleBean("陀螺仪状态","故障");
                list.add(troubleBean);
            }
        }
        if ("-1".equals(antennaStatusBean.getCompass())) {
            if("7".equals(antennaStatusBean.getOdutype())||"8".equals(antennaStatusBean.getOdutype())){
                TroubleBean troubleBean=new TroubleBean("电子罗盘状态","故障");
                list.add(troubleBean);
            }
        }
        if ("-1".equals(antennaStatusBean.getRf())) {
            TroubleBean troubleBean=new TroubleBean("射频信号状态","故障");
            list.add(troubleBean);
        }
        /*if ("-1".equals(antennaStatusBean.getPosition())) {
            TroubleBean troubleBean=new TroubleBean("位置设置状态","位置错误");
            list.add(troubleBean);
        }*/
        if ("-1".equals(antennaStatusBean.getCurrent())) {
            TroubleBean troubleBean=new TroubleBean("天线状态","过流");
            list.add(troubleBean);
        }
        if ("-1".equals(antennaStatusBean.getZaizero())) {
            if("7".equals(antennaStatusBean.getOdutype())||"8".equals(antennaStatusBean.getOdutype())){
                TroubleBean troubleBean=new TroubleBean("方位归零状态","故障");
                list.add(troubleBean);
            }
        }
        if ("-1".equals(antennaStatusBean.getRollzero())) {
            if("4".equals(antennaStatusBean.getOdutype())||"6".equals(antennaStatusBean.getOdutype())){
                TroubleBean troubleBean=new TroubleBean("横滚归零状态","故障");
                list.add(troubleBean);
            }
        }
        if ("-1".equals(antennaStatusBean.getElevout())) {
            TroubleBean troubleBean=new TroubleBean("俯仰角状态","超范围");
            list.add(troubleBean);
        }
        if ("-1".equals(antennaStatusBean.getGpsStatus())) {
            TroubleBean troubleBean=new TroubleBean("GPS状态","故障");
            list.add(troubleBean);
        }
        return list;

    }

    private String getFormatOdutype(String odutype) {
        // 0 未知天线类型 1  V系列天线  2 S系列天线 3 C系列天线
        switch (odutype) {
            case "0":
                return "未知";
            case "1":
                return "V4";
            case "2":
                return "V6";
            case "3":
                return "S6";
            case "4":
                return "S6A";
            case "5":
                return "S8";
            case "6":
                return "S9";
            case "7":
                return "C6";
            case "8":
                return "C9";
            default:
                break;
        }
        return "未知";
    }

    private String getFormat(String locatype) {
        //定位方式  0 自动  1 GPS   2 北斗
        switch (locatype) {
            case "0":
                return "手动";
            case "1":
                return "GPS";
            case "2":
                return "北斗";
            default:
                break;
        }
        return "未知";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mStusFlag = false;
    }
}
