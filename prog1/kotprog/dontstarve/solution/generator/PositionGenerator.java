package prog1.kotprog.dontstarve.solution.generator;

import prog1.kotprog.dontstarve.solution.character.Character;
import prog1.kotprog.dontstarve.solution.level.GameMap;
import prog1.kotprog.dontstarve.solution.utility.Position;
import java.util.List;

/**
 * Egy karakter játékhoz való csatlakozása után a pozició megadását segítő osztály.
 */
public class PositionGenerator {
    /**
     * Távolság.
     */
    private int threshold;

    /**
     * A map (térkép).
     */
    private final GameMap map;

    /**
     * A pályán levő karakterek.
     */
    private final List<Character> characters;

    /**
     * A pozíció generálásának konstruktora.
     * @param map térkép
     * @param characters karakterek
     */
    public PositionGenerator (GameMap map, List<Character> characters) {
        this.map = map;
        threshold = 50;
        this.characters = characters;
    }

    /**
     * Egy optimális pozíció generálása.
     * @return optimális pozíció
     */
    public Position generatePosition () {
        Position position = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                if (map.isEmpty(x, y) && isFarEnough(characters, y, x)) {
                    position.setY(y);
                    position.setX(x);

                    return position;
                }
            }
        }

        if(threshold < 5) {
            return position;
        }

        threshold -= 5;
        return generatePosition();
    }

    /**
     * Egy lehetséges pozíció elég messze van e a többi karakktertől.
     * @param characters karakterek
     * @param y y koordináta
     * @param x x koordináta
     * @return igaz ha elég messze van, hamis különben
     */
    private boolean isFarEnough (List<Character> characters, int y, int x) {
        Position pos;

        for (Character character : characters) {
            pos = character.getCurrentPosition();
            if (calcDistance(pos.getX(), pos.getY(), x, y) < threshold) {
                return false;
            }
        }

        return true;
    }

    /**
     * Távolság kiszámítása két pont között.
     * @param startX kezdő x
     * @param startY kezdő y
     * @param destX végpont x
     * @param destY végpont y
     * @return két pont távolsága
     */
    private int calcDistance (float startX, float startY, float destX, float destY) {
        float xDist = destX - startX;
        float yDist = destY - startY;

        return (int)(Math.sqrt((xDist * xDist) + (yDist * yDist)));
    }

    /**
     * ávolság kiszámítása két pont között.
     * @param pos1 kezdő pozíció
     * @param pos2 vég pozíció
     * @return két pont távolsága
     */
    public static double calcDistance (Position pos1, Position pos2) {
        return Math.sqrt(Math.pow(pos1.getX() - pos2.getX(), 2) + Math.pow((pos1.getY() - pos2.getY()), 2));
    }

    /**
     * A legközelebb eső karakter a kiválasztott karakterhez egy megadott maximális távolság alapján.
     * @param chooseCharacter a kiválasztott karakter
     * @param maxDistance a karakterünktől maximális távolság ami még elég közel van
     * @return a legközelebb esé karakter vagy null ha messzebb van mindenki mint a maxDistance
     */
    public Character nearestCharacterTo (Character chooseCharacter, float maxDistance) {
        Character nearest = null;
        double miniDist = maxDistance;
        double dist;

        for(Character tmp : characters) {
            if(tmp == chooseCharacter) {
                continue;
            }

            dist = calcDistance(tmp.getCurrentPosition(), chooseCharacter.getCurrentPosition());

            if (dist <= miniDist) {
                miniDist = dist;
                nearest = tmp;
            }
        }

        return nearest;
    }
}
