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

    private Process process = null;
    private boolean ok;


    public RGBInterface() {
        Addon.log("Starting RGBInterface...");

        File home = new File(FabricLoader.getInstance().getGameDir().toFile(), "meteor-rgb");
        File helper = new File(home, "rgbhelper.exe");
        //todo dll check
        if (!home.exists()) home.mkdirs();
        if (!helper.exists()) {
            Addon.log("Failed to create RGBInterface, rgbhelper is missing.");
            this.ok = false;
            return;
        }

        new Thread(() -> {
            ProcessBuilder builder = new ProcessBuilder(helper.getAbsolutePath(), "green");
            try {
                this.process = builder.start();
                new Thread(() -> { // helper will close otherwise lol
                    try {
                        this.process.waitFor();
                    } catch (InterruptedException ignored) {}
                }).start();
                BufferedReader br = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
                String line; // wait for the helper to be ready (synapse to give control to the helper)
                while ((line = br.readLine()) != null) {
                    //System.out.println(line);
                    if (line.contains("Command?")) break;
                }
                br.close();
                System.out.println("RGBInterface linked!");
                this.ok = true;
            } catch (IOException e) {
                System.out.println("Failed to create RGBInterface, couldn't start rgbhelper.");
                e.printStackTrace();
                this.ok = false;
            }
        }).start();

    }


    public boolean isOk() {
        return ok;
    }


    public void sendMacro(ArrayList<String> commands) {
        StringBuilder command = new StringBuilder("macro ");
        for (String cmd : commands) command.append(cmd).append(",");
        command.append("\n");
        System.out.println("sendMacro with command: " + command);
        try {
            if (!this.process.isAlive()) {
                System.out.println("sendMacro failed, process is dead.");
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
                System.out.println("sendSingle failed, process is dead.");
                return;
            }

            this.process.outputWriter().write(cmd);
            this.process.outputWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
