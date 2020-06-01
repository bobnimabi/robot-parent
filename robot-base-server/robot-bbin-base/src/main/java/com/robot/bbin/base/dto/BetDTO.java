package com.robot.bbin.base.dto;

import lombok.Data;

/**
 * Created by mrt on 2020/5/12 19:08
 */
@Data
public class BetDTO {

    // 日期：起始
    private String start;
    // 日期：结束
    private String end;
    // 产品：详细见下面的注释
    private String game = "0";
    // 币别： 人民币：RMB 试玩币：UUS
    private String currency = "RMB";
    // 游戏种类：ALL：全部  注意：这项没有在页面上显示，参数写死带上，就这一个值
    private String gametype = "ALL";
    // 会员名称
    private String name;
    // 账号查询 代理：agent 会员：member
    private String accountType = "member";
    // 分析方式： 依会员：member 依代理：agent
    private String analystType = "member";


    /**
     * game的解释：
     * 产品详细
     * 全部       0
     * BB视讯     3
     * AG视讯     19
     * 欧博视讯   22
     * GD视讯     27
     * BG视讯     36
     * EVO视讯    47
     * eBET视讯   54
     * BB电子     5
     * BB捕鱼达人 30
     * BB捕鱼大师 38
     * GNS电子    28
     * ISB电子    29
     * PT电子     20
     * HB电子     32
     * PP电子     37
     * JDB电子    39
     * AG电子     40
     * 大满贯电子  41
     * 申博电子    42
     * 5G电子     44
     * SW电子     46
     * 旧BNG电子  48
     * WM电子     50
     * Gti电子    51
     * KA电子     53
     * AW电子     56
     * CQ9电子    52
     * FG电子     59
     * 易游电子   67
     * BB体育     1
     * 沙巴体育   4
     * BB彩票     12
     * NewBB体育  31
     * VR彩票     45
     * 开元棋牌   49
     * 波音体育   55
     * BB天豪棋牌 66
     * 皇冠体育   65
     * 易游棋牌   62
     * XBB电子    76
     * XBB彩票    73
     * FG棋牌     69
     * XBB视讯    75
     * JDB棋牌    68
     * PG电子     58
     * 电竞牛     60
     * MG视讯     72
     * MT棋牌     64
     * MT电子     71
     * ACE棋牌    77
     * ACE电子    78
     * 乐游棋牌    83
     * PS电子     79
     * MG电子     82
     * 幸运棋牌    81
     * EG视讯     89
     * EG电子     90
     * BNG电子    95
     * BB区块链    93
     * 完美视讯     97
     */
}
