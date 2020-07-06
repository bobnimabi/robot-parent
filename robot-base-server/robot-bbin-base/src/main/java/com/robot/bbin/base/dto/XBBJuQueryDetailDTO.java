package com.robot.bbin.base.dto;

import com.bbin.common.dto.order.GameChild;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author tank
 * @date 2020/7/4
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XBBJuQueryDetailDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String userName;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String tenantId;
	private String gameCode;
	private String orderNo;
	private List<GameChild> children;
	private String token;
	private List<String> orderNos;


	private String pageId;
	private String userId;
	private String gameType;
}
