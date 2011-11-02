/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap;

import java.nio.channels.ClosedChannelException;
import marinesmud.lib.logging.Level;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import pl.jblew.code.globallogger.GlobalLogger;

/**
 *
 * @author jblew
 */
class TelnetHandler extends SimpleChannelUpstreamHandler {
    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        super.handleUpstream(ctx, e);
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        new TelnetAsProxyInstance(e.getChannel());
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        if (TelnetServer.getInstance().listeners.containsKey(e.getChannel())) {
            TelnetServer.getInstance().listeners.get(e.getChannel()).receivedLine(e.getChannel(), (String) e.getMessage());
        }
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        if (TelnetServer.getInstance().listeners.containsKey(e.getChannel())) {
            TelnetServer.getInstance().listeners.get(e.getChannel()).conectionClosed(e.getChannel());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        if (!(e.getCause() instanceof ClosedChannelException)) {
            GlobalLogger.getLogger("TelnetServer").log(Level.WARNING, "Unexpected exception from downstream.", e.getCause());
        }
        e.getChannel().close();
    }
}
