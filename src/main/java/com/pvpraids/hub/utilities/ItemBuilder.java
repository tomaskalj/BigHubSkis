package com.pvpraids.hub.utilities;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemBuilder {
	private ItemStack item;

	public ItemBuilder(Material material) {
		item = new ItemStack(material);
	}

	public ItemBuilder(ItemStack itemstack) {
		item = itemstack;
	}

	public ItemBuilder type(Material material) {
		item.setType(material);
		return this;
	}

	public ItemBuilder name(String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder amount(int amount) {
		item.setAmount(amount);
		return this;
	}

	public ItemBuilder addLore(String line) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();

		if (lore == null) {
			lore = new ArrayList<>();
		}

		lore.add(ChatColor.translateAlternateColorCodes('&', line));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder addLore(int index, String line) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();

		if (lore == null) {
			lore = new ArrayList<>();
		}

		lore.add(index, ChatColor.translateAlternateColorCodes('&', line));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setLore(List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		List<String> lores = new ArrayList<>();

		for (String l : lore) {
			lores.add(ChatColor.translateAlternateColorCodes('&', l));
		}

		meta.setLore(lores);
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setLore(int index, String line) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();

		if (lore == null) {
			lore = new ArrayList<>();
		}

		lore.set(index, ChatColor.translateAlternateColorCodes('&', line));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder durability(int durability) {
		item.setDurability((short) durability);
		return this;
	}

	public ItemBuilder data(int data) {
		item.setData(new MaterialData(item.getType(), (byte) data));
		return this;
	}

	public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
		item.addUnsafeEnchantment(enchantment, level);
		return this;
	}

	public ItemBuilder addEnchantment(Enchantment enchantment) {
		item.addUnsafeEnchantment(enchantment, 1);
		return this;
	}

	public ItemBuilder addEnchantments(HashMap<Enchantment, Integer> enchantments) {
		for (Enchantment enchantment : enchantments.keySet()) {
			item.addUnsafeEnchantment(enchantment, enchantments.get(enchantment));
		}

		return this;
	}

	public ItemBuilder material(Material material) {
		item.setType(material);
		return this;
	}

	public ItemBuilder clearLore() {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(new ArrayList<>());
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder removeLore(int amount) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();

		if (lore.isEmpty() && lore.size() < amount) {
			return this;
		}

		for (int i = 0; i < amount; i++) {
			if (lore.isEmpty()) {
				break;
			}

			lore.remove(lore.size() - 1);
		}

		meta.setLore(lore);
		item.setItemMeta(meta);

		return this;
	}

	public ItemBuilder clearEnchantments() {
		for (Enchantment e : item.getEnchantments().keySet()) {
			item.removeEnchantment(e);
		}

		return this;
	}

	public ItemBuilder color(Color color) {
		if (item.getType() == Material.LEATHER_HELMET || item.getType() == Material.LEATHER_CHESTPLATE
				|| item.getType() == Material.LEATHER_LEGGINGS || item.getType() == Material.LEATHER_BOOTS) {
			LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
			meta.setColor(color);
			item.setItemMeta(meta);
			return this;
		} else {
			throw new IllegalArgumentException("You can only apply color to leather armor!");
		}
	}


	public ItemBuilder owner(OfflinePlayer offlinePlayer) {
		if (item.getType() == Material.SKULL_ITEM) {
			SkullMeta meta = (SkullMeta) item.getItemMeta();

			if (meta == null) {
				return this;
			}

			meta.setOwner(offlinePlayer.getName());

			item.setItemMeta(meta);

			return this;
		} else {
			throw new IllegalArgumentException("You can only apply ownership to skulls!");
		}
	}
//
//    BookMeta bookMeta = (BookMeta) writtenBook.getItemMeta();
//bookMeta.setTitle(title);
//bookMeta.setAuthor(author);
//bookMeta.setPages(text);
//writtenBook.setItemMeta(bookMeta);

	public ItemBuilder title(String title) {
		if (item.getType() == Material.WRITTEN_BOOK) {
			BookMeta meta = (BookMeta) item.getItemMeta();

			if (meta == null) {
				return this;
			}

			meta.setTitle(ChatColor.translateAlternateColorCodes('&', title));

			return this;
		} else {
			throw new IllegalArgumentException("You can only apply a title to written books!");
		}
	}

	public ItemBuilder author(String author) {
		if (item.getType() == Material.WRITTEN_BOOK) {
			BookMeta meta = (BookMeta) item.getItemMeta();

			if (meta == null) {
				return this;
			}

			meta.setAuthor(ChatColor.translateAlternateColorCodes('&', author));

			return this;
		} else {
			throw new IllegalArgumentException("You can only apply a title to written books!");
		}
	}

	public ItemBuilder setPages(List<String> pages) {
		if (item.getType() == Material.WRITTEN_BOOK) {
			BookMeta meta = (BookMeta) item.getItemMeta();
			List<String> newPages = Lists.newArrayList();

			if (meta == null) {
				return this;
			}

			for (String page : pages) {
				newPages.add(ChatColor.translateAlternateColorCodes('&', page));
			}

			meta.setPages(newPages);

			return this;
		} else {
			throw new IllegalArgumentException("You can only apply a title to written books!");
		}
	}

	public ItemBuilder setPages(String... pages) {
		if (item.getType() == Material.WRITTEN_BOOK) {
			BookMeta meta = (BookMeta) item.getItemMeta();
			List<String> newPages = Lists.newArrayList();

			if (meta == null) {
				return this;
			}

			for (String page : pages) {
				newPages.add(ChatColor.translateAlternateColorCodes('&', page));
			}

			meta.setPages(newPages);

			return this;
		} else {
			throw new IllegalArgumentException("You can only apply a title to written books!");
		}
	}

	public ItemBuilder addPage(String page) {
		if (item.getType() == Material.WRITTEN_BOOK) {
			BookMeta meta = (BookMeta) item.getItemMeta();

			if (meta == null) {
				return this;
			}

			meta.getPages().add(ChatColor.translateAlternateColorCodes('&', page));

			return this;
		} else {
			throw new IllegalArgumentException("You can only apply a title to written books!");
		}
	}

	public ItemBuilder setPage(int index, String page) {
		if (item.getType() == Material.WRITTEN_BOOK) {
			BookMeta meta = (BookMeta) item.getItemMeta();

			if (meta == null) {
				return this;
			}

			meta.getPages().set(index, ChatColor.translateAlternateColorCodes('&', page));

			return this;
		} else {
			throw new IllegalArgumentException("You can only apply a title to written books!");
		}
	}

	public ItemBuilder unbreakable(boolean unbreakable) {
		ItemMeta meta = item.getItemMeta();

		if (unbreakable) {
			meta.spigot().setUnbreakable(true);

			item.setItemMeta(meta);

			return this;
		} else {
			meta.spigot().setUnbreakable(false);

			item.setItemMeta(meta);

			return this;
		}
	}

	public ItemBuilder hideFlags() {
		ItemMeta meta = item.getItemMeta();

		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
		meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

		item.setItemMeta(meta);

		return this;
	}

	//Not supported in this spigot version.

//    public ItemBuilder addGlow() {
//        item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
//        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
//        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
//        tag.set("HideFlags", new NBTTagInt(1));
//        nmsStack.setTag(tag);
//        item = CraftItemStack.asCraftMirror(nmsStack);
//
//        return this;
//    }
//
//    public ItemBuilder hideGlow() {
//        item.removeEnchantment(Enchantment.ARROW_DAMAGE);
//        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
//        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
//        tag.set("HideFlags", new NBTTagInt(0));
//        nmsStack.setTag(tag);
//        item = CraftItemStack.asCraftMirror(nmsStack);
//
//        return this;
//    }

//    public ItemBuilder glow(boolean glow) {
//        return (glow ? addGlow() : hideGlow());
//    }

	public ItemStack build() {
		return item;
	}

	/**
	 * WARNING: Compares based on item type and display name ONLY!
	 *
	 * @param is1
	 * @param is2
	 * @return
	 */
	public static boolean areItemsEqual(ItemStack is1, ItemStack is2) {
		ItemMeta meta1 = is1.getItemMeta();
		ItemMeta meta2 = is2.getItemMeta();

		if (meta1 == null && meta2 == null) {
			return true;
		} else if (meta1 == null || meta2 == null) {
			return false;
		} else {
			return meta1.equals(meta2);
		}
	}
}
