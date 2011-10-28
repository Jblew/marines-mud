/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world;

/**
 *
 * @author jblew
 */
public enum Direction {

    NORTH("północ", "północy", "north", 0, -1, 0, "SOUTH"),
    SOUTH("południe", "południa", "south", 0, 1, 0, "NORTH"),
    WEST("zachód", "zachodu", "west", -1, 0, 0, "EAST"),
    EAST("wschód", "wschodu", "east", 1, 0, 0, "WEST"),
    UP("góra", "góry", "up", 0, 0, 1, "DOWN"),
    DOWN("dół", "dołu", "down", 0, 0, -1, "UP");
    private final int xAddition;
    private final int yAddition;
    private final int zAddition;
    private final boolean is2D;
    private final String mianownik;
    private final String dopelniacz;
    private final String shortcutName;
    private final String oppositeDirectionName;

    private Direction(String mianownik_, String dopelniacz_, String shortcutName_, int xAddition_, int yAddition_, int zAddition_, String oppositeDirectionName_) {
        mianownik = mianownik_;
        dopelniacz = dopelniacz_;
        shortcutName = shortcutName_;

        xAddition = xAddition_;
        yAddition = yAddition_;
        zAddition = zAddition_;

        if (zAddition == 0) {
            is2D = true;
        } else {
            is2D = false;
        }
        oppositeDirectionName = oppositeDirectionName_;
    }

    public int getXAddition() {
        return xAddition;
    }

    public int getYAddition() {
        return yAddition;
    }

    public int getZAddition() {
        return zAddition;
    }

    public boolean is2D() {
        return is2D;
    }

    public String getMianownik() {
        return mianownik;
    }

    public String getDopelniacz() {
        return dopelniacz;
    }

    public String getShortcutName() {
        return shortcutName;
    }

    public Direction getOppositeDirection() {
        return Direction.valueOf(oppositeDirectionName);
    }
}
