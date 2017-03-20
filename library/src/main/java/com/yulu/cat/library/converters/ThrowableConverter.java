package com.yulu.cat.library.converters;

import com.yulu.cat.library.Converter;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Make Cat can log a throwable
 * @author yulu
 */

public class ThrowableConverter implements Converter<Throwable> {
    @Override
    public String name() {
        return "throwable";
    }

    @Override
    public Class<Throwable> type() {
        return Throwable.class;
    }

    @Override
    public String convert(Throwable throwable) {
        StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
