package prog1.kotprog.dontstarve.solution.generator;

import prog1.kotprog.dontstarve.solution.GameManager;
import prog1.kotprog.dontstarve.solution.character.Character;
import prog1.kotprog.dontstarve.solution.inventory.BaseInventory;
import prog1.kotprog.dontstarve.solution.inventory.items.AbstractItem;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemLog;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemStone;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemTwig;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemRawBerry;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemRawCarrot;

/**
 * Egy karakter játékoz való csatlakozása után kapott nyersanyagok generálását segítő osztály.
 */
public class BaseMaterialGenerator {

    /**
     * Alap nyersanyag generálása.
     * @return egy generált nyersanyag
     */
    public static AbstractItem genarateBaseMaterial () {
        int random = GameManager.getInstance().getRandom().nextInt(5);

        return switch (random) {
            case 0 -> new ItemLog(1);
            case 1 -> new ItemStone(1);
            case 2 -> new ItemTwig(1);
            case 3 -> new ItemRawBerry(1);
            case 4 -> new ItemRawCarrot(1);
            default -> null;
        };
    }

    /**
     * A megadott character inventorijában legenerálja és beleteszi a megadott számú nyersanyagot.
     * @param character a karakterünk aki kapja a nyersanyagot
     * @param amount amennyit kap
     */
    public static void addBaseMaterialsToCharacter (Character character, int amount) {
        BaseInventory inventory = character.getInventory();

        for(int i = 0; i < amount; i++) {
            inventory.addItem(genarateBaseMaterial());
        }
    }
}
