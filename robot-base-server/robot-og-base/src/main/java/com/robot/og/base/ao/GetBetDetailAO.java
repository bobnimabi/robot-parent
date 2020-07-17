package com.robot.og.base.ao;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author tank
 * @date 2020/7/14
 */
@Data
public class GetBetDetailAO {
	//账号
	private String account;
	//起始时间
	private LocalDateTime startDate;
	//终止时间
	private LocalDateTime endDate;
	//游戏编码
	private String gameCode;
	//游戏编码列表，2020-02-27加的，以前的返水等活动传递的值本来就是null，不影响
	private List<String> gameCodeList;


}
