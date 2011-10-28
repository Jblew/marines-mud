/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.system.bootstrap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import marinesmud.system.shutdown.MudShutdown;

import pl.jblew.code.jutils.utils.FileUtils;

/**
 *
 * @author jblew
 */
public class License {

    public static final String LICENSE = "UWAGA! Kod, struktura bazy danych i wszystkie pliki zawarte w katalogu głównym projektu i podkatalogach, oprócz wartości zmiennych w plikach konfiguracyjnych są objęte licencją MIT (X11). Każdy ma nieograniczone prawo do używania, kopiowania, modyfikowania i rozpowszechniania (w tym sprzedaży) oryginalnego lub zmodyfikowanego programu w postaci binarnej lub źródłowej. Jedynym wymaganiem jest, by we wszystkich wersjach zachowano warunki licencyjne i informacje o autorach. Autorem kodu jest JBLew (http://www.jblew.pl/). Szefem projektu oraz nadzorcą jest Progtryk. Struktura bazy danych została zaprojektowana przez JBLew-a. Uwaga! Licencja ta dotyczy produktu MarinesMUD4 i nie jest związana z BlazeMUD, który jest oparty na MarinesMUD4. Uwaga! Autorzy, czyli Jblew(http://www.jblew.pl/) i Progtryk zastrzegają sobie prawo do zmiany treści tej licencji, jak i dowolnych warunków licencyjnych dotyczących dowolnych części MarinesMUD4, bez pytania innych osób o zgodę.";

    private License() {
    }

    public static void checkLicense() {
        try {
            String licenseInFile = FileUtils.getFileContents("./LICENSE").trim();
            if (!licenseInFile.equals(LICENSE)) {
                Logger.getLogger("License").log(Level.SEVERE, "WARNING! License mismatch. Did someone changed the license file!?");
                Logger.getLogger("License").log(Level.INFO, "Original license: \n'{0}'; License from LICENSE file: \n'{1}'", new Object [] {LICENSE, licenseInFile});
                MudShutdown.panicShutdown();
            } else {
                Logger.getLogger("License").log(Level.FINE, "License is ok.");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger("License").log(Level.SEVERE, "WARNING! License file not found. Exiting.");
            MudShutdown.panicShutdown();
        } catch (IOException ex) {
            Logger.getLogger("License").log(Level.SEVERE, "WARNING! Cannot open license file: {0}. Exiting.", ex.getMessage());
            MudShutdown.panicShutdown();
        }
    }
}
