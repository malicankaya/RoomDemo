package com.malicankaya.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malicankaya.roomdemo.databinding.ItemsRowBinding

class ItemAdapter(
    private val employeeList: ArrayList<EmployeeEntity>,
    private val updateRecord: (id: Int) -> Unit,
    private val deleteRecord: (id: Int) -> Unit
) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(itembinding: ItemsRowBinding) : RecyclerView.ViewHolder(itembinding.root) {
        val llMain = itembinding.llMain
        val tvName = itembinding.tvRecyclerViewName
        val tvEmail = itembinding.tvRecyclerViewEmailID
        val ivEdit = itembinding.ivEdit
        val ivDelete = itembinding.ivDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemsRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val employee = employeeList[position]
        holder.tvName.text = employee.name
        holder.tvEmail.text = employee.email

        if (position % 2 == 0) {
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.lightGray
                )
            )
        } else {
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        }

        holder.ivEdit.setOnClickListener {
            updateRecord.invoke(employee.id)
        }
        holder.ivDelete.setOnClickListener {
            deleteRecord.invoke(employee.id)
        }
    }

    override fun getItemCount(): Int {
        return employeeList.size
    }
}