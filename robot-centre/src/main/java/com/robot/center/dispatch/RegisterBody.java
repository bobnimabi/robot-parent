package com.robot.center.dispatch;

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
    private Long tenantId;
    private Long channelId;
    private Long platformId;
    private Long function;

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }

    // tenantId、channelId、platformId、function范围都是0-1000
    @Override
    public int hashCode() {
       return  (int) (tenantId * Math.pow(10,9) + channelId * Math.pow(10,6) + platformId * Math.pow(10,3) + function);
    }
}
