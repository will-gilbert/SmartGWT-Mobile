package com.smartgwt.mobile.client.internal.util;

import com.google.gwt.regexp.shared.RegExp;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public final class CSSUtil {

    // GWT has a bug that causes escaped CSS3 not() selectors to result in something like `:not(.className \)'.
    // This causes problems on iOS 6.
    //
    // This function simply corrects the invalid not() selector syntax by removing the backslash
    // before the closing parenthesis.
    public static String fixNotSelectors(String cssText) {
        final RegExp re = RegExp.compile(":not\\(([^)]*?)\\\\\\)", "g");
        return re.replace(cssText, ":not($1)");
    }

    private CSSUtil() {}
}
