package com.dean.baby.common.entity;

public enum ProgressStatus {
    NOT_STARTED("未開始"),
    IN_PROGRESS("已開始"),
    COMPLETED("已完成");

    private final String displayName;

    ProgressStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
