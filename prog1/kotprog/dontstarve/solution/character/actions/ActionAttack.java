package prog1.kotprog.dontstarve.solution.character.actions;

import prog1.kotprog.dontstarve.solution.GameManager;
import prog1.kotprog.dontstarve.solution.character.Character;

/**
 * A támadás akció leírására szolgáló osztály: a legközelebbi karakter megtámadása.
 */
public class ActionAttack extends Action {
    /**
     * Támadási erő 4.
     */
    private static final int ATTACKS_STRENGTH_4 = 4;

    /**
     * Az akció létrehozására szolgáló konstruktor.
     */
    public ActionAttack() {
        super(ActionType.ATTACK);
    }

    /**
     * Az adott action végrehajtása.
     * @param character aki végzi az actiont.
     */
    @Override
    public void doAction(Character character) {
        int attack = ATTACKS_STRENGTH_4;
        if(character.getInventory().equippedItem() != null) {
            attack = character.getInventory().equippedItem().getAttackStrength();
        }
        Character other = GameManager.getInstance().getPositionGenerator().nearestCharacterTo(character, 2);

        if(other != null) {
            character.useEquippedItem(false);
            other.getHurt(attack);
        }
    }
}
