package prog1.kotprog.dontstarve.solution.character.actions;

import prog1.kotprog.dontstarve.solution.character.Character;

/**
 * A tárgy levétele akció leírására szolgáló osztály: az aktuálisan kézben lévő item visszarakása az inventory-ba.
 */
public class ActionUnequip extends Action {
    /**
     * Az akció létrehozására szolgáló konstruktor.
     */
    public ActionUnequip() {
        super(ActionType.UNEQUIP);
    }

    /**
     * Az adott action végrehajtása.
     * @param character aki végzi az actiont.
     */
    @Override
    public void doAction(Character character) {
        character.getInventory().unequipItem();
    }
}
