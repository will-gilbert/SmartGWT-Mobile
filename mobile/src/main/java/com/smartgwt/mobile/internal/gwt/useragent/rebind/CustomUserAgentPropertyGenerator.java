package com.smartgwt.mobile.internal.gwt.useragent.rebind;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.linker.ConfigurationProperty;
import com.google.gwt.core.ext.linker.PropertyProviderGenerator;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.user.rebind.StringSourceWriter;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class CustomUserAgentPropertyGenerator implements PropertyProviderGenerator {

    private static final List<String> VALID_VALUES = Arrays.asList("android", "ipad", "iphone", "safari", "gecko1_8");

    private static UserAgentPropertyGeneratorPredicate[] PREDICATES = new UserAgentPropertyGeneratorPredicate[] {
        new UserAgentPropertyGeneratorPredicate("android")
            .getPredicateBlock()
            .println("return (ua.indexOf('android') != -1);")
            .returns("'android'"),

        // iPad
        // http://stackoverflow.com/questions/2153877/what-is-the-ipad-user-agent
        new UserAgentPropertyGeneratorPredicate("ipad")
            .getPredicateBlock()
            .println("return (ua.indexOf('ipad') != -1);")
            .returns("'ipad'"),

        // iPhone
        new UserAgentPropertyGeneratorPredicate("iphone")
            .getPredicateBlock()
            .println("return (ua.indexOf('iphone') != -1);")
            .returns("'iphone'"),

        // WebKit-based browsers (e.g. Safari, Chrome)
        new UserAgentPropertyGeneratorPredicate("safari")
            .getPredicateBlock()
            .println("return (")
            .println("    (ua.indexOf('webkit') != -1) ||")
            .println("    (ua.indexOf('chromeframe') != -1) ||")
            .println("    ('ActiveXObject' in $wnd && (function tryRegisterBhoIfNeeded() {")
            .println("                                     try {")
            .println("                                         var obj = new $wnd.ActiveXObject('ChromeTab.ChromeFrame');")
            .println("                                         if (obj) {")
            .println("                                             obj.registerBhoIfNeeded();")
            .println("                                             return true;")
            .println("                                         }")
            .println("                                     } catch (e) {}")
            .println("                                     return false;")
            .println("                                 })())")
            .println(");")
            .returns("'safari'"),

        // Gecko-based browsers (e.g. Firefox)
        new UserAgentPropertyGeneratorPredicate("gecko1_8")
            .getPredicateBlock()
            .println("return (ua.indexOf('gecko') != -1);")
            .returns("'gecko1_8'")
    };

    static String ucfirst(String str) {
        if (str == null) return null;
        else if (str.isEmpty()) return str;
        final char c0 = str.charAt(0);
        return Character.toUpperCase(c0) + str.substring(1);
    }

    static void writeUserAgentDetectionJavaScript(SourceWriter body, SortedSet<String> possibleValues) {
        body.println("var ua = navigator.userAgent.toLowerCase();");
        body.println("if (typeof $wnd == 'undefined') {");
        body.println("    var $wnd = window;");
        body.println("}");

        if (possibleValues != null) {
            for (int i = 0; i < PREDICATES.length; ++i) {
                final UserAgentPropertyGeneratorPredicate predicate = PREDICATES[i];
                if (possibleValues.contains(predicate.getUserAgent())) {
                    body.println("if ((function detect" + ucfirst(predicate.getUserAgent()) + "() {");
                    body.indent();
                    body.print(predicate.toString());
                    body.outdent();
                    body.println("})()) return " + predicate.getReturnValue() + ";");
                }
            }
        }

        body.println("return 'unknown';");
    }

    @Override
    public String generate(TreeLogger logger, SortedSet<String> possibleValues,
            String fallback, SortedSet<ConfigurationProperty> configProperties)
            throws UnableToCompleteException {
        for (String value : possibleValues) {
            if (!VALID_VALUES.contains(value) &&
                !value.startsWith("ie") &&
                !value.equals("opera"))
            {
                logger.log(TreeLogger.WARN, "Unsupported user.agent property value " + (value == null ? "null" : "'" + value + "'"));
            }
        }
        StringSourceWriter body = new StringSourceWriter();
        body.println("{");
        body.indent();
        writeUserAgentDetectionJavaScript(body, possibleValues);
        body.outdent();
        body.println("}");
        return body.toString();
    }
}
