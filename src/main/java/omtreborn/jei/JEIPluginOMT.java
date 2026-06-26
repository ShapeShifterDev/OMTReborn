package omtreborn.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.ResourceLocation;
import omtreborn.OpenModularTurrets;

@JeiPlugin
public class JEIPluginOMT implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID =
            new ResourceLocation(OpenModularTurrets.MODID + ":jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }
}
