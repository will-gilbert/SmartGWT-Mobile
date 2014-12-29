package com.smartgwt.mobile.client.widgets.form;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.dom.client.UListElement;
import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.theme.FormCssResource;
import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.widgets.Canvas;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;
import com.smartgwt.mobile.client.widgets.form.fields.HasDataValue;
import com.smartgwt.mobile.client.widgets.form.fields.HiddenItem;
import com.smartgwt.mobile.client.widgets.form.fields.SearchItem;
import com.smartgwt.mobile.client.widgets.form.fields.SliderItem;
import com.smartgwt.mobile.client.widgets.form.fields.SpacerItem;
import com.smartgwt.mobile.client.widgets.form.fields.SwitchItem;
import com.smartgwt.mobile.client.widgets.form.fields.TextAreaItem;

@SGWTInternal
class DynamicFormImplIOS extends DynamicFormImpl {

    private static final FormCssResource CSS = DynamicForm._CSS;

    private final Map<String, Element> errors = new HashMap<String, Element>();
    private TableElement table = null;

    @Override
    void destroyImpl(DynamicForm self) {
        /*empty*/
    }

    @Override
    void addFields(DynamicForm self, Collection<Canvas> fields) {
        if (table != null && table.getParentNode() != null) table.removeFromParent();
        if (fields != null) {
            Document document = Document.get();
            table = document.createTableElement();
            table.setCellPadding(0);
            table.setCellSpacing(0);
            table.setClassName(CSS.formClass());
            TableSectionElement tbody = document.createTBodyElement();
            table.appendChild(tbody);

            final TableRowElement sizingTR = document.createTRElement();
            sizingTR.getStyle().setHeight(0, Style.Unit.PX);
            sizingTR.getStyle().setOverflow(Style.Overflow.HIDDEN);
            TableCellElement th = document.createTHElement();
            th.setClassName(CSS.labelCellClass());
            th.addClassName(CSS.firstFormRowOrCellClass());
            th.getStyle().setHeight(0, Style.Unit.PX);
            th.getStyle().setPadding(0, Style.Unit.PX);
            th.getStyle().setBorderWidth(0, Style.Unit.PX);
            sizingTR.appendChild(th);
            TableCellElement td = document.createTDElement();
            td.setClassName(CSS.formItemCellClass());
            td.getStyle().setHeight(0, Style.Unit.PX);
            td.getStyle().setPadding(0, Style.Unit.PX);
            td.getStyle().setBorderWidth(0, Style.Unit.PX);
            sizingTR.appendChild(td);
            tbody.appendChild(sizingTR);

            TableRowElement firstTR = null, lastTR = null;
            for (final Canvas field : fields) {
                if (field instanceof HiddenItem) {
                    self._add(field, self._getForm().<com.google.gwt.user.client.Element>cast());
                    continue;
                }

                TableRowElement tr = document.createTRElement();
                if (firstTR == null) {
                    tr.addClassName(CSS.firstFormRowOrCellClass());
                    firstTR = tr;
                }
                tbody.appendChild(tr);

                th = null;
                td = document.createTDElement();
                td.setClassName(CSS.formItemCellClass());
                final Boolean showTitle = ((HasDataValue)field).getShowTitle();
                if (!(field instanceof SpacerItem) && (showTitle == null || showTitle.booleanValue())) {
                    th = document.createTHElement();
                    th.setClassName(CSS.labelCellClass());
                    th.addClassName(CSS.firstFormRowOrCellClass());
                    tr.appendChild(th);
                } else {
                    th = null;
                    td.addClassName(CSS.firstFormRowOrCellClass());
                }
                td.addClassName(CSS.lastFormRowOrCellClass());
                if (field instanceof SpacerItem) {
                    if (lastTR != null) lastTR.addClassName(CSS.followedBySpacerItemFormRowClass());
                    tr.addClassName(CSS.spacerItemFormRowClass());
                } else if (field instanceof SearchItem) {
                    if (th != null) th.addClassName(CSS.searchItemLabelCellClass());
                    td.addClassName(CSS.searchItemCellClass());
                } else if (field instanceof SliderItem) {
                    if (th != null) th.addClassName(CSS.sliderItemLabelCellClass());
                } else if (field instanceof SwitchItem) {
                    if (th != null) th.addClassName(CSS.switchItemLabelCellClass());
                } else if (field instanceof TextAreaItem) {
                    if ((showTitle == null || showTitle.booleanValue())) {
                        if (th != null) {
                            th.addClassName(CSS.textAreaItemLabelCellClass());
                            th.setColSpan(2);
                            th.addClassName(CSS.lastFormRowOrCellClass());
                        }
                        tr = document.createTRElement();
                        tbody.appendChild(tr);
                    }
                    td.addClassName(CSS.textAreaItemCellClass());
                    td.setColSpan(2);
                    td.addClassName(CSS.firstFormRowOrCellClass());
                }
                tr.appendChild(td);
                final boolean isPlaceholder = FormItem.class.equals(field.getClass());
                if (!isPlaceholder) self._add(field, td.<com.google.gwt.user.client.Element>cast());
                if (!(field instanceof SpacerItem) && (showTitle == null || showTitle.booleanValue())) {
                    assert th != null;
                    LabelElement label = document.createLabelElement();
                    label.setHtmlFor(field.getElement().getId());
                    label.setInnerHTML(field.getTitle());
                    th.appendChild(label);
                } else td.setColSpan(2);

                lastTR = tr;
            }
            if (lastTR != null) lastTR.addClassName(CSS.lastFormRowOrCellClass());
            self._getForm().appendChild(table);
        }
    }

    @Override
    void removeField(DynamicForm self, Canvas field) {
        final boolean isPlaceholder = FormItem.class.equals(field.getClass());
        if (!isPlaceholder) self.remove(field);
        final String fieldName = ((HasDataValue)field).getFieldName();
        final Element errorElem = errors.remove(fieldName);
        if (errorElem != null) errorElem.removeFromParent();
        if (!isPlaceholder) field.getElement().removeClassName(CSS.formItemHasInvalidValueClass());
    }

    @Override
    void clearErrors(DynamicForm self) {
        final Map<String, Canvas> fields = self._getAllFieldsMap();
        for (final Map.Entry<String, Element> e : errors.entrySet()) {
            final Element errorElem = e.getValue();
            errorElem.removeFromParent();
            fields.get(e.getKey()).getElement().removeClassName(CSS.formItemHasInvalidValueClass());
        }
        errors.clear();
    }

    @Override
    void showError(DynamicForm self, boolean showError, Canvas field, Object errorObj) {
        Element error = null;
        if (errorObj instanceof String) {
            final String errorMessage = (String)errorObj;
            error = Document.get().createDivElement();
            error.setInnerHTML(errorMessage);
        } else if (errorObj instanceof List || errorObj instanceof String[]) {
            final int numMessages = errorObj instanceof List ? ((List<?>)errorObj).size() : ((String[])errorObj).length;
            if (numMessages <= 1) {
                if (numMessages == 1) {
                    final String errorMessage;
                    if (errorObj instanceof List) {
                        final Object obj = ((List<?>)errorObj).get(0);
                        if (obj == null) errorMessage = "&nbsp;";
                        else errorMessage = obj.toString();
                    } else errorMessage = ((String[])errorObj)[0];
                    error = Document.get().createDivElement();
                    error.setInnerHTML(errorMessage);
                }
            } else {
                final UListElement ul = Document.get().createULElement();
                error = ul;
                for (int i = 0; i < numMessages; ++i) {
                    final LIElement li = Document.get().createLIElement();
                    final String errorMessage;
                    if (errorObj instanceof List) {
                        final Object obj = ((List<?>)errorObj).get(i);
                        if (obj == null) errorMessage = "&nbsp;";
                        else errorMessage = obj.toString();
                    } else errorMessage = ((String[])errorObj)[i];
                    li.setInnerHTML(errorMessage);
                    ul.appendChild(li);
                }
            }
        } else {
            SC.logWarn("Unhandled error object type " + errorObj.getClass().getName() + " for field '" + ((HasDataValue)field).getFieldName() + "'");
        }

        if (error != null && showError) {
            error.setClassName(CSS.errorLabelClass());
            errors.put(((HasDataValue)field).getFieldName(), error);
            if (showError) {
                final Element itemElement = field.getElement();
                itemElement.getParentElement().insertAfter(error, itemElement);
            }
        }
    }
}
