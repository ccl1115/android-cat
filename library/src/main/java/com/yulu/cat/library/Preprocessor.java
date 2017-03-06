package com.yulu.cat.library;

/**
 * Preprocessor can process any log before it has been send to log implementations
 * @author yulu
 */

public interface Preprocessor {

    /**
     * Should log this message
     * @param tag the tag
     * @param level the level
     * @param msg the message
     * @return true if it will be logged
     */
    boolean should(String tag, @LevelDef int level, String msg);

    /**
     * Process the input msg
     * @param msg the original message
     * @return the processed message
     */
    String process(String msg);
}
