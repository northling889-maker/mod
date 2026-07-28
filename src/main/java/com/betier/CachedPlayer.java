package com.betier;

public class CachedPlayer {
    public final PlayerTierData data;
    public final long fetchTime;

    public CachedPlayer(PlayerTierData data, long time) {
        this.data = data;
        this.fetchTime = time;
    }
    public boolean isExpired() {
        return System.currentTimeMillis() - fetchTime > TierConstants.CACHE_TTL;
    }
}
