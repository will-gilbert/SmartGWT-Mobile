package com.smartgwt.mobile.client.theme;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.Shared;

@Shared
public interface FormCssResource extends CssResource {

    @ClassName("sc-form")
    public String formClass();

    @ClassName("formStyle1")
    public String style1FormClass();

    @ClassName("formStyle2")
    public String style2FormClass();

    @ClassName("formItemHasInvalidValue")
    public String formItemHasInvalidValueClass();

    @ClassName("sc-error")
    public String errorLabelClass();

    @ClassName("sc-formitem-label-cell")
    public String labelCellClass();

    @ClassName("sc-formitem-cell")
    public String formItemCellClass();

    @ClassName("placeholderText")
    public String placeholderTextClass();

    // The CSS style name applied to native dateItems. Though, because native <input type="date">
    // elements do not support the `placeholder' attribute, this style name is actually applied
    // to a DIV wrapping the native input element and a SPAN (style name placeholderTextClass())
    // containing the hint.
    @ClassName("dateItem")
    public String dateItemClass();

    @ClassName("datetimeItem")
    public String datetimeItemClass();

    @ClassName("sc-search-item")
    public String searchItemClass();

    @ClassName("showSearchIcon")
    public String showSearchIconClass();

    @ClassName("sc-spaceritem")
    public String spacerItemClass();

    @ClassName("staticTextItem")
    public String staticTextItemClass();

    @ClassName("textItem")
    public String textItemClass();

    @ClassName("timeItem")
    public String timeItemClass();

    @ClassName("uploadItem")
    public String uploadItemClass();

    @ClassName("uploadPreviewImage")
    public String uploadPreviewImageClass();

    @ClassName("firstFormRowOrCell")
    public String firstFormRowOrCellClass();

    @ClassName("lastFormRowOrCell")
    public String lastFormRowOrCellClass();

    @ClassName("followedBySpacerItemFormRow")
    public String followedBySpacerItemFormRowClass();
    @ClassName("spacerItemFormRow")
    public String spacerItemFormRowClass();

    @ClassName("searchItemLabelCell")
    public String searchItemLabelCellClass();
    @ClassName("sliderItemLabelCell")
    public String sliderItemLabelCellClass();
    @ClassName("switchItemLabelCell")
    public String switchItemLabelCellClass();
    @ClassName("textAreaItemLabelCell")
    public String textAreaItemLabelCellClass();

    @ClassName("searchItemCell")
    public String searchItemCellClass();
    @ClassName("textAreaItemCell")
    public String textAreaItemCellClass();
}
