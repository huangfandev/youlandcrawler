package com.youland.crawler.engine;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.youland.crawler.PageTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpLoaderEngine implements ILoaderEngine{
	private static final Logger logger = LoggerFactory.getLogger(OkHttpLoaderEngine.class);

    private static OkHttpClient httpClient;

	@Override
	public void init(Map<String, String> config) {
		// TODO Auto-generated method stub
        

        synchronized(OkHttpClient.class){
            if(httpClient==null){
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
                
                Proxy proxy = null;
                if(proxyHost!=null){
                    proxy = new Proxy(Proxy.Type.SOCKS, InetSocketAddress.createUnresolved(proxyHost, proxyPort));
                }
                httpClient = new OkHttpClient.Builder()
                    .proxy(proxy)
                    // .retryOnConnectionFailure(true)
                    .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();
                }
        }
		
	}

	@Override
	public void fetchPage(PageTask page) {
		// TODO Auto-generated method stub
        
        Request request = new Request.Builder()
            .url(page.getUrl())
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
            .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.146 Safari/537.36")
            .get().build();
        try (Response response = httpClient.newCall(request).execute()) {
            if(response.isSuccessful()){
                page.setContent(response.body().string());
                page.setStatus(PageTask.PageStatus.LOAD_SUCCESS);
            }else{
                page.setStatus(PageTask.PageStatus.LOAD_ERROR);
            }
        } catch (Exception e) {
            //TODO: handle exception
            page.setStatus(PageTask.PageStatus.LOAD_ERROR);
        }
    }

	@Override
	public String usage() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("<OkHttpLoaderEngine> OKHttp engine, java http request lib.\n");
		sb.append("    extra parameters:\n");
		sb.append("    -Dtimeout    connect timeout ,default 10000ms\n");
		sb.append("    -DproxyHost\n");
		sb.append("    -DproxyPort  deafult 8080");
		return sb.toString();
	}
    
}