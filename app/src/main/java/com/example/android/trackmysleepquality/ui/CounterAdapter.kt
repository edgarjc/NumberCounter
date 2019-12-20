package com.example.android.trackmysleepquality.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.Number
import com.example.android.trackmysleepquality.databinding.ListItemCounterBinding

class CounterAdapter (val clickListener: CounterListener): ListAdapter<Number, CounterAdapter.ViewHolder>(NightDiffCallback()) {


/*    var data =  listOf<Number>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size*/

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
/*
        val item = data[position]
*/

/*
        val item = getItem(position)
*/


/*
        holder.bind(item)
*/
        holder.bind(getItem(position)!!, clickListener)
    }

/*    private fun ViewHolder.bind(item: Number, clickListener: Any?) {
        val res = itemView.context.resources

        counterLabel.text = "Test"
        count.text = (item.number.toString())

    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /*val layoutInflater = LayoutInflater.from(parent.context)
        *//*val view = layoutInflater
                .inflate(R.layout.list_item_counter, parent, false)*//*
        val binding =
                ListItemCounterBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)*/

        return ViewHolder.from(parent)
    }


 /*   class ViewHolder(val binding: ListItemCounterBinding) : RecyclerView.ViewHolder(binding.root){
        val counterLabel: TextView = binding.countLabel
        val count: TextView = binding.count
    }*/

    class ViewHolder private constructor(val binding: ListItemCounterBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Number, clickListener: CounterListener) {
            binding.night = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCounterBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class CounterListener(val clickListener: (nightId: Long) -> Unit) {
    fun onClick(night: Number) = clickListener(night.nightId)
}


class NightDiffCallback : DiffUtil.ItemCallback<Number>() {
        override fun areItemsTheSame(oldItem: Number, newItem: Number): Boolean {
            return oldItem.nightId == newItem.nightId
        }

        override fun areContentsTheSame(oldItem: Number, newItem: Number): Boolean {
            return oldItem == newItem
        }
    }

@BindingAdapter("count")
fun TextView.setCountString(item: Number?) {
    item?.let {
        text = item.number.toString()
    }
}


