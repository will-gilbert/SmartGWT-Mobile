package com.smartgwt.mobile.client.util;

import java.io.IOException;
import java.util.Date;

import com.smartgwt.mobile.SGWTInternal;

public class LogicalTime extends Date {

    private static int getLogicalHours(Date d) {
        return d.getHours();
    }

    private static int getLogicalMinutes(Date d) {
        return d.getMinutes();
    }

    private static int getLogicalSeconds(Date d) {
        return d.getSeconds();
    }

    @SGWTInternal
    public static Appendable _appendTo(Appendable a, Date d) throws IOException {
        final int hrs = getLogicalHours(d),
                  min = getLogicalMinutes(d),
                  sec = getLogicalSeconds(d);
        if (hrs < 10) a.append('0');
        a.append(Integer.toString(hrs)).append(':');
        if (min < 10) a.append('0');
        a.append(Integer.toString(min)).append(':');
        if (sec < 10) a.append('0');
        a.append(Integer.toString(sec));
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

    LogicalTime(long time) {
        super(time);
    }

    /**
     * Creates a logical time from the given hours, minutes, and seconds.
     * 
     * @param hrs hours, 0 - 23
     * @param min minutes, 0 - 59
     * @param sec seconds, 0 - 59
     */
    public LogicalTime(int hrs, int min, int sec) {
        super(0L);
        setSeconds(sec);
        setMinutes(min);
        setHours(hrs);
    }

    /**
     * Returns the hours of this logical time.
     * @return the hours, 0 - 23.
     */
    public final int getLogicalHours() {
        return getLogicalHours(this);
    }

    /**
     * Returns the minutes of this logical time.
     * @return the minutes, 0 - 59.
     */
    public final int getLogicalMinutes() {
        return getLogicalMinutes(this);
    }

    /**
     * Returns the seconds of this logical time.
     * @return the seconds, 0 - 59.
     */
    public final int getLogicalSeconds() {
        return getLogicalSeconds(this);
    }

    @Override
    public LogicalTime clone() {
        return new LogicalTime(getTime());
    }

    @Override
    public int compareTo(Date other) {
        if (!(other instanceof LogicalTime)) return super.compareTo(other);
        return (getCode() - ((LogicalTime)other).getCode());
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof LogicalTime)) return super.equals(other);
        return (getCode() == ((LogicalTime)other).getCode());
    }

    private final int getCode() {
        return (getLogicalSeconds() + 60 * (getLogicalMinutes() + 60 * getLogicalHours()));
    }

    @Override
    public int hashCode() {
        return getCode();
    }

    /**
     * Appends the string representation of this logical time to <code>a</code>.
     * 
     * @param a where to append the string representation.
     * @return <code>a</code>
     * @throws IOException if one of the appendTo() operations throws an <code>IOException</code>.
     */
    public Appendable appendTo(Appendable a) throws IOException {
        return _appendTo(a, this);
    }

    /**
     * Appends the string representation of this logical time to <code>sb</code>.
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
