package com.lovely3x.common.utils;

/**
 * Created by lovely3x on 16-3-9.
 */
public class ColorUtils {


    /**
     * 输入一个颜色,将它拆成三个部分:
     * 红色,绿色和蓝色
     */
    public static int[] retrieveRGBComponent(int color) {
        int r = color >> 16;
        int g = (color >> 8) & 0xff;
        int b = color & 0xff;

        return new int[]{r, g, b};
    }


    /**
     * @param color
     * @param alpha
     * @return
     */
    public static int setAlpha(int color, int alpha) {

        int a = alpha & 0xFF;

        int r = color >> 16 & 0xFF;

        int g = color >> 8 & 0xFF;
        
        int b = color & 0xFF;

        return a << 24 | r << 16 | g << 8 | b;
    }


    /**
     * 红色,绿色和蓝色三色组合
     */
    public static int generateFromRGBComponent(int[] rgb) {
        if (rgb == null || rgb.length != 3 ||
                rgb[0] < 0 || rgb[0] > 255 ||
                rgb[1] < 0 || rgb[1] > 255 ||
                rgb[2] < 0 || rgb[2] > 255)
            return 0xFFFFFF;
        return rgb[0] << 16 | rgb[1] << 8 | rgb[2];
    }

    /**
     * color1是浅色,color2是深色,实现渐变
     * steps是指在多大的区域中渐变,
     */
    public static int[] generateTransitionalColor(int color1, int color2, int steps) {
        if (steps < 3)
            return null;

        int[] color1RGB = retrieveRGBComponent(color1);
        int[] color2RGB = retrieveRGBComponent(color2);

        int[] colors = new int[steps];
        colors[0] = color1;
        steps = steps - 2;

        int redDiff = color2RGB[0] - color1RGB[0];
        int greenDiff = color2RGB[1] - color1RGB[1];
        int blueDiff = color2RGB[2] - color1RGB[2];
        for (int i = 1; i < steps - 1; i++) {
            int[] tmpRGB = new int[]{
                    color1RGB[0] + redDiff * i / steps, color1RGB[1] + greenDiff * i / steps, color1RGB[2] + blueDiff * i / steps};

            colors[i] = generateFromRGBComponent(tmpRGB);
        }
        colors[steps - 1] = color2;
        return colors;
    }
}
