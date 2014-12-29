package com.smartgwt.mobile.internal.gwt.useragent.rebind;

import com.google.gwt.user.rebind.StringSourceWriter;

class UserAgentPropertyGeneratorPredicate {

    private final StringSourceWriter predicateWriter = new StringSourceWriter();
    private String userAgent, returnValue;

    public UserAgentPropertyGeneratorPredicate(String userAgent) {
        this.userAgent = userAgent;
    }

    public final UserAgentPropertyGeneratorPredicate getPredicateBlock() {
        return this;
    }

    public final String getUserAgent() {
        return userAgent;
    }

    public final String getReturnValue() {
        return returnValue;
    }

    public UserAgentPropertyGeneratorPredicate println(String s) {
        predicateWriter.println(s);
        return this;
    }

    public UserAgentPropertyGeneratorPredicate returns(String s) {
        returnValue = s;
        return this;
    }

    @Override
    public String toString() {
        return predicateWriter.toString();
    }
}
