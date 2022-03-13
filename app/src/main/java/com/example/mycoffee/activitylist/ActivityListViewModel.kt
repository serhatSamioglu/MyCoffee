package com.example.mycoffee.activitylist

import androidx.lifecycle.ViewModel
import com.example.mycoffee.dataclass.ActivityObject
import com.example.mycoffee.dataclass.Employee
import com.example.mycoffee.services.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ActivityListViewModel: ViewModel() {

    private val _activityList = MutableStateFlow<ArrayList<ActivityObject>>(arrayListOf())
    val activityList = _activityList.asStateFlow() // stateflow sürümden dolayı sorun olabilir

    private val employeeList: ArrayList<Employee> = arrayListOf()

    fun getActivities() {
        var tempActivityList: ArrayList<ActivityObject> = arrayListOf()
        GlobalScope.launch {
            Firebase.getActivities()?.let { snapshot ->
                for (activitySnapshot in snapshot.children){
                    activitySnapshot.getValue(ActivityObject::class.java)?.let {
                        tempActivityList.add(it)
                    }
                }
                for ((index, activityListItem) in tempActivityList.withIndex()) {
                    Firebase.getFullName(activityListItem.customerID.toString())?.let { customer_FullName ->
                        tempActivityList[index].customerName = customer_FullName
                        tempActivityList[index].formattedDate = convertTimestampToDate(tempActivityList[index].date?.toLong())

                        // Çalışanların adını her seferinde databaseden almamak için listede tutuyorum
                        // Listede çalışan var ise kullanıyorum yok ise databaseden çekip listeme ekliyorum
                        val employeeIndex = employeeList.indexOfFirst { it.uid == activityListItem.cashierID.toString() } // -1 if not found
                        if (employeeIndex >= 0) {
                            tempActivityList[index].cashierName = employeeList[employeeIndex].fullName
                        } else {
                            Firebase.getFullName(activityListItem.cashierID.toString())?.let { cashier_FullName ->
                                tempActivityList[index].cashierName = cashier_FullName
                                employeeList.add(Employee(activityListItem.cashierID, cashier_FullName)) // listede yok ise ekliyorum
                            }
                        }
                    }
                }
                _activityList.value = tempActivityList
            }
        }
    }

    private fun convertTimestampToDate(timestamp: Long?): String? {
        return if (timestamp != null)
            SimpleDateFormat("HH:mm:ss /dd/MM/yyyy").format(Date(timestamp)).toString()
        else null
    }
}