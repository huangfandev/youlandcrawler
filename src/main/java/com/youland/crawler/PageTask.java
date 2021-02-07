package com.youland.crawler;

public class PageTask {
    private long id;
    private String url;
    private String content;
    private PageStatus status=PageStatus.INIT;

    public PageTask(long id,String url){
        this.url=url;
        this.id=id;
    }

    public enum PageStatus {
        INIT,LOAD_ERROR,LOAD_SUCCESS,MISSMATCHED,MATCHED;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append(",");
        sb.append(url);
        sb.append(",");
        sb.append(status);
        return sb.toString();
    }

    /**
     * @return long return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return String return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return String return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return PageStatus return the status
     */
    public PageStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(PageStatus status) {
        this.status = status;
    }

}