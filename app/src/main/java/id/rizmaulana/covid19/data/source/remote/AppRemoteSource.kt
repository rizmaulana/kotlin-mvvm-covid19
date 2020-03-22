package id.rizmaulana.covid19.data.source.remote

/**
 * rizmaulana@live.com 2019-06-16.
 */
class AppRemoteSource(private val api: Api) {
    fun overview() = api.overview()

    fun daily() = api.daily()

    fun confirmed() = api.confirmed()

    fun deaths() = api.deaths()

    fun recovered() = api.recovered()

    fun country(id: String) = api.country(id)

    fun indonesiaDaily() = api.getIndonesiaDaily()
}