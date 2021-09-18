package com.sun.uefascore.screen.searchteam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.sun.uefascore.R
import com.sun.uefascore.data.model.TeamDetail
import com.sun.uefascore.data.source.repository.TeamRepository
import com.sun.uefascore.screen.searchteam.adapter.SearchTeamAdapter
import com.sun.uefascore.screen.teamdetail.TeamDetailFragment
import com.sun.uefascore.utils.addFragment
import com.sun.uefascore.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_fixtures.*
import kotlinx.android.synthetic.main.fragment_search_team.*
import kotlinx.android.synthetic.main.fragment_search_team.imageViewBack
import kotlinx.android.synthetic.main.fragment_team_detail.*
import kotlinx.android.synthetic.main.item_team_search.*
import kotlinx.android.synthetic.main.item_team_search.view.*

class SearchFragment : Fragment(), SearchContract.View {

    private var name: String? = null
    private var season: String? = null
    private val searchTeamPresenter by lazy {
        SearchPresenter(
            TeamRepository.instance
        )
    }
    private val searchTeamAdapter by lazy { SearchTeamAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(BUNDLE_NAME)
            season = it.getString(BUNDLE_SEASON)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
        searchTeam()
        onClickItem()
        reloadData()
        handleEvent()
    }

    override fun getTeamByNameSuccess(team: MutableList<TeamDetail>) {
        searchTeamAdapter.updateData(team)
        swipeRefreshDataSearch?.isRefreshing = false
    }

    override fun onError(exception: Exception?) {}

    private fun initData() {
        searchTeamPresenter.apply {
            name?.let {
                setView(this@SearchFragment)
                getTeamByName(it)
            }
        }
    }

    private fun searchTeam() {
        searchViewTeamName.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String?): Boolean {
                searchTeamPresenter.apply {
                    name?.let {
                        setView(this@SearchFragment)
                        getTeamByName(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
        searchViewTeamName.setQuery(name, false)
    }

    private fun initView() {
        recyclerViewTeamSearch.apply {
            adapter = this@SearchFragment.searchTeamAdapter
        }
    }

    private fun onClickItem() {
        searchTeamAdapter.apply {
            registerTeamClickListener {
                addFragment(
                    TeamDetailFragment.newInstance(it.id.toString(), season),
                    R.id.containerLayout
                )
                hideKeyboard()
            }
        }
    }

    private fun reloadData() {
        swipeRefreshDataSearch?.setOnRefreshListener {
            initData()
        }
    }

    private fun handleEvent() {
        imageViewBack.setOnClickListener {
            fragmentManager?.popBackStack()
            hideKeyboard()
        }
    }

    companion object {
        private const val BUNDLE_NAME = "BUNDLE_NAME"
        private const val BUNDLE_SEASON = "BUNDLE_SEASON"

        fun newInstance(name: String?, season: String?) = SearchFragment().apply {
            arguments = bundleOf(
                BUNDLE_NAME to name,
                BUNDLE_SEASON to season
            )
        }
    }
}
