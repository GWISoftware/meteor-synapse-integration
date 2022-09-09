package meteor.rgb.modules;

import meteor.rgb.Addon;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.Settings;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;

public class RGBSync extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public RGBSync() {
        super(Addon.CATEGORY, "rgb-sync", "sync the game with your pc's rgb");
    }
}
