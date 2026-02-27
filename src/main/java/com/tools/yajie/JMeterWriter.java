package com.tools.yajie;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JMeter文件写入器
 * 功能：
 * 1. 读取现有JMeter文件
 * 2. 解析JMeter文件结构
 * 3. 同步API信息到JMeter文件
 * 4. 保存更新后的JMeter文件
 */
public class JMeterWriter {

    /**
     * 读取现有JMeter文件，如果文件不存在则创建新的
     */
    public Document readJMeterFile(String filePath) throws DocumentException {
        File file = new File(filePath);
        if (file.exists()) {
            //先删除，在创建
            file.delete();
            return createNewJMeterDocument();
//            SAXReader reader = new SAXReader();
//            return reader.read(file);
        } else {
            // 创建新的JMeter文件结构
            return createNewJMeterDocument();
        }
    }
    
    /**
     * 创建新的JMeter文件结构（参考yunying-cms-api.jmx模板）
     */
    private Document createNewJMeterDocument() {
        org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
        
        // 创建根元素
        org.dom4j.Element root = document.addElement("jmeterTestPlan");
        root.addAttribute("version", "1.2");
        root.addAttribute("properties", "5.0");
        root.addAttribute("jmeter", "5.6.2");
        
        // 添加hashTree
        org.dom4j.Element hashTree1 = root.addElement("hashTree");
        
        // 使用JMeterElementBean初始化占位元素
        JMeterElementBean elementBean = new JMeterElementBean();
        elementBean.initPlaceholderElements();
        
        // 添加占位元素到hashTree1
        for (Element element : elementBean.getElements()) {
            hashTree1.add(element);
        }
        
        // 填充TestPlan内容
        Element testPlan = elementBean.findElement(JMeterElementBean.TYPE_TEST_PLAN, "测试计划");
        if (testPlan != null) {
            testPlan.addElement("boolProp").addAttribute("name", "TestPlan.functional_mode").setText("false");
            testPlan.addElement("boolProp").addAttribute("name", "TestPlan.serialize_threadgroups").setText("false");
            
            // 添加用户定义的变量
            org.dom4j.Element userVariables = testPlan.addElement("elementProp").addAttribute("name", "TestPlan.user_defined_variables").addAttribute("elementType", "Arguments").addAttribute("guiclass", "ArgumentsPanel").addAttribute("testclass", "Arguments").addAttribute("testname", "用户定义的变量").addAttribute("enabled", "true");
            userVariables.addElement("collectionProp").addAttribute("name", "Arguments.arguments");
            
            testPlan.addElement("boolProp").addAttribute("name", "TestPlan.tearDown_on_shutdown").setText("true");
        }
        
        // 填充HTTP信息头内容
        Element headerManager1 = elementBean.findElement(JMeterElementBean.TYPE_HEADER_MANAGER, JMeterElementBean.NAME_HTTP_HEADER);
        if (headerManager1 != null) {
            org.dom4j.Element headers = headerManager1.addElement("collectionProp").addAttribute("name", "HeaderManager.headers");
            
            // 添加deviceId头
            org.dom4j.Element deviceIdHeader = headers.addElement("elementProp").addAttribute("name", "").addAttribute("elementType", "Header");
            deviceIdHeader.addElement("stringProp").addAttribute("name", "Header.name").setText("deviceId");
            deviceIdHeader.addElement("stringProp").addAttribute("name", "Header.value").setText("${deviceId}");
            
            // 添加token头
            org.dom4j.Element tokenHeader = headers.addElement("elementProp").addAttribute("name", "").addAttribute("elementType", "Header");
            tokenHeader.addElement("stringProp").addAttribute("name", "Header.name").setText("token");
            tokenHeader.addElement("stringProp").addAttribute("name", "Header.value").setText("${token}");
        }
        
        // 填充HTTP信息头-固定sn内容
        Element headerManager2 = elementBean.findElement(JMeterElementBean.TYPE_HEADER_MANAGER, JMeterElementBean.NAME_HTTP_HEADER_FIXED);
        if (headerManager2 != null) {
            org.dom4j.Element headers = headerManager2.addElement("collectionProp").addAttribute("name", "HeaderManager.headers");
            
            // 添加Content-Type头
            org.dom4j.Element contentTypeHeader = headers.addElement("elementProp").addAttribute("name", "Content-Type").addAttribute("elementType", "Header");
            contentTypeHeader.addElement("stringProp").addAttribute("name", "Header.name").setText("Content-Type");
            contentTypeHeader.addElement("stringProp").addAttribute("name", "Header.value").setText("application/json");
            
            // 添加Authorization头
            org.dom4j.Element authHeader = headers.addElement("elementProp").addAttribute("name", "").addAttribute("elementType", "Header");
            authHeader.addElement("stringProp").addAttribute("name", "Header.name").setText("Authorization");
            authHeader.addElement("stringProp").addAttribute("name", "Header.value").setText("${token}");
        }
        
        // 填充ThreadGroup内容
        Element threadGroup = elementBean.findElement(JMeterElementBean.TYPE_THREAD_GROUP, JMeterElementBean.NAME_THREAD_GROUP);
        if (threadGroup != null) {
            threadGroup.addElement("stringProp").addAttribute("name", "ThreadGroup.num_threads").setText("20");
            threadGroup.addElement("stringProp").addAttribute("name", "ThreadGroup.ramp_time").setText("2");
            threadGroup.addElement("stringProp").addAttribute("name", "ThreadGroup.duration").setText("600");
            threadGroup.addElement("stringProp").addAttribute("name", "ThreadGroup.delay").setText("1");
            threadGroup.addElement("boolProp").addAttribute("name", "ThreadGroup.same_user_on_next_iteration").setText("false");
            threadGroup.addElement("stringProp").addAttribute("name", "ThreadGroup.on_sample_error").setText("continue");
            
            // 添加main_controller
            org.dom4j.Element mainController = threadGroup.addElement("elementProp").addAttribute("name", "ThreadGroup.main_controller").addAttribute("elementType", "LoopController").addAttribute("guiclass", "LoopControlPanel").addAttribute("testclass", "LoopController").addAttribute("testname", "循环控制器").addAttribute("enabled", "true");
            mainController.addElement("stringProp").addAttribute("name", "LoopController.loops").setText("1");
            mainController.addElement("boolProp").addAttribute("name", "LoopController.continue_forever").setText("false");
            
            threadGroup.addElement("boolProp").addAttribute("name", "ThreadGroup.delayedStart").setText("false");
            threadGroup.addElement("boolProp").addAttribute("name", "ThreadGroup.scheduler").setText("false");
            
            // 获取ThreadGroup的hashTree
            Element threadGroupHashTree = elementBean.findElement(JMeterElementBean.TYPE_HASH_TREE, JMeterElementBean.NAME_THREAD_GROUP + "-hashTree");
            if (threadGroupHashTree != null) {
                // 添加高斯随机定时器
                org.dom4j.Element gaussianTimer = threadGroupHashTree.addElement("GaussianRandomTimer");
                gaussianTimer.addAttribute("guiclass", "GaussianRandomTimerGui");
                gaussianTimer.addAttribute("testclass", "GaussianRandomTimer");
                gaussianTimer.addAttribute("testname", "高斯随机定时器");
                gaussianTimer.addAttribute("enabled", "true");
                gaussianTimer.addElement("stringProp").addAttribute("name", "ConstantTimer.delay").setText("10");
                gaussianTimer.addElement("stringProp").addAttribute("name", "RandomTimer.range").setText("0.0");
                
                threadGroupHashTree.addElement("hashTree");
            }
        }
        
        return document;
    }

    /**
     * 同步API信息到JMeter文件
     */
    public void syncAPIsToJMeter(Document document, SwaggerModel swaggerModel) {
        // 获取JMeter文件的根元素
        Element rootElement = document.getRootElement();

        // 查找TestPlan元素
        Element testPlan = null;
        List<Element> rootChildren = rootElement.elements();
        for (Element child : rootChildren) {
            if (child.getName().equals("hashTree")) {
                List<Element> hashTreeChildren = child.elements();
                for (Element hashTreeChild : hashTreeChildren) {
                    if (hashTreeChild.getName().equals("TestPlan")) {
                        testPlan = hashTreeChild;
                        break;
                    }
                }
                break;
            }
        }

        // 更新TestPlan的名称
        if (testPlan != null && swaggerModel.getContactName() != null && !swaggerModel.getContactName().isEmpty()) {
            testPlan.addAttribute("testname", swaggerModel.getContactName());
        }

        // 查找TestPlan元素的hashTree
        Element testPlanHashTree = null;
        for (Element child : rootChildren) {
            if (child.getName().equals("hashTree")) {
                testPlanHashTree = child;
                break;
            }
        }

        //添加 dev-tiger
        if (testPlanHashTree != null) {
            // 检查并添加缺失的固定用户参数表
            checkAndAddFixedUserParameters(testPlanHashTree, swaggerModel);
        }

        // 查找ThreadGroup元素
        Element threadGroup = findThreadGroup(rootElement);
        if (threadGroup == null) {
            System.err.println("未找到ThreadGroup元素");
            return;
        }

        // 更新ThreadGroup配置
        updateThreadGroupConfig(threadGroup);

        // 获取ThreadGroup的hashTree
        Element threadGroupHashTree = null;
        Element parent = threadGroup.getParent();
        List<Element> siblings = parent.elements();
        int threadGroupIndex = siblings.indexOf(threadGroup);
        
        // 查找ThreadGroup后面的hashTree元素
        for (int i = threadGroupIndex + 1; i < siblings.size(); i++) {
            Element sibling = siblings.get(i);
            if (sibling.getName().equals("hashTree")) {
                threadGroupHashTree = sibling;
                break;
            }
        }
        
        if (threadGroupHashTree == null) {
            // 如果没有找到，创建一个
            threadGroupHashTree = parent.addElement("hashTree");
        }

        // 处理tags，创建或更新TransactionController
        for (Tag tag : swaggerModel.getTags()) {
            syncTagToJMeter(threadGroupHashTree, tag, swaggerModel);
        }

        // 启用所有TransactionController
        enableAllTransactionControllers(rootElement);
    }

    /**
     * 更新ThreadGroup配置
     * 注意：只在必要时添加缺失的元素，不覆盖用户在GUI下修改的参数
     */
    private void updateThreadGroupConfig(Element threadGroup) {
        // 检查是否存在ThreadGroup.num_threads元素，如果不存在则添加
        boolean hasNumThreads = false;
        for (Element child : threadGroup.elements()) {
            if (child.getName().equals("stringProp") && "ThreadGroup.num_threads".equals(child.attributeValue("name"))) {
                hasNumThreads = true;
                break;
            }
        }
        if (!hasNumThreads) {
            threadGroup.addElement("stringProp").addAttribute("name", "ThreadGroup.num_threads").setText("100");
        }

        // 检查是否存在ThreadGroup.ramp_time元素，如果不存在则添加
        boolean hasRampTime = false;
        for (Element child : threadGroup.elements()) {
            if (child.getName().equals("stringProp") && "ThreadGroup.ramp_time".equals(child.attributeValue("name"))) {
                hasRampTime = true;
                break;
            }
        }
        if (!hasRampTime) {
            threadGroup.addElement("stringProp").addAttribute("name", "ThreadGroup.ramp_time").setText("10");
        }

        // 检查是否存在LoopController.loops元素，如果不存在则添加
        Element loopController = null;
        for (Element child : threadGroup.elements()) {
            if (child.getName().equals("elementProp") && "ThreadGroup.main_controller".equals(child.attributeValue("name"))) {
                loopController = child;
                break;
            }
        }
        if (loopController != null) {
            boolean hasLoops = false;
            for (Element child : loopController.elements()) {
                if (child.getName().equals("stringProp") && "LoopController.loops".equals(child.attributeValue("name"))) {
                    hasLoops = true;
                    break;
                }
            }
            if (!hasLoops) {
                loopController.addElement("stringProp").addAttribute("name", "LoopController.loops").setText("100");
            }
        }
    }

    /**
     * 查找ThreadGroup元素
     */
    private Element findThreadGroup(Element element) {
        if (element.getName().equals("ThreadGroup")) {
            return element;
        }

        for (Element child : element.elements()) {
            Element result = findThreadGroup(child);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    /**
     * 同步tag到JMeter文件
     */
    private void syncTagToJMeter(Element threadGroupHashTree, Tag tag, SwaggerModel swaggerModel) {
        // 查找或创建TransactionController
        Element transactionController = findOrCreateTransactionController(threadGroupHashTree, tag.getName());

        // 获取TransactionController的hashTree
        Element tcHashTree = findHashTreeForElement(transactionController);
        if (tcHashTree == null) {
            tcHashTree = transactionController.getParent().addElement("hashTree");
        }

        // 同步该tag下的API
        for (ApiPath apiPath : swaggerModel.getPaths()) {
            if (apiPath.getTags().contains(tag.getName())) {
                syncApiToJMeter(tcHashTree, apiPath);
            }
        }
    }

    /**
     * 查找元素对应的hashTree元素
     */
    private Element findHashTreeForElement(Element element) {
        Element parent = element.getParent();
        List<Element> siblings = parent.elements();
        int elementIndex = siblings.indexOf(element);
        
        // 查找element后面的第一个hashTree元素
        for (int i = elementIndex + 1; i < siblings.size(); i++) {
            if (siblings.get(i).getName().equals("hashTree")) {
                return siblings.get(i);
            }
        }
        
        // 如果没有找到，返回null
        return null;
    }

    /**
     * 查找或创建TransactionController
     */
    private Element findOrCreateTransactionController(Element parent, String tagName) {
        // 查找现有的TransactionController
        List<Element> children = parent.elements();
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            if (child.getName().equals("TransactionController")) {
                String testName = child.attributeValue("testname");
                if (testName != null && testName.equals(tagName)) {
                    return child;
                }
            }
        }

        // 创建新的TransactionController
        Element tcElement = parent.addElement("TransactionController");
        tcElement.addAttribute("guiclass", "TransactionControllerGui");
        tcElement.addAttribute("testclass", "TransactionController");
        tcElement.addAttribute("testname", tagName);
        tcElement.addAttribute("enabled", "true");

        // 添加子元素
        tcElement.addElement("boolProp").addAttribute("name", "TransactionController.includeTimers").setText("false");
        tcElement.addElement("boolProp").addAttribute("name", "TransactionController.parent").setText("false");

        // 确保在TransactionController后面添加对应的hashTree元素
        // 先获取所有子元素
        List<Element> allChildren = parent.elements();
        // 找到新创建的TransactionController的索引
        int tcIndex = allChildren.indexOf(tcElement);
        
        // 检查TransactionController后面是否已经有hashTree元素
        boolean hasHashTree = false;
        if (tcIndex < allChildren.size() - 1) {
            Element nextElement = allChildren.get(tcIndex + 1);
            if (nextElement.getName().equals("hashTree")) {
                hasHashTree = true;
            }
        }
        
        // 如果没有，添加一个
        if (!hasHashTree) {
            parent.addElement("hashTree");
        }

        return tcElement;
    }

    /**
     * 同步API到JMeter文件
     */
    private void syncApiToJMeter(Element tcHashTree, ApiPath apiPath) {
        // 检查API是否已存在
        if (apiExists(tcHashTree, apiPath)) {
            return;
        }

        // 创建HTTPSamplerProxy
        Element httpSampler = tcHashTree.addElement("HTTPSamplerProxy");
        httpSampler.addAttribute("guiclass", "HttpTestSampleGui");
        httpSampler.addAttribute("testclass", "HTTPSamplerProxy");
        httpSampler.addAttribute("testname", "    ↓--" + apiPath.getSummary());
        httpSampler.addAttribute("enabled", "true");

        // 添加HTTP请求配置
        httpSampler.addElement("boolProp").addAttribute("name", "HTTPSampler.postBodyRaw").setText("true");

        // 添加参数
        Element arguments = httpSampler.addElement("elementProp").addAttribute("name", "HTTPsampler.Arguments").addAttribute("elementType", "Arguments");
        Element argumentsCollection = arguments.addElement("collectionProp").addAttribute("name", "Arguments.arguments");

        // 添加请求体
        Element argument = argumentsCollection.addElement("elementProp").addAttribute("name", "").addAttribute("elementType", "HTTPArgument");
        argument.addElement("boolProp").addAttribute("name", "HTTPArgument.always_encode").setText("false");
        argument.addElement("stringProp").addAttribute("name", "Argument.value").setText(getRequestBody(apiPath));
        argument.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");

        // 添加HTTP请求属性
        httpSampler.addElement("stringProp").addAttribute("name", "HTTPSampler.domain").setText("${serverIP}");
        httpSampler.addElement("stringProp").addAttribute("name", "HTTPSampler.port").setText("${serverPort}");
        httpSampler.addElement("stringProp").addAttribute("name", "HTTPSampler.protocol").setText("${protocol}");
        httpSampler.addElement("stringProp").addAttribute("name", "HTTPSampler.path").setText(apiPath.getPath());
        httpSampler.addElement("stringProp").addAttribute("name", "HTTPSampler.method").setText(apiPath.getMethod());

        // 添加其他HTTP配置
        httpSampler.addElement("boolProp").addAttribute("name", "HTTPSampler.follow_redirects").setText("true");
        httpSampler.addElement("boolProp").addAttribute("name", "HTTPSampler.auto_redirects").setText("false");
        httpSampler.addElement("boolProp").addAttribute("name", "HTTPSampler.use_keepalive").setText("true");
        httpSampler.addElement("boolProp").addAttribute("name", "HTTPSampler.DO_MULTIPART_POST").setText("false");
        httpSampler.addElement("boolProp").addAttribute("name", "HTTPSampler.BROWSER_COMPATIBLE_MULTIPART").setText("true");
        httpSampler.addElement("boolProp").addAttribute("name", "HTTPSampler.image_parser").setText("false");
        httpSampler.addElement("boolProp").addAttribute("name", "HTTPSampler.concurrentDwn").setText("false");
        httpSampler.addElement("stringProp").addAttribute("name", "HTTPSampler.concurrentPool").setText("6");
        httpSampler.addElement("boolProp").addAttribute("name", "HTTPSampler.md5").setText("false");
        httpSampler.addElement("intProp").addAttribute("name", "HTTPSampler.ipSourceType").setText("0");
        httpSampler.addElement("stringProp").addAttribute("name", "HTTPSampler.connect_timeout").setText("120000");
        httpSampler.addElement("stringProp").addAttribute("name", "HTTPSampler.response_timeout").setText("1200000");

        // 添加hashTree
        Element httpSamplerHashTree = tcHashTree.addElement("hashTree");

        // 添加响应断言
        Element responseAssertion = httpSamplerHashTree.addElement("ResponseAssertion");
        responseAssertion.addAttribute("guiclass", "AssertionGui");
        responseAssertion.addAttribute("testclass", "ResponseAssertion");
        responseAssertion.addAttribute("testname", "响应断言");
        responseAssertion.addAttribute("enabled", "true");

        // 添加断言配置
        Element assertionTestStrings = responseAssertion.addElement("collectionProp").addAttribute("name", "Asserion.test_strings");
        assertionTestStrings.addElement("stringProp").addAttribute("name", "723337899").setText("\"code\":200");
        responseAssertion.addElement("stringProp").addAttribute("name", "Assertion.test_field").setText("Assertion.response_data");
        responseAssertion.addElement("boolProp").addAttribute("name", "Assertion.assume_success").setText("false");
        responseAssertion.addElement("intProp").addAttribute("name", "Assertion.test_type").setText("2");
        responseAssertion.addElement("stringProp").addAttribute("name", "Assertion.custom_message").setText("");

        // 添加断言的hashTree
        httpSamplerHashTree.addElement("hashTree");
        
        // 为register接口添加提取器
        if (apiPath.getPath().equals("/activity/account/register")) {
            // 添加token提取器
            Element tokenExtractor = httpSamplerHashTree.addElement("RegexExtractor");
            tokenExtractor.addAttribute("guiclass", "RegexExtractorGui");
            tokenExtractor.addAttribute("testclass", "RegexExtractor");
            tokenExtractor.addAttribute("testname", "token提取器");
            tokenExtractor.addAttribute("enabled", "true");
            tokenExtractor.addElement("stringProp").addAttribute("name", "RegexExtractor.useHeaders").setText("false");
            tokenExtractor.addElement("stringProp").addAttribute("name", "RegexExtractor.refname").setText("token");
            tokenExtractor.addElement("stringProp").addAttribute("name", "RegexExtractor.regex").setText("\"token\":\"([^\"]+)\"");
            tokenExtractor.addElement("stringProp").addAttribute("name", "RegexExtractor.template").setText("$1$");
            tokenExtractor.addElement("stringProp").addAttribute("name", "RegexExtractor.default").setText("not_found");
            tokenExtractor.addElement("stringProp").addAttribute("name", "RegexExtractor.match_number").setText("1");
            tokenExtractor.addElement("boolProp").addAttribute("name", "RegexExtractor.default_empty_value").setText("false");
            httpSamplerHashTree.addElement("hashTree");
            
            // 添加openId提取器
            Element openIdExtractor = httpSamplerHashTree.addElement("RegexExtractor");
            openIdExtractor.addAttribute("guiclass", "RegexExtractorGui");
            openIdExtractor.addAttribute("testclass", "RegexExtractor");
            openIdExtractor.addAttribute("testname", "openId提取器");
            openIdExtractor.addAttribute("enabled", "true");
            openIdExtractor.addElement("stringProp").addAttribute("name", "RegexExtractor.useHeaders").setText("false");
            openIdExtractor.addElement("stringProp").addAttribute("name", "RegexExtractor.refname").setText("openId");
            openIdExtractor.addElement("stringProp").addAttribute("name", "RegexExtractor.regex").setText("\"openId\":([^,]+),");
            openIdExtractor.addElement("stringProp").addAttribute("name", "RegexExtractor.template").setText("$1$");
            openIdExtractor.addElement("stringProp").addAttribute("name", "RegexExtractor.default").setText("not_found");
            openIdExtractor.addElement("stringProp").addAttribute("name", "RegexExtractor.match_number").setText("1");
            openIdExtractor.addElement("boolProp").addAttribute("name", "RegexExtractor.default_empty_value").setText("false");
            httpSamplerHashTree.addElement("hashTree");
        }
    }

    /**
     * 检查API是否已存在
     */
    private boolean apiExists(Element tcHashTree, ApiPath apiPath) {
        for (Element child : tcHashTree.elements()) {
            if (child.getName().equals("HTTPSamplerProxy")) {
                String testName = child.attributeValue("testname");
                String path = child.elementText("HTTPSampler.path");
                String method = child.elementText("HTTPSampler.method");

                if (testName != null && testName.contains(apiPath.getSummary()) &&
                        path != null && path.equals(apiPath.getPath()) &&
                        method != null && method.equals(apiPath.getMethod())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取请求体
     */
    private String getRequestBody(ApiPath apiPath) {
        // 优先使用请求示例值
        if (apiPath.getRequestExample() != null && !apiPath.getRequestExample().isEmpty()) {
            return apiPath.getRequestExample();
        }

        // 根据API参数生成请求体
        if (apiPath.getParameters().isEmpty()) {
            return "{}";
        }

        StringBuilder requestBody = new StringBuilder("{");
        for (int i = 0; i < apiPath.getParameters().size(); i++) {
            ApiParameter param = apiPath.getParameters().get(i);
            if (param.getIn().equals("body")) {
                continue; // body参数在requestBodySchema中处理
            }

            requestBody.append("\"").append(param.getName()).append("\": ");
            if (param.getType().equals("string")) {
                requestBody.append("\"").append(getDefaultValue(param)).append("\"");
            } else {
                requestBody.append(getDefaultValue(param));
            }

            if (i < apiPath.getParameters().size() - 1) {
                requestBody.append(",");
            }
        }
        requestBody.append("}");

        return requestBody.toString();
    }

    /**
     * 获取参数默认值
     */
    private String getDefaultValue(ApiParameter param) {
        if (param.getType().equals("string")) {
            return "test";
        } else if (param.getType().equals("integer")) {
            return "1";
        } else if (param.getType().equals("boolean")) {
            return "true";
        } else {
            return "";
        }
    }

    /**
     * 启用所有TransactionController
     */
    private void enableAllTransactionControllers(Element element) {
        if (element.getName().equals("TransactionController")) {
            element.addAttribute("enabled", "true");
        }

        for (Element child : element.elements()) {
            enableAllTransactionControllers(child);
        }
    }

    /**
     * 检查并添加缺失的固定用户参数表（根据API信息动态生成）
     */
    private void checkAndAddFixedUserParameters(Element testPlanHashTree, SwaggerModel swaggerModel) {
        // 从Swagger文档中获取服务器URL
        String serverUrl = swaggerModel.getServerUrl();
        String serverIP = "alpha-api.guoyunwenlv.com";
        String serverPort = "";
        String protocol = "https";
        
        // 解析服务器URL，提取serverIP、serverPort和protocol
        if (serverUrl != null && !serverUrl.isEmpty()) {
            if (serverUrl.startsWith("http://")) {
                protocol = "http";
                String urlWithoutProtocol = serverUrl.substring(7);
                if (urlWithoutProtocol.contains(":")) {
                    serverIP = urlWithoutProtocol.split(":")[0];
                    serverPort = urlWithoutProtocol.split(":")[1];
                } else {
                    serverIP = urlWithoutProtocol;
                    serverPort = "";
                }
            } else if (serverUrl.startsWith("https://")) {
                protocol = "https";
                String urlWithoutProtocol = serverUrl.substring(8);
                if (urlWithoutProtocol.contains(":")) {
                    serverIP = urlWithoutProtocol.split(":")[0];
                    serverPort = urlWithoutProtocol.split(":")[1];
                } else {
                    serverIP = urlWithoutProtocol;
                    serverPort = "";
                }
            }
        }
        
        // 精准查找元素并填充参数
        List<Element> children = testPlanHashTree.elements();
        
        // 1. 填充固定用户-dev参数表
        for (Element child : children) {
            if (child.getName().equals("Arguments") && JMeterElementBean.NAME_FIXED_USER_DEV.equals(child.attributeValue("testname"))) {
                fillArgumentsElement(child, "localhost", "8080", "http", "041Yo5nl2EKlZg4of5ml2KGUXR1Yo5nV", "071pT7000XqaIV1kLd1003hu8O0pT709", "");
                break;
            }
        }
        
        // 2. 填充固定用户 【tiger-api】参数表
        for (Element child : children) {
            if (child.getName().equals("Arguments") && JMeterElementBean.NAME_FIXED_USER_TIGER.equals(child.attributeValue("testname"))) {
                fillArgumentsElement(child, serverIP, serverPort, protocol, "041Yo5nl2EKlZg4of5ml2KGUXR1Yo5nV", "071pT7000XqaIV1kLd1003hu8O0pT709", "");
                break;
            }
        }
        
        // 3. 填充固定用户 【product】参数表
        for (Element child : children) {
            if (child.getName().equals("Arguments") && JMeterElementBean.NAME_FIXED_USER_PRODUCT.equals(child.attributeValue("testname"))) {
                fillArgumentsElement(child, "yunying.guoyunwenlv.com", "8080", "https", "041Yo5nl2EKlZg4of5ml2KGUXR1Yo5nV", "071pT7000XqaIV1kLd1003hu8O0pT709", "");
                break;
            }
        }
    }
    
    /**
     * 填充Arguments元素的内容
     */
    private void fillArgumentsElement(Element arguments, String serverIP, String serverPort, String protocol, String code, String bCode, String token) {
        // 检查是否已经有collectionProp
        Element collectionProp = null;
        for (Element child : arguments.elements()) {
            if (child.getName().equals("collectionProp") && "Arguments.arguments".equals(child.attributeValue("name"))) {
                collectionProp = child;
                break;
            }
        }
        
        // 如果没有collectionProp，创建一个
        if (collectionProp == null) {
            collectionProp = arguments.addElement("collectionProp").addAttribute("name", "Arguments.arguments");
        }
        
        // 检查是否已经有参数，如果有则跳过
        boolean hasParams = false;
        for (Element child : collectionProp.elements()) {
            if (child.getName().equals("elementProp")) {
                hasParams = true;
                break;
            }
        }
        
        if (hasParams) {
            return;
        }
        
        // 添加tcp_sleep_time参数
        Element tcpSleepArg = collectionProp.addElement("elementProp").addAttribute("name", "tcp_sleep_time").addAttribute("elementType", "Argument");
        tcpSleepArg.addElement("stringProp").addAttribute("name", "Argument.name").setText("tcp_sleep_time");
        tcpSleepArg.addElement("stringProp").addAttribute("name", "Argument.value").setText("1000");
        tcpSleepArg.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
        tcpSleepArg.addElement("stringProp").addAttribute("name", "Argument.desc").setText("socket发送请求间隔毫秒数");
        
        // 添加------A用户--------
        Element aUserArg = collectionProp.addElement("elementProp").addAttribute("name", "------A用户--------").addAttribute("elementType", "Argument");
        aUserArg.addElement("stringProp").addAttribute("name", "Argument.name").setText("------A用户--------");
        aUserArg.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
        
        // 添加code参数
        Element codeArg = collectionProp.addElement("elementProp").addAttribute("name", "code").addAttribute("elementType", "Argument");
        codeArg.addElement("stringProp").addAttribute("name", "Argument.name").setText("code");
        codeArg.addElement("stringProp").addAttribute("name", "Argument.value").setText(code);
        codeArg.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
        
        // 添加------B用户--------
        Element bUserArg = collectionProp.addElement("elementProp").addAttribute("name", "------B用户--------").addAttribute("elementType", "Argument");
        bUserArg.addElement("stringProp").addAttribute("name", "Argument.name").setText("------B用户--------");
        bUserArg.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
        
        // 添加b_code参数
        Element bCodeArg = collectionProp.addElement("elementProp").addAttribute("name", "b_code").addAttribute("elementType", "Argument");
        bCodeArg.addElement("stringProp").addAttribute("name", "Argument.name").setText("b_code");
        bCodeArg.addElement("stringProp").addAttribute("name", "Argument.value").setText(bCode);
        bCodeArg.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
        
        // 添加serverIP参数
        Element serverIpArg = collectionProp.addElement("elementProp").addAttribute("name", "serverIP").addAttribute("elementType", "Argument");
        serverIpArg.addElement("stringProp").addAttribute("name", "Argument.name").setText("serverIP");
        serverIpArg.addElement("stringProp").addAttribute("name", "Argument.value").setText(serverIP);
        serverIpArg.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
        
        // 添加protocol参数
        Element protocolArg = collectionProp.addElement("elementProp").addAttribute("name", "protocol").addAttribute("elementType", "Argument");
        protocolArg.addElement("stringProp").addAttribute("name", "Argument.name").setText("protocol");
        protocolArg.addElement("stringProp").addAttribute("name", "Argument.value").setText(protocol);
        protocolArg.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
        
        // 添加serverPort参数
        Element serverPortArg = collectionProp.addElement("elementProp").addAttribute("name", "serverPort").addAttribute("elementType", "Argument");
        serverPortArg.addElement("stringProp").addAttribute("name", "Argument.name").setText("serverPort");
        serverPortArg.addElement("stringProp").addAttribute("name", "Argument.value").setText(serverPort);
        serverPortArg.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
        
        // 添加token参数
        Element tokenArg = collectionProp.addElement("elementProp").addAttribute("name", "token").addAttribute("elementType", "Argument");
        tokenArg.addElement("stringProp").addAttribute("name", "Argument.name").setText("token");
        tokenArg.addElement("stringProp").addAttribute("name", "Argument.value").setText(token);
        tokenArg.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
    }

    /**
     * 保存更新后的JMeter文件
     */
    public void saveJMeterFile(Document document, String filePath) throws IOException {
        // 设置XML格式化选项
        org.dom4j.io.OutputFormat format = org.dom4j.io.OutputFormat.createPrettyPrint();
        format.setIndentSize(2); // 设置缩进为2个空格
        format.setNewlines(true); // 启用换行
        format.setTrimText(true); // 修剪文本
        
        XMLWriter writer = new XMLWriter(new FileWriter(filePath), format);
        writer.write(document);
        writer.close();
    }
}
