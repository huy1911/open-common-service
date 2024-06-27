package com.lpb.mid.log;

public enum LogLevel {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high");

    private String val;

    private LogLevel(String val) {
        this.val = val;
    }

    public String getVal() {
        return this.val;
    }
}
