package com.smartgwt.mobile.internal.gwt.dom.client;

import java.util.Date;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.util.LogicalDate;
import com.smartgwt.mobile.client.util.LogicalTime;

@SGWTInternal
public class SuperInputElement extends InputElement {

    public static final DateTimeFormat VALID_DATE_STRING_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd"),
            VALID_TIME_STRING_FORMAT = DateTimeFormat.getFormat("HH:mm:ss.SSS"),
            VALID_GLOBAL_DATE_AND_TIME_STRING_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),
            VALID_LOCAL_DATE_AND_TIME_STRING_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    // Ensures that the `DateTimeFormat' instances are created.
    public static void maybeInit() {}

    public static Date parseGlobalDateAndTimeString(String value) {
        if (value == null || value.length() < 19) return null;
        if (value.charAt(10) == ' ') {
            value = value.substring(0, 10) + "T" + value.substring(11);
        }
        if (value.length() == 19) value += ".000";
        else if (value.length() >= 20 || value.charAt(19) != '.') {
            value = value.substring(0, 19) + ".000" + value.substring(20);
        }
        return VALID_GLOBAL_DATE_AND_TIME_STRING_FORMAT.parse(value);
    }

    public static Date parseLocalDateAndTimeString(String value) {
        if (value == null || value.length() < 19) return null;
        if (value.charAt(10) == ' ') {
            value = value.substring(0, 10) + "T" + value.substring(11);
        }
        if (value.length() == 19) value += ".000";
        else if (value.length() >= 20 || value.charAt(19) != '.') {
            value = value.substring(0, 19) + ".000" + value.substring(20);
        }
        return VALID_LOCAL_DATE_AND_TIME_STRING_FORMAT.parse(value);
    }

    protected SuperInputElement() {}

    /**
     * Sets the HTML5 <code>max</code> attribute to <code>value</code>. Note that the given
     * {@link com.smartgwt.mobile.client.util.LogicalDate} is first converted to a
     * <a href="http://www.w3.org/TR/html5/infrastructure.html#valid-date-string">valid date string</a>.
     * @param value new value for the <code>max</code> attribute.
     * @see <a href="http://www.w3.org/TR/html5/forms.html#attr-input-max">W3C HTML5 Specification - The <code>min</code> and <code>max</code> attributes</a>
     */
    public final native void setMaxDate(LogicalDate value) /*-{
        if (value == null) {
            this.max = "";
        } else {
            var formatter = @com.smartgwt.mobile.internal.gwt.dom.client.SuperInputElement::VALID_DATE_STRING_FORMAT;
            this.max = formatter.@com.google.gwt.i18n.shared.DateTimeFormat::format(Ljava/util/Date;)(value);
        }
    }-*/;

    /**
     * Sets the HTML5 <code>min</code> attribute to <code>value</code>. Note that the given
     * {@link com.smartgwt.mobile.client.util.LogicalDate} is first converted to a
     * <a href="http://www.w3.org/TR/html5/infrastructure.html#valid-date-string">valid date string</a>.
     * @param value new value for the <code>min</code> attribute.
     * @see <a href="http://www.w3.org/TR/html5/forms.html#attr-input-min">W3C HTML5 Specification - The <code>min</code> and <code>max</code> attributes</a>
     */
    public final native void setMinDate(LogicalDate value) /*-{
        if (value == null) {
            this.min = "";
        } else {
            var formatter = @com.smartgwt.mobile.internal.gwt.dom.client.SuperInputElement::VALID_DATE_STRING_FORMAT;
            this.min = formatter.@com.google.gwt.i18n.shared.DateTimeFormat::format(Ljava/util/Date;)(value);
        }
    }-*/;

    public final native void setPlaceholder(String value) /*-{
        this.placeholder = value;
    }-*/;

    /**
     * Sets the HTML5 <code>required</code> attribute to <code>value</code>.
     * @param value new value for the <code>required</code> attribute.
     * @see <a href="http://www.w3.org/TR/html5/forms.html#attr-input-required">W3C HTML5 Specification - The <code>required</code> attribute</a>
     */
    public final native void setRequired(boolean value) /*-{
        this.required = value;
    }-*/;

    public final native LogicalDate getValueAsDate() /*-{
        if (!this.value) return null;

        // Try the special HTML5 `valueAsDate' first.
        // http://www.w3.org/TR/html5/forms.html#dom-input-valueasdate
        var date = this.valueAsDate,
            dateJ;
        if (date != null) {
            // http://www.w3.org/TR/html5/infrastructure.html#dates says
            // "A date consists of a specific proleptic Gregorian date with no time-zone information,
            // consisting of a year, a month, and a day."
            // WebKit interprets "no time-zone information" to mean UTC when a string is parsed
            // as a date according to http://www.w3.org/TR/html5/infrastructure.html#parse-a-date-string
            dateJ = @com.smartgwt.mobile.client.util.LogicalDate::new(III)(date.getUTCFullYear(), date.getUTCMonth(), date.getUTCDate());
        } else {
            var formatter = @com.smartgwt.mobile.internal.gwt.dom.client.SuperInputElement::VALID_DATE_STRING_FORMAT;
            dateJ = formatter.@com.google.gwt.i18n.shared.DateTimeFormat::parse(Ljava/lang/String;)(this.value);
            dateJ = @com.smartgwt.mobile.client.util.DateUtil::getLogicalDateOnly(Ljava/util/Date;)(dateJ);
        }
        return dateJ;
    }-*/;

    public final native Date getValueAsDatetime() /*-{
        var value = this.value;
        if (!value) return null;

        if ("datetime-local" == this.type) {
            var datetime = this.valueAsDate;
            if (datetime != null) return @com.smartgwt.mobile.client.util.JSOHelper::toDate(D)(datetime.getTime());
            else return @com.smartgwt.mobile.internal.gwt.dom.client.SuperInputElement::parseLocalDateAndTimeString(Ljava/lang/String;)(value);
        } else {
            return @com.smartgwt.mobile.internal.gwt.dom.client.SuperInputElement::parseGlobalDateAndTimeString(Ljava/lang/String;)(value);
        }
    }-*/;

    public final native LogicalTime getValueAsTime() /*-{
        if (!this.value) return null;

        // Try the special HTML5 `valueAsDate' first.
        // http://www.w3.org/TR/html5/forms.html#dom-input-valueasdate
        var time = this.valueAsDate,
            timeJ;
        if (time != null) {
            timeJ = @com.smartgwt.mobile.client.util.LogicalTime::new(III)(time.getUTCHours(), time.getUTCMinutes(), time.getUTCSeconds());
        } else {
            var formatter = @com.smartgwt.mobile.internal.gwt.dom.client.SuperInputElement::VALID_TIME_STRING_FORMAT;
            timeJ = formatter.@com.google.gwt.i18n.shared.DateTimeFormat::parse(Ljava/lang/String;)(this.value);
            timeJ = @com.smartgwt.mobile.client.util.DateUtil::getLogicalTimeOnly(Ljava/util/Date;)(timeJ);
        }
        return timeJ;
    }-*/;

    public final native void setValue(LogicalDate value) /*-{
        if (value == null) this.value = "";
        else {
            var formatter = @com.smartgwt.mobile.internal.gwt.dom.client.SuperInputElement::VALID_DATE_STRING_FORMAT;
            this.value = formatter.@com.google.gwt.i18n.shared.DateTimeFormat::format(Ljava/util/Date;)(value);
        }
    }-*/;

    public final native void setValue(LogicalTime value) /*-{
        if (value == null) this.value = "";
        else {
            var formatter = @com.smartgwt.mobile.internal.gwt.dom.client.SuperInputElement::VALID_TIME_STRING_FORMAT;
            this.value = formatter.@com.google.gwt.i18n.shared.DateTimeFormat::format(Ljava/util/Date;)(value);
        }
    }-*/;
}
