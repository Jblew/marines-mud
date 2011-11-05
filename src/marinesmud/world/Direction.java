/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world;

/**
 * An enum containing possible straight directions in 3D world.
 * It also provides some useful data about them, like: x,y,z addition, or opposite direction.
 * @author jblew
 * @license Kod jest objęty licencją zawartą w pliku LICESNE
 */
public enum Direction {
    NORTH("north", 0, -1, 0, "SOUTH"),
    SOUTH("south", 0, 1, 0, "NORTH"),
    WEST("west", -1, 0, 0, "EAST"),
    EAST("east", 1, 0, 0, "WEST"),
    UP("up", 0, 0, 1, "DOWN"),
    DOWN("down", 0, 0, -1, "UP");
    private final int xAddition;
    private final int yAddition;
    private final int zAddition;
    private final String name;
    private final String oppositeDirectionName;

    private Direction(String name, int xAddition, int yAddition, int zAddition, String oppositeDirectionName) {
        this.name = name;
        this.xAddition = xAddition;
        this.yAddition = yAddition;
        this.zAddition = zAddition;
        this.oppositeDirectionName = oppositeDirectionName;
    }

    /**
     * Returns addition in X axis. Eg. for east it's +1 and for west it's -1.
     * @return Addition in X axis.
     */
    public int getXAddition() {
        return xAddition;
    }

    /**
     * Returns addition in Y axis. Eg. for south it's +1 and for north it's -1.
     * @return Addition in Y axis.
     */
    public int getYAddition() {
        return yAddition;
    }

    /**
     * Returns addition in Z axis. Eg. for up it's -1 and for down it's +1.
     * @return Addition in Z axis.
     */
    public int getZAddition() {
        return zAddition;
    }

    /**
     * Returns the opposite direction.
     * @return The opposite direction.
     */
    public Direction getOppositeDirection() {
        return Direction.valueOf(oppositeDirectionName);
    }
}
