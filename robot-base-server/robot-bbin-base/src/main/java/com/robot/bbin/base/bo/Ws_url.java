/**
  * Copyright 2020 bejson.com 
  */
package com.robot.bbin.base.bo;

/**
 * Auto-generated: 2020-11-18 18:26:39
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Ws_url {

    private String domain;
    private String port;
    private boolean hasSSL;
    public void setDomain(String domain) {
         this.domain = domain;
     }
     public String getDomain() {
         return domain;
     }

    public void setPort(String port) {
         this.port = port;
     }
     public String getPort() {
         return port;
     }

    public void setHasSSL(boolean hasSSL) {
         this.hasSSL = hasSSL;
     }
     public boolean getHasSSL() {
         return hasSSL;
     }

}