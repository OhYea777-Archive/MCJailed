package com.mcjailed.api.util.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NMSClass {

    private final Class<?> clazz;

    public NMSClass(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getRealClass() {
        return clazz;
    }

    public boolean isInstance(Object object) {
        return clazz.isInstance(object);
    }

    public Object newInstance() {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public NMSMethod getMethod(String name, Object... types) {
        try {
            Class<?>[] classes = new Class<?>[types.length];

            for (int i = 0; i < types.length; i ++) {
                if (types[i] instanceof Class<?>) classes[i] = (Class<?>) types[i];
                else if (types[i] instanceof NMSClass) classes[i] = ((NMSClass) types[i]).getRealClass();
                else classes[i] = types[i].getClass();
            }

            try {
                return new NMSMethod(clazz.getMethod(name, classes));
            } catch (NoSuchMethodException ignored) {
                return new NMSMethod(clazz.getDeclaredMethod(name, classes));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public NMSConstructor getConstructor(Object... types) {
        try {
            Class<?>[] classes = new Class<?>[types.length];

            int i=0; for (Object e: types) {
                if (e instanceof Class<?>) classes[i++] = (Class)e;
                else if (e instanceof NMSClass) classes[i++] = ((NMSClass) e).getRealClass();
                else classes[i++] = e.getClass();
            }

            try {
                return new NMSConstructor(clazz.getConstructor(classes));
            } catch (NoSuchMethodException ignored) {
                return new NMSConstructor(clazz.getDeclaredConstructor(classes));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public NMSMethod findMethod(Object... types) {
        Class[] classes = new Class[types.length];

        int t=0; for (Object e: types) {
            if (e instanceof Class) classes[t++] = (Class)e;
            else if (e instanceof NMSClass) classes[t++] = ((NMSClass) e).getRealClass();
            else classes[t++] = e.getClass();
        }

        List<Method> methods = new ArrayList<Method>();

        Collections.addAll(methods, clazz.getMethods());
        Collections.addAll(methods, clazz.getDeclaredMethods());

        findMethod: for (Method m: methods) {
            Class<?>[] methodTypes = m.getParameterTypes();
            if (methodTypes.length != classes.length) continue;
            for (int i=0; i<classes.length; i++) {
                if (!classes.equals(methodTypes)) continue findMethod;
                return new NMSMethod(m);
            }
        }

        throw new RuntimeException("no such method");
    }

    public NMSMethod findMethodByName(String... names) {
        List<Method> methods = new ArrayList<Method>();

        Collections.addAll(methods, clazz.getMethods());
        Collections.addAll(methods, clazz.getDeclaredMethods());

        for (Method m: methods) {
            for (String name: names) {
                if (m.getName().equals(name)) {
                    return new NMSMethod(m);
                }
            }
        }

        throw new RuntimeException("no such method");
    }

    public NMSMethod findMethodByReturnType(NMSClass type) {
        return findMethodByReturnType(type.clazz);
    }

    public NMSMethod findMethodByReturnType(Class type) {
        if (type==null) type = void.class;

        List<Method> methods = new ArrayList<Method>();

        Collections.addAll(methods, clazz.getMethods());
        Collections.addAll(methods, clazz.getDeclaredMethods());

        for (Method m: methods) {
            if (type.equals(m.getReturnType())) {
                return new NMSMethod(m);
            }
        }

        throw new RuntimeException("no such method");
    }

    public NMSConstructor findConstructor(int number) {
        List<Constructor<?>> constructors = new ArrayList<Constructor<?>>();

        Collections.addAll(constructors, clazz.getConstructors());
        Collections.addAll(constructors, clazz.getDeclaredConstructors());

        for (Constructor<?> m : constructors) {
            if (m.getParameterTypes().length == number) return new NMSConstructor(m);
        }

        throw new RuntimeException("no such constructor");
    }

    public NMSField getField(String name) {
        try {
            try {
                return new NMSField(clazz.getField(name));
            } catch (NoSuchFieldException ignored) {
                return new NMSField(clazz.getDeclaredField(name));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public NMSField findField(NMSClass type) {
        return findField(type.clazz);
    }

    public NMSField findField(Class type) {
        if (type==null) type = void.class;

        List<Field> fields = new ArrayList<Field>();

        Collections.addAll(fields, clazz.getFields());
        Collections.addAll(fields, clazz.getDeclaredFields());

        for (Field f : fields) {
            if (type.equals(f.getType())) {
                return new NMSField(f);
            }
        }

        throw new RuntimeException("no such field");
    }

}
