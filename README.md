# Swagger2JMeter

[![Maven Central](https://img.shields.io/maven-central/v/io.swagger2jmeter/swagger2jmeter.svg)](https://central.sonatype.com/artifact/io.swagger2jmeter/swagger2jmeter)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Java](https://img.shields.io/badge/Java-1.8+-green.svg)](https://www.oracle.com/java/)

A powerful tool that automatically converts Swagger/OpenAPI documentation to JMeter test plans, enabling rapid API testing and performance validation.

## Features

- **Automatic Conversion**: Convert Swagger/OpenAPI 3.0 documentation to JMeter test plans with a single command
- **Smart Parameter Extraction**: Automatically extract API parameters, request bodies, and response schemas
- **Dynamic Test Plan Generation**: Generate JMeter test plans with proper thread groups, samplers, and assertions
- **Support for Multiple Data Sources**: Parse Swagger from URLs or local files
- **Flexible Configuration**: Customize thread groups, timers, and assertions
- **JMeter Integration**: Seamlessly integrate with existing JMeter workflows

## Quick Start

### Prerequisites

- Java 8 or higher
- Maven 3.6 or higher
- JMeter 5.0 or higher (optional, for running tests)

### Installation

#### From Maven Central

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.swagger2jmeter</groupId>
    <artifactId>swagger2jmeter</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### Build from Source

```bash
git clone https://github.com/swagger2jmeter/swagger2jmeter.git
cd swagger2jmeter
mvn clean package
```

This will generate two JAR files in the `target` directory:
- `swagger2jmeter-1.0.0.jar` - Main JAR
- `swagger2jmeter-1.0.0-jar-with-dependencies.jar` - Fat JAR with all dependencies

### Usage

#### Command Line

```bash
# Convert from URL
java -jar swagger2jmeter-1.0.0-jar-with-dependencies.jar \
  https://api.example.com/v3/api-docs \
  output.jmx

# Convert from local file
java -jar swagger2jmeter-1.0.0-jar-with-dependencies.jar \
  swagger.json \
  output.jmx
```

#### Programmatic Usage

```java
import io.swagger2jmeter.Swagger2JMeter;
import io.swagger2jmeter.parser.SwaggerParser;
import io.swagger2jmeter.writer.JMeterWriter;
import io.swagger2jmeter.model.SwaggerModel;
import org.dom4j.Document;

// Parse Swagger documentation
SwaggerParser parser = new SwaggerParser();
SwaggerModel model = parser.parseFromUrl("https://api.example.com/v3/api-docs");

// Generate JMeter test plan
JMeterWriter writer = new JMeterWriter();
Document document = writer.readJMeterFile("output.jmx");
writer.syncAPIsToJMeter(document, model);
writer.saveJMeterFile(document, "output.jmx");
```

## Project Structure

```
swagger2jmeter/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── io/swagger2jmeter/
│   │   │       ├── Swagger2JMeter.java          # Main entry point
│   │   │       ├── model/                       # Data models
│   │   │       │   ├── ApiParameter.java
│   │   │       │   ├── ApiPath.java
│   │   │       │   ├── SwaggerModel.java
│   │   │       │   └── Tag.java
│   │   │       ├── parser/                      # Swagger parser
│   │   │       │   └── SwaggerParser.java
│   │   │       └── writer/                      # JMeter writer
│   │   │           ├── JMeterWriter.java
│   │   │           └── JMeterElementBean.java
│   │   └── resources/
│   └── test/
│       ├── java/
│       └── resources/
├── pom.xml
├── README.md
└── LICENSE
```

## Configuration

### Thread Group Settings

You can customize thread group parameters by modifying the generated JMX file:

```xml
<stringProp name="ThreadGroup.num_threads">100</stringProp>
<stringProp name="ThreadGroup.ramp_time">10</stringProp>
<stringProp name="ThreadGroup.duration">600</stringProp>
```

### HTTP Headers

The tool automatically adds common headers:
- `Content-Type: application/json`
- `Authorization: ${token}`

You can add custom headers by modifying the `HTTP信息头` element in the generated JMX file.

## Advanced Usage

### Custom Element Templates

You can customize the JMeter element templates by modifying the `JMeterElementBean` class:

```java
public class CustomElementBean extends JMeterElementBean {
    @Override
    public void initPlaceholderElements() {
        super.initPlaceholderElements();
        // Add custom elements
    }
}
```

### Batch Processing

Process multiple Swagger files:

```bash
for file in swagger/*.json; do
  java -jar swagger2jmeter-1.0.0-jar-with-dependencies.jar \
    "$file" "output/$(basename $file .json).jmx"
done
```

## Running JMeter Tests

After generating the JMX file, you can run the tests using JMeter:

```bash
# Non-GUI mode
jmeter -n -t output.jmx -l results.jtl -e -o report

# GUI mode
jmeter -t output.jmx
```

## Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development

```bash
# Run tests
mvn test

# Build with all artifacts
mvn clean package

# Generate Javadoc
mvn javadoc:javadoc
```

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Support

- **Issues**: [GitHub Issues](https://github.com/swagger2jmeter/swagger2jmeter/issues)
- **Documentation**: [Wiki](https://github.com/swagger2jmeter/swagger2jmeter/wiki)
- **Email**: contact@swagger2jmeter.io

## Acknowledgments

- [Swagger/OpenAPI](https://swagger.io/) - API specification
- [Apache JMeter](https://jmeter.apache.org/) - Performance testing tool
- [Jackson](https://github.com/FasterXML/jackson) - JSON processing
- [DOM4J](https://dom4j.github.io/) - XML processing

## Roadmap

- [ ] Support for OpenAPI 3.1
- [ ] GraphQL support
- [ ] Web UI for configuration
- [ ] CI/CD integration plugins
- [ ] Performance report enhancements

## Changelog

### Version 1.0.0 (2026-02-27)
- Initial release
- Swagger/OpenAPI 3.0 support
- Automatic JMeter test plan generation
- URL and file-based input support
- Thread group and sampler configuration

---

Made with ❤️ by the Swagger2JMeter Team
