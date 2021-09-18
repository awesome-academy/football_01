package com.sun.uefascore.screen.fixtures.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sun.uefascore.R
import com.sun.uefascore.data.model.Fixture
import com.sun.uefascore.utils.loadImageUrl
import kotlinx.android.synthetic.main.item_fixture_of_day.view.*

@Suppress("NAME_SHADOWING")
class FixtureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var onItemClickedHome: ((Fixture) -> Unit)? = null
    private var onItemClickedAway: ((Fixture) -> Unit)? = null

    @SuppressLint("SetTextI18n")
    fun binData(fixture: Fixture) {
        itemView.apply {
            textViewNameHome.text = fixture.home?.name
            textViewNameAway.text = fixture.away?.name
            if (fixture.goals?.home == null && fixture.goals?.away == null) {
                textViewGoals.text = context.getString(R.string.upcoming)
            } else {
                textViewGoals.text =
                    fixture.goals.home.toString() + " - " + fixture.goals.away.toString()
            }
            fixture.home?.logo?.let { imageViewTeamHome?.loadImageUrl(it) }
            fixture.away?.logo?.let { imageViewTeamWay?.loadImageUrl(it) }
            imageViewTeamHome.setOnClickListener {
                onItemClickedHome?.let { it -> it(fixture) }
            }
            imageViewTeamWay.setOnClickListener {
                onItemClickedAway?.let { it -> it(fixture) }
            }
        }
    }

    fun registerHomeClickListener(onItemClickedHome: ((Fixture) -> Unit)?) {
        this.onItemClickedHome = onItemClickedHome
    }

    fun registerAwayClickListener(onItemClickedAway: ((Fixture) -> Unit)?) {
        this.onItemClickedAway = onItemClickedAway
    }
}
