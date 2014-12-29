package com.smartgwt.mobile.client.widgets;

public class ImgButton extends Img {

    public ImgButton() {
        init();
    }

    public ImgButton(String src) {
        super(src);
        init();
    }

    public ImgButton(String src, int width, int height) {
        super(src, width, height);
        init();
    }

    private void init() {
        super.setShowDown(true);
    }
}
