package com.robot.bbin.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by mrt on 11/16/2019 11:47 AM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalBetGameDTO {
    private String dateStart;
    private String dateEnd;
    private String userID;
    private String barId;
    private String gameKind;
}
