package prog1.kotprog.dontstarve.solution.ai;

import prog1.kotprog.dontstarve.solution.GameManager;
import prog1.kotprog.dontstarve.solution.level.GameMap;
import prog1.kotprog.dontstarve.solution.utility.Direction;
import prog1.kotprog.dontstarve.solution.utility.Position;

/**
 * Egy optimális útvonal keresése a karakter számára.
 */
public class PathFinding {
    /**
     * A játékos pozíciója.
     */
    private final AICharacter character;

    /**
     * A játék pályája.
     */
    private final GameMap map;

    /**
     * PathFinding publikus constructor.
     * @param character a mozgatni kívánt karakter.
     */
    public PathFinding (AICharacter character) {
        this.character = character;
        this.map = GameManager.getInstance().getMap();
    }

    /**
     * A kezdő pozíció és a vég pozíció alapján létrehoz egy Directiont (irányt) amerre mehet a karakter.
     * @param to ahova megyünk.
     * @return Direction, amerre mehetünk vagy null.
     */
    public Direction nextStepTo (Position to) {
        Position from = character.getCurrentPosition().getNearestWholePosition();

        if(from.getX() == to.getX() && from.getY() == to.getY()) {
            return null;
        } else if(from.getX() == to.getX()) {
            return from.getY() < to.getY() ? validateDirection(from, Direction.DOWN) : validateDirection(from, Direction.UP);
        } else {
            return from.getX() < to.getX() ? validateDirection(from, Direction.RIGHT) : validateDirection(from, Direction.LEFT);
        }
    }

    /**
     * Megnézi, hogy a megadott mezőre lehet-e lépni.
     * @param from ahhol éppen vagyunk.
     * @param direction Amerre mennénk.
     * @return Direction vagy null.
     */
    private Direction validateDirection (Position from, Direction direction) {
        int x = (int) from.getX();
        int y = (int) from.getY();

        return switch (direction) {
            case DOWN -> map.isWalkable(x, y + 1) ? direction : null;
            case UP -> map.isWalkable(x, y - 1) ? direction : null;
            case LEFT -> map.isWalkable(x - 1, y) ? direction : null;
            case RIGHT -> map.isWalkable(x + 1, y) ? direction : null;
        };
    }
}
