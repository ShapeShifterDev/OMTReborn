package omtreborn.api.lists;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class NeutralList {

    private static final List<String> list = new ArrayList<>();

    public static boolean contains(String entityId) {
        for (String mob : list) {
            if (mob.equalsIgnoreCase(entityId)) return true;
        }
        return false;
    }

    public static boolean contains(LivingEntity entity) {
        var type = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        return type != null && contains(type.toString());
    }

    public static void add(String entityId) {
        if (!contains(entityId)) list.add(entityId);
    }

    public static boolean remove(String entityId) {
        return list.removeIf(s -> s.equalsIgnoreCase(entityId));
    }

    public static void clear() {
        list.clear();
    }
}
