package com.my51c.see51.app.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.my51c.see51.adapter.SmartGatewayExpandableAdapter;
import com.my51c.see51.app.bean.FirewallInfo;
import com.my51c.see51.app.bean.HardDiskStatusInfo;
import com.my51c.see51.app.bean.MemoryInfo;
import com.my51c.see51.app.bean.SmartGatewayBean;
import com.my51c.see51.app.bean.SmartGatewayLevel0Item;
import com.my51c.see51.app.bean.StaticRouterTable;
import com.my51c.see51.app.bean.SystemLogInfo;
import com.my51c.see51.app.bean.VersionInfo;
import com.my51c.see51.app.bean.VersionInfoList;
import com.my51c.see51.app.http.XTHttpUtil;
import com.my51c.see51.app.utils.XTHttpJSON;
import com.my51c.see51.common.AppData;
import com.synertone.commonutil.util.GsonUtils;
import com.synertone.netAssistant.R;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SmartGatewayStatusFragment extends BaseFragment {
    @BindView(R.id.rlv_content)
    RecyclerView rlvContent;
    private SmartGatewayExpandableAdapter smartGatewayExpandableAdapter;
    private List<MultiItemEntity> dataList;
    private SmartGatewayLevel0Item versionTitle;
    private SmartGatewayLevel0Item memoryStatus;
    private SmartGatewayLevel0Item hardDiskStatus;
    private SmartGatewayLevel0Item firewallStatus;
    private SmartGatewayLevel0Item systemLog;
    private SmartGatewayLevel0Item staticRouteTable;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_smart_gateway_status_layout;
    }

    @Override
    protected void initEventAndData() {
        initRecycleView();
        initEvent();
        doStorageInfo();
        doStaticRouterInfo();
        doFirewallInfo();
        doSystemLogInfo();
    }

    private void initEvent() {
     /*   smartGatewayExpandableAdapter.setOnexpandedListener(new SmartGatewayExpandableAdapter.OnexpandedListener() {
            @Override
            public void expanded(String name) {
                if ("防火墙信息".equals(name) || "系统日志".equals(name)) {
                    rlvContent.scrollToPosition(smartGatewayExpandableAdapter.getItemCount() - 1);
                }
            }
        });*/
    }

    private void initRecycleView() {
        dataList = new ArrayList<>();
        versionTitle = new SmartGatewayLevel0Item("版本信息");
        memoryStatus = new SmartGatewayLevel0Item("内存状态");
        hardDiskStatus = new SmartGatewayLevel0Item("硬盘状态");
        staticRouteTable = new SmartGatewayLevel0Item("静态路由表");
        firewallStatus = new SmartGatewayLevel0Item("防火墙信息");
        systemLog = new SmartGatewayLevel0Item("系统日志");
        dataList.add(versionTitle);
        dataList.add(memoryStatus);
        dataList.add(hardDiskStatus);
        dataList.add(staticRouteTable);
        dataList.add(firewallStatus);
        dataList.add(systemLog);
        smartGatewayExpandableAdapter = new SmartGatewayExpandableAdapter(dataList);
        rlvContent.setAdapter(smartGatewayExpandableAdapter);
        rlvContent.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void doStorageInfo() {
        //baseActivity.showDia();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                XTHttpUtil.GET_DEVSTATU_CORESTATUS_ONE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        baseActivity.dismissDia();
                        if(!isVisible){
                            return;
                        }
                       // Toast.makeText(mContext,response.toString(),Toast.LENGTH_LONG).show();
                        SmartGatewayBean smartGatewayBean = GsonUtils.fromJson(response.toString(), SmartGatewayBean.class);
                        if (smartGatewayBean == null || !"0".equals(smartGatewayBean.getCode())) {
                            Toast.makeText(mContext, "内存硬盘信息获取失败!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        List<SmartGatewayBean.DiskpartBean> diskpartBeanList = new ArrayList<>();
                        try {
                            String disknum = smartGatewayBean.getDisknum();
                            if (!StringUtils.isEmpty(disknum)) {
                                int num = Integer.parseInt(disknum);
                                for (int i = 1; i <= num; i++) {
                                    JSONObject jsonObject = response.getJSONObject("diskpart" + i);
                                    SmartGatewayBean.DiskpartBean diskpartBean = GsonUtils.fromJson(jsonObject.toString(), SmartGatewayBean.DiskpartBean.class);
                                    diskpartBeanList.add(diskpartBean);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        initStorageNetData(smartGatewayBean, diskpartBeanList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "内存硬盘信息获取失败!", Toast.LENGTH_SHORT).show();
                }
                baseActivity.dismissDia();
            }
        });
        AppData.mRequestQueue.add(request);
    }

    private void initStorageNetData(SmartGatewayBean bean, List<SmartGatewayBean.DiskpartBean> diskpartBeanList) {
        String ver = bean.getVer();
        List<VersionInfo> infoList = new ArrayList<>();
        VersionInfo versionInfo = new VersionInfo("版本信息", ver);
        infoList.add(versionInfo);
        VersionInfoList versionInfoList = new VersionInfoList(infoList);
        versionTitle.addSubItem(versionInfoList);
       // dataList.add(versionTitle);
        SmartGatewayBean.MemBean mem = bean.getMem();
        SmartGatewayBean.BufferBean buffer = bean.getBuffer();
        SmartGatewayBean.SwapBean swap = bean.getSwap();
        MemoryInfo memoryInfo = new MemoryInfo();
        if(mem!=null){
            memoryInfo.men_total=mem.getTotal();
            memoryInfo.men_buffer=mem.getBuffers();
            memoryInfo.men_free=mem.getFree();
            memoryInfo.men_shared=mem.getShared();
            memoryInfo.men_used=mem.getUsed();
        }
        if(buffer!=null){
            memoryInfo.buffer_total=buffer.getTotal();
            memoryInfo.buffer_buffers=buffer.getBuffers();
            memoryInfo.buffer_free=buffer.getFree();
            memoryInfo.buffer_shared=buffer.getShared();
            memoryInfo.buffer_used=buffer.getUsed();
        }
        if(swap!=null){
            memoryInfo.swap_total=swap.getTotal();
            memoryInfo.swap_buffer=swap.getBuffers();
            memoryInfo.swap_free=swap.getFree();
            memoryInfo.swap_shared=swap.getShared();
            memoryInfo.swap_used=swap.getUsed();
        }

        memoryStatus.addSubItem(memoryInfo);
       // dataList.add(memoryStatus);

        HardDiskStatusInfo hardDiskStatusInfo = new HardDiskStatusInfo(getDiskStatus(bean.getDiskstatus()), StringUtils.isEmpty(bean.getDisknum())?"0":bean.getDisknum(), diskpartBeanList);
        hardDiskStatus.addSubItem(hardDiskStatusInfo);
       // dataList.add(hardDiskStatus);
        smartGatewayExpandableAdapter.notifyDataSetChanged();
    }

    /**
     * 获取静态路由表信息
     */
    private void doStaticRouterInfo() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                XTHttpUtil.GET_DEVSTATU_CORESTATUS_ROUTLIST, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(!isVisible){
                            return;
                        }
                        initRouterNetData(response);// 加载数据
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppData.mRequestQueue.add(request);
    }

    private void initRouterNetData(JSONObject response) {
        if (XTHttpJSON.getJSONString(response.toString()).equals("0")) {
            StringBuffer stringBuffer = new StringBuffer("");
            try {
                int firNum = response.getInt("num");
                for (int i = 1; i <= firNum; i++) {
                    String firMsg = response.getString("msg" + i);
                    stringBuffer.append(firMsg+"\n");
                }
                staticRouteTable.addSubItem(new StaticRouterTable(stringBuffer.toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (XTHttpJSON.getJSONString(response.toString()).equals("-1")) {
            Toast.makeText(mContext, "静态路由信息获取失败！", Toast.LENGTH_SHORT).show();
        }
        smartGatewayExpandableAdapter.notifyDataSetChanged();
    }

    /**
     * 获取网络防火墙信息
     */
    private void doFirewallInfo() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                XTHttpUtil.GET_DEVSTATU_CORESTATUS_FIRE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(!isVisible){
                            return;
                        }
                        initFirewallNetData(response);// 加载数据
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "网络防火墙信息获取失败！", Toast.LENGTH_SHORT).show();
            }
        });

        AppData.mRequestQueue.add(request);
    }

    private void initFirewallNetData(JSONObject response) {
        if (XTHttpJSON.getJSONString(response.toString()).equals("0")) {
            try {
                StringBuffer stringBuffer = new StringBuffer("");
                int firNum = response.getInt("num");
                for (int i = 1; i <= firNum; i++) {
                    String firMsg1 = response.getString("msg" + i);
                    stringBuffer.append(firMsg1+"\n");
                }
                FirewallInfo firewallInfo = new FirewallInfo(stringBuffer.toString());
                firewallStatus.addSubItem(firewallInfo);
                smartGatewayExpandableAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (XTHttpJSON.getJSONString(response.toString()).equals("-1")) {
            Toast.makeText(mContext, "网络防火墙信息获取失败！", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 获取系统日志信息
     */
    private void doSystemLogInfo() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                XTHttpUtil.GET_DEVSTATU_CORESTATUS_SYSDAILY, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(!isVisible){
                            return;
                        }
                        initSystemLogNetData(response);// 加载数据
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "系统日志获取失败！", Toast.LENGTH_SHORT).show();
            }
        });
        AppData.mRequestQueue.add(request);

    }

    private void initSystemLogNetData(JSONObject response) {
        if (XTHttpJSON.getJSONString(response.toString()).equals("0")) {
            try {
                int firNum = response.getInt("num");
                StringBuffer stringBuffer = new StringBuffer("");
                for (int i = 1; i <= firNum; i++) {
                    String firMsg1 = response.getString("msg" + i);
                    stringBuffer.append(firMsg1+"\n");
                }
                SystemLogInfo systemLogInfo = new SystemLogInfo(stringBuffer.toString());
                systemLog.addSubItem(systemLogInfo);
                smartGatewayExpandableAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (XTHttpJSON.getJSONString(response.toString()).equals("-1")) {
            Toast.makeText(mContext, "系统日志获取失败！", Toast.LENGTH_SHORT).show();
        }
    }

    private String getDiskStatus(String diskstatus) {
        if(diskstatus==null){
            return "未知";
        }
        switch (diskstatus) {
            case "0":
                return "正常";
            case "1":
                return "故障";
            default:
                break;
        }
        return "未知";
    }
}
