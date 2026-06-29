package io.github.adaptivekt.site

internal val AdaptiveDataViewComparisonCode = """
@Composable
fun UserDirectory(users: List<User>) {
    val columns = listOf(
        AdaptiveDataColumn<User>(
            id = "name",
            header = "Name",
            mobileRole = AdaptiveDataMobileRole.Title,
            cell = { user -> Text(user.name, fontWeight = FontWeight.Bold) },
        ),
        AdaptiveDataColumn<User>(
            id = "role",
            header = "Role",
            mobileRole = AdaptiveDataMobileRole.Subtitle,
            cell = { user -> Text(user.role) },
        ),
        AdaptiveDataColumn<User>(
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
""".trimIndent()

internal val PlainComposeDataViewComparisonCode = """
@Composable
fun UserDirectory(users: List<User>) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val useCards = maxWidth < 840.dp

        if (users.isEmpty()) {
            EmptyUsersState()
        } else if (useCards) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(users) { user ->
                    UserMobileCard(user)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface, RoundedCornerShape(12.dp))
                    .border(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
            ) {
                UserTableHeader()
                Divider()
                users.forEachIndexed { index, user ->
                    UserTableRow(user)
                    if (index < users.lastIndex) Divider()
                }
            }
        }
    }
}

@Composable
private fun UserMobileCard(user: User) {
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
                UserStatusBadge(user.status)
            }
        }
    }
}

@Composable
private fun UserTableHeader() {
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
private fun UserTableRow(user: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(user.name, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Text(user.role, modifier = Modifier.weight(1f))
        Box(modifier = Modifier.weight(1f)) {
            UserStatusBadge(user.status)
        }
    }
}

@Composable
private fun EmptyUsersState() {
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
private fun UserStatusBadge(status: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.primary.copy(alpha = 0.1f), RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(status, color = MaterialTheme.colors.primary, fontSize = 12.sp)
    }
}
""".trimIndent()
