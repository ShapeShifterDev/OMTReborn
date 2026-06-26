package omtreborn.lib.api;

public interface IPowerExchangeTile {

    int extractEnergyForAddon(int maxExtract, boolean simulate);

    int getStoredEnergy();

    int getMaxEnergy();
}
