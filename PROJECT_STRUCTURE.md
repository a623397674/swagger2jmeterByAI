# Swagger2JMeter - 项目结构说明

## 标准Maven项目结构

```
swagger2jmeter/
├── src/                          # 源代码目录
│   ├── main/                       # 主代码目录
│   │   ├── java/                   # Java源代码
│   │   │   └── io/swagger2jmeter/ # 主包名（符合反向域名规范）
│   │   │       ├── Swagger2JMeter.java          # 主入口类
│   │   │       ├── model/                    # 数据模型包
│   │   │       │   ├── ApiParameter.java       # API参数模型
│   │   │       │   ├── ApiPath.java          # API路径模型
│   │   │       │   ├── SwaggerModel.java      # Swagger文档模型
│   │   │       │   └── Tag.java             # 标签模型
│   │   │       ├── parser/                   # 解析器包
│   │   │       │   └── SwaggerParser.java     # Swagger文档解析器
│   │   │       └── writer/                   # 写入器包
│   │   │           ├── JMeterWriter.java       # JMeter文件写入器
│   │   │           └── JMeterElementBean.java # JMeter元素管理Bean
│   │   └── resources/                # 资源文件目录
│   └── test/                        # 测试代码目录
│       ├── java/                       # 测试Java代码
│       └── resources/                  # 测试资源文件
├── target/                        # Maven编译输出目录（自动生成，已忽略）
├── pom.xml                        # Maven项目配置文件
├── README.md                      # 项目说明文档
├── LICENSE                        # Apache 2.0许可证
└── .gitignore                     # Git忽略文件配置
```

## 包命名规范

### 主包名：io.swagger2jmeter

遵循Java包命名最佳实践：
- **反向域名**：使用反向域名格式，确保全局唯一性
- **小写字母**：包名全部使用小写字母
- **层次清晰**：按功能模块划分包结构

### 子包说明

#### 1. model包
- **职责**：定义数据模型和实体类
- **包含类**：
  - `SwaggerModel`：Swagger文档整体模型
  - `ApiPath`：API路径信息模型
  - `ApiParameter`：API参数模型
  - `Tag`：标签模型

#### 2. parser包
- **职责**：负责解析Swagger/OpenAPI文档
- **包含类**：
  - `SwaggerParser`：Swagger文档解析器
  - 支持从URL、文件、JSON字符串解析

#### 3. writer包
- **职责**：负责生成JMeter测试计划
- **包含类**：
  - `JMeterWriter`：JMeter文件写入器
  - `JMeterElementBean`：JMeter元素管理Bean

## Maven配置说明

### 项目坐标
```xml
<groupId>io.swagger2jmeter</groupId>
<artifactId>swagger2jmeter</artifactId>
<version>1.0.0</version>
<packaging>jar</packaging>
```

### 打包配置
项目配置了多个Maven插件：
1. **maven-compiler-plugin**：编译Java源代码
2. **maven-jar-plugin**：生成标准JAR包
3. **maven-assembly-plugin**：生成包含所有依赖的Fat JAR
4. **maven-source-plugin**：生成源代码JAR
5. **maven-javadoc-plugin**：生成API文档JAR

### 依赖管理
所有依赖版本使用属性统一管理：
```xml
<properties>
    <jackson.version>2.15.2</jackson.version>
    <dom4j.version>2.1.4</dom4j.version>
    <okhttp.version>4.11.0</okhttp.version>
    <slf4j.version>2.0.9</slf4j.version>
</properties>
```

## 打包产物

执行`mvn clean package`后，会在`target`目录生成以下文件：

1. **swagger2jmeter-1.0.0.jar** (27KB)
   - 标准JAR包，包含编译后的class文件
   - 不包含依赖库
   - 适合作为Maven依赖使用

2. **swagger2jmeter-1.0.0-jar-with-dependencies.jar** (5.3MB)
   - Fat JAR，包含所有依赖
   - 可直接使用`java -jar`命令运行
   - 适合独立部署

3. **swagger2jmeter-1.0.0-sources.jar** (16KB)
   - 源代码JAR包
   - 便于IDE查看源码

4. **swagger2jmeter-1.0.0-javadoc.jar** (83KB)
   - API文档JAR包
   - 便于IDE查看API文档

## 使用方式

### 方式1：作为Maven依赖
```xml
<dependency>
    <groupId>io.swagger2jmeter</groupId>
    <artifactId>swagger2jmeter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 方式2：直接运行Fat JAR
```bash
java -jar swagger2jmeter-1.0.0-jar-with-dependencies.jar \
  https://api.example.com/v3/api-docs \
  output.jmx
```

### 方式3：程序化调用
```java
import io.swagger2jmeter.Swagger2JMeter;
import io.swagger2jmeter.parser.SwaggerParser;
import io.swagger2jmeter.writer.JMeterWriter;
import io.swagger2jmeter.model.SwaggerModel;

// 解析Swagger文档
SwaggerParser parser = new SwaggerParser();
SwaggerModel model = parser.parseFromUrl("https://api.example.com/v3/api-docs");

// 生成JMeter测试计划
JMeterWriter writer = new JMeterWriter();
Document document = writer.readJMeterFile("output.jmx");
writer.syncAPIsToJMeter(document, model);
writer.saveJMeterFile(document, "output.jmx");
```

## Git忽略配置

`.gitignore`文件已配置忽略以下内容：
- Maven编译输出（target/）
- IDE配置文件（.idea/, *.iml等）
- JMeter生成的文件（*.jmx）
- 操作系统文件（.DS_Store, Thumbs.db等）
- 日志和临时文件

## 符合大厂标准

本项目遵循以下大厂和开源社区的最佳实践：

1. **包命名规范**：使用反向域名格式（io.swagger2jmeter）
2. **目录结构**：标准Maven多模块项目结构
3. **代码组织**：按功能模块划分包（model、parser、writer）
4. **依赖管理**：统一版本管理，避免版本冲突
5. **构建配置**：完善的Maven插件配置
6. **文档完善**：README.md、LICENSE、Javadoc齐全
7. **版本控制**：合理的.gitignore配置
8. **开源协议**：使用Apache 2.0开源协议

## 后续扩展建议

1. **添加单元测试**：在src/test/java目录下编写测试用例
2. **集成测试**：添加端到端测试
3. **CI/CD配置**：添加GitHub Actions或Jenkins配置
4. **发布到Maven中央仓库**：配置ossrh发布流程
5. **添加示例代码**：在src/main/resources下添加示例文件
