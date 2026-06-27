package io.github.adaptivekt.site

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adaptivekt.data.AdaptiveDataColumn
import io.github.adaptivekt.data.AdaptiveDataContent
import io.github.adaptivekt.data.AdaptiveDataDisplayMode
import io.github.adaptivekt.data.AdaptiveDataMobileRole
import io.github.adaptivekt.data.AdaptiveDataView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text

internal data class ComparisonUser(
    val name: String,
    val role: String,
    val status: String,
)

@Composable
internal fun AdaptiveUserDirectoryFixture(users: List<ComparisonUser>) {
    val columns = listOf(
        AdaptiveDataColumn<ComparisonUser>(
            id = "name",
            header = "Name",
            mobileRole = AdaptiveDataMobileRole.Title,
            cell = { user -> Text(user.name, fontWeight = FontWeight.Bold) },
        ),
        AdaptiveDataColumn<ComparisonUser>(
            id = "role",
            header = "Role",
            mobileRole = AdaptiveDataMobileRole.Subtitle,
            cell = { user -> Text(user.role) },
        ),
        AdaptiveDataColumn<ComparisonUser>(
            id = "status",
            header = "Status",
            mobileRole = AdaptiveDataMobileRole.Status,
            cell = { user -> Text(user.status) },
        )
    )

    AdaptiveDataView(
        state = AdaptiveDataContent(users),
        columns = columns,
        displayMode = AdaptiveDataDisplayMode.Auto,
    )
}

@Composable
internal fun PlainComposeUserDirectoryFixture(users: List<ComparisonUser>) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val useCards = maxWidth < 840.dp

        if (users.isEmpty()) {
            EmptyUsersStateFixture()
        } else if (useCards) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(users) { user ->
                    UserMobileCardFixture(user)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface, RoundedCornerShape(12.dp))
                    .border(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
            ) {
                UserTableHeaderFixture()
                Divider()
                users.forEachIndexed { index, user ->
                    UserTableRowFixture(user)
                    if (index < users.lastIndex) Divider()
                }
            }
        }
    }
}

@Composable
private fun UserMobileCardFixture(user: ComparisonUser) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = 0.dp,
        border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(user.name, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(user.role, color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f))
                }
                UserStatusBadgeFixture(user.status)
            }
        }
    }
}

@Composable
private fun UserTableHeaderFixture() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.05f))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text("Name", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Text("Role", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Text("Status", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun UserTableRowFixture(user: ComparisonUser) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(user.name, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Text(user.role, modifier = Modifier.weight(1f))
        Box(modifier = Modifier.weight(1f)) {
            UserStatusBadgeFixture(user.status)
        }
    }
}

@Composable
private fun EmptyUsersStateFixture() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No data available", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Try adjusting filters or create a new record.", color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f))
    }
}

@Composable
private fun UserStatusBadgeFixture(status: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.primary.copy(alpha = 0.1f), RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(status, color = MaterialTheme.colors.primary, fontSize = 12.sp)
    }
}
