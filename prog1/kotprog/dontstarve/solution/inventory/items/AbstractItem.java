package prog1.kotprog.dontstarve.solution.inventory.items;

/**
 * Egy általános itemet leíró osztály.
 */
public abstract class AbstractItem {
    /**
     * Az item típusa.
     * @see ItemType
     */
    private final ItemType type;

    /**
     * Az item mennyisége.
     */
    private int amount;

    /**
     * Konstruktor, amellyel a tárgy létrehozható.
     * @param type az item típusa
     * @param amount az item mennyisége
     */
    public AbstractItem(ItemType type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    /**
     * Egy item hozzáadása az adott item hez. Ha a típus megyegyezik és fér még elteszünk belőle.
     * @param item amit hozzá adunk.
     * @return true ha sikeresen a teljes item hozzáadódott.
     */
    public boolean addItem (AbstractItem item) {
        int space = getAvailableSpace();

        if(this.type != item.type || space == 0) {
            return false;
        }

        if(item.getAmount() > space) {
            amount += space;
            item.decreaseAmount(space);
            return false;
        }

        amount += item.getAmount();
        item.decreaseAmount(item.getAmount());
        return true;
    }

    /**
     * A maximális stack mérethez képes mennyi hely van még.
     * @return int elérhető hely.
     */
    public int getAvailableSpace () {
        return stackSize() - amount;
    }

    /**
     * Az egyes típusokhoz tartozó maximális stack méret.
     * @return int stack size.
     */
    private int stackSize() {
        return switch (type) {
            case AXE, PICKAXE, SPEAR, TORCH -> 1;
            case LOG -> 15;
            case TWIG -> 20;
            case STONE, RAW_BERRY, RAW_CARROT, COOKED_BERRY, COOKED_CARROT -> 10;
            default -> 0;
        };
    }

    /**
     * A type gettere.
     * @return a tárgy típusa
     */
    public ItemType getType() {
        return type;
    }

    /**
     * Az amount gettere.
     * @return a tárgy mennyisége
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Az adott item amountjának csökkentése a megadott mennyiséggel.
     * @param qunatity ennyivel fog csökenni.
     */
    public void decreaseAmount(int qunatity) {
        amount -= qunatity;
    }

    /**
     * Egy item elfogyasztása.
     * @return az elfogyasztott item típusa vagy null.
     */
    public ItemType eatItem() {
        if(getType() == ItemType.RAW_BERRY || getType() == ItemType.RAW_CARROT || getType() == ItemType.COOKED_CARROT || getType() == ItemType.COOKED_BERRY) {
            amount -= 1;
            return type;
        }

        return null;
    }

    /**
     * Új példány létrehozás az adott itemmből.
     * @return AbstractItem.
     */
    public abstract AbstractItem newInstance ();
}
