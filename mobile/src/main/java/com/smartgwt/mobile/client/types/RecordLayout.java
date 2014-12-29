package com.smartgwt.mobile.client.types;

public enum RecordLayout {

    /**
     * Layout is based on the fields that are found in the record. This is the default.
     */
    AUTOMATIC, 

    /**
     * Show the field specified by {@link com.smartgwt.mobile.client.widgets.DataBoundComponent#getTitleField() titleField} only.
     * 
     * <p><b>NOTE:</b> <code>TableView</code> adds the {@link com.smartgwt.mobile.client.widgets.tableview.TableView#RECORD_TITLE_CLASS_NAME} class name
     * to the element containing the record title.
     */
    TITLE_ONLY,

    /**
     * Show the fields specified by {@link com.smartgwt.mobile.client.widgets.DataBoundComponent#getTitleField() titleField} and
     * {@link com.smartgwt.mobile.client.widgets.DataBoundComponent#getDescriptionField() descriptionField} only.
     * 
     * <p><b>NOTE:</b> <code>TableView</code> adds the {@link com.smartgwt.mobile.client.widgets.tableview.TableView#RECORD_DESCRIPTION_CLASS_NAME} class name
     * to the element containing the record description.
     */
    TITLE_DESCRIPTION,

    /**
     * Show the fields specified by {@link com.smartgwt.mobile.client.widgets.DataBoundComponent#getTitleField() titleField},
     * {@link com.smartgwt.mobile.client.widgets.DataBoundComponent#getDescriptionField() descriptionField}, and
     * {@link com.smartgwt.mobile.client.widgets.DataBoundComponent#getInfoField() infoField}.
     * 
     * <p><b>NOTE:</b> <code>TableView</code> adds the {@link com.smartgwt.mobile.client.widgets.tableview.TableView#RECORD_INFO_CLASS_NAME} class name
     * to the element containing the record info.
     */
    SUMMARY_INFO,

    /**
     * Show the fields specified by {@link com.smartgwt.mobile.client.widgets.DataBoundComponent#getTitleField() titleField},
     * {@link com.smartgwt.mobile.client.widgets.DataBoundComponent#getDescriptionField() descriptionField}, and
     * {@link com.smartgwt.mobile.client.widgets.DataBoundComponent#getDataField() dataField}.
     */
    SUMMARY_DATA,

    /**
     * Show the fields specified by {@link com.smartgwt.mobile.client.widgets.DataBoundComponent#getTitleField() titleField},
     * {@link com.smartgwt.mobile.client.widgets.DataBoundComponent#getDescriptionField() descriptionField},
     * {@link com.smartgwt.mobile.client.widgets.DataBoundComponent#getInfoField() infoField}, and
     * {@link com.smartgwt.mobile.client.widgets.DataBoundComponent#getDataField() dataField},
     * similar to the iPhone Mail application.
     */
    SUMMARY_FULL
}
