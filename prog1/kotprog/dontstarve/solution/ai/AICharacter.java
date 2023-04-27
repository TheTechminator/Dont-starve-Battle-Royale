package prog1.kotprog.dontstarve.solution.ai;

import prog1.kotprog.dontstarve.solution.GameManager;
import prog1.kotprog.dontstarve.solution.character.Character;
import prog1.kotprog.dontstarve.solution.character.actions.Action;
import prog1.kotprog.dontstarve.solution.character.actions.ActionCraft;
import prog1.kotprog.dontstarve.solution.character.actions.ActionEquip;
import prog1.kotprog.dontstarve.solution.character.actions.ActionNone;
import prog1.kotprog.dontstarve.solution.character.actions.ActionAttack;
import prog1.kotprog.dontstarve.solution.character.actions.ActionEat;
import prog1.kotprog.dontstarve.solution.character.actions.ActionCollectItem;
import prog1.kotprog.dontstarve.solution.character.actions.ActionInteract;
import prog1.kotprog.dontstarve.solution.character.actions.ActionStep;
import prog1.kotprog.dontstarve.solution.inventory.Inventory;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemType;
import prog1.kotprog.dontstarve.solution.level.BaseField;
import prog1.kotprog.dontstarve.solution.terrain.TerrainObjectType;
import prog1.kotprog.dontstarve.solution.utility.Direction;
import prog1.kotprog.dontstarve.solution.utility.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Egy AI karaktert leíró osztály.
 */
public class AICharacter extends Character {
    /**
     * A maximális gally mennyiség amit az AI egyszerre felvesz.
     */
    private static final int MAX_TWIG_COUNT = 3;

    /**
     * A maximális fa mennyiség amit az AI egyszerre felvesz.
     */
    private static final int MAX_LOG_COUNT = 2;

    /**
     * A maximális kő mennyiség amit az AI egyszerre felvesz.
     */
    private static final int MAX_STONE_COUNT = 2;

    /**
     * A maximális bogyó mennyiség amit az AI egyszerre felvesz.
     */
    private static final int MAX_BERRY_COUNT = 5;

    /**
     * A minimális répa mennyiség amit fel vesz először az ai mielőtt mást keresne.
     */
    private static final int MIN_CARROT_COUNT = 3;

    /**
     * Tutorial módban van-e a játék.
     */
    private final boolean isTutorial;

    /**
     * Tereptárgy kereső.
     */
    private final MaterialFinder materialFinder;

    /**
     * Inventory manager.
     */
    private final InventoryManager inventoryManager;

    /**
     * Útvonal kereső.
     */
    private final PathFinding pathFinding;

    /**
     * Él-e még a játékos.
     */
    private boolean isPlayerAlive = true;

    /**
     * Az a koordináta ahova az ai éppen megy.
     */
    private Position destination;

    /**
     * Az osztály konstruktora.
     * @param name a karakter neve
     * @param isTutorial tutorialmódban van-e a játék
     */
    public AICharacter(String name, boolean isTutorial) {
        super(name);
        this.isTutorial = isTutorial;
        this.materialFinder = new MaterialFinder(this);
        this.inventoryManager = new InventoryManager((Inventory) getInventory());
        this.pathFinding = new PathFinding(this);
    }

    /**
     * Az idő eltelésekor meghívodik a metódus.
     * A beérkező actiont nem kezeli le mivel ő magának generál egyet
     * @param action egy action (csak a játékost érinti)
     */
    @Override
    public void doAction(Action action) {
        if(isTutorial) {
            super.doAction(new ActionNone());
        } else {
            super.doAction(whatToDo());
        }
    }

    /**
     * Mit csináljon az ai. Először egy listába kigyültjük a lehetséges optimmális akciókat fontossági sorrenben.
     * A legelső action a legfontossabb.
     * @return Action amit a karakter végrehajt.
     */
    private Action whatToDo () {
        List<Action> possibleActions = new ArrayList<>();

        possibleActions.add(escapeIfNeeded());
        possibleActions.add(protectAgainstPlayer());
        possibleActions.add(eatFoodIfNecessary());
        possibleActions.add(attackPlayer());
        possibleActions.add(craftItem());
        possibleActions.add(equipeItemIfCan());
        possibleActions.add(collectItem());
        possibleActions.add(collectMaterialOrStep());

        return possibleActions.stream().filter(Objects::nonNull).findFirst().orElse(null);
    }

    /**
     * Ha szükséges a bot megpróbál elmenekülni.
     * @return Action vagy null.
     */
    private Action escapeIfNeeded () {
        int attack = 4;

        if(GameManager.getInstance().getPlayer().getInventory().equippedItem() != null) {
            attack = GameManager.getInstance().getPlayer().getInventory().equippedItem().getAttackStrength();
        }

        attack++;

        if(getHp() < attack && GameManager.getInstance().getCharacterCount() > 3) {
            Position position = GameManager.getInstance().getPlayer().getCurrentPosition().getNearestWholePosition();
            position.setX(position.getX() * -100);
            position.setY(position.getY() * -100);

            Direction nextStep = pathFinding.nextStepTo(position);

            if(nextStep != null) {
                return new ActionStep(nextStep);
            }
        }

        return null;
    }

    /**
     * Ha kézbe tud venni egy megfelelő eszköszt létrejön egy action.
     * @return Action vagy null
     */
    private Action equipeItemIfCan () {
        Inventory inv = (Inventory)getInventory();

        if(inv.itemAmount(ItemType.LOG) < MAX_LOG_COUNT) {
            if(!inHand(ItemType.AXE)) {
                int index = inventoryManager.getItemIndex(ItemType.AXE);
                return index != -1 ? new ActionEquip(index) : null;
            }

            return null;
        }

        if(inv.itemAmount(ItemType.STONE) < MAX_STONE_COUNT) {
            if(!inHand(ItemType.PICKAXE)) {
                int index = inventoryManager.getItemIndex(ItemType.PICKAXE);
                return index != -1 ? new ActionEquip(index) : null;
            }

            return null;
        }

        if(inHand(ItemType.SPEAR)) {
            return null;
        }

        int index = inventoryManager.getItemIndex(ItemType.SPEAR);
        return index != -1 ? new ActionEquip(index) : null;
    }

    /**
     * Ha szüksége van evésre létrehoz egy actiont hozzá.
     * @return Action vagy null
     */
    private Action eatFoodIfNecessary () {
        int foodIndex;

        if (getHunger() > 60) {
            return null;
        }

        if(getHp() > 90) {
            foodIndex = inventoryManager.getFoodIndex(ItemType.RAW_BERRY);
            if(foodIndex != -1) {
                return new ActionEat(foodIndex);
            }
        }

        foodIndex = inventoryManager.getFoodIndex(ItemType.RAW_CARROT);
        return foodIndex == -1 ? null : new ActionEat(foodIndex);
    }

    /**
     * Ha tudunk valami item et begyűjteni létrejön hozzá egy action.
     * @return Action vagy null
     */
    private Action collectItem () {
        Position whole = getCurrentPosition().getNearestWholePosition();
        BaseField field = GameManager.getInstance().getField((int)whole.getX(), (int)whole.getY());

        if(field.items().length > 0) {
            return new ActionCollectItem();
        }

        return null;
    }

    /**
     * Ha tudunk egy megfelelő eszközt kraftolni ami még nincs létrejön hozzá egy action.
     * @return Action vagy null
     */
    private Action craftItem () {
        ItemType[] craftThis = {ItemType.AXE, ItemType.PICKAXE, ItemType.SPEAR};
        return Arrays.stream(craftThis).filter(this::isToolShouldBeCrafted).findFirst().map(ActionCraft::new).orElse(null);
    }

    /**
     * A megadott típusú eszközhöz megmondja, hogy érdemes-e kraftolni.
     * @param type kraftolandó eszköz típusa.
     * @return True ha le lehet kraftolni.
     */
    private boolean isToolShouldBeCrafted (ItemType type) {
        return !inHand(type) && ((Inventory)getInventory()).itemAmount(type) == 0 && inventoryManager.hasEnoughMaterialToCraft(type);
    }

    /**
     * A karakter kezében tartott eszköz típusa az e ami a paraméterben érkezett.
     * @param type eszköz típusa.
     * @return true ha az van a kezünkben.
     */
    private boolean inHand (ItemType type) {
        return getInventory().equippedItem() != null &&  getInventory().equippedItem().getType() == type;
    }

    /**
     * Tereptárgy bányászása vagy lépés egy következő pozícióra.
     * @return Action vagy null
     */
    private Action collectMaterialOrStep () {
        if(destination == null) {
            if(GameManager.getInstance().getCharacterCount() <= 2) {
                destination = materialFinder.findNearestMaterial(getSearchedMaterials());
            } else {
                destination = materialFinder.findNearestMaterial(getSearchedMaterialsMulti());
            }
        }

        Direction nextStep = pathFinding.nextStepTo(destination);

        if(nextStep == null) {
            destination = null;
            return new ActionInteract();
        }

        return new ActionStep(nextStep);
    }

    /**
     * Mijen tereptárgyakat lenne érdemes keresnie az AI nak.
     * @return List tereptárgy típusokat tartalmaz.
     */
    private List<TerrainObjectType> getSearchedMaterials () {
        Inventory inv = ((Inventory)getInventory());
        List<TerrainObjectType> types = new ArrayList<>();

        if (inv.equippedItem() != null && inv.itemAmount(ItemType.STONE) < MAX_STONE_COUNT && inv.equippedItem().getType() == ItemType.PICKAXE) {
            types.add(TerrainObjectType.STONE);
            return types;
        }

        if(inv.equippedItem() != null && inv.itemAmount(ItemType.LOG) < MAX_LOG_COUNT && inv.equippedItem().getType() == ItemType.AXE) {
            types.add(TerrainObjectType.TREE);
            return types;
        }

        if(inv.itemAmount(ItemType.TWIG) < MAX_TWIG_COUNT) {
            types.add(TerrainObjectType.TWIG);
            return types;
        }

        types.add(TerrainObjectType.CARROT);
        if(inv.itemAmount(ItemType.RAW_CARROT) < MIN_CARROT_COUNT) {
            return types;
        }

        if(inv.itemAmount(ItemType.RAW_BERRY) < MAX_BERRY_COUNT) {
            types.add(TerrainObjectType.BERRY);
        }

        return types;
    }

    private List<TerrainObjectType> getSearchedMaterialsMulti () {
        Inventory inv = ((Inventory)getInventory());
        List<TerrainObjectType> types = new ArrayList<>();
        types.add(TerrainObjectType.CARROT);

        if (inv.equippedItem() != null && inv.itemAmount(ItemType.STONE) < MAX_STONE_COUNT && inv.equippedItem().getType() == ItemType.PICKAXE) {
            types.add(TerrainObjectType.STONE);
        }

        if(inv.equippedItem() != null && inv.itemAmount(ItemType.LOG) < 4 && inv.equippedItem().getType() == ItemType.AXE) {
            types.add(TerrainObjectType.TREE);
        }

        if(inv.itemAmount(ItemType.TWIG) < 5) {
            types.add(TerrainObjectType.TWIG);
        }

        if(inv.itemAmount(ItemType.RAW_CARROT) < MIN_CARROT_COUNT) {
            return types;
        }

        if(inv.itemAmount(ItemType.RAW_BERRY) < MAX_BERRY_COUNT) {
            types.add(TerrainObjectType.BERRY);
        }

        return types;
    }

    /**
     * Ha védekezni kell a játékos ellen létrejön hozzá egy action.
     * @return Action vagy null
     */
    private Action protectAgainstPlayer () {
        if((GameManager.getInstance().getPlayer().getLastAction() instanceof ActionNone) && GameManager.getInstance().getCharacterCount() <= 4) {
            getHurt(0);
        }

        if(isAttacked()) {
            Character player = GameManager.getInstance().getPlayer();

            if(player == null) {
                isPlayerAlive = false;
            }

            return attackCharacter(player);
        }

        return null;
    }

    /**
     * Ha tudjuk a playert támadni létrejön hozzá a megfelelő action.
     * @return Action vagy null
     */
    private Action attackPlayer () {
        if(isPlayerAlive && inHand(ItemType.SPEAR)) {
            Character player = GameManager.getInstance().getPlayer();

            if(player == null) {
                isPlayerAlive = false;
            }

            return attackCharacter(player);
        }

        return null;
    }

    /**
     * A paraméterben érkező játékost próbálja megtámadni. De csak őt, ha más van közelebb nem támad csak megy.
     * @param character aki meg fogunk támadni.
     * @return Action vagy null
     */
    private Action attackCharacter (Character character) {
        if(character == null) {
            return null;
        }

        Direction nextStep = pathFinding.nextStepTo(character.getCurrentPosition().getNearestWholePosition());

        if (nextStep == null || GameManager.getInstance().getPositionGenerator().nearestCharacterTo(this, 2) == character) {
            return new ActionAttack();
        }

        return new ActionStep(nextStep);
    }
}
