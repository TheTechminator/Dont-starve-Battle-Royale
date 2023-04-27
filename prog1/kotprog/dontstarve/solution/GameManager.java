package prog1.kotprog.dontstarve.solution;

import prog1.kotprog.dontstarve.solution.ai.AICharacter;
import prog1.kotprog.dontstarve.solution.character.BaseCharacter;
import prog1.kotprog.dontstarve.solution.character.Character;
import prog1.kotprog.dontstarve.solution.character.actions.Action;
import prog1.kotprog.dontstarve.solution.generator.BaseMaterialGenerator;
import prog1.kotprog.dontstarve.solution.generator.PositionGenerator;
import prog1.kotprog.dontstarve.solution.inventory.Inventory;
import prog1.kotprog.dontstarve.solution.level.BaseField;
import prog1.kotprog.dontstarve.solution.level.Field;
import prog1.kotprog.dontstarve.solution.level.Level;
import prog1.kotprog.dontstarve.solution.level.GameMap;
import prog1.kotprog.dontstarve.solution.utility.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A játék működéséért felelős osztály.<br>
 * Az osztály a singleton tervezési mintát valósítja meg.
 */
public final class GameManager {
    /**
     * Az osztályból létrehozott egyetlen példány (nem lehet final).
     */
    private static GameManager instance = new GameManager();

    /**
     * Be van-e állítva a változó.
     */
    private static final int NOTSET = -1;

    /**
     * Random objektum, amit a játék során használni lehet.
     */
    private final Random random = new Random();

    /**
     * A játékban levő karaktereket tartalmazó lista.
     */
    private final List<Character> characters = new ArrayList<>();

    /**
     * A map.
     */
    private GameMap map;

    /**
     * Pozíció generátor. Ne kelljen folyton létrehozogatni.
     */
    private PositionGenerator positionGenerator;

    /**
     * A karakterek között a játékos indexe.
     */
    private int playerIndex = NOTSET;

    /**
     * Elkezdődött-e már a játék.
     */
    private boolean started;

    /**
     * Végetért-e már a játék.
     */
    private boolean ended;

    /**
     * Eltelt idő.
     */
    private int currentTime;

    /**
     * Tutorial módban van-e.
     */
    private boolean isTutorial;

    /**
     * Az osztály privát konstruktora.
     */
    private GameManager() {
        started = false;
        ended = false;
        currentTime = 0;
        isTutorial = false;
    }

    /**
     * Az osztályból létrehozott példány elérésére szolgáló metódus.
     * @return az osztályból létrehozott példány
     */
    public static GameManager getInstance() {
        return instance;
    }

    /**
     * A létrehozott random objektum elérésére szolgáló metódus.
     * @return a létrehozott random objektum
     */
    public Random getRandom() {
        return random;
    }

    /**
     * Egy karakter becsatlakozása a játékba.<br>
     * A becsatlakozásnak számos feltétele van:
     * <ul>
     *     <li>A pálya már be lett töltve</li>
     *     <li>A játék még nem kezdődött el</li>
     *     <li>Csak egyetlen emberi játékos lehet, a többi karaktert a gép irányítja</li>
     *     <li>A névnek egyedinek kell lennie</li>
     * </ul>
     * @param name a csatlakozni kívánt karakter neve
     * @param player igaz, ha emberi játékosról van szó; hamis egyébként
     * @return a karakter pozíciója a pályán, vagy (Integer.MAX_VALUE, Integer.MAX_VALUE) ha nem sikerült hozzáadni
     */
    public Position joinCharacter(String name, boolean player) {
        if(map == null || isGameStarted() || getCharacter(name) != null || (player && playerIndex != NOTSET)) {
            return new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }

        Position newPosition = positionGenerator.generatePosition();

        if((int)(newPosition.getY()) == Integer.MAX_VALUE || (int)(newPosition.getX()) == Integer.MAX_VALUE) {
            return newPosition;
        }

        if(player) {
            playerIndex = characters.size();
            return characterJoinToMap(new Character(name), newPosition);
        }

        return characterJoinToMap(new AICharacter(name, isTutorial), newPosition);
    }

    /**
     * Egy karakter mapra helyezése.
     * @param character egy adott karakter
     * @param position a pozíciója
     * @return az újjonnan csatlakozott karakter pozíciója
     */
    private Position characterJoinToMap (Character character, Position position) {
        characters.add(character);
        BaseMaterialGenerator.addBaseMaterialsToCharacter(character, 4);
        character.setPosition(position);
        return position;
    }

    /**
     * Egy adott nevű karakter lekérésére szolgáló metódus.<br>
     * @param name A lekérdezni kívánt karakter neve
     * @return Az adott nevű karakter objektum, vagy null, ha már a karakter meghalt vagy nem is létezett
     */
    public BaseCharacter getCharacter(String name) {
        return characters.stream().filter(character -> character.getName().equals(name) && character.isAlive()).findFirst().orElse(null);
    }

    /**
     * Ezen metódus segítségével lekérdezhető, hogy hány karakter van még életben.
     * @return Az életben lévő karakterek száma
     */
    public int remainingCharacters() {
        return characters.size();
    }

    /**
     * Ezen metódus segítségével történhet meg a pálya betöltése.<br>
     * A pálya betöltésének azelőtt kell megtörténnie, hogy akár 1 karakter is csatlakozott volna a játékhoz.<br>
     * A pálya egyetlen alkalommal tölthető be, később nem módosítható.
     * @param level a fájlból betöltött pálya
     */
    public void loadLevel(Level level) {
        if(map == null) {
            map = new GameMap(level);
            positionGenerator = new PositionGenerator(map, characters);
        }
    }

    /**
     * A pálya egy adott pozícióján lévő mező lekérdezésére szolgáló metódus.
     * @param x a vízszintes (x) irányú koordináta
     * @param y a függőleges (y) irányú koordináta
     * @return az adott koordinátán lévő mező
     */
    public BaseField getField(int x, int y) {
        return map.getField(x, y);
    }

    /**
     * A játék megkezdésére szolgáló metódus.<br>
     * A játék csak akkor kezdhető el, ha legalább 2 karakter már a pályán van,
     * és közülük pontosan az egyik az emberi játékos által irányított karakter.
     * @return igaz, ha sikerült elkezdeni a játékot; hamis egyébként
     */
    public boolean startGame() {
        if(!started && characters.size() >= 2 && playerIndex != NOTSET) {
            started = true;
            return true;
        }

        return false;
    }

    /**
     * Ez a metódus jelzi, hogy 1 időegység eltelt.<br>
     * A metódus először lekezeli a felhasználói inputot, majd a gépi ellenfelek cselekvését végzi el,
     * végül eltelik egy időegység.<br>
     * Csak akkor csinál bármit is, ha a játék már elkezdődött, de még nem fejeződött be.
     * @param action az emberi játékos által végrehajtani kívánt akció
     */
    public void tick(Action action) {
        if(started && !ended) {
            map.tick();
            doAction(action);
            characters.removeIf(character -> !character.isAlive() && dieCharacter(character));

            if(remainingCharacters() <= 1) {
                ended = true;
            }

            currentTime++;
        }
    }

    /**
     * Először az emberi játékos majd a botok is végrahajtják az actionokat.
     * @param action az emberi játékos cselekvése.
     */
    private void doAction (Action action) {
        getPlayer().doAction(action);

        characters.forEach(character -> {
            if(character != getPlayer()) {
                character.doAction(action);
            }
        });

        if(!getPlayer().isAlive()) {
            ended = true;
        }
    }

    /**
     * A karakter meghalását leíró metódus.
     * Ha a karakter meghal a mezőre hullik a cucca.
     * @param character egy megadott karakter aki épp már halott
     */
    private boolean dieCharacter (Character character) {
        Position whole = character.getCurrentPosition().getNearestWholePosition();
        Field field = map.getField((int) whole.getX(), (int) whole.getY());

        ((Inventory)(character.getInventory())).getItems().forEach(field::addItem);

        return true;
    }

    /**
     * Ezen metódus segítségével lekérdezhető az aktuális időpillanat.<br>
     * A játék kezdetekor ez az érték 0 (tehát a legelső időpillanatban az idő 0),
     * majd minden eltelt időpillanat után 1-gyel növelődik.
     * @return az aktuális időpillanat
     */
    public int time() {
        return currentTime;
    }

    /**
     * Ezen metódus segítségével lekérdezhetjük a játék győztesét.<br>
     * Amennyiben a játéknak még nincs vége (vagy esetleg nincs győztes), akkor null-t ad vissza.
     * @return a győztes karakter vagy null
     */
    public BaseCharacter getWinner() {
        return (!ended || remainingCharacters() != 1 ? null : characters.get(0));
    }

    /**
     * Ezen metódus segítségével lekérdezhetjük, hogy a játék elkezdődött-e már.
     * @return igaz, ha a játék már elkezdődött; hamis egyébként
     */
    public boolean isGameStarted() {
        return started;
    }

    /**
     * Ezen metódus segítségével lekérdezhetjük, hogy a játék befejeződött-e már.
     * @return igaz, ha a játék már befejeződött; hamis egyébként
     */
    public boolean isGameEnded() {
        return ended;
    }

    /**
     * Ezen metódus segítségével beállítható, hogy a játékot tutorial módban szeretnénk-e elindítani.<br>
     * Alapértelmezetten (ha nem mondunk semmit) nem tutorial módban indul el a játék.<br>
     * Tutorial módban a gépi karakterek nem végeznek cselekvést, csak egy helyben állnak.<br>
     * A tutorial mód beállítása még a karakterek csatlakozása előtt történik.
     * @param tutorial igaz, amennyiben tutorial módot szeretnénk; hamis egyébként
     */
    public void setTutorial(boolean tutorial) {
        isTutorial = characters.isEmpty() ? tutorial : isTutorial;
    }

    /**
     * A map gettere.
     * @return map
     */
    public GameMap getMap () {
        return map;
    }

    /**
     * Az emberi játékos lekérése.
     * @return character.
     */
    public Character getPlayer () {
        return characters.size() > playerIndex ? characters.get(playerIndex) : null;
    }

    /**
     * Visszaadja a position generátort.
     * @return position generator.
     */
    public PositionGenerator getPositionGenerator () {
        return positionGenerator;
    }

    /**
     * Le lehet kérni a karakterek számát.
     * @return karakterek száma.
     */
    public int getCharacterCount () {
        return characters.size();
    }
}
