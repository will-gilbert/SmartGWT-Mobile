package com.smartgwt.mobile.client.internal;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.ResultSet;
import com.smartgwt.mobile.client.data.events.DataArrivedEvent;
import com.smartgwt.mobile.client.data.events.DataArrivedHandler;
import com.smartgwt.mobile.client.data.events.DataChangedEvent;
import com.smartgwt.mobile.client.data.events.DataChangedHandler;
import com.smartgwt.mobile.client.data.events.HasDataArrivedHandlers;
import com.smartgwt.mobile.client.data.events.HasDataChangedHandlers;
import com.smartgwt.mobile.client.internal.data.CanGetCachedRow;
import com.smartgwt.mobile.client.internal.types.CascadingDirection;
import com.smartgwt.mobile.client.util.SC;

@SGWTInternal
public abstract class Selection<I extends SelectionItem> implements HasSelectedSetHandlers {

    protected static int _selectionID = 0;

    private static boolean isFalse(Object obj) {
        if (obj instanceof Boolean && ((Boolean)obj).booleanValue() == false) return true;
        if (obj instanceof Number && ((Number)obj).floatValue() == 0) return true;
        if (obj instanceof CharSequence && ((CharSequence)obj).length() == 0) return true;
        return false;
    }

    private HandlerManager handlerManager = null;
    private String selectionProperty;
    private String partialSelectionProperty;
    private List</*? extends */I> data;
    private HandlerRegistration dataArrivedRegistration = null,
            dataChangedRegistration = null;
    private String enabledProperty = "enabled";
    private String canSelectProperty = "canSelect";
    private boolean cascadeSelection = false;
    private boolean useRemoteSelection = false;
    private boolean dirty = true;
    private boolean cachingSelection = false;
    private boolean settingSelected = false;
    private boolean supressCaching = false;
    private List<I> cache;

    public <D extends List</*? extends */I> & HasDataChangedHandlers> Selection(D data) {
        selectionProperty = "_selection_" + (_selectionID++);
        partialSelectionProperty = "_partial" + selectionProperty;
        _setData(data);
    }

    public void _destroy() {
        if (data != null) {
            _ignoreData(data);
            data = null;
        }
    }

    public final String getCanSelectProperty() {
        return canSelectProperty;
    }

    public void setCanSelectProperty(String canSelectProperty) {
        this.canSelectProperty = canSelectProperty;
    }

    protected <D extends List</*? extends */I> & HasDataChangedHandlers> void _setData(D newData) {
        if (data != null) {
            _ignoreData(data);
        }
        data = newData;
        if (newData != null) {
            _observeData(newData);
        }
        _markForRedraw();
    }

    protected <D extends List</*? extends */I> & HasDataChangedHandlers> void _observeData(D data) {
        if (data != null) {
            assert dataChangedRegistration == null;
            dataChangedRegistration = data.addDataChangedHandler(new DataChangedHandler() {
                @Override
                public void onDataChanged(DataChangedEvent event) {
                    _dataChanged();
                }
            });
            if (data instanceof HasDataArrivedHandlers) {
                assert dataArrivedRegistration == null;
                dataArrivedRegistration = ((HasDataArrivedHandlers)data).addDataArrivedHandler(new DataArrivedHandler() {
                    @Override
                    public void onDataArrived(DataArrivedEvent event) {
                        _dataChanged();
                    }
                });
            }
        }
    }

    protected void _ignoreData(List</*? extends */I> data) {
        if (data == null) {
            return;
        }
        if (dataArrivedRegistration != null) {
            dataArrivedRegistration.removeHandler();
            dataArrivedRegistration = null;
        }
        if (dataChangedRegistration != null) {
            dataChangedRegistration.removeHandler();
            dataChangedRegistration = null;
        }
    }

    protected void _dataChanged() {
        _markForRedraw();
    }

    protected void _markForRedraw() {
        dirty = true;
    }

    public final boolean isSelected(I item) {
        if (dirty) {
            _cacheSelection();
        }

        if (item == null) {
            return false;
        }
        final Object value = item.getAttributeAsObject(selectionProperty);
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        return value != null;
    }

    public final boolean isPartiallySelected(I item) {
        if (dirty) {
            _cacheSelection();
        }

        if (item == null) {
            return false;
        }
        final Object value = item.getAttributeAsObject(partialSelectionProperty);
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        return value != null;
    }

    public boolean anySelected() {
        return getSelection().length > 0;
    }

    public boolean multipleSelected() {
        return getSelection().length > 1;
    }

    public I[] getSelection() {
        return getSelection(false);
    }

    public I[] getSelection(boolean excludePartialSelections) {
        // if the selection is dirty, cache it again
        if (dirty) {
            if (cachingSelection) cachingSelection = false;
            _cacheSelection();
        }
        assert cache != null;

        List<I> selection = cache;

        assert selection != null;
        if (excludePartialSelections && selection.size() > 0) {
            List<I> cache = this.cache;
            assert cache != null;
            selection = new ArrayList<I>();

            // Cache includes both fully and partially selected nodes.
            final int cache_size = cache.size();
            for (int i = 0; i < cache_size; ++i) {
                final I item = cache.get(i);
                if (!isPartiallySelected(item)) {
                    selection.add(item);
                }
            }
        }

        return selection.toArray(_createSelectionItemArray(selection.size()));
    }

    protected abstract I[] _createSelectionItemArray(int length);

    public I getSelectedRecord() {
        final I[] selection = getSelection();
        assert selection != null;
        if (selection.length > 0) {
            return selection[0];
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    protected void _cacheSelection() {
        if (cachingSelection || settingSelected || supressCaching) return;

        cache = new ArrayList<I>();

        List</*? extends */I> data = getItemList();
        if (data == null) return;
        final int length = data.size();

        if (data instanceof ResultSet && !((ResultSet)data).lengthIsKnown()) {
            dirty = false;
            return;
        }
        cachingSelection = true;

        boolean delayCache = false;
        for (int i = 0; i < length; ++i) {
            final I item;
            if (data instanceof CanGetCachedRow) {
                item = ((CanGetCachedRow<I>)data)._getCachedRow(i);
            } else {
                item = data.get(i);
            }
            if (item != null && isSelected(item)) {
                if (cascadeSelection && !isPartiallySelected(item)) {
                    setSelected(item, true, null, true);
                    delayCache = true;
                }
                if (!delayCache) {
                    cache.add(item);
                }
            }
        }
        if (delayCache) {
            cache.clear();
            for (int i = 0; i < length; ++i) {
                final I item = ((CanGetCachedRow<I>)data)._getCachedRow(i);
                if (item != null && isSelected(item)) {
                    cache.add(item);
                }
            }
        }

        cachingSelection = false;
        dirty = false;
    }

    public final boolean setSelected(SelectionItem item, boolean newState) {
        return setSelected(item, newState, null, false);
    }

    public boolean setSelected(SelectionItem item, boolean newState, CascadingDirection cascadingDirection, boolean recalculate) {
        if (item == null) return false;
        if (data == null || (data instanceof Destroyable && ((Destroyable)data)._isDestroyed())) return false;

        if (isFalse(item.getAttributeAsObject(enabledProperty))) return false;
        if (isFalse(item.getAttributeAsObject(canSelectProperty))) return false;

        final boolean settingSelected = this.settingSelected;
        this.settingSelected = true;

        final String property = this.selectionProperty;
        final String partialProperty = this.partialSelectionProperty;

        //Object partialValue = null;
        Object oldPartialValue = item.getAttributeAsObject(partialProperty);
        if (oldPartialValue == null) oldPartialValue = Boolean.FALSE;
        else if (!(oldPartialValue instanceof Boolean)) oldPartialValue = Boolean.valueOf(!isFalse(oldPartialValue));
        assert oldPartialValue != null;
        assert oldPartialValue instanceof Boolean;

        if (cascadeSelection && !useRemoteSelection) {
            if (cascadingDirection == CascadingDirection.UP) {

            }
        }

        Object oldState = item.getAttributeAsObject(property);
        if (oldState == null) oldState = Boolean.FALSE;
        else if (!(oldState instanceof Boolean)) oldState = Boolean.valueOf(!isFalse(oldState));
        assert oldState != null;
        assert oldState instanceof Boolean;

        item.setAttribute(property, newState);

        final SelectionItem lastSelectionItem = item;
        final boolean lastSelectionState = newState;
        final boolean lastSelectionPreviousState = ((Boolean)oldState).booleanValue();
        //final Object lastSelectionPartialValue = partialValue;
        //final boolean lastSelectionPreviousPartialValue = ((Boolean)oldPartialValue).booleanValue();

        Object newPartialValue = item.getAttributeAsObject(partialProperty);
        boolean changed = true;
        if (newState == ((Boolean)oldState).booleanValue() && !isFalse(newPartialValue) == ((Boolean)oldPartialValue).booleanValue()) {
            changed = false;
        }
        if (!recalculate && changed == false) {
            if (!settingSelected) this.settingSelected = false;
            SelectedSetEvent.fire(this, lastSelectionItem, lastSelectionState, lastSelectionPreviousState);
            return false;
        }

        _markForRedraw();

        if (!settingSelected) this.settingSelected = false;

        SelectedSetEvent.fire(this, lastSelectionItem, lastSelectionState, lastSelectionPreviousState);
        return true;
    }

    public final boolean select(SelectionItem item) {
        return setSelected(item, true);
    }

    public final boolean deselect(SelectionItem item) {
        return setSelected(item, false);
    }

    public final boolean selectSingle(SelectionItem item) {
        boolean itemWasSelected, othersWereSelected;

        itemWasSelected = deselect(item);
        othersWereSelected = deselectAll();
        select(item);

        return !itemWasSelected || othersWereSelected;
    }

    public final boolean selectList(I[] list) {
        return selectList(list, true);
    }

    public boolean selectList(I[] list, boolean newState) {
        if (list == null) return false;

        if (dirty) {
            _cacheSelection();
        }

        final List<I> cache = this.cache;
        int length = list.length;
        final List<I> selectionChanged = new ArrayList<I>();
        final ArrayList<Integer> currentIndex = new ArrayList<Integer>();
        for (int i = 0; i < length; ++i) {
            final I item = list[i];
            final boolean selected = isSelected(item);
            if (selected == newState) continue;

            selectionChanged.add(item);
            if (!newState) {
                currentIndex.ensureCapacity(selectionChanged.size());
                for (int j = currentIndex.size(); j < selectionChanged.size() - 1; ++j) {
                    currentIndex.add(null);
                }
                currentIndex.add(cache.indexOf(item));
            }
        }

        final boolean orig_supressCaching = this.supressCaching;
        this.supressCaching = true;

        boolean anyChanged = false;
        length = selectionChanged.size();

        for (int i = 0; i < length; ++i) {
            final I item = selectionChanged.get(i);

            if (newState) {
                cache.add(item);
            } else {
                Integer previousIndexInCache = currentIndex.get(i);
                assert previousIndexInCache != null;
                cache.remove(previousIndexInCache.intValue() - i);
            }

            anyChanged = setSelected(item, newState) || anyChanged;
        }

        this.supressCaching = orig_supressCaching;
        _cacheSelection();

        return anyChanged;
    }

    public boolean deselectList(I[] list) {
        return selectList(list, false);
    }

    public boolean selectAll() {
        return selectRange(0, getItemList().size());
    }

    public boolean deselectAll() {
        return deselectList(getSelection());
    }

    public boolean selectItem(int position) {
        return selectRange(position, position + 1);
    }

    public boolean deselectItem(int position) {
        return deselectRange(position, position + 1);
    }

    public final boolean selectRange(int start, int end) {
        return selectRange(start, end, true);
    }

    public boolean selectRange(int start, int end, boolean newState) {
        if (start > end) throw new IllegalArgumentException("start");
        List</*? extends */I> data = this.data;

        if ((data instanceof ResultSet && !((ResultSet)data).rangeIsLoaded(start, end)) ||
            !(0 <= start && end <= data.size()))
        {
            SC.logWarn("Can't select that many records at once. Please try working in smaller batches.");
        }

        final List</*? extends */I> range = data.subList(start, end);
        return selectList(range.toArray(_createSelectionItemArray(range.size())), newState);
    }

    public final boolean deselectRange(int start, int end) {
        return selectRange(start, end, false);
    }

    public List</*? extends */I> getItemList() {
        return data;
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
    public HandlerRegistration _addSelectedSetHandler(SelectedSetHandler handler) {
        return _ensureHandlers().addHandler(SelectedSetEvent.getType(), handler);
    }

    @Override
    public final void fireEvent(GwtEvent<?> event) {
        if (handlerManager != null) {
            handlerManager.fireEvent(event);
        }
    }
}
