package com.sun.uefascore.screen.teamdetail.adapter

import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.uefascore.R
import com.sun.uefascore.data.model.PlayerDetail
import com.sun.uefascore.utils.LoadImageUrl
import com.sun.uefascore.utils.OnItemRecyclerViewClickListener
import kotlinx.android.synthetic.main.item_layout_player.view.*

class PlayerAdapter : RecyclerView.Adapter<PlayerAdapter.ViewHolder?>() {

    private var playerDetails = mutableListOf<PlayerDetail>()
    private var listener: OnItemRecyclerViewClickListener<PlayerDetail>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_player, parent, false)
        return ViewHolder(view).apply {
            registerItemRecyclerViewClickListener {
                listener?.onItemClickListener(playerDetails[it])
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViewData(playerDetails[position])
    }

    override fun getItemCount() = playerDetails.size

    fun updateData(players: MutableList<PlayerDetail>) {
        players.let {
            this.playerDetails.clear()
            this.playerDetails.addAll(it)
            notifyDataSetChanged()
        }
    }

    fun registerItemRecyclerViewClickListener(
        listener: OnItemRecyclerViewClickListener<PlayerDetail>?
    ) {
        this.listener = listener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViewData(playerDetail: PlayerDetail) {
            itemView.apply {
                textViewNamePlayer.text = playerDetail.player?.name
                textViewAge.text = playerDetail.player?.age.toString()
                textViewGoals.text = playerDetail.statistic?.goals.toString()
            }
            getImageAvatar(playerDetail)
            getRank()
        }

        fun registerItemRecyclerViewClickListener(listener: (Int) -> Unit) {
            itemView.setOnClickListener {
                listener(adapterPosition)
            }
        }

        private fun getImageAvatar(playerDetail: PlayerDetail) {
            LoadImageUrl {
                itemView.imageViewAvatar?.setImageBitmap(it)
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, playerDetail.player?.photo)
        }

        private fun getRank() {
            itemView.textViewRank.text = "${adapterPosition + 1}"
        }
    }
}
