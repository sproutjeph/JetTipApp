package com.example.jettipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettipapp.components.InputField
import com.example.jettipapp.components.TopHeader
import com.example.jettipapp.ui.theme.JetTipAppTheme
import com.example.jettipapp.utils.calculateTotalTip
import com.example.jettipapp.widgets.RoundedIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                BillForm()
            }
        }
    }
}


@Composable
fun MyApp(content:@Composable ()-> Unit){
    JetTipAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
//            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
//            content()
            MainContent()

        }

    }
    
}





@Preview
@Composable
fun MainContent(){
    Column {
        BillForm{billAmt->
            Log.d("AMT", "MainContent: $billAmt")

        }
    }



}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm (modifier: Modifier=Modifier, onValueChange:(String)->Unit = {}){
    TopHeader()

    val totalBillState = remember{
        mutableStateOf("")
    }

    val numPeople = remember {
        mutableStateOf(1)
    }

    val sliderPosition = remember {
        mutableStateOf(0f)
    }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val tipPercentage = (sliderPosition.value * 100).toInt()
    val tipAmountState = remember {
    mutableStateOf(0.0)
    }

    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(6.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start) {
            InputField(valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    onValueChange(totalBillState.value.trim())
                    keyboardController?.hide()

                })

           if(validState){
                Row(modifier = Modifier.padding(3.dp),
                horizontalArrangement = Arrangement.Start) {
                    Text(text = "Split", modifier = Modifier.
                    align(alignment = Alignment.CenterVertically))
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(modifier = Modifier.padding(horizontal = 3.dp),
                    horizontalArrangement = Arrangement.End) {
                        RoundedIconButton( imageVector = Icons.Default.Remove,
                            onClick = {
                                if(numPeople.value > 1){

                                    numPeople.value = numPeople.value - 1
                                }else 1
                            })

                        Text(
                            text = "${numPeople.value}",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp)
                        )

                        RoundedIconButton( imageVector = Icons.Default.Add,
                            onClick = { numPeople.value = numPeople.value + 1   })
                    }


                }

                    //Tip Row
            Row(modifier = Modifier.padding(horizontal = 3.dp, vertical = 12.dp)) {

                Text(text = "Tip", modifier = Modifier.
                align(alignment = Alignment.CenterVertically))

                Spacer(modifier = Modifier.width(200.dp))

                Text(text = "$ ${tipAmountState.value}",modifier = Modifier.
                align(alignment = Alignment.CenterVertically))
            }

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "$tipPercentage %")
                Spacer(modifier = Modifier.height(14.dp))

                Slider(value = sliderPosition.value, onValueChange = {
                        newVal-> sliderPosition.value = newVal
                    tipAmountState.value = calculateTotalTip(totalBill=totalBillState.value.toDouble(),
                        tipPercentage = tipPercentage)

                }, modifier = Modifier.
                padding(start = 16.dp, end = 16.dp),
                    steps = 5,)
            }
           }else{
               Box{}
         }
        }
    }

}




//@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetTipAppTheme {
        MyApp {
            MainContent()
        }
    }
}