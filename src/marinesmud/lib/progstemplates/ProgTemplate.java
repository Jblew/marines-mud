/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.progstemplates;

import marinesmud.world.beings.Being;
import marinesmud.system.enities.StringClassTuple;
import marinesmud.world.area.room.Room;

/**
 *
 * @author jblew
 */
public enum ProgTemplate {

    global(new StringClassTuple[] {}, ""),
    room(new StringClassTuple[] {new StringClassTuple("room", Room.class)}, "Room room"),
    room_being(new StringClassTuple[] {new StringClassTuple("room", Room.class), new StringClassTuple("being", Being.class)}, "Room room, Being being");
    
    private final StringClassTuple[] variablesMapping;
    private final String variableNamesAndTypes;

    private ProgTemplate(StringClassTuple[] variablesMapping_, String variableNamesAndTypes_) {
        variablesMapping = variablesMapping_;
        variableNamesAndTypes = variableNamesAndTypes_;
    }
    
    public StringClassTuple[] getVariablesMapping() {
        return variablesMapping;
    }

    public String getVariableNamesAndTypes() {
        return variableNamesAndTypes;
    }
}