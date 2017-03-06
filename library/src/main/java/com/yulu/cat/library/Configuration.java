package com.yulu.cat.library;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Configuration of how Cat works
 * @author yulu
 */

public interface Configuration {

    Context context();

    String[] tags();

    Preprocessor[] preprocessors();

    Sender[] senders();

    Converter[] converters();

    Router router();

    class Builder {
        private boolean config;
        Context context;
        Router router;
        Set<String> tags = new HashSet<>();
        Set<Preprocessor> preprocessors = new HashSet<>();
        Set<Sender> senders = new HashSet<>();
        Set<Converter> converters = new HashSet<>();

        Builder() {

        }

        Builder(boolean config) {
            this.config = config;
        }

        public Builder context(@NonNull Context context) {
            this.context = context;
            return this;
        }

        public Builder tag(@NonNull String tag) {
            this.tags.add(tag);
            return this;
        }

        public Builder preprocessor(@NonNull Preprocessor preprocessor) {
            this.preprocessors.add(preprocessor);
            return this;
        }

        public Builder sender(@NonNull Sender sender) {
            this.senders.add(sender);
            return this;
        }

        public Builder converter(@NonNull Converter converter) {
            this.converters.add(converter);
            return this;
        }

        public Builder router(@NonNull Router router) {
            this.router = router;
            return this;
        }

        public Configuration build() {
            if (context == null) {
                throw new IllegalArgumentException("context is null when build configuration");
            }
            Configuration c = new Configuration() {
                @Override
                public Context context() {
                    return context;
                }

                @Override
                public String[] tags() {
                    String[] t = new String[tags.size()];
                    tags.toArray(t);
                    return t;
                }

                @Override
                public Preprocessor[] preprocessors() {
                    Preprocessor[] t = new Preprocessor[preprocessors.size()];
                    preprocessors.toArray(t);
                    return t;
                }

                @Override
                public Sender[] senders() {
                    Sender[] t = new Sender[senders.size()];
                    senders.toArray(t);
                    return t;
                }

                @Override
                public Converter[] converters() {
                    Converter[] t = new Converter[converters.size()];
                    converters.toArray(t);
                    return t;
                }

                @Override
                public Router router() {
                    return router;
                }
            };

            if (config) {
                Cat.configure(c);
            }
            return c;
        }
    }

}
