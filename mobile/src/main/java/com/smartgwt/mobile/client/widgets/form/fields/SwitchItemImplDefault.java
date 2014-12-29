package com.smartgwt.mobile.client.widgets.form.fields;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.theme.ios.SwitchItemCssResourceIOS;
import com.smartgwt.mobile.client.internal.util.ElementUtil;

@SGWTInternal
class SwitchItemImplDefault extends SwitchItemImpl {

    final SwitchItemCssResourceIOS CSS = (SwitchItemCssResourceIOS)SwitchItem._CSS;
    Element onTextElem, offTextElem, knobElem;

    @Override
    void create(SwitchItem self) {
        final Element element = self.getElement();
        final Document document = Document.get();
        onTextElem = document.createDivElement();
        onTextElem.setClassName(CSS.switchedOnTextClass());
        element.appendChild(onTextElem);
        offTextElem = document.createDivElement();
        offTextElem.setClassName(CSS.switchedOffTextClass());
        offTextElem.setInnerHTML("Off");
        element.appendChild(offTextElem);
        knobElem = document.createDivElement();
        knobElem.setClassName(CSS.switchItemKnobClass());
        element.appendChild(knobElem);
    }

    @Override
    void destroyImpl(SwitchItem self) {
        /*empty*/
    }

    @Override
    void setOnText(SwitchItem self, String onText) {
        onTextElem.setInnerHTML(onText == null ? "" : onText);
    }

    @Override
    void setOffText(SwitchItem self, String offText) {
        offTextElem.setInnerHTML(offText == null ? "" : offText);
    }

    @Override
    boolean isChecked(SwitchItem self) {
        return ElementUtil.hasClassName(self.getElement(), CSS.switchedOnClass());
    }

    @Override
    void setChecked(SwitchItem self, boolean checked) {
        if (checked) {
            self.getElement().removeClassName(CSS.switchedOffClass());
            self.getElement().addClassName(CSS.switchedOnClass());
        } else {
            self.getElement().removeClassName(CSS.switchedOnClass());
            self.getElement().addClassName(CSS.switchedOffClass());
        }
    }
}
