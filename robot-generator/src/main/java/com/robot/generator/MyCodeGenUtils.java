package com.robot.generator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrt on 2019/5/13 0013 下午 4:51
 * 功能：mybatis-plus代码生成工具类
 */
public class MyCodeGenUtils {

    /**
     * 自动生成代码
     * @param projectPath 项目路径
     * @param url 数据库url
     * @param username 数据库账号
     * @param password 数据库密码
     * @param modelName 子模块名称
     * @param parentName 父模块名称
     * @param tables 表
     * @
     */
    public static void genCode(String projectPath, String driverClassName, String url, String username,
            String password, String modelName, String parentName, String[] tables) {

        try {
            projectPath=projectPath+"\\";
            /***************************************以下配置不要动****************************************/
            // 代码生成器
            AutoGenerator mpg = new AutoGenerator();
            // 全局配置
            GlobalConfig gc = new GlobalConfig();
            gc.setOutputDir(projectPath + "src/main/java");
            gc.setAuthor("admin");
            gc.setOpen(false);
            gc.setSwagger2(false); //实体属性 Swagger2 注解
            gc.setFileOverride(false);
            mpg.setGlobalConfig(gc);

            // 数据源配置
            DataSourceConfig dsc = new DataSourceConfig();
            dsc.setUrl(url);
            // dsc.setSchemaName("public");
            dsc.setDriverName(driverClassName);
            dsc.setUsername(username);
            dsc.setPassword(password);
            mpg.setDataSource(dsc);

            // 包配置
            PackageConfig pc = new PackageConfig();
            pc.setModuleName(modelName);
            pc.setParent(parentName);
            mpg.setPackageInfo(pc);

            // 自定义配置
            InjectionConfig cfg = new InjectionConfig() {
                @Override
                public void initMap() {
                    // to do nothing
                }
            };

            // 如果模板引擎是 velocity
            String templatePath = "/templates/mapper.xml.vm";

            // 自定义输出配置
            List<FileOutConfig> focList = new ArrayList<>();
            // 自定义配置会被优先输出
            String finalProjectPath = projectPath;
            focList.add(new FileOutConfig(templatePath) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                    return finalProjectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                            + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                }
            });

            cfg.setFileOutConfigList(focList);
            mpg.setCfg(cfg);

            // 配置模板
            TemplateConfig templateConfig = new TemplateConfig();

            // 配置自定义输出模板
            //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
            // templateConfig.setEntity("templates/entity2.java");
            // templateConfig.setService();
            // templateConfig.setController();

            templateConfig.setXml(null);
            mpg.setTemplate(templateConfig);

            // 策略配置
            StrategyConfig strategy = new StrategyConfig();
            strategy.setNaming(NamingStrategy.underline_to_camel);
            strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
            strategy.setEntityLombokModel(true);
            strategy.setRestControllerStyle(true);
//        strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");
            strategy.setInclude(tables);
//        strategy.setSuperEntityColumns("id");
            strategy.setControllerMappingHyphenStyle(true);
            strategy.setTablePrefix(pc.getModuleName() + "_");
            mpg.setStrategy(strategy);
            mpg.setTemplateEngine(new VelocityTemplateEngine());
            mpg.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
