package craft;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public class Recipe {
	private static Map<String, Craft> crafts = new HashMap<String, Craft>();

	public static void addRecipe(Plugin plugin, Stack stack, Stack[] istack) {
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
	
	
	public static void removeRecipe(Plugin plugin) {
		for(Entry<String, Craft> craft : crafts.entrySet()) {
			Bukkit.getServer().removeRecipe(new NamespacedKey(plugin, craft.getKey()));
		}
		crafts.clear();
	}

	public static Craft getRecipe(String key) {
		return crafts.get(key);
	}

	public static Boolean hasRecipe(String key) {
		return crafts.containsKey(key);
	}
}