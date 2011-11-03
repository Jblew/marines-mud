/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.web;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import marinesmud.web.servlets.Servlets;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 *
 * @author jblew
 */
public class WebServer {
    private final Server server;
    private InetSocketAddress addr;

    private WebServer() {
        server = new Server();
    }

    public InetSocketAddress getAddress() {
        return addr;
    }

    public void bind(InetSocketAddress addr) {
        this.addr = addr;
        SocketConnector connector = new SocketConnector();

        // Set some timeout options to make debugging easier.
        //connector.setMaxIdleTime(1000 * 60 * 60);
        //connector.setSoLingerTime(-1);
        connector.setPort(addr.getPort());
        connector.setHost(addr.getHostName());

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("./resources/");

         ContextHandler resourceContext = new ContextHandler();
        resourceContext.setContextPath("/resources");
        resourceContext.setResourceBase(".");
        resourceContext.setClassLoader(Thread.currentThread().getContextClassLoader());
        resourceContext.setHandler(resource_handler);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.setContextPath("/");
        server.setHandler(context);
        for (String path : Servlets.servlets.keySet()) {
            context.addServlet(Servlets.servlets.get(path), path);
        }

        HandlerList handlers = new HandlerList();

        handlers.setHandlers(new Handler[]{resourceContext, context});
        server.setHandler(handlers);

        server.setConnectors(new Connector[]{connector});
        Logger.getLogger("WebServer").log(Level.INFO, "Binded webServer to port {0}", addr.getPort());
    }

    public void start() {
        try {
            server.start();
        } catch (Exception ex) {
            Logger.getLogger("WebServer").log(Level.SEVERE, null, ex);
        }
    }

    public void destroy() {
        try {
            server.stop();
        } catch (Exception ex) {
            Logger.getLogger("WebServer").log(Level.SEVERE, null, ex);
        }
    }

    public static WebServer getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        public static final WebServer INSTANCE = new WebServer();
    }
}

/*
package marinesmud.web;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class WebServer {
private final ExecutorService executorService = Executors.newCachedThreadPool();
private final ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(executorService, executorService));;

private WebServer() {
bootstrap.setPipelineFactory(new HttpServerPipelineFactory());
}

public void bind(InetSocketAddress addr) {
bootstrap.bind(addr);
}

public void start() {
}

public void destroy() {
bootstrap.releaseExternalResources();
}

public static WebServer getInstance() {
return InstanceHolder.INSTANCE;
}

private static class InstanceHolder {
public static final WebServer INSTANCE = new WebServer();
}
}

 */
