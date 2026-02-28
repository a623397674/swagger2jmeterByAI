package io.swagger2jmeter.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger2jmeter.model.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SwaggerParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final OkHttpClient httpClient;

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.sslSocketFactory(getSSLSocketFactory(), getTrustManager());
        builder.hostnameVerifier((hostname, session) -> true);
        httpClient = builder.build();
    }

    private static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{getTrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static X509TrustManager getTrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {}

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {}

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    public SwaggerModel parseFromUrl(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to fetch Swagger docs: " + response.code());
            }

            String json = response.body().string();
            return parseFromJson(json);
        }
    }

    public SwaggerModel parseFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        JsonNode rootNode = objectMapper.readTree(file);
        return parseFromJsonNode(rootNode);
    }

    public SwaggerModel parseFromJson(String json) throws IOException {
        JsonNode rootNode = objectMapper.readTree(json);
        return parseFromJsonNode(rootNode);
    }

    private SwaggerModel parseFromJsonNode(JsonNode rootNode) {
        SwaggerModel model = new SwaggerModel();

        if (rootNode.has("servers")) {
            JsonNode serversNode = rootNode.get("servers");
            if (serversNode.isArray() && serversNode.size() > 0) {
                JsonNode firstServerNode = serversNode.get(0);
                if (firstServerNode.has("url")) {
                    String serverUrl = firstServerNode.get("url").asText();
                    model.setServerUrl(serverUrl);
                }
            }
        }

        if (rootNode.has("info")) {
            JsonNode infoNode = rootNode.get("info");
            if (infoNode.has("contact")) {
                JsonNode contactNode = infoNode.get("contact");
                if (contactNode.has("name")) {
                    String contactName = contactNode.get("name").asText();
                    model.setContactName(contactName);
                }
            }
        }

        Map<String, JsonNode> schemas = new HashMap<>();
        if (rootNode.has("components")) {
            JsonNode componentsNode = rootNode.get("components");
            if (componentsNode.has("schemas")) {
                JsonNode schemasNode = componentsNode.get("schemas");
                Iterator<Map.Entry<String, JsonNode>> schemaEntries = schemasNode.fields();
                while (schemaEntries.hasNext()) {
                    Map.Entry<String, JsonNode> schemaEntry = schemaEntries.next();
                    schemas.put(schemaEntry.getKey(), schemaEntry.getValue());
                }
            }
        }

        if (rootNode.has("tags")) {
            JsonNode tagsNode = rootNode.get("tags");
            for (JsonNode tagNode : tagsNode) {
                if (tagNode.has("name")) {
                    String tagName = tagNode.get("name").asText();
                    String tagDescription = tagNode.has("description") ? tagNode.get("description").asText() : "";
                    model.getTags().add(new Tag(tagName, tagDescription));
                }
            }
        }

        if (rootNode.has("paths")) {
            JsonNode pathsNode = rootNode.get("paths");
            Iterator<Map.Entry<String, JsonNode>> pathEntries = pathsNode.fields();

            while (pathEntries.hasNext()) {
                Map.Entry<String, JsonNode> pathEntry = pathEntries.next();
                String path = pathEntry.getKey();
                JsonNode pathNode = pathEntry.getValue();

                Iterator<Map.Entry<String, JsonNode>> methodEntries = pathNode.fields();
                while (methodEntries.hasNext()) {
                    Map.Entry<String, JsonNode> methodEntry = methodEntries.next();
                    String method = methodEntry.getKey();
                    JsonNode operationNode = methodEntry.getValue();

                    ApiPath apiPath = new ApiPath();
                    apiPath.setPath(path);
                    apiPath.setMethod(method.toUpperCase());

                    if (operationNode.has("summary")) {
                        apiPath.setSummary(operationNode.get("summary").asText());
                    }

                    if (operationNode.has("description")) {
                        apiPath.setDescription(operationNode.get("description").asText());
                    }

                    if (operationNode.has("tags")) {
                        JsonNode tagsNode = operationNode.get("tags");
                        for (JsonNode tagNode : tagsNode) {
                            apiPath.getTags().add(tagNode.asText());
                        }
                    }

                    if (operationNode.has("parameters")) {
                        JsonNode parametersNode = operationNode.get("parameters");
                        for (JsonNode paramNode : parametersNode) {
                            ApiParameter parameter = new ApiParameter();
                            if (paramNode.has("name")) {
                                parameter.setName(paramNode.get("name").asText());
                            }
                            if (paramNode.has("in")) {
                                parameter.setIn(paramNode.get("in").asText());
                            }
                            if (paramNode.has("required")) {
                                parameter.setRequired(paramNode.get("required").asBoolean());
                            }
                            if (paramNode.has("type")) {
                                parameter.setType(paramNode.get("type").asText());
                            }
                            if (paramNode.has("schema")) {
                                JsonNode schemaNode = paramNode.get("schema");
                                if (schemaNode.has("type")) {
                                    parameter.setType(schemaNode.get("type").asText());
                                }
                            }
                            apiPath.getParameters().add(parameter);
                        }
                    }

                    if (operationNode.has("requestBody")) {
                        JsonNode requestBodyNode = operationNode.get("requestBody");
                        if (requestBodyNode.has("content")) {
                            JsonNode contentNode = requestBodyNode.get("content");
                            Iterator<Map.Entry<String, JsonNode>> contentEntries = contentNode.fields();
                            while (contentEntries.hasNext()) {
                                Map.Entry<String, JsonNode> contentEntry = contentEntries.next();
                                String mediaType = contentEntry.getKey();
                                JsonNode mediaTypeNode = contentEntry.getValue();
                                if (mediaTypeNode.has("schema")) {
                                    JsonNode schemaNode = mediaTypeNode.get("schema");
                                    apiPath.setRequestBodySchema(schemaNode.toString());
                                    
                                    if (schemaNode.has("$ref")) {
                                        String ref = schemaNode.get("$ref").asText();
                                        String schemaName = ref.substring(ref.lastIndexOf("/") + 1);
                                        if (schemas.containsKey(schemaName)) {
                                            JsonNode referencedSchema = schemas.get(schemaName);
                                            String requestExample = generateRequestExample(referencedSchema);
                                            if (!requestExample.isEmpty()) {
                                                apiPath.setRequestExample(requestExample);
                                            }
                                        }
                                    } else if (apiPath.getRequestExample() != null && !apiPath.getRequestExample().isEmpty()) {
                                    } else {
                                        String requestExample = generateRequestExample(schemaNode);
                                        if (!requestExample.isEmpty()) {
                                            apiPath.setRequestExample(requestExample);
                                        }
                                    }
                                }
                                if (mediaTypeNode.has("example")) {
                                    apiPath.setRequestExample(mediaTypeNode.get("example").toString());
                                } else if (mediaTypeNode.has("examples")) {
                                    JsonNode examplesNode = mediaTypeNode.get("examples");
                                    Iterator<Map.Entry<String, JsonNode>> examplesEntries = examplesNode.fields();
                                    if (examplesEntries.hasNext()) {
                                        Map.Entry<String, JsonNode> exampleEntry = examplesEntries.next();
                                        JsonNode exampleValueNode = exampleEntry.getValue();
                                        if (exampleValueNode.has("value")) {
                                            apiPath.setRequestExample(exampleValueNode.get("value").toString());
                                        } else {
                                            apiPath.setRequestExample(exampleValueNode.toString());
                                        }
                                    }
                                }
                            }
                        }
                    }

                    model.getPaths().add(apiPath);
                }
            }
        }

        return model;
    }

    private String generateRequestExample(JsonNode schemaNode) {
        if (schemaNode.has("$ref")) {
            return "";
        } else if (schemaNode.has("type")) {
            String type = schemaNode.get("type").asText();
            if (type.equals("object")) {
                StringBuilder example = new StringBuilder("{");
                if (schemaNode.has("properties")) {
                    JsonNode propertiesNode = schemaNode.get("properties");
                    Iterator<Map.Entry<String, JsonNode>> propertiesEntries = propertiesNode.fields();
                    boolean first = true;
                    while (propertiesEntries.hasNext()) {
                        if (!first) {
                            example.append(",");
                        }
                        first = false;
                        Map.Entry<String, JsonNode> propertyEntry = propertiesEntries.next();
                        String propertyName = propertyEntry.getKey();
                        JsonNode propertySchema = propertyEntry.getValue();
                        example.append("\"").append(propertyName).append("\": ");
                        example.append(generateRequestExample(propertySchema));
                    }
                }
                example.append("}");
                return example.toString();
            } else if (type.equals("string")) {
                if (schemaNode.has("example")) {
                    return "\"" + schemaNode.get("example").asText() + "\"";
                } else {
                    return "\"test\"";
                }
            } else if (type.equals("integer")) {
                if (schemaNode.has("example")) {
                    return schemaNode.get("example").asText();
                } else {
                    return "1";
                }
            } else if (type.equals("boolean")) {
                if (schemaNode.has("example")) {
                    return schemaNode.get("example").asText();
                } else {
                    return "true";
                }
            } else if (type.equals("array")) {
                StringBuilder example = new StringBuilder("[");
                if (schemaNode.has("items")) {
                    JsonNode itemsNode = schemaNode.get("items");
                    example.append(generateRequestExample(itemsNode));
                }
                example.append("]");
                return example.toString();
            }
        }
        return "";
    }
}
