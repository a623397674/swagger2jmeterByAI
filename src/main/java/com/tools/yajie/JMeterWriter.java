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
     * 创建新的JMeter文件结构
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
        
        // 添加TestPlan
        org.dom4j.Element testPlan = hashTree1.addElement("TestPlan");
        testPlan.addAttribute("guiclass", "TestPlanGui");
        testPlan.addAttribute("testclass", "TestPlan");
        testPlan.addAttribute("testname", "测试计划");
        testPlan.addAttribute("enabled", "true");
        
        testPlan.addElement("stringProp").addAttribute("name", "TestPlan.comments").setText("");
        testPlan.addElement("boolProp").addAttribute("name", "TestPlan.functional_mode").setText("false");
        testPlan.addElement("boolProp").addAttribute("name", "TestPlan.tearDown_on_shutdown").setText("true");
        testPlan.addElement("boolProp").addAttribute("name", "TestPlan.serialize_threadgroups").setText("false");
        
        // 添加用户定义的变量
        org.dom4j.Element userVariables = testPlan.addElement("elementProp").addAttribute("name", "TestPlan.user_defined_variables").addAttribute("elementType", "Arguments").addAttribute("guiclass", "ArgumentsPanel").addAttribute("testclass", "Arguments").addAttribute("testname", "用户定义的变量").addAttribute("enabled", "true");
        userVariables.addElement("collectionProp").addAttribute("name", "Arguments.arguments");
        
        testPlan.addElement("stringProp").addAttribute("name", "TestPlan.user_define_classpath").setText("");
        
        // 添加TestPlan的hashTree
        org.dom4j.Element hashTree2 = hashTree1.addElement("hashTree");
        
        // 添加HTTP信息头-固定sn
        org.dom4j.Element headerManager = hashTree2.addElement("HeaderManager");
        headerManager.addAttribute("guiclass", "HeaderPanel");
        headerManager.addAttribute("testclass", "HeaderManager");
        headerManager.addAttribute("testname", "HTTP信息头-固定");
        headerManager.addAttribute("enabled", "true");
        org.dom4j.Element headers = headerManager.addElement("collectionProp").addAttribute("name", "HeaderManager.headers");
        
        // 添加Content-Type头
        org.dom4j.Element contentTypeHeader = headers.addElement("elementProp").addAttribute("name", "Content-Type").addAttribute("elementType", "Header");
        contentTypeHeader.addElement("stringProp").addAttribute("name", "Header.name").setText("Content-Type");
        contentTypeHeader.addElement("stringProp").addAttribute("name", "Header.value").setText("application/json");
        
        // 添加Authorization头
        org.dom4j.Element authHeader = headers.addElement("elementProp").addAttribute("name", "").addAttribute("elementType", "Header");
        authHeader.addElement("stringProp").addAttribute("name", "Header.name").setText("Authorization");
        authHeader.addElement("stringProp").addAttribute("name", "Header.value").setText("${token}");
        
        hashTree2.addElement("hashTree");
        
        // 添加察看结果树
        org.dom4j.Element resultCollector = hashTree2.addElement("ResultCollector");
        resultCollector.addAttribute("guiclass", "ViewResultsFullVisualizer");
        resultCollector.addAttribute("testclass", "ResultCollector");
        resultCollector.addAttribute("testname", "察看结果树");
        resultCollector.addAttribute("enabled", "true");
        resultCollector.addElement("boolProp").addAttribute("name", "ResultCollector.error_logging").setText("false");
        
        org.dom4j.Element saveConfig = resultCollector.addElement("objProp");
        saveConfig.addElement("name").setText("saveConfig");
        org.dom4j.Element saveConfigValue = saveConfig.addElement("value").addAttribute("class", "SampleSaveConfiguration");
        saveConfigValue.addElement("time").setText("true");
        saveConfigValue.addElement("latency").setText("true");
        saveConfigValue.addElement("timestamp").setText("true");
        saveConfigValue.addElement("success").setText("true");
        saveConfigValue.addElement("label").setText("true");
        saveConfigValue.addElement("code").setText("true");
        saveConfigValue.addElement("message").setText("true");
        saveConfigValue.addElement("threadName").setText("true");
        saveConfigValue.addElement("dataType").setText("true");
        saveConfigValue.addElement("encoding").setText("true");
        saveConfigValue.addElement("assertions").setText("true");
        saveConfigValue.addElement("subresults").setText("true");
        saveConfigValue.addElement("responseData").setText("true");
        saveConfigValue.addElement("samplerData").setText("true");
        saveConfigValue.addElement("xml").setText("true");
        saveConfigValue.addElement("fieldNames").setText("true");
        saveConfigValue.addElement("responseHeaders").setText("true");
        saveConfigValue.addElement("requestHeaders").setText("true");
        saveConfigValue.addElement("responseDataOnError").setText("false");
        saveConfigValue.addElement("saveAssertionResultsFailureMessage").setText("true");
        saveConfigValue.addElement("assertionsResultsToSave").setText("0");
        saveConfigValue.addElement("bytes").setText("true");
        saveConfigValue.addElement("url").setText("true");
        saveConfigValue.addElement("fileName").setText("true");
        saveConfigValue.addElement("hostname").setText("true");
        saveConfigValue.addElement("threadCounts").setText("true");
        saveConfigValue.addElement("sampleCount").setText("true");
        saveConfigValue.addElement("idleTime").setText("true");
        saveConfigValue.addElement("connectTime").setText("true");
        
        resultCollector.addElement("stringProp").addAttribute("name", "filename").setText("");
        
        hashTree2.addElement("hashTree");
        
        // 添加察看结果树(异常)
        org.dom4j.Element errorResultCollector = hashTree2.addElement("ResultCollector");
        errorResultCollector.addAttribute("guiclass", "ViewResultsFullVisualizer");
        errorResultCollector.addAttribute("testclass", "ResultCollector");
        errorResultCollector.addAttribute("testname", "察看结果树(异常)");
        errorResultCollector.addAttribute("enabled", "true");
        errorResultCollector.addElement("boolProp").addAttribute("name", "ResultCollector.error_logging").setText("true");
        
        org.dom4j.Element errorSaveConfig = errorResultCollector.addElement("objProp");
        errorSaveConfig.addElement("name").setText("saveConfig");
        org.dom4j.Element errorSaveConfigValue = errorSaveConfig.addElement("value").addAttribute("class", "SampleSaveConfiguration");
        errorSaveConfigValue.addElement("time").setText("true");
        errorSaveConfigValue.addElement("latency").setText("true");
        errorSaveConfigValue.addElement("timestamp").setText("true");
        errorSaveConfigValue.addElement("success").setText("true");
        errorSaveConfigValue.addElement("label").setText("true");
        errorSaveConfigValue.addElement("code").setText("true");
        errorSaveConfigValue.addElement("message").setText("true");
        errorSaveConfigValue.addElement("threadName").setText("true");
        errorSaveConfigValue.addElement("dataType").setText("true");
        errorSaveConfigValue.addElement("encoding").setText("true");
        errorSaveConfigValue.addElement("assertions").setText("true");
        errorSaveConfigValue.addElement("subresults").setText("true");
        errorSaveConfigValue.addElement("responseData").setText("true");
        errorSaveConfigValue.addElement("samplerData").setText("true");
        errorSaveConfigValue.addElement("xml").setText("true");
        errorSaveConfigValue.addElement("fieldNames").setText("true");
        errorSaveConfigValue.addElement("responseHeaders").setText("true");
        errorSaveConfigValue.addElement("requestHeaders").setText("true");
        errorSaveConfigValue.addElement("responseDataOnError").setText("false");
        errorSaveConfigValue.addElement("saveAssertionResultsFailureMessage").setText("true");
        errorSaveConfigValue.addElement("assertionsResultsToSave").setText("0");
        errorSaveConfigValue.addElement("bytes").setText("true");
        errorSaveConfigValue.addElement("url").setText("true");
        errorSaveConfigValue.addElement("fileName").setText("true");
        errorSaveConfigValue.addElement("hostname").setText("true");
        errorSaveConfigValue.addElement("threadCounts").setText("true");
        errorSaveConfigValue.addElement("sampleCount").setText("true");
        errorSaveConfigValue.addElement("idleTime").setText("true");
        errorSaveConfigValue.addElement("connectTime").setText("true");
        
        errorResultCollector.addElement("stringProp").addAttribute("name", "filename").setText("");
        
        hashTree2.addElement("hashTree");
        
        // 添加用表格察看结果
        org.dom4j.Element tableResultCollector = hashTree2.addElement("ResultCollector");
        tableResultCollector.addAttribute("guiclass", "TableVisualizer");
        tableResultCollector.addAttribute("testclass", "ResultCollector");
        tableResultCollector.addAttribute("testname", "用表格察看结果");
        tableResultCollector.addAttribute("enabled", "true");
        tableResultCollector.addElement("boolProp").addAttribute("name", "ResultCollector.error_logging").setText("false");
        
        org.dom4j.Element tableSaveConfig = tableResultCollector.addElement("objProp");
        tableSaveConfig.addElement("name").setText("saveConfig");
        org.dom4j.Element tableSaveConfigValue = tableSaveConfig.addElement("value").addAttribute("class", "SampleSaveConfiguration");
        tableSaveConfigValue.addElement("time").setText("true");
        tableSaveConfigValue.addElement("latency").setText("false");
        tableSaveConfigValue.addElement("timestamp").setText("false");
        tableSaveConfigValue.addElement("success").setText("true");
        tableSaveConfigValue.addElement("label").setText("false");
        tableSaveConfigValue.addElement("code").setText("false");
        tableSaveConfigValue.addElement("message").setText("false");
        tableSaveConfigValue.addElement("threadName").setText("false");
        tableSaveConfigValue.addElement("dataType").setText("false");
        tableSaveConfigValue.addElement("encoding").setText("false");
        tableSaveConfigValue.addElement("assertions").setText("false");
        tableSaveConfigValue.addElement("subresults").setText("false");
        tableSaveConfigValue.addElement("responseData").setText("false");
        tableSaveConfigValue.addElement("samplerData").setText("false");
        tableSaveConfigValue.addElement("xml").setText("false");
        tableSaveConfigValue.addElement("fieldNames").setText("false");
        tableSaveConfigValue.addElement("responseHeaders").setText("false");
        tableSaveConfigValue.addElement("requestHeaders").setText("false");
        tableSaveConfigValue.addElement("responseDataOnError").setText("false");
        tableSaveConfigValue.addElement("saveAssertionResultsFailureMessage").setText("false");
        tableSaveConfigValue.addElement("assertionsResultsToSave").setText("0");
        tableSaveConfigValue.addElement("url").setText("true");
        tableSaveConfigValue.addElement("threadCounts").setText("true");
        
        tableResultCollector.addElement("stringProp").addAttribute("name", "filename").setText("");
        
        hashTree2.addElement("hashTree");
        
        // 添加聚合报告
        org.dom4j.Element aggregateResultCollector = hashTree2.addElement("ResultCollector");
        aggregateResultCollector.addAttribute("guiclass", "StatVisualizer");
        aggregateResultCollector.addAttribute("testclass", "ResultCollector");
        aggregateResultCollector.addAttribute("testname", "聚合报告");
        aggregateResultCollector.addAttribute("enabled", "true");
        aggregateResultCollector.addElement("boolProp").addAttribute("name", "ResultCollector.error_logging").setText("false");
        
        org.dom4j.Element aggregateSaveConfig = aggregateResultCollector.addElement("objProp");
        aggregateSaveConfig.addElement("name").setText("saveConfig");
        org.dom4j.Element aggregateSaveConfigValue = aggregateSaveConfig.addElement("value").addAttribute("class", "SampleSaveConfiguration");
        aggregateSaveConfigValue.addElement("time").setText("true");
        aggregateSaveConfigValue.addElement("latency").setText("true");
        aggregateSaveConfigValue.addElement("timestamp").setText("true");
        aggregateSaveConfigValue.addElement("success").setText("true");
        aggregateSaveConfigValue.addElement("label").setText("true");
        aggregateSaveConfigValue.addElement("code").setText("true");
        aggregateSaveConfigValue.addElement("message").setText("true");
        aggregateSaveConfigValue.addElement("threadName").setText("true");
        aggregateSaveConfigValue.addElement("dataType").setText("true");
        aggregateSaveConfigValue.addElement("encoding").setText("false");
        aggregateSaveConfigValue.addElement("assertions").setText("true");
        aggregateSaveConfigValue.addElement("subresults").setText("true");
        aggregateSaveConfigValue.addElement("responseData").setText("false");
        aggregateSaveConfigValue.addElement("samplerData").setText("false");
        aggregateSaveConfigValue.addElement("xml").setText("false");
        aggregateSaveConfigValue.addElement("fieldNames").setText("false");
        aggregateSaveConfigValue.addElement("responseHeaders").setText("false");
        aggregateSaveConfigValue.addElement("requestHeaders").setText("false");
        aggregateSaveConfigValue.addElement("responseDataOnError").setText("false");
        aggregateSaveConfigValue.addElement("saveAssertionResultsFailureMessage").setText("false");
        aggregateSaveConfigValue.addElement("assertionsResultsToSave").setText("0");
        aggregateSaveConfigValue.addElement("bytes").setText("true");
        aggregateSaveConfigValue.addElement("threadCounts").setText("true");
        
        aggregateResultCollector.addElement("stringProp").addAttribute("name", "filename").setText("");
        
        hashTree2.addElement("hashTree");
        
        // 添加汇总报告
        org.dom4j.Element summaryResultCollector = hashTree2.addElement("ResultCollector");
        summaryResultCollector.addAttribute("guiclass", "SummaryReport");
        summaryResultCollector.addAttribute("testclass", "ResultCollector");
        summaryResultCollector.addAttribute("testname", "汇总报告");
        summaryResultCollector.addAttribute("enabled", "true");
        summaryResultCollector.addElement("boolProp").addAttribute("name", "ResultCollector.error_logging").setText("false");
        
        org.dom4j.Element summarySaveConfig = summaryResultCollector.addElement("objProp");
        summarySaveConfig.addElement("name").setText("saveConfig");
        org.dom4j.Element summarySaveConfigValue = summarySaveConfig.addElement("value").addAttribute("class", "SampleSaveConfiguration");
        summarySaveConfigValue.addElement("time").setText("true");
        summarySaveConfigValue.addElement("latency").setText("true");
        summarySaveConfigValue.addElement("timestamp").setText("true");
        summarySaveConfigValue.addElement("success").setText("true");
        summarySaveConfigValue.addElement("label").setText("true");
        summarySaveConfigValue.addElement("code").setText("true");
        summarySaveConfigValue.addElement("message").setText("true");
        summarySaveConfigValue.addElement("threadName").setText("true");
        summarySaveConfigValue.addElement("dataType").setText("true");
        summarySaveConfigValue.addElement("encoding").setText("false");
        summarySaveConfigValue.addElement("assertions").setText("true");
        summarySaveConfigValue.addElement("subresults").setText("true");
        summarySaveConfigValue.addElement("responseData").setText("false");
        summarySaveConfigValue.addElement("samplerData").setText("false");
        summarySaveConfigValue.addElement("xml").setText("false");
        summarySaveConfigValue.addElement("fieldNames").setText("true");
        summarySaveConfigValue.addElement("responseHeaders").setText("false");
        summarySaveConfigValue.addElement("requestHeaders").setText("false");
        summarySaveConfigValue.addElement("responseDataOnError").setText("false");
        summarySaveConfigValue.addElement("saveAssertionResultsFailureMessage").setText("true");
        summarySaveConfigValue.addElement("assertionsResultsToSave").setText("0");
        summarySaveConfigValue.addElement("bytes").setText("true");
        summarySaveConfigValue.addElement("sentBytes").setText("true");
        summarySaveConfigValue.addElement("url").setText("true");
        summarySaveConfigValue.addElement("threadCounts").setText("true");
        summarySaveConfigValue.addElement("idleTime").setText("true");
        summarySaveConfigValue.addElement("connectTime").setText("true");
        
        summaryResultCollector.addElement("stringProp").addAttribute("name", "filename").setText("");
        
        hashTree2.addElement("hashTree");
        
        // 添加ThreadGroup
        org.dom4j.Element threadGroup = hashTree2.addElement("ThreadGroup");
        threadGroup.addAttribute("guiclass", "ThreadGroupGui");
        threadGroup.addAttribute("testclass", "ThreadGroup");
        threadGroup.addAttribute("testname", "线程组");
        threadGroup.addAttribute("enabled", "true");
        
        threadGroup.addElement("stringProp").addAttribute("name", "ThreadGroup.on_sample_error").setText("continue");
        
        // 添加main_controller
        org.dom4j.Element mainController = threadGroup.addElement("elementProp").addAttribute("name", "ThreadGroup.main_controller").addAttribute("elementType", "LoopController").addAttribute("guiclass", "LoopControlPanel").addAttribute("testclass", "LoopController").addAttribute("testname", "循环控制器").addAttribute("enabled", "true");
        mainController.addElement("stringProp").addAttribute("name", "LoopController.loops").setText("1");
        mainController.addElement("boolProp").addAttribute("name", "LoopController.continue_forever").setText("false");
        
        threadGroup.addElement("stringProp").addAttribute("name", "ThreadGroup.num_threads").setText("10");
        threadGroup.addElement("stringProp").addAttribute("name", "ThreadGroup.ramp_time").setText("1");
        threadGroup.addElement("longProp").addAttribute("name", "ThreadGroup.start_time").setText("1419768021000");
        threadGroup.addElement("longProp").addAttribute("name", "ThreadGroup.end_time").setText("1419768621000");
        threadGroup.addElement("boolProp").addAttribute("name", "ThreadGroup.scheduler").setText("false");
        threadGroup.addElement("stringProp").addAttribute("name", "ThreadGroup.duration").setText("600");
        threadGroup.addElement("stringProp").addAttribute("name", "ThreadGroup.delay").setText("");
        threadGroup.addElement("boolProp").addAttribute("name", "ThreadGroup.same_user_on_next_iteration").setText("true");
        threadGroup.addElement("boolProp").addAttribute("name", "ThreadGroup.delayedStart").setText("false");
        
        // 添加ThreadGroup的hashTree
        org.dom4j.Element threadGroupHashTree = hashTree2.addElement("hashTree");
        
        // 添加高斯随机定时器
        org.dom4j.Element gaussianTimer = threadGroupHashTree.addElement("GaussianRandomTimer");
        gaussianTimer.addAttribute("guiclass", "GaussianRandomTimerGui");
        gaussianTimer.addAttribute("testclass", "GaussianRandomTimer");
        gaussianTimer.addAttribute("testname", "高斯随机定时器");
        gaussianTimer.addAttribute("enabled", "true");
        gaussianTimer.addElement("stringProp").addAttribute("name", "ConstantTimer.delay").setText("10");
        gaussianTimer.addElement("stringProp").addAttribute("name", "RandomTimer.range").setText("0.0");
        
        threadGroupHashTree.addElement("hashTree");
        
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
     * 检查并添加缺失的固定用户参数表
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
        
        // 检查是否存在"固定用户-dev"参数表
        boolean hasDevParams = false;
        // 检查是否存在"固定用户 【tiger-api】"参数表
        boolean hasProdParams = false;
        
        List<Element> children = testPlanHashTree.elements();
        for (Element child : children) {
            if (child.getName().equals("Arguments")) {
                String testname = child.attributeValue("testname");
                if ("固定用户-dev".equals(testname)) {
                    hasDevParams = true;
                } else if ("固定用户 【tiger-api】".equals(testname)) {
                    hasProdParams = true;
                }
            }
        }
        
        // 如果不存在"固定用户-dev"参数表，添加一个
        if (!hasDevParams) {
            org.dom4j.Element argumentsDev = testPlanHashTree.addElement("Arguments");
            argumentsDev.addAttribute("guiclass", "ArgumentsPanel");
            argumentsDev.addAttribute("testclass", "Arguments");
            argumentsDev.addAttribute("testname", "固定用户-dev");
            argumentsDev.addAttribute("enabled", "false");
            org.dom4j.Element argsCollectionDev = argumentsDev.addElement("collectionProp").addAttribute("name", "Arguments.arguments");
            
            // 添加tcp_sleep_time参数
            org.dom4j.Element tcpSleepArgDev = argsCollectionDev.addElement("elementProp").addAttribute("name", "tcp_sleep_time").addAttribute("elementType", "Argument");
            tcpSleepArgDev.addElement("stringProp").addAttribute("name", "Argument.name").setText("tcp_sleep_time");
            tcpSleepArgDev.addElement("stringProp").addAttribute("name", "Argument.value").setText("1000");
            tcpSleepArgDev.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
            tcpSleepArgDev.addElement("stringProp").addAttribute("name", "Argument.desc").setText("socket发送请求间隔毫秒数");
            
            // 添加------A用户--------
            org.dom4j.Element aUserArgDev = argsCollectionDev.addElement("elementProp").addAttribute("name", "------A用户--------").addAttribute("elementType", "Argument");
            aUserArgDev.addElement("stringProp").addAttribute("name", "Argument.name").setText("------A用户--------");
            aUserArgDev.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
            
            // 添加code参数
            org.dom4j.Element codeArgDev = argsCollectionDev.addElement("elementProp").addAttribute("name", "code").addAttribute("elementType", "Argument");
            codeArgDev.addElement("stringProp").addAttribute("name", "Argument.name").setText("code");
            codeArgDev.addElement("stringProp").addAttribute("name", "Argument.value").setText("041Yo5nl2EKlZg4of5ml2KGUXR1Yo5nV");
            codeArgDev.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
            
            // 添加------B用户--------
            org.dom4j.Element bUserArgDev = argsCollectionDev.addElement("elementProp").addAttribute("name", "------B用户--------").addAttribute("elementType", "Argument");
            bUserArgDev.addElement("stringProp").addAttribute("name", "Argument.name").setText("------B用户--------");
            bUserArgDev.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
            
            // 添加b_code参数
            org.dom4j.Element bCodeArgDev = argsCollectionDev.addElement("elementProp").addAttribute("name", "b_code").addAttribute("elementType", "Argument");
            bCodeArgDev.addElement("stringProp").addAttribute("name", "Argument.name").setText("b_code");
            bCodeArgDev.addElement("stringProp").addAttribute("name", "Argument.value").setText("071pT7000XqaIV1kLd1003hu8O0pT709");
            bCodeArgDev.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
            
            // 添加serverIP参数
            org.dom4j.Element serverIpArgDev = argsCollectionDev.addElement("elementProp").addAttribute("name", "serverIP").addAttribute("elementType", "Argument");
            serverIpArgDev.addElement("stringProp").addAttribute("name", "Argument.name").setText("serverIP");
            serverIpArgDev.addElement("stringProp").addAttribute("name", "Argument.value").setText("localhost");
            serverIpArgDev.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
            
            // 添加serverPort参数
            org.dom4j.Element serverPortArgDev = argsCollectionDev.addElement("elementProp").addAttribute("name", "serverPort").addAttribute("elementType", "Argument");
            serverPortArgDev.addElement("stringProp").addAttribute("name", "Argument.name").setText("serverPort");
            serverPortArgDev.addElement("stringProp").addAttribute("name", "Argument.value").setText("8080");
            serverPortArgDev.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
            
            // 添加protocol参数
            org.dom4j.Element protocolArgDev = argsCollectionDev.addElement("elementProp").addAttribute("name", "protocol").addAttribute("elementType", "Argument");
            protocolArgDev.addElement("stringProp").addAttribute("name", "Argument.name").setText("protocol");
            protocolArgDev.addElement("stringProp").addAttribute("name", "Argument.value").setText("http");
            protocolArgDev.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
            
            // 添加token参数
            org.dom4j.Element tokenArgDev = argsCollectionDev.addElement("elementProp").addAttribute("name", "token").addAttribute("elementType", "Argument");
            tokenArgDev.addElement("stringProp").addAttribute("name", "Argument.name").setText("token");
            tokenArgDev.addElement("stringProp").addAttribute("name", "Argument.value").setText("");
            tokenArgDev.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
            
            testPlanHashTree.addElement("hashTree");
        }
        
        // 如果存在"固定用户 【tiger-api】"参数表，先删除它
        List<Element> elementsToRemove = new ArrayList<>();
        List<Element> hashTreesToRemove = new ArrayList<>();
        
        children = testPlanHashTree.elements();
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            if (child.getName().equals("Arguments") && "固定用户 【tiger-api】".equals(child.attributeValue("testname"))) {
                elementsToRemove.add(child);
                // 检查下一个元素是否是hashTree，如果是，也删除
                if (i + 1 < children.size() && children.get(i + 1).getName().equals("hashTree")) {
                    hashTreesToRemove.add(children.get(i + 1));
                }
            }
        }
        
        // 删除找到的元素
        for (Element element : elementsToRemove) {
            testPlanHashTree.remove(element);
        }
        for (Element element : hashTreesToRemove) {
            testPlanHashTree.remove(element);
        }
        
        // 重新获取子元素列表
        children = testPlanHashTree.elements();
        
        // 找到"固定用户-dev"的位置
        int devParamsIndex = -1;
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            if (child.getName().equals("Arguments") && "固定用户-dev".equals(child.attributeValue("testname"))) {
                devParamsIndex = i;
                break;
            }
        }
        
        // 重新排序元素，确保TestPlan元素在最前面，然后是"HTTP信息头-固定"，然后是固定用户参数表，"察看结果树"在下面
        children = testPlanHashTree.elements();
        List<Element> testPlanElements = new ArrayList<>();
        List<Element> headerElements = new ArrayList<>();
        List<Element> fixedUserElements = new ArrayList<>();
        List<Element> viewResultsTreeElements = new ArrayList<>();
        List<Element> otherElements = new ArrayList<>();
        
        // 收集元素
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            if (child.getName().equals("TestPlan")) {
                testPlanElements.add(child);
                // 检查下一个元素是否是hashTree
                if (i + 1 < children.size() && children.get(i + 1).getName().equals("hashTree")) {
                    testPlanElements.add(children.get(i + 1));
                    i++;
                }
            } else if (child.getName().equals("HeaderManager") && "HTTP信息头-固定".equals(child.attributeValue("testname"))) {
                headerElements.add(child);
                // 检查下一个元素是否是hashTree
                if (i + 1 < children.size() && children.get(i + 1).getName().equals("hashTree")) {
                    headerElements.add(children.get(i + 1));
                    i++;
                }
            } else if (child.getName().equals("Arguments") && ("固定用户-dev".equals(child.attributeValue("testname")) || "固定用户 【tiger-api】".equals(child.attributeValue("testname")))) {
                fixedUserElements.add(child);
                // 检查下一个元素是否是hashTree
                if (i + 1 < children.size() && children.get(i + 1).getName().equals("hashTree")) {
                    fixedUserElements.add(children.get(i + 1));
                    i++;
                }
            } else if (child.getName().equals("ResultCollector") && "察看结果树".equals(child.attributeValue("testname"))) {
                viewResultsTreeElements.add(child);
                // 检查下一个元素是否是hashTree
                if (i + 1 < children.size() && children.get(i + 1).getName().equals("hashTree")) {
                    viewResultsTreeElements.add(children.get(i + 1));
                    i++;
                }
            } else {
                otherElements.add(child);
                // 检查下一个元素是否是hashTree
                if (i + 1 < children.size() && children.get(i + 1).getName().equals("hashTree")) {
                    otherElements.add(children.get(i + 1));
                    i++;
                }
            }
        }
        
        // 清空原有元素
        testPlanHashTree.clearContent();
        
        // 先添加TestPlan元素
        for (Element element : testPlanElements) {
            element.detach(); // 先从原有父元素中分离
            testPlanHashTree.add(element);
        }
        
        // 然后添加HTTP信息头-固定元素
        for (Element element : headerElements) {
            element.detach(); // 先从原有父元素中分离
            testPlanHashTree.add(element);
        }
        
        // 然后添加固定用户元素
        for (Element element : fixedUserElements) {
            element.detach(); // 先从原有父元素中分离
            testPlanHashTree.add(element);
        }
        
        // 然后添加其他元素
        for (Element element : otherElements) {
            element.detach(); // 先从原有父元素中分离
            testPlanHashTree.add(element);
        }
        
        // 最后添加察看结果树元素
        for (Element element : viewResultsTreeElements) {
            element.detach(); // 先从原有父元素中分离
            testPlanHashTree.add(element);
        }
        
        // 在"固定用户-dev"后面添加"固定用户 【tiger-api】"参数表
        if (devParamsIndex != -1) {
            // 创建"固定用户 【tiger-api】"参数表
            org.dom4j.Element argumentsProd = testPlanHashTree.addElement("Arguments");
            argumentsProd.addAttribute("guiclass", "ArgumentsPanel");
            argumentsProd.addAttribute("testclass", "Arguments");
            argumentsProd.addAttribute("testname", "固定用户 【tiger-api】");
            argumentsProd.addAttribute("enabled", "true");
            org.dom4j.Element argsCollectionProd = argumentsProd.addElement("collectionProp").addAttribute("name", "Arguments.arguments");
            
            // 添加tcp_sleep_time参数
            org.dom4j.Element tcpSleepArgProd = argsCollectionProd.addElement("elementProp").addAttribute("name", "tcp_sleep_time").addAttribute("elementType", "Argument");
            tcpSleepArgProd.addElement("stringProp").addAttribute("name", "Argument.name").setText("tcp_sleep_time");
            tcpSleepArgProd.addElement("stringProp").addAttribute("name", "Argument.value").setText("1000");
            tcpSleepArgProd.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
            tcpSleepArgProd.addElement("stringProp").addAttribute("name", "Argument.desc").setText("socket发送请求间隔毫秒数");
            
            // 添加------A用户--------
            org.dom4j.Element aUserArgProd = argsCollectionProd.addElement("elementProp").addAttribute("name", "------A用户--------").addAttribute("elementType", "Argument");
            aUserArgProd.addElement("stringProp").addAttribute("name", "Argument.name").setText("------A用户--------");
            aUserArgProd.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
            
            // 添加code参数
            org.dom4j.Element codeArgProd = argsCollectionProd.addElement("elementProp").addAttribute("name", "code").addAttribute("elementType", "Argument");
            codeArgProd.addElement("stringProp").addAttribute("name", "Argument.name").setText("code");
            codeArgProd.addElement("stringProp").addAttribute("name", "Argument.value").setText("041Yo5nl2EKlZg4of5ml2KGUXR1Yo5nV");
            codeArgProd.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
            
            // 添加------B用户--------
            org.dom4j.Element bUserArgProd = argsCollectionProd.addElement("elementProp").addAttribute("name", "------B用户--------").addAttribute("elementType", "Argument");
            bUserArgProd.addElement("stringProp").addAttribute("name", "Argument.name").setText("------B用户--------");
            bUserArgProd.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
            
            // 添加b_code参数
            org.dom4j.Element bCodeArgProd = argsCollectionProd.addElement("elementProp").addAttribute("name", "b_code").addAttribute("elementType", "Argument");
            bCodeArgProd.addElement("stringProp").addAttribute("name", "Argument.name").setText("b_code");
            bCodeArgProd.addElement("stringProp").addAttribute("name", "Argument.value").setText("071pT7000XqaIV1kLd1003hu8O0pT709");
            bCodeArgProd.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
            
            // 添加serverIP参数
            org.dom4j.Element serverIpArgProd = argsCollectionProd.addElement("elementProp").addAttribute("name", "serverIP").addAttribute("elementType", "Argument");
            serverIpArgProd.addElement("stringProp").addAttribute("name", "Argument.name").setText("serverIP");
            serverIpArgProd.addElement("stringProp").addAttribute("name", "Argument.value").setText(serverIP);
            serverIpArgProd.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
            
            // 添加serverPort参数
            org.dom4j.Element serverPortArgProd = argsCollectionProd.addElement("elementProp").addAttribute("name", "serverPort").addAttribute("elementType", "Argument");
            serverPortArgProd.addElement("stringProp").addAttribute("name", "Argument.name").setText("serverPort");
            serverPortArgProd.addElement("stringProp").addAttribute("name", "Argument.value").setText(serverPort);
            serverPortArgProd.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
            
            // 添加protocol参数
            org.dom4j.Element protocolArgProd = argsCollectionProd.addElement("elementProp").addAttribute("name", "protocol").addAttribute("elementType", "Argument");
            protocolArgProd.addElement("stringProp").addAttribute("name", "Argument.name").setText("protocol");
            protocolArgProd.addElement("stringProp").addAttribute("name", "Argument.value").setText(protocol);
            protocolArgProd.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
            
            // 添加token参数
            org.dom4j.Element tokenArgProd = argsCollectionProd.addElement("elementProp").addAttribute("name", "token").addAttribute("elementType", "Argument");
            tokenArgProd.addElement("stringProp").addAttribute("name", "Argument.name").setText("token");
            tokenArgProd.addElement("stringProp").addAttribute("name", "Argument.value").setText("");
            tokenArgProd.addElement("stringProp").addAttribute("name", "Argument.metadata").setText("=");
            
            testPlanHashTree.addElement("hashTree");
            
            // 再次重新排序元素，确保"固定用户 【tiger-api】"在"固定用户-dev"后面，"察看结果树"前面
            children = testPlanHashTree.elements();
            List<Element> testPlanElementsProd = new ArrayList<>();
            List<Element> headerElementsProd = new ArrayList<>();
            fixedUserElements = new ArrayList<>();
            viewResultsTreeElements = new ArrayList<>();
            otherElements = new ArrayList<>();
            
            // 收集元素
            for (int i = 0; i < children.size(); i++) {
                Element child = children.get(i);
                if (child.getName().equals("TestPlan")) {
                    testPlanElementsProd.add(child);
                    // 检查下一个元素是否是hashTree
                    if (i + 1 < children.size() && children.get(i + 1).getName().equals("hashTree")) {
                        testPlanElementsProd.add(children.get(i + 1));
                        i++;
                    }
                } else if (child.getName().equals("HeaderManager") && "HTTP信息头-固定".equals(child.attributeValue("testname"))) {
                    headerElementsProd.add(child);
                    // 检查下一个元素是否是hashTree
                    if (i + 1 < children.size() && children.get(i + 1).getName().equals("hashTree")) {
                        headerElementsProd.add(children.get(i + 1));
                        i++;
                    }
                } else if (child.getName().equals("Arguments") && "固定用户-dev".equals(child.attributeValue("testname"))) {
                    fixedUserElements.add(child);
                    // 检查下一个元素是否是hashTree
                    if (i + 1 < children.size() && children.get(i + 1).getName().equals("hashTree")) {
                        fixedUserElements.add(children.get(i + 1));
                        i++;
                    }
                } else if (child.getName().equals("Arguments") && "固定用户 【tiger-api】".equals(child.attributeValue("testname"))) {
                    // 跳过，稍后添加
                } else if (child.getName().equals("ResultCollector") && "察看结果树".equals(child.attributeValue("testname"))) {
                    viewResultsTreeElements.add(child);
                    // 检查下一个元素是否是hashTree
                    if (i + 1 < children.size() && children.get(i + 1).getName().equals("hashTree")) {
                        viewResultsTreeElements.add(children.get(i + 1));
                        i++;
                    }
                } else {
                    otherElements.add(child);
                    // 检查下一个元素是否是hashTree
                    if (i + 1 < children.size() && children.get(i + 1).getName().equals("hashTree")) {
                        otherElements.add(children.get(i + 1));
                        i++;
                    }
                }
            }
            
            // 找到新添加的"固定用户 【tiger-api】"元素
            Element prodArguments = null;
            Element prodHashTree = null;
            for (int i = 0; i < children.size(); i++) {
                Element child = children.get(i);
                if (child.getName().equals("Arguments") && "固定用户 【tiger-api】".equals(child.attributeValue("testname"))) {
                    prodArguments = child;
                    // 检查下一个元素是否是hashTree
                    if (i + 1 < children.size() && children.get(i + 1).getName().equals("hashTree")) {
                        prodHashTree = children.get(i + 1);
                        break;
                    }
                }
            }
            
            // 清空原有元素
            testPlanHashTree.clearContent();
            
            // 先添加TestPlan元素
            for (Element element : testPlanElementsProd) {
                element.detach(); // 先从原有父元素中分离
                testPlanHashTree.add(element);
            }
            
            // 然后添加HTTP信息头-固定元素
            for (Element element : headerElementsProd) {
                element.detach(); // 先从原有父元素中分离
                testPlanHashTree.add(element);
            }
            
            // 然后添加"固定用户-dev"元素
            for (Element element : fixedUserElements) {
                element.detach(); // 先从原有父元素中分离
                testPlanHashTree.add(element);
            }
            
            // 然后添加"固定用户 【tiger-api】"元素
            if (prodArguments != null) {
                prodArguments.detach(); // 先从原有父元素中分离
                testPlanHashTree.add(prodArguments);
                if (prodHashTree != null) {
                    prodHashTree.detach(); // 先从原有父元素中分离
                    testPlanHashTree.add(prodHashTree);
                }
            }
            
            // 然后添加其他元素
            for (Element element : otherElements) {
                element.detach(); // 先从原有父元素中分离
                testPlanHashTree.add(element);
            }
            
            // 最后添加察看结果树元素
            for (Element element : viewResultsTreeElements) {
                element.detach(); // 先从原有父元素中分离
                testPlanHashTree.add(element);
            }
        }
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
