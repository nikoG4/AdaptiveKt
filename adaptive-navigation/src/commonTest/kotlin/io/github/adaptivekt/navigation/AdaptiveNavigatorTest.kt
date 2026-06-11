package io.github.adaptivekt.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdaptiveNavigatorTest {

    private enum class TestRoute {
        Home, Settings, Details;
        
        val path: String
            get() = name.lowercase()
    }

    private class TestRouteCodec : AdaptiveRouteCodec<TestRoute> {
        override fun encode(route: TestRoute): String = route.path
        override fun decode(path: String): TestRoute? = TestRoute.values().find { it.path == path }
    }

    @Test
    fun testInitialState() {
        val navigator = DefaultAdaptiveNavigator(TestRoute.Home, TestRouteCodec(), null)
        assertEquals(TestRoute.Home, navigator.currentRoute)
        assertFalse(navigator.canGoBack)
    }

    @Test
    fun testNavigateAndGoBack() {
        val navigator = DefaultAdaptiveNavigator(TestRoute.Home, TestRouteCodec(), null)
        
        navigator.navigate(TestRoute.Settings)
        assertEquals(TestRoute.Settings, navigator.currentRoute)
        assertTrue(navigator.canGoBack)

        navigator.navigate(TestRoute.Details)
        assertEquals(TestRoute.Details, navigator.currentRoute)
        assertTrue(navigator.canGoBack)

        navigator.goBack()
        assertEquals(TestRoute.Settings, navigator.currentRoute)
        assertTrue(navigator.canGoBack)

        navigator.goBack()
        assertEquals(TestRoute.Home, navigator.currentRoute)
        assertFalse(navigator.canGoBack)
        
        // At root, goBack does nothing
        navigator.goBack()
        assertEquals(TestRoute.Home, navigator.currentRoute)
        assertFalse(navigator.canGoBack)
    }

    @Test
    fun testReplace() {
        val navigator = DefaultAdaptiveNavigator(TestRoute.Home, TestRouteCodec(), null)
        
        navigator.replace(TestRoute.Settings)
        assertEquals(TestRoute.Settings, navigator.currentRoute)
        // Since we replaced the only item, we still can't go back
        assertFalse(navigator.canGoBack)

        navigator.navigate(TestRoute.Details)
        assertEquals(TestRoute.Details, navigator.currentRoute)
        assertTrue(navigator.canGoBack)

        // Replace the top item
        navigator.replace(TestRoute.Home)
        assertEquals(TestRoute.Home, navigator.currentRoute)
        assertTrue(navigator.canGoBack)

        navigator.goBack()
        assertEquals(TestRoute.Settings, navigator.currentRoute)
        assertFalse(navigator.canGoBack)
    }
}
