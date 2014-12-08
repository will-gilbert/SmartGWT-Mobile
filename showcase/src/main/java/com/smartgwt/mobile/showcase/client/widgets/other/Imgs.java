package com.smartgwt.mobile.showcase.client.widgets.other;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.smartgwt.mobile.client.types.ImageStyle;
import com.smartgwt.mobile.client.widgets.Img;
import com.smartgwt.mobile.client.widgets.Label;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;

public class Imgs extends ScrollablePanel {

    private static void setImgStyle(Img img) {
        final Style style = img.getElement().getStyle();
        style.setBorderWidth(1, Style.Unit.PX);
        style.setBorderStyle(Style.BorderStyle.SOLID);
        style.setBorderColor("black");
        style.setBackgroundColor("white");
    }
    public Imgs(String title) {
        super(title);
        getElement().getStyle().setPadding(5, Style.Unit.PX);

        addMember(new Label("imageType NORMAL"));
        final Img img1 = new Img("./sampleImages/star.gif");
        setImgStyle(img1);
        img1.setImageType(ImageStyle.NORMAL);
        addMember(img1);

        final Label label2 = new Label("imageType CENTER");
        Style style = label2.getElement().getStyle();
        style.setMarginTop(15, Style.Unit.PX);
        addMember(label2);
        final Img img2 = new Img("./sampleImages/star.gif");
        setImgStyle(img2);
        img2.setImageType(ImageStyle.CENTER);
        addMember(img2);

        final Label label3 = new Label("imageType STRETCH");
        style = label3.getElement().getStyle();
        style.setMarginTop(15, Style.Unit.PX);
        addMember(label3);
        final Img img3 = new Img("./sampleImages/star.gif");
        setImgStyle(img3);
        img3.setImageType(ImageStyle.STRETCH);
        addMember(img3);

        final Label label4 = new Label("A large image:");
        style = label4.getElement().getStyle();
        style.setMarginTop(15, Style.Unit.PX);
        addMember(label4);
        final Img img4 = new Img("./sampleImages/cpu.jpg");
        img4.setWidth(Window.getClientWidth() - 10);
        img4.setHeight(400);
        img4.setImageType(ImageStyle.NORMAL);
        addMember(img4);
    }
}
