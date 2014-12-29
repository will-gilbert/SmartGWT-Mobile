package com.smartgwt.mobile.client.internal.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.dom.client.Element;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.EventListener;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.DataSource;
import com.smartgwt.mobile.client.internal.Array;
import com.smartgwt.mobile.client.internal.types.AttributeType;
import com.smartgwt.mobile.client.internal.util.Pair;
import com.smartgwt.mobile.client.types.ValueEnum;
import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.widgets.BaseButton;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;
import com.smartgwt.mobile.client.widgets.form.fields.SelectItem;
import com.smartgwt.mobile.client.widgets.form.fields.TextItem;
import com.smartgwt.mobile.client.widgets.grid.ListGrid;
import com.smartgwt.mobile.client.widgets.layout.NavStack;
import com.smartgwt.mobile.client.widgets.tab.TabSet;
import com.smartgwt.mobile.client.widgets.tableview.TableView;

@SGWTInternal
public class AutoTest {

    public static final String FALLBACK_VALUE_ONLY_FIELD = "_$_standaloneProperty";

    public static native void init() /*-{
        $wnd.isc = $wnd.isc || {};
        $wnd.isc.AutoTest = {
            getComponent : $entry(function (locator) {
                return @com.smartgwt.mobile.client.internal.test.AutoTest::getComponent(Ljava/lang/String;)(String(locator));
            }),
            getElement : $entry(function (locator) {
                return @com.smartgwt.mobile.client.internal.test.AutoTest::getElement(Ljava/lang/String;)(String(locator));
            }),
            isElementClickable : $entry(function (obj) {
                var b;
                if (@com.smartgwt.mobile.client.util.JSOHelper::isString(Ljava/lang/Object;)(obj)) {
                    b = @com.smartgwt.mobile.client.internal.test.AutoTest::isElementClickable(Ljava/lang/String;)(obj);
                } else if ("innerHTML" in obj && obj.ownerDocument === $doc && obj.nodeType === 1) {
                    b = @com.smartgwt.mobile.client.internal.test.AutoTest::isElementClickable(Lcom/google/gwt/dom/client/Element;)(obj);
                }
                if (b == null) return null;
                return b.@java.lang.Boolean::booleanValue()();
            }),
            getLastClickedElement : $entry(function () {
                return @com.smartgwt.mobile.client.internal.EventHandler::lastClickedElem;
            }),
            getValue : $entry(function (locator) {
                var val = @com.smartgwt.mobile.client.internal.test.AutoTest::getValue(Ljava/lang/String;)(String(locator));
                return @com.smartgwt.mobile.client.internal.test.AutoTest::makeNative(Ljava/lang/Object;)(val);
            }),
            wasClickedLast : $entry(function (elem) {
                var n = @com.smartgwt.mobile.client.internal.EventHandler::lastClickedElem;
                for (; n != null; n = n.parentNode) {
                    if (elem === n) return true;
                }
                return false;
            })
        };
    }-*/;

    public static Object getAttribute(String locator, AttributeType attribute) {
        if (locator == null) return null;

        RegExp re = RegExp.compile("^(?:scLocator|ScID)=", "i");
        locator = re.replace(locator, "");

        if (locator.startsWith("'") || locator.startsWith("\"")) locator = locator.substring(1);
        if (locator.endsWith("'") || locator.endsWith("\"")) locator = locator.substring(0, locator.length() - 1);

        if (!locator.startsWith("//")) {
            // assume either just an ID or "ID=[ID]"
            if (locator.startsWith("ID=") || locator.startsWith("id=")) {
                locator = locator.substring(3);
            }
            locator = "//*any*[ID=\"" + locator + "\"]";
        }

        re = RegExp.compile("/");
        final SplitResult splitRes = re.split(locator);

        // account for the 2 slashes
        assert splitRes.length() >= 3;
        final String baseComponentID = splitRes.get(2);
        if (baseComponentID == null || baseComponentID.isEmpty()) return null;

        // knock off the first 3 slots
        final List<String> locatorArray = new ArrayList<String>(splitRes.length() - 3);
        for (int i = 3; i < splitRes.length(); ++i) {
            locatorArray.add(splitRes.get(i));
        }

        final GetAttributeConfiguration configuration = new GetAttributeConfiguration();
        configuration.setAttribute(attribute);

        final Canvas baseComponent = getBaseComponentFromLocatorSubstring(baseComponentID, configuration);

        if (baseComponent == null) return null;

        return baseComponent._getAttributeFromSplitLocator(locatorArray, configuration);
    }

    public static Canvas getBaseComponentFromLocatorSubstring(String substring, GetAttributeConfiguration configuration) {
        final int rsbPos = substring.indexOf('[');
        final String idType = (rsbPos >= 0 ? substring.substring(0, rsbPos) : null);

        final String className = idType;
        final RegExp re = RegExp.compile("\\[ID=[\\\"'](.*)['\\\"](,.*)?\\]");
        final MatchResult idMatches = re.exec(substring);
        final String id = (idMatches != null ? idMatches.getGroup(1) : null);

        // TODO idMatches[2]

        final Object baseComponent = getGlobal(id);
        if (!(baseComponent instanceof Canvas)) return null;

        if (!"*any*".equals(className)) {
            boolean res;
            if ("Button".equals(className)) {
                res = (baseComponent instanceof BaseButton);
            } else if ("DynamicForm".equals(className)) {
                res = (baseComponent instanceof DynamicForm);
            } else if ("FormItem".equals(className)) {
                res = (baseComponent instanceof FormItem);
            } else if ("ListGrid".equals(className)) {
                res = (baseComponent instanceof ListGrid);
            } else if ("NavStack".equals(className)) {
                res = (baseComponent instanceof NavStack);
            } else if ("SelectItem".equals(className)) {
                res = (baseComponent instanceof SelectItem);
            } else if ("TableView".equals(className)) {
                res = (baseComponent instanceof TableView);
            } else if ("TabSet".equals(className)) {
                res = (baseComponent instanceof TabSet);
            } else if ("TextItem".equals(className)) {
                res = (baseComponent instanceof TextItem);
            } else {
                String baseComponentClassName = baseComponent.getClass().getName();
                final int periodPos = baseComponentClassName.lastIndexOf('.');
                if (periodPos >= 0) baseComponentClassName = baseComponentClassName.substring(periodPos + 1);
                res = baseComponentClassName.equals(className);
            }
            if (!res) SC.logWarn("AutoTest.getElement(): Component:" + id + " expected to be of class:" + className);
        }

        return (Canvas)baseComponent;
    }

    public static Object getComponent(String locator) {
        return getAttribute(locator, AttributeType.COMPONENT);
    }

    public static Element getElement(String locator) {
        return (Element)getAttribute(locator, AttributeType.ELEMENT);
    }

    public static Boolean isElementClickable(Canvas baseComponent) {
        if (baseComponent == null) return null;
        final GetAttributeConfiguration configuration = new GetAttributeConfiguration();
        configuration.setAttribute(AttributeType.IS_CLICKABLE);
        Object res = baseComponent._getAttributeFromSplitLocator(Collections.<String>emptyList(), configuration);
        if (!(res instanceof Boolean)) return null;
        return (Boolean)res;
    }

    public static Boolean isElementClickable(Element elem) {
        final EventListener eventListener = DOM.getEventListener(elem.<com.google.gwt.user.client.Element>cast());
        if (!(eventListener instanceof Canvas)) return null;
        return isElementClickable((Canvas)eventListener);
    }

    public static Boolean isElementClickable(String locator) {
        Object res = getAttribute(locator, AttributeType.IS_CLICKABLE);
        if (!(res instanceof Boolean)) return null;
        return (Boolean)res;
    }

    public static native Object getGlobal(String name) /*-{
        return $wnd[name];
    }-*/;

    public static Object getValue(String locator) {
        if ("_numDSRequestsSent".equals(locator)) return DataSource._numDSRequestsSent;
        return getAttribute(locator, AttributeType.VALUE);
    }

    private static native JsArray<?> createArray(List<?> list) /*-{
        if (!list) return null;

        var ret = [];
        var list_size = list.@java.util.List::size()();
        for (var i = 0; i < list_size; ++i) {
            ret[i] = @com.smartgwt.mobile.client.internal.test.AutoTest::makeNative(Ljava/lang/Object;)(list.@java.util.List::get(I)(i));
        }
        return ret;
    }-*/;

    private static native JavaScriptObject createBoolean(boolean val) /*-{
        //return new Boolean(val);
        return val;
    }-*/;

    private static native JavaScriptObject createNumber(double val) /*-{
        //return new Number(val);
        return val;
    }-*/;

    private static native JavaScriptObject createObject(Map<?, ?> map) /*-{
        if (!map) return null;

        var ret = {};
        var entrySet = map.@java.util.Map::entrySet()();
        var it = entrySet.@java.util.Set::iterator()();
        while (it.@java.util.Iterator::hasNext()()) {
            var e = it.@java.util.Iterator::next()();
            var nativeKey = @com.smartgwt.mobile.client.internal.test.AutoTest::makeNative(Ljava/lang/Object;)(e.@java.util.Map.Entry::getKey()());
            var nativeVal = @com.smartgwt.mobile.client.internal.test.AutoTest::makeNative(Ljava/lang/Object;)(e.@java.util.Map.Entry::getValue()());
            ret[String(nativeKey)] = nativeVal;
        }
        return ret;
    }-*/;

    private static Object makeNative(Object obj) {
        if (obj == null) return null;
        if (obj instanceof CharSequence) return obj.toString();
        if (obj instanceof Number) return createNumber(((Number)obj).doubleValue());
        if (obj instanceof Date) {
            final JsDate ret = JsDate.create(((Date)obj).getTime());
            // TODO Add `logicalDate' and `logicalTime' markers.
            return ret;
        }
        if (obj instanceof Boolean) return createBoolean(((Boolean)obj).booleanValue());
        if (Array.isArray(obj)) return createArray(Array.asList(obj));
        if (obj instanceof List) return createArray((List<?>)obj);
        if (obj instanceof Map) return createObject((Map<?, ?>)obj);
        if (obj instanceof ValueEnum) return ((ValueEnum)obj).getValue();
        return obj.toString();
    }

    public static Pair<String, Map<String, String>> parseLocatorFallbackPath(String path) {
        RegExp re = RegExp.compile("\\[");
        final SplitResult pathArr = re.split(path);

        // don't crash if we were passed something we don't understand...
        if (pathArr == null || pathArr.length() < 2) return null;

        final String name = pathArr.get(0);
        path = path.substring(name.length() + 1, path.length() - 1);

        re = RegExp.compile("\\|\\|");
        final SplitResult configArr = re.split(path);
        final Map<String, String> configObj = new HashMap<String, String>();
        for (int i = 0; i < configArr.length(); ++i) {
            String string = configArr.get(i);
            final int eqPos = string.indexOf('=');
            final String fieldName;
            if (eqPos < 0) {
                fieldName = FALLBACK_VALUE_ONLY_FIELD;
            } else {
                fieldName = string.substring(0, eqPos);
                string = string.substring(eqPos + 1);
            }
            string = unescape(string);
            string = string.replace("$fs$", "/");
            configObj.put(fieldName, string);
        }
        return Pair.create(name, configObj);
    }

    public static native String unescape(String escaped) /*-{
        return $wnd.unescape(escaped);
    }-*/;
}
