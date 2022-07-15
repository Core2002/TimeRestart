package fun.fifu.timerestart;

import com.alkaidmc.alkaid.bukkit.command.AlkaidCommand;
import com.alkaidmc.alkaid.bukkit.event.AlkaidEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class TimeRestart extends JavaPlugin implements Listener {
    public static TimeRestart plugin;

    public int restartNum = -1;

    public int playerNum = 0;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        new AlkaidEvent(plugin).simple()
                .event(PlayerQuitEvent.class)
                .listener(playerQuitEvent -> {
                    playerNum = getServer().getOnlinePlayers().size() - 1;
                    checkShutdown();
                })
                .priority(EventPriority.HIGHEST)
                .ignore(false)
                .register();

        new AlkaidCommand(plugin).simple()
                .command("timerestart")
                .description("当服务器人数<=某值时重启")
                .permission("timerestart.permission")
                .usage("/timerestart <人数>  当服务器人数<=某值时重启")
                .aliases(List.of("ts"))
                .executor((sender, command, label, args) -> {
                    playerNum = getServer().getOnlinePlayers().size();
                    if (args.length == 1)
                        restartNum = Integer.parseInt(args[0]);
                    if (restartNum >= 0)
                        sender.sendMessage("服务器将在玩家人数<=" + restartNum + "时重启");
                    else
                        sender.sendMessage("重启关闭");
                    checkShutdown();
                    return true;
                })
                .tab((sender, command, alias, args) -> List.of("5", "4", "3", "2", "1"))
                .register();

    }

    private void checkShutdown() {
        if (restartNum >= 0 && playerNum <= restartNum) {
            getServer().getLogger().info("玩家人数<=" + restartNum + "，重启");
            getServer().shutdown();
        }
    }

}
