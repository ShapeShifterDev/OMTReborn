package omtreborn.api.lists;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class MobBlacklist {

    private static final Logger LOGGER = LogManager.getLogger();
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
        if (contains(entityId)) {
            LOGGER.warn("Tried to add duplicate mob to blacklist: {}", entityId);
        } else {
            list.add(entityId);
        }
    }

    public static boolean remove(String entityId) {
        return list.removeIf(s -> s.equalsIgnoreCase(entityId));
    }

    public static void clear() {
        list.clear();
    }
}
