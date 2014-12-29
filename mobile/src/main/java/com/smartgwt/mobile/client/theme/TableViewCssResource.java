package com.smartgwt.mobile.client.theme;

import com.google.gwt.resources.client.CssResource.Shared;

@Shared
public interface TableViewCssResource extends BaseCssResource {

    @ClassName("tableViewWrapper")
    public String tableViewWrapperClass();

    @ClassName("sc-table")
    public String tableViewClass();

    @ClassName("tableViewHasParentNavStack")
    public String tableViewHasParentNavStackClass();

    @ClassName("groupedTableView")
    public String groupedTableViewClass();

    @ClassName("stackedTableView")
    public String stackedTableViewClass();

    @ClassName("tableViewShowDeleteDisclosures")
    public String tableViewShowDeleteDisclosuresClass();

    @ClassName("tableViewShowMoveIndicators")
    public String tableViewShowMoveIndicatorsClass();

    @ClassName("sc-table-group")
    public String tableViewGroupClass();

    @ClassName("tableViewGroupWithoutGroupTitle")
    public String tableViewGroupWithoutGroupTitleClass();

    @ClassName("firstTableViewGroup")
    public String firstTableViewGroupClass();

    @ClassName("lastTableViewGroup")
    public String lastTableViewGroupClass();

    @ClassName("sc-row")
    public String tableViewRowClass();

    @ClassName("selectedTableViewRow")
    public String selectedTableViewRowClass();

    @ClassName("clearingTemporaryTableViewRowSelection")
    public String clearingTemporaryTableViewRowSelectionClass();

    @ClassName("selectedTableViewRowHasIcon")
    public String selectedTableViewRowHasIconClass();

    @ClassName("tableViewRowHasIcon")
    public String tableViewRowHasIconClass();

    @ClassName("tableViewRowHasRecordInfo")
    public String tableViewRowHasRecordInfoClass();

    @ClassName("tableViewRowHasNavigationDisclosure")
    public String tableViewRowHasNavigationDisclosureClass();

    @ClassName("firstTableViewRow")
    public String firstTableViewRowClass();

    @ClassName("lastTableViewRow")
    public String lastTableViewRowClass();

    @ClassName("sc-record-icon")
    public String recordIconClass();

    @ClassName("sc-record-counter")
    public String recordCounterClass();

    @ClassName("sc-record-title")
    public String recordTitleClass();

    @ClassName("sc-record-info")
    public String recordInfoClass();

    @ClassName("sc-record-description")
    public String recordDescriptionClass();

    @ClassName("sc-record-component")
    public String recordComponentClass();

    @ClassName("recordDeleteDisclosure")
    public String recordDeleteDisclosureClass();

    @ClassName("checkedSelectionOrDeleteDisclosure")
    public String checkedSelectionOrDeleteDisclosureClass();

    @ClassName("recordDetailDisclosure")
    public String recordDetailDisclosureClass();

    @ClassName("recordDetailDisclosureNavIcon")
    public String recordDetailDisclosureNavIconClass();

    @ClassName("recordSelectionDisclosure")
    public String recordSelectionDisclosureClass();

    @ClassName("nonselectableSelectionDisclosure")
    public String nonselectableSelectionDisclosureClass();

    @ClassName("recordMoveIndicator")
    public String recordMoveIndicatorClass();

    @ClassName("contextClickedElement")
    public String contextClickedElementClass();
}
