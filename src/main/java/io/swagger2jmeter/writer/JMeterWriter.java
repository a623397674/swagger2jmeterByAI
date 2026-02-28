package io.swagger2jmeter.writer;

import io.swagger2jmeter.model.*;
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

public class JMeterWriter {

    public Document readJMeterFile(String filePath) throws DocumentException {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            return createNewJMeterDocument();
        } else {
            return createNewJMeterDocument();
        }
    }
    
    private Document createNewJMeterDocument() {
        org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
        
        org.dom4j.Element root = document.addElement("jmeterTestPlan");
        root.addAttribute("version", "1.2");
        root.addAttribute("properties", "5.0");
        root.addAttribute("jmeter", "5.6.2");
        
        org.dom4j.Element hashTree1 = root.addElement("hashTree");
        
        // 1. 添加TestPlan
        org.dom4j.Element testPlan = hashTree1.addElement("TestPlan");
        testPlan.addAttribute("guiclass", "TestPlanGui");
        testPlan.addAttribute("testclass", "TestPlan");
        testPlan.addAttribute("testname", "测试计划");
        testPlan.addAttribute("enabled", "true");
        testPlan.addElement("boolProp").addAttribute("name", "TestPlan.functional_mode").setText("false");
        testPlan.addElement("boolProp").addAttribute("name", "TestPlan.serialize_threadgroups").setText("false");
        
        org.dom4j.Element userVariables = testPlan.addElement("elementProp").addAttribute("name", "TestPlan.user_defined_variables").addAttribute("elementType", "Arguments").addAttribute("guiclass", "ArgumentsPanel").addAttribute("testclass", "Arguments").addAttribute("testname", "用户定义的变量").addAttribute("enabled", "true");
        userVariables.addElement("collectionProp").addAttribute("name", "Arguments.arguments");
        
        testPlan.addElement("boolProp").addAttribute("name", "TestPlan.tearDown_on_shutdown").setText("true");
        
        hashTree1.addElement("hashTree"); // TestPlan的hashTree
        
        // 2. 添加第一个HeaderManager
        org.dom4j.Element headerManager1 = hashTree1.addElement("HeaderManager");
        headerManager1.addAttribute("guiclass", "HeaderPanel");
        headerManager1.addAttribute("testclass", "HeaderManager");
        headerManager1.addAttribute("testname", "HTTP信息头");
        headerManager1.addAttribute("enabled", "false");
        
        org.dom4j.Element headers1 = headerManager1.addElement("collectionProp").addAttribute("name", "HeaderManager.headers");
        
        org.dom4j.Element deviceIdHeader = headers1.addElement("elementProp").addAttribute("name", "").addAttribute("elementType", "Header");
        deviceIdHeader.addElement("stringProp").addAttribute("name", "Header.name").setText("deviceId");
        deviceIdHeader.addElement("stringProp").addAttribute("name", "Header.value").setText("${deviceId}");
        
        org.dom4j.Element tokenHeader = headers1.addElement("elementProp").addAttribute("name", "").addAttribute("elementType", "Header");
        tokenHeader.addElement("stringProp").addAttribute("name", "Header.name").setText("token");
        tokenHeader.addElement("stringProp").addAttribute("name", "Header.value").setText("${token}");
        
        hashTree1.addElement("hashTree"); // HeaderManager1的hashTree
        
        // 3. 添加第二个HeaderManager
        org.dom4j.Element headerManager2 = hashTree1.addElement("HeaderManager");
        headerManager2.addAttribute("guiclass", "HeaderPanel");
        headerManager2.addAttribute("testclass", "HeaderManager");
        headerManager2.addAttribute("testname", "HTTP信息头-固定sn");
        headerManager2.addAttribute("enabled", "true");
        
        org.dom4j.Element headers2 = headerManager2.addElement("collectionProp").addAttribute("name", "HeaderManager.headers");
        
        org.dom4j.Element contentTypeHeader = headers2.addElement("elementProp").addAttribute("name", "Content-Type").addAttribute("elementType", "Header");
        contentTypeHeader.addElement("stringProp").addAttribute("name", "Header.name").setText("Content-Type");
        contentTypeHeader.addElement("stringProp").addAttribute("name", "Header.value").setText("application/json");
        
        org.dom4j.Element authHeader = headers2.addElement("elementProp").addAttribute("name", "").addAttribute("elementType", "Header");
        authHeader.addElement("stringProp").addAttribute("name", "Header.name").setText("Authorization");
        authHeader.addElement("stringProp").addAttribute("name", "Header.value").setText("${token}");
        
        hashTree1.addElement("hashTree"); // HeaderManager2的hashTree
        
        // 4. 添加固定用户-dev参数表
        org.dom4j.Element devParams = hashTree1.addElement("Arguments");
        devParams.addAttribute("guiclass", "ArgumentsPanel");
        devParams.addAttribute("testclass", "Arguments");
        devParams.addAttribute("testname", "固定用户-dev");
        devParams.addAttribute("enabled", "true");
        
        hashTree1.addElement("hashTree"); // devParams的hashTree
        
        // 5. 添加固定用户 【tiger-api】参数表
        org.dom4j.Element tigerParams = hashTree1.addElement("Arguments");
        tigerParams.addAttribute("guiclass", "ArgumentsPanel");
        tigerParams.addAttribute("testclass", "Arguments");
        tigerParams.addAttribute("testname", "固定用户 【tiger-api】");
        tigerParams.addAttribute("enabled", "false");
        
        hashTree1.addElement("hashTree"); // tigerParams的hashTree
        
        // 6. 添加固定用户 【product】参数表
        org.dom4j.Element productParams = hashTree1.addElement("Arguments");
        productParams.addAttribute("guiclass", "ArgumentsPanel");
        productParams.addAttribute("testclass", "Arguments");
        productParams.addAttribute("testname", "固定用户 【product】");
        productParams.addAttribute("enabled", "false");
        
        hashTree1.addElement("hashTree"); // productParams的hashTree
        
        // 7. 添加察看结果树
        org.dom4j.Element viewResultsTree = hashTree1.addElement("ResultCollector");
        viewResultsTree.addAttribute("guiclass", "ViewResultsFullVisualizer");
        viewResultsTree.addAttribute("testclass", "ResultCollector");
        viewResultsTree.addAttribute("testname", "察看结果树");
        viewResultsTree.addAttribute("enabled", "true");
        viewResultsTree.addElement("boolProp").addAttribute("name", "ResultCollector.error_logging").setText("false");
        
        hashTree1.addElement("hashTree"); // viewResultsTree的hashTree
        
        // 8. 添加察看结果树(异常)
        org.dom4j.Element viewResultsTreeError = hashTree1.addElement("ResultCollector");
        viewResultsTreeError.addAttribute("guiclass", "ViewResultsFullVisualizer");
        viewResultsTreeError.addAttribute("testclass", "ResultCollector");
        viewResultsTreeError.addAttribute("testname", "察看结果树(异常)");
        viewResultsTreeError.addAttribute("enabled", "true");
        viewResultsTreeError.addElement("boolProp").addAttribute("name", "ResultCollector.error_logging").setText("true");
        
        hashTree1.addElement("hashTree"); // viewResultsTreeError的hashTree
        
        // 9. 添加用表格察看结果
        org.dom4j.Element tableResults = hashTree1.addElement("ResultCollector");
        tableResults.addAttribute("guiclass", "TableVisualizer");
        tableResults.addAttribute("testclass", "ResultCollector");
        tableResults.addAttribute("testname", "用表格察看结果");
        tableResults.addAttribute("enabled", "true");
        tableResults.addElement("boolProp").addAttribute("name", "ResultCollector.error_logging").setText("false");
        
        hashTree1.addElement("hashTree"); // tableResults的hashTree
        
        // 10. 添加聚合报告
        org.dom4j.Element aggregateReport = hashTree1.addElement("ResultCollector");
        aggregateReport.addAttribute("guiclass", "StatVisualizer");
        aggregateReport.addAttribute("testclass", "ResultCollector");
        aggregateReport.addAttribute("testname", "聚合报告");
        aggregateReport.addAttribute("enabled", "true");
        aggregateReport.addElement("boolProp").addAttribute("name", "ResultCollector.error_logging").setText("false");
        
        hashTree1.addElement("hashTree"); // aggregateReport的hashTree
        
        // 11. 添加汇总报告
        org.dom4j.Element summaryReport = hashTree1.addElement("ResultCollector");
        summaryReport.addAttribute("guiclass", "SummaryReport");
        summaryReport.addAttribute("testclass", "ResultCollector");
        summaryReport.addAttribute("testname", "汇总报告");
        summaryReport.addAttribute("enabled", "true");
        summaryReport.addElement("boolProp").addAttribute("name", "ResultCollector.error_logging").setText("false");
        
        hashTree1.addElement("hashTree"); // summaryReport的hashTree
        
        // 12. 添加ThreadGroup
        org.dom4j.Element threadGroup = hashTree1.addElement("ThreadGroup");
        threadGroup.addAttribute("guiclass", "ThreadGroupGui");
        threadGroup.addAttribute("testclass", "ThreadGroup");
        threadGroup.addAttribute("testname", "回归测试");
        threadGroup.addAttribute("enabled", "true");
        threadGroup.addElement("stringProp").addAttribute("name", "ThreadGroup.num_threads").setText("1");
        threadGroup.addElement("stringProp").addAttribute("name", "ThreadGroup.ramp_time").setText("1");
        threadGroup.addElement("stringProp").addAttribute("name", "ThreadGroup.duration").setText("600");
        threadGroup.addElement("stringProp").addAttribute("name", "ThreadGroup.delay").setText("1");
        threadGroup.addElement("boolProp").addAttribute("name", "ThreadGroup.same_user_on_next_iteration").setText("false");
        threadGroup.addElement("stringProp").addAttribute("name", "ThreadGroup.on_sample_error").setText("continue");
        
        org.dom4j.Element mainController = threadGroup.addElement("elementProp").addAttribute("name", "ThreadGroup.main_controller").addAttribute("elementType", "LoopController").addAttribute("guiclass", "LoopControlPanel").addAttribute("testclass", "LoopController").addAttribute("testname", "循环控制器").addAttribute("enabled", "true");
        mainController.addElement("stringProp").addAttribute("name", "LoopController.loops").setText("1");
        mainController.addElement("boolProp").addAttribute("name", "LoopController.continue_forever").setText("false");
        
        threadGroup.addElement("boolProp").addAttribute("name", "ThreadGroup.delayedStart").setText("false");
        threadGroup.addElement("boolProp").addAttribute("name", "ThreadGroup.scheduler").setText("false");
        
        // ThreadGroup的hashTree
        org.dom4j.Element threadGroupHashTree = hashTree1.addElement("hashTree");
        
        // 添加高斯随机定时器
        org.dom4j.Element gaussianTimer = threadGroupHashTree.addElement("GaussianRandomTimer");
        gaussianTimer.addAttribute("guiclass", "GaussianRandomTimerGui");
        gaussianTimer.addAttribute("testclass", "GaussianRandomTimer");
        gaussianTimer.addAttribute("testname", "高斯随机定时器");
        gaussianTimer.addAttribute("enabled", "true");
        gaussianTimer.addElement("stringProp").addAttribute("name", "ConstantTimer.delay").setText("10");
        gaussianTimer.addElement("stringProp").addAttribute("name", "RandomTimer.range").setText("0.0");
        
        threadGroupHashTree.addElement("hashTree"); // GaussianRandomTimer的hashTree
        
        return document;
    }

    public void syncAPIsToJMeter(Document document, SwaggerModel swaggerModel) {
        Element rootElement = document.getRootElement();

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

        if (testPlan != null && swaggerModel.getContactName() != null && !swaggerModel.getContactName().isEmpty()) {
            testPlan.addAttribute("testname", swaggerModel.getContactName());
        }

        Element testPlanHashTree = null;
        for (Element child : rootChildren) {
            if (child.getName().equals("hashTree")) {
                testPlanHashTree = child;
                break;
            }
        }

        if (testPlanHashTree != null) {
            checkAndAddFixedUserParameters(testPlanHashTree, swaggerModel);
        }

        Element threadGroup = findThreadGroup(rootElement);
        if (threadGroup == null) {
            System.err.println("未找到ThreadGroup元素");
            return;
        }

        updateThreadGroupConfig(threadGroup);

        Element threadGroupHashTree = null;
        Element parent = threadGroup.getParent();
        List<Element> siblings = parent.elements();
        int threadGroupIndex = siblings.indexOf(threadGroup);
        
        for (int i = threadGroupIndex + 1; i < siblings.size(); i++) {
            Element sibling = siblings.get(i);
            if (sibling.getName().equals("hashTree")) {
                threadGroupHashTree = sibling;
                break;
            }
        }
        
        if (threadGroupHashTree == null) {
            threadGroupHashTree = parent.addElement("hashTree");
        }

        for (Tag tag : swaggerModel.getTags()) {
            syncTagToJMeter(threadGroupHashTree, tag, swaggerModel);
        }

        enableAllTransactionControllers(rootElement);
    }

    private void updateThreadGroupConfig(Element threadGroup) {
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

    private void syncTagToJMeter(Element threadGroupHashTree, Tag tag, SwaggerModel swaggerModel) {
        Element transactionController = findOrCreateTransactionController(threadGroupHashTree, tag.getName());

        Element tcHashTree = findHashTreeForElement(transactionController);
        if (tcHashTree == null) {
            tcHashTree = transactionController.getParent().addElement("hashTree");
        }

        for (ApiPath apiPath : swaggerModel.getPaths()) {
            if (apiPath.getTags().contains(tag.getName())) {
                syncApiToJMeter(tcHashTree, apiPath);
            }
        }
    }

    private Element findHashTreeForElement(Element element) {
        Element parent = element.getParent();
        List<Element> siblings = parent.elements();
        int elementIndex = siblings.indexOf(element);
        
        for (int i = elementIndex + 1; i < siblings.size(); i++) {
            if (siblings.get(i).getName().equals("hashTree")) {
                return siblings.get(i);
            }
        }
        
        return null;
    }

    private Element findOrCreateTransactionController(Element parent, String tagName) {
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

        Element tcElement = parent.addElement("TransactionController");
        tcElement.addAttribute("guiclass", "TransactionControllerGui");
        tcElement.addAttribute("testclass", "TransactionController");
        tcElement.addAttribute("testname", tagName);
        tcElement.addAttribute("enabled", "true");

        tcElement.addElement("boolProp").addAttribute("name", "TransactionController.includeTimers").setText("false");
        tcElement.addElement("boolProp").addAttribute("name", "TransactionController.parent").setText("false");

        List<Element> allChildren = parent.elements();
        int tcIndex = allChildren.indexOf(tcElement);
        
        boolean hasHashTree = false;
        if (tcIndex < allChildren.size() - 1) {
            Element nextElement = allChildren.get(tcIndex + 1);
            if (nextElement.getName().equals("hashTree")) {
                hasHashTree = true;
            }
        }
        
        if (!hasHashTree) {
            parent.addElement("hashTree");
        }

        return tcElement;
    }

    private void syncApiToJMeter(Element tcHashTree, ApiPath apiPath) {
        if (apiExists(tcHashTree, apiPath)) {
            return;
        }

        Element httpSampler = tcHashTree.addElement("HTTPSamplerProxy");
        httpSampler.addAttribute("guiclass", "HttpTestSampleGui");
        httpSampler.addAttribute("testclass", "HTTPSamplerProxy");
        httpSampler.addAttribute("testname", "    ↓--" + apiPath.getSummary());
        httpSampler.addAttribute("enabled", "true");

        httpSampler.addElement("boolProp").addAttribute("name", "HTTPSampler.postBodyRaw").setText("true");

        Element arguments = httpSampler.addElement("elementProp").addAttribute("name", "HTTPsampler.Arguments").addAttribute("elementType", "Arguments");
        Element argumentsCollection = arguments.addElement("collectionProp").addAttribute("name", "Arguments.arguments");

        Element argument = argumentsCollection.addElement("elementProp").addAttribute("name", "").addAttribute("elementType", "HTTPArgument");
        argument.addElement("boolProp").addAttribute("name", "HTTPArgument.always_encode").setText("false");
        argument.addElement("stringProp").addAttribute("name", "Argument.value").setText(getRequestBody(apiPath));
        argument.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");

        httpSampler.addElement("stringProp").addAttribute("name", "HTTPSampler.domain").setText("${serverIP}");
        httpSampler.addElement("stringProp").addAttribute("name", "HTTPSampler.port").setText("${serverPort}");
        httpSampler.addElement("stringProp").addAttribute("name", "HTTPSampler.protocol").setText("${protocol}");
        httpSampler.addElement("stringProp").addAttribute("name", "HTTPSampler.path").setText(apiPath.getPath());
        httpSampler.addElement("stringProp").addAttribute("name", "HTTPSampler.method").setText(apiPath.getMethod());

        httpSampler.addElement("boolProp").addAttribute("name", "HTTPSampler.follow_redirects").setText("true");
        httpSampler.addElement("boolProp").addAttribute("name", "HTTPSampler.auto_redirects").setText("false");
        httpSampler.addElement("boolProp").addAttribute("name", "HTTPSampler.use_keepalive").setText("true");
        httpSampler.addElement("boolProp").addAttribute("name", "HTTPSampler.DO_MULTIPART_POST").setText("false");
        httpSampler.addElement("boolProp").addAttribute("name", "HTTPSampler.BROWSER_COMPATIBLE_MULTIPART").setText("true");
        httpSampler.addElement("boolProp").addAttribute("name", "HTTPSampler.image_parser").setText("false");
        httpSampler.addElement("boolProp").addAttribute("name", "HTTPSampler.concurrentDwn").setText("false");
        httpSampler.addElement("stringProp").addAttribute("name", "HTTPSampler.concurrentPool").setText("6");
        httpSampler.addElement("boolProp").addAttribute("name", "HTTPSampler.md5").setText("false");
        httpSampler.addElement("stringProp").addAttribute("name", "HTTPSampler.embedded_url_re").setText("");
        httpSampler.addElement("stringProp").addAttribute("name", "HTTPSampler.connect_timeout");
        httpSampler.addElement("stringProp").addAttribute("name", "HTTPSampler.response_timeout");

        // 添加HTTPSamplerProxy的hashTree
        Element httpSamplerHashTree = tcHashTree.addElement("hashTree");

        // 添加HTTP信息头管理器到HTTPSamplerProxy的hashTree中
        Element headerManager = httpSamplerHashTree.addElement("HeaderManager");
        headerManager.addAttribute("guiclass", "HeaderPanel");
        headerManager.addAttribute("testclass", "HeaderManager");
        headerManager.addAttribute("testname", "HTTP信息头");
        headerManager.addAttribute("enabled", "true");
        
        Element headers = headerManager.addElement("collectionProp").addAttribute("name", "HeaderManager.headers");

        // HeaderManager需要自己的hashTree
        httpSamplerHashTree.addElement("hashTree");

        // 生成接口后缀（从path中提取，去除/并转换为驼峰命名）
        String path = apiPath.getPath();
        String suffix = path.replace("/", "");
        // 简单处理，将-后的字符转换为大写
        StringBuilder sb = new StringBuilder(suffix);
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '-') {
                sb.deleteCharAt(i);
                if (i < sb.length()) {
                    sb.setCharAt(i, Character.toUpperCase(sb.charAt(i)));
                }
            }
        }
        suffix = sb.toString();
        if (suffix.isEmpty()) {
            suffix = "root";
        }

        // 添加响应提取器和对应的header参数到HTTPSamplerProxy的hashTree中
        addResponseExtractors(httpSamplerHashTree, suffix, headers);

        // 添加响应断言到HTTPSamplerProxy的hashTree中
        Element assertion = httpSamplerHashTree.addElement("ResponseAssertion");
        assertion.addAttribute("guiclass", "AssertionGui");
        assertion.addAttribute("testclass", "ResponseAssertion");
        assertion.addAttribute("testname", "响应断言");
        assertion.addAttribute("enabled", "true");
        
        Element assertionStrings = assertion.addElement("collectionProp").addAttribute("name", "Asserion.test_strings");
        assertionStrings.addElement("stringProp").addAttribute("name", "1").setText("200");
        assertion.addElement("stringProp").addAttribute("name", "Assertion.test_field").setText("Assertion.response_code");
        assertion.addElement("boolProp").addAttribute("name", "Assertion.assume_success").setText("false");
        assertion.addElement("intProp").addAttribute("name", "Assertion.test_type").setText("2");
        assertion.addElement("stringProp").addAttribute("name", "Assertion.custom_message").setText("");
        
        // ResponseAssertion需要自己的hashTree
        httpSamplerHashTree.addElement("hashTree");
    }

    private void addResponseExtractors(Element parent, String suffix, Element headers) {
        // 提取token并保存到header
        addRegexExtractor(parent, suffix + "Token", "token", "\"token\":\"([^\"]+)\"", headers);
        
        // 提取openId并保存到header
        addRegexExtractor(parent, suffix + "OpenId", "openId", "\"openId\":([^,]+)", headers);
        
        // 提取sumLightPoint并保存到header
        addRegexExtractor(parent, suffix + "SumLightPoint", "sumLightPoint", "\"sumLightPoint\":(\\d+)", headers);
        
        // 提取lightPoint并保存到header
        addRegexExtractor(parent, suffix + "LightPoint", "lightPoint", "\"lightPoint\":(\\d+)", headers);
        
        // 添加header参数
        if (headers != null) {
            // 添加token header
            Element tokenHeader = headers.addElement("elementProp").addAttribute("name", "").addAttribute("elementType", "Header");
            tokenHeader.addElement("stringProp").addAttribute("name", "Header.name").setText("token");
            tokenHeader.addElement("stringProp").addAttribute("name", "Header.value").setText("${" + suffix + "Token}");
            
            // 添加openId header
            Element openIdHeader = headers.addElement("elementProp").addAttribute("name", "").addAttribute("elementType", "Header");
            openIdHeader.addElement("stringProp").addAttribute("name", "Header.name").setText("openId");
            openIdHeader.addElement("stringProp").addAttribute("name", "Header.value").setText("${" + suffix + "OpenId}");
            
            // 添加sumLightPoint header
            Element sumLightPointHeader = headers.addElement("elementProp").addAttribute("name", "").addAttribute("elementType", "Header");
            sumLightPointHeader.addElement("stringProp").addAttribute("name", "Header.name").setText("sumLightPoint");
            sumLightPointHeader.addElement("stringProp").addAttribute("name", "Header.value").setText("${" + suffix + "SumLightPoint}");
            
            // 添加lightPoint header
            Element lightPointHeader = headers.addElement("elementProp").addAttribute("name", "").addAttribute("elementType", "Header");
            lightPointHeader.addElement("stringProp").addAttribute("name", "Header.name").setText("lightPoint");
            lightPointHeader.addElement("stringProp").addAttribute("name", "Header.value").setText("${" + suffix + "LightPoint}");
        }
    }

    private void addRegexExtractor(Element parent, String varName, String headerName, String regex, Element headers) {
        // 添加正则表达式提取器
        Element extractor = parent.addElement("RegexExtractor");
        extractor.addAttribute("guiclass", "RegexExtractorGui");
        extractor.addAttribute("testclass", "RegexExtractor");
        extractor.addAttribute("testname", varName + "提取器");
        extractor.addAttribute("enabled", "true");
        
        extractor.addElement("stringProp").addAttribute("name", "RegexExtractor.useHeaders").setText("false");
        extractor.addElement("stringProp").addAttribute("name", "RegexExtractor.refname").setText(varName);
        extractor.addElement("stringProp").addAttribute("name", "RegexExtractor.regex").setText(regex);
        extractor.addElement("stringProp").addAttribute("name", "RegexExtractor.template").setText("$1$");
        extractor.addElement("stringProp").addAttribute("name", "RegexExtractor.default").setText("not_found");
        extractor.addElement("stringProp").addAttribute("name", "RegexExtractor.match_number").setText("1");
        extractor.addElement("boolProp").addAttribute("name", "RegexExtractor.default_empty_value").setText("false");
        
        // RegexExtractor需要自己的hashTree
        parent.addElement("hashTree");
    }

    private boolean apiExists(Element tcHashTree, ApiPath apiPath) {
        List<Element> children = tcHashTree.elements();
        for (Element child : children) {
            if (child.getName().equals("HTTPSamplerProxy")) {
                String testName = child.attributeValue("testname");
                if (testName != null && testName.equals("    ↓--" + apiPath.getSummary())) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getRequestBody(ApiPath apiPath) {
        if (apiPath.getRequestExample() != null && !apiPath.getRequestExample().isEmpty()) {
            return apiPath.getRequestExample();
        }
        return "{}";
    }

    private void checkAndAddFixedUserParameters(Element testPlanHashTree, SwaggerModel swaggerModel) {
        Element devParams = null;
        Element tigerParams = null;
        Element productParams = null;

        List<Element> children = testPlanHashTree.elements();
        for (Element child : children) {
            if (child.getName().equals("Arguments")) {
                String testName = child.attributeValue("testname");
                if ("固定用户-dev".equals(testName)) {
                    devParams = child;
                } else if ("固定用户 【tiger-api】".equals(testName)) {
                    tigerParams = child;
                } else if ("固定用户 【product】".equals(testName)) {
                    productParams = child;
                }
            }
        }

        // 填充固定用户-dev参数表
        if (devParams != null) {
            populateFixedUserParameters(devParams, swaggerModel, true);
        }

        // 填充固定用户 【tiger-api】参数表
        if (tigerParams != null) {
            populateFixedUserParameters(tigerParams, swaggerModel, false);
        }

        // 填充固定用户 【product】参数表
        if (productParams != null) {
            populateFixedUserParameters(productParams, swaggerModel, false);
        }
    }

    private void populateFixedUserParameters(Element arguments, SwaggerModel swaggerModel, boolean isDev) {
        // 清除现有参数
        Element collectionProp = arguments.element("collectionProp");
        if (collectionProp == null) {
            collectionProp = arguments.addElement("collectionProp").addAttribute("name", "Arguments.arguments");
        } else {
            collectionProp.clearContent();
        }

        if (swaggerModel.getServerUrl() != null && !swaggerModel.getServerUrl().isEmpty()) {
            String serverUrl = swaggerModel.getServerUrl();
            String[] urlParts = serverUrl.replace("https://", "").replace("http://", "").split(":");
            String serverIP = urlParts[0];
            String serverPort = urlParts.length > 1 ? urlParts[1].split("/")[0] : "443";
            String protocol = serverUrl.startsWith("https") ? "https" : "http";

            addArgument(collectionProp, "serverIP", serverIP);
            addArgument(collectionProp, "serverPort", serverPort);
            addArgument(collectionProp, "protocol", protocol);
        }

        addArgument(collectionProp, "deviceId", "test-device-id");
        addArgument(collectionProp, "token", "test-token");
    }

    private void addArgument(Element collectionProp, String name, String value) {
        Element elementProp = collectionProp.addElement("elementProp");
        elementProp.addAttribute("name", name);
        elementProp.addAttribute("elementType", "Argument");
        elementProp.addElement("stringProp").addAttribute("name", "Argument.name").setText(name);
        elementProp.addElement("stringProp").addAttribute("name", "Argument.value").setText(value);
        elementProp.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
    }

    private void enableAllTransactionControllers(Element rootElement) {
        List<Element> allElements = new ArrayList<>();
        collectAllElements(rootElement, allElements);

        for (Element element : allElements) {
            if (element.getName().equals("TransactionController")) {
                element.addAttribute("enabled", "true");
            }
        }
    }

    private void collectAllElements(Element element, List<Element> allElements) {
        allElements.add(element);
        for (Element child : element.elements()) {
            collectAllElements(child, allElements);
        }
    }

    public void saveJMeterFile(Document document, String filePath) throws IOException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            XMLWriter xmlWriter = new XMLWriter(writer);
            xmlWriter.write(document);
        }
    }
}
