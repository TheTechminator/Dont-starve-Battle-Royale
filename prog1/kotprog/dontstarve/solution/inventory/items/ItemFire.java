package prog1.kotprog.dontstarve.solution.inventory.items;

/**
 * A fire item leírására szolgáló osztály.
 */
public class ItemFire extends AbstractItem {
    /**
     * Konstruktor, amellyel a tárgy létrehozható.
     */
    public ItemFire() {
        super(ItemType.FIRE, 1);
    }

    /**
     * Új példány létrehozás az adott itemmből.
     * @return AbstractItem.
     */
    @Override
    public AbstractItem newInstance() {
        ItemFire i = new ItemFire();
        i.decreaseAmount(1);
        return i;
    }
}
