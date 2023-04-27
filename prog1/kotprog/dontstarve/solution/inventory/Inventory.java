package prog1.kotprog.dontstarve.solution.inventory;

import prog1.kotprog.dontstarve.solution.inventory.items.AbstractItem;
import prog1.kotprog.dontstarve.solution.inventory.items.EquippableItem;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A karakterhez tartozó inventory.
 */
public class Inventory implements BaseInventory {
    /**
     * Az inventory mérete.
     */
    private static final int INVENTORY_SIZE = 10;

    /**
     * Az invetory slotjait jelképező tömb.
     */
    private AbstractItem[] slots = new AbstractItem[INVENTORY_SIZE];

    /**
     * A karakter keze.
     */
    private EquippableItem hand;

    /**
     * Egy ideiglenes kéz helyettesítő.
     */
    private EquippableItem tmp;

    /**
     * Egy item hozzáadása.
     * @param item a hozzáadni kívánt tárgy
     * @return igaz ha sikerült hammis ha nem
     */
    @Override
    public boolean addItem(AbstractItem item) {
        if(item == null || Arrays.stream(slots).filter(slot -> slot != null && slot.addItem(item)).findFirst().orElse(null) != null) {
            return true;
        }

        if(item.getType() == ItemType.FIRE) {
            return false;
        }

        for(int i = 0; i < INVENTORY_SIZE; i++) {
            if (slots[i] != null) {
                continue;
            }

            slots[i] = item.newInstance();
            if(slots[i].addItem(item)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Item kidobása az inventory ból.
     * @param index a slot indexe, amelyen lévő itemet szeretnénk eldobni
     * @return a kidobott item
     */
    @Override
    public AbstractItem dropItem(int index) {
        if(!slotIndexValid(index)) {
            return null;
        }

        AbstractItem drop = slots[index];
        slots[index] = null;

        return drop;
    }

    /**
     * Egy item törlése az inentoryból.
     * @param type a törlendő item típusa
     * @param amount a törlendő item mennyisége
     * @return igaz ha sikerült a törlés
     */
    @Override
    public boolean removeItem(ItemType type, int amount) {
        if(!hasEnoughItem(type, amount)) {
            return false;
        }

        for(int i = 0; i < INVENTORY_SIZE; i++) {
            if(slots[i] == null || slots[i].getType() != type) {
                continue;
            }

            if(slots[i].getAmount() <= amount) {
                amount -= slots[i].getAmount();
                slots[i] = null;
            } else {
                slots[i].decreaseAmount(amount);
                return true;
            }
        }

        return true;
    }

    /**
     * Megnézi hogy a megadott index benne van e a range ben.
     * @param index a slot indexe
     * @return igaz ha benne hamis ha nem
     */
    private boolean slotIndexValid (int index) {
        return (index >= 0 && index < INVENTORY_SIZE);
    }

    /**
     * Két item megcserélése.
     * @param index1 a slot indexe, amelyen az első item található
     * @param index2 a slot indexe, amelyen a második item található
     * @return igaz ha sikerült
     */
    @Override
    public boolean swapItems(int index1, int index2) {
        if(!slotIndexValid(index1) || !slotIndexValid(index2)  || slots[index1] == null || slots[index2] == null) {
            return false;
        }

        AbstractItem tmp = slots[index1];
        slots[index1] = slots[index2];
        slots[index2] = tmp;

        return true;
    }

    /**
     * Egy item átmozgatása egy adott helyről egy másikra.
     * @param index a mozgatni kívánt item pozíciója az inventory-ban
     * @param newIndex az új pozíció, ahova mozgatni szeretnénk az itemet
     * @return igaz hga sikerült
     */
    @Override
    public boolean moveItem(int index, int newIndex) {
        if(!slotIndexValid(index) || !slotIndexValid(newIndex)  || slots[index] == null || slots[newIndex] != null) {
            return false;
        }

        slots[newIndex] = slots[index];
        slots[index] = null;

        return true;
    }

    /**
     * Két külön sloton lévő item kombinálása.
     * @param index1 első item pozíciója az inventory-ban
     * @param index2 második item pozíciója az inventory-ban
     * @return igaz ha sikerült
     */
    @Override
    public boolean combineItems(int index1, int index2) {
        if(!slotIndexValid(index1) || !slotIndexValid(index2) || slots[index1] == null || slots[index2] == null
                || slots[index1].getType() != slots[index2].getType() || slots[index1].getAvailableSpace() == 0) {
            return false;
        }

        slots[index1].addItem(slots[index2]);

        if(slots[index2].getAmount() == 0) {
            slots[index2] = null;
        }

        return true;
    }

    /**
     * Egy item megfogása.
     * @param index a kézbe venni kívánt tárgy pozíciója az inventory-ban
     * @return igaz ha sikerült
     */
    @Override
    public boolean equipItem(int index) {
        if(!slotIndexValid(index) || !(slots[index] instanceof EquippableItem)) {
            return false;
        }

        tmp = hand;
        hand = (EquippableItem)(slots[index]);
        slots[index] = tmp;

        return true;
    }

    /**
     * Egy item elrakása az inventoryba.
     * @return null ha el tüdtul rakni
     */
    @Override
    public EquippableItem unequipItem() {
        if(addItem(hand)) {
            hand = null;
        }

        tmp = hand;
        hand = null;
        return tmp;
    }

    /**
     * Egy item megfőzése.
     * @param index A megfőzni kívánt item pozíciója az inventory-ban
     * @return a megfőzött item típusa
     */
    @Override
    public ItemType cookItem(int index) {
        if(!slotIndexValid(index) || slots[index] == null) {
            return null;
        }

        ItemType type = slots[index].getType();

        if(type == ItemType.RAW_CARROT || type == ItemType.RAW_BERRY) {
            slots[index].decreaseAmount(1);

            if(slots[index].getAmount() == 0) {
                slots[index] = null;
            }

            return type;
        }

        return null;
    }

    /**
     * Egy item megevése.
     * @param index A megenni kívánt item pozíciója az inventory-ban
     * @return a megenni kívánt item típusa
     */
    @Override
    public ItemType eatItem(int index) {
        if(!slotIndexValid(index) || slots[index] == null) {
            return null;
        }

        ItemType type = slots[index].eatItem();
        if(type == null) {
            return null;
        }

        if(slots[index].getAmount() == 0) {
            slots[index] = null;
        }

        return type;
    }

    /**
     * Üres helyek száma az inventoryban.
     * @return üres helyek
     */
    @Override
    public int emptySlots() {
        return Arrays.stream(slots).mapToInt(item -> item == null ? 1 : 0).sum();
    }

    /**
     * A kézben levő item.
     * @return - item
     */
    @Override
    public EquippableItem equippedItem() {
        return hand;
    }

    /**
     * Egy megadott indexen levő item lekérése.
     * @param index a lekérdezni kívánt pozíció
     * @return item
     */
    @Override
    public AbstractItem getItem(int index) {
        if(slotIndexValid(index)) {
            return slots[index];
        }

        return null;
    }

    /**
     * Van-e elegendő a megadott típusú itemből.
     * @param type item típusa
     * @param amount mennyiség
     * @return igaz ha van elég
     */
    public boolean hasEnoughItem (ItemType type, int amount) {
        return amount - itemAmount(type) <= 0;
    }

    /**
     * Visszaadja az összes inventoryban levő itemet beleértve a kézt is.
     * @return itemek
     */
    public List<AbstractItem> getItems () {
        List<AbstractItem> items = new ArrayList<>();

        if(hand != null) {
            items.add(hand);
        }

        for(int i = 0; i < INVENTORY_SIZE; i++) {
            if(slots[i] != null) {
                items.add(slots[i]);
            }
        }

        return items;
    }

    /**
     * Debugoláshoz kell.
     * @return String.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Inventory: ");

        for(int i = 0; i < INVENTORY_SIZE; i++) {
            if (slots[i] == null) {
                stringBuilder.append("Empty, ");
                continue;
            }

            stringBuilder.append(slots[i].getType());
            stringBuilder.append(" (");
            stringBuilder.append(slots[i].getAmount());
            stringBuilder.append("), ");
        }

        stringBuilder.append("\nHand: ");
        stringBuilder.append(hand == null ? "Empty" : hand);

        return stringBuilder.toString();
    }

    /**
     * A karakter kezében levő tárgy törlése mivel el van már használódva.
     */
    public void clearHand () {
        hand = null;
    }

    /**
     * Egy adott típusú itemből mennyi van az inventoryban.
     * @param type keresett típus.
     * @return item amount.
     */
    public int itemAmount (ItemType type) {
        int amount = 0;

        for(AbstractItem item : slots) {
            if(item != null && item.getType() == type) {
                amount += item.getAmount();
            }
        }

        return amount;
    }
}
