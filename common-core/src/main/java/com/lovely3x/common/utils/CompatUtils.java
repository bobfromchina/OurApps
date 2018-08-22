package com.lovely3x.common.utils;

import android.os.Build;

import java.lang.reflect.Method;

/**
 * Created by lovely3x on 17/2/4.
 */

public class CompatUtils {

    private static boolean sIsMIUI;
    private static String sMIUIVersion;
    private static String sNavBarOverride;

    static {
        // Android allows a system property to override the presence of the navigation bar.
        // Used by the emulator.
        // See https://github.com/android/platform_frameworks_base/blob/master/policy/src/com/android/internal/policy/impl/PhoneWindowManager.java#L1076
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sMIUIVersion = (String) m.invoke(c, "ro.miui.ui.version.name");
                sIsMIUI = true;
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
                sNavBarOverride = null;
            }
        }
    }

    public  static boolean isMIUI() {
        return sIsMIUI;
    }

    public static String getMIUIVersion() {
        return sMIUIVersion;
    }

}
