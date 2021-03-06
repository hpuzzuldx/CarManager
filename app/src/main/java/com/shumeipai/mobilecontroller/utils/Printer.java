package com.shumeipai.mobilecontroller.utils;

public interface Printer {
    Settings init(LogLevel logLevel);

    Settings getSettings();

    void d(String tag, String message, Object... args);

    void e(String tag, String message, Object... args);

    void e(String tag, Throwable throwable, String message, Object... args);

    void w(String tag, String message, Object... args);

    void i(String tag, String message, Object... args);

    void v(String tag, String message, Object... args);

    void json(String tag, String json);

    void xml(String tag, String xml);

    void normalLog(String tag, String message);
}

