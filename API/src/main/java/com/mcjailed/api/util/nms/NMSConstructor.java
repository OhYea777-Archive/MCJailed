package com.mcjailed.api.util.nms;

import java.lang.reflect.Constructor;

public class NMSConstructor {

    private final Constructor<?> constructor;

    public NMSConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    public Constructor<?> getRealConstructor() {
        return constructor;
    }

    public Object newInstance(Object... params) {
        try {
            return constructor.newInstance(params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
