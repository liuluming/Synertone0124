package com.my51c.see51.adapter;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.my51c.see51.app.bean.Level0Item;
import com.my51c.see51.app.bean.TroubleBean;
import com.synertone.netAssistant.R;

import java.util.List;

/**
 * Created by luoxw on 2016/8/9.
 */
public class ExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private static final String TAG = ExpandableItemAdapter.class.getSimpleName();

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_TRUOBLE = 1;
    private OnexpandedListener onexpandedListener;
    private OnCollapseListener onCollapseListener;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ExpandableItemAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0);
        addItemType(TYPE_TRUOBLE, R.layout.item_expandable_lv1);
    }


    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case TYPE_LEVEL_0:
                final Level0Item lv0 = (Level0Item) item;
                holder.setText(R.id.tv_troubleNum, lv0.troubleNum)
                .setImageResource(R.id.iv_arrow,lv0.isExpanded()?R.drawable.iv_arrow_right:R.drawable.iv_arrow_down);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        Log.d(TAG, "Level 0 item pos: " + pos);
                        if (lv0.isExpanded()) {
                            collapse(pos);
                            if(onCollapseListener!=null){
                                onCollapseListener.collapse();
                            }
                        } else {
                            expand(pos);
                            if(onexpandedListener!=null){
                                onexpandedListener.expanded();
                            }
                        }

                    }
                });
                break;
            case TYPE_TRUOBLE:
                final TroubleBean troubleItem = (TroubleBean) item;
                holder.setText(R.id.tv_name, troubleItem.getName())
                        .setText(R.id.tv_info, troubleItem.getInfo());
                break;
        }
    }
    public  interface OnexpandedListener{
        void expanded();
    }

    public void setOnexpandedListener(OnexpandedListener onexpandedListener) {
        this.onexpandedListener = onexpandedListener;
    }

    public  interface OnCollapseListener {
        void collapse();
    }

    public void setOnCollapseListener(OnCollapseListener onCollapseListener) {
        this.onCollapseListener = onCollapseListener;
    }
}
