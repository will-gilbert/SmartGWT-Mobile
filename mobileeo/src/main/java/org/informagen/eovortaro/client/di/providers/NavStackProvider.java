package org.informagen.eovortaro.client.di.providers;

//SmartGWT Mobile
import com.smartgwt.mobile.client.widgets.layout.NavStack;

// Google Injection
import com.google.inject.Inject;
import com.google.inject.Provider;
  
public class NavStackProvider implements Provider<NavStack> {

    // Global application singleton
    private static final NavStack navStack = new NavStack();

    @Override
    public NavStack get() {
        return navStack;
    }
}
