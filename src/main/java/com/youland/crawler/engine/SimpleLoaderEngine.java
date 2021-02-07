package com.youland.crawler.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Map;

import com.youland.crawler.PageTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleLoaderEngine implements ILoaderEngine{
    private static final Logger logger = LoggerFactory.getLogger(SimpleLoaderEngine.class);
    Proxy proxy = null;

	@Override
	public void init(Map<String, String> config) {
        String proxyHost =  null;
        String proxyPortStr = null;
        if(config!=null){
            proxyHost = config.get("proxyHost");
            proxyPortStr = config.get("proxyPort");
        }
        Integer proxyPort = getIntParam(proxyPortStr, 8080);
        if(proxyHost!=null){
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        }
    }

	@Override
	public void fetchPage(PageTask page) {
        BufferedReader bufferedReader = null;
        try{
            URL httpUrl = new URL(page.getUrl());
            HttpURLConnection httpURLConnection = null;
            if(proxy!=null){
                httpURLConnection = (HttpURLConnection) httpUrl.openConnection(proxy);
            }else{
                httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
            }
            httpURLConnection.setRequestProperty("User-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.146 Safari/537.36");
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            // logger.info("=======response======"+httpURLConnection.getResponseCode());
            if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK) {
                page.setStatus(PageTask.PageStatus.LOAD_SUCCESS);
                page.setContent(sb.toString());
            }else{
                page.setStatus(PageTask.PageStatus.LOAD_ERROR);
            }
        }catch(Exception e){
            page.setStatus(PageTask.PageStatus.LOAD_ERROR);
        }finally{
            if(bufferedReader!=null){
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
                }
            }
        }
    }

	@Override
	public String usage() {
        StringBuilder sb = new StringBuilder();
        sb.append("<SimpleLoaderEngine> default engine, use HttpURLConnection.\n");
		sb.append("    extra parameters:\n");
		sb.append("    -DproxyHost\n");
		sb.append("    -DproxyPort  deafult 8080");
        return sb.toString();
	}
}