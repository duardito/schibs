package com.schibsted.server.beans.page;


public  class PageResponse  {

    public PageResponse(String pagename, String username) {
        this.pagename = pagename;
        this.username = username;
    }

    private String pagename;
    private String username;

    public String getPagename() {
        return pagename;
    }

    public void setPagename(String pagename) {
        this.pagename = pagename;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
