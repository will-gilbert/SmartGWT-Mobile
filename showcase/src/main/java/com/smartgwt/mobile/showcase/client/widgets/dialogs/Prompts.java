package com.smartgwt.mobile.showcase.client.widgets.dialogs;

import com.google.gwt.user.client.Timer;
import com.smartgwt.mobile.client.util.BooleanCallback;
import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.util.ValueCallback;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;

public class Prompts extends ScrollablePanel {

    public Prompts(String title) {
        super(title);

        Button sayButton = new Button("Say");
        sayButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                SC.say("Hello!\n\n" +
                        "<p><b>NOTE:</b> The code for this is:<br><code>SC.say(\"Hello!\");</code>");
            }
        });
        addMember(sayButton);

        Button askButton = new Button("Ask");
        askButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                SC.ask("Is it raining outside?", new BooleanCallback() {

                    @Override
                    public void execute(Boolean b) {
                        if (b != null) {
                            if (b.booleanValue()) {
                                SC.say("It is raining.");
                            } else {
                                SC.say("It isn't raining.");
                            }
                        }
                    }
                });
            }
        });
        addMember(askButton);

        Button askForValueButton = new Button("Ask for Value");
        askForValueButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                SC.askforValue("What is your name?", new ValueCallback() {

                    @Override
                    public void execute(String value) {
                        if (value != null) {
                            SC.say("Hello, " + value + "!");
                        }
                    }
                });
            }
        });
        addMember(askForValueButton);

        Button promptButton = new Button("Prompt");
        promptButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                SC.showPrompt("Waiting", "In 3 seconds, I will call <code>SC.clearPrompt()</code>.");
                new Timer() {

                    @Override
                    public void run() {
                        SC.clearPrompt();
                        SC.say("Thanks for waiting!");
                    }
                }.schedule(3 * 1000);
            }
        });
        addMember(promptButton);

        Button confirmButton = new Button("Confirm");
        confirmButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                SC.confirm("Confirm", "Shall I schedule the reminder?", new BooleanCallback() {

                    @Override
                    public void execute(Boolean b) {
                        if (b != null && b.booleanValue()) {
                            SC.say("Success", "The reminder has been scheduled.");
                        }
                    }
                });
            }
        });
        addMember(confirmButton);
    }
}
