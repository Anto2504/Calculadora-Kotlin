package com.example.t3_calculadora

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class MainActivity : AppCompatActivity() {

    private lateinit var textViewOperation: TextView
    private lateinit var gridLayoutButtons: GridLayout

    private var currentInput = StringBuilder()
    private var currentOperator: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        textViewOperation = findViewById(R.id.textViewOperation)
        gridLayoutButtons = findViewById(R.id.gridLayoutButtons)

        // Configurar listeners para los botones
        setupButtonClickListeners()
    }

    private fun setupButtonClickListeners() {
        for (i in 0 until gridLayoutButtons.childCount) {
            val button = gridLayoutButtons.getChildAt(i) as? Button
            button?.setOnClickListener { onButtonClick(it) }
        }
    }

    private fun onButtonClick(view: View) {
        val buttonText = (view as Button).text.toString()

        when (buttonText) {
            "=" -> calculateResult()
            "AC" -> clearInput()
            "+/-" -> toggleSign()
            "+", "-", "*", "/" -> setOperator(buttonText)
            "%" -> applyPercentage()
            else -> appendInput(buttonText)
        }

        updateTextView()
    }

    private fun appendInput(value: String) {
        currentInput.append(value)
    }

    private fun calculateResult() {
        try {
            val result = evaluateExpression(currentInput.toString())
            currentInput = StringBuilder(result.toString())
        } catch (e: Exception) {
            // Handle error, e.g., invalid expression
            currentInput = StringBuilder("Error")
        }
    }

    private fun clearInput() {
        currentInput = StringBuilder()
        currentOperator = null
    }

    private fun toggleSign() {
        if (currentInput.isNotEmpty() && currentInput[0] == '-') {
            currentInput.deleteCharAt(0)
        } else {
            currentInput.insert(0, '-')
        }
    }

    private fun setOperator(operator: String) {
        if (currentOperator == null) {
            // If no current operator, set the operator
            currentOperator = operator
            currentInput.append(operator)
        } else if (currentInput.isNotEmpty() && currentInput.last() !in "+-*/") {
            // Add the operator only if the last character is not an operator
            currentOperator = operator
            currentInput.append(operator)
        }
    }

    private fun applyPercentage() {
        try {
            val result = evaluateExpression(currentInput.toString())
            val percentage = result.divide(BigDecimal("100"))
            currentInput = StringBuilder(percentage.toString())
        } catch (e: Exception) {
            currentInput = StringBuilder("Error")
        }
    }

    private fun updateTextView() {
        textViewOperation.text = currentInput.toString()
    }

    private fun evaluateExpression(expression: String): BigDecimal {
        // Reemplaza las funciones y constantes con sus implementaciones
        val replacedExpression = expression
            .replace("sin", "sinRad")
            .replace("cos", "cosRad")
            .replace("tan", "tanRad")
            .replace("exp", "exp")
            .replace("π", PI.toString())

        return evaluateReplacedExpression(replacedExpression)
    }

    private fun evaluateReplacedExpression(expression: String): BigDecimal {
        // Implementa la evaluación de la expresión aquí.
        // Utiliza Regex y funciones de Kotlin para analizar y evaluar la expresión.

        // Este es solo un ejemplo simple:
        val sanitizedExpression = sanitizeExpression(expression)
        return evaluateSimpleExpression(sanitizedExpression)
    }

    private fun sanitizeExpression(expression: String): String {
        // Elimina caracteres no deseados y asegúrate de que la expresión sea segura.
        return expression.replace(Regex("[^0-9+\\-*/.()]"), "")
    }

    private fun evaluateSimpleExpression(expression: String): BigDecimal {
        val parts = expression.split("[-+*/]".toRegex())
        if (parts.size == 2) {
            val num1 = BigDecimal(parts[0])
            val num2 = BigDecimal(parts[1])
            return when (currentOperator) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "*" -> num1 * num2
                "/" -> num1 / num2
                else -> BigDecimal("NaN")
            }
        } else {
            return BigDecimal("NaN")
        }
    }
}
