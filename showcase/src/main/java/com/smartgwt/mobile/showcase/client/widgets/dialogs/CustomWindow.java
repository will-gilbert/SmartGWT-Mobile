package com.smartgwt.mobile.showcase.client.widgets.dialogs;

import com.smartgwt.mobile.client.types.AnimationEffect;
import com.smartgwt.mobile.client.widgets.ActivityIndicator;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.Label;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.Window;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;

public class CustomWindow extends ScrollablePanel {

    private Window loadingWindow;
    private Button showWindowButton;

    public CustomWindow(String title) {
        super(title);

        // NOTE: See `index.html' for the accompanying CSS.
        loadingWindow = new Window();

        loadingWindow.setIsModal(true);
        loadingWindow.setAnimateShowEffect(AnimationEffect.FADE);
        loadingWindow.setAnimateHideEffect(AnimationEffect.FADE);
        loadingWindow.setBackgroundStyle("app-customLoadingWindowBackground");

        loadingWindow.addChild(new ActivityIndicator());
        loadingWindow.addChild(new Label("Loading...") {{ setStyleName("app-customLoadingLabel"); }});



        loadingWindow.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loadingWindow.hide();
            }
        });

        showWindowButton = new Button("Show Window");

        showWindowButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loadingWindow.show();
            }
        });
        
        addMember(showWindowButton);
    }
}
