package craft;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public class Recipe {
	private Map<String, Craft> crafts = new HashMap<String, Craft>();
	private Plugin plugin;

	public Recipe(Plugin plugin) {
		this.plugin = plugin;
	}

	public void addRecipe(Stack stack, Stack[] istack) {
		if (stack == null) {
			throw new IllegalArgumentException("Result cannot be null.");
		}
		ShapedRecipe rc = new ShapedRecipe(new NamespacedKey(plugin, UUID.randomUUID().toString()), stack.getStack());
		rc.shape("012", "345", "678");
		Craft cr = new Craft();
		cr.add(stack.getStack());
		for (int i = 0; i < 9; i++) {
			if (istack[i] != null && istack[i].getStack().getType() != Material.AIR) {
				rc.setIngredient(Character.forDigit(i, 10), istack[i].getStack().getType());
				cr.add(istack[i].getStack());
			} else {
				cr.add(null);
			}
		}
		Bukkit.getServer().addRecipe(rc);
		crafts.put(rc.getKey().getKey(), cr);
	}

	public void removeRecipe() {
		for (Entry<String, Craft> craft : this.crafts.entrySet()) {
			Bukkit.getServer().removeRecipe(new NamespacedKey(this.plugin, craft.getKey()));
		}
		this.crafts.clear();
	}

	public Craft getRecipe(String key) {
		return this.crafts.get(key);
	}

	public Boolean hasRecipe(String key) {
		return this.crafts.containsKey(key);
	}

	public void discoverRecipes(Player player) {
		for (Entry<String, Craft> craft : this.crafts.entrySet()) {
			player.discoverRecipe(new NamespacedKey(plugin, craft.getKey()));
		}
	}
}