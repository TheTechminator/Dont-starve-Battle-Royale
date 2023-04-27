package prog1.kotprog.dontstarve.solution.character.actions;

import prog1.kotprog.dontstarve.solution.character.Character;

/**
 * A várakozás akció leírására szolgáló osztály: a karakter nem végez cselekvést az aktuális körben.
 */
public class ActionNone extends Action {
    /**
     * Az akció létrehozására szolgáló konstruktor.
     */
    public ActionNone() {
        super(ActionType.NONE);
    }

    /**
     * Az adott action végrehajtása.
     * @param character aki végzi az actiont.
     */
    @Override
    public void doAction(Character character) {
        //Azt csináljam amit a legtöbben az órákon (semmit)
    }
}
