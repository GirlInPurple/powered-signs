package mod.blurple.poweredsigns;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PoweredSignsPlugin extends JavaPlugin implements Listener {

  @Override
  public void onEnable() {
    this.getServer().getPluginManager().registerEvents(this, this);
  }

  @EventHandler
  public void onRedstoneBlockChange(BlockRedstoneEvent redstoneEvent) {
    redstoneEvent.getBlock();

  }
}
