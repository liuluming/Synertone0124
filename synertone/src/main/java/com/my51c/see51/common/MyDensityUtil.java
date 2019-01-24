package com.my51c.see51.common;



import com.synertone.commonutil.util.ScreenUtil;

import static com.baidu.mapapi.common.SysOSUtil.getDensity;

public class MyDensityUtil extends ScreenUtil {
    public static int dip2px(float dpValue) {
        return (int) (dpValue * getDensity() + 0.5F);
    }
}
