
package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.theme.CalculatorTheme
import kotlin.math.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CalculatorTheme {
                ScientificCalculator()
            }
        }
    }
}

@Composable
fun ScientificCalculator() {


    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var degreeMode by remember { mutableStateOf(true) }
    var memory by remember { mutableStateOf(0.0) }
    var lastAnswer by remember { mutableStateOf(0.0) }

    fun add(value: String) {
        expression += value
    }

    fun calculate() {
        try {
            val answer = evaluateExpression(
                expression,
                degreeMode,
                lastAnswer
            )

            result = formatNumber(answer)
            lastAnswer = answer

        } catch (_: Exception) {
            result = "Error"
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(10, 12, 18))
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {

        Text(
            text = "SCIENTIFIC CALCULATOR",
            color = Color(120, 180, 255),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        // DISPLAY
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(22, 27, 38),
                    RoundedCornerShape(20.dp)
                )
                .padding(15.dp),
            horizontalAlignment = Alignment.End
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (memory != 0.0) "M" else "",
                    color = Color(90, 190, 255),
                    fontSize = 14.sp,
                    fontWeight = FontWegitight.Bold
                )

                Button(
                    onClick = { degreeMode = !degreeMode },
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(42, 52, 70)
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 13.dp,
                        vertical = 4.dp
                    )
                ) {
                    Text(
                        text = if (degreeMode) "DEG" else "RAD",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (expression.isEmpty()) "0" else expression,
                color = Color(175, 180, 190),
                fontSize = 21.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (result.isEmpty()) "0" else result,
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        CalculatorRow(
            listOf("MC", "MR", "M+", "M-", "ANS")
        ) { button ->
            when (button) {
                "MC" -> memory = 0.0
                "MR" -> add(formatNumber(memory))
                "M+" -> {
                    try {
                        memory += evaluateExpression(
                            expression,
                            degreeMode,
                            lastAnswer
                        )
                    } catch (_: Exception) {
                    }
                }
                "M-" -> {
                    try {
                        memory -= evaluateExpression(
                            expression,
                            degreeMode,
                            lastAnswer
                        )
                    } catch (_: Exception) {
                    }
                }
                "ANS" -> add(formatNumber(lastAnswer))
            }
        }

        CalculatorRow(
            listOf("sin", "cos", "tan", "sin⁻¹", "cos⁻¹")
        ) { button ->
            when (button) {
                "sin" -> add("sin(")
                "cos" -> add("cos(")
                "tan" -> add("tan(")
                "sin⁻¹" -> add("asin(")
                "cos⁻¹" -> add("acos(")
            }
        }

        CalculatorRow(
            listOf("tan⁻¹", "log", "ln", "√", "∛")
        ) { button ->
            when (button) {
                "tan⁻¹" -> add("atan(")
                "log" -> add("log(")
                "ln" -> add("ln(")
                "√" -> add("sqrt(")
                "∛" -> add("cbrt(")
            }
        }

        CalculatorRow(
            listOf("x²", "x³", "xʸ", "1/x", "|x|")
        ) { button ->
            when (button) {
                "x²" -> add("^2")
                "x³" -> add("^3")
                "xʸ" -> add("^")
                "1/x" -> add("1/")
                "|x|" -> add("abs(")
            }
        }

        CalculatorRow(
            listOf("π", "e", "!", "(", ")")
        ) { button ->
            when (button) {
                "π" -> add("pi")
                "e" -> add("E")
                "!" -> add("!")
                "(" -> add("(")
                ")" -> add(")")
            }
        }

        CalculatorRow(
            listOf("7", "8", "9", "÷")
        ) { button ->
            if (button == "÷") add("/") else add(button)
        }

        CalculatorRow(
            listOf("4", "5", "6", "×")
        ) { button ->
            if (button == "×") add("*") else add(button)
        }

        CalculatorRow(
            listOf("1", "2", "3", "−", "+")
        ) { button ->
            when (button) {
                "−" -> add("-")
                "+" -> add("+")
                else -> add(button)
            }
        }

        CalculatorRow(
            listOf("0", "00", ".", "C", "⌫")
        ) { button ->
            when (button) {
                "C" -> {
                    expression = ""
                    result = ""
                }
                "⌫" -> {
                    if (expression.isNotEmpty()) {
                        expression = expression.dropLast(1)
                    }
                }
                else -> add(button)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Button(
            onClick = { calculate() },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(35, 125, 220)
            )
        ) {
            Text(
                text = "=",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "DEG/RAD  •  Scientific Calculator",
            color = Color(90, 95, 105),
            fontSize = 11.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CalculatorRow(
    buttons: List<String>,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        buttons.forEach { button ->

            val buttonColor = when {
                button == "C" -> Color(120, 45, 50)
                button == "⌫" -> Color(85, 48, 55)
                button in listOf("÷", "×", "−", "+") ->
                    Color(35, 80, 125)
                button in listOf(
                    "sin", "cos", "tan", "sin⁻¹", "cos⁻¹",
                    "tan⁻¹", "log", "ln", "√", "∛",
                    "x²", "x³", "xʸ", "1/x", "|x|"
                ) -> Color(48, 55, 78)
                button in listOf("MC", "MR", "M+", "M-", "ANS") ->
                    Color(45, 48, 60)
                button in listOf("π", "e", "!", "(", ")") ->
                    Color(42, 58, 78)
                else -> Color(32, 36, 45)
            }

            Button(
                onClick = { onClick(button) },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor
                ),
                contentPadding = PaddingValues(2.dp)
            ) {
                Text(
                    text = button,
                    fontSize = if (button.length > 4) 12.sp else 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(4.dp))
}

fun evaluateExpression(
    input: String,
    degreeMode: Boolean,
    answer: Double
): Double {

    if (input.isBlank()) {
        throw Exception()
    }

    var expression = input

    expression = expression.replace("ANS", answer.toString())
    expression = expression.replace("pi", Math.PI.toString())
    expression = expression.replace("E", Math.E.toString())
    expression = expression.replace("×", "*")
    expression = expression.replace("÷", "/")

    expression = replaceFunctions(expression, degreeMode)

    while (expression.contains("(")) {

        val close = expression.indexOf(")")

        if (close == -1) {
            throw Exception()
        }

        val open = expression.lastIndexOf("(", close)

        if (open == -1) {
            throw Exception()
        }

        val inside = expression.substring(open + 1, close)

        val value = calculateMath(inside)

        expression =
            expression.substring(0, open) +
                    value +
                    expression.substring(close + 1)
    }

    return calculateMath(expression)
}

fun replaceFunctions(
    input: String,
    degreeMode: Boolean
): String {

    var expression = input

    fun toRadiansIfNeeded(value: Double): Double {
        return if (degreeMode) {
            Math.toRadians(value)
        } else {
            value
        }
    }

    fun fromRadiansIfNeeded(value: Double): Double {
        return if (degreeMode) {
            Math.toDegrees(value)
        } else {
            value
        }
    }

    while (expression.contains("sqrt(")) {

        val start = expression.lastIndexOf("sqrt(")
        val close = expression.indexOf(")", start)

        if (close == -1) throw Exception()

        val value =
            expression.substring(start + 5, close).toDouble()

        val answer = sqrt(value)

        expression =
            expression.substring(0, start) +
                    answer +
                    expression.substring(close + 1)
    }

    while (expression.contains("cbrt(")) {

        val start = expression.lastIndexOf("cbrt(")
        val close = expression.indexOf(")", start)

        if (close == -1) throw Exception()

        val value =
            expression.substring(start + 5, close).toDouble()

        val answer = cbrt(value)

        expression =
            expression.substring(0, start) +
                    answer +
                    expression.substring(close + 1)
    }

    while (expression.contains("sin(")) {

        val start = expression.lastIndexOf("sin(")
        val close = expression.indexOf(")", start)

        if (close == -1) throw Exception()

        val value =
            expression.substring(start + 4, close).toDouble()

        val answer =
            sin(toRadiansIfNeeded(value))

        expression =
            expression.substring(0, start) +
                    answer +
                    expression.substring(close + 1)
    }

    while (expression.contains("cos(")) {

        val start = expression.lastIndexOf("cos(")
        val close = expression.indexOf(")", start)

        if (close == -1) throw Exception()

        val value =
            expression.substring(start + 4, close).toDouble()

        val answer =
            cos(toRadiansIfNeeded(value))

        expression =
            expression.substring(0, start) +
                    answer +
                    expression.substring(close + 1)
    }

    while (expression.contains("tan(")) {

        val start = expression.lastIndexOf("tan(")
        val close = expression.indexOf(")", start)

        if (close == -1) throw Exception()

        val value =
            expression.substring(start + 4, close).toDouble()

        val answer =
            tan(toRadiansIfNeeded(value))

        expression =
            expression.substring(0, start) +
                    answer +
                    expression.substring(close + 1)
    }

    while (expression.contains("asin(")) {

        val start = expression.lastIndexOf("asin(")
        val close = expression.indexOf(")", start)

        if (close == -1) throw Exception()

        val value =
            expression.substring(start + 5, close).toDouble()

        val answer =
            fromRadiansIfNeeded(asin(value))

        expression =
            expression.substring(0, start) +
                    answer +
                    expression.substring(close + 1)
    }

    while (expression.contains("acos(")) {

        val start = expression.lastIndexOf("acos(")
        val close = expression.indexOf(")", start)

        if (close == -1) throw Exception()

        val value =
            expression.substring(start + 5, close).toDouble()

        val answer =
            fromRadiansIfNeeded(acos(value))

        expression =
            expression.substring(0, start) +
                    answer +
                    expression.substring(close + 1)
    }

    while (expression.contains("atan(")) {

        val start = expression.lastIndexOf("atan(")
        val close = expression.indexOf(")", start)

        if (close == -1) throw Exception()

        val value =
            expression.substring(start + 5, close).toDouble()

        val answer =
            fromRadiansIfNeeded(atan(value))

        expression =
            expression.substring(0, start) +
                    answer +
                    expression.substring(close + 1)
    }

    while (expression.contains("log(")) {

        val start = expression.lastIndexOf("log(")
        val close = expression.indexOf(")", start)

        if (close == -1) throw Exception()

        val value =
            expression.substring(start + 4, close).toDouble()

        val answer = log10(value)

        expression =
            expression.substring(0, start) +
                    answer +
                    expression.substring(close + 1)
    }

    while (expression.contains("ln(")) {

        val start = expression.lastIndexOf("ln(")
        val close = expression.indexOf(")", start)

        if (close == -1) throw Exception()

        val value =
            expression.substring(start + 3, close).toDouble()

        val answer = ln(value)

        expression =
            expression.substring(0, start) +
                    answer +
                    expression.substring(close + 1)
    }

    while (expression.contains("abs(")) {

        val start = expression.lastIndexOf("abs(")
        val close = expression.indexOf(")", start)

        if (close == -1) throw Exception()

        val value =
            expression.substring(start + 4, close).toDouble()

        val answer = abs(value)

        expression =
            expression.substring(0, start) +
                    answer +
                    expression.substring(close + 1)
    }

    return expression
}

fun calculateMath(input: String): Double {

    if (input.isBlank()) {
        throw Exception()
    }

    var expression = input

    // Factorial
    while (expression.contains("!")) {

        val index = expression.indexOf("!")

        var start = index - 1

        while (
            start >= 0 &&
            (
                    expression[start].isDigit() ||
                            expression[start] == '.'
                    )
        ) {
            start--
        }

        start++

        val number =
            expression.substring(start, index).toDouble()

        if (
            number < 0 ||
            number != floor(number) ||
            number > 170
        ) {
            throw Exception()
        }

        var factorial = 1.0

        for (i in 1..number.toInt()) {
            factorial *= i
        }

        expression =
            expression.substring(0, start) +
                    factorial +
                    expression.substring(index + 1)
    }

    // Power
    while (expression.contains("^")) {

        val index = expression.indexOf("^")

        var leftStart = index - 1

        while (
            leftStart >= 0 &&
            (
                    expression[leftStart].isDigit() ||
                            expression[leftStart] == '.'
                    )
        ) {
            leftStart--
        }

        leftStart++

        var rightEnd = index + 1

        while (
            rightEnd < expression.length &&
            (
                    expression[rightEnd].isDigit() ||
                            expression[rightEnd] == '.'
                    )
        ) {
            rightEnd++
        }

        val left =
            expression.substring(leftStart, index).toDouble()

        val right =
            expression.substring(index + 1, rightEnd).toDouble()

        val value = left.pow(right)

        expression =
            expression.substring(0, leftStart) +
                    value +
                    expression.substring(rightEnd)
    }

    return basicMath(expression)
}

fun basicMath(input: String): Double {

    val tokens = mutableListOf<String>()
    var number = ""

    for (char in input) {

        if (char.isDigit() || char == '.') {

            number += char

        } else {

            if (number.isNotEmpty()) {
                tokens.add(number)
                number = ""
            }

            tokens.add(char.toString())
        }
    }

    if (number.isNotEmpty()) {
        tokens.add(number)
    }

    if (tokens.isEmpty()) {
        throw Exception()
    }

    // Multiplication, division and percentage first
    var index = 1

    while (index < tokens.size - 1) {

        val operator = tokens[index]

        if (
            operator == "*" ||
            operator == "/" ||
            operator == "%"
        ) {

            val left =
                tokens[index - 1].toDouble()

            val right =
                tokens[index + 1].toDouble()

            val value = when (operator) {

                "*" -> left * right

                "/" -> {
                    if (right == 0.0) {
                        throw Exception()
                    }

                    left / right
                }

                "%" -> left % right

                else -> throw Exception()
            }

            tokens[index - 1] = value.toString()
            tokens.removeAt(index)
            tokens.removeAt(index)

        } else {

            index += 2
        }
    }

    // Addition and subtraction
    var value =
        tokens[0].toDouble()

    index = 1

    while (index < tokens.size) {

        val operator = tokens[index]
        val next = tokens[index + 1].toDouble()

        when (operator) {

            "+" -> value += next

            "-" -> value -= next

            else -> throw Exception()
        }

        index += 2
    }

    return value
}

fun formatNumber(number: Double): String {

    if (
        number.isNaN() ||
        number.isInfinite()
    ) {
        return "Error"
    }

    return if (number % 1.0 == 0.0) {

        number.toLong().toString()

    } else {

        String.format(
            "%.10f",
            number
        )
            .trimEnd('0')
            .trimEnd('.')
    }
}

