package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author mrt
 * @Date 2020/6/19 16:32
 * @Version 2.0
 */
public class Test {
    /**
     * 匹配html里面的meta头里的charset专用正则
     */
    private static final Pattern PATTERN = Pattern.compile("var BarID = '([\\w-]*)'");

    public static void main(String[] args) {
        Matcher matcher = PATTERN.matcher(html);
        if (matcher.find()) {
            String barID = matcher.group(1);
            System.out.println(barID);
        }
    }

    private static final String html = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "    <title>【局查询】</title>\n" +
            "    <script type=\"text/javascript\" src=\"/ruxitagentjs_ICA2SVfhqru_10147180705145128.js\" data-dtconfig=\"rid=RID_652125485|rpid=952900805|domain=20550595.com|reportUrl=/rb_903c3853-143c-4285-ab19-ed8981193e98|app=0624f59e31164ff3|featureHash=ICA2SVfhqru|rdnt=1|uxrgce=1|bp=2|cuc=j99zsa8x|srms=1,1,,,|uxrgcm=100,25,300,3;100,25,300,3|dpvc=1|md=1=a#appContainer ^rb header ^rb section ^rb div.profile ^rb div.dropdown.el-dropdown ^rb div ^rb div|lastModification=1592295048746|dtVersion=10147180705145128|tp=500,50,0,1|uxdcw=1500|agentUri=/ruxitagentjs_ICA2SVfhqru_10147180705145128.js\"></script><link rel=\"stylesheet\" type=\"text/css\" href=\"/assets/vendor/jquery-ui/jquery-ui.min.css\">\n" +
            "    <link rel=\"stylesheet\" type=\"text/css\" href=\"/assets/vendor/bootstrap/css/bootstrap.min.css\">\n" +
            "    <link rel=\"stylesheet\" type=\"text/css\" href=\"/assets/lib/cust/bootstrap.base.css?20181128\">\n" +
            "    <link rel=\"stylesheet\" type=\"text/css\" href=\"/assets/lib/cust/bootstrap.cust.css\">\n" +
            "\n" +
            "</head>\n" +
            "<body class=cust>\n" +
            "\n" +
            "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/game/game_betrecord_kind5.css\">\n" +
            "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/game/game_wagers_detail.css\">\n" +
            "<link rel=\"stylesheet\" type=\"text/css\" href=\"/assets/vendor/jquery-ui-plugin/bootstrap-modal/css/bootstrap-modal.css\">\n" +
            "<div class=\"container-fluid\">\n" +
            "    <h3>局查询</h3>\n" +
            "    <input type=\"button\" value=\"◄回上一页\" class=\"btn btn-default\" onclick=\"javascript:history.go(-1);\">\n" +
            "    <ul class=\"nav nav-tabs\">\n" +
            "                                                    <li><a href=\"/game/betrecord_search/kind3?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">BB视讯</a></li>\n" +
            "                                                                                                                        <li><a href=\"/game/betrecord_search/kind19?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">AG视讯</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind22?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">欧博视讯</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind27?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">GD视讯</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind36?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">BG视讯</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind47?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">EVO视讯</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind54?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">eBET视讯</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind72?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">MG视讯</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind75?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">XBB视讯</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord/kind?SearchData=BetQuery&GameKind=89&date_start=2020-06-19&date_end=2020-06-19\">EG视讯</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord/kind?SearchData=BetQuery&GameKind=93&date_start=2020-06-19&date_end=2020-06-19\">BB区块链</a></li>\n" +
            "                                                                                                    <li><a href=\"/game/betrecord/kind?SearchData=BetQuery&GameKind=97&date_start=2020-06-19&date_end=2020-06-19\">完美视讯</a></li>\n" +
            "                                                                                                    <li class=\"active\"><a>BB电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind38?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">BB捕鱼大师</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind30?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">BB捕鱼达人</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind20?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">PT电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind28?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">GNS电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind29?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">ISB电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind32?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">HB电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind37?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">PP电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind39?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">JDB电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind40?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">AG电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind41?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">大满贯电子</a></li>\n" +
            "                                                                                                    <li><a href=\"/game/betrecord_search/kind44?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">SG电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind46?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">SW电子</a></li>\n" +
            "                                                                                                    <li><a href=\"/game/betrecord_search/kind50?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">WM电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind51?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">Gti电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind52?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">CQ9电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind53?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">KA电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind56?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">AW电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind58?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">PG电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind59?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">FG电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind67?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">易游电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind71?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">MT电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind76?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">XBB电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind78?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">ACE电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind79?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">PS电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind82?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">MG电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord/kind?SearchData=BetQuery&GameKind=90&date_start=2020-06-19&date_end=2020-06-19\">EG电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord/kind?SearchData=BetQuery&GameKind=94&date_start=2020-06-19&date_end=2020-06-19\">TP电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord/kind?SearchData=BetQuery&GameKind=95&date_start=2020-06-19&date_end=2020-06-19\">BNG电子</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind55?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">波音体育</a></li>\n" +
            "                                                                                                    <li><a href=\"/game/betrecord_search/kind65?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">皇冠体育</a></li>\n" +
            "                                                                                                    <li><a href=\"/game/betrecord_search/kind12?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">BB彩票</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind45?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">VR彩票</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind73?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">XBB彩票</a></li>\n" +
            "                                                                                                    <li><a href=\"/game/betrecord_search/kind49?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">开元棋牌</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind62?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">易游棋牌</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind64?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">MT棋牌</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind66?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">BB天豪棋牌</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind68?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">JDB棋牌</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind69?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">FG棋牌</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind77?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">ACE棋牌</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind81?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">幸运棋牌</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord_search/kind83?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\">乐游棋牌</a></li>\n" +
            "                                                                                <li><a href=\"/game/betrecord/kind?SearchData=BetQuery&GameKind=92&date_start=2020-06-19&date_end=2020-06-19\">博乐棋牌</a></li>\n" +
            "                                        </ul>\n" +
            "    <div class=\"tab-content\">\n" +
            "        <div class=\"tab-pane active\">\n" +
            "            <div class=\"alert alert-info alert-minpadding\">\n" +
            "                <form id=\"rallydata\" name=\"rallydata\" method=\"GET\" action=\"/game/wagersip_search\" autocomplete=\"off\" class=\"form-inline HideForm\">\n" +
            "                    <div class=\"form-group\">\n" +
            "                        <label>请选择：</label>\n" +
            "                        <select id=\"functionselects\" name=\"functionselects\">\n" +
            "                                                        <option value=\"2\" selected=\"selected\" >注单编号</option>\n" +
            "                                                        <option value=\"3\" >连消查询</option>\n" +
            "                                                    </select>\n" +
            "                    </div>\n" +
            "                    <div class=\"form-group\" id=\"gametype\">\n" +
            "                        <label>游戏类别：</label>\n" +
            "                        <select id=\"gametypelist\" name=\"gametypelist\">\n" +
            "                                                        <option value=\"5005\" >惑星战记</option>\n" +
            "                                                        <option value=\"5010\" >外星战记</option>\n" +
            "                                                        <option value=\"5012\" >外星争霸</option>\n" +
            "                                                        <option value=\"5013\" >传统</option>\n" +
            "                                                        <option value=\"5014\" >丛林</option>\n" +
            "                                                        <option value=\"5015\" >FIFA2010</option>\n" +
            "                                                        <option value=\"5019\" >水果乐园</option>\n" +
            "                                                        <option value=\"5027\" >功夫龙</option>\n" +
            "                                                        <option value=\"5030\" >幸运财神</option>\n" +
            "                                                        <option value=\"5039\" >鱼虾蟹</option>\n" +
            "                                                        <option value=\"5043\" >钻石水果盘</option>\n" +
            "                                                        <option value=\"5044\" >明星97II</option>\n" +
            "                                                        <option value=\"5045\" >森林舞会</option>\n" +
            "                                                        <option value=\"5046\" >斗魂</option>\n" +
            "                                                        <option value=\"5054\" >爆骰</option>\n" +
            "                                                        <option value=\"5057\" >明星97</option>\n" +
            "                                                        <option value=\"5058\" >疯狂水果盘</option>\n" +
            "                                                        <option value=\"5061\" >超级7</option>\n" +
            "                                                        <option value=\"5062\" >龙在囧途</option>\n" +
            "                                                        <option value=\"5063\" >水果拉霸</option>\n" +
            "                                                        <option value=\"5064\" >扑克拉霸</option>\n" +
            "                                                        <option value=\"5065\" >筒子拉霸</option>\n" +
            "                                                        <option value=\"5066\" >足球拉霸</option>\n" +
            "                                                        <option value=\"5067\" >大话西游</option>\n" +
            "                                                        <option value=\"5068\" >酷搜马戏团</option>\n" +
            "                                                        <option value=\"5069\" >水果擂台</option>\n" +
            "                                                        <option value=\"5076\" >数字大转轮</option>\n" +
            "                                                        <option value=\"5077\" >水果大转轮</option>\n" +
            "                                                        <option value=\"5079\" >3D数字大转轮</option>\n" +
            "                                                        <option value=\"5080\" >乐透转轮</option>\n" +
            "                                                        <option value=\"5083\" >钻石列车</option>\n" +
            "                                                        <option value=\"5084\" >圣兽传说</option>\n" +
            "                                                        <option value=\"5088\" >斗大</option>\n" +
            "                                                        <option value=\"5089\" >红狗</option>\n" +
            "                                                        <option value=\"5090\" >金鸡报喜</option>\n" +
            "                                                        <option value=\"5093\" >金瓶梅</option>\n" +
            "                                                        <option value=\"5094\" >金瓶梅2</option>\n" +
            "                                                        <option value=\"5095\" >斗鸡</option>\n" +
            "                                                        <option value=\"5096\" >五行</option>\n" +
            "                                                        <option value=\"5097\" >海底世界</option>\n" +
            "                                                        <option value=\"5098\" >五福临门</option>\n" +
            "                                                        <option value=\"5099\" >金狗旺岁</option>\n" +
            "                                                        <option value=\"5100\" >七夕</option>\n" +
            "                                                        <option value=\"5105\" >欧式轮盘</option>\n" +
            "                                                        <option value=\"5106\" >三国</option>\n" +
            "                                                        <option value=\"5107\" >美式轮盘</option>\n" +
            "                                                        <option value=\"5108\" >彩金轮盘</option>\n" +
            "                                                        <option value=\"5109\" >法式轮盘</option>\n" +
            "                                                        <option value=\"5110\" >夜上海</option>\n" +
            "                                                        <option value=\"5119\" >神秘岛</option>\n" +
            "                                                        <option value=\"5120\" >女娲补天</option>\n" +
            "                                                        <option value=\"5121\" >奥林帕斯</option>\n" +
            "                                                        <option value=\"5122\" >球球大作战</option>\n" +
            "                                                        <option value=\"5123\" >经典21点</option>\n" +
            "                                                        <option value=\"5127\" >绝地求生</option>\n" +
            "                                                        <option value=\"5128\" >多福多财</option>\n" +
            "                                                        <option value=\"5138\" >夹猪珠</option>\n" +
            "                                                        <option value=\"5139\" >熊猫乐园</option>\n" +
            "                                                        <option value=\"5140\" >啤酒嘉年华</option>\n" +
            "                                                        <option value=\"5141\" >斗牛赢家</option>\n" +
            "                                                        <option value=\"5142\" >魔光幻音</option>\n" +
            "                                                        <option value=\"5143\" >糖果派对3</option>\n" +
            "                                                        <option value=\"5144\" >情人夜</option>\n" +
            "                                                        <option value=\"5145\" >步步高升</option>\n" +
            "                                                        <option value=\"5146\" >糖果派</option>\n" +
            "                                                        <option value=\"5150\" >宝石传奇</option>\n" +
            "                                                        <option value=\"5151\" >Jenga</option>\n" +
            "                                                        <option value=\"5152\" >埃及传奇</option>\n" +
            "                                                        <option value=\"5153\" >九尾狐</option>\n" +
            "                                                        <option value=\"5154\" >初音大进击</option>\n" +
            "                                                        <option value=\"5155\" >东海龙宫</option>\n" +
            "                                                        <option value=\"5156\" >祖玛帝国</option>\n" +
            "                                                        <option value=\"5157\" >三元四喜</option>\n" +
            "                                                        <option value=\"5159\" >疯狂麦斯</option>\n" +
            "                                                        <option value=\"5160\" >雷神索尔</option>\n" +
            "                                                        <option value=\"5161\" >回转寿司</option>\n" +
            "                                                        <option value=\"5162\" >开心蛋</option>\n" +
            "                                                        <option value=\"5165\" >战神</option>\n" +
            "                                                        <option value=\"5166\" >葫芦娃</option>\n" +
            "                                                        <option value=\"5169\" >中国好声音</option>\n" +
            "                                                        <option value=\"5170\" >海底传奇</option>\n" +
            "                                                        <option value=\"5171\" >糖果派对-极速版</option>\n" +
            "                                                        <option value=\"5204\" >2014 FIFA</option>\n" +
            "                                                        <option value=\"5402\" >夜市人生</option>\n" +
            "                                                        <option value=\"5404\" >沙滩排球</option>\n" +
            "                                                        <option value=\"5407\" >大红帽与小野狼</option>\n" +
            "                                                        <option value=\"5601\" >秘境冒险</option>\n" +
            "                                                        <option value=\"5803\" >阿兹特克宝藏</option>\n" +
            "                                                        <option value=\"5805\" >凯萨帝国</option>\n" +
            "                                                        <option value=\"5810\" >航海时代</option>\n" +
            "                                                        <option value=\"5823\" >发大财</option>\n" +
            "                                                        <option value=\"5824\" >恶龙传说</option>\n" +
            "                                                        <option value=\"5828\" >霸王龙</option>\n" +
            "                                                        <option value=\"5835\" >喜福牛年</option>\n" +
            "                                                        <option value=\"5837\" >喜福猴年</option>\n" +
            "                                                        <option value=\"5839\" >经典高球</option>\n" +
            "                                                        <option value=\"5901\" >连环夺宝</option>\n" +
            "                                                        <option value=\"5902\" >糖果派对</option>\n" +
            "                                                        <option value=\"5903\" >秦皇秘宝</option>\n" +
            "                                                        <option value=\"5904\" >蒸气炸弹</option>\n" +
            "                                                        <option value=\"5907\" >趣味台球</option>\n" +
            "                                                        <option value=\"5908\" >糖果派对2</option>\n" +
            "                                                        <option value=\"5909\" >开心消消乐</option>\n" +
            "                                                        <option value=\"5910\" >魔法元素</option>\n" +
            "                                                        <option value=\"5911\" >宝石派对</option>\n" +
            "                                                        <option value=\"5912\" >连环夺宝2</option>\n" +
            "                                                    </select>\n" +
            "                    </div>\n" +
            "                    <div class=\"form-group\" id=\"timezone\">\n" +
            "                        <label>时间：</label>\n" +
            "                        <input id=\"datepicker01\" name=\"datepicker01\" class=\"form-control\" size=\"10\" value=\"2020-06-19\">\n" +
            "                        <span id=\"datechoice\">~</span>\n" +
            "                        <input id=\"datepicker02\" name=\"datepicker02\" class=\"form-control\" size=\"10\" value=\"2020-06-19\">\n" +
            "                    </div>\n" +
            "                    <div class=\"form-group\" id=\"wagers5id\">\n" +
            "                        <label> 注单编号 :</label>\n" +
            "                        <input type=\"text\" class=\"form-control\" id=\"wagersid\" value=\"\">\n" +
            "                    </div>\n" +
            "                    <div class=\"form-group\" id=\"pagecount\">\n" +
            "                        <label>每页笔数：</label>\n" +
            "                        <select id=\"pagenum\" name=\"pagenum\">\n" +
            "                                                        <option value=\"100\" selected=\"selected\" >100</option>\n" +
            "                                                        <option value=\"200\" >200</option>\n" +
            "                                                        <option value=\"500\" >500</option>\n" +
            "                                                    </select>\n" +
            "                    </div>\n" +
            "                    <div class=\"form-group\" id=\"sorttype\">\n" +
            "                        <label> 时间排序 :</label>\n" +
            "                        <select id=\"sort\" name=\"sort\">\n" +
            "                            <option value=\"ASC\" >升幂(由小到大)</option>\n" +
            "                            <option value=\"DESC\" selected=\"selected\" >降幂(由大到小)</option>\n" +
            "                        </select>\n" +
            "                    </div>\n" +
            "                    <button id=\"startsearch\" type=\"button\" class=\"btn btn-info\" value=\"\"> 查询 </button>\n" +
            "                </form>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "    \n" +
            "    \n" +
            "                <div class=\"panel panel-default\">\n" +
            "            <table class=\"table table-hover text-middle table-bordered\">\n" +
            "                <thead>\n" +
            "                                        <tr>\n" +
            "                        <th class=\"text-center\">时间</th>\n" +
            "                        <th class=\"text-center\">注单编号</th>\n" +
            "                        <th class=\"text-center\">游戏类别</th>\n" +
            "                                                <th class=\"text-center\">厅主</th>\n" +
            "                                                                        <th class=\"text-center\">帐号</th>\n" +
            "                                                <th class=\"text-center\">结果</th>\n" +
            "                        <th class=\"text-center\">总投注</th>\n" +
            "                        <th class=\"text-center\">总派彩</th>\n" +
            "                        <th class=\"text-center\">彩金资料(RMB)</th>\n" +
            "                    </tr>\n" +
            "                </thead>\n" +
            "                <tbody>\n" +
            "                    <!--//時間    注單編號    遊戲類別    廳主  帳號  結果  總投注 總派彩 備註-->\n" +
            "                                    </tbody>\n" +
            "                <tfoot>\n" +
            "                                    </tfoot>\n" +
            "            </table>\n" +
            "        </div>\n" +
            "            </div>\n" +
            "<div id=\"WagersExtendModal\" class=\"modal fade\" tabindex=\"-1\" data-replace=\"true\" data-modal-overflow=\"true\" style=\"display: none;\">\n" +
            "    <div class=\"modal-header\">\n" +
            "        <button type=\"button\" class=\"btn btn-default btn-close\" data-dismiss=\"modal\">\n" +
            "            <span class=\"glyphicon glyphicon-remove\"></span>\n" +
            "        </button>\n" +
            "        <h4 class=\"modal-title gamename\"></h4>\n" +
            "    </div>\n" +
            "    <div class=\"modal-body wagersinfo\">\n" +
            "        Loading\n" +
            "    </div>\n" +
            "</div>\n" +
            "<script src=\"/assets/vendor/jquery/jquery.min.js\"></script>\n" +
            "<script src=\"/assets/vendor/jquery-ui/jquery-ui.min.js\"></script>\n" +
            "<script src=\"/assets/vendor/bootstrap/js/bootstrap.min.js\"></script>\n" +
            "<script src=\"/assets/vendor/jquery-ui-plugin/jquery-ui-combobox.js\"></script>\n" +
            "<script src=\"/assets/vendor/jquery-ui-plugin/datepicker-i18n/datepicker-cn.js\"></script>\n" +
            "<script src=\"/assets/lib/cust/jquery.cust.js\"></script>\n" +
            "<script src=\"/assets/vendor/jquery-ui-plugin/bootstrap-modal/js/bootstrap-modalmanager.js\"></script>\n" +
            "<script src=\"/assets/vendor/jquery-ui-plugin/bootstrap-modal/js/bootstrap-modal.js\"></script>\n" +
            "<script language=\"JavaScript\">\n" +
            "var Terminal = 'N';\n" +
            "var selectwebsite = '请选择站别'; //請選擇站別\n" +
            "var selectgametype = '请选择游戏类别'; //請選擇遊戲類別\n" +
            "var wagersid = '请务必输入注单编号!!'; //注單編號\n" +
            "var noserial = '请务必输入局号!!'; //請務必輸入局號!!\n" +
            "var notable = '没有桌号'; //沒有桌號\n" +
            "var dateerror = '日期验证错误'; //日期驗證錯誤\n" +
            "var overday = '查询日期区间不可超过7天!!'; //超過七天\n" +
            "var setdateerror = '查询日期区间不可超过7天!!'; //日期區間錯誤\n" +
            "var warning = '警告'; //警告\n" +
            "var confirm = '确定'; //確定\n" +
            "var cancel = '取消'; //取消\n" +
            "var alldict = '全部'; //全部\n" +
            "var BarID = '4';\n" +
            "var freshtime = '';\n" +
            "var SearchData = 'BetQuery';\n" +
            "var UserID = '';\n" +
            "var score = '切换分数',\n" +
            "    money = '切换金额',\n" +
            "    wagersdetail = '下注纪录';\n" +
            "var renovate = '线路维护'; //線路維護\n" +
            "var amenu = '{\"3\":{\"title\":\"BB\\u89c6\\u8baf\",\"url\":\"\\/game\\/betrecord_search\\/kind3?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"99\":{\"title\":\"BB\\u5c0f\\u8d39\",\"url\":\"\\/game\\/betrecord_search\\/kind99?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":1,\"renovate\":0},\"35\":{\"title\":\"\\u8d4c\\u795e\\u5385\",\"url\":\"\\/game\\/betrecord_search\\/kind35?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":1,\"renovate\":0},\"19\":{\"title\":\"AG\\u89c6\\u8baf\",\"url\":\"\\/game\\/betrecord_search\\/kind19?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"22\":{\"title\":\"\\u6b27\\u535a\\u89c6\\u8baf\",\"url\":\"\\/game\\/betrecord_search\\/kind22?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"27\":{\"title\":\"GD\\u89c6\\u8baf\",\"url\":\"\\/game\\/betrecord_search\\/kind27?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"36\":{\"title\":\"BG\\u89c6\\u8baf\",\"url\":\"\\/game\\/betrecord_search\\/kind36?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"47\":{\"title\":\"EVO\\u89c6\\u8baf\",\"url\":\"\\/game\\/betrecord_search\\/kind47?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"54\":{\"title\":\"eBET\\u89c6\\u8baf\",\"url\":\"\\/game\\/betrecord_search\\/kind54?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"72\":{\"title\":\"MG\\u89c6\\u8baf\",\"url\":\"\\/game\\/betrecord_search\\/kind72?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"75\":{\"title\":\"XBB\\u89c6\\u8baf\",\"url\":\"\\/game\\/betrecord_search\\/kind75?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"89\":{\"title\":\"EG\\u89c6\\u8baf\",\"url\":\"\\/game\\/betrecord\\/kind?SearchData=BetQuery&GameKind=89&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"93\":{\"title\":\"BB\\u533a\\u5757\\u94fe\",\"url\":\"\\/game\\/betrecord\\/kind?SearchData=BetQuery&GameKind=93&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"96\":{\"title\":\"BC\\u89c6\\u8baf\",\"url\":\"\\/game\\/betrecord\\/kind?SearchData=BetQuery&GameKind=96&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":1,\"renovate\":0},\"97\":{\"title\":\"\\u5b8c\\u7f8e\\u89c6\\u8baf\",\"url\":\"\\/game\\/betrecord\\/kind?SearchData=BetQuery&GameKind=97&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"98\":{\"title\":\"SSG\\u89c6\\u8baf\",\"url\":\"\\/game\\/betrecord\\/kind?SearchData=BetQuery&GameKind=98&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":1,\"renovate\":0},\"5\":{\"title\":\"BB\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind5?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":1,\"close\":0,\"renovate\":0},\"38\":{\"title\":\"BB\\u6355\\u9c7c\\u5927\\u5e08\",\"url\":\"\\/game\\/betrecord_search\\/kind38?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"30\":{\"title\":\"BB\\u6355\\u9c7c\\u8fbe\\u4eba\",\"url\":\"\\/game\\/betrecord_search\\/kind30?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"20\":{\"title\":\"PT\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind20?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"28\":{\"title\":\"GNS\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind28?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"29\":{\"title\":\"ISB\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind29?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"32\":{\"title\":\"HB\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind32?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"37\":{\"title\":\"PP\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind37?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"39\":{\"title\":\"JDB\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind39?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"40\":{\"title\":\"AG\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind40?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"41\":{\"title\":\"\\u5927\\u6ee1\\u8d2f\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind41?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"42\":{\"title\":\"\\u7533\\u535a\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind42?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":1,\"renovate\":0},\"44\":{\"title\":\"SG\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind44?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"46\":{\"title\":\"SW\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind46?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"48\":{\"title\":\"\\u65e7BNG\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind48?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":1,\"renovate\":0},\"50\":{\"title\":\"WM\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind50?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"51\":{\"title\":\"Gti\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind51?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"52\":{\"title\":\"CQ9\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind52?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"53\":{\"title\":\"KA\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind53?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"56\":{\"title\":\"AW\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind56?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"58\":{\"title\":\"PG\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind58?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"59\":{\"title\":\"FG\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind59?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"67\":{\"title\":\"\\u6613\\u6e38\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind67?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"71\":{\"title\":\"MT\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind71?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"76\":{\"title\":\"XBB\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind76?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"78\":{\"title\":\"ACE\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind78?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"79\":{\"title\":\"PS\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind79?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"82\":{\"title\":\"MG\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord_search\\/kind82?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"90\":{\"title\":\"EG\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord\\/kind?SearchData=BetQuery&GameKind=90&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"94\":{\"title\":\"TP\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord\\/kind?SearchData=BetQuery&GameKind=94&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"95\":{\"title\":\"BNG\\u7535\\u5b50\",\"url\":\"\\/game\\/betrecord\\/kind?SearchData=BetQuery&GameKind=95&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"55\":{\"title\":\"\\u6ce2\\u97f3\\u4f53\\u80b2\",\"url\":\"\\/game\\/betrecord_search\\/kind55?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"60\":{\"title\":\"\\u7535\\u7ade\\u725b\",\"url\":\"\\/game\\/betrecord_search\\/kind60?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":1,\"renovate\":0},\"65\":{\"title\":\"\\u7687\\u51a0\\u4f53\\u80b2\",\"url\":\"\\/game\\/betrecord_search\\/kind65?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"84\":{\"title\":\"BR\\u865a\\u62df\",\"url\":\"\\/game\\/betrecord_search\\/kind84?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":1,\"renovate\":0},\"12\":{\"title\":\"BB\\u5f69\\u7968\",\"url\":\"\\/game\\/betrecord_search\\/kind12?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"45\":{\"title\":\"VR\\u5f69\\u7968\",\"url\":\"\\/game\\/betrecord_search\\/kind45?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"73\":{\"title\":\"XBB\\u5f69\\u7968\",\"url\":\"\\/game\\/betrecord_search\\/kind73?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"91\":{\"title\":\"XBB\\u5fae\\u5f69\",\"url\":\"\\/game\\/betrecord_search\\/kind91?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":1,\"renovate\":0},\"49\":{\"title\":\"\\u5f00\\u5143\\u68cb\\u724c\",\"url\":\"\\/game\\/betrecord_search\\/kind49?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"62\":{\"title\":\"\\u6613\\u6e38\\u68cb\\u724c\",\"url\":\"\\/game\\/betrecord_search\\/kind62?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"64\":{\"title\":\"MT\\u68cb\\u724c\",\"url\":\"\\/game\\/betrecord_search\\/kind64?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"66\":{\"title\":\"BB\\u5929\\u8c6a\\u68cb\\u724c\",\"url\":\"\\/game\\/betrecord_search\\/kind66?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"68\":{\"title\":\"JDB\\u68cb\\u724c\",\"url\":\"\\/game\\/betrecord_search\\/kind68?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"69\":{\"title\":\"FG\\u68cb\\u724c\",\"url\":\"\\/game\\/betrecord_search\\/kind69?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"77\":{\"title\":\"ACE\\u68cb\\u724c\",\"url\":\"\\/game\\/betrecord_search\\/kind77?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"81\":{\"title\":\"\\u5e78\\u8fd0\\u68cb\\u724c\",\"url\":\"\\/game\\/betrecord_search\\/kind81?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"83\":{\"title\":\"\\u4e50\\u6e38\\u68cb\\u724c\",\"url\":\"\\/game\\/betrecord_search\\/kind83?SearchData=BetQuery&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0},\"92\":{\"title\":\"\\u535a\\u4e50\\u68cb\\u724c\",\"url\":\"\\/game\\/betrecord\\/kind?SearchData=BetQuery&GameKind=92&date_start=2020-06-19&date_end=2020-06-19\",\"now\":0,\"close\":0,\"renovate\":0}}';\n" +
            "var BarType = '0';\n" +
            "\n" +
            "</script>\n" +
            "<script src=\"/script/game/game_refreshtime.js\"></script>\n" +
            "<script src=\"/script/game/game_betrecord_renovate.js\"></script>\n" +
            "<script src=\"/script/game/game_betrecord_kind5.js?2019091901\"></script>\n" +
            "<script src=\"/script/game/game_betrecord_extend.js\"></script>\n" +
            "\n" +
            "\n" +
            "</body>\n" +
            "</html>";


}
