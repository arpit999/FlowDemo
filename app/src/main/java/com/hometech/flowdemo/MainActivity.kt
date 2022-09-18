package com.hometech.flowdemo

import android.content.Context
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
import androidx.core.content.ContextCompat.getSystemService
import com.hometech.flowdemo.ui.theme.FlowDemoTheme
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.coroutines.coroutineContext

class MainActivity : ComponentActivity() {
    //Channel object declaration
    private val channel = Channel<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val users by remember {
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
                            producer()
                            consumer()
                        }) {
                            Text(text = "Fire Channel", color = Color.White)
                        }
                        Text(text = users, color = Color.DarkGray)
                    }

                }
            }
        }
    }

    //Send data through the channel
    private fun producer() {
        CoroutineScope(Dispatchers.Main).launch {
            channel.send(1)
            channel.send(2)
        }

    }

    //Receive data through the channel
    private fun consumer() {
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("TAG", "consumer: ${channel.receive()}")
            Log.d("TAG", "consumer: ${channel.receive()}")
        }
    }


}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlowDemoTheme {

    }
}