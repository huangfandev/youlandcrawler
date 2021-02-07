package com.youland.crawler;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.beust.jcommander.JCommander;
import com.youland.crawler.engine.ILoaderEngine;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 */
public final class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    // private static String[] newArgs = {};

    private App() {
    }

    /**
     * main method
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        logger.info("=============start===============");
        Config config = setConfig(args);
        if(config==null){
            return ;
        }

        PageTaskHelper pageTaskHelper = new PageTaskHelper(config);
        PageLoader pageLoader = new PageLoader(config);
        while(Config.isStart()){
            logger.info(PageContainer.printStatis());
            try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        pageTaskHelper.stop();
        pageLoader.stop();
        logger.info("=============stop===============");
    }

    /**
     * set confiugration 
     * @param args The arguments of the program.
     */
    private static Config setConfig(String args[]){
        Config config = new Config();
        JCommander jc = JCommander.newBuilder().addObject(config).build();
        jc.setProgramName("java -jar <jarfile>");
        jc.parse(args);
        if(config.isHelp()){
            printUsage(jc);
            config = null;
        }
        
        return config;
    }


    /**
     * print usage , jcommander usage add all engine description
     * @param jcommander.
     */
    public static void printUsage(JCommander jc){
        StringBuilder engineDes = new StringBuilder();
        Reflections reflections = new Reflections("com.youland.crawler.engine");
        Set<Class<? extends ILoaderEngine>> allEngines =reflections.getSubTypesOf(ILoaderEngine.class);
        for(Class<? extends ILoaderEngine> engineClass: allEngines){
            try{
                ILoaderEngine engine = (ILoaderEngine)engineClass.newInstance();
                String description = engine.usage();
                if(description!=null){
                    engineDes.append("\n").append(description);
                }
            } catch(Exception e){
                logger.info("check engine error======"+engineClass.getName());
            }
        }

        StringBuilder sb = new StringBuilder();
        jc.getUsageFormatter().usage(sb);
        sb.append(engineDes);
        jc.getConsole().println(sb.toString());
    }

}
