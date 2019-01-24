package com.my51c.see51.app.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.my51c.see51.app.utils.ChechIpMask;
import com.synertone.netAssistant.R;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

//卫星状态
public class StartStauFragment extends Fragment {
    protected static final String TAG = "StartStauFragment";
    private TextView mDeviceAzi, mDevicElevcar, mDeviceRoll, mDeviceCurrent,
            mDeviceCurrentlat, mDeviceRssi, mDevicElev,
            mDeviceTemp, mDeviceLocatype, mDeviceHot, mDeviceVol,
            mDeviceAzimotor, mDevicElevmotor, mDeviceSendzero, mDeviceElevzero,
            mDeviceGyroscope, mGpsStatus,mDeviceRf, mDeviceCtrlnum, mDeviceOudver, mDeviceOverCurrent,mDeviceStartDirZero,mDeviceStartHengZero,mDeviceLnbFreq,buc_swith_status,
            mDeviceBucFreq,buc_adjust_range,mDeviceOdenum,device_star_compass;
    private View view;// 缓存Fragment view
    private String statUrl;
    private JSONObject response;
    private LinearLayout ll_start_gyroscope,ll_start_hengzero,ll_start_dirzero,ll_start_roll,ll_star_compass,ll_start_elevcarrier;
    private TextView mTianxianStyle;
    private HashMap<String, Toast> toaHashMap=new HashMap<>();
    public StartStauFragment(String statUrl, JSONObject response) {
        this.statUrl = statUrl;
        this.response = response;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.device_start_statu_fragment,
                container, false);
		/*
		 * AppData.mRequestQueue = Volley.newRequestQueue(StartStauFragment.this
		 * .getActivity());
		 */
        // 缓存的rootView需要判断是否已经被加过parent，
        // 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		/*
		 * ViewGroup parent = (ViewGroup) view.getParent(); if (parent != null)
		 * { parent.removeView(view); }
		 */
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // devStatuQuery();
    }
    /*
     * @Override public void setUserVisibleHint(boolean isVisibleToUser) { if
     * (isVisibleToUser) {
     *
     * } else { return; } super.setUserVisibleHint(isVisibleToUser); }
     */
    private void initView(View view) {
        mDeviceAzi = (TextView) view.findViewById(R.id.device_start_azi);
        mGpsStatus = (TextView) view.findViewById(R.id.device_gps_status);
        mDevicElevcar= (TextView) view
                .findViewById(R.id.device_start_elevcarrier);
        mDeviceRoll = (TextView) view.findViewById(R.id.device_start_roll);
        mDeviceCurrent = (TextView) view
                .findViewById(R.id.device_start_currentlon);
        mDeviceCurrentlat = (TextView) view
                .findViewById(R.id.device_start_currentlat);
        mDeviceRssi = (TextView) view.findViewById(R.id.device_start_rssi);

        mDevicElev = (TextView) view.findViewById(R.id.device_start_elev);
        mDeviceTemp = (TextView) view.findViewById(R.id.device_start_temp);
        mDeviceLocatype = (TextView) view
                .findViewById(R.id.device_start_locatype);
        mDeviceHot = (TextView) view.findViewById(R.id.device_start_hot);
        mDeviceVol = (TextView) view.findViewById(R.id.device_start_vol);

        mDeviceAzimotor = (TextView) view
                .findViewById(R.id.device_start_azimotor);
        mDevicElevmotor = (TextView) view
                .findViewById(R.id.device_start_elevmotor);
        mDeviceSendzero = (TextView) view
                .findViewById(R.id.device_start_sendzero);
        mDeviceElevzero = (TextView) view
                .findViewById(R.id.device_start_elevzero);
        mDeviceGyroscope = (TextView) view
                .findViewById(R.id.device_start_gyroscope);
        mDeviceRf = (TextView) view.findViewById(R.id.device_start_rf);
        mDeviceOverCurrent = (TextView) view.findViewById(R.id.device_over_current);
        mDeviceStartDirZero = (TextView) view.findViewById(R.id.device_start_dirzero);
        mDeviceStartHengZero = (TextView) view.findViewById(R.id.device_start_hengzero);
        mDeviceLnbFreq = (TextView) view.findViewById(R.id.device_lnb_locfreq);
        mDeviceBucFreq = (TextView) view.findViewById(R.id.device_buc_locfreq);
        mDeviceOudver = (TextView) view.findViewById(R.id.device_start_oudver);
        mDeviceOdenum = (TextView) view.findViewById(R.id.device_start_odenum);
        mTianxianStyle=(TextView) view.findViewById(R.id.device_star_type);
        ll_start_gyroscope=(LinearLayout) view.findViewById(R.id.ll_start_gyroscope);
        ll_star_compass=(LinearLayout) view.findViewById(R.id.ll_star_compass);
        device_star_compass=(TextView) view.findViewById(R.id.device_star_compass);
        ll_start_elevcarrier=(LinearLayout)view.findViewById(R.id.ll_start_elevcarrier);
        ll_start_hengzero=(LinearLayout)view.findViewById(R.id.ll_start_hengzero);
        ll_start_dirzero=(LinearLayout)view.findViewById(R.id.ll_start_dirzero);
        ll_start_roll=(LinearLayout)view.findViewById(R.id.ll_start_roll);
        buc_swith_status=(TextView) view.findViewById(R.id.buc_swith_status);
        buc_adjust_range=(TextView) view.findViewById(R.id.buc_adjust_range);
        initToasts();
    }
    @SuppressLint("ShowToast")
    private void initToasts() {
        Toast toast=Toast.makeText(getActivity().getBaseContext(),"无法获取天线类型信息，请检查ODU模块是否正常！", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toaHashMap.put("无法获取天线类型信息，请检查ODU模块是否正常！", toast);
    }

    private String aziStr, elevcarrStr, rollStr, curlonStr, currlatStr,
            elevStr, tempStr, rssiStr, swithStatusStr,adjustRangeStr,ctrlverStr, ctrlnumStr, oduverStr,
            odunumStr, locatypeStr, hotStr, volStr, azimotorStr, elevmotorStr,
            sendzeroStr, elevzeroStr, rfStr, positionStr,mOduType,mSensor,overCurrentStr,dirZeroStr,gpsStr,hengZeroStr,lnbStr,overRangeStr,bucStr;

    public void loadData(JSONObject response) {
        // if (XTHttpJSON.getJSONString(response.toString()).equals("0")) {
        try {
            aziStr = response.getString("azi");
            elevcarrStr = response.getString("elevcarr");
            rollStr = response.getString("roll");
            curlonStr = response.getString("curlon");
            currlatStr = response.getString("currlat");
            elevStr = response.getString("elev");
            tempStr = response.getString("temp");
            rssiStr = response.getString("rssi");
            ctrlverStr = response.getString("ctrlver");
            ctrlnumStr = response.getString("ctrlnum");
            oduverStr = response.getString("oduver");
            odunumStr = response.getString("odunum");
            locatypeStr = response.getString("locatype");
            hotStr = response.getString("hot");
            volStr = response.getString("vol");
            azimotorStr = response.getString("azimotor");
            elevmotorStr = response.getString("elevmotor");
            sendzeroStr = response.getString("sendzero");
            elevzeroStr = response.getString("elevzero");
            rfStr = response.getString("rf");
            positionStr = response.getString("position");
            mOduType = response.getString("odutype");
            mSensor = response.getString("sensor");
            overCurrentStr= response.getString("current");
            dirZeroStr= response.getString("zaizero");
            hengZeroStr= response.getString("rollzero");
            lnbStr= response.getString("lnboscillator");
            overRangeStr= response.getString("elevout");
            bucStr= response.getString("bucoscillator");
            gpsStr= response.getString("gpsStatus");
            if (gpsStr.equals("0")) {
                mGpsStatus.setText("正常");
            } else if (gpsStr.equals("-1")) {
                mGpsStatus.setText("故障");
            }
            mDeviceAzi.setText(aziStr);// 方位角
            mDeviceCurrent.setText(curlonStr);// 经度
            mDeviceCurrentlat.setText(currlatStr);// 纬度
            mDeviceRssi.setText(rssiStr);// RSSI数值
            buc_adjust_range.setText(adjustRangeStr);
            if (overRangeStr.equals("0")) {
                mDevicElev.setText(elevStr);// 天线仰角
            } else if (overRangeStr.equals("-1")) {
                mDevicElev.setText("超范围");
            }
            mDeviceTemp.setText(tempStr);// 天线温度
            // 定位方式
            if (locatypeStr.equals("0")) {
                mDeviceLocatype.setText("手动");
            } else if (locatypeStr.equals("1")) {
                mDeviceLocatype.setText("GPS");
            } else if (locatypeStr.equals("2")) {
                mDeviceLocatype.setText("北斗");
            }

            if (hotStr.equals("0")) {
                mDeviceHot.setText("正常");// 过温
            } else if (hotStr.equals("-1")) {
                mDeviceHot.setText("故障");// 故障
            }

            // 电压故障
            if (volStr.equals("0")) {
                mDeviceVol.setText("正常");
            } else if (volStr.equals("-1")) {
                mDeviceVol.setText("故障");
            }

            // 方位点击故障
            if (azimotorStr.equals("0")) {
                mDeviceAzimotor.setText("正常");
            } else if (azimotorStr.equals("-1")) {
                mDeviceAzimotor.setText("故障");
            }

            // 俯仰电机故障
            if (elevmotorStr.equals("0")) {
                mDevicElevmotor.setText("正常");
            } else if (elevmotorStr.equals("-1")) {
                mDevicElevmotor.setText("故障");
            }

            // 发射归零故障
            if (sendzeroStr.equals("0")) {
                mDeviceSendzero.setText("正常");
            } else if (sendzeroStr.equals("-1")) {
                mDeviceSendzero.setText("故障");
            }
            // 俯仰归零故障
            if (elevzeroStr.equals("0")) {
                mDeviceElevzero.setText("正常");
            } else if (elevzeroStr.equals("-1")) {
                mDeviceElevzero.setText("故障");
            }

            // 射频信号故障
            if (rfStr.equals("0")) {
                mDeviceRf.setText("正常");
            } else if (rfStr.equals("-1")) {
                mDeviceRf.setText("故障");
            }
            // 天线类型
            if("0".equals(mOduType)){
                mTianxianStyle.setText("--");
                if (toaHashMap.get("无法获取天线类型信息，请检查ODU模块是否正常！")!=null){
                    toaHashMap.get("无法获取天线类型信息，请检查ODU模块是否正常！").show();
                }
                ll_start_gyroscope.setVisibility(View.VISIBLE);
                ll_star_compass.setVisibility(View.GONE);
                ll_start_elevcarrier.setVisibility(View.GONE);
            }else if("1".equals(mOduType)){
                ll_start_gyroscope.setVisibility(View.VISIBLE);
                ll_star_compass.setVisibility(View.GONE);
                mTianxianStyle.setText("V4");
                ll_start_elevcarrier.setVisibility(View.VISIBLE);
                mDevicElevcar.setText(elevcarrStr);
            }else if("2".equals(mOduType)){
                ll_start_gyroscope.setVisibility(View.VISIBLE);
                ll_star_compass.setVisibility(View.GONE);
                mTianxianStyle.setText("V6");
                ll_start_elevcarrier.setVisibility(View.VISIBLE);
                mDevicElevcar.setText(elevcarrStr);
            }else if("3".equals(mOduType)){
                ll_start_gyroscope.setVisibility(View.VISIBLE);
                ll_star_compass.setVisibility(View.GONE);
                mTianxianStyle.setText("S6");
                ll_start_elevcarrier.setVisibility(View.VISIBLE);
                mDevicElevcar.setText(elevcarrStr);
            }else if("4".equals(mOduType)){
                mTianxianStyle.setText("S6A");//三轴
                ll_start_gyroscope.setVisibility(View.VISIBLE);
                ll_star_compass.setVisibility(View.GONE);
                ll_start_elevcarrier.setVisibility(View.VISIBLE);
                mDevicElevcar.setText(elevcarrStr);
            }else if("5".equals(mOduType)){
                mTianxianStyle.setText("S8");
                ll_start_gyroscope.setVisibility(View.VISIBLE);
                ll_star_compass.setVisibility(View.GONE);
                ll_start_elevcarrier.setVisibility(View.VISIBLE);
                mDevicElevcar.setText(elevcarrStr);
            }else if("6".equals(mOduType)){
                mTianxianStyle.setText("S9");
                ll_start_gyroscope.setVisibility(View.VISIBLE);
                ll_star_compass.setVisibility(View.GONE);
                ll_start_elevcarrier.setVisibility(View.VISIBLE);
                mDevicElevcar.setText(elevcarrStr);
            }else if("7".equals(mOduType)){
                mTianxianStyle.setText("C6");
                ll_start_gyroscope.setVisibility(View.GONE);
                ll_star_compass.setVisibility(View.VISIBLE);
                ll_start_elevcarrier.setVisibility(View.GONE);
                ll_start_dirzero.setVisibility(View.VISIBLE);
            }else if("8".equals(mOduType)){
                mTianxianStyle.setText("C9");
                ll_start_gyroscope.setVisibility(View.GONE);
                ll_star_compass.setVisibility(View.VISIBLE);
                ll_start_elevcarrier.setVisibility(View.GONE);
            }
            // 陀螺仪故障或电子罗盘故障
            if (mSensor.equals("0")) {
                device_star_compass.setText("正常");
                mDeviceGyroscope.setText("正常");
            } else if (mSensor.equals("-1")) {
                device_star_compass.setText("故障");
                mDeviceGyroscope.setText("故障");
            }
            if (overCurrentStr.equals("0")) {
                mDeviceOverCurrent.setText("正常");
            } else if (overCurrentStr.equals("-1")) {
                mDeviceOverCurrent.setText("故障");
            }
            if("4".equals(mOduType)||("6".equals(mOduType))) {
                ll_start_roll.setVisibility(View.VISIBLE);
                mDeviceRoll.setText(rollStr);// 横滚角
            }else{
                ll_start_roll.setVisibility(View.GONE);
            }
            if("7".equals(mOduType)||("8".equals(mOduType))) {
                ll_start_dirzero.setVisibility(View.VISIBLE);
                if (dirZeroStr.equals("0")) {
                    mDeviceStartDirZero.setText("正常");
                } else if (dirZeroStr.equals("-1")) {
                    mDeviceStartDirZero.setText("故障");
                }
            }else{
                ll_start_dirzero.setVisibility(View.GONE);
            }
            if("4".equals(mOduType)||("6".equals(mOduType))) {
                ll_start_hengzero.setVisibility(View.VISIBLE);
                if (hengZeroStr.equals("0")) {
                    mDeviceStartHengZero.setText("正常");
                } else if (hengZeroStr.equals("-1")) {
                    mDeviceStartHengZero.setText("故障");
                }
            }else{
                ll_start_hengzero.setVisibility(View.GONE);
            }
            mDeviceLnbFreq.setText(lnbStr);
            mDeviceBucFreq.setText(bucStr);
            mDeviceOudver.setText(oduverStr);
            mDeviceOdenum.setText(odunumStr);
            swithStatusStr= response.getString("bucSwitch");
            if (swithStatusStr.equals("1")||swithStatusStr.equals("3")) {
                buc_swith_status.setText("开启");
            } else if (swithStatusStr.equals("0")||swithStatusStr.equals("2")) {
                buc_swith_status.setText("关闭");
            }else if (swithStatusStr.equals("4")){
                buc_swith_status.setText("异常");
            }
            if(ChechIpMask.a2b(response.getString("bucGain"), 0, 300)){
                String _superRange=String.valueOf(ChechIpMask.a2b(response.getString("bucGain"), 0, 300));
                String superRange=String.valueOf(new BigDecimal(_superRange).divide(new BigDecimal(10)));
                adjustRangeStr=ChechIpMask.numDigite(superRange, 1);
                buc_adjust_range.setText(adjustRangeStr);

            }else if(!ChechIpMask.a2b(response.getString("bucGain"), 0, 300)){
                buc_adjust_range.setText("--");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            Iterator<Entry<String, Toast>> iter = toaHashMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Toast> entry = iter.next();
                Toast toast=entry.getValue();
                Field field = toast.getClass().getDeclaredField("mTN");
                field.setAccessible(true);
                Object obj = field.get(toast);
                java.lang.reflect.Method m=obj.getClass().getDeclaredMethod("hide");
                m.invoke(obj);
                iter.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
