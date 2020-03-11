package id.rizmaulana.covid19.util

/**
 * rizmaulana@live.com 2019-06-14.
 */
object Constant {
    const val API_VERSION = "1.0.0"
    const val DB_VERSION = 1
    const val NETWORK_TIMEOUT = 60L
    const val ERROR_MESSAGE = "Cannot proceed your request, please try again later"
}

object CacheKey {
    const val OVERVIEW = "cache_statistics"
    const val DAYS = "cache_days"
    const val CONFIRMED = "cache_confirmed"
    const val DEATH = "cache_death"
    const val RECOVERED = "cache_recovered"
    const val FULL_STATS = "cache_full_details"
}

object CaseType {
    const val CONFIRMED = 0
    const val DEATHS = 1
    const val RECOVERED = 2
    const val FULL = 3
}

object IncrementStatus {
    const val FLAT = 0
    const val INCREASE = 1
    const val DECREASE = 2
}