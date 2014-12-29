package com.smartgwt.mobile.client.data;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.storage.client.StorageEvent;
import com.smartgwt.mobile.client.internal.data.StorageImpl;

/**
 * {@link java.util.Map Map<String, String>} adapter for native browser Web Storage objects.
 * 
 * <p><b>NOTE:</b> Storage maps do not support <code>null</code> keys or values.  This is
 * because in tests of Google Chrome's Web Storage support, setting an entry with key
 * <code>null</code> actually sets an entry with key <code>"null"</code>.  Similarly, setting
 * an entry with value <code>null</code> actually sets an entry with value <code>"null"</code>.
 * Following the principle of least astonishment, Storage maps disallow <code>null</code> for
 * keys and values.
 * 
 * @see <a href="http://dev.w3.org/html5/webstorage/">Web Storage - W3C</a>
 */
public class Storage implements Map<String, String> {

    public static final Storage LOCAL_STORAGE = new Storage(com.google.gwt.storage.client.Storage.getLocalStorageIfSupported());
    public static final Storage SESSION_STORAGE = new Storage(com.google.gwt.storage.client.Storage.getSessionStorageIfSupported());

    public static class QuotaExceededException extends RuntimeException {

        public QuotaExceededException(Throwable cause) {
            super(cause);
        }

        public QuotaExceededException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public class KeySet implements Set<String> {

        @Override
        public int size() {
            return Storage.this.size();
        }

        @Override
        public boolean isEmpty() {
            return Storage.this.isEmpty();
        }

        @Override
        public boolean contains(Object obj) {
            return Storage.this.containsKey(obj);
        }

        /**
         * Returns an Iterator for iterating the keys.
         * 
         * <p>The returned Iterator does not support the {@link java.util.Iterator#remove()
         * remove()} operation because "adding or removing a key may change the order of the
         * keys" in the native Web Storage object.
         */
        @Override
        public Iterator<String> iterator() {
            return new Iterator<String>() {
                private int i = 0;

                @Override
                public boolean hasNext() {
                    return i < size();
                }

                @Override
                public String next() {
                    return Storage.this.getKey(i++);
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public Object[] toArray() {
            final int size = size();
            String[] ret = new String[size];
            int i = 0;
            for (String key : this) {
                ret[i++] = key;
            }
            return ret;
        }

        @Override
        public <T> T[] toArray(T[] a) {
            a.getClass().getComponentType();
            // GWT does not support java.lang.reflect.Array.newInstance().
            @SuppressWarnings("unchecked")
            T[] ret = (T[])toArray();
            return ret;
        }

        @Override
        public boolean add(String str) {
            // Can't just insert a key.
            throw new UnsupportedOperationException();
        }

        /**
         * @return <code>true</code> if this set contained the specified element.
         */
        @Override
        public boolean remove(Object obj) {
            return Storage.this.remove(obj) != null;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            for (Object obj : c) {
                if (! contains(obj)) return false;
            }
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends String> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        /**
         * @return <code>true</code> if this set changed as a result of the call.
         */
        @Override
        public boolean removeAll(Collection<?> c) {
            boolean didChange = false;
            for (Object obj : c) {
                didChange |= remove(obj);
            }
            return didChange;
        }

        @Override
        public void clear() {
            Storage.this.clear();
        }
    }

    public class ValueCollection implements Collection<String> {

        @Override
        public int size() {
            return Storage.this.size();
        }

        @Override
        public boolean isEmpty() {
            return Storage.this.isEmpty();
        }

        @Override
        public boolean contains(Object obj) {
            return Storage.this.containsValue(obj);
        }

        @Override
        public Iterator<String> iterator() {
            return new Iterator<String>() {
                private int i = 0;

                @Override
                public boolean hasNext() {
                    return i < size();
                }

                @Override
                public String next() {
                    return Storage.this.get(Storage.this.getKey(i++));
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public Object[] toArray() {
            final int size = size();
            String[] ret = new String[size];
            int i = 0;
            for (String value : this) {
                ret[i++] = value;
            }
            return ret;
        }

        @Override
        public <T> T[] toArray(T[] a) {
            a.getClass().getComponentType();
            // GWT does not support java.lang.reflect.Array.newInstance().
            @SuppressWarnings("unchecked")
            T[] ret = (T[])toArray();
            return ret;
        }

        @Override
        public boolean add(String str) {
            // Can't just insert a value.
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends String> c) {
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
        public void clear() {
            Storage.this.clear();
        }
    }

    public class StorageEntry implements Entry<String, String> {

        private final String key;
        private String value;

        StorageEntry(String key) {
            this.key = key;
            assert key != null;
            this.value = Storage.this.get(key);
            assert value != null;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String setValue(String value) throws QuotaExceededException {
            final String oldValue = this.value;
            Storage.this.put(key, value);
            this.value = value;
            return oldValue;
        }
    }

    public class EntrySet implements Set<Entry<String, String>> {

        @Override
        public int size() {
            return Storage.this.size();
        }

        @Override
        public boolean isEmpty() {
            return Storage.this.isEmpty();
        }

        @Override
        public boolean contains(Object obj) {
            if (obj == null) throw new NullPointerException();
            if (! (obj instanceof Entry)) return false;
            Entry<?, ?> e = (Entry<?, ?>)obj;
            String value = Storage.this.get(e.getKey());
            return value != null && value.equals(e.getValue());
        }

        @Override
        public Iterator<Entry<String, String>> iterator() {
            return new Iterator<Entry<String, String>>() {

                private int i = 0;

                @Override
                public boolean hasNext() {
                    return i < Storage.this.size();
                }

                @Override
                public StorageEntry next() {
                    final String key = Storage.this.getKey(i++);
                    return Storage.this.new StorageEntry(key);
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public Object[] toArray() {
            final int size = size();
            StorageEntry[] ret = new StorageEntry[size];
            int i = 0;
            for (Entry<String, String> e : this) {
                ret[i++] = (StorageEntry)e;
            }
            return ret;
        }

        @Override
        public <T> T[] toArray(T[] a) {
            a.getClass().getComponentType();
            // GWT does not support java.lang.reflect.Array.newInstance().
            @SuppressWarnings("unchecked")
            T[] ret = (T[])toArray();
            return ret;
        }

        /**
         * @return <code>true</code> if this set did not already contain the specified element.
         */
        @Override
        public boolean add(Entry<String, String> e) throws QuotaExceededException {
            String oldValue = Storage.this.put(e.getKey(), e.getValue());
            return oldValue == null || ! oldValue.equals(e.getValue());
        }

        @Override
        public boolean remove(Object obj) {
            if (obj == null) return false;
            if (! (obj instanceof Entry)) return false;
            Entry<?, ?> e = (Entry<?, ?>)obj;
            String value = Storage.this.get(e.getKey());
            if (value == null) return false;
            if (value.equals(e.getValue())) {
                Storage.this.remove(e.getKey());
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            for (Object obj : c) {
                if (! contains(obj)) {
                    return false;
                }
            }
            return true;
        }

        /**
         * @return <code>true</code> if this set changed as a result of the call.
         */
        @Override
        public boolean addAll(Collection<? extends Entry<String, String>> c) {
            boolean didChange = false;
            for (Entry<String, String> e : c) {
                didChange |= add(e);
            }
            return didChange;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        /**
         * @return <code>true</code> if this set changed as a result of the call.
         */
        @Override
        public boolean removeAll(Collection<?> c) {
            boolean didChange = false;
            for (Object obj : c) {
                didChange |= remove(obj);
            }
            return didChange;
        }

        @Override
        public void clear() {
            Storage.this.clear();
        }
        
    }

    private StorageImpl impl;
    private com.google.gwt.storage.client.Storage storage;

    public Storage(com.google.gwt.storage.client.Storage storage) {
        this.impl = GWT.create(StorageImpl.class);
        this.storage = storage;
    }

    @Override
    public int size() {
        return storage.getLength();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) throw new NullPointerException();
        if (! (key instanceof String)) return false;
        return containsKey((String)key);
    }

    public boolean containsKey(String key) {
        if (key == null) throw new NullPointerException();
        return storage.getItem(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) return false;
        for (String val : values()) {
            if (val.equals(value)) return true;
        }
        return false;
    }

    /**
     * Returns the key of the entry at index <code>i</code>.
     * 
     * @param i index of entry.
     * @return the key of the entry at index <code>i</code>.
     * @throws java.lang.ArrayIndexOutOfBoundsException if <code>i</code> is out of range.
     */
    public String getKey(int i) {
        if (i < 0 || storage.getLength() <= i) {
            throw new ArrayIndexOutOfBoundsException(i);
        }
        return storage.key(i);
    }

    @Override
    public String get(Object key) {
        if (key == null) throw new NullPointerException();
        if (! (key instanceof String)) return null;
        return storage.getItem((String)key);
    }

    @Override
    public String put(String key, String value) throws QuotaExceededException {
        if (key == null) throw new NullPointerException("key");
        if (value == null) throw new NullPointerException("value");
        final String oldValue = storage.getItem(key);
        try {
            storage.setItem(key, value);
        } catch (JavaScriptException ex) {
            if (impl.isQuotaExceededException(ex)) {
                throw new QuotaExceededException(ex.getMessage(), ex);
            }
        }
        return oldValue;
    }

    @Override
    public String remove(Object key) {
        if (key == null) throw new NullPointerException();
        if (key instanceof String) {
            return remove((String)key);
        } else return null;
    }

    public String remove(String key) {
        if (key == null) throw new NullPointerException();
        final String oldValue = storage.getItem(key);
        storage.removeItem(key);
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> map) throws QuotaExceededException {
        for (Entry<? extends String, ? extends String> e : map.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public KeySet keySet() {
        return this.new KeySet();
    }

    @Override
    public ValueCollection values() {
        return this.new ValueCollection();
    }

    @Override
    public EntrySet entrySet() {
        return this.new EntrySet();
    }

    public HandlerRegistration addStorageEventHandler(final StorageEvent.Handler handler) {
        return com.google.gwt.storage.client.Storage.addStorageEventHandler(new StorageEvent.Handler() {

            @Override
            public void onStorageChange(StorageEvent event) {
                if (storage.equals(event.getStorageArea())) {
                    handler.onStorageChange(event);
                }
            }
        });
    }
}
