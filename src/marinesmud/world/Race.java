/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world;

import marinesmud.lib.Dictionary;
import marinesmud.lib.MudEncoder;

/**
 * @author jblew
 */
public enum Race {
    HUMAN(MudEncoder.Manager.EMPTY.getEncoder(), Dictionary.getDictionary("human"), 4f, 4f),
    ELF(MudEncoder.Manager.ELF.getEncoder(), Dictionary.getDictionary("elf"), 3f, 6f),
    DWARF(MudEncoder.Manager.DWARF.getEncoder(), Dictionary.getDictionary("dwarf"), 6f, 3f);

    private final MudEncoder encoder;
    private final Dictionary dictionary;
    private final float healthConst;
    private final float movementConst;

    private Race(MudEncoder encoder, Dictionary dictionary, float healthConst, float movementConst) {
        this.encoder = encoder;
        this.dictionary = dictionary;
        this.healthConst = healthConst;
        this.movementConst = movementConst;
    }

    public MudEncoder getEncoder() {
        return encoder;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public float getHealthConst() {
        return healthConst;
    }

    public float getMovementConst() {
        return movementConst;
    }

    public static Race getDefault() {
        return HUMAN;
    }
}
