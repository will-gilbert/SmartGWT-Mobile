package com.smartgwt.mobile.client.internal.widgets;

import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.test.AutoTest;
import com.smartgwt.mobile.client.internal.test.AutoTestLocatable;
import com.smartgwt.mobile.client.internal.test.GetAttributeConfiguration;
import com.smartgwt.mobile.client.internal.util.ElementUtil;
import com.smartgwt.mobile.client.internal.util.Pair;
import com.smartgwt.mobile.client.internal.widgets.layout.NavigationBarItem;
import com.smartgwt.mobile.client.types.NavigationDirection;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.NavigationButton;
import com.smartgwt.mobile.client.widgets.layout.NavigationBar;

@SGWTInternal
public class NavigationButtons extends Canvas implements NavigationBarItem {

    private NavigationButton[] buttons;

    public NavigationButtons(NavigationButton... buttons) {
        super(Document.get().createDivElement());
        _setClassName(NavigationBar._CSS.navigationButtonsClass(), false);

        this.buttons = buttons;

        for (final NavigationButton button : buttons) {
            button.setDirection(NavigationDirection.NONE);
            addChild(button);
        }
    }

    public final NavigationButton getButton(int index) {
        return (buttons == null || buttons.length <= index ? null : buttons[index]);
    }

    public final NavigationButton[] getButtons() {
        return buttons;
    }

    @Override
    public AutoTestLocatable _getChildFromLocatorSubstring(String substring, int index, List<String> locatorArray, GetAttributeConfiguration configuration) {
        if (buttons != null && substring.startsWith("button[")) {
            final Pair<String, Map<String, String>> p = AutoTest.parseLocatorFallbackPath(substring);
            if (p != null) {
                assert "button".equals(p.getFirst());
                final Map<String, String> configObj = p.getSecond();
                final String valueOnly = configObj.get(AutoTest.FALLBACK_VALUE_ONLY_FIELD);
                if (valueOnly != null) {
                    int i = -1;
                    try {
                        i = Integer.parseInt(valueOnly, 10);
                    } catch (NumberFormatException ex) {}
                    if (i >= 0 && i < buttons.length) {
                        return buttons[i];
                    }
                }
            }
        }
        return super._getChildFromLocatorSubstring(substring, index, locatorArray, configuration);
    }

    @Override
    public final float _getContentWidth() {
        int ret = 0;
        for (NavigationButton button : buttons) {
            ret += ElementUtil.getOuterWidth(button.getElement(), true);
        }
        return ret;
    }

    public final NavigationButton getFirstButton() {
        return getButton(0);
    }

    @Override
    public void destroy() {
        assert buttons != null;
        for (final NavigationButton button : buttons) {
            removeChild(button);
        }
        super.destroy();
    }
}
