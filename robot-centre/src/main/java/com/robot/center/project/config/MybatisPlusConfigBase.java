package com.robot.center.project.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import com.robot.center.tenant.RobotThreadLocalUtils;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//Spring boot方式

public abstract class MybatisPlusConfigBase {
    // 不需要tenantId修改时的表
    private static final Set<String> TENANT_PERMIT = new HashSet<>();
    // 不需要channleId修改时的表
    private static final Set<String> CHANNEL_PERMIT = new HashSet<>();
    // 不需要platformId修改时的表
    protected static final Set<String> PLATFORM_PERMIT = new HashSet<>();
    // 不需要fucntionCode修改时的表
    protected static final Set<String> FUNCTION_PERMIT = new HashSet<>();
    // 不需要isValid修改时的表
    private static final Set<String> IS_VALID_PERMIT = new HashSet<>();
    // 不需要status修改时的表
    private static final Set<String> STATUS_PERMIT = new HashSet<>();

    static {
        TENANT_PERMIT.add("tenant_channel");
        TENANT_PERMIT.add("tenant_platform");
        TENANT_PERMIT.add("tenant_robot_resp_log");
        CHANNEL_PERMIT.add("tenant_channel");
        CHANNEL_PERMIT.add("tenant_platform");
        CHANNEL_PERMIT.add("tenant_robot_resp_log");
        PLATFORM_PERMIT.add("tenant_robot_resp_log");
        FUNCTION_PERMIT.add("tenant_robot_resp_log");
        IS_VALID_PERMIT.add("tenant_robot_resp_log");
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

    // 自动添加tenantId
    public TenantSqlParser getTenantIdSqlParser() {
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue(RobotThreadLocalUtils.getTenantId());
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

    // 自动添加channelId
    public TenantSqlParser getChannelSqlParser() {
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue(RobotThreadLocalUtils.getChannelId());
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

    // 自动添加isValid
    public TenantSqlParser getIsValidSqlParser() {
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue(1L);
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

//--------------------------以下为改动源码!=的实现--------------------------
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
        public void processInsert(Insert insert) {

        }
    }

}