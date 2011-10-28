/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.lib.time;

import java.util.Date;

/**
 *
 * @author jblew
 */
public class Era {
    public final String name;
    public final String dopelniacz;

    //public final MudDate mudDateStart;
    //public final MudDate mudDateEnd;

    public final Date start;
    public final Date end;


    public Era(String name, String dopelniacz, Date start, Date end) {
        this.name = name;
        this.dopelniacz = dopelniacz;
        this.start = start;
        this.end = end;
    }
}
