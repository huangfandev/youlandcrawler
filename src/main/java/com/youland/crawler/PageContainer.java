package com.youland.crawler;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * task and result queue container ,it makes a pipe lien
 */
public class PageContainer {
    private static AtomicInteger inTaskQNum = new AtomicInteger(0);
    private static AtomicInteger outTaskQNum = new AtomicInteger(0);
    private static AtomicInteger inResultQNum = new AtomicInteger(0);
    private static AtomicInteger outResultQNum = new AtomicInteger(0);
    private static LinkedBlockingQueue<PageTask> pageTaskQueue = new LinkedBlockingQueue<>(100);
    private static LinkedBlockingQueue<PageTask> pageResultQueue = new LinkedBlockingQueue<>(100);

    private PageContainer(){}

    /**
     * put task to task queue
     * 
     */
    public static void putPageTask(PageTask page) throws InterruptedException{
        pageTaskQueue.put(page);
        inTaskQNum.incrementAndGet();
    }

     /**
     * get task from task queue
     * 
     */
    public static PageTask takePageTask() throws InterruptedException{
        PageTask page = pageTaskQueue.poll(3, TimeUnit.SECONDS);
        if(page!=null){
            outTaskQNum.incrementAndGet();
        }
        return page;
    }

    /**
     * put result to result queue
     * 
     */
    public static void putPageResult(PageTask page) throws InterruptedException{
        pageResultQueue.put(page);
        inResultQNum.incrementAndGet();
    }

    /**
     * get result from result queue
     * 
     */
    public static PageTask takePageResult() throws InterruptedException{
        PageTask page = pageResultQueue.poll(3,TimeUnit.SECONDS);
        if(page!=null){
            outResultQNum.incrementAndGet();
        }
        return page;
    }

    /**
     * check if the pipe line is empty;
     * 
     */
    public static boolean isEmpty(){
        return inTaskQNum.get()==outResultQNum.get();
    }

    /**
     * print the statistics of queues;
     * 
     */
    public static String printStatis(){
        StringBuilder sb = new StringBuilder();
        sb.append("==InTaskQNum==");
        sb.append(inTaskQNum);
        sb.append("==OutTaskQNum==");
        sb.append(outTaskQNum);
        sb.append("==InResultQNum==");
        sb.append(inResultQNum);
        sb.append("==OutResultQNum==");
        sb.append(outResultQNum);
        return sb.toString();
    }

}