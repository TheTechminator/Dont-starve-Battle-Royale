package prog1.kotprog.dontstarve.solution.ai;

import prog1.kotprog.dontstarve.solution.GameManager;
import prog1.kotprog.dontstarve.solution.character.Character;
import prog1.kotprog.dontstarve.solution.level.Field;
import prog1.kotprog.dontstarve.solution.level.GameMap;
import prog1.kotprog.dontstarve.solution.terrain.TerrainObjectType;
import prog1.kotprog.dontstarve.solution.utility.Position;

import java.util.List;

/**
 * Tereptárgy megkeresése a pályán.
 */
public class MaterialFinder {
    /**
     * Aki keresi az eszközt.
     */
    private final Character character;

    /**
     * A pálya.
     */
    private final GameMap map;

    /**
     * MaterialFinder publikus konstructora.
     * @param character akinek keresünk.
     */
    public MaterialFinder (Character character) {
        this.character = character;
        this.map = GameManager.getInstance().getMap();
    }

    /**
     * A legközelebbi nyersanyag helyét adja vissza a paraméterben érkező típusok közül.
     * @param searchedMaterials ezeket a típusú nyersanyagokat keressük.
     * @return Position ahhol egy optimális material van.
     */
    public Position findNearestMaterial (List<TerrainObjectType> searchedMaterials) {
        Position pos = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Position tmp;

        for(int y = 0; y < map.getHeight(); y++) {
            for(int x = 0; x < map.getWidth(); x++) {
                tmp = new Position(x, y);

                if(fieldContainsMaterial(map.getField(x, y), searchedMaterials) && distance(pos) > distance(tmp)) {
                    pos = tmp;
                }
            }
        }

        return pos;
    }

    /**
     * A megadott fielden van-e a listában érkező materiálok közül.
     * @param field ahhol megnézzük milyen materialok vannak.
     * @param searchedMaterials ezeket a tpusú materialokat keressük.
     * @return true ha van bármelyfajta nyersanyagból a megadott fielden.
     */
    private boolean fieldContainsMaterial (Field field, List<TerrainObjectType> searchedMaterials) {
        return searchedMaterials.stream().anyMatch(searchedMaterial -> field.getTerrainObject().getType() == searchedMaterial);
    }

    /**
     * A megadott pozíció milyen messze van a karakterünktől.
     * @param pos1 egy kiválasztott pozíció.
     * @return távolság.
     */
    public double distance (Position pos1) {
        Position pos2 = character.getCurrentPosition().getNearestWholePosition();
        return Math.sqrt(Math.pow(pos1.getX() - pos2.getX(), 2) + Math.pow((pos1.getY() - pos2.getY()), 2));
    }
}
