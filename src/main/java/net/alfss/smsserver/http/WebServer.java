package net.alfss.smsserver.http;

import net.alfss.smsserver.config.GlobalConfig;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebServer
{
	private static final String WEB_XML = "META-INF/webapp/WEB-INF/web.xml";
    private static final String CLASS_ONLY_AVAILABLE_IN_IDE = "net.alfss.IDE";
    private static final String PROJECT_RELATIVE_PATH_TO_WEBAPP = "src/main/java/META-INF/webapp";
    private GlobalConfig config;

    public static interface WebContext
    {
        public File getWarPath();
        public String getContextPath();
    }
    
    
    private Server server;
    
    public WebServer(GlobalConfig globalConfig)
    {
        this.config = globalConfig;
    }
    
    public void start() throws Exception
    {
        //thread pool
        QueuedThreadPool _threadPool = new QueuedThreadPool();
        _threadPool.setMinThreads(config.getJettyMinPool());
        _threadPool.setMaxThreads(config.getRedisMaxPool());


        //http settings
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setOutputBufferSize(config.getJettyOutputBufferSize());
        http_config.setRequestHeaderSize(config.getJettyRequestHeaderSize());
        http_config.setResponseHeaderSize(config.getJettyResponseHeaderSize());

        server = new Server(_threadPool);

        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(http_config));
        http.setPort(config.getJettyPort());
        http.setHost(config.getJettyAddress());

        server.addConnector(http);
        server.addConnector(http);
        server.setHandler(createHandlers());
        server.setStopAtShutdown(true);

        server.start();
    }
    
    public void join() throws InterruptedException
    {
        server.join();
    }
    
    public void stop() throws Exception
    {        
        server.stop();
    }
    
    private HandlerCollection createHandlers()
    {                
        WebAppContext _ctx = new WebAppContext();
        _ctx.setContextPath("/");

        
        if(isRunningInShadedJar())
        {          
            _ctx.setWar(getShadedWarUrl());
        }
        else
        {            
            _ctx.setWar(PROJECT_RELATIVE_PATH_TO_WEBAPP);
        }
        
        List<Handler> _handlers = new ArrayList<Handler>();
        
        _handlers.add(_ctx);
        
        HandlerList _contexts = new HandlerList();
        _contexts.setHandlers(_handlers.toArray(new Handler[0]));
        
        RequestLogHandler _log = new RequestLogHandler();
        _log.setRequestLog(createRequestLog());
        
        HandlerCollection _result = new HandlerCollection();
        _result.setHandlers(new Handler[] {_contexts, _log});
        
        return _result;
    }
    
    private RequestLog createRequestLog()
    {
        return new WebServerRequestLog();
    }
    
//---------------------------
// Discover the war path
//---------------------------   
    
    private boolean isRunningInShadedJar()
    {
        try
        {
            Class.forName(CLASS_ONLY_AVAILABLE_IN_IDE);
            return false;
        }
        catch(ClassNotFoundException anExc)
        {
            return true;
        }
    }
    
    private URL getResource(String aResource)
    {
        return Thread.currentThread().getContextClassLoader().getResource(aResource); 
    }
    
    private String getShadedWarUrl()
    {
        String _urlStr = getResource(WEB_XML).toString();
        // Strip off "WEB-INF/web.xml"
        return _urlStr.substring(0, _urlStr.length() - 15);
    }
}
