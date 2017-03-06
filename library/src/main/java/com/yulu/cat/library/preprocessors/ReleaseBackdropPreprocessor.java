package com.yulu.cat.library.preprocessors;


import com.yulu.cat.library.LevelDef;
import com.yulu.cat.library.Preprocessor;

/**
 * A preprocessor that drop all logs that level is less than {@link #level} when not in debug version
 * @author yulu
 */
public class ReleaseBackdropPreprocessor implements Preprocessor {

    private final boolean debug;
    private final int level;

    public ReleaseBackdropPreprocessor(boolean debug, @LevelDef int level) {
        this.debug = debug;
        this.level = level;
    }

    @Override
    public boolean should(String tag, @LevelDef int level, String msg) {
        return debug || level >= this.level;
    }

    @Override
    public String process(String msg) {
        return msg;
    }
}
