//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.robot.center.tenant;

import com.robot.core.common.TContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class FeignTenantInterceptor implements RequestInterceptor {
    private static final Logger log = LoggerFactory.getLogger(FeignTenantInterceptor.class);

    public FeignTenantInterceptor() {
    }

    @Override
    public void apply(RequestTemplate template) {
        String url = template.request().url();
        if (url.indexOf("/domain/get") == -1) {
            this.setOriginHost(template);
            log.info("FeignTenantInterceptor 拦截器进入================================");
            Long tenantId = Long.parseLong(TContext.getTenantId());
            if (null == tenantId) {
                throw  new IllegalArgumentException("没有tenantId");
            }

            Long channelId = Long.parseLong(TContext.getChannelId());
            if (null == tenantId) {
                throw  new IllegalArgumentException("没有channelId");
            }

            log.info("FeignTenantInterceptor 成功设置 请求头tenantId={},channelId={}", tenantId, channelId);
            template.header("tenant_id", new String[]{tenantId+""});
            template.header("channel_id", new String[]{channelId+""});
        }
    }

    private void setOriginHost(RequestTemplate template) {
        log.info("设置host ");
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        Optional.ofNullable(requestAttributes).map((t) -> {
            try {
                String host = requestAttributes.getRequest().getServerName();
                String originHost = requestAttributes.getRequest().getHeader("originHost");
                if (StringUtils.isBlank(originHost)) {
                    log.info("设置 originHost={} ", host);
                    template.header("originHost", new String[]{host});
                } else {
                    log.info("设置 originHost={} ", originHost);
                    template.header("originHost", new String[]{originHost});
                }
            } catch (Exception var5) {
                log.error("设置host 发生异常", var5);
            }

            return t;
        });
    }
}
