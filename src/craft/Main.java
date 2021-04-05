package craft;

import java.util.Map.Entry;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private Recipe recipe;

	@Override
	public void onEnable() {
		this.recipe = new Recipe(this);
		this.getServer().getPluginManager().registerEvents(new EventListener(this.recipe), this);
		Configs cfgs = new Configs(this, this.getDataFolder());
		for (Entry<String, CustomConfig> cfg : cfgs.getConfigs().entrySet()) {
			CustomConfig recipe = cfgs.get(cfg.getKey());
			StackList list = new StackList(recipe.getCfg());
			this.recipe.addRecipe(cfg.getKey(), list.getMap().get("result"),
					new Stack[] { list.getMap().get("1"), list.getMap().get("2"), list.getMap().get("3"),
							list.getMap().get("4"), list.getMap().get("5"), list.getMap().get("6"),
							list.getMap().get("7"), list.getMap().get("8"), list.getMap().get("9") });
		}
		new Scheduler().runTaskTimerAsynchronously(this, 1, 1);
	}

	@Override
	public void onDisable() {
		this.recipe.removeRecipe();
	}
}