package com.example.proyecto_1

import android.R
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_1.ui.theme.Proyecto_1Theme
import java.math.BigDecimal
import java.math.BigInteger

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Proyecto_1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppContent(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppContent(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginContent(navController, modifier) }
        composable("menu") { MenuContent(navController, modifier) }
        composable("mascotas") { MascotasFormContent(navController, modifier) }
        composable("padrinos") { PadrinosFormContent(navController, modifier) }
        composable("apoyo") { ApoyosFormContent(navController, modifier) }
        composable("mascotaslst") { MascotaslstContent(navController, modifier) }
        composable("padrinoslst") { PadrinoslstContent(navController, modifier) }
        composable("apoyolst") { ApoyoslstContent(navController, modifier) }
    }
}

@Composable
fun LoginContent(navController: NavHostController, modifier: Modifier) {
    val context = LocalContext.current
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Inicio de Sesión",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color(0xFF2E8B57)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Usuario:")
        TextField(
            value = usuario,
            onValueChange = { usuario = it },
            placeholder = { Text("Ingresa tu usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Contraseña:")
        TextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            placeholder = { Text("Ingresa tu contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val usuarioValido = "admin"
                val contrasenaValida = "12345"

                if (usuario == usuarioValido && contrasena == contrasenaValida) {
                    Toast.makeText(context, "¡Bienvenido, $usuario!", Toast.LENGTH_SHORT).show()
                    navController.navigate("menu")
                } else {
                    Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4E9187),
                contentColor = Color.White
            )
        ) {
            Text("Iniciar sesión")
        }
    }
}

@Composable
fun MenuContent(navController: NavHostController, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Menú",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color(0xFF2E8B57)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("login") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E9187)),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
        ) { Text("Login") }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("mascotas") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E9187)),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
        ) { Text("Mascotas") }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("padrinos") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E9187)),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) { Text("Padrinos") }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("apoyo") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E9187)),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) { Text("Apoyos") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotasFormContent(navController: NavHostController, modifier: Modifier) {
    val context = LocalContext.current
    data class Opcion(val value: String, val label: String)
    val sexo_lista = listOf(
        Opcion("1", "Macho"),
        Opcion("2", "Hembra")
    )
    var sexoExpandido by remember { mutableStateOf(false) }
    var sexo by remember { mutableStateOf(sexo_lista[0]) }

    val mascota_lista = listOf(
        Opcion("1", "Perro"),
        Opcion("2", "Gato")
    )
    var mascotaExpandido by remember { mutableStateOf(false) }
    var mascota by remember { mutableStateOf(mascota_lista[0]) }

    var id_mascotas: String by remember { mutableStateOf(("")) }
    var nombre: String by remember { mutableStateOf(("")) }
    var raza: String by remember { mutableStateOf(("")) }
    var peso: String by remember { mutableStateOf(("")) }
    var condicion: String by remember { mutableStateOf(("")) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Formulario de Mascotas",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color(0xFF2E8B57)
        )
        Text(
            text = "Favor de llenar el siguiente formulario para registrar la mascota al refugio.",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 16.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Id Mascota",
            color = Color(0xFF4E9187))
        TextField(
            value = id_mascotas,
            onValueChange = { id_mascotas = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Nombre",
            color = Color(0xFF4E9187))
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            placeholder = { Text("Nombre de la mascota") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Tipo de Mascota",
            color = Color(0xFF4E9187))
        ExposedDropdownMenuBox(
            expanded = mascotaExpandido,
            onExpandedChange = { mascotaExpandido = !mascotaExpandido }
        ) {
            TextField(
                value = mascota.label,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccione tipo de mascota") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = mascotaExpandido)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            DropdownMenu(
                expanded = mascotaExpandido,
                onDismissRequest = { mascotaExpandido = false }
            ) {
                mascota_lista.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion.label) },
                        onClick = {
                            mascota = opcion
                            mascotaExpandido = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Sexo",
            color = Color(0xFF4E9187))
        ExposedDropdownMenuBox(
            expanded = sexoExpandido,
            onExpandedChange = { sexoExpandido = !sexoExpandido }
        ) {
            TextField(
                value = sexo.label,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccione sexo de mascota") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = sexoExpandido)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            DropdownMenu(
                expanded = sexoExpandido,
                onDismissRequest = { sexoExpandido = false }
            ) {
                sexo_lista.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion.label) },
                        onClick = {
                            sexo = opcion
                            sexoExpandido = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Raza",
            color = Color(0xFF4E9187))
        TextField(
            value = raza,
            onValueChange = { raza = it },
            placeholder = { Text("Raza de la masccota") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Peso",
            color = Color(0xFF4E9187)
            )
        TextField(
            value = peso,
            onValueChange = { peso = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Peso estimado de mascota") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Condiciones",
            color = Color(0xFF4E9187))
        TextField(
            value = condicion,
            onValueChange = { condicion = it },
            label = { Text("¿Que condiciones padece la mascota?") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            maxLines = 3,
            singleLine = false
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                Toast.makeText(context, "Id Macota: ${id_mascotas}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Tipo de Mascota: ${mascota.value}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Mascota: ${mascota.label}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Nombre: ${nombre}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Sexo de Mascota: ${sexo.label}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Raza: ${raza}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Peso: ${peso}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Condiciones: ${condicion}", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4E9187),
                contentColor = Color.White
            )
        ) {
            Text("Enviar")
        }
        Button(
            onClick = { navController.navigate("menu") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E9187)),
            modifier = Modifier.align(Alignment.End),
        ) { Text("Menú") }
        Button(
            onClick = { navController.navigate("mascotaslst") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E9187)),
            modifier = Modifier.align(Alignment.End),
        ) { Text("Lista") }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PadrinosFormContent(navController: NavHostController, modifier: Modifier) {
    val context = LocalContext.current

    data class Opcion(val value: String, val label: String)
    val sexo_padrino = listOf(
        Opcion("1", "Masculino"),
        Opcion("2", "Femenino"),
        Opcion("3", "Otro")
    )
    var sexopadrinoExpandido by remember { mutableStateOf(false) }
    var sexo_p by remember { mutableStateOf(sexo_padrino[0]) }
    var id_padrino: String by remember { mutableStateOf(("")) }
    var nombre: String by remember { mutableStateOf(("")) }
    var apellido1: String by remember { mutableStateOf(("")) }
    var telefono: String by remember { mutableStateOf(("")) }
    var correo: String by remember { mutableStateOf(("")) }

    Column(
        modifier = modifier
                .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Formulario de Padrinos",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color(0xFF2E8B57)
        )
        Text(
            text = "Favor de llenar el siguiente formulario para que se registre como Padrino.",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 16.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Id Padrino",
            color = Color(0xFF4E9187))
        TextField(
            value = id_padrino,
            onValueChange = { id_padrino = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Nombre",
            color = Color(0xFF4E9187))
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            placeholder = { Text("Su nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Primer Apellido",
            color = Color(0xFF4E9187))
        TextField(
            value = apellido1,
            onValueChange = { apellido1 = it },
            placeholder = { Text("Su primer apellido") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Sexo",
            color = Color(0xFF4E9187))
        ExposedDropdownMenuBox(
            expanded = sexopadrinoExpandido,
            onExpandedChange = { sexopadrinoExpandido = !sexopadrinoExpandido }
        ) {
            TextField(
                value = sexo_p.label,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccione su sexo") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = sexopadrinoExpandido)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            DropdownMenu(
                expanded = sexopadrinoExpandido,
                onDismissRequest = { sexopadrinoExpandido = false }
            ) {
                sexo_padrino.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion.label) },
                        onClick = {
                            sexo_p = opcion
                            sexopadrinoExpandido = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Telefono",
            color = Color(0xFF4E9187)
        )
        TextField(
            value = telefono,
            onValueChange = { telefono = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Su telefono personal") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Correo Electronico",
            color = Color(0xFF4E9187))
        TextField(
            value = correo,
            onValueChange = { correo = it },
            placeholder = { Text("Su correo personal") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                Toast.makeText(context, "Id Padrino: ${id_padrino}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Nombre: ${nombre}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Sexo: ${sexo_p.value}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Primer Apellido: ${apellido1}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Telefono: ${telefono}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Correo: ${correo}", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4E9187),
                contentColor = Color.White
            )
        ) {
            Text("Enviar")
        }
        Button(
            onClick = { navController.navigate("menu") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E9187)),
            modifier = Modifier.align(Alignment.End),
        ) { Text("Menú") }
        Button(
            onClick = { navController.navigate("padrinoslst") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E9187)),
            modifier = Modifier.align(Alignment.End),
        ) { Text("Lista") }



    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApoyosFormContent(navController: NavHostController, modifier: Modifier) {
    val context = LocalContext.current
    data class Opcion(val value: String, val label: String)
    val id_mascota = listOf(
        Opcion("1", "Max"),
        Opcion("2", "Bella"),
        Opcion("3", "Pelusa")
    )
    var mascotaExpandido by remember { mutableStateOf(false) }
    var mascota by remember { mutableStateOf(id_mascota[0]) }

    val id_padrino = listOf(
        Opcion("1", "Valeria Salazar"),
        Opcion("2", "Janeth Piña"),
        Opcion("3", "Mariajose Rodriguez")
    )
    var padrinoExpandido by remember { mutableStateOf(false) }
    var padrino by remember { mutableStateOf(id_padrino[0]) }

    var id_apoyo: String by remember { mutableStateOf(("")) }
    var monto: String by remember { mutableStateOf(("")) }
    var causa: String by remember { mutableStateOf(("")) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {

    Text(
            text = "Formulario de Apoyos",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color(0xFF2E8B57)
        )
        Text(
            text = "Favor de llenar el siguiente formulario para que se registre su apoyo.",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 16.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Id Apoyo",
            color = Color(0xFF4E9187))
        TextField(
            value = id_apoyo,
            onValueChange = { id_apoyo = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Mascotas",
            color = Color(0xFF4E9187))
        ExposedDropdownMenuBox(
            expanded = mascotaExpandido,
            onExpandedChange = { mascotaExpandido = !mascotaExpandido }
        ) {
            TextField(
                value = mascota.label,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccione la mascota a apoyar") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = mascotaExpandido)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            DropdownMenu(
                expanded = mascotaExpandido,
                onDismissRequest = { mascotaExpandido = false }
            ) {
                id_mascota.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion.label) },
                        onClick = {
                            mascota = opcion
                            mascotaExpandido = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Padrinos",
            color = Color(0xFF4E9187))
        ExposedDropdownMenuBox(
            expanded = padrinoExpandido,
            onExpandedChange = { padrinoExpandido = !padrinoExpandido }
        ) {
            TextField(
                value = padrino.label,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccione el padrino") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = padrinoExpandido)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            DropdownMenu(
                expanded = padrinoExpandido,
                onDismissRequest = { padrinoExpandido = false }
            ) {
                id_padrino.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion.label) },
                        onClick = {
                            padrino = opcion
                            padrinoExpandido = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Monto",
            color = Color(0xFF4E9187)
        )
        TextField(
            value = monto,
            onValueChange = { monto = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Monto a donar") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Causa",
            color = Color(0xFF4E9187))
        TextField(
            value = causa,
            onValueChange = { causa = it },
            label = { Text("¿Por que se ayuda?") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            maxLines = 3,
            singleLine = false
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                Toast.makeText(context, "Id Mascota: ${mascota.value}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Mascota: ${mascota.label}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Id Padrino: ${padrino.value}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Padrino: ${padrino.label}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Id Apoyo: ${id_apoyo}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Monto: ${monto}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Causa: ${causa}", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4E9187),
                contentColor = Color.White
            )
        ) {
            Text("Enviar")
        }
        Button(
            onClick = { navController.navigate("menu") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E9187)),
            modifier = Modifier.align(Alignment.End),
        ) { Text("Menú") }
        Button(
            onClick = { navController.navigate("apoyolst") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E9187)),
            modifier = Modifier.align(Alignment.End),
        ) { Text("Lista") }

    }

}

@Composable
fun ApoyoslstContent(navController: NavHostController, modifier: Modifier) {
    val scrollState = rememberScrollState()
    data class Apoyos(val id: Int, val mascotas: String,val padrinos:String, val monto: Double, val causa: String)
    val apoyo = remember {
        mutableStateListOf(
            Apoyos(1,"Bella","Janeth Piña", 545.50,  "Para que Bella tenga una vida más saludable"),
            Apoyos(2, "Pelusa", "Mariajose Rodriguez", 1200.00, "Para que Pelusa tenga los tratamientos necesrios para vivir" ),
            Apoyos(3,"Max", "Valeria Salazar", 2450.00,  "Darle una mejor vida a Max" )
        )
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .horizontalScroll(scrollState)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = {
                navController.navigate("apoyo")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4E9187),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Apoyos",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                apoyo.add(Apoyos(1,"Garfield","Alejandro Vega",6700.00,"Para que Garfiled pueda moverse sin problemas"))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF914e58),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Agregar Apoyo",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text("Id", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)
            Text("Mascota", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Padrino", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Monto", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Causa", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Eliminar", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
        }
        Divider()
        apoyo.forEachIndexed { index, objeto ->
            val bgColor = if (index % 2 == 0) Color(0xFFdaebe8) else Color.White

            Row (
                modifier = Modifier
                    .background(bgColor),

            ) {
                Text("${objeto.id} ", modifier = Modifier
                    .width(100.dp), Color.Gray
                )
                Text(objeto.mascotas, modifier = Modifier
                    .width(150.dp), Color.Gray
                )
                Text(objeto.padrinos, modifier = Modifier
                    .width(150.dp), Color.Gray
                )
                Text("$${objeto.monto} ", modifier = Modifier
                    .width(100.dp), Color.Gray
                )
                Text(objeto.causa, modifier = Modifier
                    .width(150.dp), Color.Gray
                )
                Button(onClick = {
                    apoyo.removeAt(index)
                }) {
                    Text("Eliminar")
                }
            }
        }


    }
}

@Composable
fun PadrinoslstContent(navController: NavHostController, modifier: Modifier){
    val scrollState = rememberScrollState()
    data class Padrinos(val id: Int, val nombre: String,val apellido:String, val sexo: String, val telefono: String,val correo: String)
    val padrinos = remember {
        mutableStateListOf(
            Padrinos(1,"Valeria","Salazar", "Femenino", "8781034180",  "val26@gmail.com"),
            Padrinos(2, "Janeth", "Piña", "Femenino", "8781541493" ,"janeth13@gmail.com" ),
            Padrinos(3,"Mariajose", "Rodriguez", "Femenino", "8781151530" , "majordz@gmail.com" )
        )
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .horizontalScroll(scrollState)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = {
                navController.navigate("padrinos")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4E9187),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Padrinos",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                padrinos.add(Padrinos(1,"Alejandro","Vega","Masculino","8781023940","aleVe11@gmail.com"))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF914e58),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Agregar Padrino",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text("Id", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)
            Text("Nombre", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Apellido", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Sexo", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Teléfono", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Correo", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Eliminar", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
        }
        Divider()
        padrinos.forEachIndexed { index, objeto ->
            val bgColor = if (index % 2 == 0) Color(0xFFdaebe8) else Color.White

            Row (
                modifier = Modifier
                    .background(bgColor)
            ) {
                Text("${objeto.id} ", modifier = Modifier
                    .width(100.dp), Color.Gray
                )
                Text(objeto.nombre, modifier = Modifier
                    .width(150.dp), Color.Gray
                )
                Text(objeto.apellido, modifier = Modifier
                    .width(150.dp), Color.Gray
                )
                Text(objeto.sexo, modifier = Modifier
                    .width(150.dp), Color.Gray
                )
                Text(objeto.telefono, modifier = Modifier
                    .width(150.dp), Color.Gray
                )
                Text(objeto.correo, modifier = Modifier
                    .width(150.dp), Color.Gray
                )
                Button(onClick = {
                    padrinos.removeAt(index)
                }) {
                    Text("Eliminar")
                }
            }
        }


    }
}




@Composable
fun MascotaslstContent(navController: NavHostController, modifier: Modifier) {
    val scrollState = rememberScrollState()
    data class Mascotas(val id: Int, val nombre: String,val tipo:String, val sexo: String, val raza: String,val peso: Double, val condiciones:String)
    val mascotas = remember {
        mutableStateListOf(
            Mascotas(1,"Max","Perro", "Macho", "Husky", 18.5, "Problemas de articulacion en la cadera"),
            Mascotas(2, "Bella", "Gato", "Hembra", "Siamese", 2.5,"Enfermedades en la piel" ),
            Mascotas(3,"Pelusa", "Perro", "Hembra", "Chihuahua", 2.7, "Problemas cardiacos" )
        )
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .horizontalScroll(scrollState)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = {
                navController.navigate("mascotas")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4E9187),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Mascotas",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                mascotas.add(Mascotas(1,"Garfield","Gato","Macho","Persian",5.3,"Su patas traseras estan discapacitadas"))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF914e58),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Agregar Mascota",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text("Id", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)
            Text("Nombre", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Tipo", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Sexo", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Raza", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Peso", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Condiciones", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Eliminar", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
        }
        Divider()
        mascotas.forEachIndexed { index, objeto ->
            val bgColor = if (index % 2 == 0) Color(0xFFdaebe8) else Color.White

            Row (
                modifier = Modifier
                    .background(bgColor)
            ) {
                Text("${objeto.id} ", modifier = Modifier
                    .width(100.dp), Color.Gray
                )
                Text(objeto.nombre, modifier = Modifier
                    .width(150.dp), Color.Gray
                )
                Text(objeto.tipo, modifier = Modifier
                    .width(150.dp), Color.Gray
                )
                Text(objeto.sexo, modifier = Modifier
                    .width(150.dp), Color.Gray
                )
                Text(objeto.raza, modifier = Modifier
                    .width(150.dp), Color.Gray
                )
                Text("${objeto.peso} kg", modifier = Modifier
                    .width(100.dp), Color.Gray
                )
                Text(objeto.condiciones, modifier = Modifier
                    .width(150.dp), Color.Gray
                )
                Button(onClick = {
                 mascotas.removeAt(index)
                }) {
                    Text("Eliminar")
                }
            }
        }


}
}


@Preview(showBackground = true)
@Composable
fun AppContentPreview() {
    Proyecto_1Theme {
        AppContent()
    }
}
