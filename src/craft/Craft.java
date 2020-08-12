package craft;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Craft {
	private List<ItemStack> stacks = new ArrayList<ItemStack>();

	public void add(ItemStack stack) {
		stacks.add(stack);
	}

	public ItemStack get(int i) {
		return stacks.get(i);
	}
}