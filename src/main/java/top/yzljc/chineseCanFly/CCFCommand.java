package top.yzljc.chineseCanFly;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public final class CCFCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("该命令只能由玩家执行。");
            return true;
        }
        if (args.length != 0) {
            player.sendMessage("§e用法：/" + label);
            return true;
        }
        if (!CCFEvent.isChineseLocale(player.getLocale())) {
            player.sendMessage("§c黄皮肤才对，讲中文才飞，中国就是美");
            return true;
        }
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            player.sendMessage("§e当前游戏模式下无需使用该命令！");
            return true;
        }

        if (player.getAllowFlight()) {
            player.setFlying(false);
            player.setAllowFlight(false);
            CCFEvent.getCNNBPLAYER().remove(player.getUniqueId());
            player.sendMessage("§e飞行已关闭。");
        } else {
            player.setAllowFlight(true);
            player.setFlying(true);
            CCFEvent.getCNNBPLAYER().add(player.getUniqueId());
            player.sendMessage("§a中国人能飞，中国人能飞");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return List.of();
    }
}
