package com.yulu.cat.library;

/**
 * Converter is used to convert a object to string when log the object.
 */

public interface Converter<T> {
    String name();

    Class<T> type();

    String convert(T t);
}
