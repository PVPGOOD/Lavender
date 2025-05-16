package io.justme.lavender.handler.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.justme.lavender.events.game.EventTick;
import io.justme.lavender.events.game.EventWorldReload;
import io.justme.lavender.events.network.EventPacket;
import io.justme.lavender.handler.AbstractHandler;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;

/**
 * @author JustMe.
 * @since 2025/5/16
 **/
@Getter
@Setter
public class ServerHandler extends AbstractHandler {

    private StatusEnum statusEnum = StatusEnum.NULL;
    private static final String[] PLAYING_KEYWORDS = {
            "Players left", "Next Event",
            "Gray", "Pink", "Red", "Blue", "Green", "White", "Yellow", "Cyan", "Aqua"
    };
    private static final String[] WAITING_KEYWORDS = {
            "即将开始", "等待中", "Starting in", "Waiting"
    };

    @EventTarget
    public void onTick(EventTick event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) {
            return;
        }

        updateStatusFromScoreboard(mc);
        updateStatusFromInventory(mc);
    }

    private void updateStatusFromScoreboard(Minecraft mc) {
        ScoreObjective objective = getScoreObjective(mc);
        if (objective == null) return;

        var scoreboard = objective.getScoreboard();

        var filtered = Lists.newArrayList(
                Iterables.filter(
                        scoreboard.getSortedScores(objective),
                        this::isValidScoreEntry
                )
        );
        int start = Math.max(0, filtered.size() - 15);
        var recentScores = filtered.subList(start, filtered.size());

        // Analyze
        for (Score score : recentScores) {
            String playerName = score.getPlayerName();
            ScorePlayerTeam team = scoreboard.getPlayersTeam(playerName);
            String formatted = ScorePlayerTeam.formatPlayerName(team, playerName);

            if (containsKeyword(formatted, WAITING_KEYWORDS)) {
                statusEnum = StatusEnum.WAITING_LOBBY;
                return;
            }
            if (containsKeyword(formatted, PLAYING_KEYWORDS)) {
                statusEnum = StatusEnum.PLAYING;
                return;
            }
        }
    }

    private boolean isValidScoreEntry(Score score) {
        String name = score.getPlayerName();
        return name != null && !name.startsWith("#");
    }

    private boolean containsKeyword(String text, String[] keywords) {
        for (String kw : keywords) {
            if (text.contains(kw)) return true;
        }
        return false;
    }

    private void updateStatusFromInventory(Minecraft mc) {
        for (var itemStack : mc.thePlayer.inventory.mainInventory) {
            if (itemStack == null) continue;
            if (!(itemStack.getItem() instanceof net.minecraft.item.ItemSkull)) continue;

            String name = itemStack.getDisplayName();
            if ("§aMy Profile §7(Right Click)".equals(name)
                    || "§a个人档案§7（右键点击）".equals(name)) {
                statusEnum = StatusEnum.MAIN_LOBBY;
                return;
            }
        }
    }

    private ScoreObjective getScoreObjective(Minecraft mc) {
        var scoreboard = mc.theWorld.getScoreboard();
        ScoreObjective scoreobjective = null;
        var scoreplayerteam = scoreboard.getPlayersTeam(mc.thePlayer.getName());

        if (scoreplayerteam != null)
        {
            int i1 = scoreplayerteam.getChatFormat().getColorIndex();

            if (i1 >= 0)
            {
                scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
            }
        }

        return scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket) {

    }


    @EventTarget
    public void onWorldReload(EventWorldReload eventWorldReload) {
        setStatusEnum(StatusEnum.NULL);
    }

    public enum StatusEnum {
        NULL,
        PLAYING,
        MAIN_LOBBY,
        WAITING_LOBBY
    }
}
