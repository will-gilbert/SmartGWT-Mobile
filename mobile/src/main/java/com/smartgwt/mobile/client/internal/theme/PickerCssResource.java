package com.smartgwt.mobile.client.internal.theme;

import com.google.gwt.resources.client.CssResource.Shared;
import com.smartgwt.mobile.SGWTInternal;

@Shared
@SGWTInternal
public interface PickerCssResource extends PopupCssResource {

    @ClassName("timePicker")
    public String timePickerClass();

    @ClassName("pickerDial")
    public String pickerDialClass();

    @ClassName("yearPickerDial")
    public String yearPickerDialClass();

    @ClassName("monthPickerDial")
    public String monthPickerDialClass();

    @ClassName("datePickerDial")
    public String datePickerDialClass();

    @ClassName("hoursPickerDial")
    public String hoursPickerDialClass();

    @ClassName("minutesPickerDial")
    public String minutesPickerDialClass();

    @ClassName("ampmPickerDial")
    public String ampmPickerDialClass();

    @ClassName("dtYearPickerDial")
    public String dtYearPickerDialClass();
    @ClassName("dtMonthPickerDial")
    public String dtMonthPickerDialClass();
    @ClassName("dtDatePickerDial")
    public String dtDatePickerDialClass();
    @ClassName("dtHoursPickerDial")
    public String dtHoursPickerDialClass();
    @ClassName("dtMinutesPickerDial")
    public String dtMinutesPickerDialClass();

    @ClassName("pickerCell")
    public String pickerCellClass();

    @ClassName("firstPickerCell")
    public String firstPickerCellClass();

    @ClassName("emptyDisplayValueCell")
    public String emptyDisplayValueCellClass();
}
