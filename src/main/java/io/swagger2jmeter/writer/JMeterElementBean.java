package io.swagger2jmeter.writer;

import org.dom4j.Element;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JMeterElementBean {
    public static final String TYPE_TEST_PLAN = "TestPlan";
    public static final String TYPE_HEADER_MANAGER = "HeaderManager";
    public static final String TYPE_ARGUMENTS = "Arguments";
    public static final String TYPE_RESULT_COLLECTOR = "ResultCollector";
    public static final String TYPE_THREAD_GROUP = "ThreadGroup";
    public static final String TYPE_HASH_TREE = "hashTree";
    
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
    
    private Map<String, Element> elementMap;
    private List<String> elementOrder;
    
    public JMeterElementBean() {
        this.elementMap = new HashMap<>();
        this.elementOrder = new ArrayList<>();
    }
    
    private void addElement(String type, String name, Element element) {
        String key = type + ":" + name;
        elementMap.put(key, element);
        elementOrder.add(key);
    }
    
    public Element findElement(String type, String name) {
        String key = type + ":" + name;
        return elementMap.get(key);
    }
    
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
    
    public void clear() {
        elementMap.clear();
        elementOrder.clear();
    }
    
    public int size() {
        return elementMap.size();
    }
    
    public void initPlaceholderElements() {
        clear();
        
        Element testPlan = createTestPlan();
        addElement(TYPE_TEST_PLAN, "测试计划", testPlan);
        
        Element testPlanHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, "TestPlan-hashTree", testPlanHashTree);
        
        Element headerManager1 = createHeaderManager(NAME_HTTP_HEADER, false);
        addElement(TYPE_HEADER_MANAGER, NAME_HTTP_HEADER, headerManager1);
        
        Element headerHashTree1 = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_HTTP_HEADER + "-hashTree", headerHashTree1);
        
        Element headerManager2 = createHeaderManager(NAME_HTTP_HEADER_FIXED, true);
        addElement(TYPE_HEADER_MANAGER, NAME_HTTP_HEADER_FIXED, headerManager2);
        
        Element headerHashTree2 = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_HTTP_HEADER_FIXED + "-hashTree", headerHashTree2);
        
        Element devParams = createArguments(NAME_FIXED_USER_DEV, true);
        addElement(TYPE_ARGUMENTS, NAME_FIXED_USER_DEV, devParams);
        
        Element devParamsHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_FIXED_USER_DEV + "-hashTree", devParamsHashTree);
        
        Element tigerParams = createArguments(NAME_FIXED_USER_TIGER, false);
        addElement(TYPE_ARGUMENTS, NAME_FIXED_USER_TIGER, tigerParams);
        
        Element tigerParamsHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_FIXED_USER_TIGER + "-hashTree", tigerParamsHashTree);
        
        Element productParams = createArguments(NAME_FIXED_USER_PRODUCT, false);
        addElement(TYPE_ARGUMENTS, NAME_FIXED_USER_PRODUCT, productParams);
        
        Element productParamsHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_FIXED_USER_PRODUCT + "-hashTree", productParamsHashTree);
        
        Element viewResultsTree = createResultCollector(NAME_VIEW_RESULTS_TREE, "ViewResultsFullVisualizer", false);
        addElement(TYPE_RESULT_COLLECTOR, NAME_VIEW_RESULTS_TREE, viewResultsTree);
        
        Element viewResultsTreeHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_VIEW_RESULTS_TREE + "-hashTree", viewResultsTreeHashTree);
        
        Element viewResultsTreeError = createResultCollector(NAME_VIEW_RESULTS_TREE_ERROR, "ViewResultsFullVisualizer", true);
        addElement(TYPE_RESULT_COLLECTOR, NAME_VIEW_RESULTS_TREE_ERROR, viewResultsTreeError);
        
        Element viewResultsTreeErrorHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_VIEW_RESULTS_TREE_ERROR + "-hashTree", viewResultsTreeErrorHashTree);
        
        Element tableResults = createResultCollector(NAME_TABLE_RESULTS, "TableVisualizer", false);
        addElement(TYPE_RESULT_COLLECTOR, NAME_TABLE_RESULTS, tableResults);
        
        Element tableResultsHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_TABLE_RESULTS + "-hashTree", tableResultsHashTree);
        
        Element aggregateReport = createResultCollector(NAME_AGGREGATE_REPORT, "StatVisualizer", false);
        addElement(TYPE_RESULT_COLLECTOR, NAME_AGGREGATE_REPORT, aggregateReport);
        
        Element aggregateReportHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_AGGREGATE_REPORT + "-hashTree", aggregateReportHashTree);
        
        Element summaryReport = createResultCollector(NAME_SUMMARY_REPORT, "SummaryReport", false);
        addElement(TYPE_RESULT_COLLECTOR, NAME_SUMMARY_REPORT, summaryReport);
        
        Element summaryReportHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_SUMMARY_REPORT + "-hashTree", summaryReportHashTree);
        
        Element threadGroup = createThreadGroup();
        addElement(TYPE_THREAD_GROUP, NAME_THREAD_GROUP, threadGroup);
        
        Element threadGroupHashTree = createHashTree();
        addElement(TYPE_HASH_TREE, NAME_THREAD_GROUP + "-hashTree", threadGroupHashTree);
    }
    
    private Element createTestPlan() {
        Element testPlan = org.dom4j.DocumentHelper.createElement(TYPE_TEST_PLAN);
        testPlan.addAttribute("guiclass", "TestPlanGui");
        testPlan.addAttribute("testclass", "TestPlan");
        testPlan.addAttribute("testname", "测试计划");
        testPlan.addAttribute("enabled", "true");
        return testPlan;
    }
    
    private Element createHeaderManager(String testname, boolean enabled) {
        Element headerManager = org.dom4j.DocumentHelper.createElement(TYPE_HEADER_MANAGER);
        headerManager.addAttribute("guiclass", "HeaderPanel");
        headerManager.addAttribute("testclass", "HeaderManager");
        headerManager.addAttribute("testname", testname);
        headerManager.addAttribute("enabled", String.valueOf(enabled));
        return headerManager;
    }
    
    private Element createArguments(String testname, boolean enabled) {
        Element arguments = org.dom4j.DocumentHelper.createElement(TYPE_ARGUMENTS);
        arguments.addAttribute("guiclass", "ArgumentsPanel");
        arguments.addAttribute("testclass", "Arguments");
        arguments.addAttribute("testname", testname);
        arguments.addAttribute("enabled", String.valueOf(enabled));
        return arguments;
    }
    
    private Element createResultCollector(String testname, String guiclass, boolean errorLogging) {
        Element resultCollector = org.dom4j.DocumentHelper.createElement(TYPE_RESULT_COLLECTOR);
        resultCollector.addAttribute("guiclass", guiclass);
        resultCollector.addAttribute("testclass", "ResultCollector");
        resultCollector.addAttribute("testname", testname);
        resultCollector.addAttribute("enabled", "true");
        resultCollector.addElement("boolProp").addAttribute("name", "ResultCollector.error_logging").setText(String.valueOf(errorLogging));
        return resultCollector;
    }
    
    private Element createThreadGroup() {
        Element threadGroup = org.dom4j.DocumentHelper.createElement(TYPE_THREAD_GROUP);
        threadGroup.addAttribute("guiclass", "ThreadGroupGui");
        threadGroup.addAttribute("testclass", "ThreadGroup");
        threadGroup.addAttribute("testname", NAME_THREAD_GROUP);
        threadGroup.addAttribute("enabled", "true");
        return threadGroup;
    }
    
    private Element createHashTree() {
        return org.dom4j.DocumentHelper.createElement(TYPE_HASH_TREE);
    }
}
