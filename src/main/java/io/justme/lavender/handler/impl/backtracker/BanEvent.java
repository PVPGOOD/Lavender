package io.justme.lavender.handler.impl.backtracker;

public class BanEvent {
    public long timestamp;
    public int number;
    public boolean watchdog;

    public BanEvent(long timestamp, int number, boolean watchdog) {
        this.timestamp = timestamp;
        this.number = number;
        this.watchdog = watchdog;
    }
}