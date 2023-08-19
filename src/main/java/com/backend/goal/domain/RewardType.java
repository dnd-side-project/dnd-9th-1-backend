package com.backend.goal.domain;

public enum RewardType {
    BLUE_JEWEL_1(1),
    BLUE_JEWEL_2(2),
    BLUE_JEWEL_3(3),
    BLUE_JEWEL_4(4),
    BLUE_JEWEL_5(5),
    PURPLE_JEWEL_1(6),
    PURPLE_JEWEL_2(7),
    PURPLE_JEWEL_3(8),
    PURPLE_JEWEL_4(9),
    PURPLE_JEWEL_5(10),
    PINK_JEWEL_1(11),
    PINK_JEWEL_2(12),
    PINK_JEWEL_3(13),
    PINK_JEWEL_4(14),
    PINK_JEWEL_5(15),
    GREEN_JEWEL_1(16),
    GREEN_JEWEL_2(17),
    GREEN_JEWEL_3(18),
    GREEN_JEWEL_4(19),
    GREEN_JEWEL_5(20);

    public int order;

    RewardType(int order) {
        this.order = order;
    }
}
