package com.smartgwt.mobile.client.widgets.form.fields;

import java.util.LinkedHashMap;

public class ColorPickerItem extends SelectItem {

    private static final LinkedHashMap<String, String> DEFAULT_VALUE_MAP = new LinkedHashMap<String, String>();
    static {
        DEFAULT_VALUE_MAP.put("black", "Black");
        DEFAULT_VALUE_MAP.put("silver", "Silver");
        DEFAULT_VALUE_MAP.put("gray", "Gray");
        DEFAULT_VALUE_MAP.put("white", "White");
        DEFAULT_VALUE_MAP.put("maroon", "Maroon");
        DEFAULT_VALUE_MAP.put("red", "Red");
        DEFAULT_VALUE_MAP.put("purple", "Purple");
        DEFAULT_VALUE_MAP.put("fuchsia", "Fuchsia");
        DEFAULT_VALUE_MAP.put("green", "Green");
        DEFAULT_VALUE_MAP.put("lime", "Lime");
        DEFAULT_VALUE_MAP.put("olive", "Olive");
        DEFAULT_VALUE_MAP.put("yellow", "Yellow");
        DEFAULT_VALUE_MAP.put("navy", "Navy");
        DEFAULT_VALUE_MAP.put("blue", "Blue");
        DEFAULT_VALUE_MAP.put("teal", "Teal");
        DEFAULT_VALUE_MAP.put("aqua", "Aqua");
    }

	public ColorPickerItem(String name) {
		super(name);
        super.setValueMap(DEFAULT_VALUE_MAP);
	}

	public ColorPickerItem(String name, String title) {
		this(name);
        super.setTitle(title);
	}

    public ColorPickerItem(String name, String title, String hint) {
        this(name, title);
        super.setHint(hint);
    }
}
