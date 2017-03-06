package com.yulu.cat.library;

/**
 */

class Route {
    private String tag;

    private int level;

    String[] senders;

    Route(String tag, @LevelDef int level, String... senders) {
        if (senders == null || senders.length == 0) {
            throw new RuntimeException("at least one sender in a route define");
        }
        this.tag = tag;
        this.level = level;
        this.senders = senders;
    }

    Route(String tag, String... senders) {
        if (senders == null || senders.length == 0) {
            throw new RuntimeException("at least one sender in a route define");
        }
        this.tag = tag;
        this.level = Cat.LEVEL_ALL;
        this.senders = senders;
    }

    Route(@LevelDef int level, String... senders) {
        if (senders == null || senders.length == 0) {
            throw new RuntimeException("at least one sender in a route define");
        }
        this.tag = "*";
        this.level = level;
        this.senders = senders;
    }

    boolean match(String tag, @LevelDef int level) {
        if (level >= this.level) {
            if (this.tag.equals(tag) || "*".equals(tag)) {
                return true;
            }
        }
        return false;
    }

}

