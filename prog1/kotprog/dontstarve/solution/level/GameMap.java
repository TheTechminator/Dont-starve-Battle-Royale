package prog1.kotprog.dontstarve.solution.level;

import prog1.kotprog.dontstarve.solution.terrain.TerrainObjectType;

/**
 * A játék mapját leíró osztály.
 */
public class GameMap {
    /**
     * A map mezői.
     */
    private Field[][] fields; //[Y:height][X:width]

    /**
     * Szélesség.
     */
    private int width;

    /**
     * Magasság.
     */
    private int height;

    /**
     * Map betöltése.
     * @param level - egy adott pálya
     */
    public GameMap(Level level) {
        initFields(level);
    }

    /**
     * A mezők inicializálása.
     * @param level - egy adott pálya
     */
    private void initFields (Level level) {
        width = level.getWidth();
        height = level.getHeight();
        fields = new Field[height][width];
        loadUpFields(level);
    }

    /**
     * Egy field lekérése.
     * @param x - x koordináta
     * @param y - y koordináta
     * @return a lekérni kívánt mező vagy null
     */
    public Field getField (int x, int y) {
        if(x >= 0 && x < width && y >= 0 && y < height) {
            return fields[y][x];
        }

        return null;
    }

    /**
     * A mezők tereptárgyakkal való feltötlése.
     * @param level - egy adott pálya
     */
    private void loadUpFields (Level level) {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                fields[y][x] = MapColors.generateField(level.getColor(x, y));
            }
        }
    }

    /**
     * Lehet-e sátélni a kiválasztott fielden.
     * @param x - x koordináta
     * @param y - y koordináta
     * @return igaz ha lehet hamis ha nem
     */
    public boolean isWalkable (int x, int y) {
        if(x >= 0 && x < width && y >= 0 && y < height) {
            return fields[y][x].isWalkable();
        }

        return false;
    }

    /**
     * Sélesség lekérése.
     * @return width
     */
    public int getWidth () {
        return width;
    }

    /**
     * Magasság lekérése.
     * @return height
     */
    public int getHeight () {
        return height;
    }

    /**
     * Üres-e a kiválasztott mező.
     * @param x - x koordináta
     * @param y - y koordináta
     * @return igaz ha igen, hamis ha nem
     */
    public boolean isEmpty (int x, int y) {
        if(x >= 0 && x < width && y >= 0 && y < height) {
            return fields[y][x].getTerrainObject().getType() == TerrainObjectType.EMPTY;
        }

        return false;
    }

    /**
     * Az idő telése.
     */
    public void tick () {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                fields[y][x].tick();
            }
        }
    }
}
