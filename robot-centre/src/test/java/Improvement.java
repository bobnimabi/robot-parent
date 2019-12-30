import org.apache.logging.log4j.ThreadContext;

/**
 * Created by mrt on 2019/12/29 0029 13:24
 */
public class Improvement {
    /**
     * 1.日志打印需打印tenantId，channelId，platformId，functionCode
     * 2.登录：获取图片验证码 + 登录 必须使用同一个httpclient--------------------->不做，没意义
     * 3.图片验证码的获取也放在function的Package包里面
     * 3.登录时候，如何保证在分布式条件下，所有的httpclient都会新建（刷新配置）----->已完成
     * 5.机器人标志现在是无限期的，可以改进下，可以每次检查时更新下时间------------->已完成
     * 6.由于redis宕机或项目突然挂了，导致机器人没有归还回来
     */

}
