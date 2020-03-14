package id.rizmaulana.covid19.ui.overview

import id.rizmaulana.covid19.data.model.CovidDaily
import id.rizmaulana.covid19.data.model.CovidDetail
import id.rizmaulana.covid19.data.model.CovidOverview
import id.rizmaulana.covid19.data.repository.Repository
import id.rizmaulana.covid19.ui.InstantTaskExecutorRule
import id.rizmaulana.covid19.ui.shouldBeInstanceOf
import id.rizmaulana.covid19.util.Constant.ERROR_MESSAGE
import id.rizmaulana.covid19.util.rx.TestSchedulerProvider
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
                val errorMessage = dashboardViewModel.toastMessage.value
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
                val errorMessage = dashboardViewModel.toastMessage.value
                assertEquals(true, errorMessage?.isNotEmpty()?: false)
                assertEquals(true, dashboardViewModel.loading.value != null)
                assert(errorMessage == ERROR_MESSAGE)
            }
        }
    }

    Feature("Pinned Data") {
        Scenario("no pinned region data") {
            Given("null pref data") {
                `when`(repository.getPrefCountry()).thenReturn(null)
            }

            When("request pin data") {
                dashboardViewModel.getPinUpdate()
            }

            Then("pin data should be null") {
                assertNull(dashboardViewModel.pinData.value)
            }
        }

        Scenario("having pinned region data") {
            val detail = CovidDetail(countryRegion = "IND")
            val expectedConf = 123
            val updateList = listOf(CovidDetail(countryRegion = "IND", confirmed = 123))
            Given("null pref data") {
                `when`(repository.getPrefCountry()).thenReturn(detail)
                `when`(repository.confirmed()).thenReturn(
                    Observable.just(updateList)
                )
            }

            When("request pin data") {
                dashboardViewModel.getPinUpdate()
            }

            Then("pin data should be updated") {
                assertEquals(expectedConf, dashboardViewModel.pinData.value?.confirmed)
            }
        }

        Scenario("having pinned region data but update fail") {
            val region = "IND"
            val detail = CovidDetail(countryRegion = region)
            Given("null pref data") {
                `when`(repository.getPrefCountry()).thenReturn(detail)
                `when`(repository.confirmed()).thenReturn(
                    Observable.error(Exception(ERROR_MESSAGE))
                )
            }

            When("request pin data") {
                dashboardViewModel.getPinUpdate()
            }

            Then("pin data should not be updated") {
                assertEquals(region, dashboardViewModel.pinData.value?.countryRegion)
            }

            Then("error message is notified") {
                assertEquals(ERROR_MESSAGE, dashboardViewModel.toastMessage.value)
            }
        }
    }
})