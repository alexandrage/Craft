package craft;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import com.destroystokyo.paper.event.player.PlayerRecipeBookClickEvent;

public class EventListener implements Listener {
	private Recipe recipe;

	public EventListener(Recipe recipe) {
		this.recipe = recipe;
	}

	@EventHandler
	public void on(PlayerJoinEvent e) {
		this.recipe.discoverRecipes(e.getPlayer());
	}

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
		if (this.recipe.hasRecipe(rc.getKey().getKey())) {
			CraftingInventory inv = e.getInventory();
			Craft craft = this.recipe.getRecipe(rc.getKey().getKey());
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

	@EventHandler
	public void on(PlayerRecipeBookClickEvent e) {
		if (this.recipe.hasRecipe(e.getRecipe().getKey())) {
			e.getPlayer().getOpenInventory().getTopInventory();
			// TODO
		}
	}
}