/*
 * SmartGWT Mobile
 * Copyright 2008 and beyond, Isomorphic Software, Inc.
 *
 * SmartGWT Mobile is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  SmartGWT Mobile is also
 * available under typical commercial license terms - see
 * http://smartclient.com/license
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package com.smartgwt.mobile.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.MetaElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.util.PageStaticImpl;
import com.smartgwt.mobile.client.types.PageOrientation;
import com.smartgwt.mobile.client.util.events.OrientationChangeEvent;
import com.smartgwt.mobile.client.util.events.OrientationChangeHandler;

public class Page {

    private static final PageStaticImpl IMPL = GWT.create(PageStaticImpl.class);

    private final static String[] PROTOCOL_URLS = { "http://", "https://", "file://",
            "mailto:", "app-resource:", "data:" };

    private final static String DEFAULT_ISOMORPHIC_DIR = "/isomorphic/";
    private static String APP_DIR;
    private static String ISOMORPHIC_DIR;

    private static HandlerManager handlerManager;
    private static boolean setUpOrientationChangeListener = false;

    static {
        // get the path to the current file and strip off any query params and leaf file names
        String filePath = Window.Location.getHref();
        // strip off anything after a "?"
        if (filePath.contains("?")) filePath = filePath.substring(0,filePath.indexOf("?"));
        // # references node IDs which, according to the W3C cannot have slashes in them, but in
        // the AJAX world, # refs are often used to provide back button support rather than
        // actually reference any node ids in the DOM, so it's best that we don't break if # refs
        // contain slashes in the value.
        if (filePath.contains("#")) filePath = filePath.substring(0,filePath.indexOf("#"));
        // strip off the leaf file name if one exists
        if (filePath.charAt(filePath.length() - 1) != '/') {
            filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
        }
        Page.APP_DIR = filePath;
        Page.setIsomorphicDir(null);
    }

    public static void updateViewport(Float scale, Integer width, Integer height, Boolean scalable) {
        _updateViewport(scale,
                width == null ? null : Integer.toString(width.intValue()),
                height == null ? null : Integer.toString(height.intValue()),
                scalable,
                "minimal-ui");
    }

    @SGWTInternal
    public static void _updateViewport(Float scale, String width, String height, Boolean scalable, String extraViewportParams) {
        final StringBuilder sb = new StringBuilder();

        if (scale != null) {
            sb.append("initial-scale=").append(scale.floatValue());
        }
        if (width != null) {
            if (sb.length() != 0) sb.append(", ");
            sb.append("width=").append(width);
        }
        if (height != null) {
            if (sb.length() != 0) sb.append(", ");
            sb.append("height=").append(height);
        }
        if (scalable != null) {
            if (sb.length() != 0) sb.append(", ");
            sb.append("user-scalable=").append(scalable.booleanValue() ? "yes" : "no");
            // setting user-scalable to 'no' seems to reliably disable pinch zooming
            // However on pivot the iPhone zooms by default and this seems to still occur
            // with user-scalable set to 'no'. If a desired 'scale' was specified,
            // setting the min/max scale to it appears to really disable scale on pivot
            if (scalable == false && scale != null) {
                sb.append(", minimum-scale=").append(scale.floatValue()).append(", maximum-scale=").append(scale.floatValue());
            }
        }
        if (extraViewportParams != null && !extraViewportParams.isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(extraViewportParams);
        }

        final NodeList<Element> metaTags = Document.get().getElementsByTagName("meta");
        MetaElement vpTag = null;
        // remove all but the last viewport <meta> tag and select the last one
        for (int i = 0; i < metaTags.getLength(); /*empty*/) {
            if ("viewport".equals(metaTags.getItem(i).<MetaElement>cast().getName())) {
                if (vpTag != null) {
                    vpTag.getParentNode().removeChild(vpTag);
                    vpTag = metaTags.getItem(i - 1).cast();
                } else {
                    vpTag = metaTags.getItem(i).cast();
                    ++i;
                }
            } else ++i;
        }
        if (vpTag != null) {
            vpTag.setContent(sb.toString());
        } else {
            vpTag = Document.get().createMetaElement();
            vpTag.setName("viewport");
            vpTag.setContent(sb.toString());
            Document.get().getElementsByTagName("head").getItem(0).appendChild(vpTag);
        }
    }

    public static native int getHeight() /*-{
        return $wnd.innerHeight << 0;
    }-*/;

    public static void setIsomorphicDir(String URL) {
        Page.ISOMORPHIC_DIR = combineURLs(Page.APP_DIR, URL != null ? URL
                : Page.DEFAULT_ISOMORPHIC_DIR);
    }

    public static String getIsomorphicDir() {
        return Page.ISOMORPHIC_DIR;
    }

    public static PageOrientation getOrientation() {
        return IMPL.getOrientation();
    }

    /**
     * Return the protocol for a given URL. Returns the full protocol (eg: "http://"), or the
     * empty string ("") if protocol was not understood.
     * 
     * @param URL URL to get protocol for.
     * @return Protocol for the URL, or "" if not found/recognized.
     */
    public static String getProtocol(String URL) {
        for (String protocol : Page.PROTOCOL_URLS) {
            if (URL.startsWith(protocol)) {
                return protocol;
            }
        }
        return "";
    }

    public static native int getWidth() /*-{
        return $wnd.innerWidth << 0;
    }-*/;

    /**
     * Combine a "masterURL" and a "localURL" into a single URL. If the localURL is a fully
     * specified URL (starts with "http:", "https:" or "file:"), we use that. If the localURL
     * is a relative URL, combine with the masterURL to a single URL.
     * 
     * @param masterURL Master URL.
     * @param localURL Local URL.
     * @return Combined URL.
     */
    public static String combineURLs(String masterURL, String localURL) {
        localURL = getURL(localURL);

        // if local directory was specified as a full URL, simply return that
        if (masterURL == null || masterURL.length() == 0
                || getProtocol(localURL).length() != 0) {
            return localURL;
        }

        String masterProtocol = getProtocol(masterURL);
        if (localURL.startsWith("/")) {
            if (masterProtocol.length() == 0) {
                // if master URL has no protocol and hence no host, empty it so
                // that we we will the
                // already absolute "localURL" unchanged
                masterURL = "";
            } else if (masterURL.indexOf("/", masterProtocol.length()) != -1) {
                // We want to chop off everything in the master URL after the
                // first "/"
                masterURL = masterURL.substring(0,
                        masterURL.indexOf("/", masterProtocol.length()));
            }

            // eliminate any "./" entries in the localURL
            // go up a directory in the masterURL for any "../" in the localURL
        } else if (localURL.indexOf("./") > -1) {
            // >IDocument This case never occurs during normal use and should be
            // considered an
            // internal-use only facility for, eg prototyping widgets and
            // temporarily stealing
            // images from other widget's directories.. //<IDocument
            // alert("backups in local URL: " + localURL);

            // break up masterURL into protocol and directories
            // break up localURL into directories
            masterURL = masterURL.substring(masterProtocol.length(), masterURL.length() - 1);
            String[] masterDirs = masterURL.split("/");
            String[] localDirs = localURL.split("/");

            // the first "dir" is actually the host
            masterURL = masterProtocol + masterDirs[0] + "/";
            localURL = "";
            boolean up = true;
            int masterInd = masterDirs.length;
            for (int i = 0; i < localDirs.length; i++) {
                String localDir = localDirs[i];
                if (up && ".".equals(localDir)) {
                    continue;
                } else if (up && "..".equals(localDir)) {
                    masterInd--;
                } else {
                    up = false;
                    localURL += localDir + "/";
                }
            }
            for (int i = 1; i < masterInd; i++) {
                masterURL += masterDirs[i] + "/";
            }
            if (localURL.length() > 0) {
                localURL = localURL.substring(0, localURL.length() - 1);
            }
        }
        // return the combined URLs
        return masterURL + localURL;
    }

    /**
     * Return a full URL for a relative path that uses a special prefix "[ISOMORPHIC]"
     * 
     * @param URL Local file name for the image.
     * @return URL.
     */
    public static String getURL(String URL) {
        if (URL.startsWith("[")) {
            int endIndex = URL.indexOf("]");
            if (endIndex > 0) {
                String directoryName = URL.substring(1, endIndex).toUpperCase();
                if ("ISOMORPHIC".equals(directoryName)) {
                    String isomorphicDir = Page.ISOMORPHIC_DIR;
                    if (!isomorphicDir.endsWith("/")) {
                        isomorphicDir += "/";
                    }
                    if (URL.charAt(endIndex + 1) == '/') {
                        endIndex++;
                    }
                    URL = combineURLs(isomorphicDir, URL.substring(endIndex + 1));
                }
            }
        }
        return URL;
    }

    private static HandlerManager ensureHandlerManager() {
        if (handlerManager == null) handlerManager = new HandlerManager(null);
        return handlerManager;
    }

    private static void setOrientationChangeEvent() {
        IMPL.setupOrientationChangeListener();
        setUpOrientationChangeListener = true;
    }

    public static HandlerRegistration addOrientationChangeHandler(OrientationChangeHandler handler) {
        if (!setUpOrientationChangeListener) setOrientationChangeEvent();
        return ensureHandlerManager().addHandler(OrientationChangeEvent.getType(), handler);
    }

    private static <H extends com.google.gwt.event.shared.EventHandler> void fireEvent(GwtEvent<H> event) {
        if (handlerManager != null) {
            handlerManager.fireEvent(event);
        }
    }
}