package io.justme.lavender.ui.screens.configscreen.frame.impl.list;

import io.justme.lavender.La;
import io.justme.lavender.ui.screens.configscreen.AbstractConfigFrame;
import io.justme.lavender.ui.screens.configscreen.frame.impl.AbstractComponents;
import io.justme.lavender.ui.screens.configscreen.frame.impl.list.components.ListComponents;
import io.justme.lavender.ui.screens.notifacation.NotificationsEnum;
import io.justme.lavender.utility.gl.OGLUtility;
import io.justme.lavender.utility.math.TimerUtility;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Stream;

/**
 * @author JustMe.
 * @since 2024/5/3
 **/

@Getter
@Setter
public class ConfigListFrame extends AbstractConfigFrame {

    private ArrayList<AbstractComponents> componentsArrayList = new ArrayList<>();

    public ConfigListFrame() {
        super("ListFrame");
        FileReader();
    }

    @Override
    public void initGui() {
        for (AbstractComponents components : getComponentsArrayList()) {
            components.initGui();
        }

    }

    private float interval = 0;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        //配置列表
//        RenderUtility.drawRect(getX(),getY(),getWidth(),100,new Color(0,0,0,128));


        //绘制组件
        OGLUtility.scissor(getX(),getY(),getWidth(),La.getINSTANCE().getConfigScreen().getFinalHeight() + getHeight(),getHeight() + 10 , () -> {

            float interval = 10;
            for (AbstractComponents components : getComponentsArrayList()) {
                components.setX(getX() + 5);
                components.setY(getY() + interval);
                components.setWidth(getWidth() - 10);
                components.setHeight(30);
                interval += 40;

                setInterval(interval);
                components.drawScreen(mouseX, mouseY, partialTicks);
                setHeight(getInterval() + 60);
            }

            La.getINSTANCE().getConfigScreen().setFinalHeight((int) getHeight());


        });
    }

    private final TimerUtility timerUtility = new TimerUtility();
    private boolean ConfirmAction = false;
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (AbstractComponents components : getComponentsArrayList()) {
            if (components.mouseClicked(mouseX, mouseY, mouseButton)) {
                La.getINSTANCE().getElementsManager().getGroupCircleArrayListManager().checkRebuild();
                La.getINSTANCE().getConfigScreen().setSelectConfig(components.getName());
                La.getINSTANCE().getConfigsManager().save();
                La.getINSTANCE().getConfigsManager().loadAnotherConfig(components.getName());
                La.getINSTANCE().getNotificationsManager().push("Config Manager",String.format("[%s] 已加载 你的上一个配置已保存",components.getName()), NotificationsEnum.SUCCESS,5000);
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

    public void FileReader() {

        var directory = Path.of(Minecraft.getMinecraft().mcDataDir.getAbsolutePath(), La.getINSTANCE().getLa()).toAbsolutePath();

        var pathHashSet = new HashSet<>();
        try (Stream<Path> stream = Files.walk(directory, 1)) {
            stream
                    //遍历 获取 .minecraft/lavender/ 下的 文件夹 并 过滤掉无 包含.json 和 空文件
                    .filter(Files::isDirectory)
                    .filter(path -> path.getFileName().toString().equals(directory.relativize(path).toString()))
                    .forEach(path -> {
                        try (Stream<Path> fileStream = Files.list(path)) {
                            if (fileStream.anyMatch(filePath -> filePath.getFileName().toString().endsWith(".json"))) {
                                //去重 (在遍历目录下
                                if (!pathHashSet.contains(path)) {
                                    pathHashSet.add(path);
                                    getComponentsArrayList().add(new ListComponents(directory.relativize(path).toString()));
                                }
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
