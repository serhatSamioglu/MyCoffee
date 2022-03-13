package com.example.mycoffee.employeelist

import androidx.lifecycle.ViewModel
import com.example.mycoffee.dataclass.Employee
import com.example.mycoffee.services.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EmployeeListViewModel: ViewModel() {
    private val _employeeList = MutableStateFlow<ArrayList<Employee>>(arrayListOf())
    val employeeList = _employeeList.asStateFlow() // stateflow sürümden dolayı sorun olabilir

    fun getEmployees() {
        var tempEmployeeList: ArrayList<Employee> = arrayListOf()
        GlobalScope.launch {
            Firebase.getEmployees()?.let { snapshot ->
                for (employeeSnapshot in snapshot.children){

                    employeeSnapshot.value?.let { employee_FullName ->
                        tempEmployeeList.add(Employee(employeeSnapshot.key, employee_FullName.toString()))
                    }
                }
                _employeeList.value = tempEmployeeList
            }
        }
    }

    fun addEmployee(employeeID: String) {
        GlobalScope.launch {
            Firebase.addEmployee(employeeID)
        }
    }

    fun removeEmployee(employeeID: String) {
        GlobalScope.launch {
            Firebase.removeEmployee(employeeID)
        }
    }
}