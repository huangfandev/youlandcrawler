package com.youland.crawler;

import java.util.ArrayList;
import java.util.List;
import com.youland.crawler.engine.ILoaderEngine;
import com.youland.crawler.engine.OkHttpLoaderEngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * master of worker thread fixed thread number.
 */
public class PageLoader {
    private static final Logger logger = LoggerFactory.getLogger(PageLoader.class);
    
    private List<Thread> threadList = new ArrayList<>();

    public PageLoader(Config config){
        for(int i=0;i<config.getWorkingThreadNum();i++){
            addWorker(config);
        }
    }

    private void addWorker(Config config){
        Thread thread = new Thread(new Worker(config)){};
        threadList.add(thread);
        thread.start();
    }

    /**
     * stop all workers
     * 
     */
    public void stop() {
        for(Thread thread:threadList){
            if(thread.isAlive()){
                thread.interrupt();
            }
        }
    }

    /**
     * worker class
     * 
     */
    private static class Worker implements Runnable {
        private ILoaderEngine engine;

        public Worker(Config config){
            try{
                Class clazz = Class.forName("com.youland.crawler.engine."+config.getEngine());
                engine = (ILoaderEngine) clazz.newInstance();
            }catch(Exception e){
                logger.info("engine <{}> init faild, use default engine ", config.getEngine());
                if(engine==null){
                    engine= new OkHttpLoaderEngine();
                }
            }
            engine.init(config.getEngineConfigs());
        }

		@Override
		public void run() {
            while(Config.isStart()){
                PageTask page = null;
				try {
                    page = PageContainer.takePageTask();
                    if(page!=null){
                        engine.fetchPage(page);
                        PageContainer.putPageResult(page);
                    }
				} catch (InterruptedException e) {
                    logger.info("=========worker interruptd==========");
				} catch (Exception e) {
                    e.printStackTrace();
                    if(page!=null&&page.getStatus()==PageTask.PageStatus.INIT){
                        page.setStatus(PageTask.PageStatus.LOAD_ERROR);
                        try {
							PageContainer.putPageResult(page);
						} catch (InterruptedException e1) {
							logger.info("=========worker interruptd==========");
						}
                    }
				}
            }
            engine.close();
		}
    }
}