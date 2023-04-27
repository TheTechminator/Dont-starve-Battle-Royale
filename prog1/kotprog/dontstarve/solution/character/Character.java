package prog1.kotprog.dontstarve.solution.character;

import prog1.kotprog.dontstarve.solution.character.actions.Action;
import prog1.kotprog.dontstarve.solution.inventory.BaseInventory;
import prog1.kotprog.dontstarve.solution.inventory.Inventory;
import prog1.kotprog.dontstarve.solution.inventory.items.EquippableItem;
import prog1.kotprog.dontstarve.solution.utility.Position;
import java.util.ArrayList;
import java.util.List;

/**
 * Egy karakter leíró osztály beleérve AI is csak annak vannak plusz dolgai.
 */
public class Character implements BaseCharacter {
    /**
     * A maximális hp és hunger érték.
     */
    private static final int MAX_HP_HUNGER_COUNT = 100;

    /**
     * Megadja az egyes hp szinteket ahhol változik a sebesség.
     */
    private static final float[] HP_THRESHOLDS = {50, 30, 10};

    /**
     * A HP_THRESHOLDS hoz társítva megadja az egyes hp szintekhez tartozó szorzót.
     */
    private static final float[] HP_SPEED_MULTIPLIERS = {1, 0.9f, 0.75f, 0.6f};

    /**
     * Megadja az egyes hunger szinteket ahhol változik a sebesség.
     */
    private static final float[] HUNGER_THRESHOLDS = {50, 20, 0};

    /**
     * A HUNGER_THRESHOLDS hoz társítva megadja az egyes HUNGER szintekhez tartozó szorzót.
     */
    private static final float[] HUNGER_SPEED_MULTIPLIERS = {1, 0.9f, 0.8f, 0.5f};

    /**
     * A karakter neve.
     */
    private final String name;

    /**
     * A karakter éhség szintje.
     */
    private float hunger;

    /**
     * A karakter hp-ja.
     */
    private float hp;

    /**
     * A karakter inventoryja.
     */
    private final Inventory inventory;

    /**
     * A karakter pozíciója.
     */
    private Position position;

    /**
     * Meg támadta-e valaki a karaktert.
     */
    private boolean attacked;

    /**
     * A karakter végrehajtott cselekedetei.
     */
    private final List<Action> actions = new ArrayList<>();

    /**
     * A karakter nyilvános konstruktora.
     * @param name a karakter neve
     */
    public Character (String name) {
        this.name = name;

        inventory = new Inventory();

        hp = MAX_HP_HUNGER_COUNT;
        hunger = MAX_HP_HUNGER_COUNT;
        attacked = false;
    }

    /**
     * A karakter aktuális sebességét kiszámító metódus.
     * @return a karakter sebessége
     */
    @Override
    public float getSpeed() {
        float hpSpeed = calculateSpeedMultiplier(hp, HP_THRESHOLDS, HP_SPEED_MULTIPLIERS);
        float hungerSpeeed = calculateSpeedMultiplier(hunger, HUNGER_THRESHOLDS, HUNGER_SPEED_MULTIPLIERS);
        return hpSpeed * hungerSpeeed;
    }

    /**
     * A megadott adatok alapján kiszámítja a sebesség szorzóját.
     * Megadunk thresholds okat és amelyiknél nagyobb a value az ahhoz tartozó multiplier-t kapjuk vissza.
     * @param value a hp vagy a hunger vagy bármi egyéb aktuális
     * @param thresholds a sebességet befolyásoló tényezők értéke (pl. [50, 30])
     * @param multipliers a thresholds hoz tartozó értékek (pl. [1, 0.8, 0.6])
     * @return a kiszámított érték
     */
    private float calculateSpeedMultiplier(float value, float[] thresholds, float[] multipliers) {
        for (int i = 0; i < thresholds.length; i++) {
            if (value >= thresholds[i]) {
                return multipliers[i];
            }
        }

        return multipliers[multipliers.length - 1];
    }

    /**
     * Az éhség gettere.
     * @return hunger
     */
    @Override
    public float getHunger() {
        return hunger;
    }

    /**
     * A hp gettere.
     * @return hp
     */
    @Override
    public float getHp() {
        return hp;
    }

    /**
     * Él-e még a karakter.
     * @return igaz ha él még
     */
    public boolean isAlive () {
        return getHp() > 0;
    }

    /**
     * Az inventory gettere.
     * @return inventory
     */
    @Override
    public BaseInventory getInventory() {
        return inventory;
    }

    /**
     * A position gettere.
     * @return position
     */
    @Override
    public Position getCurrentPosition() {
        return position;
    }

    /**
     * Pozició beállítása az adott karakter számára (csak egyszer lehet).
     * @param position a beállítani kívánt pozíció
     */
    public void setPosition (Position position) {
        this.position = (this.position == null) ? position : this.position;
    }

    /**
     * A legutoljára végrehatjott cselekedet.
     * @return action
     */
    @Override
    public Action getLastAction() {
        return actions.isEmpty() ? null : actions.get(actions.size() - 1);
    }

    /**
     * A name gettere.
     * @return name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Egy adott cselekedet végrehajtása. Elvileg a HP lehetne mínusz is de nem fogadja a bíró el úgy.
     * @param action egy akció amit végrehajt a karakter
     */
    public void doAction (Action action) {
        action.doAction(this);
        actions.add(action);

        hunger = (hunger - 0.4f >= MAX_HP_HUNGER_COUNT ? MAX_HP_HUNGER_COUNT : hunger - 0.4f);

        if (hunger <= 0) {
            hunger = 0;
            hp = hp - 5 > 0 ? hp - 5 : 0;
        }

        useEquippedItem(true);
    }

    /**
     * Item elfogyasztása.
     * @param foodHunger hunger szint növelő
     * @param foodHp hp szint növelő
     */
    public void eatItem (float foodHunger, float foodHp) {
        hunger += foodHunger;
        hp += foodHp;

        hp = hp > MAX_HP_HUNGER_COUNT ? MAX_HP_HUNGER_COUNT : hp;
        hp = hp > 0 ? hp : 0;
    }

    /**
     * A karakter valahonna sebzést kap.
     * @param amount a sebzés mértéke
     */
    public void getHurt (float amount) {
        hp -= amount;
        hp = hp > 0 ? hp : 0;
        attacked = true;
    }

    /**
     * Megtámadta-e már valaki az adott karaktert.
     * @return attacked
     */
    protected boolean isAttacked() {
        return attacked;
    }

    /**
     * A kézben levő tárgy haszálata.
     * @param timeUsed időről függ az elhasználódás vagy sem.
     */
    public void useEquippedItem (boolean timeUsed) {
        EquippableItem handTool = inventory.equippedItem();

        if(handTool != null && handTool.isTimeUsed() == timeUsed) {
            handTool.use();

            if(handTool.percentage() == 0) {
                inventory.clearHand();
            }
        }
    }

    /**
     * Debugoláshoz kell.
     * @return String.
     */
    public String toString() {
        return "Name: " + name + "\nHunger: " + getHunger() + "\nHP: " + getHp();
    }
}
