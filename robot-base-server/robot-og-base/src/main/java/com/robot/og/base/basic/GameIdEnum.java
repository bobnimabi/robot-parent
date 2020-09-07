package com.robot.og.base.basic;

import com.robot.core.function.base.IGameIdEnum;

/**
 * Created by mrt on 11/14/2019 7:50 PM
 * 数据库中id  1~20内
 */
public enum GameIdEnum implements IGameIdEnum {

    CPCP(2,"传统彩票"),
    BBSX(3,"BB视讯"),
    BBCP(5,"BB彩票"),
    BBJL(6,"BB电子"),
    AGSX(7,"AG视讯"),
    AGBY(8,"AG捕鱼"),
    AGJL(9,"AG电子"),
    OGSX(10,"OG视讯"),
    NMGJL(11,"MG电子"),
    PTJL(13,"PT电子"),
    MWGJL(14,"MWG电子"),
    LEBOSX(15,"LEBO视讯"),
    ABSX(16,"AB视讯"),
    PPJL(17,"PP电子"),
    VGQP(19,"VG棋牌"),
    CQ9JL(20,"CQ9电子"),
    BGSX(23,"BG视讯"),
    BGBY(24,"BG捕鱼"),
    BGJL(25,"BG电子"),
    PNGJL(26,"PNG电子"),
    JDBJL(27,"JDB电子"),
    FGQP(28,"FG棋牌"),
    FGBY(29,"FG捕鱼"),
    FGJL(30,"FG电子"),
    KYQP(49,"KY棋牌"),
    NWQP(50,"NW棋牌"),
    DTQP(51,"DT棋牌"),
    DTJL(52,"DT电子"),
    IBCTY(53,"沙巴体育"),
    BCTY(54,"OG体育"),
    BBTY(55,"BB体育"),
    CMDTY(56,"CMD体育"),
    LGQP(10000,"LG棋牌"),
    SCSX(10001,"性感百家乐"),
    WMJL(10002,"WM电子"),
    JDBQP(10003,"JDB棋牌"),
    JDBBY(10004,"JDB捕鱼"),
    MWGQP(10005,"MWG棋牌"),
    BSPQP(10006,"BSP棋牌"),
    BSPBY(10007,"BSP捕鱼"),
    PGJL(10008,"PG电子"),
    SGJL(10009,"SG电子"),
    EBETSX(100010,"EBET视讯"),
    MGPJL(100011,"新MG电子"),
    SCGQP(100020,"SCG棋牌"),
    SCGBY(100021,"SCG捕鱼"),
    SCGJL(100022,"SCG电子"),
    YOPLAYJL(100023,"YOPLAY电子"),
    THQP(100024,"TH棋牌"),
    THBY(100025,"TH捕鱼"),
    THJL(100026,"TH电子"),
    OGPSX(100027,"新OG视讯"),
    SBSX(100029,"新OG视讯"),
    FYDJ(100028,"FY电竞")

    ;
    private final Integer gameId;
    private final String Plat;


    private GameIdEnum( int gameId,String Plat) {
        this.Plat = Plat;
        this.gameId = gameId;
    }



    @Override
    public Integer getGameId() {
        return this.gameId;
    }
}
