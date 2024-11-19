package com._roomthon.irumso.policy;

public enum IncomeLevel {
    BELOW_50("중위소득 0~50%"),
    BETWEEN_51_AND_75("중위소득 51~75%"),
    BETWEEN_76_AND_100("중위소득 76~100%"),
    BETWEEN_101_AND_200("중위소득 101~200%"),
    ABOVE_200("중위소득 200% 초과");

    private final String description;

    IncomeLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
