package io.justme.lavender.ui.screens.clickgui.panel.settings;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import io.justme.lavender.ui.screens.clickgui.components.AbstractControlsComponent;
import io.justme.lavender.ui.screens.clickgui.panel.AbstractPanel;
import io.justme.lavender.ui.screens.clickgui.panel.settings.impl.SettingWindow;
import io.justme.lavender.utility.gl.RenderUtility;
import io.justme.lavender.value.impl.BoolValue;
import io.justme.lavender.value.impl.NumberValue;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author JustMe.
 * @since 2025/4/4
 **/
@Getter
@Setter
public class SettingPanel extends AbstractPanel {

    private final CopyOnWriteArrayList<AbstractSetting> abstractSettings = new CopyOnWriteArrayList<>();

    //设置
    private final BoolValue notificationValue = new BoolValue("启用 通知", true);
    private final NumberValue notificationAliveValue = new NumberValue("通知存活时间", 500, 10, 1000, 10);

    //渲染
    private final BoolValue blurValue = new BoolValue("启用 模糊渲染", false);
    private final BoolValue shadowValue = new BoolValue("启用 阴影渲染", false);

    public SettingPanel() {
        setName("SettingPanel");

        getAbstractSettings().add(new SettingWindow(SettingType.GLOBAL_SETTING));
        getAbstractSettings().add(new SettingWindow(SettingType.RENDERING));
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtility.drawRoundRectWithCustomRounded(getX(),getY(),getWidth(),55, new Color(255, 228, 238),0,0,15,15);

        FontDrawer fontDrawer = La.getINSTANCE().getFontManager().getPingFang_Bold22();
        fontDrawer.drawString("设置 ",getX() + 10,getY() + 10,new Color(129, 57, 80,128).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void handleMouseInput() throws IOException {

    }
}
