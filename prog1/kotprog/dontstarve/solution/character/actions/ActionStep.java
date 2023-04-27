package prog1.kotprog.dontstarve.solution.character.actions;

import prog1.kotprog.dontstarve.solution.GameManager;
import prog1.kotprog.dontstarve.solution.character.Character;
import prog1.kotprog.dontstarve.solution.utility.Direction;
import prog1.kotprog.dontstarve.solution.utility.Position;

/**
 * A lépés akció leírására szolgáló osztály: a karakter egy lépést tesz balra, jobbra, fel vagy le.
 */
public class ActionStep extends Action {
    /**
     * A mozgás iránya.
     */
    private final Direction direction;

    /**
     * Az akció létrehozására szolgáló konstruktor.
     *
     * @param direction a mozgás iránya
     */
    public ActionStep(Direction direction) {
        super(ActionType.STEP);
        this.direction = direction;
    }

    /**
     * A direction gettere.
     * @return a mozgás iránya
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Az adott action végrehajtása.
     * @param character aki végzi az actiont.
     */
    @Override
    public void doAction(Character character) {
        float xMove = 0f;
        float yMove = 0f;

        switch (direction) {
            case LEFT -> xMove -= character.getSpeed();
            case RIGHT -> xMove += character.getSpeed();
            case UP -> yMove -= character.getSpeed();
            case DOWN -> yMove += character.getSpeed();
        }

        moveTo(character, xMove, yMove);
    }

    /**
     * A játékos az adott pozícióra lép.
     * @param character aki lép.
     * @param xMove x pozíció.
     * @param yMove y pozíció.
     */
    private void moveTo (Character character, float xMove, float yMove) {
        Position pos = character.getCurrentPosition();

        int x = Math.round(pos.getX() + xMove);
        int y = Math.round(pos.getY() + yMove);

        if(GameManager.getInstance().getMap() != null && GameManager.getInstance().getMap().isWalkable(x, y)) {
            pos.setX(pos.getX() + xMove);
            pos.setY(pos.getY() + yMove);
        }
    }
}
