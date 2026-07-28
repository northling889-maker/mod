package com.betier;

import com.google.gson.annotations.SerializedName;

public class PlayerTierData {
    public String id;
    public String name;
    public String color;
    @SerializedName("Sword") public String Sword;
    @SerializedName("Axe") public String Axe;
    @SerializedName("SMP") public String SMP;
    @SerializedName("UHC") public String UHC;
    @SerializedName("Crystal") public String Crystal;
    @SerializedName("Mace") public String Mace;
    @SerializedName("Pot") public String Pot;
    @SerializedName("NethPot") public String NethPot;
    @SerializedName("OPot") public String OPot;
    @SerializedName("OUHC") public String OUHC;
    @SerializedName("Bedwars") public String Bedwars;
    @SerializedName("Fireball") public String Fireball;
    @SerializedName("TopFight") public String TopFight;
    @SerializedName("Archer") public String Archer;
    @SerializedName("Sumo") public String Sumo;
    @SerializedName("Boxing") public String Boxing;

    public int calcTotal() {
        int sum = 0;
        sum += TierConstants.getTierScore(Sword);
        sum += TierConstants.getTierScore(Axe);
        sum += TierConstants.getTierScore(SMP);
        sum += TierConstants.getTierScore(UHC);
        sum += TierConstants.getTierScore(Crystal);
        sum += TierConstants.getTierScore(Mace);
        sum += TierConstants.getTierScore(Pot);
        sum += TierConstants.getTierScore(NethPot);
        sum += TierConstants.getTierScore(OPot);
        sum += TierConstants.getTierScore(OUHC);
        sum += TierConstants.getTierScore(Bedwars);
        sum += TierConstants.getTierScore(Fireball);
        sum += TierConstants.getTierScore(TopFight);
        sum += TierConstants.getTierScore(Archer);
        sum += TierConstants.getTierScore(Sumo);
        sum += TierConstants.getTierScore(Boxing);
        return sum;
    }

    public String getTierByMode(String mode) {
        return switch (mode) {
            case "Sword" -> Sword;
            case "Axe" -> Axe;
            case "SMP" -> SMP;
            case "UHC" -> UHC;
            case "Crystal" -> Crystal;
            case "Mace" -> Mace;
            case "Pot" -> Pot;
            case "NethPot" -> NethPot;
            case "OPot" -> OPot;
            case "OUHC" -> OUHC;
            case "Bedwars" -> Bedwars;
            case "Fireball" -> Fireball;
            case "TopFight" -> TopFight;
            case "Archer" -> Archer;
            case "Sumo" -> Sumo;
            case "Boxing" -> Boxing;
            default -> "-";
        };
    }
}
