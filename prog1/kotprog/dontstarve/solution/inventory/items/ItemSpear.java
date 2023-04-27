package prog1.kotprog.dontstarve.solution.inventory.items;

/**
 * A lándzsa item leírására szolgáló osztály.
 */
public class ItemSpear extends EquippableItem {
    /**
     * Konstruktor, amellyel a tárgy létrehozható.
     */
    public ItemSpear() {
        super(ItemType.SPEAR);
    }

    /**
     * Új példány létrehozás az adott itemmből.
     * @return AbstractItem.
     */
    @Override
    public AbstractItem newInstance() {
        ItemSpear i = new ItemSpear();
        i.decreaseAmount(1);
        return i;
    }
}
