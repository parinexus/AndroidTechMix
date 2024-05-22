package android.tech.mix.domain.usecase

import android.tech.mix.domain.model.CityInfo
import android.tech.mix.domain.model.DataResult
import android.tech.mix.domain.model.WeatherTypeData
import android.tech.mix.domain.repository.WeatherRepository

class WeatherForecastRetrievalUseCase(private val repository: WeatherRepository) {

    /**
     * Retrieves all weather forecast information for the specified city from the repository.
     *
     * @param city The city for which to retrieve the forecast
     * @param hours The number of hours for which to retrieve the hourly forecast
     * @param days The number of days for which to retrieve the daily forecast
     * @return A [DataResult] containing a list of [WeatherTypeData] objects representing the weather forecast
     */
    suspend operator fun invoke(
        city: CityInfo,
        hours: Int,
        days: Int
    ): DataResult<List<WeatherTypeData>> {

        // Check if the city is valid
        if (!city.isValidCity()) {
            return DataResult.Failure(IllegalArgumentException("Invalid city or information"))
        }

        // Fetch all weather forecast information from the repository
        return when (val response =
            repository.fetchAllWeatherForecast(city, hours, days)) {
            is DataResult.Success -> {
                val output = mutableListOf<WeatherTypeData>()

                // Process the response data and create WeatherTypeModel objects
                response.data.forEach { entry ->
                    val type = entry.key
                    val weatherList = entry.value
                    val model = WeatherTypeData(type, weatherList)
                    output.add(model)
                }

                // Sort the output list based on the ordinal value of WeatherType
                output.sortBy {
                    it.type.ordinal
                }

                // Return the sorted list wrapped in a DataResult.Success
                DataResult.Success(output)
            }
            is DataResult.Failure -> {
                // Return the error response from the repository
                response
            }
        }
    }
}
