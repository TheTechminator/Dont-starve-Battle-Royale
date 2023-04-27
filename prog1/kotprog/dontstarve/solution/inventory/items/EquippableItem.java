package prog1.kotprog.dontstarve.solution.inventory.items;

/**
 * Felvehető / kézbe vehető item leírására szolgáló osztály.
 */
public abstract class EquippableItem extends AbstractItem {
    /**
     * Támadási erő 19.
     */
    private static final int ATTACKS_STRENGTH_19 = 19;

    /**
     * Támadási erő 8.
     */
    private static final int ATTACKS_STRENGTH_8 = 8;

    /**
     * Támadási erő 6.
     */
    private static final int ATTACKS_STRENGTH_6 = 6;

    /**
     * Használható 40-szer.
     */
    private static final int USABLE_40 = 40;

    /**
     * Használható 30-szer.
     */
    private static final int USABLE_30 = 30;

    /**
     * Használható 20-szer.
     */
    private static final int USABLE_20 = 20;

    /**
     * Használható 10-szer.
     */
    private static final int USABLE_10 = 10;

    /**
     * Az eszköz állapota.
     */
    private float state;

    /**
     * Konstruktor, amellyel a tárgy létrehozható.
     * @param type az item típusa
     */
    public EquippableItem(ItemType type) {
        super(type, 1);
        state = getWorkingTime(type);
    }

    /**
     * Az eszköz használatának ideje.
     * @param type ezköz típusa.
     * @return ennyi használatot bír ki az adott eszköz.
     */
    private float getWorkingTime (ItemType type) {
        if(type == null) {
            return 0;
        }

        return switch (type) {
            case AXE -> USABLE_40;
            case PICKAXE -> USABLE_30;
            case SPEAR -> USABLE_10;
            case TORCH -> USABLE_20;
            default -> 0;
        };
    }

    /**
     * A elhasználódás idő függő-e.
     * @return true ha időtöl függ.
     */
    public boolean isTimeUsed () {
        return getType() == ItemType.TORCH;
    }

    /**
     * Az eszköz használata. Csökkenti az állapotot.
     * @return true ha még használható.
     */
    public boolean use() {
        if(percentage() > 0) {
            state--;
            return true;
        }

        return false;
    }

    /**
     * Megadja, hogy milyen állapotban van a tárgy.
     * @return a tárgy használatlansága, %-ban (100%: tökéletes állapot)
     */
    public float percentage() {
        return (state / getWorkingTime(getType())) * 100;
    }

    /**
     * Az adott eszköz sebzése.
     * @return a sebzés mértéke.
     */
    public int getAttackStrength () {
        return switch (getType()) {
            case SPEAR -> ATTACKS_STRENGTH_19;
            case AXE, PICKAXE -> ATTACKS_STRENGTH_8;
            case TORCH -> ATTACKS_STRENGTH_6;
            default -> 0;
        };
    }

    /**
     * Debugoláshoz kell.
     * @return String.
     */
    @Override
    public String toString() {
        return getType() + "\nState: " + percentage() + "%";
    }
}
