package com.robot.bbin.activity.dto;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameChild {
    private Long id;
    private String gameCode;
    private String name;
}
