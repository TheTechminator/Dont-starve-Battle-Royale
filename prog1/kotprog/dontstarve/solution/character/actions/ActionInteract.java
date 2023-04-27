package prog1.kotprog.dontstarve.solution.character.actions;

import prog1.kotprog.dontstarve.solution.GameManager;
import prog1.kotprog.dontstarve.solution.character.Character;
import prog1.kotprog.dontstarve.solution.inventory.items.AbstractItem;
import prog1.kotprog.dontstarve.solution.level.Field;
import prog1.kotprog.dontstarve.solution.level.GameMap;
import prog1.kotprog.dontstarve.solution.utility.Position;

/**
 * Az aktuális mezőn lévő tereptárggyal való interakcióba lépés (favágás, kőcsákányozás, gally / bogyó / répa leszedése)
 * leírására szolgáló osztály.
 */
public class ActionInteract extends Action {
    /**
     * Az akció létrehozására szolgáló konstruktor.
     */
    public ActionInteract() {
        super(ActionType.INTERACT);
    }

    /**
     * Az adott action végrehajtása.
     * @param character aki végzi az actiont.
     */
    @Override
    public void doAction(Character character) {
        GameMap map = GameManager.getInstance().getMap();
        Position whole = character.getCurrentPosition().getNearestWholePosition();
        Field field = map.getField((int)whole.getX(), (int)whole.getY());

        AbstractItem item = field.extractTerrainObject(character);

        if(item != null) {
            character.getInventory().addItem(item);
        }
    }
}
