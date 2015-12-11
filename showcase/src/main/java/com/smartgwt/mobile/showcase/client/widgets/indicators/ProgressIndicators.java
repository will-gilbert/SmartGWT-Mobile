package com.smartgwt.mobile.showcase.client.widgets.indicators;

import com.smartgwt.mobile.client.internal.util.AnimationUtil;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.widgets.HRWidget;
import com.smartgwt.mobile.client.widgets.Header2;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.Progressbar;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.events.PercentChangedEvent;
import com.smartgwt.mobile.client.widgets.events.PercentChangedHandler;
import com.smartgwt.mobile.client.widgets.layout.HLayout;

public class ProgressIndicators extends ScrollablePanel {

    private Progressbar progressbar;

    public ProgressIndicators(String title) {
        super(title);
        this.setWidth("100%");
        final Panel output = new Panel();
        output.setStyleName("sc-rounded-panel");
        output.getElement().getStyle().setProperty("textAlign", "center");
        output.getElement().getStyle().setOpacity(0.0);
        HLayout panelWrapper = new HLayout();
        panelWrapper.setWidth("auto");
        panelWrapper.setHeight("auto");
        panelWrapper.setLayoutMargin(20);
        panelWrapper.setPaddingAsLayoutMargin(true);
        panelWrapper.setMembersMargin(20);
        panelWrapper.addMember(output);
        this.addMember(new Header2("Progressbar in a view"));
        HLayout hlayout = new HLayout();
        hlayout.setWidth("100%");
        hlayout.setAlign(Alignment.CENTER);
        progressbar = new Progressbar();
        progressbar.addPercentChangedHandler(new PercentChangedHandler() {
            @Override
            public void onPercentChanged(PercentChangedEvent event) {
                output.setContents("Percent = "+event.getPercentage());
                AnimationUtil.fadeTransition(output, true);
            }
        });
        hlayout.addMember(progressbar);
        addMember(hlayout);
        addMember(new HRWidget());
        addMember(panelWrapper);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        assert progressbar.isAttached();
        progressbar.setPercentDone(100, 5);
    }
}
