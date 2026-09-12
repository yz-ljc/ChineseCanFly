package top.yzljc.chineseCanFly;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @Author YZ_Ljc_
 * @ClassName CCFEvent
 * @Created_at 2026/09/12
 * @Project ChineseCanFly
 * @Package top.yzljc.chineseCanFly
 */
public class CCFEvent implements Listener, Runnable {

    @Getter
    private static final Set<UUID> CNNBPLAYER = new HashSet<>();

    private static final Set<String> ZHONGGUORENNENGFGEI = Set.of("zh_cn", "zh_tw", "zh_hk", "lzh");

    private final Map<UUID, String> lastLocales = new HashMap<>();

    public static boolean isChineseLocale(String locale) {
        return ZHONGGUORENNENGFGEI.contains(locale.toLowerCase(Locale.ROOT));
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            checkLocale(player);
        }
    }

    private void checkLocale(Player player) {
        UUID playerId = player.getUniqueId();
        String locale = player.getLocale();
        String previousLocale = lastLocales.get(playerId);
        if (locale.equals(previousLocale)) {
            return;
        }
        lastLocales.put(playerId, locale);

        boolean chinese = isChineseLocale(locale);
        if (previousLocale != null && chinese == isChineseLocale(previousLocale)) {
            return;
        }

        if (chinese) {
            if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR
                    || player.getAllowFlight()) {
                return;
            }
            player.setAllowFlight(true);
            player.setFlying(true);
            CNNBPLAYER.add(playerId);
            player.sendMessage("§a中国人能飞，你已起飞");
        } else {
            CNNBPLAYER.remove(playerId);
            if (player.isFlying()) {
                player.setFlying(false);
            }
            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
            }
            player.sendMessage("§c黄皮肤才对，讲中文才飞，中国就是美");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Bukkit.getScheduler().runTaskLater(ChineseCanFly.getInstance(), () -> {

            if (!event.getPlayer().isOnline()) {
                return;
            }

            checkLocale(event.getPlayer());

        }, 20L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        lastLocales.remove(event.getPlayer().getUniqueId());
        CNNBPLAYER.remove(event.getPlayer().getUniqueId());
    }
}
