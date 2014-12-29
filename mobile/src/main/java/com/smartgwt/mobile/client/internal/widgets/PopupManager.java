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

package com.smartgwt.mobile.client.internal.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.internal.util.PopupHiddenCallback;
import com.smartgwt.mobile.client.internal.util.PopupShownCallback;
import com.smartgwt.mobile.client.internal.widgets.events.PopupDismissedEvent;

@SGWTInternal
public final class PopupManager {

    private interface OpDetails {
        /*empty*/
    }

    private static final class ShowPopupDetails implements OpDetails {
        public final Popup popup;

        public ShowPopupDetails(Popup popup) {
            assert popup != null;
            this.popup = popup;
        }
    }

    private static final class HidePopupDetails implements OpDetails {
        public static final HidePopupDetails INSTANCE = new HidePopupDetails();

        private HidePopupDetails() {
            /*empty*/
        }
    }

    private static transient final List<OpDetails> opDetailsQueue = new ArrayList<OpDetails>();
    private static transient ShowPopupDetails currentShownPopupDetails;

    private static final PopupShownCallback SHOWN_CALLBACK = new PopupShownCallback() {
        @Override
        public void execute() {
            assert currentShownPopupDetails == null;
            assert !opDetailsQueue.isEmpty();
            assert opDetailsQueue.get(0) instanceof ShowPopupDetails;
            currentShownPopupDetails = (ShowPopupDetails)opDetailsQueue.remove(0);
            assert currentShownPopupDetails.popup._isShown();
            if (!opDetailsQueue.isEmpty()) {
                final Popup currentShownPopup = currentShownPopupDetails.popup;
                PopupDismissedEvent.fire(currentShownPopup);
                currentShownPopupDetails = null;

                final OpDetails nextOp = opDetailsQueue.get(0);
                if (nextOp instanceof ShowPopupDetails) {
                    opDetailsQueue.add(0, HidePopupDetails.INSTANCE);
                }
                currentShownPopup.hide(HIDDEN_CALLBACK);
            }
        }
    };
    private static final PopupHiddenCallback HIDDEN_CALLBACK = new PopupHiddenCallback() {
        @Override
        public void execute() {
            assert currentShownPopupDetails == null;
            assert !opDetailsQueue.isEmpty();
            assert opDetailsQueue.get(0) instanceof HidePopupDetails;
            do {
                opDetailsQueue.remove(0);
            } while (!opDetailsQueue.isEmpty() && opDetailsQueue.get(0) instanceof HidePopupDetails);
            if (!opDetailsQueue.isEmpty()) {
                final OpDetails nextOp = opDetailsQueue.get(0);
                assert nextOp instanceof ShowPopupDetails;
                callShow((ShowPopupDetails)nextOp);
            }
        }
    };

    public static void requestShow(Popup popup) {
        assert popup != null;

        // If the popup is already showing or shown, then there's nothing to do.
        if (popup._isShowing() || popup._isShown()) return;

        final ShowPopupDetails details = new ShowPopupDetails(popup);
        if (opDetailsQueue.isEmpty()) {
            // Hide the popup that is on screen to show the new popup
            if (currentShownPopupDetails != null) {
                // It might be that firing the popupDismissed event causes the popup to be
                // destroyed. To make sure that destroy()'s call to requestHide() does not
                // *also* call the popup's hide() method, add hide popup details to the queue
                // before firing the popupDismissed event.
                opDetailsQueue.add(HidePopupDetails.INSTANCE);

                final Popup currentShownPopup = currentShownPopupDetails.popup;
                PopupDismissedEvent.fire(currentShownPopup);
                currentShownPopupDetails = null;

                opDetailsQueue.add(details);
                currentShownPopup.hide(HIDDEN_CALLBACK);

            } else {
                opDetailsQueue.add(details);
                callShow(details);
            }
        } else {
            assert currentShownPopupDetails == null;
            opDetailsQueue.add(details);
        }
    }

    private static void callShow(ShowPopupDetails details) {
        assert currentShownPopupDetails == null;
        assert details != null;
        assert !opDetailsQueue.isEmpty();
        assert opDetailsQueue.get(0) == details;
        details.popup.show(SHOWN_CALLBACK);
    }

    public static void requestHide(Popup popup) {
        assert popup != null;

        // If the popup is already hiding, then there's nothing to do.
        if (popup._isHiding()) return;

        // If the popup is hidden, go through the op details queue looking for show popup
        // details for the popup, and delete these from the queue. The idea behind this is that
        // if a popup queued-to-be-shown is requested to be hidden before the popup even started
        // showing itself, then we can skip doing anything with the popup.
        if (popup._isHidden()) {
            if (opDetailsQueue.isEmpty()) return;

            // If the first op details in the queue are show popup details, then the corresponding
            // popup should be in the showing state (and thus is not the popup passed to
            // requestHide()).
            assert !(opDetailsQueue.get(0) instanceof ShowPopupDetails) ||
                   ((ShowPopupDetails)opDetailsQueue.get(0)).popup._isShowing();
            final Iterator<OpDetails> it = opDetailsQueue.iterator();
            assert it.hasNext();
            it.next();

            while (it.hasNext()) {
                final OpDetails details = it.next();
                if (details instanceof ShowPopupDetails) {
                    if (((ShowPopupDetails)details).popup == popup) {
                        it.remove();
                    }
                }
            }

            return;
        }

        opDetailsQueue.add(HidePopupDetails.INSTANCE);
        if (opDetailsQueue.size() != 1) return;

        assert currentShownPopupDetails != null;
        assert currentShownPopupDetails.popup._isShown();
        final Popup currentShownPopup = currentShownPopupDetails.popup;
        assert currentShownPopup == popup;
        PopupDismissedEvent.fire(currentShownPopup);
        currentShownPopupDetails = null;
        currentShownPopup.hide(HIDDEN_CALLBACK);
    }

    private PopupManager() {
        /*empty*/
    }
}
