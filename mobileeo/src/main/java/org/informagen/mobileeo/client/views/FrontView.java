package org.informagen.mobileeo.client.views;


// EO Vortaro - Application
import org.informagen.mobileeo.client.application.Callback;

// EO Vortaro - Presenters
import org.informagen.mobileeo.client.presenters.FrontPresenter;

// SmartGWT Mobile - Widgets
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.Dialog;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.Label;


import com.smartgwt.mobile.client.widgets.tableview.TableView;

// SmartGWT Mobile - Core
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.types.NavigationMode;
import com.smartgwt.mobile.client.types.SelectionStyle;
import com.smartgwt.mobile.client.types.TableMode;

// SmartGWT Mobile - Events
import com.smartgwt.mobile.client.widgets.grid.events.RowContextClickHandler;
import com.smartgwt.mobile.client.widgets.grid.events.RowContextClickEvent;

//SmartGWT Mobile - Navigation Bar
import com.smartgwt.mobile.client.widgets.layout.NavStack;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickHandler;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickEvent;

import org.informagen.mobileeo.client.icons.Icons;

// GWT
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

// Google Inject Annotation
import com.google.inject.Inject;

public class FrontView implements FrontPresenter.View, RecordNavigationClickHandler {


    // final Menu<String> interchangeMenu;
    final Panel panel = new ScrollablePanel("EO Retaĵaro");

    Callback<String> recordClickedCallback = null;
    Callback<String> goToWebSiteCallback = null;


    final NavStack navigationStack;

    @Inject
    public FrontView(NavStack navigationStack) {
        this.navigationStack = navigationStack;

/*        
        interchangeMenu = createMenu("eo-glossary", "Interŝanĝilo", "WordLookupPage");

	    // If this device supports adding web pages to the home screens, give the
	    //   the user the "FullScreen" option to do so
	    
        if(Browser.hasFullScreenMode() && Browser.isFullScreenMode() == false)
            addFullScreen();

        // For older browsers, give the user an option of download a better version
        if(Browser.isMSIE() || Browser.isOldFirefox()) 
            addBrowserAlternatives();
*/        

        addDictionaries();
        addPodcasts();
        addPreferences();

	}

    // FrontPresenter.View ---------------------------------------------------------------------

    @Override
    public Panel asPanel() {        
        return panel;
    }

    @Override
    public void setRecordClickedCallback(Callback<String> recordClickedCallback) {
        this.recordClickedCallback = recordClickedCallback;
    }

    @Override
    public void setGoToWebSiteCallback(Callback<String> goToWebSiteCallback) {
        this.goToWebSiteCallback = goToWebSiteCallback;
    }

    // RecordNavigationClickHandler ---------------------------------------------------------------------

    @Override
    public void onRecordNavigationClick(RecordNavigationClickEvent event) {
        final Record selectedRecord = event.getRecord();
        String key = selectedRecord.getAttribute("key");

        if(key != null && key.equals("lernu") && goToWebSiteCallback != null) {
            goToWebSiteCallback.onSuccess("http://lernu.net");
            return;
        }

        if (key != null && recordClickedCallback != null )
            recordClickedCallback.onSuccess(key);   
    }

    //-----------------------------------------------------------------------------------------

/*
    void addFullScreen() {
        RoundedBox roundedBox = new RoundedBox();
        roundedBox.add(createMenu("iPhone","Try Full Screen Mode", "Home.Fullscreen").bottomBorder(false));
        add(roundedBox);
    }
*/    

    void addDictionaries() {

        final Label title = new Label("Vortoj per Retservoj");
        final TableView tableView = createTableView();
        final RecordList recordList = new RecordList();

        title.setStyleName("Mobile-Heading");

        recordList.add(createTableRecord(
            "lernu", 
            "Lernu.net", 
            Icons.INSTANCE.lernu()
        ));

        recordList.add(createTableRecord(
            "WordOfTheDay", 
            "Vorto de l' Tago", 
            Icons.INSTANCE.calendar()
        ));

        recordList.add(createTableRecord(
            "EOGlossaryPage", 
            "Esperanto Vortaro", 
            Icons.INSTANCE.dictionary()
        ));

        recordList.add(createTableRecord(
            "WordLookup", 
            "Interŝanĝilo", 
            Icons.INSTANCE.eoGlossary()
        ));

        recordList.add(createTableRecord(
            "ESPDICLookup", 
            "ESPDIC Glosaro", 
            Icons.INSTANCE.espdic()
        ));

        tableView.setData(recordList);

        tableView.addRecordNavigationClickHandler(this);

        panel.addMember(title);
        panel.addMember(tableView);
    }

    void addPodcasts() {

        final Label title = new Label("Podkastoj kaj Aŭdvidoj");
        final TableView tableView = createTableView();
        final RecordList recordList = new RecordList();

        title.setStyleName("Mobile-Heading");

        recordList.add(createTableRecord(
            "radioverda", 
            "Radio Verda", 
            Icons.INSTANCE.radioVerda()
        ));

        recordList.add(createTableRecord(
            "esperanta-retradio", 
            "Esperanta Retradio", 
            Icons.INSTANCE.esperantaRetradio()
        ));

        tableView.setData(recordList);

        tableView.addRecordNavigationClickHandler(this);

        panel.addMember(title);
        panel.addMember(tableView);
    }

    void addPreferences() {

        final TableView tableView = createTableView();
        final RecordList recordList = new RecordList();

        recordList.add(createTableRecord(
            "settings", 
            "Preferoj", 
            Icons.INSTANCE.settings()
        ));

        tableView.setData(recordList);

        tableView.addRecordNavigationClickHandler(this);

        panel.addMember(tableView);
    }

/*        
        // Podcasts/Audio-Visual --------------------------------------------------------------
        roundedBox = new RoundedBox();

        if(Browser.hasAudio()) {
            roundedBox.add(createMenu("radioverda", "Radio Verda", "RVPodcastPage") );
            roundedBox.add(createMenu("esperanta-retradio", "Esperanta Retradio", "EsperantaRetradioPage") );
        } else {
            roundedBox.add(createDownloadMenu("radioverda", "Radio Verda", "http://radioverda.com/", "Vizitu") );
            roundedBox.add(createDownloadMenu("esperanta-retradio", "Esperanta Retradio", "http://peranto.posterous.com/", "Vizitu") );
        }


    }
*/
    //===========================================

    private TableView createTableView() {
        final TableView tableView = new TableView();
        tableView.setTitleField("title");
        tableView.setShowNavigation(true);
        tableView.setSelectionType(SelectionStyle.SINGLE);
        tableView.setNavigationMode(NavigationMode.WHOLE_RECORD);
        tableView.setParentNavStack(navigationStack);
        tableView.setTableMode(TableMode.GROUPED);
        tableView.setHiliteOnTouch(true);
        tableView.setShowIcons(true);

        return tableView;
    }


    private Record createTableRecord(String key, String title, ImageResource imageResource) {
        Record record = new Record();
        record.setAttribute("key", key);
        record.setAttribute("title", title);
        if(imageResource != null)
            record.setAttribute("icon", imageResource);
        return record;
    }


/*
    private void addBrowserAlternatives() {
        Panel roundedBox = new Panel();
        roundedBox.add(new TextBox("<h3>This site works best with...</h3>"));
        roundedBox.add(createDownloadMenu("safari","Apple Safari", "http://www.apple.com/safari/download/", "Download"));
        roundedBox.add(createDownloadMenu("chrome","Google Chrome", "http://www.google.com/chrome", "Download"));
        roundedBox.add(createDownloadMenu("firefox","Firefox", "http://www.mozilla.com/", "Download"));
        roundedBox.showLastBorder(false);
        add(roundedBox);
    }
*/


    

}
