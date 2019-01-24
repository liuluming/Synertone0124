package com.my51c.see51.app.bean;

import com.my51c.see51.app.annotation.FieldTitle;

public class SmartGatewayBean {

    /**
     * code : 0
     * msg : return_msg
     * ver : V1.0.0.1
     * mem : {"total":"508344","used":"395096","free":"113248","shared":"0","buffers":"36384","cached":"223320"}
     * buffer : {"total":"508344","used":"395096","free":"113248","shared":"0","buffers":"36384","cached":"223320"}
     * swap : {"total":"508344","used":"395096","free":"113248","shared":"0","buffers":"36384","cached":"223320"}
     * diskstatus : 0
     * disknum : 3
     * diskpart1 : {"total":"100","left":"30","filesys":"ext4"}
     * diskpart2 : {"total":"100","left":"30","filesys":"ntfs"}
     * diskpart3 : {"total":"100","left":"30","filesys":"ntfs"}
     */

    private String code;
    private String msg;
    private String ver;
    private MemBean mem;
    private BufferBean buffer;
    private SwapBean swap;
    private String diskstatus;
    private String disknum;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public MemBean getMem() {
        return mem;
    }

    public void setMem(MemBean mem) {
        this.mem = mem;
    }

    public BufferBean getBuffer() {
        return buffer;
    }

    public void setBuffer(BufferBean buffer) {
        this.buffer = buffer;
    }

    public SwapBean getSwap() {
        return swap;
    }

    public void setSwap(SwapBean swap) {
        this.swap = swap;
    }

    public String getDiskstatus() {
        return diskstatus;
    }

    public void setDiskstatus(String diskstatus) {
        this.diskstatus = diskstatus;
    }

    public String getDisknum() {
        return disknum;
    }

    public void setDisknum(String disknum) {
        this.disknum = disknum;
    }

    public static class MemBean {
        /**
         * total : 508344
         * used : 395096
         * free : 113248
         * shared : 0
         * buffers : 36384
         * cached : 223320
         */

        private String total;
        private String used;
        private String free;
        private String shared;
        private String buffers;
        private String cached;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }

        public String getFree() {
            return free;
        }

        public void setFree(String free) {
            this.free = free;
        }

        public String getShared() {
            return shared;
        }

        public void setShared(String shared) {
            this.shared = shared;
        }

        public String getBuffers() {
            return buffers;
        }

        public void setBuffers(String buffers) {
            this.buffers = buffers;
        }

        public String getCached() {
            return cached;
        }

        public void setCached(String cached) {
            this.cached = cached;
        }
    }

    public static class BufferBean {
        /**
         * total : 508344
         * used : 395096
         * free : 113248
         * shared : 0
         * buffers : 36384
         * cached : 223320
         */

        private String total;
        private String used;
        private String free;
        private String shared;
        private String buffers;
        private String cached;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }

        public String getFree() {
            return free;
        }

        public void setFree(String free) {
            this.free = free;
        }

        public String getShared() {
            return shared;
        }

        public void setShared(String shared) {
            this.shared = shared;
        }

        public String getBuffers() {
            return buffers;
        }

        public void setBuffers(String buffers) {
            this.buffers = buffers;
        }

        public String getCached() {
            return cached;
        }

        public void setCached(String cached) {
            this.cached = cached;
        }
    }

    public static class SwapBean {
        /**
         * total : 508344
         * used : 395096
         * free : 113248
         * shared : 0
         * buffers : 36384
         * cached : 223320
         */

        private String total;
        private String used;
        private String free;
        private String shared;
        private String buffers;
        private String cached;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }

        public String getFree() {
            return free;
        }

        public void setFree(String free) {
            this.free = free;
        }

        public String getShared() {
            return shared;
        }

        public void setShared(String shared) {
            this.shared = shared;
        }

        public String getBuffers() {
            return buffers;
        }

        public void setBuffers(String buffers) {
            this.buffers = buffers;
        }

        public String getCached() {
            return cached;
        }

        public void setCached(String cached) {
            this.cached = cached;
        }
    }

    public static class DiskpartBean {
        /**
         * total : 100
         * left : 30
         * filesys : ext4
         */
        @FieldTitle(value = "总大小",index =0 )
        public String total;
        @FieldTitle(value = "剩余空间",index =1 )
        public String left;
        @FieldTitle(value = "文件系统内容",index =2 )
        public String filesys;
    }


}
