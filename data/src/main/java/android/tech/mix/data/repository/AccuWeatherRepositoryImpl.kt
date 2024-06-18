package android.tech.mix.data.repository

import android.tech.mix.data.BuildConfig
import android.tech.mix.data.network.AccuWeatherApiService
import android.tech.mix.domain.model.*
import android.tech.mix.domain.repository.WeatherRepository
import android.tech.mix.domain.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class AccuWeatherRepositoryImpl(
    private val service: AccuWeatherApiService,
    private val dispatcher: AppCoroutineDispatchers
) : WeatherRepository {

    override suspend fun fetchCityInfo(cityName: String): DataResult<List<CityInfo>> =
        withContext(dispatcher.ioDispatcher) {
            return@withContext try {
                val response = service.fetchCities(
                    cityName = cityName,
                    apiKey = BuildConfig.ACC_WEATHER_API_KEY
                ).awaitResponse()
                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    DataResult.Success(response.body()!!.map { it.toCity() })
                } else {
                    DataResult.Failure(KotlinNullPointerException())
                }
            } catch (e: Exception) {
                DataResult.Failure(e)
            }
        }

    override suspend fun fetchHourlyWeatherForecast(
        city: CityInfo,
        numberOfHours: Int
    ): DataResult<WeatherForecastInfo> = withContext(dispatcher.ioDispatcher) {
        return@withContext try {
            val response = service.fetchHourlyForecastByCityId(
                cityId = city.cityKey,
                appKey = BuildConfig.ACC_WEATHER_API_KEY,
                numberOfHours = "${numberOfHours}hour"
            ).awaitResponse()
            if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                DataResult.Success(
                    WeatherForecastInfo(
                        headlines = "",
                        weatherForecasts = response.body()!!.map { it.toWeatherModel() }
                    )
                )
            } else {
                DataResult.Failure(KotlinNullPointerException())
            }
        } catch (e: Exception) {
            DataResult.Failure(e)
        }
    }

    override suspend fun fetchDailyWeatherForecast(
        city: CityInfo,
        numberOfDays: Int
    ): DataResult<WeatherForecastInfo> = withContext(dispatcher.ioDispatcher) {
        return@withContext try {
            val response = service.fetchDailyForecastByCityId(
                cityId = city.cityKey,
                appKey = BuildConfig.ACC_WEATHER_API_KEY,
                numberOfDays = "${numberOfDays}day"
            ).awaitResponse()
            if (response.isSuccessful && response.body() != null) {
                DataResult.Success(response.body()!!.toWeatherForecastModel())
            } else {
                DataResult.Failure(KotlinNullPointerException())
            }
        } catch (e: Exception) {
            DataResult.Failure(e)
        }
    }

    override suspend fun fetchAllWeatherForecast(
        city: CityInfo,
        numberOfHours: Int,
        numberOfDays: Int
    ): DataResult<Map<WeatherDataType, List<WeatherInfo>>> {
        TODO("Not yet implemented")
    }
}