package com.malicankaya.roomdemo

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.malicankaya.roomdemo.databinding.ActivityMainBinding
import com.malicankaya.roomdemo.databinding.DialogUpdateBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val employeeDao = (application as EmployeeApp).db.employeeDao()

        binding?.btnSubmit?.setOnClickListener {
            addRecord(employeeDao)
        }

        lifecycleScope.launch {
            employeeDao.fetchAllEmployees().collect {
                val employees = ArrayList(it)
                setUpRecyclerViewAdapterAndList(employees, employeeDao)
            }
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
                    Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_SHORT).show()
                }

                binding?.etName?.text?.clear()
                binding?.etEmailID?.text?.clear()
            }
        }
    }

    private fun setUpRecyclerViewAdapterAndList(
        employeeList: ArrayList<EmployeeEntity>,
        employeeDao: EmployeeDao
    ) {

        if (employeeList.isNotEmpty()) {
            val itemAdapter = ItemAdapter(employeeList,
                {
                    updateRecordDialog(it, employeeDao)
                },
                {
                    deleteRecordAlertDialog(it, employeeDao)
                }
            )
            binding?.tvNoRecordsAvailable?.visibility = View.INVISIBLE
            binding?.rvItems?.visibility = View.VISIBLE
            binding?.rvItems?.adapter = itemAdapter
        } else {
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
            binding?.rvItems?.visibility = View.INVISIBLE
        }

    }

    private fun updateRecordDialog(id: Int, employeeDao: EmployeeDao) {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        val binding = DialogUpdateBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        lifecycleScope.launch {
            employeeDao.fetchEmployeeById(id).collect {
                binding.etDialogName.setText(it.name)
                binding.etDialogEmailID.setText(it.email)
            }

            Toast.makeText(
                applicationContext,
                "Record updated successfully.",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.tvUpdate.setOnClickListener {
            lifecycleScope.launch {
                if (binding.etDialogName.text.isNotEmpty() && binding.etDialogEmailID.text.isNotEmpty()) {
                    employeeDao.update(
                        EmployeeEntity(
                            id,
                            binding.etDialogName.text.toString(),
                            binding.etDialogEmailID.text.toString()
                        )
                    )
                    dialog.dismiss()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Name or Email cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        binding.tvCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteRecordAlertDialog(id: Int, employeeDao: EmployeeDao) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
            .setIcon(R.drawable.ic_alert)

        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            lifecycleScope.launch {
                employeeDao.delete(EmployeeEntity(id))
                Toast.makeText(
                    applicationContext,
                    "Record deleted successfully.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog = builder.create()

        alertDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}