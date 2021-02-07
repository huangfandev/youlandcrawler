package com.youland.crawler;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import com.beust.jcommander.JCommander;
import com.youland.crawler.engine.HtmlUnitLoaderEngine;
import com.youland.crawler.engine.OkHttpLoaderEngine;
import com.youland.crawler.engine.ILoaderEngine;
import com.youland.crawler.engine.SimpleLoaderEngine;

/**
 * Unit test for simple App.
 */
class AppTest {
    private static Config config;

    @BeforeAll
    static void initConfig(){
        config = new Config();
        JCommander jc = JCommander.newBuilder().addObject(config).build();
        jc.parse("-s","google","inptufile.txt");
    }



    @Test
    void testCreatePage(){
        String line = "3,\"google.com/\",5868081,807173458,9.30,9.27";
        Parser parser = new Parser(config);
        PageTask page = parser.createPage(line);
        assertEquals(3, page.getId());
    }

    @Test
    void testSearch(){
        Parser parser = new Parser(config);
        PageTask page = new PageTask(1, "www.google.com");
        page.setContent("sicmde,sigoleakis \n ssasfgsGooglesoem");
        page.setStatus(PageTask.PageStatus.LOAD_SUCCESS);
        parser.search(page);
        assertEquals(PageTask.PageStatus.MATCHED,page.getStatus());
    }

    @Test
    void testCommandArgs(){
        Config c = new Config();
        JCommander jc = JCommander.newBuilder().addObject(c).build();        
        jc.setProgramName("java -jar <jarfile>");
        // jc.parse("-help","","-s","something","inptufile.txt");
        jc.parse("-help");
        com.youland.crawler.App.printUsage(jc);
        assertEquals(true,c.isHelp());
    }

    // @Test
    // void testOkHttpLoaderEngine(){
    //     ILoaderEngine engine = new OkHttpLoaderEngine();
    //     PageTask page = new PageTask(1, "http://www.google.com");
    //     Map<String,String> econfig = new HashMap<>();
    //     econfig.put("proxyHost", "localhost");
    //     econfig.put("proxyPort", "7890");
    //     engine.init(econfig);
    //     engine.fetchPage(page);
    //     engine.close();
    //     assertEquals(PageTask.PageStatus.LOAD_SUCCESS,page.getStatus());
    // }

    // @Test
    // void testSimpleLoaderEngine(){
    //     ILoaderEngine engine = new SimpleLoaderEngine();
    //     PageTask page = new PageTask(1, "https://www.google.com");
    //     Map<String,String> econfig = new HashMap<>();
    //     econfig.put("proxyHost", "localhost");
    //     econfig.put("proxyPort", "7890");
    //     engine.init(econfig);
    //     engine.fetchPage(page);
    //     engine.close();
    //     assertEquals(PageTask.PageStatus.LOAD_SUCCESS,page.getStatus());
    // }

    // @Test
    // void testHtmlUnitLoaderEngine(){
    //     HtmlUnitLoaderEngine engine = new HtmlUnitLoaderEngine();
    //     PageTask page = new PageTask(1, "http://www.google.com");
    //     Map<String,String> econfig = new HashMap<>();
    //     econfig.put("proxyHost", "localhost");
    //     econfig.put("proxyPort", "7890");
    //     engine.init(econfig);
    //     engine.fetchPage(page);
    //     engine.close();
    //     assertEquals(PageTask.PageStatus.LOAD_SUCCESS,page.getStatus());
    // }

}
