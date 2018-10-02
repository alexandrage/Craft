package craft;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class EventListener implements Listener {
	@EventHandler
	public void on(PrepareItemCraftEvent e) {
		org.bukkit.inventory.Recipe r = e.getRecipe();
		if (r == null) {
			return;
		}
		if (!(r instanceof ShapedRecipe)) {
			return;
		}
		ShapedRecipe rc = (ShapedRecipe) r;
		if (Recipe.hasRecipe(rc.getKey().getKey())) {
			CraftingInventory inv = e.getInventory();
			Craft craft = Recipe.getRecipe(rc.getKey().getKey());
			ItemStack[] cont = inv.getContents();
			for (int i = 0; i < cont.length; i++) {
				if (craft.get(i) != null) {
					if (!craft.get(i).isSimilar(cont[i])) {
						e.getInventory().setResult(new ItemStack(Material.AIR));
						return;
					}
				}
			}
		}
	}
}