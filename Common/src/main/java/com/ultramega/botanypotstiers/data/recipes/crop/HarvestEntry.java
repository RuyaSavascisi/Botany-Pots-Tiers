package com.ultramega.botanypotstiers.data.recipes.crop;

import net.minecraft.world.item.ItemStack;

public class HarvestEntry {
    /**
     * The chance that the entry should happen.
     */
    private final float chance;

    /**
     * The item to give.
     */
    private final ItemStack item;

    /**
     * The lowest amount of the item to give.
     */
    private final int minRolls;

    /**
     * The maximum amount of the item to give.
     */
    private final int maxRolls;

    public HarvestEntry(float chance, ItemStack item, int minRolls, int maxRolls) {
        this.chance = chance;
        this.item = item;
        this.minRolls = minRolls;
        this.maxRolls = maxRolls;

        if (minRolls < 0 || maxRolls < 0) {
            throw new IllegalArgumentException("Rolls must not be negative!");
        }

        if (minRolls > maxRolls) {
            throw new IllegalArgumentException("Min rolls must not be greater than max rolls!");
        }
    }

    /**
     * Gets the chance for the entry to happen.
     *
     * @return The chance for the entry to happen.
     */
    public float getChance() {
        return this.chance;
    }

    /**
     * Gets the item to give from this entry.
     *
     * @return The item to give.
     */
    public ItemStack getItem() {
        return this.item;
    }

    /**
     * Gets the minimum amount of items to give.
     *
     * @return The minimum amount of items to give.
     */
    public int getMinRolls() {
        return this.minRolls;
    }

    /**
     * Gets the maximum amount of items to give.
     *
     * @return The maximum amount of items to give.
     */
    public int getMaxRolls() {
        return this.maxRolls;
    }
}