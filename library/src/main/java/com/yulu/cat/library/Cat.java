package com.yulu.cat.library;

import java.util.HashMap;
import java.util.Map;

/**
 * Cat is a log util on Android with many features. It highly configurable and extensible.
 * <p>
 * # Preprocessor<br/>
 * Before log a message, it can be processed by preprocessors, like format message, drop message.
 * <p>
 * # Converter<br/>
 * Converter converts object to string, register a converter to Cat, then you can simply call {@link Cat#log(int, Object)}
 * <p>
 * # Router<br/>
 * Router can route a message to any sender by its tag name and level.
 * <p>
 * # Sender<br/>
 * Sender do the real logging business.
 * <p>
 * A log has three properties: message, tag and level. it can be routed to more than one sender.
 *
 * @author yulu
 */

public class Cat {

    public static final int LEVEL_ALL = 0x0;
    public static final int LEVEL_V = 0x1;
    public static final int LEVEL_I = 0x2;
    public static final int LEVEL_D = 0x3;
    public static final int LEVEL_W = 0x4;
    public static final int LEVEL_E = 0x5;

    private static Map<String, Cat> instances;

    private static Configuration configuration;

    private static Map<Class, Converter> converters;
    private static Map<String, Sender> senders;

    private static Router router;

    static void configure(Configuration configuration) {
        Cat.configuration = configuration;

        String[] tags = Cat.configuration.tags();
        Cat.instances = new HashMap<>(tags.length);

        for (String tag : tags) {
            Cat.instances.put(tag, new Cat(tag));
        }

        Converter[] converters = Cat.configuration.converters();
        Cat.converters = new HashMap<>(converters.length);
        for (Converter converter : converters) {
            Cat.converters.put(converter.type(), converter);
        }

        Sender[] senders = Cat.configuration.senders();
        Cat.senders = new HashMap<>(senders.length);
        for (Sender sender : senders) {
            Cat.senders.put(sender.name(), sender);
        }

        Cat.router = configuration.router();
    }

    /**
     * Config Cat using a {@link everphoto.util.log.cat.Configuration.Builder}
     *
     * @return the builder
     */
    public static Configuration.Builder builder() {
        return new Configuration.Builder(true);
    }

    /**
     * Get a Cat by its tag name
     * @param tagName the tag name
     * @return the Cat instance
     */
    public static Cat get(String tagName) {
        return instances.get(tagName);
    }

    private Cat(String tagName) {
        this.tagName = tagName;
    }

    private String tagName;

    /**
     * Log a message with a certain level
     * @param level the level
     * @param msg the message
     */
    public void log(@LevelDef int level, String msg) {
        process(level, msg);
    }

    /**
     * Log a message with LEVEL_V
     * @param msg the message
     */
    public void v(String msg) {
        processV(msg);
    }

    /**
     * Log a message with LEVEL_I
     * @param msg the message
     */
    public void i(String msg) {
        processI(msg);
    }

    /**
     * Log a message with LEVEL_D
     * @param msg the message
     */
    public void d(String msg) {
        processD(msg);
    }

    /**
     * Log a message with LEVEL_W
     * @param msg the message
     */
    public void w(String msg) {
        processW(msg);
    }

    /**
     * Log a message with LEVEL_E
     * @param msg the message
     */
    public void e(String msg) {
        processE(msg);
    }

    /**
     * Log a object with a certain level
     * @param level the level
     * @param object the object
     * @param <T> the type of the object
     */
    public <T> void log(@LevelDef int level, T object) {
        log(level, convert(object));
    }

    /**
     * Log a object with LEVEL_V
     * @param object the object
     * @param <T> the type of the object
     */
    public <T> void v(T object) {
        v(convert(object));
    }

    /**
     * Log a object with LEVEL_I
     * @param object the object
     * @param <T> the type of the object
     */
    public <T> void i(T object) {
        i(convert(object));
    }

    /**
     * Log a object with LEVEL_D
     * @param object the object
     * @param <T> the type of the object
     */
    public <T> void d(T object) {
        d(convert(object));
    }

    /**
     * Log a object with LEVEL_W
     * @param object the object
     * @param <T> the type of the object
     */
    public <T> void w(T object) {
        w(convert(object));
    }

    /**
     * Log a object with LEVEL_E
     * @param object the object
     * @param <T> the type of the object
     */
    public <T> void e(T object) {
        e(convert(object));
    }


    private <T> String convert(T object) {
        Converter<T> c = converters.get(object.getClass());
        if (c == null) {
            throw new RuntimeException("Can not log this type of object: " + object.getClass().toString());
        }
        return c.convert(object);
    }

    private void process(@LevelDef int level, String msg) {
        for (Preprocessor preprocessor : configuration.preprocessors()) {
            if (preprocessor.should(tagName, level, msg)) {
                msg = preprocessor.process(msg);
            } else {
                return;
            }
        }
        send(level, msg);
    }

    private void processV(String msg) {
        for (Preprocessor preprocessor : configuration.preprocessors()) {
            if (preprocessor.should(tagName, LEVEL_V, msg)) {
                msg = preprocessor.process(msg);
            } else {
                return;
            }
        }
        sendV(msg);
    }

    private void processI(String msg) {
        for (Preprocessor preprocessor : configuration.preprocessors()) {
            if (preprocessor.should(tagName, LEVEL_I, msg)) {
                msg = preprocessor.process(msg);
            } else {
                return;
            }
        }
        sendI(msg);
    }

    private void processD(String msg) {
        for (Preprocessor preprocessor : configuration.preprocessors()) {
            if (preprocessor.should(tagName, LEVEL_D, msg)) {
                msg = preprocessor.process(msg);
            } else {
                return;
            }
        }
        sendD(msg);
    }

    private void processW(String msg) {
        for (Preprocessor preprocessor : configuration.preprocessors()) {
            if (preprocessor.should(tagName, LEVEL_W, msg)) {
                msg = preprocessor.process(msg);
            } else {
                return;
            }
        }
        sendW(msg);
    }

    private void processE(String msg) {
        for (Preprocessor preprocessor : configuration.preprocessors()) {
            if (preprocessor.should(tagName, LEVEL_E, msg)) {
                msg = preprocessor.process(msg);
            } else {
                return;
            }
        }
        sendE(msg);
    }

    private void send(@LevelDef int level, String msg) {
        String[] senders = Cat.router.route(tagName, level);
        for (String sender : senders) {
            Cat.senders.get(sender).log(tagName, level, msg);
        }
    }

    private void sendV(String msg) {
        String[] senders = Cat.router.route(tagName, LEVEL_V);
        for (String sender : senders) {
            Cat.senders.get(sender).v(tagName, msg);
        }
    }

    private void sendI(String msg) {
        String[] senders = Cat.router.route(tagName, LEVEL_I);
        for (String sender : senders) {
            Cat.senders.get(sender).i(tagName, msg);
        }
    }

    private void sendD(String msg) {
        String[] senders = Cat.router.route(tagName, LEVEL_D);
        for (String sender : senders) {
            Cat.senders.get(sender).d(tagName, msg);
        }
    }

    private void sendW(String msg) {
        String[] senders = Cat.router.route(tagName, LEVEL_W);
        for (String sender : senders) {
            Cat.senders.get(sender).w(tagName, msg);
        }
    }

    private void sendE(String msg) {
        String[] senders = Cat.router.route(tagName, LEVEL_E);
        for (String sender : senders) {
            Cat.senders.get(sender).e(tagName, msg);
        }
    }
}
