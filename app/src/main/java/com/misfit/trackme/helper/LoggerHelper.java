package com.misfit.trackme.helper;

/**
 * Created by VinhLe on Jun, 2018.
 */
public final class LoggerHelper
{

    static boolean IS_DEBUG = true;

    /**
     * Flag use to check state debug mode
     */
    public static boolean isDebugging()
    {
        return IS_DEBUG;
    }

    /**
     * Send a DEBUG log message.
     * @param TAG TAG name
     * @param message message content.
     * @param e
     */
    public static void d(String TAG, String message, Exception e) {
        if (!isDebugging())
        {
            return;
        }
        if (message == null)
        {
            return;
        }
        android.util.Log.d(TAG, message, e);
    }
    /**
     * Send a DEBUG log message.
     * @param TAG TAG name
     * @param message message content.
     */
    public static void d(String TAG, String message) {
        if (!isDebugging())
        {
            return;
        }
        if (message == null)
        {
            return;
        }
        android.util.Log.d(TAG, message);
    }
    /**
     * Send a WARNING log message.
     * @param TAG TAG name
     * @param message message content.
     */
    public static void w(String TAG, String message) {
        if (!isDebugging())
        {
            return;
        }
        if (message == null)
        {
            return;
        }
        android.util.Log.w(TAG, message);
    }
    /**
     * Send a ERROR log message.
     * @param TAG TAG name
     * @param message message content.
     */
    public static void e(String TAG, String message) {
        if (!isDebugging())
        {
            return;
        }
        if (message == null)
        {
            return;
        }
        android.util.Log.e(TAG, message);
    }
    /**
     * Send a INFO log message.
     * @param TAG TAG name
     * @param message message content.
     */
    public static void i(String TAG, String message) {
        if (!isDebugging())
        {
            return;
        }
        if (message == null)
        {
            return;
        }
        android.util.Log.i(TAG, message);
    }
    /**
     * send a V log message
     * @param TAG
     * @param message
     */
    public static void v(String TAG, String message) {
        if (!isDebugging())
        {
            return;
        }
        if (message == null)
        {
            return;
        }
        android.util.Log.v(TAG, message);
    }
    /**
     * Show log error with exception
     * @param TAG
     * @param message
     * @param e
     */
    public static void e(String TAG, String message, Exception e) {
        if (!isDebugging())
        {
            return;
        }
        if (message == null)
        {
            return;
        }
        android.util.Log.e(TAG, message, e);
    }
}
