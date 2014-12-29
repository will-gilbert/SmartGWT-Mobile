package com.smartgwt.mobile.client.util;

import java.io.IOException;
import java.util.Date;

import com.smartgwt.mobile.SGWTInternal;

public class LogicalDate extends Date {

    private static int getLogicalYear(Date d) {
        return d.getYear() + 1900;
    }

    private static int getLogicalMonth(Date d) {
        return d.getMonth();
    }

    public static int getLogicalDate(Date d) {
        return d.getDate();
    }

    @SGWTInternal
    public static Appendable _appendTo(Appendable a, Date d) throws IOException {
        final int year = getLogicalYear(d),
                  month = getLogicalMonth(d),
                  date = getLogicalDate(d);
        a.append(Integer.toString(year)).append('-');
        if (month < 9) a.append('0');
        a.append(Integer.toString(month + 1)).append('-');
        if (date < 10) a.append('0');
        a.append(Integer.toString(date));
        return a;
    }

    @SGWTInternal
    public static StringBuilder _appendTo(StringBuilder sb, Date d) {
        try {
            return (StringBuilder)_appendTo((Appendable)sb, d);
        } catch (IOException ex) {
            assert false;
            throw new RuntimeException(ex);
        }
    }

    @SGWTInternal
    public static String _toString(Date d) {
        return _appendTo(new StringBuilder(), d).toString();
    }

    LogicalDate(long time) {
        super(time);
    }

    public LogicalDate() {}

    /**
     * Creates a logical date from the given year, month, and date.
     * 
     * @param year the full year (2012 is the year 2012).
     * @param month month number, 0 - 11 (0 is January).
     * @param date day of month, 1 - 31.
     */
    public LogicalDate(int year, int month, int date) {
        super(year - 1900, month, date);
        setSeconds(0);
        setMinutes(0);
        setHours(12);
    }

    /**
     * Returns the year of this logical date.
     * @return the full year (2012 is the year 2012).
     */
    public final int getLogicalYear() {
        return getLogicalYear(this);
    }

    /**
     * Returns the month number of this logical date.
     * @return the month number, 0 - 11 (0 is January).
     */
    public final int getLogicalMonth() {
        return getLogicalMonth(this);
    }

    /**
     * Returns the day of month of this logical date.
     * @return the date, a number between 1 and 31, inclusive.
     */
    public final int getLogicalDate() {
        return getLogicalDate(this);
    }

    @Override
    public LogicalDate clone() {
        return new LogicalDate(getTime());
    }

    @Override
    public int compareTo(Date other) {
        if (!(other instanceof LogicalDate)) return super.compareTo(other);
        final LogicalDate otherD = (LogicalDate)other;
        int del;
        del = getLogicalYear() - otherD.getLogicalYear();
        if (del != 0) return del;
        del = getLogicalMonth() - otherD.getLogicalMonth();
        if (del != 0) return del;
        return (getLogicalDate() - otherD.getLogicalDate());
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof LogicalDate)) return super.equals(other);
        final LogicalDate otherD = (LogicalDate)other;
        return (getLogicalYear() == otherD.getLogicalYear() &&
                getLogicalMonth() == otherD.getLogicalMonth() &&
                getLogicalDate() == otherD.getLogicalDate());
    }

    // In order for hashCode() to be consistent with equals(), need to override to make the
    // calculation dependent on the year, month, and date only.
    @Override
    public int hashCode() {
        return (getLogicalDate() + 32 * (getLogicalMonth() + 12 * getLogicalYear()));
    }

    /**
     * Appends the string representation of this logical date to <code>a</code>.
     * 
     * @param a where to append the string representation.
     * @return <code>a</code>
     * @throws IOException if one of the appendTo() operations throws an <code>IOException</code>.
     */
    public Appendable appendTo(Appendable a) throws IOException {
        return _appendTo(a, this);
    }

    /**
     * Appends the string representation of this logical date to <code>sb</code>.
     * 
     * @param sb the <code>StringBuilder</code> to append to.
     * @return <code>sb</code>.
     */
    public StringBuilder appendTo(StringBuilder sb) {
        return _appendTo(sb, this);
    }

    @Override
    public String toString() {
        return _toString(this);
    }
}
