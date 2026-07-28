package com.betier;

public class TierConstants {
    public static final String API_BASE = "https://betierapi.ccwu.cc";

    public static final String[] MODES = {
            "Sword", "Axe", "SMP", "UHC", "Crystal", "Mace", "Pot", "NethPot",
            "OPot", "OUHC", "Bedwars", "Fireball", "TopFight", "Archer", "Sumo", "Boxing"
    };

    public static int getTierScore(String tier) {
        return switch (tier) {
            case "HT1", "RHT1" -> 60;
            case "LT1", "RLT1" -> 45;
            case "HT2", "RHT2" -> 30;
            case "LT2", "RLT2" -> 20;
            case "PLT1" -> 50;
            case "PHT2" -> 34;
            case "PLT2" -> 23;
            case "PHT3" -> 12;
            case "PLT3" -> 7;
            case "HT3" -> 10;
            case "LT3" -> 6;
            case "HT4" -> 4;
            case "LT4" -> 3;
            case "HT5" -> 2;
            case "LT5" -> 1;
            default -> 0;
        };
    }

    public static int getTotalTierColor(int total) {
        if (total >= 800) return 0xFFFFD700;
        if (total >= 500) return 0xFFB35CFF;
        if (total >= 200) return 0xFF42A5F5;
        if (total >= 100) return 0xFF36D399;
        if (total >= 40) return 0xFFF2EE63;
        if (total >= 20) return 0xFFFF8533;
        return 0xFF868686;
    }

    public static int getTierLabelColor(String tier) {
        return switch (tier) {
            case "HT1", "RHT1" -> 0xFFE8BA3A;
            case "LT1", "RLT1" -> 0xFFD5B355;
            case "HT2", "RHT2" -> 0xFFC4D3E7;
            case "LT2", "RLT2" -> 0xFFA0A7B2;
            case "HT3" -> 0xFFF89F5A;
            case "LT3" -> 0xFFC67B42;
            case "HT4" -> 0xFF81749A;
            case "LT4" -> 0xFF655B79;
            case "HT5" -> 0xFF8F82A8;
            case "LT5" -> 0xFF655B79;
            case "PLT1" -> 0xFFFF6B6B;
            case "PHT2" -> 0xFFFF9494;
            case "PLT2" -> 0xFFFFA8A8;
            case "PHT3" -> 0xFFFFC2C2;
            case "PLT3" -> 0xFFFFD8D8;
            default -> 0xFF5A5A5A;
        };
    }

    public static final int GOLD = 0xFFFFD700;
    public static final int SILVER = 0xFFC0C0C0;
    public static final int BRONZE = 0xFFCD7F32;
    public static final long CACHE_TTL = 300000;
}
