package com.yulu.cat.library;

/**
 * Sender implements how to do logging, like to log msg to logcat or file.
 * @author yulu
 */

public interface Sender {

    String name();

    void log(String tag, @LevelDef int level, String msg);

    void v(String tag, String msg);

    void i(String tag, String msg);

    void d(String tag, String msg);

    void w(String tag, String msg);

    void e(String tag, String msg);
}
