package com.sun.uefascore

import com.sun.uefascore.data.model.Fixture
import com.sun.uefascore.data.model.Goals
import com.sun.uefascore.data.model.Team
import com.sun.uefascore.data.source.remote.OnFetchDataJsonListener
import com.sun.uefascore.data.source.repository.FixtureRepository
import com.sun.uefascore.screen.fixtures.ContractFixture
import com.sun.uefascore.screen.fixtures.FixturePresenter
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doAnswer


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {

    @Mock
    lateinit var view: ContractFixture.View

    @Mock
    lateinit var repository: FixtureRepository

    @Mock
    private lateinit var exception: Exception

    private lateinit var presenter: FixturePresenter
    private lateinit var season: MutableList<String>
    private lateinit var fixtures: MutableList<Fixture>

    @Before
    fun setUp() {
        fixtures = mutableListOf(
            Fixture(
                "2020/12/20", Team(1, "A", "link"), Team(2, "B", "link"),
                Goals("0", "1")
            )
        )
        season = mutableListOf(
            "2022",
            "2021",
            "2020",
            "2019",
            "2018",
            "2017",
            "2016",
            "2015",
            "2014",
            "2013",
            "2012",
            "2011",
            "2010",
            "2009",
            "2008"
        )
        presenter = FixturePresenter(repository)
        presenter.setView(view)
    }


    @Test
    fun `get data season success`() {
        //when
        `when`(repository.getSeason(org.mockito.kotlin.any())).doAnswer {
            (it.arguments[0] as OnFetchDataJsonListener<MutableList<String>>).onSuccess(season)
        }
        presenter.getSeason()
        //then
        verify(view).getSeasonSuccess(season)
    }

    @Test
    fun `get data all fixture success`() {
        //when
        `when`(repository.getAllFixture(anyString(), org.mockito.kotlin.any())).doAnswer {
            (it.arguments[1] as OnFetchDataJsonListener<MutableList<Fixture>>).onSuccess(fixtures)
        }
        presenter.getAllFixture("2020")
        //then
        verify(view).onGetAllFixtureSuccess(fixtures)
    }

    @Test
    fun `get data fixture success`() {
        //when
        `when`(repository.getFixture(anyString(), anyString(), org.mockito.kotlin.any())).doAnswer {
            (it.arguments[2] as OnFetchDataJsonListener<MutableList<Fixture>>).onSuccess(fixtures)
        }
        presenter.getFixture("2020/12/20", "2020")
        //then
        verify(view).onGetFixtureSuccess(fixtures)
    }

    @Test
    fun `get data season error`() {
        //when
        `when`(repository.getSeason(org.mockito.kotlin.any())).doAnswer {
            (it.arguments[0] as OnFetchDataJsonListener<MutableList<String>>).onError(exception)
        }
        presenter.getSeason()
        //then
        verify(view).onError(exception)
    }

    @Test
    fun `get data all fixture error`() {
        //when
        `when`(repository.getAllFixture(anyString(), org.mockito.kotlin.any())).doAnswer {
            (it.arguments[1] as OnFetchDataJsonListener<MutableList<Fixture>>).onError(exception)
        }
        presenter.getAllFixture("2020")
        //then
        verify(view).onError(exception)
    }

    @Test
    fun `get data fixture error`() {
        //when
        `when`(repository.getFixture(anyString(), anyString(), org.mockito.kotlin.any())).doAnswer {
            (it.arguments[2] as OnFetchDataJsonListener<MutableList<Fixture>>).onError(exception)
        }
        presenter.getFixture("2020/12/20", "2020")
        //then
        verify(view).onError(exception)
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
