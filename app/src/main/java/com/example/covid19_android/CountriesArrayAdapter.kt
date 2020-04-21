package com.example.covid19_android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.covid19_android.model.CasesData


class CountriesArrayAdapter(
    context: Context,
    private val list: ArrayList<CasesData>
): BaseAdapter() {

    private class ViewHolder {
        var icon: ImageView? = null
        var title: TextView? = null
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.spinner_item, parent, false)

            viewHolder = ViewHolder()
            viewHolder.icon = view.findViewById(R.id.icon) as ImageView
            viewHolder.title = view.findViewById(R.id.title) as TextView

            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        if(position == 0) {
            viewHolder.title?.text = "All"

            viewHolder.icon?.setImageResource(R.drawable.ic_world)
        } else {
            viewHolder.title?.text = list[position-1].country

            viewHolder.icon?.apply {
                Glide.with(view)
                    .load(list[position - 1].countryInfo?.flag)
                    .override(100)
                    .into(this)
            }
        }

        return view
    }

    override fun getCount(): Int {
        return list.size + 1
    }

    override fun getItem(position: Int): String {
        return if(position == 0) {
            "All"
        } else {
            list[position - 1].country ?: "unknown"
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
