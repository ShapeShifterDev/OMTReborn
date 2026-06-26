package omtreborn.api.lists;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class AmmoList {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final List<Item> list = new ArrayList<>();

    public static boolean contains(ItemStack stack) {
        return !stack.isEmpty() && list.contains(stack.getItem());
    }

    public static void add(Item item) {
        if (list.contains(item)) {
            LOGGER.warn("Tried to add duplicate item to ammo list: {}", item);
        } else {
            list.add(item);
        }
    }

    public static void addIfAbsent(Item item) {
        if (!list.contains(item)) {
            list.add(item);
        }
    }

    public static boolean remove(Item item) {
        return list.remove(item);
    }

    public static void clear() {
        list.clear();
    }
}
