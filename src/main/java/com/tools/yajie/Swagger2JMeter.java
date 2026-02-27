package com.tools.yajie;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Swagger/OpenAPI文档转JMeter脚本工具
 * 功能：
 * 1. 从Swagger/OpenAPI文档中提取API信息
 * 2. 解析现有JMeter文件结构
 * 3. 同步API标题tags
 * 4. 同步API的URL参数
 * 5. 以合适的结构解析成JMeter格式
 * 6. 追加到现有JMeter文件中
 */
public class Swagger2JMeter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        try {
            // 解析命令行参数 qcloud-cms-h5-hu.jmx yunying-cms-api-formatted.jmx
            String swaggerSource = "https://alpha-api.guoyunwenlv.com/v3/api-docs";
//            String jmeterFilePath = "/Users/xinmeiti/project/jmeter-jmx/demo.jmx";
            String jmeterFilePath = "./demo.jmx";

            if (args.length > 0) {
                swaggerSource = args[0];
            }
            if (args.length > 1) {
                jmeterFilePath = args[1];
            }

            // 1. 解析Swagger文档
            SwaggerParser swaggerParser = new SwaggerParser();
            SwaggerModel swaggerModel;

            if (swaggerSource.startsWith("http://") || swaggerSource.startsWith("https://")) {
                // 从URL解析
                swaggerModel = swaggerParser.parseFromUrl(swaggerSource);
                System.out.println("从URL解析Swagger文档：" + swaggerSource);
            } else {
                // 从本地文件解析
                swaggerModel = swaggerParser.parseFromFile(swaggerSource);
                System.out.println("从本地文件解析Swagger文档：" + swaggerSource);
            }

            // 2. 解析现有JMeter文件
            JMeterWriter jmeterWriter = new JMeterWriter();
            Document jmeterDocument = jmeterWriter.readJMeterFile(jmeterFilePath);

            // 3. 同步API信息到JMeter文件
            jmeterWriter.syncAPIsToJMeter(jmeterDocument, swaggerModel);

            // 4. 保存更新后的JMeter文件
            jmeterWriter.saveJMeterFile(jmeterDocument, jmeterFilePath);

            System.out.println("Swagger转JMeter脚本成功完成！");
            System.out.println("同步的API数量：" + swaggerModel.getPaths().size());
            System.out.println("更新后的JMeter文件：" + jmeterFilePath);

            // 执行JMeter测试
            executeJMeterTest(jmeterFilePath);

        } catch (Exception e) {
            System.err.println("转换过程中发生错误：" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 执行JMeter测试
     */
    private static void executeJMeterTest(String jmeterFilePath) {
        try {
            // 检查JMeter是否安装
            ProcessBuilder checkBuilder = new ProcessBuilder("which", "jmeter");
            Process checkProcess = checkBuilder.start();
            int checkExitCode = checkProcess.waitFor();
            
            if (checkExitCode != 0) {
                System.out.println("JMeter未安装，跳过测试执行。");
                System.out.println("请安装JMeter后手动执行测试：");
                System.out.println("jmeter -n -t " + jmeterFilePath + " -l results.jtl -e -o report");
                return;
            }
            
            // 执行JMeter测试
            System.out.println("开始执行JMeter测试...");
            
            // 创建结果目录
            String resultsDir = "results";
            String reportDir = "report";
            ProcessBuilder mkdirBuilder = new ProcessBuilder("mkdir", "-p", resultsDir, reportDir);
            mkdirBuilder.start().waitFor();
            
            // 构建JMeter命令
            String resultsFile = resultsDir + "/results.jtl";
            ProcessBuilder jmeterBuilder = new ProcessBuilder(
                "jmeter",
                "-n",
                "-t", jmeterFilePath,
                "-l", resultsFile,
                "-e",
                "-o", reportDir
            );
            
            // 执行命令并打印输出
            Process jmeterProcess = jmeterBuilder.start();
            
            // 读取输出
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(jmeterProcess.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            
            // 读取错误输出
            java.io.BufferedReader errorReader = new java.io.BufferedReader(new java.io.InputStreamReader(jmeterProcess.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }
            
            // 等待测试完成
            int exitCode = jmeterProcess.waitFor();
            
            if (exitCode == 0) {
                System.out.println("JMeter测试执行成功！");
                System.out.println("测试结果：" + resultsFile);
                System.out.println("HTML报告：" + reportDir);
            } else {
                System.out.println("JMeter测试执行失败，退出码：" + exitCode);
            }
        } catch (Exception e) {
            System.err.println("执行JMeter测试失败：" + e.getMessage());
        }
    }
}
