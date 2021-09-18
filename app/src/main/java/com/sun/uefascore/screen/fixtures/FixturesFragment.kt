package com.sun.uefascore.screen.fixtures

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sun.uefascore.R
import com.sun.uefascore.data.model.Fixture
import com.sun.uefascore.data.source.repository.FixtureRepository
import com.sun.uefascore.screen.fixtures.adapter.FixtureAdapter
import com.sun.uefascore.screen.fixtures.adapter.FixtureAllAdapter
import com.sun.uefascore.screen.searchteam.SearchFragment
import com.sun.uefascore.screen.teamdetail.TeamDetailFragment
import com.sun.uefascore.utils.*
import kotlinx.android.synthetic.main.fragment_fixtures.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FixturesFragment : Fragment(), ContractFixture.View, AdapterView.OnItemSelectedListener,
    OnFavoriteListener {

    private var season = ""
    private var dayByPicker = ""
    private var dayDevice = ""
    private var onFavoriteListener: OnFavoriteListener? = null
    private var onGetSeasonListener: OnGetSeasonListener? = null
    private val fixtureAdapter by lazy { FixtureAdapter() }
    private val fixtureAllAdapter by lazy { FixtureAllAdapter() }
    private val fixturePresenter: ContractFixture.Presenter by lazy {
        FixturePresenter(
            FixtureRepository.instance
        )
    }

    override fun onClickFavoriteListener() {
        onFavoriteListener?.onClickFavoriteListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fixtures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPresenter()
        initDate()
        initView()
        searchTeam()
        onClickItem()
        onClickDatePicker()
        reloadData()
        constraintLayoutFixtures.setOnClickListener {
            hideKeyboard()
        }
    }

    override fun onGetFixtureSuccess(fixtures: MutableList<Fixture>) {
        fixtureAdapter.updateData(fixtures)
    }

    override fun onGetAllFixtureSuccess(fixtures: MutableList<Fixture>) {
        fixtureAllAdapter.updateData(fixtures)
        swipeRefreshData.isRefreshing = false
    }

    override fun getSeasonSuccess(season: MutableList<String>) {
        season.reverse()
        initSpinner(season)
    }

    override fun onError(exception: Exception?) {
        Toast.makeText(context, exception?.message, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun initPresenter() {
        val date = Date()
        val formatter = SimpleDateFormat(Constant.DAY_FORMAT)
        dayDevice = formatter.format(date)
        fixturePresenter.apply {
            setView(this@FixturesFragment)
            getFixture(dayDevice, season)
            getSeason()
        }
    }

    private fun initView() {
        recyclerViewFixtureOfDay.apply {
            adapter = this@FixturesFragment.fixtureAdapter
        }
        recyclerViewFixtureHistory.apply {
            adapter = this@FixturesFragment.fixtureAllAdapter
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun initDate() {
        val date = Date()
        val formatter = SimpleDateFormat(Constant.DAY_FORMAT)
        val dayDevice = formatter.format(date)
        dayByPicker = dayDevice
        textViewDayOfMatches.text = dayDevice
    }

    fun registerGetSeasonListener(onGetSeasonListener: OnGetSeasonListener) {
        this.onGetSeasonListener = onGetSeasonListener
    }

    private fun searchTeam() {
        searchViewTeam.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String?): Boolean {
                addFragment(SearchFragment.newInstance(name?.trim(), season), R.id.containerLayout)
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
    }

    private fun onClickItem() {
        fixtureAdapter.apply {
            registerHomeClickListener {
                addFragment(
                    TeamDetailFragment.newInstance(it.home?.id.toString(), season).apply {
                        registerFavoriteListener(this@FixturesFragment)
                    },
                    R.id.containerLayout
                )
                hideKeyboard()
            }
            registerAwayClickListener {
                addFragment(
                    TeamDetailFragment.newInstance(it.away?.id.toString(), season).apply {
                        registerFavoriteListener(this@FixturesFragment)
                    },
                    R.id.containerLayout
                )
                hideKeyboard()
            }
        }
        fixtureAllAdapter.apply {
            registerHomeClickListener {
                addFragment(
                    TeamDetailFragment.newInstance(it.home?.id.toString(), season).apply {
                        registerFavoriteListener(this@FixturesFragment)
                    },
                    R.id.containerLayout
                )
                hideKeyboard()
            }
            registerAwayClickListener {
                addFragment(
                    TeamDetailFragment.newInstance(it.away?.id.toString(), season).apply {
                        registerFavoriteListener(this@FixturesFragment)
                    },
                    R.id.containerLayout
                )
                hideKeyboard()
            }
        }
    }

    private fun onClickDatePicker() {
        imageViewCalendar.setOnClickListener {
            onGetDataByDatePicker()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun onGetDataByDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker =
            context?.let {
                DatePickerDialog(it, { _, year, monthOfYear, dayOfMonth ->
                    val stringDate = SimpleDateFormat(Constant.DAY_FORMAT).format(
                        SimpleDateFormat(
                            Constant.DAY_FORMAT_FORWARD
                        ).parse("$year/${monthOfYear + 1}/$dayOfMonth")
                    )
                    dayByPicker = stringDate
                    textViewDayOfMatches.text = stringDate
                    fixturePresenter.apply {
                        setView(this@FixturesFragment)
                        getFixture(stringDate, season)
                    }
                }, year, month, day)
            }
        datePicker?.show()
    }

    private fun initSpinner(seasons: MutableList<String>) {
        val stateAdapter = view?.let {
            ArrayAdapter(
                it.context,
                R.layout.support_simple_spinner_dropdown_item,
                seasons
            )
        }
        spinnerSeason?.adapter = stateAdapter
        spinnerSeason?.onItemSelectedListener = this
    }

    fun registerFavoriteListener(onFavoriteListener: OnFavoriteListener) {
        this.onFavoriteListener = onFavoriteListener
    }

    private fun reloadData() {
        swipeRefreshData.setOnRefreshListener {
            fixturePresenter.apply {
                getAllFixture(season)
            }
        }
    }

    override fun onItemSelected(p: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val seasonSpinner = p?.selectedItem.toString()
        textViewSeason.text = seasonSpinner
        season = seasonSpinner
        fixturePresenter.apply {
            setView(this@FixturesFragment)
            getAllFixture(season)
            getFixture(dayByPicker, season)
        }
        onGetSeasonListener?.getSeason(seasonSpinner)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    companion object {
        fun newInstance() = FixturesFragment()
    }
}
