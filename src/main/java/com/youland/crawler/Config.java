package com.youland.crawler;

import java.util.HashMap;
import java.util.Map;

import com.beust.jcommander.DynamicParameter;
import com.beust.jcommander.Parameter;

/**
 * all configuration form command line
 */
public class Config {
    private static volatile boolean start = true;

    @Parameter(names="-s",description = "search term",required = true, order = 2)
    private String searchTerm;

    @Parameter(names="-e",description = "loading engine, the options are as follows", order = 6)
    private String engine = "OkHttpLoaderEngine";

    @Parameter(names="-t",description = "working thread number", order = 4)
    private int workingThreadNum = 20;

    @Parameter(description = "inputFile",required = true)
    private String inputFile;
    
    @Parameter(names="-of", description = "output file", order = 3)
    private String outputFile = "result.txt";

    @DynamicParameter(names="-D",description = "engine extra config", order = 5)
    private Map<String,String> engineConfigs = new HashMap<>();

    @Parameter(names = "-help", description = "show usage", help = true, order = 1)
    private boolean help;
    
    /**
     * check the crawler status
     * 
     */
    public static boolean isStart(){
        return start;
    }

    /**
     * stop the crawler
     * 
     */
    public static void stop(){
        start = false;
    }

    public boolean isHelp(){
        return help;
    }
    
    /**
     * @return String return the searchTerm
     */
    public String getSearchTerm() {
        return searchTerm;
    }

    /**
     * @return String return the engine
     */
    public String getEngine() {
        return engine;
    }

    /**
     * @return int return the workingThreadNum
     */
    public int getWorkingThreadNum() {
        return workingThreadNum;
    }

    /**
     * @return String return the inputFile
     */
    public String getInputFile() {
        return inputFile;
    }

    /**
     * @return String return the outputFile
     */
    public String getOutputFile() {
        return outputFile;
    }

    /**
     * @return Map<String,String> return the engineConfigs
     */
    public Map<String,String> getEngineConfigs() {
        return engineConfigs;
    }


}