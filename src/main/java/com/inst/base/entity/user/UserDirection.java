package com.inst.base.entity.user;

public enum UserDirection {
    FOOD("Еда"),
    SPORT("Спорт");

    private final String displayName;

    UserDirection(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
