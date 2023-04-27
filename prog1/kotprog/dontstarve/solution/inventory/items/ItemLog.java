package prog1.kotprog.dontstarve.solution.inventory.items;

/**
 * A fa item leírására szolgáló osztály.
 */
public class ItemLog extends AbstractItem {
    /**
     * Konstruktor, amellyel a tárgy létrehozható.
     *
     * @param amount az item mennyisége
     */
    public ItemLog(int amount) {
        super(ItemType.LOG, amount);
    }

    /**
     * Új példány létrehozás az adott itemmből.
     * @return AbstractItem.
     */
    @Override
    public AbstractItem newInstance() {
        return new ItemLog(0);
    }
}
