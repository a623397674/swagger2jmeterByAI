package io.swagger2jmeter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger2jmeter.model.SwaggerModel;
import io.swagger2jmeter.parser.SwaggerParser;
import io.swagger2jmeter.writer.JMeterWriter;
import org.dom4j.Document;

public class Swagger2JMeter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        try {
            String swaggerSource = "https://alpha-api.guoyunwenlv.com/v3/api-docs";
            String jmeterFilePath = "./demo.jmx";

            if (args.length > 0) {
                swaggerSource = args[0];
            }
            if (args.length > 1) {
                jmeterFilePath = args[1];
            }

            SwaggerParser swaggerParser = new SwaggerParser();
            SwaggerModel swaggerModel;

            if (swaggerSource.startsWith("http://") || swaggerSource.startsWith("https://")) {
                swaggerModel = swaggerParser.parseFromUrl(swaggerSource);
                System.out.println("从URL解析Swagger文档：" + swaggerSource);
            } else {
                swaggerModel = swaggerParser.parseFromFile(swaggerSource);
                System.out.println("从本地文件解析Swagger文档：" + swaggerSource);
            }

            JMeterWriter jmeterWriter = new JMeterWriter();
            Document jmeterDocument = jmeterWriter.readJMeterFile(jmeterFilePath);

            jmeterWriter.syncAPIsToJMeter(jmeterDocument, swaggerModel);

            jmeterWriter.saveJMeterFile(jmeterDocument, jmeterFilePath);

            System.out.println("Swagger转JMeter脚本成功完成！");
            System.out.println("同步的API数量：" + swaggerModel.getPaths().size());
            System.out.println("更新后的JMeter文件：" + jmeterFilePath);

            executeJMeterTest(jmeterFilePath);

        } catch (Exception e) {
            System.err.println("转换过程中发生错误：" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void executeJMeterTest(String jmeterFilePath) {
        try {
            ProcessBuilder checkBuilder = new ProcessBuilder("which", "jmeter");
            Process checkProcess = checkBuilder.start();
            int checkExitCode = checkProcess.waitFor();
            
            if (checkExitCode != 0) {
                System.out.println("JMeter未安装，跳过测试执行。");
                System.out.println("请安装JMeter后手动执行测试：");
                System.out.println("jmeter -n -t " + jmeterFilePath + " -l results.jtl -e -o report");
                return;
            }
            
            System.out.println("开始执行JMeter测试...");
            
            String resultsDir = "results";
            String reportDir = "report";
            ProcessBuilder mkdirBuilder = new ProcessBuilder("mkdir", "-p", resultsDir, reportDir);
            mkdirBuilder.start().waitFor();
            
            String resultsFile = resultsDir + "/results.jtl";
            ProcessBuilder jmeterBuilder = new ProcessBuilder(
                "jmeter",
                "-n",
                "-t", jmeterFilePath,
                "-l", resultsFile,
                "-e",
                "-o", reportDir
            );
            
            Process jmeterProcess = jmeterBuilder.start();
            
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(jmeterProcess.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            
            java.io.BufferedReader errorReader = new java.io.BufferedReader(new java.io.InputStreamReader(jmeterProcess.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }
            
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
