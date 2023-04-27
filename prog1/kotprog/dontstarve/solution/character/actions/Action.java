package prog1.kotprog.dontstarve.solution.character.actions;

import prog1.kotprog.dontstarve.solution.character.Character;

/**
 * A karakterek egy akciójának leírására szolgáló osztály.
 */
public abstract class Action {
    /**
     * Az akció típusa.
     * @see ActionType
     */
    private final ActionType type;

    /**
     * Az akció létrehozására szolgáló konstruktor.
     * @param type az akció típusa
     */
    public Action(ActionType type) {
        this.type = type;
    }

    /**
     * A type gettere.
     * @return az akció típusa
     */
    public ActionType getType() {
        return type;
    }

    /**
     * Az adott action végrehajtása.
     * @param character aki végzi az actiont.
     */
    public abstract void doAction (Character character);
}
