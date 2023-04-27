package prog1.kotprog.dontstarve.solution.ai;

import prog1.kotprog.dontstarve.solution.inventory.Inventory;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemType;

/**
 * Az inventory kezelését segítő osztály.
 */
public class InventoryManager {
    /**
     * Anyagszükséglet 1.
     */
    private static final int MATERIAL_AMOUNT_1 = 1;

    /**
     * Anyagszükséglet 2.
     */
    private static final int MATERIAL_AMOUNT_2 = 2;

    /**
     * Anyagszükséglet 3.
     */
    private static final int MATERIAL_AMOUNT_3 = 3;

    /**
     * Anyagszükséglet 4.
     */
    private static final int MATERIAL_AMOUNT_4 = 4;

    /**
     * A játékos inventoryja.
     */
    private final Inventory inventory;

    /**
     * Inventory manager publikuc konstructora.
     * @param inventory a managed inventory.
     */
    public InventoryManager (Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * A paraméterben érkező ételt keresi meg az inventoryban.
     * @param foodType az étel típusa.
     * @return az az inventory index ahhol megtalálható vagy -1.
     */
    public int getFoodIndex (ItemType foodType) {
        return getItemIndex(foodType);
    }

    /**
     * A paraméterben érkező típusú tárgyat keresi meg az inventoryban.
     * @param type a tárgyat típusa.
     * @return az az inventory index ahhol megtalálható vagy -1.
     */
    public int getItemIndex (ItemType type) {
        for(int i = 0; i < 10; i++) {
            if(inventory.getItem(i) != null && inventory.getItem(i).getType() == type) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Megmondja van e elegendő nyersanyag a kraftoláshoz.
     * @param item az eszköz típusa amit kraftolnánk.
     * @return true ha van elég, false egyébként.
     */
    public boolean hasEnoughMaterialToCraft (ItemType item) {
        return switch (item) {
            case AXE -> inventory.hasEnoughItem(ItemType.TWIG, MATERIAL_AMOUNT_3);
            case PICKAXE -> inventory.hasEnoughItem(ItemType.TWIG, MATERIAL_AMOUNT_2) && inventory.hasEnoughItem(ItemType.LOG, MATERIAL_AMOUNT_2);
            case SPEAR -> inventory.hasEnoughItem(ItemType.LOG, MATERIAL_AMOUNT_2) && inventory.hasEnoughItem(ItemType.STONE, MATERIAL_AMOUNT_2);
            case TORCH -> inventory.hasEnoughItem(ItemType.LOG, MATERIAL_AMOUNT_1) && inventory.hasEnoughItem(ItemType.TWIG, MATERIAL_AMOUNT_3);
            case FIRE -> inventory.hasEnoughItem(ItemType.TWIG, MATERIAL_AMOUNT_2) && inventory.hasEnoughItem(ItemType.LOG, MATERIAL_AMOUNT_2)
                        && inventory.hasEnoughItem(ItemType.STONE, MATERIAL_AMOUNT_4);
            default -> false;
        };
    }
}
