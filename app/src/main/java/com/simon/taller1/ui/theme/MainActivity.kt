package com.simon.taller1.ui.theme

/****
 * Realizado por Simón y Jorge
 * Basado en ejemplos dados por el Profesor Mauricio Cuibiedes
 */

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil3.compose.AsyncImage
import com.simon.taller1.api.KtorApiClient
import com.simon.taller1.modelo.User


class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimonJorgeTheme {
                NavigationStack()
            }
        }
    }
}

//prueba committ
//Lista de usuarios con LazyColumn y StickyHeader
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserListScreen(users: List<User>, onUserClick: (User) -> Unit) {
    Surface (
        color = MaterialTheme.colorScheme.background
    ) {
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

// Navegacion Taller 1
@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    val ktorApiClient = KtorApiClient()
    var users by remember { mutableStateOf(listOf<User>()) }

    LaunchedEffect(Unit) {
        users = ktorApiClient.getUsers().users
    }

    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(route = Screen.Main.route) {
            // Guarda el estado en el backstack
            MainScreen(navController = navController, users)
        }

        composable(route = Screen.Detail.route) {
            //Carga el estado guardado en el backstack
            DetailScreen(navController)
        }
    }
}

sealed class Screen(val route: String) {
    object Main: Screen("main_screen")
    object Detail: Screen("detail_screen")

}

@Composable
fun MainScreen(navController: NavController, users: List<User>) {
    UserListScreen(users) { user ->
        navController.currentBackStackEntry?.savedStateHandle?.set("user", user)
        navController.navigate(Screen.Detail.route)
    }
}



@Composable
fun DetailScreen(navController: NavController) {
    //Obtiene la pantalla anterior en la pila de navegacion
    val user = navController.previousBackStackEntry
        ?.savedStateHandle // accede al usuario guardado
        ?.get<User>("user")

    // Revisa si es nulo y imprime la info en una funcion de alcance
    // la funcion permite acceder sin usar el nombre user
    user?.let {
        Card(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                PfP(it)
                NameUser(it)
                UserText(it)
            }
        }
    }
}


@Composable
fun UserText(user: User) {
    val context = LocalContext.current
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Empresa: ${user.company.name}", style = MaterialTheme.typography.bodyMedium)
        Text(
            text = "Teléfono: ${user.phone}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable {
                dialPhoneNumber(context, user.phone)
            }
        )
        Text(text = "Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Edad: ${user.age}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Género: ${user.gender}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Altura: ${user.height} m", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Peso: ${user.weight} kg", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Universidad: ${user.university}", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun PfP(user: User){
    Row (
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = user.image,
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
    }
}

@Composable
fun NameUser(user: User) {
    Row (
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "${user.firstName} ${user.lastName}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
        )
    }
}

//Llamar cliente
fun dialPhoneNumber(context: android.content.Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    context.startActivity(intent)
}