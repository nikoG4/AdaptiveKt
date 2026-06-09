package io.github.adaptivekt.examples.aiworkspace.mock

import io.github.adaptivekt.examples.aiworkspace.model.*

public object AiMockData {
    public val models: List<ModelProfile> = listOf(
        ModelProfile("gpt-4-turbo", "GPT-4 Turbo", "OpenAI", 128000, true, true),
        ModelProfile("gpt-3.5-turbo", "GPT-3.5 Turbo", "OpenAI", 16385, true, false),
        ModelProfile("gemini-1.5-pro", "Gemini 1.5 Pro", "Google", 1048576, true, true),
        ModelProfile("gemini-1.5-flash", "Gemini 1.5 Flash", "Google", 1048576, true, true),
        ModelProfile("claude-3-opus", "Claude 3 Opus", "Anthropic", 200000, true, true)
    )

    public val tools: List<AiTool> = listOf(
        AiTool("t1", "Web Search", "Search the internet for real-time information", true, ToolRisk.Low, "Success"),
        AiTool("t2", "Code Interpreter", "Execute python code in a sandboxed environment", true, ToolRisk.Medium, "Success"),
        AiTool("t3", "Database Query", "Run SQL queries against the internal analytics DB", false, ToolRisk.High, "Failed"),
        AiTool("t4", "Jira Integration", "Read and create Jira tickets", true, ToolRisk.Low, "Success"),
        AiTool("t5", "GitHub Actions", "Trigger GitHub Action workflows", false, ToolRisk.Medium, "Never Run"),
        AiTool("t6", "Slack Notify", "Send messages to Slack channels", true, ToolRisk.Low, "Success"),
        AiTool("t7", "Internal API", "Access internal CRM data", false, ToolRisk.Medium, "Success"),
        AiTool("t8", "File Generator", "Generate PDF/Excel reports", true, ToolRisk.Low, "Success")
    )

    public val files: List<KnowledgeFile> = listOf(
        KnowledgeFile("f1", "Q3_Financial_Report.pdf", "PDF", "2.4 MB", FileIndexStatus.Ready, 450, "2023-10-01"),
        KnowledgeFile("f2", "Employee_Handbook_2024.pdf", "PDF", "1.1 MB", FileIndexStatus.Ready, 120, "2024-01-15"),
        KnowledgeFile("f3", "API_Documentation.md", "MD", "45 KB", FileIndexStatus.Ready, 35, "2024-02-10"),
        KnowledgeFile("f4", "Customer_Feedback_Q1.csv", "CSV", "14.2 MB", FileIndexStatus.Indexing, 0, "2024-04-01"),
        KnowledgeFile("f5", "Architecture_Diagram.png", "Image", "4.1 MB", FileIndexStatus.Failed, 0, "2024-03-22"),
        KnowledgeFile("f6", "Project_Alpha_Specs.docx", "DOCX", "800 KB", FileIndexStatus.Ready, 85, "2024-01-20"),
        KnowledgeFile("f7", "Server_Logs_Feb.txt", "TXT", "45.1 MB", FileIndexStatus.Uploaded, 0, "2024-03-01"),
        KnowledgeFile("f8", "Marketing_Copy_Drafts.txt", "TXT", "12 KB", FileIndexStatus.Ready, 5, "2024-04-12"),
        KnowledgeFile("f9", "Sales_Deck.pptx", "PPTX", "8.5 MB", FileIndexStatus.Ready, 140, "2024-02-28"),
        KnowledgeFile("f10", "Product_Roadmap.csv", "CSV", "1.2 MB", FileIndexStatus.Ready, 60, "2024-01-10"),
        KnowledgeFile("f11", "User_Interviews.mp3", "Audio", "24 MB", FileIndexStatus.Failed, 0, "2024-03-15"),
        KnowledgeFile("f12", "Legal_Contracts.zip", "Archive", "112 MB", FileIndexStatus.Uploaded, 0, "2024-04-05")
    )

    public val assistants: List<AssistantProfile> = listOf(
        AssistantProfile("a1", "Code Copilot", "Expert in Kotlin and Compose", "CC", "gemini-1.5-pro", 0.2f, 2, 1, listOf("dev", "kotlin")),
        AssistantProfile("a2", "Data Analyst", "Helps analyze CSV and generate SQL", "DA", "gpt-4-turbo", 0.1f, 3, 5, listOf("data", "sql")),
        AssistantProfile("a3", "Creative Writer", "Drafts marketing copy and emails", "CW", "claude-3-opus", 0.8f, 0, 2, listOf("marketing", "writing")),
        AssistantProfile("a4", "Support Agent", "Answers tier 1 customer questions", "SA", "gpt-3.5-turbo", 0.4f, 1, 10, listOf("support", "external")),
        AssistantProfile("a5", "DevOps Helper", "Generates scripts and config files", "DO", "gemini-1.5-flash", 0.1f, 4, 0, listOf("devops", "bash")),
        AssistantProfile("a6", "Legal Reviewer", "Reviews contracts against company policy", "LR", "gpt-4-turbo", 0.0f, 1, 8, listOf("legal", "internal"))
    )

    public val prompts: List<PromptTemplate> = listOf(
        PromptTemplate("p1", "Refactor Code", "Refactors the provided code to follow clean architecture.", "Development", listOf("code", "refactor"), "Refactor the following {{language}} code to follow clean architecture principles:\n\n```\n{{code}}\n```", listOf("language", "code"), true),
        PromptTemplate("p2", "Summarize Meeting", "Creates a concise summary of meeting transcripts.", "Productivity", listOf("meeting", "notes"), "Summarize the following meeting transcript. Extract the key decisions and action items.\n\n{{transcript}}", listOf("transcript"), true),
        PromptTemplate("p3", "Generate Unit Tests", "Generates tests using JUnit5.", "Development", listOf("test", "junit"), "Write JUnit 5 tests for the following class:\n\n{{class_code}}", listOf("class_code"), false),
        PromptTemplate("p4", "Write Blog Post", "Drafts a blog post based on an outline.", "Marketing", listOf("blog", "content"), "Write a 500-word blog post about {{topic}}. Use the following outline:\n{{outline}}", listOf("topic", "outline"), true),
        PromptTemplate("p5", "Translate to SQL", "Converts plain text to SQL query.", "Data", listOf("sql", "db"), "Translate the following request into a valid SQL query for a PostgreSQL database:\n\nRequest: {{request}}\nSchema: {{schema}}", listOf("request", "schema"), false),
        PromptTemplate("p6", "Code Review", "Reviews code for bugs and style issues.", "Development", listOf("review"), "Review the following pull request. Point out security issues, performance bottlenecks, and style violations.\n\n{{pr_diff}}", listOf("pr_diff"), false),
        PromptTemplate("p7", "Customer Apology", "Drafts a professional apology email.", "Support", listOf("email"), "Draft an apology email to a customer who experienced {{issue}}. Offer them {{compensation}}.", listOf("issue", "compensation"), false),
        PromptTemplate("p8", "Extract JSON", "Extracts structured JSON from unstructured text.", "Data", listOf("json"), "Extract the following entities from the text and format them as a JSON array of objects with keys: name, role, company.\n\nText: {{text}}", listOf("text"), true),
        PromptTemplate("p9", "Explain Concept", "Explains a complex topic simply.", "Education", listOf("explain", "simple"), "Explain the concept of {{concept}} to a 10-year-old.", listOf("concept"), false),
        PromptTemplate("p10", "Generate Regex", "Creates regular expressions from descriptions.", "Development", listOf("regex"), "Create a regular expression that matches: {{description}}. Provide examples of matches and non-matches.", listOf("description"), false)
    )

    public val evaluations: List<EvaluationRun> = listOf(
        EvaluationRun("e1", "Nightly Code Gen", "Kotlin-Eval-v2", "gemini-1.5-pro", 92, "1.2s", 450, listOf(
            EvaluationCase("c1", "Write a binary search in Kotlin", "fun binarySearch...", "fun binarySearch...", true, 1.0f),
            EvaluationCase("c2", "Implement a linked list", "class LinkedList...", "class Node...", true, 0.9f)
        )),
        EvaluationRun("e2", "SQL Translation Benchmark", "Spider-DB", "gpt-4-turbo", 85, "2.1s", 120, listOf(
            EvaluationCase("c3", "Get employees in Sales", "SELECT * FROM emp WHERE dept='Sales'", "SELECT * FROM emp WHERE dept='Sales'", true, 1.0f),
            EvaluationCase("c4", "Count users by country", "SELECT count(*), country GROUP BY country", "SELECT country, count(*) GROUP BY country", true, 0.8f)
        )),
        EvaluationRun("e3", "Customer Support Tone", "Support-QA-100", "claude-3-opus", 98, "1.5s", 200, emptyList()),
        EvaluationRun("e4", "Regex Generation", "Regex-Hard", "gpt-3.5-turbo", 65, "0.8s", 50, emptyList()),
        EvaluationRun("e5", "Summarization Test", "CNN-DailyMail", "gemini-1.5-flash", 88, "0.5s", 150, emptyList())
    )

    public val defaultMetrics: UsageMetric = UsageMetric(
        tokensThisWeek = 1450000,
        estimatedCost = 14.50f,
        activeConversations = 124,
        filesIndexed = 45,
        evaluationPassRate = 88,
        toolsEnabledCount = 4
    )

    public val defaultSettings: AiSettings = AiSettings(
        defaultModelId = "gemini-1.5-pro",
        defaultTemperature = 0.7f,
        developerMode = true,
        saveHistory = true
    )

    public val conversations: List<Conversation> = listOf(
        Conversation("c1", "Refactoring AdaptiveGrid", "a1", listOf(
            ChatMessage("m1", MessageRole.User, listOf(MessagePart.Text("How do I make the grid columns responsive?")), "10:00 AM", 12, MessageStatus.Complete),
            ChatMessage("m2", MessageRole.Assistant, listOf(MessagePart.Text("You can use AdaptiveGrid Defaults. Here is an example:"), MessagePart.CodeBlock("kotlin", "AdaptiveGrid(\n  columns = AdaptiveGridDefaults.columns()\n) { ... }")), "10:01 AM", 45, MessageStatus.Complete)
        ), "10:01 AM", true, false, listOf("ui", "kotlin")),
        Conversation("c2", "Q3 Revenue Analysis", "a2", listOf(
            ChatMessage("m3", MessageRole.User, listOf(MessagePart.Text("What was the total revenue in Q3?")), "Yesterday", 10, MessageStatus.Complete),
            ChatMessage("m4", MessageRole.Assistant, listOf(MessagePart.ToolCall("Database Query", ToolCallStatus.Success, "Queried revenue table for Q3"), MessagePart.Text("The total revenue for Q3 was $4.2M, representing a 15% increase over Q2.")), "Yesterday", 85, MessageStatus.Complete)
        ), "Yesterday", false, false, listOf("finance")),
        Conversation("c3", "Marketing Email Draft", "a3", listOf(
            ChatMessage("m5", MessageRole.User, listOf(MessagePart.Text("Draft an email for our new AI feature launch.")), "Monday", 15, MessageStatus.Complete),
            ChatMessage("m6", MessageRole.Assistant, listOf(MessagePart.Text("Subject: Introducing our new AI Workspace!\n\nHi team,\n\nWe're excited to announce...")), "Monday", 120, MessageStatus.Complete)
        ), "Monday", true, false, listOf("launch")),
        Conversation("c4", "Reset Password Help", "a4", listOf(
            ChatMessage("m7", MessageRole.User, listOf(MessagePart.Text("A user is complaining they can't reset their password.")), "Tuesday", 12, MessageStatus.Complete),
            ChatMessage("m8", MessageRole.Assistant, listOf(MessagePart.Source("Help Center: Password Reset", "Users must click the link within 24 hours."), MessagePart.Text("Please tell the user to check their spam folder and ensure they are clicking the link within 24 hours of requesting it.")), "Tuesday", 55, MessageStatus.Complete)
        ), "Tuesday", false, true, listOf("support")),
        Conversation("c5", "Docker Compose Setup", "a5", listOf(
            ChatMessage("m9", MessageRole.User, listOf(MessagePart.Text("Create a docker-compose file for postgres and redis.")), "Last Week", 12, MessageStatus.Complete),
            ChatMessage("m10", MessageRole.Assistant, listOf(MessagePart.CodeBlock("yaml", "version: '3'\nservices:\n  db:\n    image: postgres\n  cache:\n    image: redis")), "Last Week", 60, MessageStatus.Complete)
        ), "Last Week", false, false, listOf("devops")),
        Conversation("c6", "NDA Review", "a6", listOf(
            ChatMessage("m11", MessageRole.User, listOf(MessagePart.Text("Does this NDA cover third-party contractors?")), "Last Week", 10, MessageStatus.Complete),
            ChatMessage("m12", MessageRole.Assistant, listOf(MessagePart.Text("Yes, section 4.2 explicitly extends confidentiality obligations to third-party contractors.")), "Last Week", 35, MessageStatus.Complete)
        ), "Last Week", false, false, listOf("legal")),
        Conversation("c7", "Generate UI Components", "a1", listOf(
            ChatMessage("m13", MessageRole.User, listOf(MessagePart.Text("Create a generic Settings card in Compose.")), "2 weeks ago", 12, MessageStatus.Complete),
            ChatMessage("m14", MessageRole.Assistant, listOf(MessagePart.CodeBlock("kotlin", "@Composable\nfun SettingsCard(title: String) { ... }")), "2 weeks ago", 80, MessageStatus.Complete)
        ), "2 weeks ago", false, false, listOf("compose")),
        Conversation("c8", "Monthly User Growth", "a2", listOf(
            ChatMessage("m15", MessageRole.User, listOf(MessagePart.Text("Plot user growth for the last 6 months.")), "1 month ago", 12, MessageStatus.Complete),
            ChatMessage("m16", MessageRole.Assistant, listOf(MessagePart.ToolCall("Code Interpreter", ToolCallStatus.Success, "Generated matplotlib chart"), MessagePart.Text("User growth has been steady at 5% month-over-month.")), "1 month ago", 95, MessageStatus.Complete)
        ), "1 month ago", false, false, listOf("metrics"))
    )
}
