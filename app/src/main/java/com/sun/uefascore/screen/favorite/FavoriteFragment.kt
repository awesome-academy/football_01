package com.sun.uefascore.screen.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sun.uefascore.R
import com.sun.uefascore.data.model.TeamDetail
import com.sun.uefascore.data.source.local.TeamLocalDataSource
import com.sun.uefascore.data.source.repository.FavoriteRepository
import com.sun.uefascore.screen.favorite.adapter.FavoriteAdapter
import com.sun.uefascore.screen.favorite.adapter.OnFavoriteRecyclerViewClickListener
import com.sun.uefascore.screen.teamdetail.TeamDetailFragment
import com.sun.uefascore.utils.OnFavoriteListener
import com.sun.uefascore.utils.addFragment
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_favorite.swipeRefreshData
import kotlinx.android.synthetic.main.fragment_search_team.*
import kotlinx.android.synthetic.main.fragment_standing.*

class FavoriteFragment : Fragment(), FavoriteContract.View,
    OnFavoriteRecyclerViewClickListener, OnFavoriteListener {

    private val adapter: FavoriteAdapter by lazy {
        FavoriteAdapter()
    }
    private val presenter by lazy {
        FavoritePresenter(
            FavoriteRepository.getInstance(
                TeamLocalDataSource.getInstance(requireContext())
            )
        )
    }
    private var season: String? = null
    private var onFavoriteListener: OnFavoriteListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
        reloadData()
    }

    override fun onDeleteTeamLocalSuccess() {
        presenter.onGetFavorites()
    }

    override fun onDeletePlayersLocalSuccess() {
        presenter.onGetFavorites()
    }

    override fun onGetFavoritesSuccess(teamDetails: MutableList<TeamDetail>) {
        adapter.updateData(teamDetails)
        swipeRefreshData.isRefreshing = false
    }

    override fun onFailed(idMessage: Int) {
        Toast.makeText(context, getString(idMessage), Toast.LENGTH_LONG).show()
    }

    override fun onClickItemListener(item: TeamDetail) {
        addFragment(
            TeamDetailFragment.newInstance(item.id.toString(), season).apply {
                registerFavoriteListener(this@FavoriteFragment)
            },
            R.id.containerLayout
        )
    }

    override fun onClickFavoriteListener(item: TeamDetail) {
        presenter.apply {
            onDeletePlayersLocal(item.id.toString())
            onDeleteTeamLocal(item.id.toString())
        }
    }

    override fun onClickFavoriteListener() {
        this.onFavoriteListener?.onClickFavoriteListener()
    }

    fun registerFavoriteListener(onFavoriteListener: OnFavoriteListener) {
        this.onFavoriteListener = onFavoriteListener
    }

    private fun initView() {
        recyclerViewFavorite.adapter = adapter
        adapter.registerRecyclerViewClickListener(this)
    }

    private fun initData() {
        presenter.apply {
            setView(this@FavoriteFragment)
            onGetFavorites()
        }
    }

    fun onUpdateFavorite() {
        initData()
    }

    fun updateSeason(season: String) {
        this.season = season
        initData()
    }

    private fun reloadData() {
        swipeRefreshData.setOnRefreshListener {
            initData()
        }
    }

    companion object {

        fun newInstance() = FavoriteFragment()
    }
}
