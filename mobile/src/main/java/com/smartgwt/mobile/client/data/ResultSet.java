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

import java.util.Comparator;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.events.DataArrivedEvent;
import com.smartgwt.mobile.client.data.events.DataArrivedHandler;
import com.smartgwt.mobile.client.data.events.DataChangedEvent;
import com.smartgwt.mobile.client.data.events.HasDataArrivedHandlers;
import com.smartgwt.mobile.client.internal.Array;
import com.smartgwt.mobile.client.internal.HasReferenceCount;
import com.smartgwt.mobile.client.internal.data.CanGetCachedRow;
import com.smartgwt.mobile.client.internal.data.HasCriteria;
import com.smartgwt.mobile.client.internal.data.HasSortSpecifiers;
import com.smartgwt.mobile.client.internal.data.events.DSDataChangedEvent;
import com.smartgwt.mobile.client.internal.data.events.DSDataChangedHandler;
import com.smartgwt.mobile.client.rpc.RPCManager;
import com.smartgwt.mobile.client.types.DSOperationType;
import com.smartgwt.mobile.client.types.FetchMode;
import com.smartgwt.mobile.client.types.SortDirection;
import com.smartgwt.mobile.client.types.TextMatchStyle;
import com.smartgwt.mobile.client.util.Offline;
import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.widgets.Canvas;

public class ResultSet extends RecordList implements CanGetCachedRow<Record>, DSDataChangedHandler,
HasCriteria, HasDataArrivedHandlers, HasReferenceCount, HasSortSpecifiers {

    private int refCount = 0;

    private RecordList cache;
    private int cacheStartRow;
    private int cacheEndRow;
    private DSRequest context;
    private Criteria criteria;
    private DataSource dataSource;
    private FetchMode fetchMode;
    private TextMatchStyle textMatchStyle;
    private boolean updateCacheFromRequest = true;
    private boolean dropCacheOnUpdate = false;
    private boolean updatePartialCache = false;
    private boolean allCached;
    private boolean allMatchingCached;
    private boolean lengthKnown;
    private Boolean useClientSorting;
    private boolean useClientFiltering;
    private HandlerRegistration dsDataChangedRegistration;
    private SortSpecifier[] sortSpecifiers;

    public ResultSet(DataSource ds) {
        if (ds == null) throw new NullPointerException("`ds' cannot be `null'.");
        cache = new RecordList();
        cacheStartRow = 0;
        cacheEndRow = 0;
        dataSource = ds;
        fetchMode = FetchMode.BASIC;
        textMatchStyle = TextMatchStyle.SUBSTRING;
        allCached = false;
        allMatchingCached = false;
        lengthKnown = false;
        useClientFiltering = true;
    }

    public ResultSet(DataSource ds, List<? extends Record> initialData) {
        this(ds);
        cache.addAll(initialData);
        cacheStartRow = 0;
        cacheEndRow = initialData.size();
        allMatchingCached = true;
        allCached = true;
        lengthKnown = true;
        addAll(initialData);
    }

    @SGWTInternal
    public void _addRef() {
        if (refCount++ == 0) {
            // `ResultSet's register themselves with their attached `DataSource' to enable cache sync.
            assert dsDataChangedRegistration == null;
            dsDataChangedRegistration = dataSource._addDSDataChangedHandler(this);
        }
    }

    @SGWTInternal
    public void _unref() {
        assert refCount > 0 && dsDataChangedRegistration != null;
        if (--refCount == 0) {
            dsDataChangedRegistration.removeHandler();
            dsDataChangedRegistration = null;
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public void setAllCached() {
        allCached = true;
    }

    @SGWTInternal
    public void _setContext(DSRequest context) {
        this.context = context;
    }

    public final Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria newCriteria) {
        final Criteria oldCriteria = criteria;
        criteria = newCriteria;
        if (useClientFiltering && compareCriteria(newCriteria, oldCriteria) > 0) {
            // If we have a full cache and client filtering is switched on and the current
            // criteria is not an AdvancedCriteria, we can filter client-side
            if (allRowsCached() && useClientFiltering && (newCriteria == null || !newCriteria.isAdvanced())) {
                _filter();
            } else {
                invalidateCache();
            }
        }
    }

    protected int compareCriteria(Criteria newCriteria, Criteria oldCriteria) {
        return dataSource.compareCriteria(newCriteria, oldCriteria);
    }

    private final boolean shouldUseClientSorting() {
        if (!RPCManager._isOnLine()) return true;
        return _getUseClientSorting();
    }

    private final boolean canSortOnClient() {
        return shouldUseClientSorting() && (allMatchingRowsCached() || Canvas._booleanValue(Offline.isOffline(), false));
    }

    private void doSort() {
        final SortSpecifier[] specifiers = this.sortSpecifiers;

        if (isEmpty() || specifiers == null || specifiers.length == 0) return;

        if (canSortOnClient()) {
            _sortByProperties(specifiers);

            _maybeFireDataChangedEvent();

            return;
        }
    }

    private void fetchServerData() {
        DSRequest reqProperties = new DSRequest();
        reqProperties.copyAttributes(context);
        if (criteria != null) {
            reqProperties.setData(criteria);
        }
        if (sortSpecifiers != null) {
            reqProperties.setSortBy(sortSpecifiers);
        }
        dataSource.fetchData(criteria, getDSCallback(), reqProperties);
    }

    private void fetchServerData(int startRow, int endRow) {
        DSRequest reqProperties = new DSRequest();
        reqProperties.copyAttributes(context);
        if (criteria != null) {
            reqProperties.setData(criteria);
        }
        reqProperties.setStartRow((startRow >= 0) ? startRow : 0);
        if (endRow > startRow) {
            reqProperties.setEndRow(endRow);
        }
        if (sortSpecifiers != null) {
            reqProperties.setSortBy(sortSpecifiers);
        }
        dataSource.fetchData(criteria, getDSCallback(), reqProperties);
    }

    // Putting this defn inside a function so that the class remains serializable
    private DSCallback getDSCallback() {
        return new DSCallback() {
            // fetchRemoteDataReply?
            public void execute(DSResponse response, Object rawData, DSRequest request) {
                int startRow, endRow, totalRows;

                Integer i = response.getStartRow();
                if (i == null) {
                    startRow = 0;
                } else {
                    startRow = i.intValue();
                }

                i = response.getEndRow();
                if (i == null) {
                    endRow = response._getNumRecords();
                } else {
                    endRow = i.intValue();
                }

                i = response.getTotalRows();
                if (i == null) {
                    totalRows = response._getNumRecords();
                } else {
                    totalRows = i.intValue();
                }

                // We do not support cache with gaps - clear cache
                if (cacheStartRow > endRow || cacheEndRow < startRow) {
                    cache.clear();
                    final RecordList l = response.getRecordList();
                    if (l != null) cache.addAll(l);
                    cacheStartRow = startRow;
                    cacheEndRow = endRow;
                } else {
                    // Update cache with response data
                    if (startRow <= cacheStartRow) {
                        if (endRow < cacheEndRow) {
                            // End of response data overlaps with beginning of cached data
                            cache.removeRange(0, endRow - cacheStartRow);
                            final RecordList l = response.getRecordList();
                            if (l != null) cache.addAll(0, l);
                            cacheStartRow = startRow;
                        } else {
                            // Response data set overlap cached data completely
                            cache.clear();
                            final RecordList l = response.getRecordList();
                            if (l != null) cache.addAll(l);
                            cacheStartRow = startRow;
                            cacheEndRow = endRow;
                        }
                    } else {
                        if (endRow < cacheEndRow) {
                            // Response data overlaps part of cached data
                            cache.removeRange(startRow - cacheStartRow, endRow - cacheStartRow);
                            cache.addAll(startRow - cacheStartRow, response.getRecordList());
                        } else {
                            if (startRow < cacheEndRow) {
                                // Beginning of response data overlaps with end of cached data
                                cache.removeRange(startRow - cacheStartRow, cacheEndRow - cacheStartRow);
                            }
                            final RecordList l = response.getRecordList();
                            if (l != null) cache.addAll(l);
                            cacheEndRow = endRow;
                        }
                    }
                }
                if (cacheStartRow == 0 && cacheEndRow >= (totalRows - 1)) {
                    allMatchingCached = true;
                    allCached = true;
                } else {
                    allMatchingCached = false;
                    allCached = false;
                }
                lengthKnown = true;

                _startChangingData();
                try {
                    // for paged mode we add new data to already loaded so we should not clear data
                    if (ResultSet.this.fetchMode != FetchMode.PAGED) {
                        clear();
                    }
                    final RecordList l = response.getRecordList();
                    if (l != null) {
                        // splice in the new data. We need to make sure that there aren't two
                        // Records for the same index, so truncate localData to `startRow' records.
                        for (int ri = size(); ri > startRow; --ri) {
                            remove(ri - 1);
                        }
                        addAll(l);
                    }

                    if (allRowsCached() && useClientFiltering) _filter();

                    doSort();
                } finally {
                    // don't need to fire a `DataChangedEvent' because we will be firing a
                    // `DataArrivedEvent' instead.
                    --_dataChangeFlag;
                }

                dataArrived(startRow, endRow);

                context._setAfterFlowCallback(null);
            }
        };
    }

    @SGWTInternal
    public void _filter() {
        if (cache.size() < size()) {
            // Log.error("Client-side filtering invoked but allRows contains fewer records than " +
            //           "localData - invalidating obviously-broken cache");
            invalidateCache();
            return;
        }
        if (criteria == null) return;

        _startChangingData();
        try {
            clear();
            for (Record record : cache) {
                if (dataSource.recordMatchesCriteria(record, criteria, textMatchStyle)) {
                    add(record);
                }
            }
        } finally {
            _doneChangingData();
        }
    }

    public void invalidateCache() {
        _startChangingData();
        try {
            cache.clear();
            cacheEndRow = cacheStartRow = 0;
            allMatchingCached = false;
            allCached = false;
            lengthKnown = false;
            clear();
        } finally {
            --_dataChangeFlag;
            DataChangedEvent._fire(this, "invalidateCache", null, null, null, null);
        }
    }

    public boolean lengthIsKnown() {
        return lengthKnown;
    }

    public boolean allRowsCached() {
        return allCached;
    }

    public final boolean allMatchingRowsCached() {
        return allMatchingCached;
    }

    public FetchMode getFetchMode() {
        return fetchMode;
    }

    public void setFetchMode(FetchMode fetchMode) {
        this.fetchMode = fetchMode;
    }

    public TextMatchStyle getTextMatchStyle() {
        return textMatchStyle;
    }

    public void setTextMatchStyle(TextMatchStyle textMatchStyle) {
        this.textMatchStyle = textMatchStyle;
    }

    // NOTE: These two methods need to be much smarter if we ever update this code branch to
    // support partial caches
    @Override
    public Record get(int pos) {
        if (pos >= size()) {
            fetchServerData();
            Record placeholder = Array.LOADING;
            //for (int i = 0; i <= pos; i++) {
            //    localData.add(placeholder);
           // }
            return placeholder;
        } else {
            return super.get(pos);
        }
    }

    @Override
    public Record[] getRange(int start, int end) {
        if (!lengthKnown || end > size()) {
            if (end == 0) fetchServerData();
            else fetchServerData(start, end);
            Record placeHolder = Array.LOADING;
            Record[] rtn = new Record[end];
            for (int i = 0; i < end; i++) {
                rtn[i] = placeHolder;
            }
            //localData.addList(rtn);
            return rtn;
        } else {
            return subList(start, end).toArray(new Record[end - start]);
        }
    }

    public RecordList getRangeList(int start, int end) {
        if (end > size()) {
            fetchServerData();
            Record placeHolder = Array.LOADING;
            RecordList rtn = new RecordList();
            for (int i = 0; i < end; i++) {
                rtn.add(placeHolder);
            }
            //localData.addList(rtn);
            return rtn;
        } else {
            return subList(start, end);
        }
    }

    public boolean rowIsLoaded(int rowNum) {
        if (0 <= rowNum && rowNum < size()) {
            final Record row = get(rowNum);
            if (row != null && row != Array.LOADING) return true;
        }
        return false;
    }

    @Override
    @SGWTInternal
    public Record _getCachedRow(int rowNum) {
        if (0 <= rowNum && rowNum < size()) {
            final Record row = get(rowNum);
            if (row != null && row != Array.LOADING) return row;
        }
        return null;
    }

    public boolean rangeIsLoaded(int startRow, int endRow) {
        if (startRow > endRow) throw new IllegalArgumentException("startRow");
        assert startRow <= endRow;
        if (!(0 <= startRow && endRow <= size())) {
            return false;
        }
        for (int i = 0; i < endRow; ++i) {
            final Record row = get(i);
            if (row == null || row == Array.LOADING) return false;
        }
        return true;
    }

    public void dataArrived(int startRow, int endRow) {
        DataArrivedEvent.fire(this, startRow, endRow);
    }

    public ResultSet sortByProperty(String property) {
        return sortByProperty(property, true);
    }

    @Override
    public ResultSet sortByProperty(String property, boolean up) {
        return _sortByProperty(property, up, _getDefaultComparator());
    }

    @Override
    public ResultSet _sortByProperty(String property, boolean up, Comparator<Object> comparator) {
        sortSpecifiers = new SortSpecifier[] { new SortSpecifier(property, up ? SortDirection.ASCENDING : SortDirection.DESCENDING) };

        if (canSortOnClient()) {
            super._sortByProperty(property, up, comparator);
            DataChangedEvent._fire(this, null, null, null, null, null);
        } else {
            invalidateCache();
        }

        return this;
    }

    @Override
    public final SortSpecifier[] getSort() {
        return SortSpecifier._shallowClone(sortSpecifiers);
    }

    @Override
    public void setSort(SortSpecifier... sortSpecifiers) {
        if (sortSpecifiers == null) sortSpecifiers = new SortSpecifier[0];

        this.sortSpecifiers = sortSpecifiers;
        doSort();
    }

    public final Boolean getUseClientSorting() {
        return useClientSorting;
    }

    @SGWTInternal
    public final boolean _getUseClientSorting() {
        return Canvas._booleanValue(getUseClientSorting(), true);
    }

    public void setUseClientSorting(boolean useClientSorting) {
        this.useClientSorting = useClientSorting;
    }

    public void setUseClientFiltering(boolean useClientFiltering) {
        if (this.useClientFiltering != useClientFiltering) {
            this.useClientFiltering = useClientFiltering;
            // If we're no longer using client filtering, invalidate the cache because previous
            // client-side filtering may have filtered out some records.
            if (!useClientFiltering) invalidateCache();
        }
    }

    private final boolean getUseClientFiltering() {
        return useClientFiltering;
    }

    // Data Arrived

    @Override
    public HandlerRegistration addDataArrivedHandler(DataArrivedHandler handler) {
        return _ensureHandlers().addHandler(DataArrivedEvent.getType(), handler);
    }

    // Cache sync

    @SGWTInternal
    protected void _dataSourceDataChanged(DSRequest dsRequest, DSResponse dsResponse) {
        final Record[] updateData = dataSource.getUpdatedData(dsRequest, dsResponse, updateCacheFromRequest);

        _handleUpdate(dsRequest.getOperationType(), updateData, dsResponse.getInvalidateCache(), dsRequest);
    }

    @SGWTInternal
    protected boolean _shouldUpdatePartialCache() {
        return !RPCManager._isOnLine() || updatePartialCache;
    }

    @SGWTInternal
    protected void _handleUpdate(DSOperationType operationType, Record[] updateData, boolean forceCacheInvalidation, DSRequest dsRequest) {
        // invalidate the cache if explicitly told to...
        if (dropCacheOnUpdate || forceCacheInvalidation ||
            (operationType != DSOperationType.REMOVE &&
             !allMatchingRowsCached() &&
             _shouldUpdatePartialCache()))
        {
            invalidateCache();
            return;
        }


        _updateCache(operationType, updateData, dsRequest);

    }

    @SGWTInternal
    @SuppressWarnings("incomplete-switch")
    protected void _updateCache(DSOperationType operationType, Record[] updateData, DSRequest dsRequest) {
        operationType = operationType == null ? DSOperationType.FETCH : operationType;

        switch (operationType) {
            case FETCH:
                if (updateData == null || updateData.length == 0) DataChangedEvent._fire(this, null, null, null, null, null);
                break;
            case REMOVE:
                _removeCacheData(updateData, dsRequest);
                break;
            case ADD:
                _addCacheData(updateData, dsRequest);
                break;
            case UPDATE:
                _updateCacheData(updateData, dsRequest);
                break;
        }
    }

    @Override
    public void _onDSDataChanged(DSDataChangedEvent event) {
        _dataSourceDataChanged(event.getDSRequest(), event.getDSResponse());
    }

    @SGWTInternal
    protected boolean _updateCacheData(Record[] updateData, DSRequest dsRequest) {
        Record record = updateData[0];
        if (record == null) return false;

        final Record oldValues = dsRequest.getOldValues();
        final String pkName = dataSource.getPrimaryKeyFieldName();
        final String oldPKValue = oldValues == null ? null : oldValues.getAttribute(pkName);
        int index = -1;
        if (oldPKValue != null) {
            index = findIndex(pkName, oldPKValue);
        }
        if (oldPKValue == null || index == -1) {
            String pkValue = record.getAttribute(pkName);
            if (pkValue == null) {
                SC.logEcho(record, "syncUpdate:No value in returned record for primary key field '" + pkName + "'");
//              return false;
            }
            index = findIndex(pkName, pkValue);
        }

        boolean dataChanged = false;
        if (dataSource.recordMatchesCriteria(record, criteria, textMatchStyle)) {
            if (index != -1) {
                set(index, record);
                // If there is a sort order in force, make sure that this row moves to the
                // appropriate position
                if (sortSpecifiers != null && sortSpecifiers.length > 0) {
                    insertIntoCache(record, index);
                }
                dataChanged = true;
            } else {
                // The record has changed such that it now matches this ResultSet's filter;
                // If we have a full local cache (at the moment, we always do), insert it into
                // the cache at the appropriate point; otherwise, just add it to the end
                if (allMatchingRowsCached()) {
                    insertIntoCache(record);
                } else {
                    add(record);
                }
                dataChanged = true;
            }
        } else {
            if (index != -1) {
                // The record is in the local cache but no longer matches the filter, so chuck
                // it out
                remove(index);
                dataChanged = true;
            } else {
                // The record wasn't in the cache before, and it still shouldn't be - nothing
                // to do
            }
        }
        return dataChanged;
    }

    @SGWTInternal
    protected boolean _addCacheData(Record[] newRows, DSRequest _) {
        final Record record = newRows[0];
        final String pkName = dataSource.getPrimaryKeyFieldName();
        String pkValue = record.getAttribute(pkName);
        if (pkValue == null) {
            SC.logEcho(record, "syncAdd:No value in returned record for primary key field '" + pkName + "'");
//            return false;
        }
        int index = findIndex(pkName, pkValue);

        if (index != -1) {
//            Log.warn("cacheSync", "Found an existing record with matching primaryKey value "
//                    + pkValue + " during cache sync of an 'add' operation");
            return false;
        }

        if (dataSource.recordMatchesCriteria(record, criteria, textMatchStyle)) {
            // The record matches this ResultSet's filter; if we have a full local cache (at
            // the moment, we always do), insert it into the cache at the appropriate point;
            // otherwise, just add it to the end
            if (allMatchingRowsCached()) {
                insertIntoCache(record);
            } else {
                add(record);
            }
            return true;
        }

        return false;
    }

    @SGWTInternal
    protected boolean _removeCacheData(Record[] updateData, DSRequest _) {
        if (updateData == null || updateData.length == 0) return false;
        final Record record = updateData[0];
        final String pkName = dataSource.getPrimaryKeyFieldName();
        Object pkValue = record.getAttribute(pkName);
        if (pkValue == null) {
            SC.logEcho(record, "syncRemove:No value in returned record for primary key field '" + pkName + "'");
//            return false;
        }
        int index = findIndex(pkName, pkValue);

        if (index != -1) {
            remove(index);
            return true;
        }

        // Else the local cache didn't contain the removed record, nothing to do
        return false;
    }

    protected void insertIntoCache(Record record) {
        insertIntoCache(record, -1);
    }

    @SuppressWarnings("unchecked")
    protected void insertIntoCache(Record record, int existingPosition) {
        boolean inserted = false;

        if (sortSpecifiers != null && sortSpecifiers.length != 0) {
            @SuppressWarnings("rawtypes")
            final Comparator comp = _getDefaultComparator();
            outer: for (int i = 0; i < size(); i++) {
                final Record existing = get(i);
                if (record == existing) continue;
                for (int j = 0; j < sortSpecifiers.length; ++j) {
                    final SortSpecifier sortSpecifier = sortSpecifiers[j];
                    final boolean up = sortSpecifier._isUp();
                    final String sortBy = sortSpecifier.getField();
                    if ((comp.compare(existing.get(sortBy), record.get(sortBy)) > 0) != up) continue outer;
                }
                if (existingPosition != -1) {
                    if (existingPosition > i) {
                        remove(existingPosition);
                        add(i, record);
                    } else {
                        add(i, record);
                        remove(existingPosition);
                    }
                } else {
                    add(i, record);
                }
                inserted = true;
                break;
            }
        }

        if (!inserted) {
            if (existingPosition != -1) remove(existingPosition);
            add(record);
        }
    }
}
