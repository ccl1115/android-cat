package com.yulu.cat.library.converters;

import com.yulu.cat.library.Converter;

import java.util.Collection;
import java.util.List;

/**
 * @author yulu
 */

public class ListConverter implements Converter<List> {
    @Override
    public String name() {
        return "collection";
    }

    @Override
    public Class<List> type() {
        return List.class;
    }

    @Override
    public String convert(List list) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        for (Object o : list) {
            builder.append("\t");
            builder.append(o.toString());
            builder.append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("\n}\n");

        return builder.toString();
    }
}
