package com.robot.bbin.activity.config;

import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import com.robot.center.constant.RobotConsts;
import com.robot.center.config.MybatisPlusConfigBase;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by mrt on 11/18/2019 2:31 PM
 */
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig extends MybatisPlusConfigBase {
    @Override
    protected TenantSqlParser getPlatformSqlParser() {
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue(RobotConsts.PLATFORM_ID.BBIN);
            }

            @Override
            public String getTenantIdColumn() {
                return "platform_id";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                // 返回true，表示放行,全部不放行
                return PLATFORM_PERMIT.contains(tableName);
            }
        });
        return tenantSqlParser;
    }

    @Override
    protected TenantSqlParser getFunctionSqlParser() {
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue(RobotConsts.FUNCTION_CODE.ACTIVITY);
            }

            @Override
            public String getTenantIdColumn() {
                return "function_code";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                // 返回true，表示放行,全部不放行
                return FUNCTION_PERMIT.contains(tableName);
            }
        });
        return tenantSqlParser;
    }
}
