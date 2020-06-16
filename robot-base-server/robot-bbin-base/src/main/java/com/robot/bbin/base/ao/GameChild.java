package com.robot.bbin.base.ao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameChild {
    private Long id;
    private String gameCode;
    private String name;
}
