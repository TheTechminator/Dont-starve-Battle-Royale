package prog1.kotprog.dontstarve.solution.level;

import prog1.kotprog.dontstarve.solution.terrain.TerrainObjectType;

/**
 * A betöltendő mapen előforduló színek enumja.
 */
public class MapColors {
    /**
     * Üres mező.
     */
    static final int EMPTY = 0xFF32C832;

    /**
     * Vizet tartalmazó mező.
     */
    static final int WATER = 0xFF3264C8;

    /**
     * Fát tartalmazó mező.
     */
    static final int TREE = 0xFFC86432;

    /**
     * Követ tartalmazó mező.
     */
    static final int STONE = 0xFFC8C8C8;

    /**
     * Gallyat tartalmazó mező.
     */
    static final int TWIG = 0xFFF0B478;

    /**
     * Bogyót tartalmazó mező.
     */
    static final int BERRY = 0xFFFF0000;

    /**
     * Répát tartalmazó mező.
     */
    static final int CARROT = 0xFFFAC800;

    /**
     * Field létrehozása tereptárgyal együtt.
     * @param color szín.
     * @return Field.
     */
    public static Field generateField (int color) {
        return switch (color) {
            case EMPTY -> new Field(TerrainObjectType.EMPTY);
            case WATER -> new Field(TerrainObjectType.WATER);
            case TREE -> new Field(TerrainObjectType.TREE);
            case STONE -> new Field(TerrainObjectType.STONE);
            case TWIG -> new Field(TerrainObjectType.TWIG);
            case BERRY -> new Field(TerrainObjectType.BERRY);
            case CARROT -> new Field(TerrainObjectType.CARROT);
            default -> null;
        };
    }
}
