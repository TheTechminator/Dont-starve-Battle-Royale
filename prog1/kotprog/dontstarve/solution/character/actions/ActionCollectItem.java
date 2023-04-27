package prog1.kotprog.dontstarve.solution.character.actions;

import prog1.kotprog.dontstarve.solution.GameManager;
import prog1.kotprog.dontstarve.solution.character.Character;
import prog1.kotprog.dontstarve.solution.inventory.items.AbstractItem;
import prog1.kotprog.dontstarve.solution.level.GameMap;
import prog1.kotprog.dontstarve.solution.utility.Position;

/**
 * Az item begyűjtése akció leírására szolgáló osztály: egy item begyűjtése az aktuális mezőről.
 */
public class ActionCollectItem extends Action {
    /**
     * Az akció létrehozására szolgáló konstruktor.
     */
    public ActionCollectItem() {
        super(ActionType.COLLECT_ITEM);
    }

    /**
     * Az adott action végrehajtása.
     * @param character aki végzi az actiont.
     */
    @Override
    public void doAction(Character character) {
        GameMap map = GameManager.getInstance().getMap();
        Position whole = character.getCurrentPosition().getNearestWholePosition();

        AbstractItem[] items = map.getField((int)whole.getX(), (int)whole.getY()).items();
        AbstractItem tmp;

        if(items != null && items.length > 0) {
            tmp = items[0];
            if(!character.getInventory().addItem(tmp)) {
                map.getField((int)whole.getX(), (int)whole.getY()).removeItem(tmp);
                map.getField((int)whole.getX(), (int)whole.getY()).addItem(tmp);
            } else {
                map.getField((int)whole.getX(), (int)whole.getY()).removeItem(tmp);
            }
        }

    }
}
