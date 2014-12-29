package com.smartgwt.mobile.client.internal.widgets;

import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;
import com.smartgwt.mobile.client.internal.EventUtil;
import com.smartgwt.mobile.client.internal.test.AutoTest;
import com.smartgwt.mobile.client.internal.test.GetAttributeConfiguration;
import com.smartgwt.mobile.client.internal.theme.ios.PickerCssResourceIOS;
import com.smartgwt.mobile.client.internal.types.AttributeType;
import com.smartgwt.mobile.client.internal.util.Pair;
import com.smartgwt.mobile.client.widgets.grid.ListGridField;
import com.smartgwt.mobile.internal.gwt.dom.client.DOMConstants;
import com.smartgwt.mobile.internal.gwt.dom.client.InsertAdjacentHTMLPosition;
import com.smartgwt.mobile.internal.gwt.dom.client.SuperElement;

class PickerDialImplIOS extends PickerDialImpl {

    private static final PickerCssResourceIOS CSS = (PickerCssResourceIOS)Picker2.CSS;

    // Android 2.x does not support 3D transforms.
    // http://caniuse.com/transforms3d
    //private static final String translateYStart = Canvas.isAndroid() ? "translateY(" : "translate3d(0,",
    //        translateYEnd = Canvas.isAndroid() ? "px)" : "px,0)";
    private static final String translateYStart = "translateY(",
            translateYEnd = "px)";

    private Element pickerDialElem;
    private TableElement tableElem;

    private boolean active = false;
    private int selectedRowNum = 0;
    private int startClientY, lastY = 0;

    @Override
    void init(PickerDial self) {
        final Element elem = self.getElement();
        elem.addClassName(CSS.pickerDialWrapperClass());
        pickerDialElem = Document.get().createDivElement();
        pickerDialElem.setClassName(CSS.pickerDialClass());
        elem.appendChild(pickerDialElem);
        self.sinkEvents((TouchEvent.isSupported() ? Event.TOUCHEVENTS : Event.MOUSEEVENTS) | Event.ONLOSECAPTURE);
    }

    @Override
    Object _getAttributeFromSplitLocator(PickerDial self, List<String> locatorArray, GetAttributeConfiguration configuration) {
        if (configuration.getAttribute() == AttributeType.ELEMENT) {
            if (locatorArray.size() == 1) {
                String substring = locatorArray.get(0);
                if ("table".equals(substring)) return tableElem;
            } else if (locatorArray.size() == 2) {
                String substring = locatorArray.get(0);
                final RecordList data = self._getData();
                int r = -1;
                if (substring.startsWith("row[") && data != null && !data.isEmpty()) {
                    final Pair<String, Map<String, String>> p = AutoTest.parseLocatorFallbackPath(substring);
                    if (p != null) {
                        assert "row".equals(p.getFirst());
                        final Map<String, String> configObj = p.getSecond();
                        final String valueOnly = configObj.get(AutoTest.FALLBACK_VALUE_ONLY_FIELD);
                        if (valueOnly != null) {
                            try {
                                r = Integer.parseInt(valueOnly, 10);
                            } catch (NumberFormatException ex) {}
                        }
                    }
                } else if (substring.equals("selectedRow")) {
                    r = selectedRowNum;
                }

                substring = locatorArray.get(1);
                final ListGridField[] fields = self.getFields();
                int c = -1;
                if (substring.startsWith("col[") && fields != null && fields.length != 0) {
                    final Pair<String, Map<String, String>> p = AutoTest.parseLocatorFallbackPath(substring);
                    if (p != null) {
                        assert "col".equals(p.getFirst());
                        final Map<String, String> configObj = p.getSecond();
                        final String fieldName = configObj.get("fieldName");
                        if (fieldName != null) {
                            c = self.getFieldNum(fieldName);
                        }
                        final String valueOnly = configObj.get(AutoTest.FALLBACK_VALUE_ONLY_FIELD);
                        if (valueOnly != null) {
                            try {
                                c = Integer.parseInt(valueOnly, 10);
                            } catch (NumberFormatException ex) {}
                        }
                    }
                }

                if (tableElem != null && r >= 0 && c >= 0) {
                    final Element tbodyElem = tableElem.getChildNodes().getItem(1).cast();
                    assert tbodyElem != null && "TBODY".equals(tbodyElem.getTagName());
                    if (r < tbodyElem.getChildCount()) {
                        final Element trElem = (Element)tbodyElem.getChild(r);
                        if (trElem != null && c < trElem.getChildCount()) return trElem.getChild(c);
                    }
                }
            }
        } else if (configuration.getAttribute() == AttributeType.VALUE) {
            if (locatorArray.size() == 2) {
                String substring = locatorArray.get(0);
                final RecordList data = self._getData();
                int recordIndex = -1;
                if (substring.startsWith("row[") && data != null && !data.isEmpty()) {
                    final Pair<String, Map<String, String>> p = AutoTest.parseLocatorFallbackPath(substring);
                    if (p != null) {
                        assert "row".equals(p.getFirst());
                        final Map<String, String> configObj = p.getSecond();
                        final String valueOnly = configObj.get(AutoTest.FALLBACK_VALUE_ONLY_FIELD);
                        if (valueOnly != null) {
                            try {
                                recordIndex = Integer.parseInt(valueOnly, 10);
                            } catch (NumberFormatException ex) {}
                        }
                    }
                } else if (substring.equals("selectedRow")) {
                    recordIndex = selectedRowNum;
                }

                if (recordIndex >= 0) {
                    substring = locatorArray.get(1);
                    if ("value".equals(substring)) {
                        final boolean allowEmptyValue = self.getAllowEmptyValue();
                        final int data_size = (data == null ? 0 : data.size());
                        final int lastPos = (allowEmptyValue ? data_size : data_size - 1);
                        if (recordIndex >= 0 && recordIndex <= lastPos) {
                            if (allowEmptyValue && recordIndex == 0) return null;
                            final Record record = data.get(allowEmptyValue ? recordIndex - 1 : recordIndex);
                            return record.getAttributeAsObject(self.getValueField());
                        }
                    }
                }
            }
        }
        return super._getAttributeFromSplitLocator(self, locatorArray, configuration);
    }

    private void setPosition(int rowNum) {
        lastY = -rowNum * CSS.pickerSlotHeightPx();
        selectedRowNum = rowNum;

        if (tableElem != null) tableElem.getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), translateYStart + (lastY + CSS.topPickerCoverHeightPx()) + translateYEnd);
    }

    @Override
    void onBrowserEvent(PickerDial self, Event event) {
        final Element targetElem = EventUtil.getTargetElem(event);
        if (targetElem != null) {
            switch (event.getTypeInt()) {
                case Event.ONMOUSEDOWN:
                case Event.ONTOUCHSTART:
                    if (!active) {
                        active = true;
                        onStart(self, event);
                    }
                    return;
                case Event.ONMOUSEMOVE:
                case Event.ONTOUCHMOVE:
                    if (active) {
                        onMove(self, event);
                    }
                    return;
                case Event.ONMOUSEUP:
                case Event.ONTOUCHEND:
                case Event.ONTOUCHCANCEL:
                    if (active) {
                        active = false;
                        DOM.releaseCapture(self.getElement());
                        onEnd(self, event);
                    }
                    return;
                case Event.ONLOSECAPTURE:
                    if (active) {
                        active = false;
                        onEnd(self, event);
                    }
                    return;
            }
        }
    }

    @Override
    void onRecordSelected(PickerDial self, Record selectedRecord) {
        int selectedRowNum = 0;
        if (selectedRecord != null) {
            final RecordList data = self._getData();
            if (data != null && !data.isEmpty()) {
                for (int rowNum = 0; rowNum < data.size(); ++rowNum) {
                    final Record record = data.get(rowNum);
                    if (record == selectedRecord) {
                        selectedRowNum = (self.getAllowEmptyValue() ? rowNum + 1 : rowNum);
                        break;
                    }
                }
            }
        }
        setPosition(selectedRowNum);
    }

    private void onStart(PickerDial self, Event event) {
        startClientY = (event.getTypeInt() == Event.ONTOUCHSTART ? event.getTouches().get(0).getClientY() : event.getClientY());
    }

    private void onMove(PickerDial self, Event event) {
        // Stop the screen from shifting.
        event.preventDefault();

        final int clientY = (event.getTypeInt() == Event.ONTOUCHMOVE ? event.getTouches().get(0).getClientY() : event.getClientY());
        final int delY = clientY - startClientY;
        if (tableElem != null) tableElem.getStyle().setProperty(DOMConstants.INSTANCE.getTransformPropertyName(), translateYStart + (lastY + delY + CSS.topPickerCoverHeightPx()) + translateYEnd);
    }

    private void onEnd(PickerDial self, Event event) {
        final int clientY = (event.getTypeInt() == Event.ONTOUCHEND ? event.getChangedTouches().get(0).getClientY() : event.getClientY());
        final int delY = clientY - startClientY;
        final double position = -(lastY + delY) / ((double)CSS.pickerSlotHeightPx());
        final RecordList data = self._getData();
        final int data_size = (data == null ? 0 : data.size());
        final boolean allowEmptyValue = self.getAllowEmptyValue();
        final int lastPos = (allowEmptyValue ? data_size : data_size - 1);
        final int pos = Math.max(0, Math.min(Math.round((float)position), lastPos));

        if (allowEmptyValue && pos == 0) {
            self.deselectAllRecords();
            setPosition(0);
        } else if (data_size != 0) {
            assert data != null;
            self.selectSingleRecord(data.get(allowEmptyValue ? pos - 1 : pos));
        }
        startClientY = 0; 
    }

    @Override
    void refreshRows(PickerDial self) {
        if (tableElem != null) tableElem.removeFromParent();

        final StringBuilder tableHTML = new StringBuilder();
        tableHTML.append("<table>");
        final ListGridField[] fields = self.getFields();
        if (fields != null && fields.length != 0) {
            tableHTML.append("<colgroup span=\"").append(Integer.toString(fields.length)).append("\">");
            for (int colNum = 0; colNum < fields.length; ++colNum) {
                final ListGridField field = fields[colNum];
                tableHTML.append("<col");
                final Object width = field._getWidth();
                if (width != null && !"*".equals(width)) tableHTML.append(" width=\"").append(width).append('"');
                tableHTML.append(">");
            }
            tableHTML.append("</colgroup><tbody>");
            if (self.getAllowEmptyValue()) {
                String emptyDisplayValue = self._getEmptyDisplayValue();
                if (emptyDisplayValue == null || emptyDisplayValue.isEmpty()) emptyDisplayValue = "&mdash;";
                tableHTML.append("<tr><td class=\"").append(CSS.firstPickerCellClass()).append(' ').append(CSS.emptyDisplayValueCellClass());
                final String baseStyle = self.getBaseStyle();
                if (baseStyle != null && !baseStyle.isEmpty()) tableHTML.append(' ').append(SafeHtmlUtils.htmlEscape(baseStyle));
                tableHTML.append("\" colspan=\"").append(fields.length).append("\">")
                        .append(emptyDisplayValue).append("</td></tr>");
            }
            final RecordList data = self._getData();
            Record selectedRecord = self.getSelectedRecord();
            selectedRowNum = 0;
            if (data != null && !data.isEmpty()) {
                for (int rowNum = 0; rowNum < data.size(); ++rowNum) {
                    final Record record = data.get(rowNum);

                    if (record == selectedRecord) {
                        selectedRowNum = (self.getAllowEmptyValue() ? rowNum + 1 : rowNum);
                    }

                    tableHTML.append("<tr>");
                    for (int colNum = 0; colNum < fields.length; ++colNum) {
                        final ListGridField field = fields[colNum];
                        tableHTML.append("<td class=\"").append(colNum == 0 ? CSS.firstPickerCellClass() : CSS.pickerCellClass());
                        final String baseStyle = self.getBaseStyle(record, rowNum, colNum);
                        if (baseStyle != null && !baseStyle.isEmpty()) tableHTML.append(' ').append(SafeHtmlUtils.htmlEscape(baseStyle));
                        tableHTML.append('"');
                        final Object width = field._getWidth();
                        if (width != null && !"*".equals(width)) tableHTML.append(" width=\"").append(width).append('"');
                        tableHTML.append(">")
                                .append(self._formatCellValue(record, rowNum, field.getName()))
                                .append("</td>");
                    }
                    tableHTML.append("</tr>");
                }
            }
        } else {
            tableHTML.append("<tbody>");
        }
        tableHTML.append("</tbody></table>");
        pickerDialElem.<SuperElement>cast().insertAdjacentHTML(InsertAdjacentHTMLPosition.AFTER_BEGIN, tableHTML.toString());
        tableElem = pickerDialElem.getFirstChildElement().cast();

        setPosition(selectedRowNum);

        self._fireContentChangedEvent();
    }
}
