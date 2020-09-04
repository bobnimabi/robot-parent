/**
  * Copyright 2020 bejson.com 
  */
package com.robot.bbin.base.bo.rebate;
import lombok.Data;

import java.util.List;

/**
 * Auto-generated: 2020-09-04 13:41:23
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class Rebate {

    private String index;
    private String agent;
    private String member;
    private List<Premium_data> premium_data;

}