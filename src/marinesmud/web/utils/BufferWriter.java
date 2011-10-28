/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.web.utils;

/**
 *
 * @author jblew
 */
public class BufferWriter {
	private String buffer = "";

	public void println(String s) {
		buffer += s;
		buffer += "\n";
	}

	public void print(String s) {
		buffer += s;
	}

	public String getBuffer() {
		return buffer;
	}
}
