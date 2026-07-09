package com.example.aiwriter.prompt;

public enum PromptTemplate {

    GENERATE(
            "你是一位专业的文章撰写助手。请根据用户提供的主题和要求，生成结构清晰、内容丰富的文章。",
            "请根据以下主题生成一篇文章：\n主题：%s\n风格要求：%s\n\n要求：\n1. 内容丰富，逻辑清晰\n2. 语言通顺，表达准确\n3. 篇幅适中，约800-1500字\n4. 包含引言、正文和结论"
    ),

    POLISH(
            "你是一位专业的语言润色专家。请帮助用户优化文章的语言表达、语法和结构，使文章更加流畅、专业、有说服力。",
            "请润色以下文章，优化语法、风格和表达：\n\n%s\n\n风格要求：%s\n\n优化方向：\n1. 语法和拼写错误修正\n2. 词汇选择优化\n3. 句式结构调整\n4. 段落逻辑优化\n5. 整体风格统一"
    ),

    EXPAND(
            "你是一位专业的内容扩展专家。请帮助用户将简短的内容扩展为更详细、更丰富的文章，保持原意不变的同时增加更多细节和深度。",
            "请将以下内容扩写至约%d字：\n\n%s\n\n扩写要求：\n1. 保持核心观点不变\n2. 增加详细的例子和数据\n3. 补充背景信息和相关知识\n4. 深化分析和论证\n5. 丰富语言表达"
    ),

    SUMMARIZE(
            "你是一位专业的内容摘要专家。请帮助用户提取文章的核心要点，生成简明扼要的摘要。",
            "请为以下文章生成一篇摘要，字数不超过%d字：\n\n%s\n\n摘要要求：\n1. 涵盖文章核心观点\n2. 保留关键数据和结论\n3. 结构清晰，逻辑连贯\n4. 语言简洁，表达准确\n5. 不遗漏重要信息"
    ),

    TRANSLATE(
            "你是一位专业的翻译专家。请帮助用户进行准确、流畅的多语种翻译。",
            "请将以下内容翻译成%s：\n\n%s\n\n翻译要求：\n1. 翻译准确，语义完整\n2. 语言流畅，符合目标语言习惯\n3. 保留原文风格和语气\n4. 专业术语翻译正确\n5. 格式保持一致"
    );

    private final String systemPrompt;
    private final String userPromptTemplate;

    PromptTemplate(String systemPrompt, String userPromptTemplate) {
        this.systemPrompt = systemPrompt;
        this.userPromptTemplate = userPromptTemplate;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public String getUserPrompt(Object... args) {
        return String.format(userPromptTemplate, args);
    }
}
