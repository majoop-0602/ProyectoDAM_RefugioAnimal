package com.example.proyecto_1

import com.example.proyecto_1.R
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import java.math.BigDecimal
import java.math.BigInteger
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.input.pointer.pointerInput

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




fun setSessionValue(context: Context, key: String, value: String) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    val editor = sharedPrefs.edit()
    editor.putString(key, value)
    editor.apply()
    // setSessionValue(context, "sesionIniciada", "yes")
}

fun getSessionValue(context: Context, key: String, defaultValue: String): String? {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    return sharedPrefs.getString(key, defaultValue)
    // val sesionIniciada : String = getSessionValue(context, "sesionIniciada", "no")
}
@Parcelize
data class ModeloPadrino(
    val idPadrino: Int,
    val nombrePadrino: String,
    val sexo: String,
    val telefono: String,
    val correoElectronico: String
) : Parcelable

@Parcelize
data class ModeloMascota(
    val idMascota: Int,
    val nombre: String,
    val tipo_mascota: String,
    val sexo: String,
    val raza: String?,
    val peso: Double,
    val condiciones: String?
) : Parcelable

data class ModeloApoyo(
    val idApoyo: Int,
    val idMascota: Int,
    val nombreMascota: String,
    val idPadrino: Int,
    val nombrePadrino: String,
    val monto: Double,
    val causa: String
)

data class OpcionCategoria(val value: String, val label: String)

@Parcelize
data class ModeloUsuario(
    val id: Int,
    val usuario: String,
    val contrasena: String
) : Parcelable

interface ApiService {
    @POST("refugio-animales-a.php?iniciarSesion=1")
    @FormUrlEncoded
    suspend fun iniciarSesion(
        @Field("usuario") usuario: String,
        @Field("contrasena") contrasena: String

    ): Response<String>
    @POST("refugio-animales-a.php?padrinos=1")
    @FormUrlEncoded
    suspend fun padrinos(@Field("dummy") dummy: Int = 1): List<ModeloPadrino>
    /*@GET("rodriguez-ballesteros.php?padrinos=1")
    suspend fun padrinos(): List<ModeloPadrino> */

    @POST("refugio-animales-a.php?agregarPadrinos=1")
    @FormUrlEncoded
    suspend fun agregarPadrinos(
        @Field("nombrePadrino") nombrePadrino: String,
        @Field("sexo") sexo: String,
        @Field("telefono") telefono: String,
        @Field("correoElectronico") correoElectronico: String,
    ): Response<String>

    @POST("refugio-animales-a.php?modificarPadrinos=1")
    @FormUrlEncoded
    suspend fun modificarPadrinos(
        @Field("idPadrino") idPadrino: Int,
        @Field("nombrePadrino") nombrePadrino: String,
        @Field("sexo") sexo: String,
        @Field("telefono") telefono: String,
        @Field("correoElectronico") correoElectronico: String,
    ): Response<String>

    @POST("refugio-animales-a.php?eliminarPadrino=1")
    @FormUrlEncoded
    suspend fun eliminarPadrino(
        @Field("idPadrino") idPadrino: Int
    ): Response<String>


    @POST("refugio-animales-a.php?agregarMascota=1")
    @FormUrlEncoded
    suspend fun agregarMascota(
        @Field("nombre") nombre: String,
        @Field("tipo_mascota") tipoMascota: String,
        @Field("sexo") sexo: String,
        @Field("raza") raza: String?,
        @Field("peso") peso: Double,
        @Field("condiciones") condiciones: String?,
    ): Response<String>

    @POST("refugio-animales-a.php?modificarMascota=1")
    @FormUrlEncoded
    suspend fun modificarMascota(
        @Field("idMascota") idMascota: Int,
        @Field("nombre") nombre: String,
        @Field("tipo_mascota") tipoMascota: String,
        @Field("sexo") sexo: String,
        @Field("raza") raza: String?,
        @Field("peso") peso: Double,
        @Field("condiciones") condiciones: String?,
    ): Response<String>

    /*@GET("salazar-valenzuela.php?mascotas=1")
    suspend fun mascotas(): List<ModeloMascota>*/
    @POST("refugio-animales-a.php?mascotas=1")
    @FormUrlEncoded
    suspend fun mascotas(
        @Field("dummy") dummy: Int = 1
    ): List<ModeloMascota>

    @POST("refugio-animales-a.php?eliminarMascota=1")
    @FormUrlEncoded
    suspend fun eliminarMascota(
        @Field("idMascota") idMascota: Int
    ): Response<String>


    @GET("refugio-animales-a.php?apoyo")
    suspend fun apoyo(): List<ModeloApoyo>

    @GET("refugio-animales-a.php?opcionMascota")
    suspend fun opcionMascota(): List<OpcionCategoria>

    @GET("refugio-animales-a.php?opcionPadrino")
    suspend fun opcionPadrino(): List<OpcionCategoria>

    @POST("refugio-animales-a.php?agregarApoyo")
    @FormUrlEncoded
    suspend fun agregarApoyo(
        @Field("mascota") mascota: Int,
        @Field("padrino") padrino: Int,
        @Field("monto") monto: Double,
        @Field("causa") causa: String
    ): Response<String>

    @POST("refugio-animales-a.php?modificarApoyo")
    @FormUrlEncoded
    suspend fun modificarApoyo(
        @Field("idApoyo") idApoyo: Int,
        @Field("mascota") mascota: Int,
        @Field("padrino") padrino: Int,
        @Field("monto") monto: Double,
        @Field("causa") causa: String
    ): Response<String>

    @POST("refugio-animales-a.php?eliminarApoyo")
    @FormUrlEncoded
    suspend fun eliminarApoyo(
        @Field("idApoyo") idApoyo: Int
    ): Response<String>



    //-------DEMOSTRACION EXTRA--------
    @POST("refugio-animales-a.php?usuarios=1")
    @FormUrlEncoded
    suspend fun usuarios(@Field("dummy") dummy: Int = 1): List<ModeloUsuario>

    @POST("refugio-animales-a.php?agregarUsuario=1")
    @FormUrlEncoded
    suspend fun agregarUsuario(
        @Field("usuario") usuario: String,
        @Field("contrasena") contrasena: String
    ): Response<String>

    @POST("refugio-animales-a.php?modificarUsuario=1")
    @FormUrlEncoded
    suspend fun modificarUsuario(
        @Field("id") id: Int,
        @Field("usuario") usuario: String,
        @Field("contrasena") contrasena: String
    ): Response<String>

    @POST("refugio-animales-a.php?eliminarUsuario=1")
    @FormUrlEncoded
    suspend fun eliminarUsuario(
        @Field("id") id: Int
    ): Response<String>
}



val retrofit = Retrofit.Builder()
    .baseUrl("https://dfhash.com/temporal/practicasDAM/")
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val api = retrofit.create(ApiService::class.java)

@Composable
fun AppContent(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val sesionIniciada : String? = getSessionValue(context, "sesionIniciada", "no")
    var StartDestination: String = "login"
    if(sesionIniciada == "yes"){
        StartDestination = "menu"
    }

    NavHost(navController = navController, startDestination =  StartDestination ) {
        composable("login") { LoginContent(navController, modifier) }
        composable("menu") { MenuContent(navController, modifier) }
        composable("mascotas") { val mascota = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<ModeloMascota>("mascotaSeleccionada")

            MascotasFormContent(
                navController = navController,
                mascota = mascota
            )
         }
        composable("padrinos") {
            val padrino = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<ModeloPadrino>("padrinoSeleccionado")

            PadrinosFormContent(
                navController = navController,
                padrino = padrino
            )
        }
        composable("apoyo") {
            val apoyos = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<ModeloApoyo>("apoyo seleccionado")
            ApoyosFormContent(
                navController = navController,
                apoyos = apoyos
            )
        }
        composable("mascotaslst") { MascotaslstContent(navController, modifier) }
        composable("padrinoslst") { PadrinoslstContent(navController, modifier) }
        composable("apoyolst") { ApoyoslstContent(navController, modifier) }


        //------DEMOSTRACION EXTRA--------
        composable("usuariolst") { UsuariolstContent(navController, modifier) }

        composable("usuarios") {
            val usuario = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<ModeloUsuario>("usuarioSeleccionado")

            UsuarioFormContent(
                navController = navController,
                usuarioSeleccionado = usuario
            )
        }




    }
}


@Composable
fun LoginContent(navController: NavHostController, modifier: Modifier) {
    val context = LocalContext.current
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_animales),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAAFFFFFF))
        )
    }

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

              setSessionValue(context, "sesionIniciada", "yes")
               scope.launch {
                   try {

                       val respuesta : Response<String> = api.iniciarSesion(usuario, contrasena)
                      if (respuesta.body() == "correcto"){
                          Toast.makeText(context, "Inicio de sesion con exito", Toast.LENGTH_SHORT).show()
                           navController.navigate("menu")
                                 //setSessionValue(context, "sesionIniciada", "yes")
                       }
                     else {
                          Toast.makeText(context, "Inicio de sesion incorrecto", Toast.LENGTH_SHORT).show()
                      }
                   }
                  catch (e: Exception) {
                      Log.e("API", "Error al intentar iniciar sesion: ${e.message}")
                   }
               }
               // val usuarioValido = "admin"
               //val contrasenaValida = "12345"

             //  if (usuario == usuarioValido && contrasena == contrasenaValida) {
                  //  Toast.makeText(context, "¡Bienvenido, $usuario!", Toast.LENGTH_SHORT).show()

                   // navController.navigate("menu")

               // } else {
                //   Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                 //  navController.navigate("Error")
            //   }
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
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_animales),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAAFFFFFF))
        )
    }

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
            onClick = {   setSessionValue(context, "sesionIniciada", "no") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E9187)),
                   shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Icon(Icons.Default.Pets, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
           Text("Login") }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("mascotas") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E9187)),
            shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Icon(Icons.Default.Pets, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Mascotas") }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("padrinos") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E9187)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ){
            Icon(Icons.Default.Pets, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Padrinos") }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("apoyo") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E9187)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Icon(Icons.Default.Pets, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Apoyos") }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("usuarios") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E9187)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Icon(Icons.Default.Pets, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Usuarios") }


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotasFormContent(navController: NavHostController, mascota: ModeloMascota? = null, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    data class Opcion(val value: String, val label: String)

    val sexoLista = listOf(
        Opcion("Macho", "Macho"),
        Opcion("Hembra", "Hembra")
    )
    var sexoSeleccionado by remember { mutableStateOf(sexoLista.find { it.label == mascota?.sexo } ?: sexoLista[0]) }
    var sexoExpandido by remember { mutableStateOf(false) }

    val tipoLista = listOf(
        Opcion("Perro", "Perro"),
        Opcion("Gato", "Gato")
    )
    var tipoSeleccionado by remember { mutableStateOf(tipoLista.find { it.label == mascota?.tipo_mascota } ?: tipoLista[0]) }
    var tipoExpandido by remember { mutableStateOf(false) }

    var idMascota by remember { mutableStateOf(mascota?.idMascota?.toString() ?: "") }
    var nombre by remember { mutableStateOf(mascota?.nombre ?: "") }
    var raza by remember { mutableStateOf(mascota?.raza ?: "") }
    var peso by remember { mutableStateOf(mascota?.peso?.toString() ?: "") }
    var condiciones by remember { mutableStateOf(mascota?.condiciones ?: "") }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.fondo2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Fondo semitransparente que NO bloquea clics
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAAFFFFFF))
                .pointerInput(Unit) {} // deja pasar los clics
        )

        // Contenido del formulario
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
            Spacer(modifier = Modifier.height(16.dp))

            // Nombre
            Text("Nombre")
            TextField(value = nombre, onValueChange = { nombre = it }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(8.dp))

            // Tipo de Mascota - ComboBox
            ExposedDropdownMenuBox(
                expanded = tipoExpandido,
                onExpandedChange = { tipoExpandido = !tipoExpandido }
            ) {
                TextField(
                    value = tipoSeleccionado.label,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Mascota") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tipoExpandido) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor() // esto es clave
                )
                ExposedDropdownMenu(
                    expanded = tipoExpandido,
                    onDismissRequest = { tipoExpandido = false }
                ) {
                    tipoLista.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion.label) },
                            onClick = {
                                tipoSeleccionado = opcion
                                tipoExpandido = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sexo - ComboBox
            ExposedDropdownMenuBox(
                expanded = sexoExpandido,
                onExpandedChange = { sexoExpandido = !sexoExpandido }
            ) {
                TextField(
                    value = sexoSeleccionado.label,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Sexo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sexoExpandido) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor() // esto también es clave
                )
                ExposedDropdownMenu(
                    expanded = sexoExpandido,
                    onDismissRequest = { sexoExpandido = false }
                ) {
                    sexoLista.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion.label) },
                            onClick = {
                                sexoSeleccionado = opcion
                                sexoExpandido = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Raza
            Text("Raza")
            TextField(value = raza, onValueChange = { raza = it }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(8.dp))

            // Peso
            Text("Peso")
            TextField(
                value = peso,
                onValueChange = { peso = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Condiciones
            Text("Condiciones")
            TextField(value = condiciones, onValueChange = { condiciones = it }, modifier = Modifier.fillMaxWidth(), maxLines = 3)

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Enviar
            Button(
                onClick = {
                    scope.launch {
                        try {
                            val body: Response<String> = if (mascota == null) {
                                api.agregarMascota(
                                    nombre = nombre,
                                    tipoMascota = tipoSeleccionado.value,
                                    sexo = sexoSeleccionado.value,
                                    raza = raza.ifBlank { null },
                                    peso = peso.toDoubleOrNull() ?: 0.0,
                                    condiciones = condiciones.ifBlank { null }
                                )
                            } else {
                                api.modificarMascota(
                                    idMascota = idMascota.toInt(),
                                    nombre = nombre,
                                    tipoMascota = tipoSeleccionado.value,
                                    sexo = sexoSeleccionado.value,
                                    raza = raza.ifBlank { null },
                                    peso = peso.toDoubleOrNull() ?: 0.0,
                                    condiciones = condiciones.ifBlank { null }
                                )
                            }

                            val id = body.body()?.trim()?.toIntOrNull()
                            if (id != null && id > 0) {
                                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                navController.navigate("mascotaslst")
                            } else {
                                Toast.makeText(context, "Error al guardar mascota", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Log.e("API", "Error al agregar/modificar mascota: ${e.message}")
                        }
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) { Text("Enviar") }

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { navController.navigate("menu") }, modifier = Modifier.align(Alignment.End)) { Text("Menú") }
            Button(onClick = { navController.navigate("mascotaslst") }, modifier = Modifier.align(Alignment.End)) { Text("Lista") }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PadrinosFormContent( navController: NavHostController, padrino: ModeloPadrino?, modifier: Modifier = Modifier  ) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    data class Opcion(val value: String, val label: String)
    val sexo_padrino = listOf(
        Opcion("1", "Masculino"),
        Opcion("2", "Femenino"),
        Opcion("3", "Otro")
    )

    // Variables del formulario, usando padrino si es edición
    var id_padrino by remember { mutableStateOf(padrino?.idPadrino?.toString() ?: "") }
    var nombre by remember { mutableStateOf(padrino?.nombrePadrino ?: "") }
    var telefono by remember { mutableStateOf(padrino?.telefono ?: "") }
    var correo by remember { mutableStateOf(padrino?.correoElectronico ?: "") }
    var sexoSeleccionado by remember {
        mutableStateOf(sexo_padrino.find { it.label == padrino?.sexo } ?: sexo_padrino[0])
    }
    var sexopadrinoExpandido by remember { mutableStateOf(false) }
    /*data class Opcion(val value: String, val label: String)
    val sexo_padrino = listOf(
        Opcion("1", "Masculino"),
        Opcion("2", "Femenino"),
        Opcion("3", "Otro")
    )
    var sexopadrinoExpandido by remember { mutableStateOf(false) }
    var sexo_p by remember { mutableStateOf(sexo_padrino[0]) }

    var id_padrino: String by remember { mutableStateOf(("")) }
    var nombre: String by remember { mutableStateOf(("")) }

    var telefono: String by remember { mutableStateOf(("")) }
    var correo: String by remember { mutableStateOf(("")) }

    val padrinos = remember {
        mutableStateListOf<ModeloPadrino>(

        )
    } */

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAAFFFFFF))
        )
    }

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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Nombre",
            color = Color(0xFF4E9187))
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            placeholder = { Text("Su nombre y primer apellido") },
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
                value = sexoSeleccionado.label,
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
                            sexoSeleccionado = opcion
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
                scope.launch {
                    try {
                        var respuesta: Response<String> = if (padrino == null) {
                            api.agregarPadrinos(nombrePadrino = nombre, sexo = sexoSeleccionado.label, telefono = telefono, correoElectronico = correo)
                        }
                        else {
                            api.modificarPadrinos(idPadrino = id_padrino.toInt(), nombrePadrino = nombre, sexo = sexoSeleccionado.label, telefono = telefono,
                                correoElectronico = correo
                            )
                        }
                                val body = respuesta.body()?.trim()?.replace("\n", "")?.replace("\r", "")
                                val id = body?.toIntOrNull()

                                if (id != null && id > 0) {

                                    //padrinos.add(ModeloPadrino(idPadrino = id, nombrePadrino = nombre, sexo = sexoSeleccionado.label, telefono = telefono, correoElectronico = correo))
                                    Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                    navController.navigate("padrinoslst")

                                } else {
                                    Toast.makeText(context, "Error al guardar padrino", Toast.LENGTH_SHORT).show()
                                }
                        id_padrino = ""
                        nombre = ""
                        telefono = ""
                        correo = ""
                    } catch (e: Exception) {
                        Log.e("API", "Error al intentar agregar padrino: ${e.message}")
                    }
                }
            }
        ,
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
fun ApoyosFormContent(navController: NavHostController, apoyos: ModeloApoyo?,modifier: Modifier = Modifier)  {
    val context = LocalContext.current

    val mascotaOpcion = remember {
        mutableStateListOf(OpcionCategoria("", "Seleccione una mascota"))

    }
    var mascotaExpandido by remember { mutableStateOf(false) }
    var mascota by remember { mutableStateOf(mascotaOpcion[0]) }

    val padrinoOpcion = remember {
        mutableStateListOf(OpcionCategoria("", "Seleccione un padrino"))

    }
    var padrinoExpandido by remember { mutableStateOf(false) }
    var padrino by remember { mutableStateOf(padrinoOpcion[0]) }

    var id_apoyo by remember { mutableStateOf(apoyos?.idApoyo?.toString() ?: "") }
    var monto by remember { mutableStateOf(apoyos?.monto?.toString() ?: "") }
    var causa by remember { mutableStateOf(apoyos?.causa ?: "") }

    LaunchedEffect(key1 = Unit) {

        val respuestaMascotas = api.opcionMascota()

        mascotaOpcion.clear()
        mascotaOpcion.addAll(
            respuestaMascotas.map {
                OpcionCategoria(
                    value = it.value,   // id mascota
                    label = it.label    // nombre mascota
                )
            }
        )


        val respuestaPadrinos = api.opcionPadrino()

        padrinoOpcion.clear()
        padrinoOpcion.addAll(
            respuestaPadrinos.map {
                OpcionCategoria(
                    value = it.value,   // id padrino
                    label = it.label    // nombre padrino
                )
            }
        )


        if (apoyos != null) {


            id_apoyo = apoyos.idApoyo.toString()

            // Rellenar monto
            monto = apoyos.monto.toString()


            causa = apoyos.causa


            mascota = mascotaOpcion.find {
                it.value == apoyos.idMascota.toString()
            } ?: mascotaOpcion[0]


            padrino = padrinoOpcion.find {
                it.value == apoyos.idPadrino.toString()
            } ?: padrinoOpcion[0]
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAAFFFFFF))
        )
    }

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
        ) {TextField(
            value = mascota?.label ?: "Seleccione la mascota",
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
                mascotaOpcion.forEach { opcionMascota ->
                    DropdownMenuItem(
                        text = { Text(opcionMascota.label) },
                        onClick = {
                            mascota = opcionMascota
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
                value = padrino?.label ?: "Seleccione el padrino",
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
                padrinoOpcion.forEach { opcionPadrino ->
                    DropdownMenuItem(
                        text = { Text(opcionPadrino.label) },
                        onClick = {
                            padrino = opcionPadrino
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
        val scope = rememberCoroutineScope()

        Button(
            onClick = {
                scope.launch {
                    try {
                        val respuesta = if (id_apoyo.isEmpty()) {
                            api.agregarApoyo(
                                mascota.value.toInt(),
                                padrino.value.toInt(),
                                monto.toDouble(),
                                causa
                            )
                        } else {
                            api.modificarApoyo(
                                id_apoyo.toInt(),
                                mascota.value.toInt(),
                                padrino.value.toInt(),
                                monto.toDouble(),
                                causa
                            )
                        }

                        if (respuesta.body() != null) {
                            Toast.makeText(context, "Guardado correctamente", Toast.LENGTH_SHORT).show()
                            navController.navigate("apoyolst")
                        } else {
                            Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e("API", "Error guardando: ${e.message}")
                    }
                }
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
    val apoyo = remember {
        mutableStateListOf<ModeloApoyo>(
            //Apoyos(1,"Bella","Janeth Piña", 545.50,  "Para que Bella tenga una vida más saludable"),
            //Apoyos(2, "Pelusa", "Mariajose Rodriguez", 1200.00, "Para que Pelusa tenga los tratamientos necesrios para vivir" ),
            //Apoyos(3,"Max", "Valeria Salazar", 2450.00,  "Darle una mejor vida a Max" )
        )
    }

    val mascotaOpcion = remember {
        mutableStateListOf<OpcionCategoria>(
            OpcionCategoria("", "Selecciona una opcion")
        )
    }
    var mascotaExpandido by remember { mutableStateOf(false) }
    var mascota by remember { mutableStateOf(mascotaOpcion[0]) }

    val padrinoOpcion = remember {
        mutableStateListOf<OpcionCategoria>(
            OpcionCategoria("", "Selecciona una opcion")
        )
    }
    var padrinoExpandido by remember { mutableStateOf(false) }
    var padrino by remember { mutableStateOf(padrinoOpcion[0]) }

    var index2: String by remember{mutableStateOf(value = "")}
    var idApoyo: String by remember{mutableStateOf(value = "")}
    var monto: String by remember{mutableStateOf(value = "")}
    var causa: String by remember{mutableStateOf(value = "")}

    LaunchedEffect(Unit) {
        try {
            val respuesta = api.apoyo()
            apoyo.clear()
            apoyo.addAll(respuesta)
            val respuesta2 = api.opcionMascota()
            mascotaOpcion.clear()
            mascotaOpcion.addAll(respuesta2)
            val respuesta3 = api.opcionPadrino()
            padrinoOpcion.clear()
            padrinoOpcion.addAll(respuesta3)

        }
        catch (e: Exception) {
            Log.e("API", "Error al cargar apoyos: ${e.message}")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAAFFFFFF))
        )
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .horizontalScroll(scrollState)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    )


    {
        Text(
            text = "Tabla de Apoyos",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color(0xFF2E8B57)
        )
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
                //apoyo.add(Apoyos(1,"Garfield","Emannuel Cazares",6700.00,"Para que Garfiled pueda moverse sin problemas"))
                scope.launch {
                    try {
                        var respuesta : Response<String>

                        if (idApoyo == ""){
                            respuesta = api.agregarApoyo(
                                mascota.value.toInt(),
                                padrino.value.toInt(),
                                monto.toDouble(),
                                causa)

                            if (respuesta.body() != "0"){
                                Toast.makeText(context, "Apoyo agregado", Toast.LENGTH_SHORT).show()
                                apoyo.add( ModeloApoyo(respuesta.body()?.toInt() ?: 0, mascota.value.toInt(), mascota.label, padrino.value.toInt(), padrino.label,monto.toDouble(), causa))
                            }
                            else{
                                Toast.makeText(context, "Agregado de apoyo incorrecto", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            respuesta = api.modificarApoyo(idApoyo.toInt(), mascota.value.toInt(), padrino.value.toInt(), monto.toDouble(), causa)

                            if (respuesta.body() == "correcto"){
                                Toast.makeText(context, "Apoyo modificado", Toast.LENGTH_SHORT).show()
                                apoyo[index2.toInt()] = ModeloApoyo(idApoyo.toInt(), mascota.value.toInt(), mascota.label, padrino.value.toInt(), padrino.label, monto.toDouble(), causa)
                            }
                            else{
                                Toast.makeText(context, "Modificacion de apoyo incorrecto", Toast.LENGTH_SHORT).show()
                            }
                        }

                        index2    = ""
                        idApoyo   = ""
                        causa     = ""
                        monto     = ""
                    }
                    catch (e: Exception) {
                        Log.e("API", "Error al intentar agregar apoyo: ${e.message}")
                    }
                }
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

        val ColId = 100.dp
        val ColMascota = 150.dp
        val ColPadrino = 150.dp
        val ColMonto = 120.dp
        val ColCausa = 200.dp
        val ColEliminar = 100.dp
        val ColModificar = 100.dp
        Row {
            Text("Id", modifier = Modifier.width(ColId), fontWeight = FontWeight.Bold)
            Text("Mascota", modifier = Modifier.width(ColMascota), fontWeight = FontWeight.Bold)
            Text("Padrino", modifier = Modifier.width(ColPadrino), fontWeight = FontWeight.Bold)
            Text("Monto", modifier = Modifier.width(ColMonto), fontWeight = FontWeight.Bold)
            Text("Causa", modifier = Modifier.width(ColCausa), fontWeight = FontWeight.Bold)
            Text("Eliminar", modifier = Modifier.width(ColEliminar), fontWeight = FontWeight.Bold)
            Text("Editar", modifier = Modifier.width(ColModificar), fontWeight = FontWeight.Bold)

        }
        Divider()
        apoyo.forEachIndexed { index, objeto ->
            val bgColor = if (index % 2 == 0) Color(0xFFdaebe8) else Color.White

            Row (
                modifier = Modifier
                    .background(bgColor),

                ) {
                Text("${objeto.idApoyo} ", modifier = Modifier
                    .width(ColId), Color.Gray
                )
                Text(objeto.idMascota.toString(), modifier = Modifier
                    .width(ColMascota), Color.Gray
                )
                Text(objeto.idPadrino.toString(), modifier = Modifier
                    .width(ColPadrino), Color.Gray
                )
                Text("$${objeto.monto} ", modifier = Modifier
                    .width(ColMonto), Color.Gray
                )
                Text(objeto.causa, modifier = Modifier
                    .width(ColCausa), Color.Gray
                )
                Button(onClick = {
                    val idApoyo: Int = apoyo[index].idApoyo
                    //apoyo.removeAt(index)
                    scope.launch {
                        try {

                            val respuesta : Response<String> = api.eliminarApoyo(idApoyo)
                            if (respuesta.body() == "correcto"){
                                Toast.makeText(context, "Apoyo eliminado", Toast.LENGTH_SHORT).show()
                                apoyo.removeAt(index)
                            }
                            else{
                                Toast.makeText(context, "Error al eliminar apoyo", Toast.LENGTH_SHORT).show()
                            }
                        }
                        catch (e: Exception) {
                            Log.e("API", "Error al intentar eliminar apoyo: ${e.message}")
                        }
                    }
                }) {
                    Text("Eliminar")
                }
                Button(onClick = {
                    //apoyo.removeAt(index)
                    index2    = index.toString()

                    idApoyo   = apoyo[index].idApoyo.toString()
                    monto     = apoyo[index].monto.toString()
                    causa     = apoyo[index].causa

                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "apoyo seleccionado",
                        apoyo[index]
                    )

                    navController.navigate("apoyo")
                }) {
                    Text("Editar")
                }
            }
        }


    }
}


@Composable
fun PadrinoslstContent(navController: NavHostController, modifier: Modifier){

    val padrinos = remember {
        mutableStateListOf<ModeloPadrino>(
            //Padrinos(1,"Valeria","Salazar", "Femenino", "8781034180",  "val26@gmail.com"),
            //Padrinos(2, "Janeth", "Piña", "Femenino", "8781541493" ,"janeth13@gmail.com" ),
            //Padrinos(3,"Mariajose", "Rodriguez", "Femenino", "8781151530" , "majordz@gmail.com" )
        )
    }



    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            val respuesta = api.padrinos()
            Log.e("API", "Respuesta: $respuesta")
            padrinos.clear()
            padrinos.addAll(respuesta)
        }
        catch (e: Exception) {
            Log.e("API", "Error al cargar padrinos: ${e.message}")
        }
    }


    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAAFFFFFF))
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
        Text(
            text = "Tabla de Padrinos",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color(0xFF2E8B57)
        )
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
               // padrinos.add(Padrinos(1,"Alejandro","Vega","Masculino","8781023940","aleVe11@gmail.com"))
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
        val ColId = 100.dp
        val ColNombre = 150.dp
        val ColSexo = 120.dp
        val ColTelefono = 150.dp
        val ColCorreo = 200.dp
        val ColEliminar = 100.dp
        val ColModificar = 100.dp

        Row {
            Text("Id", modifier = Modifier.width(ColId), fontWeight = FontWeight.Bold)
            Text("Nombre", modifier = Modifier.width(ColNombre), fontWeight = FontWeight.Bold)
            Text("Sexo", modifier = Modifier.width(ColSexo), fontWeight = FontWeight.Bold)
            Text("Teléfono", modifier = Modifier.width(ColTelefono), fontWeight = FontWeight.Bold)
            Text("Correo", modifier = Modifier.width(ColCorreo), fontWeight = FontWeight.Bold)
            Text("Eliminar", modifier = Modifier.width(ColEliminar), fontWeight = FontWeight.Bold)
            Text("Modificar", modifier = Modifier.width(ColModificar), fontWeight = FontWeight.Bold)
        }
        Divider()
        padrinos.forEachIndexed { index, objeto ->
            val bgColor = if (index % 2 == 0) Color(0xFFdaebe8) else Color.White

            Row (
                modifier = Modifier
                    .background(bgColor)
            ) {
                Text(objeto.idPadrino.toString(), modifier = Modifier
                    .width(ColId), Color.Gray
                )
                Text(objeto.nombrePadrino, modifier = Modifier
                    .width(ColNombre), Color.Gray
                )

                Text(objeto.sexo, modifier = Modifier
                    .width(ColSexo), Color.Gray
                )
                Text(objeto.telefono, modifier = Modifier
                    .width(ColTelefono), Color.Gray
                )
                Text(objeto.correoElectronico, modifier = Modifier
                    .width(ColCorreo), Color.Gray
                )
                Button(onClick = {
                  val id: Int = padrinos[index].idPadrino
                    scope.launch {
                        try {

                            val respuesta : Response<String> = api.eliminarPadrino(id)
                            val body = respuesta.body()?.trim()
                            if (body == "correcto"){
                                Toast.makeText(context, "Padrino eliminado con exito", Toast.LENGTH_SHORT).show()
                                padrinos.removeAt(index)
                            }
                            else {
                                Toast.makeText(context, "Padrino no eliminado", Toast.LENGTH_SHORT).show()
                            }
                        }
                        catch (e: Exception) {
                            Log.e("API", "Error al intentar eliminar padrino: ${e.message}")
                        }
                    }

                }) {
                    Text("Eliminar")
                }
                Button(onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "padrinoSeleccionado",
                        padrinos[index]
                    )
                    navController.navigate("padrinos")


                }) {
                    Text("Editar")
                }
            }
        }


    }
}


@Composable
fun MascotaslstContent(navController: NavHostController, modifier: Modifier) {
    val mascotas = remember { mutableStateListOf<ModeloMascota>() }
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            val lista = api.mascotas()
            mascotas.clear()
            mascotas.addAll(lista)
        } catch (e: Exception) {
            Log.e("API", "Error al cargar mascotas: ${e.message}")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.fondo2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier.fillMaxSize().background(Color(0xAAFFFFFF)))

        // Contenido de la lista
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .horizontalScroll(scrollState)
        ) {
            Text(
                "Tabla de Mascotas",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color(0xFF2E8B57)
            )

            Button(onClick = { navController.navigate("mascotas") }, modifier = Modifier.fillMaxWidth()) {
                Text("Agregar Mascota")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text("Id", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                Text("Nombre", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
                Text("Tipo", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                Text("Sexo", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                Text("Raza", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                Text("Peso", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                Text("Condiciones", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)
                Text("Eliminar", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                Text("Editar", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold)
            }
            Divider()

            mascotas.forEachIndexed { index, mascota ->
                val bgColor = if (index % 2 == 0) Color(0xFFdaebe8) else Color.White
                Row(modifier = Modifier.background(bgColor)) {
                    Text("${mascota.idMascota}", modifier = Modifier.width(80.dp), color = Color.Gray)
                    Text(mascota.nombre, modifier = Modifier.width(100.dp), color = Color.Gray)
                    Text(mascota.tipo_mascota, modifier = Modifier.width(80.dp), color = Color.Gray)
                    Text(mascota.sexo, modifier = Modifier.width(80.dp), color = Color.Gray)
                    Text(mascota.raza ?: "-", modifier = Modifier.width(80.dp), color = Color.Gray)
                    Text("${mascota.peso}", modifier = Modifier.width(80.dp), color = Color.Gray)
                    Text(mascota.condiciones ?: "-", modifier = Modifier.width(150.dp), color = Color.Gray)

                    Button(onClick = {
                        scope.launch {
                            try {
                                val respuesta = api.eliminarMascota(mascota.idMascota)
                                if (respuesta.body()?.trim() == "correcto") {
                                    mascotas.removeAt(index)
                                    Toast.makeText(context, "Mascota eliminada", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Log.e("API", "Error al eliminar mascota: ${e.message}")
                            }
                        }
                    }) { Text("Eliminar") }

                    Button(onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("mascotaSeleccionada", mascota)
                        navController.navigate("mascotas")
                    }) { Text("Editar") }
                }
            }
        }
    }
}





//--------DEMOSTRACION EXTRA--------
@Composable
fun UsuariolstContent(navController: NavHostController, modifier: Modifier){

    val usuarios = remember {
        mutableStateListOf<ModeloUsuario>(
            //Padrinos(1,"Valeria","Salazar", "Femenino", "8781034180",  "val26@gmail.com"),
            //Padrinos(2, "Janeth", "Piña", "Femenino", "8781541493" ,"janeth13@gmail.com" ),
            //Padrinos(3,"Mariajose", "Rodriguez", "Femenino", "8781151530" , "majordz@gmail.com" )
        )
    }



    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            val respuesta = api.usuarios()
            Log.e("API", "Respuesta: $respuesta")
            usuarios.clear()
            usuarios.addAll(respuesta)
        }
        catch (e: Exception) {
            Log.e("API", "Error al cargar usuarios: ${e.message}")
        }
    }


    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAAFFFFFF))
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
        Text(
            text = "Tabla de Usuarios",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color(0xFF2E8B57)
        )
        Button(
            onClick = {
                navController.navigate("usuarios")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4E9187),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Usuarios",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // padrinos.add(Padrinos(1,"Alejandro","Vega","Masculino","8781023940","aleVe11@gmail.com"))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF914e58),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Agregar Usuario",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        val ColId = 100.dp
        val ColNombre = 150.dp

        val ColContrasena = 150.dp

        val ColEliminar = 100.dp
        val ColModificar = 100.dp

        Row {
            Text("Id", modifier = Modifier.width(ColId), fontWeight = FontWeight.Bold)
            Text("Usuario", modifier = Modifier.width(ColNombre), fontWeight = FontWeight.Bold)
            Text("Contrasena", modifier = Modifier.width(ColContrasena), fontWeight = FontWeight.Bold)
            Text("Eliminar", modifier = Modifier.width(ColEliminar), fontWeight = FontWeight.Bold)
            Text("Modificar", modifier = Modifier.width(ColModificar), fontWeight = FontWeight.Bold)
        }
        Divider()
        usuarios.forEachIndexed { index, objeto ->
            val bgColor = if (index % 2 == 0) Color(0xFFdaebe8) else Color.White

            Row (
                modifier = Modifier
                    .background(bgColor)
            ) {
                Text(objeto.id.toString(), modifier = Modifier
                    .width(ColId), Color.Gray
                )
                Text(objeto.usuario, modifier = Modifier
                    .width(ColNombre), Color.Gray
                )
                Text(objeto.contrasena, modifier = Modifier
                    .width(ColContrasena), Color.Gray
                )

                Button(onClick = {
                    val id: Int = usuarios[index].id
                    scope.launch {
                        try {

                            val respuesta : Response<String> = api.eliminarUsuario(id)
                            val body = respuesta.body()?.trim()
                            if (body == "correcto"){
                                Toast.makeText(context, "Usuario eliminado con exito", Toast.LENGTH_SHORT).show()
                                usuarios.removeAt(index)
                            }
                            else {
                                Toast.makeText(context, "Usuario no eliminado", Toast.LENGTH_SHORT).show()
                            }
                        }
                        catch (e: Exception) {
                            Log.e("API", "Error al intentar eliminar usuario: ${e.message}")
                        }
                    }

                }) {
                    Text("Eliminar")
                }
                Button(onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "usuarioSeleccionado",
                        usuarios[index]
                    )
                    navController.navigate("usuarios")


                }) {
                    Text("Editar")
                }
            }
        }


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioFormContent( navController: NavHostController, usuarioSeleccionado: ModeloUsuario?, modifier: Modifier = Modifier  ) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    // Variables del formulario, usando usuario si es edición
    var id by remember { mutableStateOf(usuarioSeleccionado?.id?.toString() ?: "") }
    var usuario by remember { mutableStateOf(usuarioSeleccionado?.usuario ?: "") }
    var contrasena by remember { mutableStateOf(usuarioSeleccionado?.contrasena ?: "") }




    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAAFFFFFF))
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Formulario de Usuarios",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color(0xFF2E8B57)
        )
        Text(
            text = "Favor de llenar el siguiente formulario para que se registrar un nuevo usuario.",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 16.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Id Usuario",
            color = Color(0xFF4E9187))
        TextField(
            value = id,
            onValueChange = { id = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Usuario",
            color = Color(0xFF4E9187))
        TextField(
            value = usuario,
            onValueChange = { usuario = it },
            placeholder = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Contraseña",
            color = Color(0xFF4E9187))
        TextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            placeholder = { Text("Contraseña para el usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                scope.launch {
                    try {
                        var respuesta: Response<String> = if (usuario == null) {
                            api.agregarUsuario(usuario = usuario, contrasena = contrasena)
                        }
                        else {
                            api.modificarUsuario(id = id.toInt(), usuario = usuario, contrasena = contrasena)

                        }
                        val body = respuesta.body()?.trim()?.replace("\n", "")?.replace("\r", "")
                        val id1 = body?.toIntOrNull()

                        if (id1 != null && id1 > 0) {

                            //padrinos.add(ModeloPadrino(idPadrino = id, nombrePadrino = nombre, sexo = sexoSeleccionado.label, telefono = telefono, correoElectronico = correo))
                            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                            navController.navigate("usuariolst")

                        } else {
                            Toast.makeText(context, "Error al guardar usuario", Toast.LENGTH_SHORT).show()
                        }
                        id = ""
                        usuario = ""
                        contrasena = ""
                    } catch (e: Exception) {
                        Log.e("API", "Error al intentar agregar usuario: ${e.message}")
                    }
                }
            }
            ,
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
            onClick = { navController.navigate("usuariolst") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E9187)),
            modifier = Modifier.align(Alignment.End),
        ) { Text("Lista") }



    }

}

@Preview(showBackground = true)
@Composable
fun AppContentPreview() {
    Proyecto_1Theme {
        AppContent()
    }
}
