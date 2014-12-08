package com.smartgwt.mobile.showcase.client.widgets.other;

import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.types.SelectionType;
import com.smartgwt.mobile.client.widgets.ImgButton;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;

public class ImgButtons extends ScrollablePanel {

    public ImgButtons(String title) {
        super(title);

        final Panel infoPanel = new Panel();
        infoPanel.setStyleName("sc-rounded-panel");
        infoPanel.setContents("Tap on the buttons for Bold, Italic, and Underline and note " +
                "that they stick in a down state. Tap on the buttons for left, center and " +
                "right justify and note that they are mutually exclusive.");
        infoPanel.setMargin(10);
        addMember(infoPanel);

        final ToolStrip toolStrip = new ToolStrip();
        toolStrip.setAlign(Alignment.CENTER);

        final ImgButton boldButton = new ImgButton("sampleImages/icons/24/text_bold.png", 24, 24);
        boldButton.setActionType(SelectionType.CHECKBOX);
        final ImgButton italicsButton = new ImgButton("sampleImages/icons/24/text_italics.png", 24, 24);
        italicsButton.setActionType(SelectionType.CHECKBOX);
        final ImgButton underlineButton = new ImgButton("sampleImages/icons/24/text_underlined.png", 24, 24);
        underlineButton.setActionType(SelectionType.CHECKBOX);

        final ImgButton alignLeftButton = new ImgButton("sampleImages/icons/24/text_align_left.png", 24, 24);
        alignLeftButton.setActionType(SelectionType.RADIO);
        alignLeftButton.setRadioGroup("textAlign");
        final ImgButton alignCenterButton = new ImgButton("sampleImages/icons/24/text_align_center.png", 24, 24);
        alignCenterButton.setActionType(SelectionType.RADIO);
        alignCenterButton.setRadioGroup("textAlign");
        final ImgButton alignRightButton = new ImgButton("sampleImages/icons/24/text_align_right.png", 24, 24);
        alignRightButton.setActionType(SelectionType.RADIO);
        alignRightButton.setRadioGroup("textAlign");

        toolStrip.addMembers(boldButton, italicsButton, underlineButton, alignLeftButton, alignCenterButton, alignRightButton);
        addMember(toolStrip);
    }
}
