/**
  * Copyright 2020 bejson.com 
  */
package com.robot.bbin.base.bo.slogin;

/**
 * Auto-generated: 2020-11-18 18:38:3
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class SecData {

    private SecUser user;
    private int login_result;
    private String session_id;
    private boolean password_reset;
    private boolean expire;
    public void setUser(SecUser user) {
         this.user = user;
     }
     public SecUser getUser() {
         return user;
     }

    public void setLogin_result(int login_result) {
         this.login_result = login_result;
     }
     public int getLogin_result() {
         return login_result;
     }

    public void setSession_id(String session_id) {
         this.session_id = session_id;
     }
     public String getSession_id() {
         return session_id;
     }

    public void setPassword_reset(boolean password_reset) {
         this.password_reset = password_reset;
     }
     public boolean getPassword_reset() {
         return password_reset;
     }

    public void setExpire(boolean expire) {
         this.expire = expire;
     }
     public boolean getExpire() {
         return expire;
     }

}