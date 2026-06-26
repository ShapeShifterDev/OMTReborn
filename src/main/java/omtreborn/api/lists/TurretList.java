package omtreborn.api.lists;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TurretList {

    private static final Map<String, String> types = new HashMap<>();

    public static void addTurret(String registryName) {
        types.put(registryName, registryName);
    }

    @Nullable
    public static String getTurretType(String registryName) {
        return types.get(registryName);
    }

    public static boolean isTurret(String registryName) {
        return types.containsKey(registryName);
    }

    public static Collection<String> getAllTurrets() {
        return Collections.unmodifiableCollection(types.keySet());
    }
}
