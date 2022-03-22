package com.example.mycoffee.activitylist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycoffee.R
import com.example.mycoffee.authentication.AuthenticationActivity
import com.example.mycoffee.databinding.ActivityListBinding
import com.example.mycoffee.employeelist.EmployeeListActivity
import com.example.mycoffee.services.Firebase
import kotlinx.coroutines.flow.collectLatest

class ActivityListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding

    private lateinit var viewModel: ActivityListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ActivityListViewModel::class.java)

        viewModel.getActivities()

        setDefaultValues()
        setObserver()
        setOnClickListener()
    }

    private fun setObserver() {
        lifecycleScope.launchWhenStarted {
            viewModel.activityList.collectLatest {
                var activityListAdapter = ActivityListAdapter(it, this@ActivityListActivity)
                binding.activityList.adapter = activityListAdapter
                setAdapterOnClickListener(activityListAdapter)
            }
        }
    }

    private fun setAdapterOnClickListener(activityListAdapter: ActivityListAdapter) {
        activityListAdapter.setOnItemClickListener(object : ActivityListAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                /*var intent = Intent(applicationContext, CafeDetailActivity::class.java)
                intent.putExtra("CafeListItem", tempCafeList[position]) // todo navigationlar base e alınırken putExtraları degisken ile alınabilir
                startActivity(intent)*/
            }
        })
    }

    private fun setDefaultValues() {
        binding.activityList.layoutManager = LinearLayoutManager(this)
        binding.activityList.setHasFixedSize(true)

        binding.activityListSwipeRefreshLayout.setOnRefreshListener {
            viewModel.getActivities()
            binding.activityListSwipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setOnClickListener() {
        binding.activityListFab.setOnClickListener {
            navigateNewScreen(Intent(applicationContext, EmployeeListActivity::class.java))
        }
    }

    private fun navigateNewScreen(intent: Intent) {
        startActivity(intent)
        // finish() // Todo: BaseActivitye taşındığında finish() olayı parametre ile kontrol edilebilir
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
}