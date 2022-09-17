package com.hometech.flowdemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hometech.flowdemo.ui.theme.FlowDemoTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Returns a scope that's cancelled when F is removed from composition
            val coroutineScope = rememberCoroutineScope()
            var users by remember {
                mutableStateOf("")
            }
            FlowDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(modifier = Modifier, onClick = {
                            coroutineScope.launch(Dispatchers.Main) {
                                getUserNames().forEach {
                                    Log.d("UI", "onCreate: $it")
                                    users = users.plus(it).plus("\n")
                                }
                            }
                        }) {
                            Text(text = "Fire Coroutine", color = Color.White)
                        }
                        Text(text = users, color = Color.DarkGray)

                    }

                }
            }
        }
    }
}

/**
 * FLOW
 * Flow is used for transporting stream data (Continuous data) asynchronously. It is work with coroutines and
 * There are two types of flow in kotlin
 * 1) Hot Flow(Channel/SharedFlow) : Continuously produce data even if there is no consumer. If consumer join hot flow in middle of the emission it will receive latest emission and lose previous emission. Can't think of any use case in android where we need that. If hot flow is running and there is no consumer than there would resource wastage.
 * 2) Cold Flow(StateFlow) : In most cases in android we use cold flow. If cold flow is running and there is no consumer then it will not produce any emission until there is a consumer. Cold floe provide emission from start even consumer join in middle.
 *
 * Flow have three main components:
 * 1) Producer: That produce the emissions for each consumer.
 * 2) (Optional) Intermediaries/Operators : It is provide opportunity to modify emission values before consuming them. Example: onEach, onComplete, map, buffer, etc.
 * 3) Consumer : That collect emitted values from the producer.
 *
 */

/**
 * EXAMPLE why we need flow with suspend function. Here in this example we are making api calls for single user id and creating list of users so we can access them in our UI.
 */

//This function returns a list of users. It will take 5 seconds to get list of users.
suspend fun getUserNames(): List<String> {
    return listOf(getUser(1), getUser(2), getUser(3), getUser(4), getUser(5))
}

// Assume that here we are making API call for getting that user details
suspend fun getUser(userId: Int): String {
    delay(1000)
    return "User: $userId"
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlowDemoTheme {
        Greeting("Android")
    }
}