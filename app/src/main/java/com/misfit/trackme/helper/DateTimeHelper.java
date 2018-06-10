package com.misfit.trackme.helper;

/**
 * Created by VinhLe on Jun, 2018.
 */
public final class DateTimeHelper
{

    public static String parseStringToTime(long milliseconds)
    {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        long hours = minutes / 60;
        minutes = minutes % 60;

        String result = "";
        if (hours < 10)
        {
            result += "0" + hours + ":";
        }
        else
        {
            result += hours + ":";
        }
        if (minutes < 10)
        {
            result += "0" + minutes + ":";
        }
        else
        {
            result += minutes + ":";
        }
        if (seconds < 10)
        {
            result += "0" + seconds;
        }
        else
        {
            result += seconds;
        }

        return result;
    }

}
