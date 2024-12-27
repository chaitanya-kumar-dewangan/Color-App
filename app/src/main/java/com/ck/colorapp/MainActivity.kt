package com.ck.colorapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.ck.colorapp.data.ColorAppDB
import com.ck.colorapp.data.ColorEntity
import com.ck.colorapp.data.ColorRepo
import com.ck.colorapp.presentation.ViewModel
import com.ck.colorapp.ui.theme.ColorAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(applicationContext, ColorAppDB::class.java, "articles.db").build()
    }
    private val viewModel by viewModels<ViewModel> {
        object : ViewModelProvider.Factory {

            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return ViewModel(ColorRepo(db.colorDao())) as T
            }

        }
    }
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ColorAppTheme {

                val light = Color(android.graphics.Color.parseColor("#B6B9FF"))
                val white = Color(android.graphics.Color.parseColor("#FFFFFF"))
                val dark = Color(android.graphics.Color.parseColor("#5659A4"))
                val context = LocalContext.current

                val count = viewModel.count.collectAsState()
                val colors = viewModel.colors.collectAsState()
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar(title = {
                        Text("Color App ")
                    }, colors = TopAppBarDefaults.topAppBarColors(
                        containerColor =
                        dark, titleContentColor = white
                    ),
                        actions = {
                            Button(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = light),
                                onClick = {
                                    Toast.makeText(context, "Syncing", Toast.LENGTH_SHORT).show()
                                    viewModel.addListOfColorToFirebase()
                                }
                            ) {
                                Row(
                                    modifier = Modifier.width(30.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(count.value.toString(), fontSize = 14.sp)
                                    Icon(
                                        painter = painterResource(id = R.drawable.sync),
                                        modifier = Modifier.size(20.dp),
                                        tint = dark,
                                        contentDescription = null
                                    )
                                }
                            }
                        })
                }, floatingActionButton = {
                    Button(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = light),
                        onClick = {
                            viewModel.generateColor()
                        }) {

                        Text("Add Color", color = dark)
                        Spacer(modifier = Modifier.width(20.dp))
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            tint = dark,
                            contentDescription = ""
                        )
                    }
                }) { innerPadding ->
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(horizontal = 10.dp)
                    ) {
                        items(colors.value) { color ->
                            ColorCard(color)
                        }
                    }
                }
            }
        }
    }
    @Composable
    private fun ColorCard(color: ColorEntity) {
        val clr = Color(android.graphics.Color.parseColor(color.color))
        Column(
            modifier = Modifier
                .width(157.dp)
                .height(118.dp)
                .padding(6.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(clr)
                .padding(10.dp),

            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text(text = color.color, fontSize = 18.sp, color = Color.White)
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth(.6f)
                        .background(Color.White)
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {

                Text(text = "Created At ", fontSize = 12.sp, color = Color.White)
                Text(
                    text = formatDateFromLong(color.timeStamp),
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }
}
@Composable
fun Hello(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Button(onClick = {
        Toast.makeText(context, "hello clicked", Toast.LENGTH_SHORT).show()
    }) {

        Text("button ")
    }
}
fun formatDateFromLong(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}
