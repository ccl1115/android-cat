package com.yulu.cat.library;

import java.util.ArrayList;
import java.util.List;

/**
 */

public class Router {

    private List<Route> routes = new ArrayList<>();

    String[] route(String tag, int level) {
        for (Route route : routes) {
            if (route.match(tag, level)) {
                return route.senders;
            }
        }
        return new String[0];
    }

    public static class Builder {

        Router router = new Router();

        public Builder add(String tag, @LevelDef int level, String... senders) {
            Route r = new Route(tag, level, senders);
            router.routes.add(r);
            return this;
        }

        public Builder add(String tag, String... senders) {
            Route r = new Route(tag, senders);
            router.routes.add(r);
            return this;
        }

        public Builder add(@LevelDef int level, String... senders) {
            Route r = new Route(level, senders);
            router.routes.add(r);
            return this;
        }

        public Builder add(Route route) {
            router.routes.add(route);
            return this;
        }

        public Router build() {
            return router;
        }
    }
}
