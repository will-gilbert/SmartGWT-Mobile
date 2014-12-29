/*
 * SmartGWT Mobile
 * Copyright 2008 and beyond, Isomorphic Software, Inc.
 *
 * SmartGWT Mobile is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  SmartGWT Mobile is also
 * available under typical commercial license terms - see
 * http://smartclient.com/license
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package com.smartgwt.mobile.client.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.events.DataChangedEvent;
import com.smartgwt.mobile.client.data.events.DataChangedHandler;
import com.smartgwt.mobile.client.data.events.HasDataChangedHandlers;

public class RecordList extends ArrayList<Record> implements HasDataChangedHandlers {

    private HandlerManager handlerManager = null;

    @SGWTInternal
    protected int _dataChangeFlag = 0;

    public RecordList() {
    }

    public RecordList(Collection<? extends Record> records) {
        addAll(records);
    }
    
    /**
     * Construct a new RecordList containing the supplied records
     * @param records
     */
    public RecordList(Record... records) {
        addAll(Arrays.asList(records));
    }

    /**
     * Construct a new RecordList containing the specified subset of the supplied records.  Note
     * that the range is non-inclusive at the end
     * @param records
     * @param start
     * @param end
     */
    public RecordList(Record[] records, int start, int end) {
        addAll(Arrays.asList(records).subList(start, end));
    }

    /**
     * Construct a new RecordList containing the same records as the supplied RecordList; the
     * new RecordList is effectively a copy of the supplied RecordList
     * @param recordList
     */
    public RecordList(RecordList recordList) {
        addAll(recordList);
    }

    /**
     * Construct a new RecordList containing the specified subset of the Records contained in
     * the supplied RecordList.  Note that the range is non-inclusive at the end
     * @param recordList
     * @param start
     * @param end
     */
    public RecordList(RecordList recordList, int start, int end) {
        addAll(recordList.subList(start, end));
    }

    @SGWTInternal
    public final void _startChangingData() {
        ++_dataChangeFlag;
    }

    @SGWTInternal
    public final void _maybeFireDataChangedEvent() {
        if (_dataChangeFlag == 0) DataChangedEvent._fire(this, null, null, null, null, null);
    }

    @SGWTInternal
    public final void _doneChangingData() {
        --_dataChangeFlag;
        _maybeFireDataChangedEvent();
    }

    /**
     * Returns a Record array made up of the Records in this list inside the range denoted by
     * the start and end parameters (non-inclusive at the end).
     * @param start  First Record to include in the subset
     * @param end  Include every Record up to (but not including) this one
     * @return  The requested range, as a Record array
     */
    public Record[] getRange(int start, int end) {
        if (start < 0) start = 0;
        if (end > size()) end = size();
        Record[] records = new Record[end-start];
        for (int i = 0; i < end-start; i++) {
            records[i] = get(i);
        }
        return records;
    }

    /**
     * Returns a RecordList made up of the Records in this list inside the range denoted by
     * the start and end parameters (non-inclusive at the end).  Note that the returned object
     * is a copy, not a view over the original list, though it is a shallow copy (so the actual
     * Records involved are the same Records as in the original list)
     *
     * @param start  First Record to include in the subset
     * @param end  Include every Record up to (but not including) this one
     * @return  The requested range, as a RecordList
     */
    @Override
    public RecordList subList(int start, int end) {
        if (start < 0) start = 0;
        if (end > size()) end = size();
        final RecordList ret = new RecordList();
        ret.addAll(super.subList(start, end));
        return ret;
    }

    // Finding and Sorting

    /**
     * Finds index of first Record in the list where the value of the parameter property matches the
     * parameter value.
     * @param property  The name of the property to find by
     * @param value  The property value to match
     * @return  The index of first matching Record, or -1 if there is no such record
     */
    protected int findIndex(String property, Object value) {
        return findIndex(property, value, 0, size());
    }

    /**
     * Finds index of first Record where the value of the parameter property matches the
     * parameter value in a range from startIndex to the end of the list.
     * @param property  The name of the property to find by
     * @param value  The property value to match
     * @param start  The index of the first Record to include in the search
     * @return  The index of first matching Record, or -1 if there is no such record
     */
    protected int findIndex(String property, Object value, int start) {
        return findIndex(property, value, 0, size());
    }

    /**
     * Finds index of first Record where the value of the parameter property matches the
     * parameter value in a range from startIndex to endIndex.
     * @param property  The name of the property to find by
     * @param value  The property value to match
     * @param start  The index of the first Record to include in the search
     * @param end  The index of the end of the range (non-inclusive)
     * @return  The index of first matching Record, or -1 if there is no such record
     */
    protected int findIndex(String property, Object value, int start, int end) {
        if (start < 0) {
            start = 0;
        }
        if (end > size()) {
            end = size();
        }
        for (int i = start; i < end; i++) {
            if (get(i) == null) {
                continue;
            }
            if (get(i).get(property) == null) {
                if (value == null) {
                    return i;
                }
                continue;
            }
            if (get(i).get(property).equals(value)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Find the index of the first Record in the list where the values of all the properties in
     * the parameter Map match the corresponding property in the Record
     * @param properties  A Map of property/value pairs to match
     * @return  The index of the first matching Record, or -1 if there is no such record
     */
    protected int findIndex(Map<String, ?> properties) {
        return findIndex(properties, 0, size());
    }

    /**
     * Find the index of the first Record where the values of all the properties in
     * the parameter Map match the corresponding property in the Record in a range
     * from startIndex to the end of the list.
     * @param properties  A Map of property/value pairs to match
     * @param start  The index of the first Record to include in the search
     * @return  The first matching Record, or null if there is no such record
     */
    protected int findIndex(Map<String, ?> properties, int start) {
        return findIndex(properties, 0, size());
    }

    /**
     * Find the index of the first Record where the values of all the properties in
     * the parameter Map match the corresponding property in the Record in a range
     * from startIndex to endIndex.
     * @param properties  A Map of property/value pairs to match
     * @param start  The index of the first Record to include in the search
     * @param end  The index of the end of the range (non-inclusive)
     * @return  The first matching Record, or null if there is no such record
     */
    public int findIndex(Map<String, ?> properties, int start, int end) {
        if (start < 0) {
            start = 0;
        }
        if (end > size()) {
            end = size();
        }
        if (properties == null || properties.keySet().isEmpty()) {
            return -1;
        }
        for (int i = start; i < end; i++) {
            boolean failed = false;
            if (get(i) != null) {
                for (String property : properties.keySet()) {
                    if (get(i).get(property) == null) {
                        if (properties.get(property) == null) {
                            continue;
                        }
                        failed = true;
                        break;
                    }
                    if (!get(i).get(property).equals(properties.get(property))) {
                        failed = true;
                        break;
                    }
                }
            }
            if (!failed) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Find the first Record in the list where the value of the parameter property matches the
     * parameter value
     * @param property  The name of the property to find by
     * @param value  The property value to match
     * @return  The first matching Record, or null if there is no such record
     */
    public Record find(String property, Object value) {
        return find(property, value, 0, size());
    }

    /**
     * Find the first Record where the value of the parameter property matches the
     * parameter value a range from startIndex to the end of the list
     * @param property  The name of the property to find by
     * @param value  The property value to match
     * @param start  The index of the first Record to include in the search
     * @return  The first matching Record, or null if there is no such record
     */
    public Record find(String property, Object value, int start) {
        return find(property, value, start, size());
    }

    /**
     * Find the first Record where the value of the parameter property matches the
     * parameter value a range from startIndex to endIndex
     * @param property  The name of the property to find by
     * @param value  The property value to match
     * @param start  The index of the first Record to include in the search
     * @param end  The index of the end of the range (non-inclusive)
     * @return  The first matching Record, or null if there is no such record
     */
    protected Record find(String property, Object value, int start, int end) {
        int index = findIndex(property, value, start, end);
        return index == -1 ? null : get(index);
    }

    /**
     * Find the first Record in the list where the values of all the properties in the parameter
     * Map match the corresponding property in the Record
     * @param properties  A Map of property/value pairs to match
     * @return  The first matching Record, or null if there is no such record
     */
    public Record find(Map<String, ?> properties) {
        int index = findIndex(properties);
        return index == -1 ? null : get(index);
    }

    /**
     * Like find(), but inspects a range from startIndex to the end of the list
     * @param properties  A Map of property/value pairs to match
     * @param start  The index of the first Record to include in the search
     * @return  The first matching Record, or null if there is no such record
     */
    public Record find(Map<String, ?> properties, int start) {
        int index = findIndex(properties, start, size());
        return index == -1 ? null : get(index);
    }

    /**
     * Like find(), but inspects a range from startIndex to endIndex
     * @param properties  A Map of property/value pairs to match
     * @param start  The index of the first Record to include in the search
     * @param end  The index of the end of the range (non-inclusive)
     * @return  The first matching Record, or null if there is no such record
     */
    public Record find(Map<String, ?> properties, int start, int end) {
        int index = findIndex(properties, start, end);
        return index == -1 ? null : get(index);
    }

    /**
     * Sort the RecordList in-place, according to the value of the supplied property on each
     * Record, using the default comparator
     * @param property  The name of the property to sort by
     * @param up  If true, sort in ascending order; otherwise in descending order
     * @return  The sorted RecordList itself
     */
    public RecordList sortByProperty(String property, boolean up) {
        return _sortByProperty(property, up, _getDefaultComparator());
    }

    /**
     * Sort the RecordList in-place, according to the value of the supplied property on each
     * Record, using the supplied comparator
     * @param property  The name of the property to sort by
     * @param up  If true, sort in ascending order; otherwise in descending order
     * @param comparator  A Comparator that is able to provide meaningful comparisons between
     * two values of the property being sorted by
     * @return  The sorted RecordList itself
     */
    @SGWTInternal
    public RecordList _sortByProperty(String property, boolean up, Comparator<Object> comparator) {
        int size = size();
        for (int outer = 0; outer < size - 1; outer++) {
            boolean swapped = false;
            for (int inner = 0; inner < size - outer - 1; inner++) {
                Object o1 = get(inner).get(property);
                Object o2 = get(inner + 1).get(property);
                if ((comparator.compare(o1, o2) > 0) == up) {
                    swap(inner, inner + 1);
                    swapped = true;
                }
            }
            // If we went through a whole iter without needing to swap, the list is sorted
            if (!swapped) {
                break;
            }
        }
        return this;
    }

    @SGWTInternal
    public RecordList _sortByProperties(SortSpecifier[] specifiers) {
        return _sortByProperties(specifiers, _getDefaultComparator());
    }

    @SGWTInternal
    public RecordList _sortByProperties(final SortSpecifier[] specifiers, final Comparator<Object> comparator) {
        if (specifiers == null || specifiers.length == 0) return this;
        Collections.sort(this, new Comparator<Record>() {
            @Override
            public int compare(Record lhs, Record rhs) {
                if (lhs == null) return (rhs == null ? 0 : -1);
                else if (rhs == null) return 1;
                for (final SortSpecifier specifier : specifiers) {
                    final String property = specifier.getField();
                    final Object l = lhs.getAttributeAsObject(property),
                            r = rhs.getAttributeAsObject(property);
                    final int c = comparator.compare(l, r);
                    if (c != 0) return (specifier._isUp() ? c : -c);
                }
                return 0;
            }
        });
        return this;
    }

    // Wrapping this in a method in the interests of keeping the class serializable
    @SGWTInternal
    public Comparator<Object> _getDefaultComparator() {
        return new Comparator<Object>() {
            @SuppressWarnings({"rawtypes", "unchecked"})
            public int compare(Object o1, Object o2) {
                double val1 = 0;
                double val2 = 0;
                if (o1 == null) {
                    return o2 == null ? 0 : -1;
                } else if (o2 == null) {
                    return 1;
                }
                if (o1 instanceof Comparable && o2 instanceof Comparable) {
                    return ((Comparable) o1).compareTo((Comparable) o2);
                }
                if (o1 instanceof Number) {
                    val1 = ((Number) o1).doubleValue();
                    val2 = ((Number) o2).doubleValue();
                }
                double result = val1 - val2;
                return result == 0 ? 0 : result > 0 ? 1 : -1;
            }
        };
    }

    /**
     * Swap the positions of two Records in the list
     * @param index1
     * @param index2
     */
    public void swap(int index1, int index2) {
        final Record tmp = get(index1);
        if (index1 != index2) {
            super.set(index1, get(index2));
            super.set(index2, tmp);
            _maybeFireDataChangedEvent();
        }
    }

    // Overrides for DataChanged purposes
    @Override
    public boolean add(Record record) {
        super.add(record);
        _maybeFireDataChangedEvent();
        return true;
    }

    @Override
    public void add(int index, Record record) {
        super.add(index, record);
        _maybeFireDataChangedEvent();
    }

    @Override
    public boolean addAll(Collection<? extends Record> c) {
        if (super.addAll(c)) {
            _maybeFireDataChangedEvent();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends Record> c) {
        if (super.addAll(index, c)) {
            _maybeFireDataChangedEvent();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Record remove(int index) {
        final Record ret = super.remove(index);
        _maybeFireDataChangedEvent();
        return ret;
    }

    @Override
    public boolean remove(Object o) {
        if (super.remove(o)) {
            _maybeFireDataChangedEvent();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void removeRange(int start, int end) {
        super.removeRange(start, end);
        if (start != end) _maybeFireDataChangedEvent();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (super.removeAll(c)) {
            _maybeFireDataChangedEvent();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (super.retainAll(c)) {
            _maybeFireDataChangedEvent();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Record set(int index, Record record) {
        final Record ret = super.set(index, record);
        if (ret != record) _maybeFireDataChangedEvent();
        return ret;
    }

    @Override
    public void clear() {
        final boolean wasEmpty = isEmpty();
        super.clear();
        if (!wasEmpty) _maybeFireDataChangedEvent();
    }

    @SGWTInternal
    protected HandlerManager _createHandlerManager() {
        return new HandlerManager(this);
    }

    @SGWTInternal
    protected final HandlerManager _ensureHandlers() {
        if (handlerManager == null) {
            handlerManager = _createHandlerManager();
        }
        return handlerManager;
    }

    @Override
    public HandlerRegistration addDataChangedHandler(DataChangedHandler handler) {
        return _ensureHandlers().addHandler(DataChangedEvent.getType(), handler);
    }

    @Override
    public final void fireEvent(GwtEvent<?> event) {
        if (handlerManager != null) {
            handlerManager.fireEvent(event);
        }
    }
}