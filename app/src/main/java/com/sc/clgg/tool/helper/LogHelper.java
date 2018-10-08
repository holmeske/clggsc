package com.sc.clgg.tool.helper;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class LogHelper {

    private static final String TAG = "logcat";
    private static boolean logSwitch = true;
    private static boolean showClassMethodName = false;

    /**
     * 日志开关
     *
     * @param isOpen true 打印  false不打印
     */
    public static void setLogSwitch(boolean isOpen) {
        logSwitch = isOpen;
    }

    /**
     * @see Log#d(String, String)
     */
    public static void d(String message) {
        if (!logSwitch) {
            return;
        }
        Log.d(TAG, buildMessage(message));
    }

    /**
     * @see Log#d(String, String)
     */
    public static void d(String Tag, String message) {
        if (!logSwitch) {
            return;
        }
        Log.d(Tag, buildMessage(message));
    }

    /**
     * @see Log#e(String, String)
     */
    public static void e(Exception exception) {
        if (!logSwitch) {
            return;
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        Log.e(TAG, buildMessage(sw.toString()));
    }

    /**
     * @see Log#e(String, String)
     */
    public static void e(String message) {
        if (!logSwitch) {
            return;
        }
        Log.e(TAG, buildMessage(message));
    }

    /**
     * @see Log#e(String, String)
     */
    public static void e(String Tag, String message) {
        if (!logSwitch) {
            return;
        }
        Log.e(Tag, buildMessage(message));
    }

    /**
     * @see Log#i(String, String)
     */
    public static void i(String message) {
        if (!logSwitch) {
            return;
        }
        Log.i(TAG, buildMessage(message));
    }

    /**
     * @see Log#i(String, String)
     */
    public static void i(String Tag, String message) {
        if (!logSwitch) {
            return;
        }
        Log.i(Tag, buildMessage(message));
    }

    /**
     * @see Log#v(String, String)
     */
    public static void v(String message) {
        if (!logSwitch) {
            return;
        }
        Log.v(TAG, buildMessage(message));
    }

    /**
     * @see Log#v(String, String)
     */
    public static void v(String Tag, String message) {
        if (!logSwitch) {
            return;
        }
        Log.v(Tag, buildMessage(message));
    }

    /**
     * @see Log#w(String, String)
     */
    public static void w(String message) {
        if (!logSwitch) {
            return;
        }
        Log.w(TAG, buildMessage(message));
    }

    /**
     * @see Log#w(String, String)
     */
    public static void w(String Tag, String message) {
        if (!logSwitch) {
            return;
        }
        Log.w(Tag, buildMessage(message));
    }

    /**
     * @see Log#wtf(String, String)
     */
    public static void wtf(String message) {
        if (!logSwitch) {
            return;
        }
        Log.wtf(TAG, buildMessage(message));
    }

    /**
     * @see Log#wtf(String, String)
     */
    public static void wtf(String Tag, String message) {
        if (!logSwitch) {
            return;
        }
        Log.wtf(Tag, buildMessage(message));
    }

    /**
     * @see Log#println(int, String, String)
     */
    public static void println(String message) {
        if (!logSwitch) {
            return;
        }
        Log.println(Log.INFO, TAG, message);
    }

    private static String buildMessage(String log) {
        /*if (caller.getClassName().equals("com.sc.clgg.http.HttpManager$1")) {
            return "日志:  " + log + "   \n   ";
        } else {
            return "方法:  " + caller.getClassName() + "." + caller.getMethodName() + "();" + "\n"
                    + "日志:  " + log + "   \n   ";
        }*/
        if (showClassMethodName) {
            StackTraceElement caller = new Throwable().getStackTrace()[2];
            return caller.getClassName() + "." + caller.getMethodName() + "();"
                    + "\n"
                    + log + "  \n  ";
        } else {
            return log + "  \n  ";
        }
    }
}