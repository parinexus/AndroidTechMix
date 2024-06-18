package android.tech.mix.data.network

import android.tech.mix.data.network.response.CityData
import android.tech.mix.data.network.response.DailyWeatherForecastResponse
import android.tech.mix.data.network.response.HourlyWeatherForecastResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AccuWeatherApiService {

    @GET("locations/v1/cities/search")
    fun fetchCities(
        @Query("q") cityName: String,
        @Query("apikey") apiKey: String
    ): Call<List<CityData>>

    @GET("forecasts/v1/daily/{days}/{cityId}")
    fun fetchDailyForecastByCityId(
        @Path("cityId") cityId: String,
        @Path("days") numberOfDays: String = FORECAST_DAY,
        @Query("apikey") appKey: String,
        @Query("metric") metric: Boolean = DEFAULT_METRIC,
        @Query("language") lang: String = DEFAULT_LANG
    ): Call<DailyWeatherForecastResponse>

    @GET("forecasts/v1/hourly/{hours}/{cityId}")
    fun fetchHourlyForecastByCityId(
        @Path("cityId") cityId: String,
        @Path("hours") numberOfHours: String = FORECAST_HOUR,
        @Query("apikey") appKey: String,
        @Query("metric") metric: Boolean = DEFAULT_METRIC,
        @Query("language") lang: String = DEFAULT_LANG
    ): Call<List<HourlyWeatherForecastResponse>>

    companion object {
        const val CITY_SEARCH_LIMIT_VALUE = 5

        const val DEFAULT_METRIC = true

        const val DEFAULT_LANG = "en"
        const val FORECAST_DAY = "5day"
        const val FORECAST_HOUR = "12hour"

        const val BASE_URL = "https://api.openweathermap.org/"
        const val BASE_IMAGE_URL = "https://openweathermap.org/img/wn/%s@2x.png"
    }
}