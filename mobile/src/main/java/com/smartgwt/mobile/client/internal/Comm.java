package com.smartgwt.mobile.client.internal;

import com.google.gwt.regexp.shared.RegExp;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public final class Comm {

    public static String indent = "    ";

    public static boolean legacyJSMode = false;

    public static final RegExp SIMPLE_IDENTIFIER_RE = RegExp.compile("^[\\$_a-zA-Z][\\$\\w]*$");

    public static boolean xmlSchemaMode = false;

    private Comm() {}
}
