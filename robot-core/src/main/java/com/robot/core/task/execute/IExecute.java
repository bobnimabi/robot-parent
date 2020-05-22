package com.robot.core.task.execute;//package com.bbin.robotWrapper.core.execute;


import com.robot.core.http.response.StanderHttpResponse;


/**
 * Created by mrt on 2019/7/10 0010 下午 4:08
 */
public interface IExecute {

    /**
     * 执行请求
     * @param property
     * @return
     */
    StanderHttpResponse request(IFunctionProperty property);
}
