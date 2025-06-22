package io.justme.lavender.ui.screens.clickgui.imgui.theme;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public class ThemeColor {
    private final Map<ThemeColorEnum, Color> DARK_COLORS = new EnumMap<>(ThemeColorEnum.class);

    public void initDarkColors() {
        DARK_COLORS.put(ThemeColorEnum.CLICKSCREEN_BACKGROUND, new Color(24, 24, 31, 255));

        DARK_COLORS.put(ThemeColorEnum.PANEL_SETTING_TITLE, new Color(255, 255, 255, 255));
        DARK_COLORS.put(ThemeColorEnum.PANEL_SETTING_TITLE_FONT, new Color(129, 57, 80,128));
        DARK_COLORS.put(ThemeColorEnum.PANEL_POPUP_TEXT, new Color(255,255,255));
        DARK_COLORS.put(ThemeColorEnum.PANEL_POPUP_TOGGLE_ON, new Color(30, 34, 44, 255));
        DARK_COLORS.put(ThemeColorEnum.PANEL_POPUP_TOGGLE_OFF, new Color(17, 19, 26, 255));
        DARK_COLORS.put(ThemeColorEnum.PANEL_POPUP_HIGHLIGHT, new Color(129, 57, 80,255));
        DARK_COLORS.put(ThemeColorEnum.PANEL_POPUPCOMBOBOX_MASK, new Color(0,0,0,64));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_CHECKBOX_FONT, new Color(0x899AC0));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_CHECKBOX_CHECKED, new Color(103, 84, 150, 255));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_CHECKBOX_UNCHECKED, new Color(255, 187, 213, 0));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_CHECKBOX_OUTLINE, new Color(0, 0,0,150));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_CHECKBOX_MARK, new Color(255, 255, 255));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_CHECKBOX_SHADOW, new Color(0,0,0,28));

        DARK_COLORS.put(ThemeColorEnum.PANEL_MODULEPANEL_BACKGROUND, new Color(0x0B0E15));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_COMBOBOX_BACKGROUND, new Color(11, 14, 21, 255));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_COMBOBOX_OUTLINE, new Color(30, 34, 44, 255));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_COMBOBOX_FONT, new Color(137, 154, 192, 255));

        DARK_COLORS.put(ThemeColorEnum.COMPONENT_MODE_BACKGROUND, new Color(11, 14, 21, 255));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_MODE_OUTLINE, new Color(30, 34, 44, 255));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_MODE_FONT, new Color(137, 154, 192, 255));

        DARK_COLORS.put(ThemeColorEnum.COMPONENT_MODE_DROPDOWN_BACKGROUND, new Color(11, 14, 21, 255));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_MODE_DROPDOWN_OUTLINE, new Color(30, 34, 44, 255));

        DARK_COLORS.put(ThemeColorEnum.COMPONENT_SEGMENTEDBUTTON_BACKGROUND, new Color(40, 40, 50));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_SEGMENTEDBUTTON_SELECTED, new Color(103, 84, 150));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_SEGMENTEDBUTTON_OUTLINE, new Color(60, 60, 80));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_SEGMENTEDBUTTON_FONT, new Color(255, 255, 255));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_SLIDER_BACKGROUND, new Color(40, 40, 50));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_SLIDER_FILLED, new Color(103, 84, 150));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_SLIDER_OUTLINE, new Color(60, 60, 80));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_SLIDER_KNOB, new Color(255, 255, 255));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_SLIDER_FONT, new Color(137, 154, 192));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_SWITCH_BACKGROUND, new Color(40, 40, 50));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_SWITCH_ON, new Color(103, 84, 150));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_SWITCH_OFF, new Color(120, 120, 120));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_SWITCH_OUTLINE, new Color(60, 60, 80));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_SWITCH_KNOB, new Color(255, 255, 255));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_SWITCH_FONT, new Color(137, 154, 192));

        DARK_COLORS.put(ThemeColorEnum.PANEL_MODULE_BACKGROUND, new Color(17, 19, 26));
        DARK_COLORS.put(ThemeColorEnum.PANEL_MODULE_FONT, new Color(137, 154, 192, 255));
        DARK_COLORS.put(ThemeColorEnum.PANEL_MODULE_BUTTON_TOGGLE_ON, new Color(30, 34, 44, 255));
        DARK_COLORS.put(ThemeColorEnum.PANEL_MODULE_BUTTON_TOGGLE_OFF, new Color(17, 19, 26, 255));
        DARK_COLORS.put(ThemeColorEnum.PANEL_MODULE_BUTTON_FONT, new Color(137, 154, 192, 255));

        DARK_COLORS.put(ThemeColorEnum.COMPONENT_SCROLLBAR_BACKGROUND, new Color(40, 40, 50));
        DARK_COLORS.put(ThemeColorEnum.COMPONENT_SCROLLBAR_THUMB, new Color(103, 84, 150));
    }


    public Color getColor(ThemeType type, ThemeColorEnum colorEnum) {
        switch (type) {
            case DARK:
            default:
                return DARK_COLORS.get(colorEnum);
        }
    }
}
