package com.my51c.see51.app.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.my51c.see51.adapter.SmartGatewayExpandableAdapter;

public class MemoryInfo implements MultiItemEntity{
    public String men_total;
    public String men_used;
    public String men_free;
    public String men_shared;
    public String men_buffer;
    public String buffer_total;
    public String buffer_used;
    public String buffer_free;
    public String buffer_shared;
    public String buffer_buffers;
    public String swap_total;
    public String swap_used;
    public String swap_free;
    public String swap_shared;
    public String swap_buffer;

    public MemoryInfo() {
    }

    public MemoryInfo(String men_total, String men_used, String men_free, String men_shared, String men_buffer, String buffer_total, String buffer_used, String buffer_free, String buffer_shared, String buffer_buffers, String swap_total, String swap_used, String swap_free, String swap_shared, String swap_buffer) {
        this.men_total = men_total;
        this.men_used = men_used;
        this.men_free = men_free;
        this.men_shared = men_shared;
        this.men_buffer = men_buffer;
        this.buffer_total = buffer_total;
        this.buffer_used = buffer_used;
        this.buffer_free = buffer_free;
        this.buffer_shared = buffer_shared;
        this.buffer_buffers = buffer_buffers;
        this.swap_total = swap_total;
        this.swap_used = swap_used;
        this.swap_free = swap_free;
        this.swap_shared = swap_shared;
        this.swap_buffer = swap_buffer;
    }

    @Override
    public int getItemType() {
        return SmartGatewayExpandableAdapter.MEMORY_INFO;
    }
}
