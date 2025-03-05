package com.simon.taller1.ui.theme

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.simon.taller1.api.KtorApiClient
import com.simon.taller1.modelo.User
import okhttp3.internal.http2.Header

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                UserListScreen () { user ->
                    //Cambiar a pantalla de detalles de usuario
                    Toast.makeText(
                        this,
                        "Usuario ${user.firstName} seleccionado",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


//Lista de usuarios con LazyColumn y StickyHeader
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserListScreen(onUserClick: (User) -> Unit) {
    val ktorApiClient = KtorApiClient()
    var users by remember { mutableStateOf(listOf<User>()) }

    LaunchedEffect(Unit) {
        users = ktorApiClient.getUsers().users;
    }

    LazyColumn {
        stickyHeader {
            Surface(
                color = MaterialTheme.colorScheme.primary
            ) {
                Header(users = users)
            }
        }

        items(users) { user ->
            UserListItem(user = user, onClick = { onUserClick(user) })
        }
    }
}

// Composable para crear el header de la lista
@Composable
fun Header (users: List<User>) {
    Text (
        text = "Total de usuarios: ${users.size}",
        modifier = Modifier
            .fillMaxWidth().padding(16.dp).statusBarsPadding(),
        style = MaterialTheme.typography.headlineSmall
    )
}

//Composable para crear item en la lista, se usa ListItem de Material3

@Composable
fun UserListItem(user: User, onClick: () -> Unit) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        ListItem(
            modifier = Modifier.padding(5.dp),
            leadingContent = {
                AsyncImage(
                    model = user.image,
                    contentDescription = null,
                    modifier = Modifier.size(85.dp)
                        .clip(CircleShape)
                )
            },
            headlineContent = {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            },
            supportingContent = {
                Text(
                    text = user.company.name,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            trailingContent = {
                Icon(Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.Gray)
            }
        )
        HorizontalDivider(thickness = 1.dp)
    }
}


