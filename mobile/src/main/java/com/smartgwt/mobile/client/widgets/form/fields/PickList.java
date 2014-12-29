package com.smartgwt.mobile.client.widgets.form.fields;

import com.smartgwt.mobile.client.data.Criteria;
import com.smartgwt.mobile.client.data.DataSource;

public interface PickList {

    public String getDisplayField();

    public void setDisplayField(String displayField);

    public String getDisplayFieldName();

    public DataSource getOptionDataSource();

    public void setOptionDataSource(DataSource ds);

    public String getPickListBaseStyle();

    public void setPickListBaseStyle(String pickListBaseStyle);

    public Criteria getPickListCriteria();

    public void setPickListCriteria(Criteria criteria);

    public String getValueField();

    public void setValueField(String valueField);

    public String getValueFieldName();
}
