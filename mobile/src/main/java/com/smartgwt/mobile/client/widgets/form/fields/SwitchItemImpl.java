package com.smartgwt.mobile.client.widgets.form.fields;

import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
abstract class SwitchItemImpl {

    abstract void create(SwitchItem self);

    abstract void destroyImpl(SwitchItem self);

    void onLoad(SwitchItem self) {
        /*empty*/
    }

    void setVisible(SwitchItem self, boolean visible) {
        /*empty*/
    }

    abstract void setOnText(SwitchItem self, String onText);

    abstract void setOffText(SwitchItem self, String offText);

    abstract boolean isChecked(SwitchItem self);

    abstract void setChecked(SwitchItem self, boolean checked);
}
