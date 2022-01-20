package com.mcjailed.api.util.nms;

import java.lang.reflect.Method;

public class NMSMethod {

    private final Method method;

    public NMSMethod(Method method) {
        this.method = method;

        method.setAccessible(true);
    }

    public Method getRealMethod() {
        return method;
    }

    public NMSClass getNMSClass() {
        return new NMSClass(method.getDeclaringClass());
    }

    public NMSExecutor of(Object object) {
        return new NMSExecutor(object);
    }

    public Object call(Object... params) {
        try {
            return method.invoke(null, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public class NMSExecutor {

        private final Object object;

        public NMSExecutor(Object object) {
            this.object = object;
        }

        public Object call(Object... params) {
            try {
                return method.invoke(object, params);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}
