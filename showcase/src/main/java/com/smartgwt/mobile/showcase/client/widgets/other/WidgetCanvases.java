package com.smartgwt.mobile.showcase.client.widgets.other;

import java.util.Date;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.datepicker.client.CalendarModel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.DefaultCalendarView;
import com.google.gwt.user.datepicker.client.MonthSelector;
import com.smartgwt.mobile.client.widgets.Label;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;

public class WidgetCanvases extends ScrollablePanel {

    private static class MobileDatePicker extends DatePicker {

        // GWT's DefaultMonthSelector uses PushButtons for the "previous month" and
        // "next month" buttons. PushButtons do not work on mobile devices, so we use a
        // different MonthSelector implementation that relies on ClickEvents only.
        private static class MobileMonthSelector extends MonthSelector {

            private com.google.gwt.user.client.ui.Label previousMonthButton;
            private com.google.gwt.user.client.ui.Label nextMonthButton;
            private Grid grid;

            @Override
            protected void refresh() {
                grid.setText(0, 1, getModel().formatCurrentMonth());
            }

            @Override
            protected void setup() {
                previousMonthButton = new com.google.gwt.user.client.ui.HTML("&#171;");
                previousMonthButton.setStyleName("datePickerPreviousButton");
                previousMonthButton.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {

                    public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
                        addMonths(-1);
                    }
                });

                nextMonthButton = new com.google.gwt.user.client.ui.HTML("&#187;");
                nextMonthButton.setStyleName("datePickerNextButton");
                nextMonthButton.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {

                    public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
                        addMonths(1);
                    }
                });

                grid = new Grid(1, 3);
                grid.setWidget(0,  0, previousMonthButton);
                grid.setWidget(0,  2, nextMonthButton);

                CellFormatter formatter = grid.getCellFormatter();
                formatter.setStyleName(0, 1, "datePickerMonth");
                formatter.setWidth(0, 0, "1");
                formatter.setWidth(0, 1, "100%");
                formatter.setWidth(0, 2, "1");
                grid.setStyleName("datePickerMonthSelector");
                initWidget(grid);
            }
        }

        public MobileDatePicker() {
            super(new MobileMonthSelector(), new DefaultCalendarView(), new CalendarModel());
        }
    }

    static {
        // This is the example CSS used in GWT Showcase, but without hover styling because that
        // does not work on iPhone.
        // http://gwt.google.com/samples/Showcase/Showcase.html#!CwDatePicker
        StyleInjector.injectAtEnd(".gwt-DatePicker {\n" +
                "  border: 1px solid #ccc;\n" +
                "  border-top:1px solid #999;\n" +
                "  cursor: default;\n" +
                "}\n" +
                ".gwt-DatePicker td,\n" +
                ".datePickerMonthSelector td:focus {\n" +
                "  outline: none;\n" +
                "}\n" +
                ".datePickerMonthSelector td:focus {\n" +
                "  outline: none;\n" +
                "}\n" +
                ".datePickerDays {\n" +
                "  width: 100%;\n" +
                "  background: white;\n" +
                "}\n" +
                ".datePickerDay,\n" +
                ".datePickerWeekdayLabel,\n" +
                ".datePickerWeekendLabel {\n" +
                "  font-size: 85%;\n" +
                "  text-align: center;\n" +
                "  padding: 4px;\n" +
                "  outline: none;\n" +
                "  font-weight:bold;\n" +
                "  color:#333;\n" +
                "  border-right: 1px solid #EDEDED;\n" +
                "  border-bottom: 1px solid #EDEDED;\n" +
                "}\n" +
                ".datePickerWeekdayLabel,\n" +
                ".datePickerWeekendLabel {\n" +
                "  background: #fff;\n" +
                "  padding: 0px 4px 2px;\n" +
                "  cursor: default;\n" +
                "  color:#666;\n" +
                "  font-size:70%;\n" +
                "  font-weight:normal;\n" +
                "}\n" +
                ".datePickerDay {\n" +
                "  padding: 4px 7px;\n" +
                "  cursor: hand;\n" +
                "  cursor: pointer;\n" +
                "}\n" +
                ".datePickerDayIsWeekend {\n" +
                "  background: #f7f7f7;\n" +
                "}\n" +
                ".datePickerDayIsFiller {\n" +
                "  color: #999;\n" +
                "  font-weight:normal;\n" +
                "}\n" +
                ".datePickerDayIsValue {\n" +
                "  background: #d7dfe8;\n" +
                "}\n" +
                ".datePickerDayIsDisabled {\n" +
                "  color: #AAAAAA;\n" +
                "  font-style: italic;\n" +
                "}\n" +
                ".datePickerDayIsValueAndHighlighted {\n" +
                "  background: #d7dfe8;\n" +
                "}\n" +
                ".datePickerDayIsToday {\n" +
                "  padding: 3px;\n" +
                "  color: #fff;\n" +
                "  background: url(/sampleImages/hborder.png) repeat-x 0px -2607px;\n" +
                "}\n" +
                ".datePickerMonthSelector {\n" +
                "  width: 100%;\n" +
                "  padding: 1px 0 5px 0;\n" +
                "  background: #fff;\n" +
                "}\n" +
                ".datePickerPreviousButton,\n" +
                ".datePickerNextButton {\n" +
                "  font-size: 120%;\n" +
                "  line-height: 1em;\n" +
                "  color: #3a6aad;\n" +
                "  cursor: hand;\n" +
                "  cursor: pointer;\n" +
                "  font-weight: bold;\n" +
                "  padding: 0px 4px;\n" +
                "  outline: none;\n" +
                "}\n" +
                "td.datePickerMonth {\n" +
                "  text-align: center;\n" +
                "  vertical-align: middle;\n" +
                "  white-space: nowrap;\n" +
                "  font-size: 100%;\n" +
                "  font-weight: bold;\n" +
                "  color: #333;\n" +
                "}\n" +
                ".gwt-DateBox {\n" +
                "  padding: 5px 4px;\n" +
                "  border: 1px solid #ccc;\n" +
                "  border-top: 1px solid #999;\n" +
                "  font-size: 100%;\n" +
                "}\n" +
                ".gwt-DateBox input {\n" +
                "  width: 8em;\n" +
                "}\n" +
                ".dateBoxFormatError {\n" +
                "  background: #ffcccc;\n" +
                "}\n" +
                ".dateBoxPopup {\n" +
                "}", true);
    }

    private DateTimeFormat dateFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_FULL);
    private Label selectedDateLabel;

    public WidgetCanvases(String title) {
        super(title);

        final Panel introduction = new Panel();
        introduction.setContents("GWT widgets can be added directly to SmartGWT.mobile layouts. As an example, the following shows a slighty-adapted GWT <code>DatePicker</code> that has been added to a <code>ScrollablePanel</code>.");
        introduction.setStyleName("sc-rounded-panel");
        introduction.setStyleName("sc-stretch");
        introduction.setMargin(10);
        addMember(introduction);

        final DatePicker datePicker = new MobileDatePicker();
        datePicker.getElement().getStyle().setProperty("margin", "0 auto");
        datePicker.setValue(new Date(), true);
        addMember(datePicker);

        selectedDateLabel = new Label(dateFormat.format(datePicker.getValue()));
        Style style = selectedDateLabel.getElement().getStyle();
        style.setMarginTop(2, Style.Unit.PX);
        style.setMarginBottom(10, Style.Unit.PX);
        style.setProperty("webkitMarginBottomCollapse", "separate");
        addMember(selectedDateLabel);

        datePicker.addValueChangeHandler(new com.google.gwt.event.logical.shared.ValueChangeHandler<Date>() {

            @Override
            public void onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent<Date> event) {
                selectedDateLabel.setContents(dateFormat.format(event.getValue()));
            }
        });
    }
}
