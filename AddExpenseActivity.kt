package com.example.personalexpensetrackerapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var expenseViewModel: ExpenseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]

        val etTitle = findViewById<TextInputEditText>(R.id.etTitle)
        val etAmount = findViewById<TextInputEditText>(R.id.etAmount)
        val etCategory = findViewById<TextInputEditText>(R.id.etCategory)
        val btnSave = findViewById<Button>(R.id.btnSaveExpense)

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val amountStr = etAmount.text.toString().trim()
            val category = etCategory.text.toString().trim()

            if (title.isNotEmpty() && amountStr.isNotEmpty() && category.isNotEmpty()) {
                val amount = amountStr.toDoubleOrNull() ?: 0.0
                val expense = Expense(title = title, amount = amount, category = category)

                expenseViewModel.insert(expense)
                finish()
            }
        }
    }
}