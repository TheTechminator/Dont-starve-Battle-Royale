package prog1.kotprog.dontstarve.solution.character.actions;

import prog1.kotprog.dontstarve.solution.GameManager;
import prog1.kotprog.dontstarve.solution.character.Character;
import prog1.kotprog.dontstarve.solution.inventory.Inventory;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemAxe;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemPickaxe;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemSpear;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemType;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemTorch;
import prog1.kotprog.dontstarve.solution.level.Field;
import prog1.kotprog.dontstarve.solution.utility.Position;

/**
 * A kraftolás akció leírására szolgáló osztály: adott típusú item kraftolása.
 */
public class ActionCraft extends Action {
    private static final int MATERIAL_AMOUNT_1 = 1;
    private static final int MATERIAL_AMOUNT_2 = 2;
    private static final int MATERIAL_AMOUNT_3 = 3;
    private static final int MATERIAL_AMOUNT_4 = 4;
    /**
     * A lekraftolandó item típusa.
     */
    private final ItemType itemType;

    /**
     * Az akció létrehozására szolgáló konstruktor.
     *
     * @param itemType a lekraftolandó item típusa
     */
    public ActionCraft(ItemType itemType) {
        super(ActionType.CRAFT);
        this.itemType = itemType;
    }

    /**
     * Az itemType gettere.
     * @return a lekraftolandó item típusa
     */
    public ItemType getItemType() {
        return itemType;
    }

    /**
     * Az adott action végrehajtása.
     * @param character aki végzi az actiont.
     */
    @Override
    public void doAction(Character character) {
        Inventory inv = (Inventory) character.getInventory();
        Position whole = character.getCurrentPosition().getNearestWholePosition();
        Field field = GameManager.getInstance().getMap().getField((int)whole.getX(), (int)whole.getY());

        if (itemType == null) {
            return;
        }

        switch (itemType) {
            case AXE -> {
                if(inv.hasEnoughItem(ItemType.TWIG, MATERIAL_AMOUNT_3)) {
                    inv.removeItem(ItemType.TWIG, MATERIAL_AMOUNT_3);
                    inv.addItem(new ItemAxe());
                }
            }

            case PICKAXE -> {
                if(inv.hasEnoughItem(ItemType.TWIG, MATERIAL_AMOUNT_2) && inv.hasEnoughItem(ItemType.LOG, MATERIAL_AMOUNT_2)) {
                    inv.removeItem(ItemType.TWIG, MATERIAL_AMOUNT_2);
                    inv.removeItem(ItemType.LOG, MATERIAL_AMOUNT_2);
                    inv.addItem(new ItemPickaxe());
                }
            }

            case SPEAR -> {
                if(inv.hasEnoughItem(ItemType.LOG, MATERIAL_AMOUNT_2) && inv.hasEnoughItem(ItemType.STONE, MATERIAL_AMOUNT_2)) {
                    inv.removeItem(ItemType.LOG, MATERIAL_AMOUNT_2);
                    inv.removeItem(ItemType.STONE, MATERIAL_AMOUNT_2);
                    inv.addItem(new ItemSpear());
                }
            }

            case TORCH -> {
                if(inv.hasEnoughItem(ItemType.LOG, MATERIAL_AMOUNT_1) && inv.hasEnoughItem(ItemType.TWIG, MATERIAL_AMOUNT_3)) {
                    inv.removeItem(ItemType.LOG, MATERIAL_AMOUNT_1);
                    inv.removeItem(ItemType.TWIG, MATERIAL_AMOUNT_3);
                    inv.addItem(new ItemTorch());
                }
            }

            case FIRE -> {
                if(inv.hasEnoughItem(ItemType.TWIG, MATERIAL_AMOUNT_2) && inv.hasEnoughItem(ItemType.LOG, MATERIAL_AMOUNT_2)
                        && inv.hasEnoughItem(ItemType.STONE, MATERIAL_AMOUNT_4) && field.isEmpty()) {

                    inv.removeItem(ItemType.TWIG, MATERIAL_AMOUNT_2);
                    inv.removeItem(ItemType.LOG, MATERIAL_AMOUNT_2);
                    inv.removeItem(ItemType.STONE, MATERIAL_AMOUNT_4);

                    field.putFire();
                }
            }
        }
    }
}
