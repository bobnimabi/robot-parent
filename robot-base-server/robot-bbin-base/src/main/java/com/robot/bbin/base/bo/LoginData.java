/**
  * Copyright 2020 bejson.com 
  */
package com.robot.bbin.base.bo;

import lombok.Data;

/**
 * Auto-generated: 2020-11-18 18:26:39
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class LoginData {

    private LoginUser user;
    private int login_result;
    private String session_id;
    private boolean password_reset;
    private boolean expire;
    private Ws_url ws_url;
    private String device_id;
    private String token;


}