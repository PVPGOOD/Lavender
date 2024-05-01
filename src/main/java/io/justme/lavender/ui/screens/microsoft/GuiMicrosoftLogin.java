package io.justme.lavender.ui.screens.microsoft;

import io.justme.lavender.La;
import io.justme.lavender.fonts.FontDrawer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Session;

import java.awt.*;
import java.io.IOException;

public final class GuiMicrosoftLogin extends GuiScreen {
    private volatile MicrosoftLogin microsoftLogin;
    private volatile boolean closed = false;
    public static int much;
    private final GuiScreen parentScreen;

    public GuiMicrosoftLogin(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;

        new Thread("MicrosoftLogin Thread") {
            @Override
            public void run() {
                try {

                    microsoftLogin = new MicrosoftLogin();
                    microsoftLogin.show();

                    while (true) {
                        if (microsoftLogin.logged) {
                            microsoftLogin.close();
                            microsoftLogin.status = EnumChatFormatting.GREEN + "Login succfull! " + microsoftLogin.userName;
                            much = 100;
                            mc.session = new Session(microsoftLogin.userName,microsoftLogin.uuid,microsoftLogin.accessToken,"mojang");
                            closed = true;
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    microsoftLogin.status = EnumChatFormatting.RED + "Login Faild! " + e.getClass().getName() + ":" + e.getMessage();
                    microsoftLogin.close();
                    closed = true;
                }

                interrupt();
            }
        }.start();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.id == 0) {
            if (microsoftLogin != null && !closed) {
                microsoftLogin.close();
                closed = true;
            }

            mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        much = 0;
        buttonList.add(new GuiButton(0,width / 2 - 100,height / 2 + 50,"Back"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);

        drawDefaultBackground();
        FontDrawer biggest = La.getINSTANCE().getFontManager().getSFBold22();
        FontDrawer normal = La.getINSTANCE().getFontManager().getSFBold18();

        normal.drawCenteredString(much + ".0%", sr.getScaledWidth() / 2F, sr.getScaledHeight()  / 2 - 20, -1);

        normal.drawStringWithShadow("Microsoft Login code by yalan !", 4, height - 12, new Color(255, 255, 255, 200).getRGB());

        if (microsoftLogin == null) {
            normal.drawStringWithShadow(EnumChatFormatting.YELLOW + "Login...",width / 2.0f - 120,height / 2.0f - 80,-1);
        } else {
            biggest.drawCenteredString(microsoftLogin.status,width / 2.0f,height / 2.0f- 80,-1);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
 }
