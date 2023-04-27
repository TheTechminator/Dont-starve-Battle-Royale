package prog1.kotprog.dontstarve.solution.level;

import prog1.kotprog.dontstarve.solution.character.Character;
import prog1.kotprog.dontstarve.solution.inventory.items.AbstractItem;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemFire;
import prog1.kotprog.dontstarve.solution.terrain.TerrainObject;
import prog1.kotprog.dontstarve.solution.terrain.TerrainObjectType;
import java.util.ArrayList;
import java.util.List;

/**
 * Egy mező leírására szolgáló osztály.
 */
public class Field implements BaseField {
    /**
     * A mezőn levő itemek.
     */
    private final List<AbstractItem> abstractItems = new ArrayList<>();

    /**
     * A mezőhöz tartozó terrain object.
     */
    private final TerrainObject terrainObject;

    /**
     * A mezőn levő tábortűz.
     */
    private AbstractItem campFire;

    /**
     * A tábortűz égési ideje.
     */
    private int fireTime;

    /**
     * Mező létrehozássa.
     * @param type a mezőn levő terrain object
     */
    public Field (TerrainObjectType type) {
        terrainObject = new TerrainObject(type);
        fireTime = 0;
    }

    /**
     * Lehet e a mezőn járni.
     * @return igaz ha igen, hamis ha nem
     */
    @Override
    public boolean isWalkable() {
        return terrainObject.getType() != TerrainObjectType.WATER;
    }

    /**
     * Van-e fa.
     * @return igaz ha igen, hamis ha nem
     */
    @Override
    public boolean hasTree() {
        return terrainObject.getType() == TerrainObjectType.TREE;
    }

    /**
     * Van-e kő.
     * @return igaz ha igen, hamis ha nem
     */
    @Override
    public boolean hasStone() {
        return terrainObject.getType() == TerrainObjectType.STONE;
    }

    /**
     * Van-e gally (már nem gélly re gondoltam).
     * @return igaz ha igen, hamis ha nem
     */
    @Override
    public boolean hasTwig() {
        return terrainObject.getType() == TerrainObjectType.TWIG;
    }

    /**
     * Van-e bogyó.
     * @return igaz ha igen, hamis ha nem
     */
    @Override
    public boolean hasBerry() {
        return terrainObject.getType() == TerrainObjectType.BERRY;
    }

    /**
     * Van-e répa.
     * @return igaz ha igen, hamis ha nem
     */
    @Override
    public boolean hasCarrot() {
        return terrainObject.getType() == TerrainObjectType.CARROT;
    }

    /**
     * Van-e még égő tábortűz.
     * @return igaz ha igen, hamis ha nem
     */
    @Override
    public boolean hasFire() {
        return campFire != null;
    }

    /**
     * Üres-e, tehát se tűz se más egyéb.
     * @return igaz ha igen, hamis ha nem
     */
    public boolean isEmpty () {
        return (terrainObject.getType() == TerrainObjectType.EMPTY && campFire == null);
    }

    /**
     * Tábortűz lerakása a mezőre.
     * @return igaz ha sikeres volt, hamis ha nem
     */
    public boolean putFire () {
        if(isEmpty()) {
            campFire = new ItemFire();
            fireTime = 60;
            return true;
        }

        return false;
    }

    /**
     * Az idő telése, a tábortűznnek fontos.
     */
    public void tick () {
        fireTime--;
        if(fireTime == 0) {
            campFire = null;
        }
    }

    /**
     * A mezőn levő itemek.
     * @return itemeket tartalmazó tömb
     */
    @Override
    public AbstractItem[] items() {
        AbstractItem[] array = new AbstractItem[abstractItems.size()];
        return abstractItems.toArray(array);
    }

    /**
     * Item hozzáadása.
     * @param item egy item
     */
    public void addItem (AbstractItem item) {
        abstractItems.add(item);
    }

    /**
     * Item eltávolítása.
     * @param item egy item
     */
    public void removeItem (AbstractItem item) {
        abstractItems.remove(item);
    }

    /**
     * A mezőn levő terrain object gettere.
     * @return terrain object
     */
    public TerrainObject getTerrainObject () {
        return terrainObject;
    }

    /**
     * A mezőn levő terrain object bányászása.
     * @param character a bányászó karakter
     * @return a kibányászott nyersanyag vagy null
     */
    public AbstractItem extractTerrainObject (Character character) {
        if(terrainObject.extraction(character)) {
            if(terrainObject.isToInventory()) {
                return terrainObject.getItem();
            }

            abstractItems.add(terrainObject.getItem());
        }

        return null;
    }
}
