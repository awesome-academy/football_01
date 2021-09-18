package com.sun.uefascore.screen.fixtures.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.uefascore.R
import com.sun.uefascore.data.model.Fixture
import com.sun.uefascore.utils.Constant

class FixtureAdapter() :
    RecyclerView.Adapter<FixtureViewHolder>() {

    private var onItemClickedHome: ((Fixture) -> Unit)? = null
    private var onItemClickedAway: ((Fixture) -> Unit)? = null
    private val fixtures = mutableListOf<Fixture>()

    fun updateData(fixture: MutableList<Fixture>?) {
        fixture?.let {
            fixtures.clear()
            fixtures.addAll(it)
            notifyDataSetChanged()
        }
    }

    fun registerHomeClickListener(onItemClickedHome: ((Fixture) -> Unit)?) {
        this.onItemClickedHome = onItemClickedHome
    }

    fun registerAwayClickListener(onItemClickedAway: ((Fixture) -> Unit)?) {
        this.onItemClickedAway = onItemClickedAway
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) Constant.TYPE_HEADER
        else Constant.TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixtureViewHolder {
        return if (viewType == 1) {
            FixtureViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_fixture_of_day_green, parent, false)
            ).apply {
                registerAwayClickListener(onItemClickedAway)
                registerHomeClickListener(onItemClickedHome)
            }
        } else {
            FixtureViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_fixture_of_day, parent, false)
            ).apply {
                registerAwayClickListener(onItemClickedAway)
                registerHomeClickListener(onItemClickedHome)
            }
        }
    }

    override fun onBindViewHolder(holder: FixtureViewHolder, position: Int) {
        holder.binData(fixtures[position])
    }

    override fun getItemCount() = fixtures.size
}
