package craft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

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
					if (!this.isSimilar(craft.get(i), cont[i])) {
						e.getInventory().setResult(new ItemStack(Material.AIR));
						return;
					}
				}
			}
		}
	}

	@EventHandler
	public void on(PlayerRecipeBookClickEvent e) {
		String key = e.getRecipe().getKey();
		if (this.recipe.hasRecipe(key)) {
			e.setCancelled(true);
			Player player = e.getPlayer();
			Craft craft = this.recipe.getRecipe(e.getRecipe().getKey());
			Inventory pinv = player.getInventory();
			Inventory inv = player.getOpenInventory().getTopInventory();
			ItemStack[] cont = inv.getContents();
			List<Integer> list = new ArrayList<Integer>();
			int min = 0;
			for (int i = 1; i < cont.length; ++i) {
				if (craft.get(i) != null) {
					if (!this.isSimilar(craft.get(i), cont[i])) {
						inv.setItem(i, new ItemStack(Material.AIR));
						pinv.addItem(cont[i]);
						list.add(0);
					} else {
						list.add(cont[i].getAmount());
					}
				} else if (cont[i] != null || cont[i].getType() != Material.AIR) {
					inv.setItem(i, new ItemStack(Material.AIR));
					pinv.addItem(cont[i]);
				}
			}
			Collections.sort(list);
			if (!list.isEmpty()) {
				min = list.get(0);
			}
			Inventory temp = Bukkit.createInventory(null, 54);
			temp.setContents(pinv.getContents());
			for (int i = 1; i < cont.length; ++i) {
				if ((craft.get(i) != null) && cont[i].getAmount() == min) {
					if (!this.check(temp, craft.get(i), 1)) {
						return;
					}
				}
			}
			cont = inv.getContents();
			for (int i = 1; i < cont.length; ++i) {
				if ((craft.get(i) != null) && cont[i].getAmount() == min) {
					if (this.check(pinv, craft.get(i), 1)) {
						ItemStack set = craft.get(i);
						set.setAmount(cont[i].getAmount() + 1);
						inv.setItem(i, set);
					}
				}
			}
			cont = null;
			temp.clear();
		}
	}

	public int calc(Inventory inv, ItemStack s) {
		int count = 0;
		for (int i = 0; i < inv.getSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (stack == null) {
				continue;
			}
			if (this.isSimilar(stack, s)) {
				count += stack.getAmount();
			}
		}
		return count;
	}

	public boolean check(Inventory inv, ItemStack s, int c) {
		if (this.calc(inv, s) >= c) {
			this.clear(inv, s, c);
			return true;
		}
		return false;
	}

	public void clear(Inventory inv, ItemStack s, int c) {
		for (int i = 0; i < inv.getSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (stack == null)
				continue;
			if (this.isSimilar(stack, s)) {
				if (stack.getAmount() == 0)
					break;
				if (stack.getAmount() <= c) {
					c = c - stack.getAmount();
					stack.setAmount(-1);
				}
				if (stack.getAmount() > c) {
					stack.setAmount(stack.getAmount() - c);
					c = 0;
				}
			}
		}
	}

	public boolean isSimilar(ItemStack s1, ItemStack s2) {
		ItemMeta m1 = s1.getItemMeta();
		ItemMeta m2 = s2.getItemMeta();
		if (m1 == null || m2 == null) {
			return false;
		}
		if (m1.hasCustomModelData() && m2.hasCustomModelData()) {
			return s1.getType() == s2.getType() && m1.getCustomModelData() == m2.getCustomModelData();
		}
		return s1.isSimilar(s2);
	}
}