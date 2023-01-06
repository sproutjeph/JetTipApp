package com.example.jettipapp.utils

fun calculateTotalTip(totalBill: Double, tipPercentage: Int): Double {

    return if(totalBill > 1 && totalBill.toString().isNotEmpty())
        (totalBill * tipPercentage ) / 100 else 0.0


}


fun calculateTotalPerperson(totalBill:Double, numPerson:Int, tipPercentage: Int):Double{
    val bill = calculateTotalTip(totalBill=totalBill,tipPercentage =tipPercentage) + totalBill

    return  (bill/ numPerson)
}