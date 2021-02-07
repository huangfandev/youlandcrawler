package com.youland.crawler;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parser { 
    private static final Logger logger = LoggerFactory.getLogger(Parser.class);

    private final Pattern searchPattern ;

    public Parser(Config config){
        this.searchPattern = Pattern.compile("[\\s\\S]*"+config.getSearchTerm()+"[\\s\\S]*", Pattern.CASE_INSENSITIVE);
    }

    /**
    * create PageTask form line, return null if have wrone format
    */
    public PageTask createPage(String line){
        PageTask result = null;
        if(line!=null){
            String[] cols = line.split(",");
            if(cols.length==6) {
                Integer id = null;
                try {
                    id = Integer.parseInt(cols[0]);
                } catch (Exception e) {
                    logger.info("===number format error===<{}>", cols[0]);
                }
                if(id!=null){
                    String url = "http://"+cols[1].replaceAll("^\"|\"$","");
                    result = new PageTask(id,url);
                }
            }
        }
        return result;
    }

    /**
    * search term form content
    */
    public void search(PageTask page){
        if(page!=null&&page.getStatus()==PageTask.PageStatus.LOAD_SUCCESS){
            String content = page.getContent();
            boolean matched = searchPattern.matcher(content).matches();
            if(matched){
                page.setStatus(PageTask.PageStatus.MATCHED);
            }else{
                page.setStatus(PageTask.PageStatus.MISSMATCHED);
            }
        }
    }
}