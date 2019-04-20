package craft;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.plugin.java.JavaPlugin;

public class Configs {
	private Map<String, CustomConfig> sfg = new ConcurrentHashMap<String, CustomConfig>();

	public Configs(JavaPlugin plugin, File file) {
		file.mkdirs();
		File[] list = file.listFiles((dir, name) -> name.endsWith(".yml"));
		for (File f : list) {
			add(plugin, f.getName().replace(".yml", ""), false);
		}
	}

	public CustomConfig get(String name) {
		return sfg.get(name);
	}

	public Map<String, CustomConfig> getConfigs() {
		return sfg;
	}

	public void add(JavaPlugin plugin, String name, boolean isResource) {
		CustomConfig custom = new CustomConfig(plugin, name, isResource);
		sfg.put(name, custom);
	}

	public void remove(String name) {
		if (contains(name)) {
			sfg.get(name).saveCfg();
			sfg.remove(name);
		}
	}

	public boolean contains(String name) {
		return sfg.containsKey(name);
	}

	public void saveAll() {
		for (Entry<String, CustomConfig> s : sfg.entrySet()) {
			sfg.get(s.getKey()).saveCfg();
			sfg.remove(s.getKey());
		}
	}
}
