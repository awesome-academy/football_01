package com.sun.uefascore.screen.searchteam.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.uefascore.R
import com.sun.uefascore.data.model.TeamDetail

class SearchTeamAdapter : RecyclerView.Adapter<SearchTeamViewHolder>() {

    private val teams = mutableListOf<TeamDetail>()
    private var onItemClickTeam: ((TeamDetail) -> Unit)? = null

    fun updateData(teamDetail: MutableList<TeamDetail>?) {
        teamDetail?.let {
            teams.clear()
            teams.addAll(it)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTeamViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_team_search, parent, false)
        return SearchTeamViewHolder(view).apply {
            registerTeamClickListener(onItemClickTeam)
        }
    }

    override fun onBindViewHolder(holder: SearchTeamViewHolder, position: Int) {
        holder.binData(teams[position])
    }

    override fun getItemCount() = teams.size

    fun registerTeamClickListener(onItemClickTeam: ((TeamDetail) -> Unit)?) {
        this.onItemClickTeam = onItemClickTeam
    }
}
