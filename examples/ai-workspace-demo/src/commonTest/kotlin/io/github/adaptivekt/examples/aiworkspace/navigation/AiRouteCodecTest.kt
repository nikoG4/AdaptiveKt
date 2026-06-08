package io.github.adaptivekt.examples.aiworkspace.navigation

import kotlin.test.Test
import kotlin.test.assertEquals

class AiRouteCodecTest {

    @Test
    fun testDashboard() {
        val route = AiRoute.Dashboard
        val encoded = AiRouteCodec.encode(route)
        assertEquals("/", encoded)
        assertEquals(route, AiRouteCodec.decode(encoded))
    }

    @Test
    fun testChatDetail() {
        val route = AiRoute.Chat("123")
        val encoded = AiRouteCodec.encode(route)
        assertEquals("/chats/123", encoded)
        assertEquals(route, AiRouteCodec.decode(encoded))
    }

    @Test
    fun testPromptDetail() {
        val route = AiRoute.PromptDetail("p-456")
        val encoded = AiRouteCodec.encode(route)
        assertEquals("/prompts/p-456", encoded)
        assertEquals(route, AiRouteCodec.decode(encoded))
    }

    @Test
    fun testAssistantDetail() {
        val route = AiRoute.AssistantDetail("ast-789")
        val encoded = AiRouteCodec.encode(route)
        assertEquals("/assistants/ast-789", encoded)
        assertEquals(route, AiRouteCodec.decode(encoded))
    }

    @Test
    fun testFileDetail() {
        val route = AiRoute.FileDetail("file-abc")
        val encoded = AiRouteCodec.encode(route)
        assertEquals("/knowledge/file-abc", encoded)
        assertEquals(route, AiRouteCodec.decode(encoded))
    }

    @Test
    fun testToolDetail() {
        val route = AiRoute.ToolDetail("tool-def")
        val encoded = AiRouteCodec.encode(route)
        assertEquals("/tools/tool-def", encoded)
        assertEquals(route, AiRouteCodec.decode(encoded))
    }

    @Test
    fun testEvaluationDetail() {
        val route = AiRoute.EvaluationDetail("eval-xyz")
        val encoded = AiRouteCodec.encode(route)
        assertEquals("/evaluations/eval-xyz", encoded)
        assertEquals(route, AiRouteCodec.decode(encoded))
    }

    @Test
    fun testInvalidRouteFallsBackToDashboard() {
        assertEquals(AiRoute.Dashboard, AiRouteCodec.decode("/invalid/path/here"))
    }

    @Test
    fun testEmptyPathResolvesToDashboard() {
        assertEquals(AiRoute.Dashboard, AiRouteCodec.decode(""))
        assertEquals(AiRoute.Dashboard, AiRouteCodec.decode("/"))
    }
}
