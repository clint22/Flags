package com.clint.flags.features.countries

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.clint.flags.R
import com.clint.flags.core.extensions.inflate
import com.clint.flags.core.extensions.loadFromUrl
import com.skydoves.transformationlayout.TransformationLayout
import javax.inject.Inject
import kotlin.properties.Delegates

class CountriesAdapter @Inject constructor() : RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {

    internal var countriesList: List<Countries> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryImageView: ImageView = itemView.findViewById(R.id.countryImageView)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val itemTransformationLayout: TransformationLayout =
            itemView.findViewById(R.id.item_transformation_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflate(R.layout.row_flags))

    override fun getItemCount(): Int = countriesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = countriesList[position]
        holder.run {
            countryImageView.loadFromUrl(country.flag)
            textViewDescription.text = country.name
            itemView.setOnClickListener {
                CountryDetailActivity.startActivity(
                    itemView.context,
                    itemTransformationLayout,
                    country,
                    position
                )
            }
        }

    }
}