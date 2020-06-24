package com.robot.jiuwu.base.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by mrt on 2019/12/31 0031 13:30
 */
@Data
public class InfoData {
    @JSONField(name="ServiceRight")
    private Integer ServiceRight;
    @JSONField(name="UserRight")
    private Integer UserRight;
    @JSONField(name="LastLogonMobile")
    private String LastLogonMobile;
    @JSONField(name="LogonPass")
    private String LogonPass;
    @JSONField(name="SpreaderID")
    private Integer SpreaderID;
    @JSONField(name="Gender")
    private Integer Gender;
    @JSONField(name="CustomFaceVer")
    private Integer CustomFaceVer;
    @JSONField(name="RegisterMachine")
    private String RegisterMachine;
    @JSONField(name="CustomID")
    private String CustomID;
    @JSONField(name="PlayTimeCount")
    private String PlayTimeCount;
    @JSONField(name="UnderWrite")
    private String UnderWrite;
    @JSONField(name="MemberOverDate")
    private String MemberOverDate;
    @JSONField(name="UserUin")
    private String UserUin;
    @JSONField(name="GameID")
    private Integer GameID;
    @JSONField(name="MoorMachine")
    private Integer MoorMachine;
    @JSONField(name="FaceID")
    private Integer FaceID;
    @JSONField(name="WebLogonTimes")
    private Integer WebLogonTimes;
    @JSONField(name="PassPortID")
    private String PassPortID;
    @JSONField(name="NickName")
    private String NickName;
    @JSONField(name="DynamicPass")
    private String DynamicPass;
    private String checkCode;
    @JSONField(name="Compellation")
    private String Compellation;
    @JSONField(name="MemberOrder")
    private Integer MemberOrder;
    @JSONField(name="StunDown")
    private Integer StunDown;
    @JSONField(name="PlatformID")
    private Integer PlatformID;
    @JSONField(name="AgentID")
    private Integer AgentID;
    @JSONField(name="MasterOrder")
    private Integer MasterOrder;
    @JSONField(name="LastLogonDate")
    private String LastLogonDate;
    @JSONField(name="DynamicPassTime")
    private String DynamicPassTime;
    @JSONField(name="RegAccounts")
    private String RegAccounts;
    @JSONField(name="LastLogonMachine")
    private String LastLogonMachine;
    @JSONField(name="NullityReasonID")
    private Integer NullityReasonID;
    @JSONField(name="RegisterMobile")
    private String RegisterMobile;
    @JSONField(name="MasterRight")
    private Integer MasterRight;
    @JSONField(name="IsAndroid")
    private Integer IsAndroid;
    @JSONField(name="Source")
    private String Source;
    @JSONField(name="NullityOverDate")
    private String NullityOverDate;
    @JSONField(name="PlaceName")
    private String PlaceName;
    @JSONField(name="MemberSwitchDate")
    private String MemberSwitchDate;
    @JSONField(name="UserID")
    private String UserID;
    @JSONField(name="Nullity")
    private Integer Nullity;
    @JSONField(name="LastLogonIP")
    private String LastLogonIP;
    @JSONField(name="GameLogonTimes")
    private Integer GameLogonTimes;
    @JSONField(name="RegisterDate")
    private String RegisterDate;
    @JSONField(name="RegisterOrigin")
    private Integer RegisterOrigin;
    @JSONField(name="IsChaneSource")
    private Integer IsChaneSource;
    @JSONField(name="RegisterIP")
    private String RegisterIP;
    @JSONField(name="OnLineTimeCount")
    private Integer OnLineTimeCount;
    @JSONField(name="InsurePass")
    private String InsurePass;
    @JSONField(name="Accounts")
    private String Accounts;
}
