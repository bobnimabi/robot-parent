package com.robot.generator;


import com.bbin.utils.project.MyCodeGenUtils;

public class CodeGenerator {
    public static void main(String[] args) {
        //此类所在的项目名称
        String sourceProject = "robot-generator";
        //要生到的项目的名称
        String destProject = "robot-service";

        //游戏路径
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String projectPath = path.substring(0, path.indexOf("/" + sourceProject)) + "/" + destProject;
        //父package
        String parentName="com.robot";
        //模块名
        String modelName = "code";

        //配置数据库信息
        String driverClassName = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://mysql.com/second_robot?characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "0PKpK0JJD?mM";

        //生成表的名称
        String[] tables={
                "async_request_config",
                "connection_pool_config",
                "http_request_config",
                "platform",
                "request_record",
                "response_record",
                "tenant_robot",
                "tenant_robot_config",
                "tenant_robot_dict",
                "tenant_robot_domain",
                "tenant_robot_head",
                "tenant_robot_path",
                "tenant_robot_proxy",
                "tenant_robot_template"
        };
        MyCodeGenUtils.genCode(projectPath, driverClassName,url, username, password, modelName, parentName, tables);
    }
}