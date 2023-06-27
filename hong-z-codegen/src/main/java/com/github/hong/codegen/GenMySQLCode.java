package com.github.hong.codegen;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.converts.OracleTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.io.File;

/**
 * 代码生成器
 *
 * @author hanson
 * @link <a href="https://baomidou.com/pages/981406/#%E5%9F%BA%E7%A1%80%E9%85%8D%E7%BD%AE">使用参考</a>
 * @since 2023/5/11
 */
public class GenMySQLCode {

    public static final String THIS_MODULE_NAME = "hong-z-codegen"; //当前项目名称

    public static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/hong?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=GMT%2B8";

    public static final String USERNAME = "root";

    public static final String PASSWORD = "root";


    public static void main(String[] args) {

        String projectPath = System.getProperty("user.dir");  //获取当前项目的 文件夹地址

        if (!projectPath.endsWith(THIS_MODULE_NAME)) {  //解决IDEA中 项目目录问题
            projectPath += File.separator + THIS_MODULE_NAME;
        }
        String finalProjectPath = projectPath;
        FastAutoGenerator.create(
                        new DataSourceConfig.Builder(JDBC_URL, USERNAME, PASSWORD)
                                .typeConvert(
                                        new OracleTypeConvert() //MySqlTypeConvert
                                )
                )
                .globalConfig(builder -> builder.author("hansonreal") // 设置作者
                        //.enableSwagger() // 开启 swagger 模式
                        .fileOverride() // 覆盖已生成文件
                        .outputDir(finalProjectPath + "/src/main/java")// 指定输出目录
                        .dateType(DateType.ONLY_DATE)//
                        .disableOpenDir()//不打开资源管理器
                        .enableSwagger()// 开启swagger
                )
                .packageConfig(builder -> {
                    builder.parent("com.github.hong") // 设置父包名
                            //.moduleName("pay") // 设置父包模块名,系统表不需要父包模块名称ls.ov.ipay.thirdpay.wx.entity
                            .entity("entity.auth")//实体包名
                            .mapper("auth.mapper")// Mapper接口目录
                            .xml("auth.mapper") //xml目录
                            .service("auth.service")//service 目录
                            .serviceImpl("auth.service.impl")//service impl 目录
                            .controller("auth.web.ctrl") //ctrl 目录
                    ;
                })
                .strategyConfig(builder ->
                        // 设置需要生成的表名
                        builder.addInclude(
                                        //"T_SYS_CONFIG", "T_SYS_DICT", "T_SYS_DICT_ITEM"
                                        "T_SYS_USER","T_SYS_USER_AUTH"
                                //"T_SYS_LOG"
                                )
                                .addTablePrefix("a_", "t_", "c_", "pf_") // 设置过滤表前缀
                                .entityBuilder() // 实体构建器
                                .idType(IdType.NONE)//主键生成策略，默认的雪花算法
                                .columnNaming(NamingStrategy.underline_to_camel) // 下划线到驼峰的命名方式
                                .naming(NamingStrategy.underline_to_camel) // 下划线到驼峰的命名方式
                                .enableLombok() // 开启lombok
                                .enableTableFieldAnnotation() // 自动添加 field注解
                                .build()
                                .mapperBuilder() //mapper构建器
                                .enableBaseResultMap() //生成resultMap
                                .enableBaseColumnList() //XML中生成基础列
                                .build()
                                .serviceBuilder() //service构建器
                                .formatServiceImplFileName("%sService")// IUserService接口实现类UserServiceImpl 改为 UserService
                                .build()
                                .controllerBuilder() //controller构建器
                                .enableRestStyle()
                                .enableHyphenStyle())
                .templateEngine(new VelocityTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
}
