package prog1.kotprog.dontstarve.solution.inventory.items;

/**
 * A kő item leírására szolgáló osztály.
 */
public class ItemStone extends AbstractItem {
    /**
     * Konstruktor, amellyel a tárgy létrehozható.
     *
     * @param amount az item mennyisége
     */
    public ItemStone(int amount) {
        super(ItemType.STONE, amount);
    }

    /**
     * Új példány létrehozás az adott itemmből.
     * @return AbstractItem.
     */
    @Override
    public AbstractItem newInstance() {
        return new ItemStone(0);
    }
}
