package com.example.myapplication

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val heightInput = findViewById<EditText>(R.id.heightInput)
        val weightInput = findViewById<EditText>(R.id.weightInput)
        val calculateButton = findViewById<Button>(R.id.calculateButton)
        val resultCircle = findViewById<TextView>(R.id.bmiResultCircle)
        val resultComment = findViewById<TextView>(R.id.bmiResultComment)

        // Initially hide the result circle and comment
        resultCircle.visibility = View.INVISIBLE
        resultComment.visibility = View.INVISIBLE

        calculateButton.setOnClickListener {
            val height = heightInput.text.toString().toDoubleOrNull()
            val weight = weightInput.text.toString().toDoubleOrNull()

            if (height != null && weight != null) {
                val bmi = weight / (height / 100).pow(2)
                updateUIWithBmiResult(bmi, resultCircle, resultComment)

                // Animate the result circle and comment
                fadeInView(resultCircle, 1000) // 1000 milliseconds for 1 second
                fadeInView(resultComment, 1000)
            } else {
                Toast.makeText(this, "Please enter valid numbers.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUIWithBmiResult(bmi: Double, resultCircle: TextView, resultComment: TextView) {
        val (category, color) = when {
            bmi < 18.5 -> "Underweight" to R.color.bmiUnderweight
            bmi in 18.5..24.9 -> "Normal weight" to R.color.bmiNormal
            bmi in 25.0..29.9 -> "Overweight" to R.color.bmiOverweight
            else -> "Obese" to R.color.bmiObese
        }

        resultCircle.text = "%.1f".format(bmi)
        resultComment.text = category
        val circleColor = ContextCompat.getColor(this, color)
        resultCircle.background.colorFilter = PorterDuffColorFilter(circleColor, PorterDuff.Mode.SRC_IN)
    }

    private fun fadeInView(view: View, duration: Long) {
        view.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate().alpha(1f).setDuration(duration).setListener(null)
        }
    }

    fun onGenderToggleClicked(view: View) {
        if (view is ToggleButton) {
            val isMale = view.id == R.id.maleToggleButton
            val otherButton = findViewById<ToggleButton>(if (isMale) R.id.femaleToggleButton else R.id.maleToggleButton)
            otherButton.isChecked = false // Deselect the other button
            // Set the background of the clicked button to selected
            view.background = ContextCompat.getDrawable(this, R.drawable.toggle_button_selected)
            // Set the background of the other button to unselected
            otherButton.background = ContextCompat.getDrawable(this, R.drawable.toggle_button_unselected)
        }
    }

    fun decrementAge(view: View) {
        val ageInput = findViewById<EditText>(R.id.ageInput)
        val age = ageInput.text.toString().toIntOrNull() ?: 0
        if (age > 0) {
            ageInput.setText((age - 1).toString())
        }
    }

    fun incrementAge(view: View) {
        val ageInput = findViewById<EditText>(R.id.ageInput)
        val age = ageInput.text.toString().toIntOrNull() ?: 0
        ageInput.setText((age + 1).toString())
    }
}