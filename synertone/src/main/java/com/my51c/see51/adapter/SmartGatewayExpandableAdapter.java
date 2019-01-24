package com.my51c.see51.adapter;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.style.LineStyle;
import com.bin.david.form.data.table.TableData;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.my51c.see51.app.annotation.FieldTitle;
import com.my51c.see51.app.bean.FirewallInfo;
import com.my51c.see51.app.bean.HardDiskStatusInfo;
import com.my51c.see51.app.bean.MemoryInfo;
import com.my51c.see51.app.bean.SmartGatewayBean;
import com.my51c.see51.app.bean.SmartGatewayLevel0Item;
import com.my51c.see51.app.bean.StaticRouterTable;
import com.my51c.see51.app.bean.SystemLogInfo;
import com.my51c.see51.app.bean.VersionInfo;
import com.my51c.see51.app.bean.VersionInfoList;
import com.synertone.netAssistant.R;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by luoxw on 2016/8/9.
 */
public class SmartGatewayExpandableAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private static final String TAG = SmartGatewayExpandableAdapter.class.getSimpleName();

    public static final int TYPE_LEVEL_0 = 0;
    public static final int VERSION_INFO = 1;
    public static final int MEMORY_INFO = 2;
    public static final int HARD_DISK_INFO = 3;
    public static final  int STATIC_ROUTER_INFO=4;
    public static final  int FIRE_WALL_INFO=5;
    public static final  int SYSTEM_LOG_INFO=6;
    private OnexpandedListener onexpandedListener;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public SmartGatewayExpandableAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_smart_gateway_lv0);
        addItemType(VERSION_INFO, R.layout.item_version_layout);
        addItemType(MEMORY_INFO, R.layout.item_memory_status_layout);
        addItemType(HARD_DISK_INFO,R.layout.item_hard_disk_status_layout);
        addItemType(STATIC_ROUTER_INFO,R.layout.item_static_router_layout);
        addItemType(FIRE_WALL_INFO,R.layout.item_fire_wall_layout);
        addItemType(SYSTEM_LOG_INFO,R.layout.item_system_log_layout);
    }


    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case TYPE_LEVEL_0:
                final SmartGatewayLevel0Item lv0 = (SmartGatewayLevel0Item) item;
                holder.setText(R.id.tv_content, lv0.name)
                        .setImageResource(R.id.iv_arrow, lv0.isExpanded() ? R.drawable.iv_arrow_right : R.drawable.iv_arrow_down);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        Log.d(TAG, "Level 0 item pos: " + pos);
                        if (lv0.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                            if (onexpandedListener != null) {
                                onexpandedListener.expanded(lv0.name);
                            }
                        }

                    }
                });
                break;
            case VERSION_INFO:
                final VersionInfoList versionInfoList = (VersionInfoList) item;
                List<VersionInfo> infoList = versionInfoList.infoList;
                ViewGroup viewGroup = holder.getView(R.id.ll_content);
                viewGroup.removeAllViews();
                for (VersionInfo versionInfo : infoList) {
                    View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_version_info_layout, null);
                    TextView tv_version_name = (TextView) inflate.findViewById(R.id.tv_version_name);
                    TextView tv_version_code = (TextView) inflate.findViewById(R.id.tv_version_code);
                    tv_version_name.setText(versionInfo.versionName);
                    tv_version_code.setText(versionInfo.versionCode);
                    viewGroup.addView(inflate);
                }
                break;
            case MEMORY_INFO:
                final MemoryInfo memoryInfo = (MemoryInfo) item;
                holder  .setText(R.id.men_total, memoryInfo.men_total)
                        .setText(R.id.men_used, memoryInfo.men_used)
                        .setText(R.id.men_free, memoryInfo.men_free)
                        .setText(R.id.men_buffer, memoryInfo.men_buffer)
                        .setText(R.id.men_shared, memoryInfo.men_shared)
                        .setText(R.id.buffer_total, memoryInfo.buffer_total)
                        .setText(R.id.buffer_used, memoryInfo.buffer_used)
                        .setText(R.id.buffer_free, memoryInfo.buffer_free)
                        .setText(R.id.buffer_buffers, memoryInfo.buffer_buffers)
                        .setText(R.id.buffer_shared, memoryInfo.buffer_shared)
                        .setText(R.id.swap_total, memoryInfo.swap_total)
                        .setText(R.id.swap_used, memoryInfo.swap_used)
                        .setText(R.id.swap_free, memoryInfo.swap_free)
                        .setText(R.id.swap_buffer, memoryInfo.swap_buffer)
                        .setText(R.id.swap_shared, memoryInfo.swap_shared);
                break;
            case HARD_DISK_INFO:
                final HardDiskStatusInfo hardDiskStatusInfo= (HardDiskStatusInfo) item;
                holder.setText(R.id.device_corestatus_work,hardDiskStatusInfo.deviceCorestatusWork)
                        .setText(R.id.device_corestatus_disknum,hardDiskStatusInfo.deviceCorestatusDisknum)
                        ;
                SmartTable diskTable=holder.getView(R.id.st_disk);
                List<SmartGatewayBean.DiskpartBean> diskpartBeanList = hardDiskStatusInfo.diskpartBeanList;
                if(diskpartBeanList!=null&&diskpartBeanList.size()>0){
                    diskTable.setVisibility(View.VISIBLE);
                    Class<SmartGatewayBean.DiskpartBean> diskpartBeanClass = SmartGatewayBean.DiskpartBean.class;
                    Field[] diskpartFields = diskpartBeanClass.getFields();
                    Column[] diskpartColumns=new Column[diskpartFields.length-2];
                    for(Field field:diskpartFields){
                        FieldTitle annotation = field.getAnnotation(FieldTitle.class);
                        if(annotation!=null){
                            int index = annotation.index();
                            diskpartColumns[index]=new Column(annotation.value(),field.getName());
                        }
                    }
                    final TableData diskpartTableData = new TableData("硬盘信息",diskpartBeanList, diskpartColumns);
                    LineStyle diskpartLineStyle = new LineStyle();
                    diskpartLineStyle.setWidth(1.5f);
                    diskpartLineStyle.setColor(ContextCompat.getColor(mContext,R.color.gray_line));
                    diskTable.getConfig().setShowTableTitle(false)
                            .setShowXSequence(false)
                            .setShowYSequence(false)
                            .setContentGridStyle(diskpartLineStyle)
                            .setSequenceGridStyle(diskpartLineStyle)
                            .setColumnTitleGridStyle(diskpartLineStyle)
                            .setVerticalPadding(30)
                            .setColumnTitleVerticalPadding(30);
                    diskTable.setTableData(diskpartTableData);
                }
                break;
            case STATIC_ROUTER_INFO:
                final StaticRouterTable table= (StaticRouterTable) item;
                holder.setText(R.id.tv_content,table.content);
               /* SmartTable smartTable=holder.getView(R.id.st_router);
                Class<StaticRouterTableInfo> staticRouterTableInfoClass = StaticRouterTableInfo.class;
                Field[] fields = staticRouterTableInfoClass.getFields();
                Column[] columns=new Column[fields.length-2];
               for(Field field:fields){
                   FieldTitle annotation = field.getAnnotation(FieldTitle.class);
                   if(annotation!=null){
                       int index = annotation.index();
                       columns[index]=new Column(annotation.value(),field.getName());
                   }
               }
                final TableData tableData = new TableData("静态路由表",tableList.list, columns);
                LineStyle lineStyle = new LineStyle();
                lineStyle.setWidth(1.5f);
                lineStyle.setColor(ContextCompat.getColor(mContext,R.color.gray_line));
                smartTable.getConfig().setShowTableTitle(false)
                        .setShowXSequence(false)
                        .setShowYSequence(false)
                        .setContentGridStyle(lineStyle)
                         .setSequenceGridStyle(lineStyle)
                          .setColumnTitleGridStyle(lineStyle)
                .setVerticalPadding(30)
                .setColumnTitleVerticalPadding(30);
                smartTable.setTableData(tableData);*/
                break;
            case FIRE_WALL_INFO:
                final FirewallInfo firewallInfo= (FirewallInfo) item;
                holder.setText(R.id.tv_content,firewallInfo.content);
                break;
            case SYSTEM_LOG_INFO:
                final SystemLogInfo systemLogInfo= (SystemLogInfo) item;
                holder.setText(R.id.tv_content,systemLogInfo.content);
                break;
            default:
                break;
        }
    }

    public interface OnexpandedListener {
        void expanded(String name);
    }

    public void setOnexpandedListener(OnexpandedListener onexpandedListener) {
        this.onexpandedListener = onexpandedListener;
    }
}
