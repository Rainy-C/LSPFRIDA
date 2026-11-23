package com.bail.lspfrifa.ui.screen

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Info
import top.yukonga.miuix.kmp.icon.extended.Ok
import top.yukonga.miuix.kmp.theme.MiuixTheme
import com.bail.lspfrifa.ui.component.MiuixPageBackground
import com.bail.lspfrifa.ui.component.UiTokens

@Composable
fun DashboardScreenV093(
    isXposedActive: Boolean = false,
    apiVersion: Int = 0,
    frameworkName: String = "",
    frameworkVersion: String = "",
    activeProjectCount: Int = 0,
    activeProcessCount: Int = 0,
    onOpenSelector: () -> Unit = {},
) {
    val statusTitle = if (isXposedActive) "已激活" else "模块未激活"
    val statusSummary = if (isXposedActive) {
        if (apiVersion > 0) "Xposed API $apiVersion" else "已检测到 Xposed 框架"
    } else "未检测到 Xposed 框架"
    val statusColor = if (isXposedActive) Color(0xFF35C759) else MiuixTheme.colorScheme.primary
    val frameworkText = listOf(frameworkName, frameworkVersion)
        .filter { it.isNotBlank() }
        .joinToString(" ")
    var showDocs by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MiuixPageBackground(),
        contentWindowInsets = WindowInsets(0),
        topBar = { TopAppBar(title = "LSPFRIFA", largeTitle = "LSPFRIFA") },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 128.dp),
        ) {
            item {
                StatusCard(
                    title = statusTitle,
                    summary = statusSummary,
                    framework = frameworkText,
                    active = isXposedActive,
                    statusColor = statusColor,
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = UiTokens.CardRadius,
                    insideMargin = PaddingValues(16.dp),
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        HomeMetric("活跃项目", activeProjectCount, Modifier.weight(1f))
                        HomeMetric("在线进程", activeProcessCount, Modifier.weight(1f))
                    }
                }
            }

            item {
                Button(onClick = onOpenSelector, modifier = Modifier.fillMaxWidth()) {
                    Text("选择目标应用")
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = UiTokens.CardRadius,
                    insideMargin = PaddingValues(horizontal = UiTokens.CardMarginH, vertical = UiTokens.CardMarginV),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(UiTokens.CardSpacing)) {
                        HomeInfoText("软件版本", "1.0 (1)")
                        HomeInfoText("API 版本", if (apiVersion > 0) apiVersion.toString() else "?")
                        HomeInfoText("设备型号", "${Build.MANUFACTURER} ${Build.MODEL}".ifBlank { Build.DEVICE })
                        HomeInfoText("安卓版本", "${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = UiTokens.CardRadius,
                    insideMargin = PaddingValues(horizontal = UiTokens.CardMarginH, vertical = UiTokens.CardMarginV),
                    showIndication = true,
                    onClick = { showDocs = true },
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("插件文档", fontSize = UiTokens.TitleSize, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "查看脚本、注入与日志使用说明",
                                fontSize = UiTokens.SubtitleSize,
                                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                            )
                        }
                        Text("›", fontSize = 30.sp, color = MiuixTheme.colorScheme.onSurfaceVariantSummary)
                    }
                }
            }
        }
    }

    if (showDocs) {
        Dialog(onDismissRequest = { showDocs = false }) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = UiTokens.CardRadius,
                insideMargin = PaddingValues(16.dp),
            ) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("插件文档", fontSize = UiTokens.TitleSize, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "使用步骤：\n\n" +
                                "1. 在「项目」页选择目标应用，进入项目详情。\n" +
                                "2. 编写 JavaScript 脚本（Frida GumJS / QuickJS）。\n" +
                                "3. 点击「运行脚本」下发并热加载；脚本会自动保存，目标应用冷启动时自动加载。\n" +
                                "4. 在「分析」页签查看实时日志（console.log / send）。\n" +
                                "5. 项目列表右侧 Switch 可快速启用/停用目标应用的 Hook。\n\n" +
                                "提示：只有已选择的目标应用才会被注入（系统关键进程除外）。",
                        fontSize = UiTokens.SubtitleSize,
                        lineHeight = 21.sp,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { showDocs = false },
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text("知道了") }
                }
            }
        }
    }
}

@Composable
private fun StatusCard(
    title: String,
    summary: String,
    framework: String,
    active: Boolean,
    statusColor: Color,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = UiTokens.CardRadius,
        insideMargin = PaddingValues(16.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(44.dp).background(statusColor.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = if (active) MiuixIcons.Ok else MiuixIcons.Info,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(24.dp),
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(title, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Text(summary, fontSize = 13.sp, color = MiuixTheme.colorScheme.onSurfaceVariantSummary)
                if (framework.isNotBlank()) {
                    Text(framework, fontSize = 12.sp, color = MiuixTheme.colorScheme.onSurfaceVariantSummary)
                }
            }
        }
    }
}

@Composable
private fun HomeMetric(label: String, value: Int, modifier: Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(value.toString(), fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
        Text(label, fontSize = 13.sp, color = MiuixTheme.colorScheme.onSurfaceVariantSummary)
    }
}

@Composable
private fun HomeInfoText(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, modifier = Modifier.weight(1f), fontSize = 13.sp, color = MiuixTheme.colorScheme.onSurfaceVariantSummary)
        Text(value, modifier = Modifier.weight(1.4f), fontSize = 14.sp, textAlign = TextAlign.End)
    }
}
