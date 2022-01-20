package com.mcjailed.api.util.nms;

import java.lang.reflect.Field;

public class NMSField {

    private final Field field;

    public NMSField(Field field) {
        this.field = field;
    }

    public Field getRealField() {
        return field;
    }

    public NMSClass getNMSClass() {
        return new NMSClass(field.getDeclaringClass());
    }

    public NMSClass getTypeNMSClass() {
        return new NMSClass(field.getType());
    }

    public NMSExecutor of(Object object) {
        return new NMSExecutor(object);
    }

    public void set(Object value) {
        try {
            field.set(null, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object get() {
        try {
            return field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public class NMSExecutor {

        private final Object object;

        public NMSExecutor(Object object) {
            this.object = object;
        }

        public void set(Object value) {
            try {
                field.set(object, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public Object get() {
            try {
                return field.get(object);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

}
