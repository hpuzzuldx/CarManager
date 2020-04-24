package com.shumeipai.mobilecontroller.utils;


public enum LogLevel {

    OFF(Integer.MAX_VALUE),

    VERBOSE(1),

    DEBUG(2),

    INFO(3),

    WARN(4),

    ERROR(5);

    public final int level;

    private LogLevel(final int level) {
        this.level = level;
    }

}
