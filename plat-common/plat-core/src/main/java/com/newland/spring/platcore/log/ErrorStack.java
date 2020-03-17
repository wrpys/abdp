package com.newland.spring.platcore.log;

import java.io.Serializable;

/**
 * 异常堆栈处理
 * @author Administrator
 *
 */
public class ErrorStack implements Serializable {
    private static final long serialVersionUID = 464062795461173066L;

    public static String getErrorMsg(Throwable t) {
        if (null == t)
            return null;
        StringBuffer errorMsg = new StringBuffer();
        errorMsg.append(t);
        StackTraceElement[] trace = t.getStackTrace();
        for (int i = 0; i < trace.length; i++)
            errorMsg.append("\r\n\tat " + trace[i]);
        Throwable ourCause = t.getCause();
        if (ourCause != null)
            errorMsg.append(getErrorCause(ourCause, trace));
        return errorMsg.toString();
    }

    private static String getErrorCause(Throwable ourCause, StackTraceElement[] causedTrace) {
        StringBuffer errorMsg = new StringBuffer();
        StackTraceElement[] trace = ourCause.getStackTrace();
        int m = trace.length - 1, n = causedTrace.length - 1;
        while (m >= 0 && n >= 0 && trace[m].equals(causedTrace[n])) {
            m--;
            n--;
        }
        int framesInCommon = trace.length - 1 - m;
        errorMsg.append("\r\nCaused by: \r\n" + ourCause);
        for (int i = 0; i <= m; i++)
            errorMsg.append("\r\n\tat " + trace[i]);
        if (framesInCommon != 0)
            errorMsg.append("\r\n\t... " + framesInCommon + " more");
        Throwable ourCause2 = ourCause.getCause();
        if (ourCause2 != null)
            errorMsg.append(getErrorCause(ourCause2, trace));
        return errorMsg.toString();
    }
}
