package com.robot.center.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import com.robot.core.common.TContext;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.FromItem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//Spring boot方式

public abstract class MybatisPlusConfigBase {
    // 不需要tenantId修改时的表
    private static final Set<String> TENANT_PERMIT = new HashSet<>(9);
    // 不需要channleId修改时的表
    private static final Set<String> CHANNEL_PERMIT = new HashSet<>(9);
    // 不需要platformId修改时的表
    protected static final Set<String> PLATFORM_PERMIT = new HashSet<>(9);
    // 不需要fucntionCode修改时的表
    protected static final Set<String> FUNCTION_PERMIT = new HashSet<>(9);
    // 不需要isValid修改时的表
    private static final Set<String> IS_VALID_PERMIT = new HashSet<>(1);
    // 不需要status修改时的表
    private static final Set<String> STATUS_PERMIT = new HashSet<>(1);

    static {
        TENANT_PERMIT.add("async_request_config");
        CHANNEL_PERMIT.add("async_request_config");
        PLATFORM_PERMIT.add("async_request_config");
        FUNCTION_PERMIT.add("async_request_config");

        TENANT_PERMIT.add("connection_pool_config");
        CHANNEL_PERMIT.add("connection_pool_config");
        PLATFORM_PERMIT.add("connection_pool_config");
        FUNCTION_PERMIT.add("connection_pool_config");

        TENANT_PERMIT.add("http_request_config");
        CHANNEL_PERMIT.add("http_request_config");
        PLATFORM_PERMIT.add("http_request_config");
        FUNCTION_PERMIT.add("http_request_config");

        TENANT_PERMIT.add("platform");
        CHANNEL_PERMIT.add("platform");
        PLATFORM_PERMIT.add("platform");
        FUNCTION_PERMIT.add("platform");

        TENANT_PERMIT.add("request_record");
        CHANNEL_PERMIT.add("request_record");
        PLATFORM_PERMIT.add("request_record");
        FUNCTION_PERMIT.add("request_record");

        TENANT_PERMIT.add("response_record");
        CHANNEL_PERMIT.add("response_record");
        PLATFORM_PERMIT.add("response_record");
        FUNCTION_PERMIT.add("response_record");

        TENANT_PERMIT.add("response_record");
        CHANNEL_PERMIT.add("response_record");
        FUNCTION_PERMIT.add("response_record");

        TENANT_PERMIT.add("tenant_robot_path");
        CHANNEL_PERMIT.add("tenant_robot_path");
        FUNCTION_PERMIT.add("tenant_robot_path");

        TENANT_PERMIT.add("tenant_robot_proxy");
        CHANNEL_PERMIT.add("tenant_robot_proxy");
        PLATFORM_PERMIT.add("tenant_robot_proxy");
        FUNCTION_PERMIT.add("tenant_robot_proxy");

        TENANT_PERMIT.add("tenant_robot_template");
        CHANNEL_PERMIT.add("tenant_robot_template");
        FUNCTION_PERMIT.add("tenant_robot_template");
    }


    /**
     * SQL执行效率插件
     */
    @Bean
    @Profile({"dev", "test", "prod"})// 设置 dev test 环境开启
    public PerformanceInterceptor performanceInterceptorProd() {
        return new PerformanceInterceptor().setWriteInLog(true).setMaxTime(500);
    }

    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }

    /**
     * 分页插件(这个不配，分页不生效)
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        // 拦截器执行器
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        List<ISqlParser> sqlParserList = new ArrayList<>();
        // 攻击 SQL 阻断解析器、加入解析链,防止恶意全表更新和删除
        sqlParserList.add(new BlockAttackSqlParser());
        sqlParserList.add(this.getTenantIdSqlParser());
        sqlParserList.add(this.getChannelSqlParser());
        sqlParserList.add(this.getIsValidSqlParser());
        sqlParserList.add(this.getPlatformSqlParser());
        sqlParserList.add(this.getFunctionSqlParser());
        // 每条sql自动添加!=status,暂时不用
//        sqlParserList.add(StatusTenantSqlParser.INSTANCE);
        // 将所有sqlParser加入拦截器执行器
        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }

    /**
     * tenant_id
     */
    public TenantSqlParser getTenantIdSqlParser() {
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue(TContext.getTenantId());
            }

            @Override
            public String getTenantIdColumn() {
                return "tenant_id";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                // 返回true，表示放行
                return TENANT_PERMIT.contains(tableName);
            }
        });
        return tenantSqlParser;
    }

    /**
     * channel_id
     */
    public TenantSqlParser getChannelSqlParser() {
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue(TContext.getChannelId());
            }

            @Override
            public String getTenantIdColumn() {
                return "channel_id";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                // 返回true，表示放行
                return CHANNEL_PERMIT.contains(tableName);
            }
        });
        return tenantSqlParser;
    }

    /**
     * is_valid
     */
    public TenantSqlParser getIsValidSqlParser() {
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue("1");
            }

            @Override
            public String getTenantIdColumn() {
                return "is_valid";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                // 返回true，表示放行,全部不放行
                return IS_VALID_PERMIT.contains(tableName);
            }
        });
        return tenantSqlParser;
    }

    /**
     * 获取PlatForm的TenantSqlParser
     * @return
     */
    protected abstract TenantSqlParser getPlatformSqlParser();

    /**
     * 获取FunctionCode的TenantSqlParser
     * @return
     */
    protected abstract TenantSqlParser getFunctionSqlParser();








//--------------------------以下为改动源码!=的实现,暂时不用--------------------------
    /**
     * 重写实现!=
     */
    public static final class StatusTenantSqlParser extends TenantSqlParser {
        public static final StatusTenantSqlParser INSTANCE = new StatusTenantSqlParser();
        private StatusTenantSqlParser() {}
        private final TenantHandler HANDLE_INSTANCE = new TenantHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue(-1L);
            }

            @Override
            public String getTenantIdColumn() {
                return "status";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                // 返回true，表示放行
                return STATUS_PERMIT.contains(tableName);
            }
        };

        // 修改、删除sql
        @Override
        protected BinaryExpression andExpression(Table table, Expression where) {

            NotEqualsTo notEqualsTo = new NotEqualsTo();
            notEqualsTo.setLeftExpression(this.getAliasColumn(table));
            notEqualsTo.setRightExpression(HANDLE_INSTANCE.getTenantId());
            if (null != where) {
                return where instanceof OrExpression ? new AndExpression(notEqualsTo, new Parenthesis(where)) : new AndExpression(notEqualsTo, where);
            } else {
                return notEqualsTo;
            }
        }

        // 查询sql
        @Override
        protected Expression builderExpression(Expression expression, Table table) {
            NotEqualsTo notEqualsTo = new NotEqualsTo();
            notEqualsTo.setLeftExpression(this.getAliasColumn(table));
            notEqualsTo.setRightExpression(HANDLE_INSTANCE.getTenantId());
            if (expression == null) {
                return notEqualsTo;
            } else {
                if (expression instanceof BinaryExpression) {
                    BinaryExpression binaryExpression = (BinaryExpression) expression;
                    if (binaryExpression.getLeftExpression() instanceof FromItem) {
                        this.processFromItem((FromItem) binaryExpression.getLeftExpression());
                    }

                    if (binaryExpression.getRightExpression() instanceof FromItem) {
                        this.processFromItem((FromItem) binaryExpression.getRightExpression());
                    }
                }
                return expression instanceof OrExpression ? new AndExpression(notEqualsTo, new Parenthesis(expression)) : new AndExpression(notEqualsTo, expression);
            }
        }

        // 插入（不能把is_valid=0代入sql）
        @Override
        public void processInsert(Insert insert) {

        }
    }

}