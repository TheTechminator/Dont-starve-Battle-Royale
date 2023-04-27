package prog1.kotprog.dontstarve.solution.character.actions;

import prog1.kotprog.dontstarve.solution.character.Character;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemType;

/**
 * Az étel elfogyasztása akció leírására szolgáló osztály: egy étel elfogyasztása az inventory-ból.
 */
public class ActionEat extends Action {
    /**
     * Hunger +20.
     */
    private static final int HUNGER_PLUS_20 = 20;

    /**
     * Hunger +12.
     */
    private static final int HUNGER_PLUS_12 = 12;

    /**
     * Hunger +10.
     */
    private static final int HUNGER_PLUS_10 = 10;

    /**
     * HP -5.
     */
    private static final int HP_MINUS_5 = -5;

    /**
     * HP +3.
     */
    private static final int HP_PLUS_3 = 3;

    /**
     * A megenni kívánt étel pozíciója az inventory-ban.
     */
    private final int index;

    /**
     * Az akció létrehozására szolgáló konstruktor.
     *
     * @param index a megenni kívánt étel pozíciója az inventory-ban
     */
    public ActionEat(int index) {
        super(ActionType.EAT);
        this.index = index;
    }

    /**
     * Az index gettere.
     * @return a megenni kívánt étel pozíciója az inventory-ban
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
        ItemType food = character.getInventory().eatItem(index);

        float foodHunger = 0;
        float foodHp = 0;

        if(food != null) {
            switch (food) {
                case RAW_BERRY -> {
                    foodHunger += HUNGER_PLUS_20;
                    foodHp += HP_MINUS_5;
                }
                case RAW_CARROT -> {
                    foodHunger += HUNGER_PLUS_12;
                    foodHp++;
                }
                case COOKED_BERRY -> {
                    foodHunger += HUNGER_PLUS_10;
                    foodHp++;
                }
                case COOKED_CARROT -> {
                    foodHunger += HUNGER_PLUS_10;
                    foodHp += HP_PLUS_3;
                }
            }
        }

        character.eatItem(foodHunger, foodHp);
    }
}
