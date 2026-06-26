package omtreborn.lib.util;

import net.minecraft.nbt.CompoundTag;

public class TargetingSettings {

    private boolean targetPlayers;
    private boolean targetMobs;
    private boolean targetPassive;
    private int range;
    private int maxRange;

    public TargetingSettings(boolean targetPlayers, boolean targetMobs, boolean targetPassive, int range, int maxRange) {
        this.targetPlayers = targetPlayers;
        this.targetMobs = targetMobs;
        this.targetPassive = targetPassive;
        this.maxRange = maxRange;
        this.range = Math.min(range, maxRange);
    }

    public static TargetingSettings readFromNBT(CompoundTag tag) {
        if (tag.contains("targetingSettings")) {
            CompoundTag sub = tag.getCompound("targetingSettings");
            return new TargetingSettings(
                sub.getBoolean("targetPlayers"),
                sub.getBoolean("targetMobs"),
                sub.getBoolean("targetPassive"),
                sub.getInt("range"),
                sub.getInt("maxRange")
            );
        }
        // Legacy 1.12.2 format fallback
        return new TargetingSettings(
            tag.getBoolean("attacksPlayers"),
            tag.getBoolean("attacksMobs"),
            tag.getBoolean("attacksPassive"),
            tag.getInt("currentMaxRange"),
            tag.contains("maxRange") ? tag.getInt("maxRange") : 16
        );
    }

    public void writeToNBT(CompoundTag tag) {
        CompoundTag sub = new CompoundTag();
        sub.putBoolean("targetPlayers", targetPlayers);
        sub.putBoolean("targetMobs", targetMobs);
        sub.putBoolean("targetPassive", targetPassive);
        sub.putInt("range", range);
        sub.putInt("maxRange", maxRange);
        tag.put("targetingSettings", sub);
    }

    public boolean isTargetPlayers() { return targetPlayers; }
    public TargetingSettings setTargetPlayers(boolean v) { targetPlayers = v; return this; }

    public boolean isTargetMobs() { return targetMobs; }
    public TargetingSettings setTargetMobs(boolean v) { targetMobs = v; return this; }

    public boolean isTargetPassive() { return targetPassive; }
    public TargetingSettings setTargetPassive(boolean v) { targetPassive = v; return this; }

    public int getRange() { return range; }
    public TargetingSettings setRange(int v) { range = Math.min(v, maxRange); return this; }

    public int getMaxRange() { return maxRange; }
    public TargetingSettings setMaxRange(int v) {
        maxRange = v;
        range = Math.min(range, maxRange);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TargetingSettings)) return false;
        TargetingSettings other = (TargetingSettings) o;
        return targetPlayers == other.targetPlayers &&
               targetMobs == other.targetMobs &&
               targetPassive == other.targetPassive &&
               range == other.range &&
               maxRange == other.maxRange;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(targetPlayers, targetMobs, targetPassive, range, maxRange);
    }
}
