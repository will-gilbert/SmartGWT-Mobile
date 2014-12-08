package com.smartgwt.mobile.showcase.client.widgets.forms;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.internal.util.AnimationUtil;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.types.FormStyle;
import com.smartgwt.mobile.client.widgets.HRWidget;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.FormItemValueFormatter;
import com.smartgwt.mobile.client.widgets.form.FormItemValueParser;
import com.smartgwt.mobile.client.widgets.form.fields.EmailItem;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;
import com.smartgwt.mobile.client.widgets.form.fields.PhoneItem;
import com.smartgwt.mobile.client.widgets.form.fields.SearchItem;
import com.smartgwt.mobile.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.mobile.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.mobile.client.widgets.form.fields.TextItem;
import com.smartgwt.mobile.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.mobile.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.mobile.client.widgets.layout.HLayout;

public class Inputs extends ScrollablePanel {

    private static class NumberItem extends TextItem {

        public NumberItem(String name) {
            super(name);
            init();
        }

        public NumberItem(String name, String title) {
            super(name, title);
            init();
        }

        public NumberItem(String name, String title, String placeholder) {
            super(name, title, placeholder);
            init();
        }

        private void init() {
            super.setBrowserInputType("number");
            super.setEditorValueParser(new FormItemValueParser() {
                private final RegExp leadingZeroesRE = RegExp.compile("^([-+]?)\\s*0+");

                @Override
                public Object parseValue(String value, DynamicForm form, FormItem item) {
                    if (value == null) return null;

                    // Remove leading zeroes in the whole number portion. This allows strings
                    // like "00171" and "-00010" to be parsed as numbers.
                    value = leadingZeroesRE.replace(value.trim(), "$1");

                    // Interpret an empty string as 0.
                    if (value.isEmpty()) return Double.valueOf(0.0);

                    try {
                        return Double.valueOf(value);
                    } catch (NumberFormatException ex) {
                        return value;
                    }
                }
            });
            super.setTextAlign(Alignment.RIGHT);
        }
    }

    private static class ZIPCodeItem extends TextItem {

        public ZIPCodeItem(String name) {
            super(name);
            init();
        }

        public ZIPCodeItem(String name, String title) {
            super(name, title);
            init();
        }

        public ZIPCodeItem(String name, String title, String placeholder) {
            super(name, title, placeholder);
            init();
        }

        private void init() {
            // https://bugzilla.mozilla.org/show_bug.cgi?id=746142
            // https://bugs.webkit.org/show_bug.cgi?id=23588
            getElement().setAttribute("inputmode", "latin digits");
            super.setEditorValueFormatter(new FormItemValueFormatter() {
                private final RegExp zipCodeRE = RegExp.compile("^([0-9]{1,5})\\s*-?\\s*([0-9]{0,4})$");

                @Override
                public String formatValue(Object value, Record record, DynamicForm form, FormItem item) {
                    if (value == null) return null;
                    final String text = value.toString().trim();
                    final MatchResult match = zipCodeRE.exec(text);
                    if (match != null) {
                        final int zip5 = Integer.parseInt(match.getGroup(1), 10);
                        final String $2 = match.getGroup(2);
                        final Integer plusFour;
                        if ($2 == null || $2.isEmpty()) plusFour = null;
                        else plusFour = Integer.valueOf($2, 10);

                        final StringBuilder sb = new StringBuilder();
                        sb.append(zip5);
                        while (sb.length() < 5) sb.insert(0, '0');
                        if (plusFour != null) {
                            sb.append('-').append(plusFour.intValue());
                            while (sb.length() < 5 + 1 + 4) sb.insert(5 + 1, '0');
                        }
                        return sb.toString();
                    }
                    return text;
                }
            });
        }
    }

    private DynamicForm form;
    private StaticTextItem staticTextItem;
    private NumberItem numberItem;
    private ZIPCodeItem zipItem;
    private PhoneItem phoneItem;
    private EmailItem emailItem;
    private SearchItem searchItem;
    private TextItem textItem;
    private TextAreaItem textAreaItem;
    private Panel output = new Panel();

    public Inputs(String title) {
        super(title);
        this.setWidth("100%");
        output.setStyleName("sc-rounded-panel");
        output.getElement().getStyle().setProperty("textAlign", "center");
        output.getElement().getStyle().setOpacity(0.0);
        HLayout panelWrapper = new HLayout();
        panelWrapper.setLayoutMargin(20);
        panelWrapper.setPaddingAsLayoutMargin(true);
        panelWrapper.setMembersMargin(20);
        panelWrapper.addMember(output);
        form = new DynamicForm();
        form.setFormStyle(FormStyle.STYLE2);
        staticTextItem = new StaticTextItem("_staticText");
        staticTextItem.setValue("This is a <code>StaticTextItem</code>.");
        numberItem = new NumberItem("number", "Number", "Number");
        numberItem.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (numberItem.getValue() != null) {
                    output.setContents("Number is " + numberItem.getValue());
                    AnimationUtil.fadeTransition(output, true);
                }
            }
        });
        zipItem = new ZIPCodeItem("zip", "ZIP Code", "ZIP Code");
        phoneItem = new PhoneItem("phone", "Phone Number", "Phone Number");
        phoneItem.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (phoneItem.getValue() != null) {
                    output.setContents("Phone input is " + phoneItem.getValue() + " " + (phoneItem.validate() ? "VALID" : "INVALID"));
                    AnimationUtil.fadeTransition(output, true);
                }
            }
        });
        emailItem = new EmailItem("email", "Email", "Email");
        emailItem.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (emailItem.getValue() != null) {
                    output.setContents("Email input is " + emailItem.getValue() + " " + (emailItem.validate() ? "VALID" : "INVALID"));
                    AnimationUtil.fadeTransition(output, true);
                }
            }
        });
        searchItem = new SearchItem("search", "Search", "Search Term");
        searchItem.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (searchItem.getValue() != null) {
                    output.setContents("Search input is " + searchItem.getValue());
                    AnimationUtil.fadeTransition(output, true);
                }
            }
        });
        textItem = new TextItem("text", "Text", "Text");
        textItem.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (textItem.getValue() != null) {
                    output.setContents("Text input is " + textItem.getValue());
                    AnimationUtil.fadeTransition(output, true);
                }
            }
        });
        textAreaItem = new TextAreaItem("message", "Message", "Message");
        textAreaItem.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (searchItem.getValue() != null) {
                    output.setContents("Message is <pre style='text-align:left'>" + textAreaItem.getValue() + "</pre>");
                    AnimationUtil.fadeTransition(output, true);
                }
            }
        });

        form.setFields(staticTextItem, numberItem, zipItem, phoneItem, emailItem, searchItem, textItem, textAreaItem);
        addMember(form);
        addMember(new HRWidget());
        addMember(panelWrapper);
    }

    @Override
    public void reset() {
        super.reset();
        form.clearValues();
        staticTextItem.setValue("This is a <code>StaticTextItem</code>.");
        output.getElement().getStyle().setOpacity(0.0);
    }
}
