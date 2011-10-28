/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.tap.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 *  * @author jblew  * @license Kod jest objęty licencją zawartą w pliku LICESNE
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestPositionDisallowed {
	public String message() default "Nie możesz tego zrobić odpoczywając!";
}
