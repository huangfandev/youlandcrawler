package com.youland.crawler.engine;

import java.util.Map;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.youland.crawler.PageTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlUnitLoaderEngine implements ILoaderEngine{
	private static final Logger logger = LoggerFactory.getLogger(HtmlUnitLoaderEngine.class);
	private WebClient webclient = new WebClient(BrowserVersion.CHROME);

	@Override
	public void init(Map<String,String> config){
		String timeoutStr = null;
		String proxyHost =  null;
		String proxyPortStr = null;
		if(config!=null){
            timeoutStr = config.get("timeout");
			proxyHost = config.get("proxyHost");
			proxyPortStr = config.get("proxyPort");
        }
		Integer timeout = getIntParam(timeoutStr,10000);
		Integer proxyPort = getIntParam(proxyPortStr, 8080);
		webclient = new WebClient(BrowserVersion.CHROME);
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		
		WebClientOptions options = webclient.getOptions();

		if(proxyHost!=null){
			// logger.info("=========open proxy===="+proxyHost+":"+proxyPort);
			ProxyConfig proxyConfig = new ProxyConfig(proxyHost, proxyPort, false);
			options.setProxyConfig(proxyConfig);
		}
		
		options.setJavaScriptEnabled(true);
		options.setCssEnabled(false);
        // options.setRedirectEnabled(false);
        options.setUseInsecureSSL(true);
		options.setThrowExceptionOnScriptError(true);
        options.setTimeout(timeout);
	}

	@Override
	public void fetchPage(PageTask page) {
		HtmlPage p;
		try {
			p = webclient.getPage(page.getUrl());
			String content = p.asXml();
			page.setContent(content);
			page.setStatus(PageTask.PageStatus.LOAD_SUCCESS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			page.setStatus(PageTask.PageStatus.LOAD_ERROR);
		}
		
	}

	@Override
	public String usage() {
		StringBuilder sb = new StringBuilder();
		sb.append("<HtmlUnitLoaderEngine> HtmlUnit is GUI-Less browser for Java programs, support javascript.\n");
		sb.append("    extra parameters:\n");
		sb.append("    -Dtimeout    connect timeout ,default 10000ms\n");
		sb.append("    -DproxyHost\n");
		sb.append("    -DproxyPort  deafult 8080");
		return sb.toString();
	}

	@Override
	public void close() {
		webclient.close();
	}
    
}