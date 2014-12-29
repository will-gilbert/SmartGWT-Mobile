package com.smartgwt.mobile.client.widgets.form.fields;

import com.smartgwt.mobile.client.theme.FormCssResource;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;

public class SearchItem extends TextItem {

    private static final FormCssResource CSS = DynamicForm._CSS;

    /**
     * <code>sc-search-item</code>, the CSS class name that is added to the <code>SearchItem</code>
     * {@link com.google.gwt.user.client.ui.UIObject#getElement() element}.
     */
    public static final String COMPONENT_CLASS_NAME = CSS.searchItemClass();

    private boolean showSearchIcon = true;

    public SearchItem(String name) {
        super(name);
        super.setTextBoxStyle(COMPONENT_CLASS_NAME);
        getElement().addClassName(CSS.showSearchIconClass());
    }

    public SearchItem(String name, String title) {
        this(name);
        super.setTitle(title);
    }

    public SearchItem(String name, String title, String hint) {
        this(name, title);
        super.setHint(hint);
    }

    /**
     * Whether to show a search icon (typically a magnifying glass).
     * 
     * @return whether to show a search icon.
     */
    public final boolean getShowSearchIcon() {
        return showSearchIcon;
    }

    public void setShowSearchIcon(Boolean showSearchIcon) {
        this.showSearchIcon = showSearchIcon == null ? true : showSearchIcon.booleanValue();
        if (this.showSearchIcon) getElement().addClassName(CSS.showSearchIconClass());
        else getElement().removeClassName(CSS.showSearchIconClass());
    }
}
