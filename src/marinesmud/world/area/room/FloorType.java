/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.world.area.room;

import marinesmud.containers.Vector2i;

/**
 *
 * @author jblew
 */
public class FloorType {
    public final String tilesFile;
    public final Vector2i topTilePosition;
    public final Vector2i middleTilePosition;
    public final Vector2i bottomTilePosition;

    public FloorType(String tilesFile, Vector2i topTilePosition, Vector2i middleTilePosition, Vector2i bottomTilePosition) {
        this.tilesFile = tilesFile;
        this.topTilePosition = topTilePosition;
        this.middleTilePosition = middleTilePosition;
        this.bottomTilePosition = bottomTilePosition;
    }

    public static enum List {
        
        SMALL_GRASS(new FloorType("ground_tiles", new Vector2i(1, 1), new Vector2i(2, 1), new Vector2i(3, 1))),
        DEFAULT(SMALL_GRASS.type);

        public final FloorType type;

        private List(FloorType type) {
            this.type = type;
        }
    }
}
