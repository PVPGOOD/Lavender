package io.justme.lavender.ui.screens.configscreen.frame.impl.button;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.configscreen.AbstractConfigFrame;
import io.justme.lavender.ui.screens.configscreen.frame.impl.AbstractComponents;
import io.justme.lavender.ui.screens.configscreen.frame.impl.button.components.impl.CheckBoxComponents;
import io.justme.lavender.ui.screens.notifacation.NotificationsEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author JustMe.
 * @since 2024/5/3
 **/

@Getter
@Setter
public class ConfigButtonFrame extends AbstractConfigFrame {

    private ArrayList<AbstractComponents> componentsArrayList = new ArrayList<>();

    public ConfigButtonFrame() {
        super("ButtonFrame");

        //必须的组件
        getComponentsArrayList().add(getReloadButton());
        getComponentsArrayList().add(getRefreshButton());
    }

    @Override
    public void initGui() {
        for (AbstractComponents components : getComponentsArrayList()) {
            components.initGui();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        float leftY = getHeight(), rightY = getHeight(), index = 0;
        for (AbstractComponents components : getComponentsArrayList()) {

            if (index % 2 == 0) {
                leftY -= 27;
            } else {
                rightY -= 27;
            }

            components.setX(getX() + (index % 2 == 0 ? 13 : 83));
            components.setY(getY() + (index % 2 == 0 ? leftY : rightY));
            components.drawScreen(mouseX, mouseY, partialTicks);
            index++;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractComponents components : getComponentsArrayList()) {
            if (components.mouseClicked(mouseX, mouseY, mouseButton)) {

                var configListFrame = La.getINSTANCE().getConfigListFrame();;

                switch (components.getName()) {

                    case "重载" -> {
                        La.getINSTANCE().getElementsManager().getGroupCircleArrayListManager().checkRebuild();
                        La.getINSTANCE().getConfigsManager().load();La.getINSTANCE().getNotificationsManager().push("成功",String.format("[%s] 已重新加载",La.getINSTANCE().getConfigsManager().getPageName()), NotificationsEnum.SUCCESS,5000);
                    }

                    case "刷新" -> {
                        La.getINSTANCE().getElementsManager().getGroupCircleArrayListManager().checkRebuild();
                        configListFrame.getComponentsArrayList().clear();
                        configListFrame.FileReader();
                        La.getINSTANCE().getNotificationsManager().push("成功","当前配置列表已刷新", NotificationsEnum.SUCCESS,5000);
                    }
                }

            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (AbstractComponents components : getComponentsArrayList()) {
            components.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        for (AbstractComponents components : getComponentsArrayList()) {
            components.keyTyped(typedChar, keyCode);
        }
    }

    public CheckBoxComponents getReloadButton() {
        return new CheckBoxComponents("重载");
    }

    public CheckBoxComponents getRefreshButton() {
        return new CheckBoxComponents("刷新");
    }
}
