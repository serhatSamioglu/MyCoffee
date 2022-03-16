package com.example.mycoffee.employeelist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycoffee.R
import com.example.mycoffee.authentication.AuthenticationActivity
import com.example.mycoffee.databinding.ActivityEmployeeListBinding
import com.example.mycoffee.services.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.flow.collectLatest
import org.json.JSONException

class EmployeeListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmployeeListBinding

    private lateinit var viewModel: EmployeeListViewModel

    private var qrScanIntegrator: IntentIntegrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEmployeeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(EmployeeListViewModel::class.java)
        viewModel.getEmployees()

        setDefaultValues()
        setObserver()
        setOnClickListener()
        setupScanner()
    }

    private fun setObserver() {
        lifecycleScope.launchWhenStarted {
            viewModel.employeeList.collectLatest {
                var employeeAdapter = EmployeeListAdapter(it)
                binding.employeeList.adapter = employeeAdapter
                setAdapterOnItemLongClickListener(employeeAdapter)
            }
        }
    }

    private fun setOnClickListener() {
        binding.employeeListFab.setOnClickListener {
            qrScanIntegrator?.initiateScan()
        }
    }

    private fun setAdapterOnItemLongClickListener(employeeAdapter: EmployeeListAdapter) {
        employeeAdapter.setOnItemLongClickListener(object : EmployeeListAdapter.onItemLongClickListener{
            override fun onItemLongClick(position: Int) {
                showAlertDialog(employeeAdapter, position)
            }
        })
    }

    private fun setDefaultValues() {
        binding.employeeList.layoutManager = LinearLayoutManager(this)
        binding.employeeList.setHasFixedSize(true)

        binding.employeeListSwipeRefreshLayout.setOnRefreshListener {
            viewModel.getEmployees()
            binding.employeeListSwipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setupScanner() {
        qrScanIntegrator = IntentIntegrator(this)
    }

    private fun showAlertDialog(employeeAdapter: EmployeeListAdapter, position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.remove_employee)
        builder.setPositiveButton(R.string.yes) { _, _ ->
            viewModel.removeEmployee(viewModel.employeeList.value[position].uid.toString())
            viewModel.employeeList.value.removeAt(position)
            employeeAdapter.notifyItemRemoved(position)
        }
        builder.setNegativeButton(R.string.cancel) { _, _ -> }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            // If QRCode has no data.
            if (result.contents == null) {
                Toast.makeText(this, "sonuç yok", Toast.LENGTH_LONG).show()
            } else {
                // If QRCode contains data.
                try {
                    viewModel.addEmployee(result.contents.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Data not in the expected format. So, whole object as toast message.
                    Toast.makeText(this, result.contents, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Firebase.signOutUser()
        navigateNewScreen(Intent(applicationContext, AuthenticationActivity::class.java))
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun navigateNewScreen(intent: Intent) {
        startActivity(intent)
        // finish() // Todo: BaseActivitye taşındığında finish() olayı parametre ile kontrol edilebilir
    }
}