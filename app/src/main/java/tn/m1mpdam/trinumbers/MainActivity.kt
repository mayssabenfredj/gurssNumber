package tn.m1mpdam.trinumbers

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import tn.m1mpdam.trinumbers.data.Record
import tn.m1mpdam.trinumbers.data.RecordViewModel
import tn.m1mpdam.trinumbers.ui.theme.TriNumbersTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class MainActivity : ComponentActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TriNumbersTheme {
                App()
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "screen1"
    ) {
        composable("screen1") {
            Screen1 {
                navController.navigate("screen2")
            }
        }
        composable("screen2") {
            Screen2()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Screen1(onNavigate: () -> Unit) {
    val generatedNumbers = mutableListOf<Int>()
    val changedList = mutableListOf<Int>()
    val isCounting = false

    // App content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            // Title
            Text(
                text = "Sorting Game",
                style = TextStyle(
                    fontSize = 24.sp,
                    shadow = Shadow(
                        color = Color.Blue, blurRadius = 3f
                    )
                ),
                modifier = Modifier

                    .padding(bottom = 8.dp)
                    .padding(end = 30.dp)
            )


            OutlinedButton(
                onClick = onNavigate,
                modifier = Modifier

                    .height(26.dp)
            ) {
                Text(text = "Records")
            }
        }

        Text(
            text = "You need to sort those 5 numbers from smaller to bigger in less time.",
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.DarkGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        GenerateNumbers(generatedNumbers)
        Chronometer(generatedNumbers,changedList,isCounting)



    }
}

@Composable
fun Screen2() {
    val mRecordViewModel: RecordViewModel = viewModel()
    val listRecord = mRecordViewModel.readAllData.value

    // App content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        if (listRecord != null) {
            LazyColumn {

                items(listRecord.size) { listItem ->
                    ListItem(listRecord[listItem])
                }
            }
        } else {
                    Text(text = "There is no Data !!!")
                }
            }
        }


@Composable
fun ListItem(record: Record) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ){
            Text(text = record.recordID.toString(),
                color = Color.DarkGray
            )
            Text(text = record.date.toString(),color = Color.DarkGray)
            Text(text = record.time.toString(), color = Color.DarkGray)
        }
    }



@Composable
fun GenerateNumbers(generatedNumbers : MutableList<Int>){
    var randomNumber: Int
    for (i in 1..5){
        do {
            randomNumber = Random.nextInt(1, 1001)
        } while (randomNumber in generatedNumbers)
        // Add the number to the list of generated numbers
        generatedNumbers.add(randomNumber)
    }




}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun HorizontalReorderList(generatedNumbers: MutableList<Int>,changedList: MutableList<Int> ) {
    val data = remember { mutableStateOf(generatedNumbers) }
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        data.value = data.value.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    })
    changedList.clear()
    changedList.addAll(data.value)
    // Generate a random number different from the ones already generated

    LazyRow(
        state = state.listState,
        modifier = Modifier
            .reorderable(state)
            .detectReorderAfterLongPress(state)
    ) {
        items(data.value, { it }) { item ->
            ReorderableItem(state, key = item) { isDragging ->
                val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation.value)
                        .background(color = Color.Transparent)
                        .padding(2.dp)
                ) {
                    Text(
                        text = "$item",
                        modifier = Modifier
                            .padding(8.dp)
                            .background(
                                color = Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)

                    )
                }
            }
        }

    }

}





@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Chronometer(generatedNumbers: MutableList<Int>, changedList: MutableList<Int>, isCounter: Boolean) {
    val mRecordViewModel: RecordViewModel = viewModel()
    var elapsedTime by remember { mutableIntStateOf(0) }
    var isCounting by remember { mutableStateOf(isCounter) }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(isCounting) {
        while (isCounting) {
            delay(1000)
            elapsedTime++
        }
    }


    fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }


    fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertData(times:Int){

        var recordDate = getCurrentDate()
        var recordTime = formatTime(times)
        var record = Record(0,recordDate,recordTime)
        mRecordViewModel.addRecord(record)
        Toast.makeText(context,"Record Successfully Added",Toast.LENGTH_LONG).show()

    }

    fun validateTriNumbers(generatedNumbers: MutableList<Int>,changedList: MutableList<Int>):Boolean{
        val sortedNumbers = generatedNumbers.sorted()
        return sortedNumbers == changedList
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {

        Text(
            text = formatTime(elapsedTime),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))


        if (!isCounting) {
            OutlinedButton(
                onClick = {
                    isCounting = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Start Timer")
            }
        } else {
            HorizontalReorderList(generatedNumbers, changedList)
            OutlinedButton(
                onClick = {
                    if (validateTriNumbers(generatedNumbers,changedList)){
                        val currentTime = elapsedTime
                        isCounting = false
                        elapsedTime = 0
                        keyboardController?.hide()
                        Toast.makeText(context,"Congratulations!, you made it.",Toast.LENGTH_SHORT).show()
                        insertData(currentTime)
                    } else {
                        Toast.makeText(context,"Wrong!, Try again.",Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Validate")
            }
        }
    }
}




@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun AppPreview() {
    App()
}
