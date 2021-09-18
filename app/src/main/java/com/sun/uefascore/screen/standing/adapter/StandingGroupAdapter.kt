package com.sun.uefascore.screen.standing.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.uefascore.R
import com.sun.uefascore.data.model.Standing
import com.sun.uefascore.data.model.StandingGroup
import com.sun.uefascore.data.model.Team
import com.sun.uefascore.utils.OnItemRecyclerViewClickListener
import kotlinx.android.synthetic.main.item_layout_standing_group.view.*

class StandingGroupAdapter : RecyclerView.Adapter<StandingGroupAdapter.ViewHolder?>() {

    private var standingGroups = mutableListOf<StandingGroup>()
    private var listener: OnItemRecyclerViewClickListener<Team>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_standing_group, parent, false)
        return ViewHolder(view, listener).apply {
            bindSubAdapter()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViewData(standingGroups[position].standings)
    }

    override fun getItemCount() = standingGroups.size

    fun updateData(standingGroups: MutableList<StandingGroup>?) {
        standingGroups?.let {
            this.standingGroups.clear()
            this.standingGroups.addAll(it)
            notifyDataSetChanged()
        }
    }

    fun registerItemRecyclerViewClickListener(listener: OnItemRecyclerViewClickListener<Team>) {
        this.listener = listener
    }

    class ViewHolder(
        itemView: View,
        private val itemListener: OnItemRecyclerViewClickListener<Team>?
    ) : RecyclerView.ViewHolder(itemView) {

        private val standingAdapter: StandingAdapter by lazy {
            StandingAdapter()
        }

        fun bindSubAdapter() {
            with(itemView.recyclerViewStanding) {
                adapter = standingAdapter
                setHasFixedSize(true)
                setRecycledViewPool(RecyclerView.RecycledViewPool())
            }
            standingAdapter.registerItemViewHolderListener(itemListener)
        }

        fun bindViewData(standings: MutableList<Standing>?) {
            standings?.let {
                standingAdapter.updateData(it)
                getGroup(it)
            }
        }

        private fun getGroup(standings: MutableList<Standing>?) {
            itemView.textViewTitleGroup.text = standings?.get(0)?.group
        }
    }
}
