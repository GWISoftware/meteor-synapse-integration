package meteor.rgb.util;

import meteor.rgb.Addon;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RGBInterface {

    private final File home = new File(FabricLoader.getInstance().getGameDir().toFile(), "meteor-rgb");
    private final ExecutorService thread = Executors.newCachedThreadPool();
    private Process process = null;
    private boolean ok;
    private boolean created;


    public RGBInterface() {
        Addon.log("Starting RGBInterface...");

        this.create();


    }

    private void create() {
        if (created) {
            Addon.log("rgbhelper crashed, attempting to restart interface.");
        } else {
            created = true;
        }
        File helper = new File(home, "rgbhelper.exe");
        if (!helper.exists()) {
            Addon.log("Failed to create RGBInterface, rgbhelper is missing.");
            this.ok = false;
            return;
        }
        File colore = new File(home, "Colore.dll");
        if (!colore.exists()) {
            Addon.log("Failed to create RGBInterface, Colore.dll is missing.");
            this.ok = false;
            return;
        }
        ProcessBuilder builder = new ProcessBuilder(helper.getAbsolutePath(), "blue");
        try {
            this.process = builder.start();
            thread.execute(() -> { // helper will close otherwise lol
                try {
                    this.process.waitFor();
                } catch (InterruptedException ignored) {}
            });
            BufferedReader br = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
            String line; // wait for the helper to be ready (synapse to give control to the helper)
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                if (line.contains("Command?")) break;
            }
            br.close();
            Addon.log("RGBInterface linked!");
            this.ok = true;
            thread.execute(() -> {
                try {
                    Thread.sleep(500);
                } catch (Exception ignored) {}
                this.startupEffect();
            });
        } catch (IOException e) {
            System.out.println("Failed to create RGBInterface, couldn't start rgbhelper.");
            e.printStackTrace();
            this.ok = false;
        }
    }

    public boolean isOk() {
        return ok;
    }


    public void sendMacro(ArrayList<String> commands) {
        StringBuilder command = new StringBuilder("macro ");
        for (String cmd : commands) command.append(cmd).append(",");
        command.append("\n");
        try {
            if (!this.process.isAlive()) {
                this.ok = false;
                return;
            }
            this.process.outputWriter().write(command.toString());
            this.process.outputWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendSingle(String cmd) {
        cmd += "\n";
        try {
            if (!this.process.isAlive()) {
                this.ok = false;
                return;
            }
            this.process.outputWriter().write(cmd);
            this.process.outputWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // built-in effects

    public void startupEffect() {
        if (!this.ok) return;
        ArrayList<String> macro = new ArrayList<>();
        macro.add("solid blank");
        macro.add("delay 500");
        macro.add("scroll custom F519B3 custom 913DE2 2 top_to_bottom delay 35");
        macro.add("delay 150");
        macro.add("restore");
        this.sendMacro(macro);
    }




}
