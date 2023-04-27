package prog1.kotprog.dontstarve.solution.character.actions;

import prog1.kotprog.dontstarve.solution.GameManager;
import prog1.kotprog.dontstarve.solution.character.Character;
import prog1.kotprog.dontstarve.solution.inventory.items.AbstractItem;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemCookedBerry;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemCookedCarrot;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemType;
import prog1.kotprog.dontstarve.solution.level.Field;
import prog1.kotprog.dontstarve.solution.utility.Position;

/**
 * A főzés akció leírására szolgáló osztály: egy item megfőzése.
 */
public class ActionCook extends Action {
    /**
     * A megfőzni kívánt tárgy pozíciója az inventory-ban.
     */
    private final int index;

    /**
     * Az akció létrehozására szolgáló konstruktor.
     *
     * @param index a megfőzni kívánt tárgy pozíciója az inventory-ban
     */
    public ActionCook(int index) {
        super(ActionType.COOK);
        this.index = index;
    }

    /**
     * Az index gettere.
     * @return a megfőzni kívánt tárgy pozíciója az inventory-ban
     */
    public int getIndex() {
        return index;
    }

    /**
     * Az adott action végrehajtása.
     * @param character aki végzi az actiont.
     */
    @Override
    public void doAction(Character character) {
        Position whole = character.getCurrentPosition().getNearestWholePosition();
        Field field = GameManager.getInstance().getMap().getField((int)whole.getX(), (int)whole.getY());

        if(field.hasFire()) {
            AbstractItem cooked = null;
            ItemType cookedType = character.getInventory().cookItem(getIndex());

            if (cookedType == ItemType.RAW_BERRY) {
                cooked = new ItemCookedBerry(1);
            }else if (cookedType == ItemType.RAW_CARROT) {
                cooked = new ItemCookedCarrot(1);
            }

            if (!character.getInventory().addItem(cooked)) {
                field.addItem(cooked);
            }
        }
    }
}
