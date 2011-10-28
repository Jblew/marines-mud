/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.system.threadmanagers.tickers;

import java.util.logging.Level;
import java.util.logging.Logger;
import marinesmud.lib.time.MudCalendar;
import marinesmud.lib.time.MudDate;
import marinesmud.lib.time.PartOfDay;
import marinesmud.world.World;
import marinesmud.world.communication.MessageToEverybody;
import pl.jblew.code.jutils.utils.RandomUtils;

/**
 *
 *  * @author jblew  * @license Kod jest objęty licencją zawartą w pliku LICESNE
 */
public class DayNight implements Runnable {

	private int lastHour = 0;
        private PartOfDay lastPartOfDay = null;

	public DayNight() {
	}

	public void run() {
		//Thread.currentThread().setName("DayNight-thread-"+IdGenerator.generate());
                MudDate time = MudCalendar.getInstance().getDateTime();

		int hour = time.getHour();

		if (hour >= 0 && lastHour < 0) {
			Logger.getLogger("").log(Level.INFO, "NEW DAY: {0}", time.getStringTime());
		}

                PartOfDay partOfDay = time.getPartOfDay();

                if(lastPartOfDay != null && partOfDay != lastPartOfDay) {
                    //System.out.println("POD: "+partOfDay+"("+partOfDay.name+"); LPOD: "+lastPartOfDay+"("+lastPartOfDay.name+")");
                    World.getInstance().sendMessage(new MessageToEverybody(RandomUtils.chooseStringRandomly(partOfDay.messages)));
                }

		lastHour = hour;
                lastPartOfDay = partOfDay;
	}
}
