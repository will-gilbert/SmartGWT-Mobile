package com.smartgwt.mobile.internal.gwt;

import java.util.SortedSet;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.linker.ConfigurationProperty;
import com.google.gwt.core.ext.linker.PropertyProviderGenerator;
import com.google.gwt.dev.About;
import com.google.gwt.user.rebind.StringSourceWriter;

public class MinGWTVersionPropertyGenerator implements PropertyProviderGenerator {

    private static final int lexicographicCompare(int[] lhs, int[] rhs) {
        int i = 0;
        for (; i < lhs.length; ++i) {
            if (i >= rhs.length) return 1; // e.g. [ 2, 4, 0 ] > [ 2, 4 ]
            final int c = lhs[i] - rhs[i];
            if (c != 0) return c;
        }
        if (i < rhs.length) return -1; // e.g. [ 2, 4 ] < [ 2, 4, 0 ]
        return 0;
    }

    @Override
    public String generate(TreeLogger logger, SortedSet<String> possibleValues,
            String fallback, SortedSet<ConfigurationProperty> configProperties)
            throws UnableToCompleteException {
        StringSourceWriter body = new StringSourceWriter();
        body.println("{");
        body.indent();

        final int[] versionArray = About.getGwtVersionArray(); // GWT.getVersion() is not available to `MinGWTVersionPropertyGenerator'.
        if (lexicographicCompare(versionArray, new int[] { 2, 4 }) < 0) {
            body.println("return 'legacy';");
        } else if (lexicographicCompare(versionArray, new int[] { 2, 5 }) < 0) {
            body.println("return 'v2_4';");
        } else {
            body.println("return 'v2_5';");
        }

        body.outdent();
        body.println("}");
        return body.toString();
    }
}
