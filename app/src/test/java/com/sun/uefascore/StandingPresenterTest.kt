package com.sun.uefascore

import com.sun.uefascore.data.model.StandingLeague
import com.sun.uefascore.data.source.remote.OnFetchDataJsonListener
import com.sun.uefascore.data.source.repository.StandingRepository
import com.sun.uefascore.screen.standing.StandingContract
import com.sun.uefascore.screen.standing.StandingPresenter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doAnswer
import java.lang.Exception

@RunWith(MockitoJUnitRunner::class)
class StandingPresenterTest {

    @Mock
    private lateinit var view: StandingContract.View

    @Mock
    private lateinit var standingRepository: StandingRepository

    @Mock
    private lateinit var exception: Exception

    private lateinit var presenter: StandingPresenter

    @Before
    fun setUp() {
        presenter = StandingPresenter(standingRepository)
        presenter.setView(view)
    }

    @Test
    fun `get data standing league success`() {
        val standingLeague = StandingLeague(1, "", mutableListOf())
        `when`(
            standingRepository.getStandingLeague(
                anyString(),
                org.mockito.kotlin.any()
            )
        ).doAnswer {
            (it.arguments[1] as OnFetchDataJsonListener<StandingLeague>).onSuccess(standingLeague)
        }
        presenter.getStandingLeague("2020")
        verify(view).onGetStandingLeagueSuccess(standingLeague)
    }

    @Test
        fun `get data standing league error`() {
        `when`(
            standingRepository.getStandingLeague(
                anyString(),
                org.mockito.kotlin.any()
            )
        ).doAnswer {
            (it.arguments[1] as OnFetchDataJsonListener<StandingLeague>).onError(exception)
        }
        presenter.getStandingLeague("2020")
        verify(view).onError(exception)
    }
}
