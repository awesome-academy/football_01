package com.sun.uefascore.screen.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.sun.uefascore.R
import com.sun.uefascore.screen.favorite.FavoriteFragment
import com.sun.uefascore.screen.fixtures.FixturesFragment
import com.sun.uefascore.screen.standing.StandingFragment
import com.sun.uefascore.screen.topscorers.TopScorersFragment
import com.sun.uefascore.utils.Constant
import com.sun.uefascore.utils.MenuItem
import com.sun.uefascore.utils.OnFavoriteListener
import com.sun.uefascore.utils.OnGetSeasonListener
import com.sun.uefascore.utils.*
import kotlinx.android.synthetic.main.fragment_home_page.*

class HomePageFragment : Fragment(), OnGetSeasonListener, OnFavoriteListener {

    private val fragments = mutableListOf<Fragment>()
    private val fixturesFragment = FixturesFragment.newInstance()
    private val standingFragment = StandingFragment.newInstance()
    private val topScorersFragment = TopScorersFragment.newInstance()
    private val favoriteFragment = FavoriteFragment.newInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onClickFavoriteListener() {
        favoriteFragment.onUpdateFavorite()
    }

    override fun getSeason(season: String) {
        standingFragment.updateSeason(season)
        topScorersFragment.updateSeason(season)
        favoriteFragment.updateSeason(season)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fixturesFragment.apply {
            registerGetSeasonListener(this@HomePageFragment)
            registerFavoriteListener(this@HomePageFragment)
        }
        standingFragment.registerFavoriteListener(this)
        favoriteFragment.registerFavoriteListener(this)
        initFragment()
        fragmentManager?.let {
            viewPagerContainer.adapter = ViewPagerContainerAdapter(it, fragments)
            viewPagerContainer.offscreenPageLimit = Constant.LIMIT_OFFSET
            initBottomItem()
        }
    }

    private fun initFragment() {
        fragments.apply {
            add(MenuItem.FIXTURES.ordinal, fixturesFragment)
            add(MenuItem.STANDING.ordinal, standingFragment)
            add(MenuItem.SCORERS.ordinal, topScorersFragment)
            add(MenuItem.FAVORITE.ordinal, favoriteFragment)
        }
    }

    private fun initBottomItem() {
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeItem -> {
                    viewPagerContainer.currentItem = MenuItem.FIXTURES.ordinal
                    true
                }
                R.id.standingItem -> {
                    viewPagerContainer.currentItem = MenuItem.STANDING.ordinal
                    true
                }
                R.id.topScorersItem -> {
                    viewPagerContainer.currentItem = MenuItem.SCORERS.ordinal
                    true
                }
                R.id.favoriteItem -> {
                    viewPagerContainer.currentItem = MenuItem.FAVORITE.ordinal
                    true
                }
                else -> false
            }
        }
        viewPagerContainer.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                hideKeyboard()
                bottomNavigation.menu.getItem(position).isChecked = true
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    companion object {
        fun newInstance() = HomePageFragment()
    }
}
