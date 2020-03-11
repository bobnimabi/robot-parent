package com.robot.suxiao.base.vo;
import java.util.List;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Auto-generated: 2020-03-09 15:44:25
 *
 * @author http://www.itjson.com 
 * @website http://www.itjson.com/itjson/json2java.html 
 */
@Data
public class BankInfoVO {
    private List<Bankcardlist> bankCardList;
    private List<Drawtypesettings> drawTypeSettings;
    private List<String> weixinDrawAccountList;
    private M m;
}