package com.example.personalexpensetrackerapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val expenseDao: ExpenseDao = ExpenseDatabase.getDatabase(application).expenseDao()

    val allExpenses: LiveData<List<Expense>> = expenseDao.getAllExpenses()

    fun insert(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        expenseDao.insert(expense)
    }
}