package com.smartgwt.mobile.client.internal.util;

public class Pair<T1, T2> {

    public static <T1, T2> Pair<T1, T2> create(T1 first, T2 second) {
        return new Pair<T1, T2>(first, second);
    }

    private final T1 first;
    private final T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public final T1 getFirst() {
        return first;
    }

    public final T2 getSecond() {
        return second;
    }
}
