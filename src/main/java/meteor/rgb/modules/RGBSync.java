package meteor.rgb.modules;

import meteor.rgb.Addon;
import meteor.rgb.util.Maths;
import meteordevelopment.meteorclient.events.entity.DamageEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.Systems;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.projectile.ArrowEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RGBSync extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();



    private final Setting<Boolean> topBarHealth = sgGeneral.add(new BoolSetting.Builder().name("topbar-health").description("Shows your health across the top of the keyboard.").defaultValue(true).build());
    private final Setting<Integer> topBarHealthDelay = sgGeneral.add(new IntSetting.Builder().name("topbar-health-delay").description("The delay in ms between updating topbar health.").defaultValue(500).min(100).sliderMax(1000).visible(topBarHealth::get).build());
    private final Setting<Boolean> onDamage = sgGeneral.add(new BoolSetting.Builder().name("onDamage").description("run a lighting command on damage.").defaultValue(true).build());
    private final Setting<List<String>> damageMacro = sgGeneral.add(new StringListSetting.Builder().name("onDamage-command").description("what effects to do on damage").defaultValue(List.of("blink red 2 delay 90", "restore")).visible(onDamage::get).build());
    private final Setting<Integer> damageDelay = sgGeneral.add(new IntSetting.Builder().name("onDamage-delay").description("The delay in ms between showing damage effect(s).").defaultValue(1000).min(100).sliderMax(2000).visible(onDamage::get).build());


    private final Setting<Boolean> onEat = sgGeneral.add(new BoolSetting.Builder().name("onEat").description("run a lighting command when eating.").defaultValue(true).build());
    private final Setting<List<String>> eatMacro = sgGeneral.add(new StringListSetting.Builder().name("onDamage-command").description("what effects to do on damage")
        .defaultValue(List.of("scroll blank yellow 1 bottom_to_top delay 55", "delay 100", "restore")).visible(onEat::get).build());
    private final Setting<Integer> eatDelay = sgGeneral.add(new IntSetting.Builder().name("onDamage-delay").description("The delay in ms between showing damage effect(s).")
        .defaultValue(1000).min(100).sliderMax(2000).visible(onEat::get).build());


    public RGBSync() {
        super(Addon.CATEGORY, "rgb-sync", "sync the game with your pc's rgb");
    }
    private long lastDamage;
    private long lastHealth;
    private long lastEat;

    @Override
    public void onActivate() {
        if (Addon.RGB_INTERFACE == null) {
            error("RGB Interface is not connected.");
            this.toggle();
            return;
        }
        lastDamage = Maths.now();
        lastHealth = Maths.now();
        lastEat = Maths.now();
    }


    @EventHandler
    public void onTick(TickEvent.Post event) {
        if (topBarHealth.get() && Maths.msPassed(lastHealth) >= topBarHealthDelay.get()) setTopBarHealth();
        if (onEat.get() && isEating() && Maths.msPassed(lastEat) >= eatDelay.get()) runEating();
    }


    @EventHandler
    public void onDamage(DamageEvent de) {
        if (de.entity != mc.player || Maths.msPassed(lastDamage) <= damageDelay.get()) return;
        Addon.RGB_INTERFACE.sendMacro((ArrayList<String>) damageMacro.get());
    }



    private void runEating() {
        Addon.RGB_INTERFACE.sendMacro((ArrayList<String>) eatMacro.get());
        lastEat = Maths.now();
    }

    private void setTopBarHealth() {
        float absorption = mc.player.getAbsorptionAmount();
        int health = Math.round(mc.player.getHealth() + absorption);
        double healthPercentage = health / (mc.player.getMaxHealth() + absorption);

        String healthColor;
        if (healthPercentage <= 0.333) healthColor = "red";
        else if (healthPercentage <= 0.666) healthColor = "yellow";
        else healthColor = "green";

        Addon.RGB_INTERFACE.sendMacro(new ArrayList<>(List.of("topbar " + healthColor)));
        lastHealth = Maths.now();
    }

    private boolean isEating() {
        return mc.player.getMainHandStack().getItem().isFood() && mc.player.isUsingItem();
    }
}
