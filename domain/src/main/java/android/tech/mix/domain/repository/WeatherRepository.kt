package android.tech.mix.domain.repository

import android.tech.mix.domain.model.*

interface WeatherRepository {

    /**
     * Retrieves city information using the provided city name.
     *
     * @param cityName The name of the city
     * @return A [Resource] containing a list of available city details
     */
    suspend fun fetchCityInfo(cityName: String): DataResult<List<CityInfo>>

    /**
     * Retrieves hourly weather forecast information for the specified city.
     *
     * @param city The city for which to fetch the forecast
     * @param numberOfHours The number of hours for which to retrieve the forecast
     * @return A [Resource] containing the hourly weather forecast
     */
    suspend fun fetchHourlyWeatherForecast(city: CityInfo, numberOfHours: Int): DataResult<WeatherForecastInfo>

    /**
     * Retrieves daily weather forecast information for the specified city.
     *
     * @param city The city for which to fetch the forecast
     * @param numberOfDays The number of days for which to retrieve the forecast
     * @return A [Resource] containing the daily weather forecast
     */
    suspend fun fetchDailyWeatherForecast(city: CityInfo, numberOfDays: Int): DataResult<WeatherForecastInfo>

    /**
     * Retrieves all weather forecast information for the specified city, including hourly and daily forecasts.
     *
     * @param city The city for which to fetch the forecast
     * @param numberOfHours The number of hours for which to retrieve the hourly forecast
     * @param numberOfDays The number of days for which to retrieve the daily forecast
     * @return A [Resource] containing a map of weather types to corresponding weather forecasts
     */
    suspend fun fetchAllWeatherForecast(
        city: CityInfo,
        numberOfHours: Int,
        numberOfDays: Int
    ): DataResult<Map<WeatherDataType, List<WeatherInfo>>>
}
