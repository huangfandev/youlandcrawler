package com.youland.crawler.engine;

import java.util.Map;

import com.youland.crawler.PageTask;

public interface ILoaderEngine {

    default Integer getIntParam(String input, Integer defaultValue){
        Integer result = null;
        if(input==null){
            result = defaultValue;
        }else {
            try {
                result = Integer.parseInt(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * init engine
     * @param config map.
     */
    void init(Map<String,String> config);

    /**
     * fetch page
     * @param PageTask task.
     */
    void fetchPage(PageTask page);
    
    /**
     * main method
     * @return usage about engine.
     */
    String usage();

    /**
     * close opend resources (optional)
     * 
     */
    default void close(){}

}