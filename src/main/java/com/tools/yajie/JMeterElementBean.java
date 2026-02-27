package com.tools.yajie;

import org.dom4j.Element;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JMeter元素管理Bean
 * 功能：
 * 1. 管理所有一级元素的占位和初始化
 * 2. 按照预定顺序维护元素
 * 3. 提供元素访问和修改方法
 * 4. 保持单一职责设计模式
 */
public class JMeterElementBean {
    // 元素类型常量
    public static final String TYPE_TEST_PLAN = "TestPlan";
    public static final String TYPE_HEADER_MANAGER = "HeaderManager";
    public static final String TYPE_ARGUMENTS = "Arguments";
    public static final String TYPE_RESULT_COLLECTOR = "ResultCollector";
    public static final String TYPE_THREAD_GROUP = "ThreadGroup";
    public static final String TYPE_HASH_TREE = "hashTree";
    
    // 元素名称常量
    public static final String NAME_HTTP_HEADER = "HTTP信息头";
    public static final String NAME_HTTP_HEADER_FIXED = "HTTP信息头-固定sn";
    public static final String NAME_FIXED_USER_DEV = "固定用户-dev";
    public static final String NAME_FIXED_USER_TIGER = "固定用户 【tiger-api】";
    public static final String NAME_FIXED_USER_PRODUCT = "固定用户 【product】";
    public static final String NAME_VIEW_RESULTS_TREE = "察看结果树";
    public static final String NAME_VIEW_RESULTS_TREE_ERROR = "察看结果树(异常)";
    public static final String NAME_TABLE_RESULTS = "用表格察看结果";
    public static final String NAME_AGGREGATE_REPORT = "聚合报告";
    public static final String NAME_SUMMARY_REPORT = "汇总报告";
    public static final String NAME_THREAD_GROUP = "回归测试";
    
    // 元素存储Map，使用类型和名称作为key
    private Map<String, Element> elementMap;
    
    // 元素顺序列表
    private List<String> elementOrder;
    
    // 构造函数
    public JMeterElementBean() {
        this.elementMap = new HashMap<>();
        this.elementOrder = new ArrayList<>();
    }
    
    /**
     * 添加元素到Map和顺序列表
     */
    private void addElement(String type, String name, Element element) {
        String key = type + ":" + name;
        elementMap.put(key, element);
        elementOrder.add(key);
    }
    
    /**
     * 根据类型和名称查找元素（精准查找，不循环）
     */
    public Element findElement(String type, String name) {
        String key = type + ":" + name;
        return elementMap.get(key);
    }
    
    /**
     * 获取所有元素（按照顺序）
     */
    public List<Element> getElements() {
        List<Element> result = new ArrayList<>();
        for (String key : elementOrder) {
            Element element = elementMap.get(key);
            if (element != null) {
                result.add(element);
            }
        }
        return result;
    }
    
    /**
     * 清空元素列表
     */
    public void clear() {
        elementMap.clear();
        elementOrder.clear();
    }
    
    /**
     * 获取元素数量
     */
    public int size() {
        return elementMap.size();
    }
    
    /**
     * 初始化占位元素（参考yunying-cms-api.jmx模板）
     */
    public void initPlaceholderElements() {
        clear();
        
        // 1. TestPlan占位
        Element testPlan = createTestPlan();
        addElement(TYPE_TEST_PLAN, "测试计划", testPlan);
        
        // TestPlan的hashTree
        Element testPlanHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, "TestPlan-hashTree", testPlanHashTree);
        
        // 2. HeaderManager占位 - HTTP信息头
        Element headerManager1 = createHeaderManager(NAME_HTTP_HEADER, false);
        addElement(TYPE_HEADER_MANAGER, NAME_HTTP_HEADER, headerManager1);
        
        // HTTP信息头的hashTree
        Element headerHashTree1 = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_HTTP_HEADER + "-hashTree", headerHashTree1);
        
        // 3. HeaderManager占位 - HTTP信息头-固定sn
        Element headerManager2 = createHeaderManager(NAME_HTTP_HEADER_FIXED, true);
        addElement(TYPE_HEADER_MANAGER, NAME_HTTP_HEADER_FIXED, headerManager2);
        
        // HTTP信息头-固定sn的hashTree
        Element headerHashTree2 = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_HTTP_HEADER_FIXED + "-hashTree", headerHashTree2);
        
        // 4. Arguments占位 - 固定用户-dev
        Element devParams = createArguments(NAME_FIXED_USER_DEV, true);
        addElement(TYPE_ARGUMENTS, NAME_FIXED_USER_DEV, devParams);
        
        // 固定用户-dev的hashTree
        Element devParamsHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_FIXED_USER_DEV + "-hashTree", devParamsHashTree);
        
        // 5. Arguments占位 - 固定用户 【tiger-api】
        Element tigerParams = createArguments(NAME_FIXED_USER_TIGER, false);
        addElement(TYPE_ARGUMENTS, NAME_FIXED_USER_TIGER, tigerParams);
        
        // 固定用户 【tiger-api】的hashTree
        Element tigerParamsHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_FIXED_USER_TIGER + "-hashTree", tigerParamsHashTree);
        
        // 6. Arguments占位 - 固定用户 【product】
        Element productParams = createArguments(NAME_FIXED_USER_PRODUCT, false);
        addElement(TYPE_ARGUMENTS, NAME_FIXED_USER_PRODUCT, productParams);
        
        // 固定用户 【product】的hashTree
        Element productParamsHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_FIXED_USER_PRODUCT + "-hashTree", productParamsHashTree);
        
        // 7. ResultCollector占位 - 察看结果树
        Element viewResultsTree = createResultCollector(NAME_VIEW_RESULTS_TREE, "ViewResultsFullVisualizer", false);
        addElement(TYPE_RESULT_COLLECTOR, NAME_VIEW_RESULTS_TREE, viewResultsTree);
        
        // 察看结果树的hashTree
        Element viewResultsTreeHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_VIEW_RESULTS_TREE + "-hashTree", viewResultsTreeHashTree);
        
        // 8. ResultCollector占位 - 察看结果树(异常)
        Element viewResultsTreeError = createResultCollector(NAME_VIEW_RESULTS_TREE_ERROR, "ViewResultsFullVisualizer", true);
        addElement(TYPE_RESULT_COLLECTOR, NAME_VIEW_RESULTS_TREE_ERROR, viewResultsTreeError);
        
        // 察看结果树(异常)的hashTree
        Element viewResultsTreeErrorHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_VIEW_RESULTS_TREE_ERROR + "-hashTree", viewResultsTreeErrorHashTree);
        
        // 9. ResultCollector占位 - 用表格察看结果
        Element tableResults = createResultCollector(NAME_TABLE_RESULTS, "TableVisualizer", false);
        addElement(TYPE_RESULT_COLLECTOR, NAME_TABLE_RESULTS, tableResults);
        
        // 用表格察看结果的hashTree
        Element tableResultsHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_TABLE_RESULTS + "-hashTree", tableResultsHashTree);
        
        // 10. ResultCollector占位 - 聚合报告
        Element aggregateReport = createResultCollector(NAME_AGGREGATE_REPORT, "StatVisualizer", false);
        addElement(TYPE_RESULT_COLLECTOR, NAME_AGGREGATE_REPORT, aggregateReport);
        
        // 聚合报告的hashTree
        Element aggregateReportHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_AGGREGATE_REPORT + "-hashTree", aggregateReportHashTree);
        
        // 11. ResultCollector占位 - 汇总报告
        Element summaryReport = createResultCollector(NAME_SUMMARY_REPORT, "SummaryReport", false);
        addElement(TYPE_RESULT_COLLECTOR, NAME_SUMMARY_REPORT, summaryReport);
        
        // 汇总报告的hashTree
        Element summaryReportHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_SUMMARY_REPORT + "-hashTree", summaryReportHashTree);
        
        // 12. ThreadGroup占位 - 回归测试
        Element threadGroup = createThreadGroup();
        addElement(TYPE_THREAD_GROUP, NAME_THREAD_GROUP, threadGroup);
        
        // ThreadGroup的hashTree
        Element threadGroupHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_THREAD_GROUP + "-hashTree", threadGroupHashTree);
    }
    
    /**
     * 创建TestPlan元素
     */
    private Element createTestPlan() {
        Element testPlan = org.dom4j.DocumentHelper.createElement(TYPE_TEST_PLAN);
        testPlan.addAttribute("guiclass", "TestPlanGui");
        testPlan.addAttribute("testclass", "TestPlan");
        testPlan.addAttribute("testname", "测试计划");
        testPlan.addAttribute("enabled", "true");
        return testPlan;
    }
    
    /**
     * 创建HeaderManager元素
     */
    private Element createHeaderManager(String testname, boolean enabled) {
        Element headerManager = org.dom4j.DocumentHelper.createElement(TYPE_HEADER_MANAGER);
        headerManager.addAttribute("guiclass", "HeaderPanel");
        headerManager.addAttribute("testclass", "HeaderManager");
        headerManager.addAttribute("testname", testname);
        headerManager.addAttribute("enabled", String.valueOf(enabled));
        return headerManager;
    }
    
    /**
     * 创建Arguments元素
     */
    private Element createArguments(String testname, boolean enabled) {
        Element arguments = org.dom4j.DocumentHelper.createElement(TYPE_ARGUMENTS);
        arguments.addAttribute("guiclass", "ArgumentsPanel");
        arguments.addAttribute("testclass", "Arguments");
        arguments.addAttribute("testname", testname);
        arguments.addAttribute("enabled", String.valueOf(enabled));
        return arguments;
    }
    
    /**
     * 创建ResultCollector元素
     */
    private Element createResultCollector(String testname, String guiclass, boolean errorLogging) {
        Element resultCollector = org.dom4j.DocumentHelper.createElement(TYPE_RESULT_COLLECTOR);
        resultCollector.addAttribute("guiclass", guiclass);
        resultCollector.addAttribute("testclass", "ResultCollector");
        resultCollector.addAttribute("testname", testname);
        resultCollector.addAttribute("enabled", "true");
        resultCollector.addElement("boolProp").addAttribute("name", "ResultCollector.error_logging").setText(String.valueOf(errorLogging));
        return resultCollector;
    }
    
    /**
     * 创建ThreadGroup元素
     */
    private Element createThreadGroup() {
        Element threadGroup = org.dom4j.DocumentHelper.createElement(TYPE_THREAD_GROUP);
        threadGroup.addAttribute("guiclass", "ThreadGroupGui");
        threadGroup.addAttribute("testclass", "ThreadGroup");
        threadGroup.addAttribute("testname", NAME_THREAD_GROUP);
        threadGroup.addAttribute("enabled", "true");
        return threadGroup;
    }
    
    /**
     * 创建hashTree元素
     */
    private Element createHashTree() {
        return org.dom4j.DocumentHelper.createElement(TYPE_HASH_TREE);
    }
}
