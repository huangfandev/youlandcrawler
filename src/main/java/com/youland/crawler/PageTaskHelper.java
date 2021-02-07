package com.youland.crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * load task from file and save result to file
 */
public class PageTaskHelper {  
    private static final Logger logger = LoggerFactory.getLogger(PageTaskHelper.class);
    private Config config;
    private Thread readThread;
    private Thread writeThread;
    private Parser parser;
    
    public PageTaskHelper(Config config){
        this.config=config;
        this.parser = new Parser(config);
        this.loading(config.getInputFile());
        this.saving(config.getOutputFile());
    }
    
    /**
     * loading PageTask form input file
     * 
     */
    public void loading(String filePath) {
        this.readThread = new Thread( () -> {
            String str;
            try (
                BufferedReader in = new BufferedReader(new FileReader(filePath));
            ){
                while ((str = in.readLine()) != null) {
                    PageTask page = parser.createPage(str);
                    if(page!=null){
                        PageContainer.putPageTask(page);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Config.stop();
            } catch (InterruptedException e1){
                e1.printStackTrace();
            }
            logger.info("========read file end==============");
        });
        this.readThread.start();
    }

    
    /**
     * get page form queue and match terms, save rsult to result file.
     * 
     */
    public void saving(String filePath){
        this.writeThread=new Thread( () -> {
            try (
                BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
            ){
                out.write("-----------search for-----------"+config.getSearchTerm()+"\n\n");
                while(Config.isStart()){
                    PageTask page = null;
                    try {
                        page = PageContainer.takePageResult();
                        if(page!=null){
                            parser.search(page);
                            out.write(page.toString()+"\n");
                            out.flush();
                        }
                    } catch (InterruptedException e) {
                        logger.info("---------write thread interrupted-----");
                    }
                    if(!this.readThread.isAlive()&&PageContainer.isEmpty()){
                        logger.info("------write  finished ------");
                        Config.stop();
                    }
                }
            } catch (IOException e) {
                logger.info("------save result error----");
                Config.stop();
            }
            
        });
        this.writeThread.start();
    }

    /**
     * stop write and read thread
     * 
     */
    public void stop(){
        if(this.readThread.isAlive()){
            this.readThread.interrupt();
        }
        if(this.writeThread.isAlive()){
            this.writeThread.interrupt();
        }
    }

}