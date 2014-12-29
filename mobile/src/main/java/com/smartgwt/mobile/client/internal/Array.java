package com.smartgwt.mobile.client.internal;

import java.util.AbstractList;
import java.util.List;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.util.IsCallback;

@SGWTInternal
public final class Array {

    public static final Record LOADING = new Record();
    static {
        LOADING.put("__loading__", "LOADING...");
    }

    public static interface ArrayElementCallback extends IsCallback {
        public void execute(int index, Object element);
    }

    public static boolean isArray(Object obj) {
        // http://ideone.com/KiJXIA
        return (obj != null && obj.getClass().isArray());
    }

    /**
     * An implementation of {@link java.lang.reflect.Array#getLength(Object)} for GWT.
     */
    public static int arrayLength(Object array) {
        assert isArray(array);
        if (array instanceof Object[]) return ((Object[])array).length;
        else if (array instanceof char[]) return ((char[])array).length;
        else if (array instanceof byte[]) return ((byte[])array).length;
        else if (array instanceof short[]) return ((short[])array).length;
        else if (array instanceof int[]) return ((int[])array).length;
        else if (array instanceof long[]) return ((long[])array).length;
        else if (array instanceof float[]) return ((float[])array).length;
        else if (array instanceof double[]) return ((double[])array).length;
        else return ((boolean[])array).length;
    }

    /**
     * An implementation of {@link java.lang.reflect.Array#get(Object, int)} for GWT.
     */
    public static Object arrayGet(Object array, int i) {
        assert isArray(array);
        if (array instanceof Object[]) return ((Object[])array)[i];
        else if (array instanceof char[]) return ((char[])array)[i];
        else if (array instanceof byte[]) return ((byte[])array)[i];
        else if (array instanceof short[]) return ((short[])array)[i];
        else if (array instanceof int[]) return ((int[])array)[i];
        else if (array instanceof long[]) return ((long[])array)[i];
        else if (array instanceof float[]) return ((float[])array)[i];
        else if (array instanceof double[]) return ((double[])array)[i];
        else return ((boolean[])array)[i];
    }

    public static void forEach(Object array, ArrayElementCallback callback) {
        assert isArray(array);
        final int len = arrayLength(array);
        for (int i = 0; i < len; ++i) {
            callback.execute(i, arrayGet(array, i));
        }
    }

    public static List<?> asList(final Object array) {
        assert isArray(array);
        return new AbstractList<Object>() {

            @Override
            public Object get(int index) {
                return arrayGet(array, index);
            }

            @Override
            public int size() {
                return arrayLength(array);
            }
        };
    }

    private Array() {}
}
