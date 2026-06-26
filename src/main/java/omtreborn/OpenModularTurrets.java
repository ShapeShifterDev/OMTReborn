package omtreborn;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import omtreborn.config.OMTConfig;
import omtreborn.init.*;
import omtreborn.network.OMTNetwork;

@Mod(OpenModularTurrets.MODID)
public class OpenModularTurrets {

    public static final String MODID = "omtreborn";

    public OpenModularTurrets(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModWorldgen.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);
        ModCreativeTab.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, OMTConfig.SPEC);
        modEventBus.addListener(OMTConfig::onLoad);
        modEventBus.addListener(OMTConfig::onReload);
        modEventBus.addListener(this::onCommonSetup);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(OMTNetwork::init);
    }
}
