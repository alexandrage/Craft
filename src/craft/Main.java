package craft;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new EventListener(), this);
		
		//TODO test start
		ItemStack istack = new ItemStack(Material.STONE);
		ItemStack stack = new ItemStack(Material.DIAMOND);
		Recipe.addRecipe(this, stack,
				new ItemStack[] { null, istack, istack, istack, istack, istack, istack, istack, istack });
		//TODO test stop
	}
}