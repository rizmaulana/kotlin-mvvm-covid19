package id.rizmaulana.covid19.ui.overview

import id.rizmaulana.covid19.data.model.CovidDaily
import id.rizmaulana.covid19.data.model.CovidOverview
import id.rizmaulana.covid19.data.repository.Repository
import id.rizmaulana.covid19.ui.InstantTaskExecutorRule
import id.rizmaulana.covid19.ui.shouldBeInstanceOf
import id.rizmaulana.covid19.util.Constant.ERROR_MESSAGE
import id.rizmaulana.covid19.util.rx.TestSchedulerProvider
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.mockito.Mockito.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.lang.Exception

class DashboardViewModelTest: Spek({

    InstantTaskExecutorRule(this)

    val repository = mock(Repository::class.java)
    val dashboardViewModel = DashboardViewModel(repository, TestSchedulerProvider())

    Feature("Dashboard") {

        Scenario("get dashboard data properly") {
            val overviewData = CovidOverview()

            Given("overview data") {
                doReturn(Observable.just(overviewData)).`when`(repository).overview()
                `when`(repository.getCacheOverview()).thenReturn(overviewData)
            }

            When("request overview data") {
                dashboardViewModel.getOverview()
            }

            Then("it should return data correctly") {
                dashboardViewModel.overviewData.value.shouldBeInstanceOf<CovidOverview>()
                assertEquals(true, dashboardViewModel.overviewData.value != null)
                assertEquals(true, dashboardViewModel.loading.value != null)
            }
        }

        Scenario("get invalid dashboard data") {
            Given("throwable data") {
                `when`(repository.overview()).thenReturn(
                    Observable.error(Exception(ERROR_MESSAGE))
                )
            }

            When("request overview data") {
                dashboardViewModel.getOverview()
            }

            Then("it should error message") {
                val errorMessage = dashboardViewModel.errorMessage.value
                assertEquals(true, errorMessage?.isNotEmpty()?: false)
                assertEquals(true, dashboardViewModel.loading.value != null)
                assert(errorMessage == ERROR_MESSAGE)
            }
        }
    }

    Feature("Daily") {
        Scenario("get daily data properly") {
            val dailyData = listOf(CovidDaily())

            Given("overview data") {
                doReturn(Observable.just(dailyData)).`when`(repository).daily()
                `when`(repository.getCacheDaily()).thenReturn(dailyData)
            }

            When("request daily data") {
                dashboardViewModel.getDailyUpdate()
            }

            Then("it should return data correctly") {
                dashboardViewModel.dailyListData.value.shouldBeInstanceOf<List<CovidDaily>>()
                assertEquals(true, dashboardViewModel.dailyListData.value != null)
            }
        }

        Scenario("get empty daily data") {
            Given("throwable data") {
                `when`(repository.daily()).thenReturn(
                    Observable.error(Exception(ERROR_MESSAGE))
                )
            }

            When("request overview data") {
                dashboardViewModel.getDailyUpdate()
            }

            Then("it should error message") {
                val errorMessage = dashboardViewModel.errorMessage.value
                assertEquals(true, errorMessage?.isNotEmpty()?: false)
                assertEquals(true, dashboardViewModel.loading.value != null)
                assert(errorMessage == ERROR_MESSAGE)
            }
        }
    }
})