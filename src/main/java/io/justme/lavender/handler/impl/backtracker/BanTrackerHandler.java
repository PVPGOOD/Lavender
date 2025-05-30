package io.justme.lavender.handler.impl.backtracker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.justme.lavender.La;
import io.justme.lavender.events.game.EventTick;
import io.justme.lavender.handler.AbstractHandler;
import io.justme.lavender.ui.screens.notifacation.NotificationsEnum;
import io.justme.lavender.utility.system.HttpUtility;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.EventTarget;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

@Setter
@Getter
public class BanTrackerHandler extends AbstractHandler {
    private final String API_URL = "https://api.plancke.io/hypixel/v1/punishmentStats";
    private final int HISTORY_LIMIT = 10;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final ExecutorService networkExecutor = Executors.newFixedThreadPool(2);

    private final Gson gson = new Gson();
    private final ReentrantLock lock = new ReentrantLock();

    private BanData banData = new BanData();
    private final CopyOnWriteArrayList<Map<String, Object>> banHistory = new CopyOnWriteArrayList<>();

    private final Deque<BanEvent> staffHalfHourEvents = new ArrayDeque<>();
    private final Deque<BanEvent> staffLastMinuteEvents = new ArrayDeque<>();
    private final Deque<BanEvent> watchdogHalfHourEvents = new ArrayDeque<>();


    public BanTrackerHandler() {
        scheduler.scheduleAtFixedRate(this::asyncFetchBanData, 0, 6, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::updateWindowStats, 0, 3, TimeUnit.SECONDS);
    }

    private void asyncFetchBanData() {
        CompletableFuture.supplyAsync(() -> {
            try {
                return HttpUtility.get(API_URL, 5000,3000,null);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, networkExecutor).thenAccept(response -> {
            if (response == null) return;
            processResponse(response);
        });
    }

    private void processResponse(String response) {
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> resp = gson.fromJson(response, mapType);
        if (!resp.containsKey("success") || !(Boolean) resp.get("success")) {
            System.out.println("BanTrackerHandler: API request failed or returned no data.");
            return;
        }

        Map<String, Object> record = (Map<String, Object>) resp.get("record");
        if (record == null) return;

        try {
            int staffDay = ((Number) record.get("staff_rollingDaily")).intValue();
            int dogDay = ((Number) record.get("watchdog_rollingDaily")).intValue();
            int dogMinute = ((Number) record.get("watchdog_lastMinute")).intValue();
            int staffTotal = ((Number) record.get("staff_total")).intValue();
            int dogTotal = ((Number) record.get("watchdog_total")).intValue();

            int wdiff = dogTotal - banData.watchdogLastDay;
            int sdiff = staffTotal - banData.staffLastDay;

            long now = Instant.now().getEpochSecond();

            if (wdiff > 0) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("time", now);
                entry.put("formated", String.format("%tT", now * 1000));
                entry.put("watchdog", true);
                entry.put("number", wdiff);
                entry.put("pushed", false);
                banHistory.addFirst(entry);
                addBanEvent(watchdogHalfHourEvents, wdiff, true);
            }

            if (sdiff > 0) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("time", now);
                entry.put("formated", String.format("%tT", now * 1000));
                entry.put("watchdog", false);
                entry.put("number", sdiff);
                entry.put("pushed", false);
                banHistory.addFirst(entry);
                addBanEvent(staffHalfHourEvents, sdiff, false);
                addBanEvent(staffLastMinuteEvents, sdiff, false);
            }


            while (banHistory.size() > HISTORY_LIMIT) {
                banHistory.removeLast();
            }

            banData.staffLastDay = staffTotal;
            banData.watchdogLastDay = dogTotal;
            banData.watchdogLastMinute = dogMinute;
            banData.lastUpdated = System.currentTimeMillis();

        } finally {
            lock.unlock();
        }
    }

    private void addBanEvent(Deque<BanEvent> queue, int number, boolean watchdog) {
        long now = Instant.now().getEpochSecond();
        queue.addLast(new BanEvent(now, number, watchdog));
    }

    private void removeOldEvents(Deque<BanEvent> queue, int windowSeconds) {
        long now = Instant.now().getEpochSecond();
        while (!queue.isEmpty() && now - queue.peekFirst().timestamp > windowSeconds) {
            queue.pollFirst();
        }
    }

    private int getBanCount(Deque<BanEvent> queue, int windowSeconds) {
        removeOldEvents(queue, windowSeconds);
        int sum = 0;
        for (BanEvent event : queue) sum += event.number;
        return sum;
    }

    private void updateWindowStats() {
        banData.staffLastHalfHour = getBanCount(staffHalfHourEvents, 1800);
        banData.staffLastMinute = getBanCount(staffLastMinuteEvents, 60);
        banData.watchdogLastHalfHour = getBanCount(watchdogHalfHourEvents, 1800);
    }


    private long lastTickTime = System.currentTimeMillis();

    @EventTarget
    public void onTicks(EventTick eventTick) {
        long now = System.currentTimeMillis();
        if (now - lastTickTime >= 1000) {
            lastTickTime = now;

            boolean hasNewNotification = false;

            var format = "";
            for (Map<String, Object> ban : banHistory) {
                var pushed = (Boolean) ban.get("pushed");
                if (pushed != null && pushed) continue;  // 已推送过跳过

                boolean isWatchdog = Boolean.TRUE.equals(ban.get("watchdog"));
                String emoji = isWatchdog ? EnumChatFormatting.GREEN + "狗" + EnumChatFormatting.RESET :  EnumChatFormatting.RED + "客服" + EnumChatFormatting.RESET ;
                format = String.format("[%s][%s] 封禁了 %s 个玩家", ban.get("formated"),emoji,ban.get("number"));
                ban.put("pushed", true);
                hasNewNotification = true;
            }

            if (hasNewNotification) {
                La.getINSTANCE().getNotificationsManager().push("Watchdog Ban Tracker", format, NotificationsEnum.SUCCESS, 5000);
            }
        }
    }

    public String getBanTrackerMessage() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("🐕🐕 Hypixel Ban Tracker 👮‍👮‍\n");
            sb.append("[🐕] 过去一分钟有 ").append(banData.watchdogLastMinute).append(" 人被狗咬了\n");
            sb.append("[🐕] 过去半小时有 ").append(banData.watchdogLastHalfHour).append(" 人被狗咬了\n");
            sb.append("[🐕‍] 狗在过去二十四小时内已封禁 ").append(banData.watchdogLastDay).append(" 人,\n\n");
            sb.append("[👮‍] 过去的一分钟有 ").append(banData.staffLastMinute).append(" 人被逮捕了\n");
            sb.append("[👮‍] 过去的半小时有 ").append(banData.staffLastHalfHour).append(" 人被逮捕了\n");
            sb.append("[👮‍] 客服在过去二十四小时内已封禁 ").append(banData.staffLastDay).append(" 人,\n\n");
            sb.append("上次更新: ").append(new SimpleDateFormat("HH:mm:ss").format(new Date(banData.lastUpdated))).append("\n");

            if (banHistory.isEmpty()) {
                sb.append("无最近封禁");
            } else {
                sb.append("最近封禁记录:\n");
                for (Map<String, Object> ban : banHistory) {
                    boolean isWatchdog = Boolean.TRUE.equals(ban.get("watchdog"));
                    String emoji = isWatchdog ? "🐕" : "👮";
                    sb.append("[").append(emoji).append("] [")
                            .append(ban.get("formated")).append("] banned ")
                            .append(ban.get("number")).append(" player.\n");
                }
            }
            return sb.toString().trim();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}