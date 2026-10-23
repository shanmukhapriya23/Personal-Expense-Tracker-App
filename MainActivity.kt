package com.example.personalexpensetrackerapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.rvExpenses)
        expenseAdapter = ExpenseAdapter()
        recyclerView.adapter = expenseAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]

        val tvTotalAmount = findViewById<TextView>(R.id.tvTotalAmount)

        // Observe all expenses and update adapter + total amount automatically
        expenseViewModel.allExpenses.observe(this) { expenses ->
            expenses?.let {
                expenseAdapter.setData(it)

                // Calculate total sum directly from the list
                val totalSum = it.sumOf { expense -> expense.amount }
                tvTotalAmount.text = String.format(Locale.getDefault(), "$%.2f", totalSum)
            }
        }

        val fab = findViewById<FloatingActionButton>(R.id.fabAddExpense)
        fab.setOnClickListener {
            val intent = Intent(this, AddExpenseActivity::class.java)
            startActivity(intent)
        }
    }
}