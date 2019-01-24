package com.my51c.see51.app.fragment;

import android.content.Context;
import android.view.View;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.my51c.see51.adapter.CommonAdapter;
import com.my51c.see51.adapter.CommonViewHolder;
import com.my51c.see51.app.bean.WarnBean;
import com.my51c.see51.app.bean.WarnInfoBean;
import com.my51c.see51.app.http.XTHttpUtil;
import com.my51c.see51.app.utils.TimeUtils;
import com.my51c.see51.common.AppData;
import com.my51c.see51.widget.PinnedSectionListView;
import com.synertone.commonutil.util.GsonUtils;
import com.synertone.netAssistant.R;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class AddServiceWarnInfoFragment extends BaseFragment{
    @BindView(R.id.pslv_content)
    PinnedSectionListView pslvContent;
    @BindView(R.id.emptyview)
    View emptyView;
    private HashMap<String, String> arrayMap;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_warn_info_layout;
    }

    @Override
    protected void initEventAndData() {
        initAlarmData();
        doAntennaAlarm();
    }
    private void initAlarmData() {
        arrayMap = new HashMap<>();
        arrayMap.put("100000", "路由板重启检测");
        arrayMap.put("100001", "手机板（4G模块）接入检测");
        arrayMap.put("100002", "4G通信链路检测");
        arrayMap.put("100003", "卫星调制解调器接入检测");
        arrayMap.put("100004", "卫星通信链路检测");
        arrayMap.put("100005", "路由板与ODU通信链路检测");
        arrayMap.put("100006", "WIFI模块异常检测");
        arrayMap.put("100007", "WIFI链路检测");
        arrayMap.put("100008", "内存存储满检测");
        arrayMap.put("100009", "内存存储满检测");
        arrayMap.put("100010", "SATA硬盘存储满检测");

        arrayMap.put("110000", "一般错误");
        arrayMap.put("110001", "过流");
        arrayMap.put("110002", "过温");
        arrayMap.put("110003", "低电压");
        arrayMap.put("110004", "卫星未找到");
        arrayMap.put("110005", "方位电机过载");
        arrayMap.put("110006", "发射归零故障");
        arrayMap.put("110007", "俯仰电机过载");
        arrayMap.put("110008", "射频信号故障");
        arrayMap.put("110009", "横滚归零错误");
        arrayMap.put("110010", "方位归零故障");
        arrayMap.put("110011", "GPS异常告警");
        arrayMap.put("110012", "俯仰归零故障");
        arrayMap.put("110013", "电子罗盘/陀螺仪故障");

        arrayMap.put("120000", "增值业务摄像头未找到");
    }

    private void doAntennaAlarm() {

        try {
            //baseActivity.showDia();
            RequestParams params = new RequestParams("UTF-8");
            JSONObject jsonObjet = new JSONObject();
            jsonObjet.put("sessionToken",
                    AppData.accountModel.getSessionToken());
            jsonObjet.put("dev_type", "12");
            String currnetTime = TimeUtils.getCurrentTime();
            jsonObjet.put("startTime", TimeUtils.addDay(currnetTime, -7));
            jsonObjet.put("endTime", currnetTime);
            params.setBodyEntity(new StringEntity(jsonObjet.toString(), "UTF-8"));
            params.setContentType("applicatin/json");
            AppData.http.send(HttpRequest.HttpMethod.POST, XTHttpUtil.devstatuAlarm, params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                           // Toast.makeText(mContext, responseInfo.result.toString(), Toast.LENGTH_LONG).show();
                            baseActivity.dismissDia();
                            if(!isVisible){
                                return;
                            }
                            WarnInfoBean warnInfoBean = GsonUtils.fromJson(
                                    responseInfo.result, WarnInfoBean.class);
                            if (warnInfoBean != null) {
                                String code = warnInfoBean.getCode();
                                String msg = warnInfoBean.getMsg();
                                if ("0".equals(code)) {
                                    List<WarnInfoBean.AlarmListBean> alarmList = warnInfoBean
                                            .getAlarmList();
                                    if (alarmList != null&&alarmList.size()>0) {
                                        for (WarnInfoBean.AlarmListBean alarmListBean : alarmList) {
                                            if (alarmListBean.getTrigger() == 1) {
                                                alarmListBean
                                                        .setAlarmType(" （解除）");
                                            } else {
                                                alarmListBean
                                                        .setAlarmType(" （触发）");
                                            }
                                            String alarmCode = alarmListBean
                                                    .getAlarmCode();
                                            alarmListBean.setAlarmContent(arrayMap
                                                    .get(alarmCode)
                                                    + alarmListBean
                                                    .getAlarmType());
                                        }
                                        initWarnAdapter(alarmList);
                                    }else{
                                        visableEmptyView();
                                    }
                                } else if ("-2".equals(code)) {
                                    baseActivity.showLoginDialog();
                                }else{
                                    visableEmptyView();
                                }
                            }
                        }

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            baseActivity.dismissDia();
                            visableEmptyView();

                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void visableEmptyView() {
        if(emptyView==null){
            return;
        }
        emptyView.setVisibility(View.VISIBLE);
    }

    private void initWarnAdapter(List<WarnInfoBean.AlarmListBean> alarmList) {
        String lastTime = "";
        int sectionPosition = 0, listPosition = 0;
        List<WarnInfoBean.AlarmListBean> dataSource = new ArrayList<>();
        for (WarnInfoBean.AlarmListBean alarmListBean : alarmList) {
            String alarmTime = alarmListBean.getAlarmTime();// 2016-07-05
            // 14:30:12
            if (TimeUtils.isSameDate(alarmTime, lastTime)) {
                alarmListBean.setType(WarnInfoBean.AlarmListBean.ITEM);
                alarmListBean.sectionPosition = sectionPosition;
                alarmListBean.listPosition = listPosition++;
            } else {
                alarmListBean.setType(WarnInfoBean.AlarmListBean.SECTION);
                alarmListBean.sectionPosition = sectionPosition;
                alarmListBean.listPosition = listPosition++;
                sectionPosition++;
            }
            dataSource.add(alarmListBean);
            if (alarmListBean.getType() == WarnInfoBean.AlarmListBean.SECTION) {
                WarnInfoBean.AlarmListBean bean = new WarnInfoBean.AlarmListBean();
                bean.setType(WarnInfoBean.AlarmListBean.ITEM);
                bean.sectionPosition = sectionPosition;
                bean.listPosition = listPosition++;
                bean.setAlarmCode(alarmListBean.getAlarmCode());
                bean.setAlarmContent(alarmListBean.getAlarmContent());
                bean.setAlarmTime(alarmListBean.getAlarmTime());
                bean.setTrigger(alarmListBean.getTrigger());
                dataSource.add(bean);
            }
            lastTime = alarmTime;
        }
        SimpleAdapter adapter = new SimpleAdapter(mContext,
                R.layout.item_warn_info_layout, dataSource) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder,
                                        int position, WarnInfoBean.AlarmListBean item) {
                if (item.type == WarnBean.SECTION) {
                    viewHolder.setTextForTextView(R.id.tv_section, item
                            .getAlarmTime().split(" ")[0]);
                    viewHolder.setBackgroundForView(R.id.ll_content,
                            getResources().getColor(R.color.green_light));
                    viewHolder.setVisibility(R.id.tv_section, View.VISIBLE);
                } else if (item.type == WarnBean.ITEM) {
                    viewHolder.setTextForTextView(R.id.tv_content,
                            item.getAlarmContent());
                    viewHolder.setTextForTextView(R.id.tv_time,
                            item.getAlarmTime());
                    viewHolder.setVisibility(R.id.tv_section, View.GONE);
                }

            }

        };
        pslvContent.setAdapter(adapter);
    }
    abstract class SimpleAdapter extends CommonAdapter<WarnInfoBean.AlarmListBean> implements
            PinnedSectionListView.PinnedSectionListAdapter {

        public SimpleAdapter(Context context, int itemLayoutResId,
                             List<WarnInfoBean.AlarmListBean> dataSource) {
            super(context, itemLayoutResId, dataSource);

        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).type;
        }

        @Override
        public boolean isItemViewTypePinned(int viewType) {
            return viewType == WarnBean.SECTION;
        }

    }
}
