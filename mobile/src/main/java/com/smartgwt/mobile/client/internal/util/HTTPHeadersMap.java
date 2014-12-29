package com.smartgwt.mobile.client.internal.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class HTTPHeadersMap implements Map<String, String> {

    private final Map<String /* name, lowercased */, Entry<String /* name in original case */, String /* value */>> headers = new HashMap<String, Entry<String, String>>();

    @Override
    public void clear() {
        headers.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof String)) return false;
        final String lcName = ((String)key).toLowerCase();
        return headers.containsKey(lcName);
    }

    @Override
    public boolean containsValue(Object value) {
        for (final Entry<String, String> headersValue : headers.values()) {
            if (headersValue.getValue().equals(value)) return true;
        }
        return false;
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return new Set<Entry<String, String>>() {

            @Override
            public boolean add(Entry<String, String> e) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean addAll(Collection<? extends Entry<String, String>> entries) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void clear() {
                headers.clear();
            }

            @Override
            public boolean contains(Object obj) {
                if (!(obj instanceof Entry)) return false;
                final Entry<?, ?> e = (Entry<?, ?>)obj;
                if (!(e.getKey() instanceof String)) return false;
                final String lcName = ((String)e.getKey()).toLowerCase();
                final Entry<String, String> headersValue = headers.get(lcName);
                return (headersValue != null && headersValue.getValue().equals(e.getValue()));
            }

            @Override
            public boolean containsAll(Collection<?> objects) {
                for (final Object obj : objects) {
                    if (!contains(obj)) return false;
                }
                return true;
            }

            @Override
            public boolean isEmpty() {
                return headers.isEmpty();
            }

            @Override
            public Iterator<Entry<String, String>> iterator() {
                return headers.values().iterator();
            }

            @Override
            public boolean remove(Object obj) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int size() {
                return headers.size();
            }

            @Override
            public Object[] toArray() {
                return headers.values().toArray();
            }

            @Override
            public <T> T[] toArray(T[] arr) {
                return headers.values().toArray(arr);
            }
        };
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof String)) return null;
        final String lcName = ((String)key).toLowerCase();
        final Entry<String, String> headersValue = headers.get(lcName);
        if (headersValue == null) return null;
        return headersValue.getValue();
    }

    @Override
    public boolean isEmpty() {
        return headers.isEmpty();
    }

    @Override
    public Set<String> keySet() {
        return headers.keySet();
    }

    @Override
    public String put(final String name, final String value) {
        if (name == null) throw new NullPointerException("name");
        if (value == null) throw new NullPointerException("value");
        final String lcName = name.toLowerCase();
        final Entry<String, String> headersValue = headers.put(lcName, new Entry<String, String>() {

            @Override
            public String getKey() {
                return name;
            }

            @Override
            public String getValue() {
                return value;
            }

            @Override
            public String setValue(String value) {
                throw new UnsupportedOperationException();
            }
        });
        if (headersValue == null) return null;
        return headersValue.getValue();
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        for (Entry<? extends String, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof String)) return null;
        final String lcName = ((String)key).toLowerCase();
        final Entry<String, String> headersValue = headers.remove(lcName);
        if (headersValue == null) return null;
        return headersValue.getValue();
    }

    @Override
    public int size() {
        return headers.size();
    }

    @Override
    public Collection<String> values() {
        return new Collection<String>() {

            @Override
            public boolean add(String value) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean addAll(Collection<? extends String> values) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void clear() {
                headers.clear();
            }

            @Override
            public boolean contains(Object obj) {
                return HTTPHeadersMap.this.containsValue(obj);
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                for (final Object obj : c) {
                    if (!contains(obj)) return false;
                }
                return true;
            }

            @Override
            public boolean isEmpty() {
                return headers.isEmpty();
            }

            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {

                    private final Iterator<Entry<String, String>> it = headers.values().iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public String next() {
                        final Entry<String, String> headersValue = it.next();
                        return headersValue.getValue();
                    }

                    @Override
                    public void remove() {
                        it.remove();
                    }
                };
            }

            @Override
            public boolean remove(Object obj) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int size() {
                return headers.size();
            }

            @Override
            public Object[] toArray() {
                final String[] ret = new String[headers.size()];
                int i = 0;
                for (final Entry<String, String> headersValue : headers.values()) {
                    ret[i++] = headersValue.getValue();
                }
                return ret;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <T> T[] toArray(T[] arr) {
                if (arr.length != headers.size()) arr = (T[])new String[headers.size()];
                int i = 0;
                for (final Entry<String, String> headersValue : headers.values()) {
                    arr[i++] = (T)headersValue.getValue();
                }
                return arr;
            }
        };
    }
}
