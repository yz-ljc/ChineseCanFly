package top.yzljc.chineseCanFly;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class ChineseCanFly extends JavaPlugin {

    @Getter
    private static ChineseCanFly instance;

    private BukkitTask languageScanTask;

    @Override
    public void onEnable() {
        instance = this;
        var events = new CCFEvent();
        getServer().getPluginManager().registerEvents(events, this);

        languageScanTask = getServer().getScheduler().runTaskTimer(this, events, 200L, 200L);

        var executor = new CCFCommand();
        getCommand("ccf").setExecutor(executor);
        getCommand("ccf").setTabCompleter(executor);
    }

    @Override
    public void onDisable() {
        if (languageScanTask != null) {
            languageScanTask.cancel();
        }
        CCFEvent.getCNNBPLAYER().clear();
        instance = null;
    }
}
