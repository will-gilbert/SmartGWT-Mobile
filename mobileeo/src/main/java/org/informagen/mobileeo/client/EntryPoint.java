package org.informagen.mobileeo.client;

// EOVortaro - Application
import org.informagen.mobileeo.client.application.ApplicationController;
import org.informagen.mobileeo.client.application.Configuration;
import org.informagen.mobileeo.client.application.Analytics;

// GWT - Core, UI, Command
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.core.client.Scheduler;

// GWT - EventBus
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;

/**
 * Entry point for the application.  Name it 'EntryPoint' so that it's pretty
 *   obvious this is where it starts.
 */
 
public class EntryPoint implements com.google.gwt.core.client.EntryPoint {

	public void onModuleLoad() {
 
        Window.setTitle(Configuration.getProperty("windowTitle", "EO Vortaro"));
        
        Analytics.createInstance(Configuration.getProperty("analyticsKey"));
        Analytics.track(Analytics.APPLICATION,"startup");

        // Remove the loading spinner prior to loading the application
        RootPanel.getBodyElement().removeChild(RootPanel.get("App-loading").getElement());

        // Hide any SmartPhone location bar; this command is harmless for desktop browsers
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            public void execute() {
                Window.scrollTo(0, 0);
            }
        });

        // Create the 'ApplicationController' and inject the entire browser page as the container
        ApplicationController controller = new ApplicationController();
        controller.loadApplication(RootPanel.get("rootPanel"));

	}

}
