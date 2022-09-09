package meteor.rgb;

import meteor.rgb.commands.CommandExample;
import meteor.rgb.hud.HudExample;
import meteor.rgb.modules.ModuleExample;
import com.mojang.logging.LogUtils;
import meteor.rgb.util.RGBInterface;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

import java.util.ArrayList;

public class Addon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Example");
    public static final HudGroup HUD_GROUP = new HudGroup("Example");

    public static final RGBInterface RGB_INTERFACE = new RGBInterface();

    @Override
    public void onInitialize() {
        LOG.info("Starting MeteorRGB...");

        // Modules
        Modules.get().add(new ModuleExample());

        // Commands
        Commands.get().add(new CommandExample());

        // HUD
        Hud.get().register(HudExample.INFO);


    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "meteor.rgb";
    }

    public static void log(String s) {
        LOG.info(s);
    }
}
