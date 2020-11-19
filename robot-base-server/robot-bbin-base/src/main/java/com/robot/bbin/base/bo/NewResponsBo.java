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
public class NewResponsBo {

    private boolean result;
    private LoginData data;
    private String response_code;
    public void setResult(boolean result) {
         this.result = result;
     }
     public boolean getResult() {
         return result;
     }

    public void setData(LoginData data) {
         this.data = data;
     }
     public LoginData getData() {
         return data;
     }

    public void setResponse_code(String response_code) {
         this.response_code = response_code;
     }
     public String getResponse_code() {
         return response_code;
     }

}