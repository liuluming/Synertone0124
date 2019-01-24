package com.my51c.see51.app.utils;

import com.my51c.see51.app.bean.StarCodeModel;

public class ModeUtil {
    private static String mode;
    /**
     * 实际对星参数与spinner的postion不一致，需要转换
     * @param currentStar
     */
    public static void changle(StarCodeModel currentStar) {
        mode = currentStar.getMode();
        switch (mode){
            case "0":
                currentStar.setMode("0");
                break;
            case "1":
                currentStar.setMode("2");
                break;
            case "2":
                currentStar.setMode("1");
                break;
            /*case "3":
                currentStar.setMode("3");
                break;*/

        }

    }
    public static void restore(StarCodeModel currentStar){
        currentStar.setMode(mode);
    }
}
