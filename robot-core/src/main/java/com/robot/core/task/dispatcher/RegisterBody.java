package com.robot.core.task.dispatcher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by mrt on 11/11/2019 3:42 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterBody {
    private String tenantId;
    private String channelId;
    private String platformId;
    private String function;

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }

    // tenantId、channelId、platformId、function范围都是0-1000
    @Override
    public int hashCode() {
       return  (int) (Integer.parseInt(tenantId) * Math.pow(10,9) + Integer.parseInt(channelId) * Math.pow(10,6) + Integer.parseInt(platformId) * Math.pow(10,3) + Integer.parseInt(function));
    }
}
