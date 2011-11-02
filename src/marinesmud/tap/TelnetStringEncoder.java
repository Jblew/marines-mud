/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.tap;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.string.StringEncoder;

/**
 *
 * @author jblew
 */
class TelnetStringEncoder extends StringEncoder {

    public TelnetStringEncoder() {
    }

    @Override
    public Object encode(ChannelHandlerContext ctx, Channel channel, Object msg_) throws Exception {
        if(!(msg_ instanceof String)) throw new UnsupportedOperationException("TelnetStringEncoder can only encode Strings.");

        String msg = (String) msg_;

        return super.encode(ctx, channel, Color.colorify(msg));
    }
}
