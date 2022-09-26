package com.malicankaya.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.malicankaya.roomdemo.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val employeeDao = (application as EmployeeApp).db.employeeDao

        binding?.btnSubmit?.setOnClickListener {
            addRecord(employeeDao)
        }
    }

    private fun addRecord(employeeDao: EmployeeDao) {
        val name = binding?.etName?.text.toString()
        val email = binding?.etEmailID?.text.toString()

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Name and email must be filled.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            lifecycleScope.launch {
                employeeDao.insert(EmployeeEntity(name = name, email = email))

                runOnUiThread {
                    Toast.makeText(applicationContext,"Record saved",Toast.LENGTH_SHORT).show()
                }

                binding?.etName?.text?.clear()
                binding?.etEmailID?.text?.clear()
            }
        }
    }
}