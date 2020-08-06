package com.robot.og.base.ao;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 *  查询注单参数
 * </p>
 *
 * @author tanke
 * @date 2020/7/17
 */
@Data
public class QueryOrderNoAO {

	private String type;
	private String accountId;
	private String bettingCode;
	private String platform;
	private LocalDateTime startDate;
	private LocalDateTime lastDate;
	private String pageNo;
	private String pageSize;

}
