package com.smartgwt.mobile.client.core;

public class Rectangle {

    private int left, top, width, height;

    public Rectangle(int left, int top, int width, int height) {
        if (width < 0) throw new IllegalArgumentException("`width' cannot be negative.");
        if (height < 0) throw new IllegalArgumentException("`height' cannot be negative.");
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    public final int getLeft() {
        return left;
    }

    public final int getTop() {
        return top;
    }

    public final int getWidth() {
        return width;
    }

    public final int getHeight() {
        return height;
    }
}
