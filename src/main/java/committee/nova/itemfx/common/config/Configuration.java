package committee.nova.itemfx.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Configuration {
    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec.BooleanValue blackListItem;
    public static final ForgeConfigSpec.BooleanValue whiteListItem;

    static {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("QuickPlant Configuration");
        blackListItem = builder.comment("Activate blacklist for item traits by tag 'itemfx:blacklist'. Items in won't have traits")
                .define("blackList", true);
        whiteListItem = builder.comment("Activate whitelist for item traits by tag 'itemfx:whitelist'. Only items in will have traits")
                .define("whiteList", false);
        COMMON_CONFIG = builder.build();
    }
}
