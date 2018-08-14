package craft;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public class Recipe {
	private static Map<String, Craft> craft = new HashMap<String, Craft>();

	public static void addRecipe(Plugin plugin, ItemStack stack, ItemStack[] istack) {
		ShapedRecipe rc = new ShapedRecipe(new NamespacedKey(plugin, UUID.randomUUID().toString()), stack);
		rc.shape("012", "345", "678");
		Craft cr = new Craft();
		cr.add(stack);
		for (int i = 0; i < 9; i++) {
			if (istack[i] != null && istack[i].getType() != Material.AIR) {
				rc.setIngredient(String.valueOf(i).toCharArray()[0], istack[i].getType());
			}
			cr.add(istack[i]);
		}
		Bukkit.getServer().addRecipe(rc);
		craft.put(rc.getKey().getKey(), cr);
	}

	public static Craft getRecipe(String key) {
		return craft.get(key);
	}

	public static Boolean hasRecipe(String key) {
		return craft.containsKey(key);
	}
}