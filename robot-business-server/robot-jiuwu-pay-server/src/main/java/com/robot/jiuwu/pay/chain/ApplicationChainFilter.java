package com.robot.jiuwu.pay.chain;

import com.robot.center.chain.FilterChainBase;
import com.robot.jiuwu.pay.check.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 2020/1/10 0010 11:46
 */
@Service
public class ApplicationChainFilter extends FilterChainBase implements InitializingBean {
    @Autowired
    private AmountRangeInvoker amountRangeInvoker;
    @Autowired
    private FirstInvoker firstInvoker;
    @Autowired
    private OrderUniqueInvoker orderUniqueInvoker;
    @Autowired
    private RemarkInvoker remarkInvoker;
    @Autowired
    private UserNameInvoker userNameInvoker;
    @Autowired
    private LockAndRemarkInvoker lockAndRemarkInvoker;
    @Autowired
    private SenderInvoker senderInvoker;

    /**
     * 注意：顺序很重要，不能随便更改
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        setInvoker(orderUniqueInvoker);
        setInvoker(userNameInvoker);
        setInvoker(amountRangeInvoker);
        setInvoker(remarkInvoker);
        setInvoker(firstInvoker);
        setInvoker(lockAndRemarkInvoker);
        setInvoker(senderInvoker);
    }
}
