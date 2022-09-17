package meteor.rgb;

import me.ghost.rgbhelper.iterf.RGBInterface;
import meteor.rgb.commands.CommandExample;
import meteor.rgb.hud.HudExample;
import meteor.rgb.modules.ModuleExample;
import com.mojang.logging.LogUtils;
import meteor.rgb.modules.RGBSync;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Addon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Example");
    public static final HudGroup HUD_GROUP = new HudGroup("Example");

    public static RGBInterface RGB_INTERFACE = null;
    public static ExecutorService RGB_THREAD = Executors.newCachedThreadPool();

    @Override
    public void onInitialize() {
        LOG.info("Starting MeteorRGB...");

        RGB_THREAD.execute(() -> {
            RGB_INTERFACE = new RGBInterface(FabricLoader.getInstance().getGameDir().toFile());
            while (!RGB_INTERFACE.isOk()) {
                try {
                    Thread.sleep(1000);
                } catch (Exception ignored) {}
            }
        });


        Modules.get().add(new RGBSync());

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
