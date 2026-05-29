package io.github.adaptivekt.site

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.components.AdaptiveBadge
import io.github.adaptivekt.components.AdaptiveBadgeTone
import io.github.adaptivekt.core.AdaptiveTokens

internal val SiteInk = Color(0xFF162033)
internal val SiteMuted = Color(0xFF64748B)
internal val SiteLine = Color(0xFFD8E0EC)
internal val SiteSoft = Color(0xFFF7F9FC)
internal val SiteAccent = Color(0xFF176B87)

@Composable
internal fun SiteLayout(
    route: SiteRoute,
    onNavigate: (SiteRoute) -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SiteSoft),
    ) {
        SiteNavigation(route = route, onNavigate = onNavigate)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 1180.dp)
                    .padding(horizontal = 24.dp, vertical = 28.dp),
            ) {
                content()
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
internal fun PageHeader(
    eyebrow: String,
    title: String,
    description: String,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AdaptiveBadge(text = eyebrow, tone = AdaptiveBadgeTone.Info)
        Spacer(modifier = Modifier.height(12.dp))
        SiteText(
            text = title,
            fontSize = 42.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SiteInk,
        )
        Spacer(modifier = Modifier.height(10.dp))
        SiteText(
            text = description,
            fontSize = 16.sp,
            color = SiteMuted,
            maxLines = 4,
        )
    }
}

@Composable
internal fun SiteText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: androidx.compose.ui.unit.TextUnit = 14.sp,
    color: Color = SiteInk,
    fontWeight: FontWeight = FontWeight.Normal,
    maxLines: Int = 1,
    monospace: Boolean = false,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = TextStyle(
            fontSize = fontSize,
            lineHeight = fontSize * 1.18f,
            color = color,
            fontWeight = fontWeight,
            fontFamily = if (monospace) FontFamily.Monospace else FontFamily.Default,
        ),
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
internal fun FeatureRow(title: String, body: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 7.dp)
                .width(8.dp)
                .height(8.dp)
                .background(SiteAccent),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            SiteText(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(4.dp))
            SiteText(text = body, color = SiteMuted, maxLines = 3)
        }
    }
}

@Composable
internal fun CodeBlock(code: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0F172A))
            .border(1.dp, Color(0xFF1E293B))
            .padding(AdaptiveTokens.Spacing.Medium),
    ) {
        SiteText(
            text = code.trimIndent(),
            color = Color(0xFFE2E8F0),
            fontSize = 12.sp,
            monospace = true,
            maxLines = 12,
        )
    }
}
