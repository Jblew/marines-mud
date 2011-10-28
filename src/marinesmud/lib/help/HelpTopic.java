/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.lib.help;

/**
 *
 * @author jblew
 */
public class HelpTopic {
	private final String name;
	private final String content;

	public HelpTopic(String name_, String content_) {
		name = name_;
		content = content_;
	}

	public String getName() {
		return name;
	}

	public String getContent() {
		return content;
	}

	public String getKeywords() {
		if(content.isEmpty() && name.isEmpty()) {
			return "";
		}
		if(!content.isEmpty() && name.isEmpty()) {
			return content;
		}
		if(content.isEmpty() && !name.isEmpty()) {
			return name;
		}
		return (content.trim()+" "+name.trim()).trim();
	}
}
