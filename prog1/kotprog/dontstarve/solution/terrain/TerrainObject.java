package prog1.kotprog.dontstarve.solution.terrain;

import prog1.kotprog.dontstarve.solution.character.Character;
import prog1.kotprog.dontstarve.solution.inventory.items.AbstractItem;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemLog;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemStone;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemTwig;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemRawBerry;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemRawCarrot;
import prog1.kotprog.dontstarve.solution.inventory.items.EquippableItem;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemType;

/**
 * Egy terrain object leírására szolgáló osztály.
 */
public class TerrainObject {
    /**
     * A tereptárgy típusa.
     */
    private TerrainObjectType type;

    /**
     * A tereptárgy kibányászásával kapható nyersanyag.
     */
    private AbstractItem item;

    /**
     * A bányászáshoz szükséges idő.
     */
    private int requiredTime = -1;

    /**
     * A TerrainObject publikus konstruktora.
     * @param type a tereptárgy típusa.
     */
    public TerrainObject(TerrainObjectType type) {
        this.type = type;
        setUpItems();
    }

    /**
     * A metódus segítségével a megadott terrain objecthez le tudjuk generálni az itemeket amit a terrain object kibányászásával
     * fog kapni a karakter.
     */
    private void setUpItems () {
        switch (type) {
            case TREE -> {
                requiredTime = 4;
                item = new ItemLog(2);
            }
            case STONE -> {
                requiredTime = 5;
                item = new ItemStone(3);
            }
            case TWIG -> {
                requiredTime = 2;
                item = new ItemTwig(1);
            }
            case BERRY -> {
                requiredTime = 1;
                item = new ItemRawBerry(1);
            }
            case CARROT -> {
                requiredTime = 1;
                item = new ItemRawCarrot(1);
            }
        }
    }

    /**
     * Visszaadja a típust.
     * @return type
     */
    public TerrainObjectType getType() {
        return type;
    }

    /**
     * A bányászást végrehatjó metódus.
     * @param character a karakterünk
     * @return igaz ha sikeres volt a bányászás, hamis különben
     */
    public boolean extraction (Character character) {
        EquippableItem item = character.getInventory().equippedItem();

        if(neededTool() == null) {
            requiredTime--;
        } else if(item != null && neededTool() == item.getType()) {
            requiredTime--;
            character.useEquippedItem(false);
        }

        if(requiredTime == 0) {
            requiredTime--;
            type = TerrainObjectType.EMPTY;
            return true;
        }

        return false;
    }

    /**
     * A bányászáshoz szükséges eszköz.
     * @return ItemType.
     */
    public ItemType neededTool () {
        return switch (getType()) {
            case TREE -> ItemType.AXE;
            case STONE -> ItemType.PICKAXE;
            default -> null;
        };
    }

    /**
     * Visszaadja az item et amit kapunk ha kibányásztuk.
     * @return item
     */
    public AbstractItem getItem () {
        return item;
    }

    /**
     * Megmondja hogy kibányászás után az inventoryba kerül-e.
     * @return igaz ha az inventoryba kerül, hamis különben
     */
    public boolean isToInventory () {
        return !(getItem().getType() == ItemType.LOG || getItem().getType() == ItemType.STONE);
    }
}
