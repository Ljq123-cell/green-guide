package com.greenguide.module.ai.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenguide.module.ai.dto.AIResponse;
import com.greenguide.module.ai.service.AIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

@Slf4j
@Service
public class AIServiceImpl implements AIService {

    private final String apiKey;
    private final String apiUrl;
    private final String model;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AIServiceImpl(
            @Value("${deepseek.api-key}") String apiKey,
            @Value("${deepseek.api-url}") String apiUrl,
            @Value("${deepseek.model}") String model) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.model = model;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public AIResponse askGarbageClassification(String question) {
        String systemPrompt = """
            你是一个专业的垃圾分类助手。用户会询问某种垃圾的分类信息。
            请用中文回答，格式如下：
            1. 首先明确告诉用户该垃圾属于哪一类（可回收物/有害垃圾/厨余垃圾/其他垃圾）
            2. 简要解释分类依据
            3. 给出正确的投放指导

            回复末尾，用一行 JSON 标明分类结果（仅此一行，不要多解释）：
            {"category": "RECYCLABLE|HARMFUL|KITCHEN|OTHER", "name": "垃圾名称"}
            """;

        String raw = callDeepSeek(systemPrompt, question);
        return parseResponse(raw);
    }

    @Override
    public AIResponse generateQuizQuestion(String category, String difficulty) {
        return generateQuizQuestion(category, difficulty, "");
    }

    @Override
    public AIResponse generateQuizQuestion(String category, String difficulty, String hint) {
        String catName = switch (category != null ? category : "") {
            case "RECYCLABLE" -> "可回收物";
            case "HARMFUL" -> "有害垃圾";
            case "KITCHEN" -> "厨余垃圾";
            case "OTHER" -> "其他垃圾";
            default -> "垃圾分类";
        };

        // 使用传入的提示词或随机生成一个
        String finalHint = hint != null && !hint.isEmpty() ? hint : getRandomHint();

        String systemPrompt = """
            你是一个垃圾分类教育出题助手。请根据指定类别和难度，生成一道选择题。
            题目格式固定为：
            【题目】xxxxx
            A. xxxx
            B. xxxx
            C. xxxx
            D. xxxx
            【答案】X
            【解析】xxxxx

            要求：
            1. 题目内容要有变化，不要重复
            2. 选项要合理设置干扰项
            3. 解析要详细准确
            4. 确保答案正确
            """;

        String userMsg = "请生成一道关于" + catName + "的" + (difficulty != null ? difficulty : "") + "选择题。" + finalHint;
        // 使用更高的温度值增加随机性，生成题目时温度为0.8
        String raw = callDeepSeekWithTemperature(systemPrompt, userMsg, 0.8);

        AIResponse resp = new AIResponse();
        resp.setAnswer(raw);
        resp.setClassification(category);
        return resp;
    }

    private String getRandomHint() {
        String[] hints = {
            "请选择一个具体的垃圾物品作为题目对象。",
            "可以围绕日常生活中的常见垃圾设计题目。",
            "请设计一道有教育意义的垃圾分类题目。",
            "题目应该具有实用性和指导性。",
            "请考虑垃圾分类的难点和易错点。",
            "可以设计一些容易混淆的垃圾分类题目。"
        };
        int index = (int)(Math.random() * hints.length);
        return hints[index];
    }

    private String callDeepSeekWithTemperature(String systemPrompt, String userMessage, double temperature) {
        try {
            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("model", model);
            requestBody.put("temperature", temperature); // 使用传入的温度值

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", systemPrompt));
            messages.add(Map.of("role", "user", "content", userMessage));
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 1024);

            String json = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .timeout(Duration.ofSeconds(30))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                return root.path("choices").get(0).path("message").path("content").asText();
            } else {
                log.error("DeepSeek API error: {} - {}", response.statusCode(), response.body());
                return "抱歉，AI 服务暂时不可用（" + response.statusCode() + "），请稍后重试。";
            }
        } catch (Exception e) {
            log.error("DeepSeek call failed", e);
            return "抱歉，AI 服务请求超时，请稍后重试。";
        }
    }

    private String callDeepSeek(String systemPrompt, String userMessage) {
        try {
            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("model", model);
            requestBody.put("temperature", 0.3);

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", systemPrompt));
            messages.add(Map.of("role", "user", "content", userMessage));
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 1024);

            String json = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .timeout(Duration.ofSeconds(30))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                return root.path("choices").get(0).path("message").path("content").asText();
            } else {
                log.error("DeepSeek API error: {} - {}", response.statusCode(), response.body());
                return "抱歉，AI 服务暂时不可用（" + response.statusCode() + "），请稍后重试。";
            }
        } catch (Exception e) {
            log.error("DeepSeek call failed", e);
            return "抱歉，AI 服务请求超时，请稍后重试。";
        }
    }

    private AIResponse parseResponse(String raw) {
        AIResponse resp = new AIResponse();
        resp.setAnswer(raw);

        // 尝试从回复中提取分类 JSON，并从文本中移除
        try {
            int start = raw.lastIndexOf("{");
            int end = raw.lastIndexOf("}");
            if (start >= 0 && end > start) {
                String jsonStr = raw.substring(start, end + 1);
                JsonNode node = objectMapper.readTree(jsonStr);
                if (node.has("category")) {
                    resp.setClassification(node.get("category").asText());
                    // 从回答文本中去除这段 JSON
                    String cleanAnswer = raw.substring(0, start).trim();
                    resp.setAnswer(cleanAnswer);
                }
                if (node.has("name")) {
                    resp.setExtra(Map.of("name", node.get("name").asText()));
                }
            }
        } catch (Exception ignored) {
            // 解析失败不影响主流程
        }

        return resp;
    }
}
