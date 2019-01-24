package com.my51c.see51.app.bean;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.my51c.see51.adapter.ExpandableItemAdapter;

/**
 * Created by luoxw on 2016/8/10.
 */
public class Level0Item extends AbstractExpandableItem<TroubleBean> implements MultiItemEntity {
    public String troubleNum;

    public Level0Item(String troubleNum) {
        this.troubleNum=troubleNum;
    }

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_LEVEL_0;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
