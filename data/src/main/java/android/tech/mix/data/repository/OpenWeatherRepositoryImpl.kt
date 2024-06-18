package android.tech.mix.data.repository

import android.tech.mix.data.BuildConfig
import android.tech.mix.data.mapper.CurWeatherDataMapper
import android.tech.mix.data.mapper.DailyWeatherMapper
import android.tech.mix.data.mapper.HourWeatherMapper
import android.tech.mix.data.network.OpenWeatherApiService
import android.tech.mix.domain.model.CityInfo
import android.tech.mix.domain.model.DataResult
import android.tech.mix.domain.model.WeatherDataType
import android.tech.mix.domain.model.WeatherForecastInfo
import android.tech.mix.domain.model.WeatherInfo
import android.tech.mix.domain.repository.WeatherRepository
import android.tech.mix.domain.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class OpenWeatherRepositoryImpl(
    private val service: OpenWeatherApiService,
    private val curWeatherDataMapper: CurWeatherDataMapper,
    private val hourWeatherMapper: HourWeatherMapper,
    private val dailyWeatherMapper: DailyWeatherMapper,
    private val dispatcher: AppCoroutineDispatchers
) : WeatherRepository {

    override suspend fun fetchCityInfo(cityName: String): DataResult<List<CityInfo>> =
        withContext(dispatcher.ioDispatcher) {
            return@withContext try {
                val response = service.fetchCities(
                    cityName = cityName,
                    apiKey = BuildConfig.OPEN_WEATHER_API_KEY
                ).awaitResponse()
                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    DataResult.Success(response.body()!!.map { it.toCityModel() })
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
            val response = service.fetchCurrentAndForecastData(
                apiKey = BuildConfig.OPEN_WEATHER_API_KEY,
                latitude = city.latitude!!,
                longitude = city.longitude!!
            ).awaitResponse()
            if (response.isSuccessful && response.body() != null) {
                DataResult.Success(
                    WeatherForecastInfo(
                        headlines = "",
                        weatherForecasts = hourWeatherMapper.mapObject(response.body()!!)
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
            val response = service.fetchCurrentAndForecastData(
                apiKey = BuildConfig.OPEN_WEATHER_API_KEY,
                latitude = city.latitude!!,
                longitude = city.longitude!!
            ).awaitResponse()
            if (response.isSuccessful && response.body() != null) {
                DataResult.Success(
                    WeatherForecastInfo(
                        headlines = "",
                        weatherForecasts = dailyWeatherMapper.mapObject(response.body()!!)
                    )
                )
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
    ): DataResult<Map<WeatherDataType, List<WeatherInfo>>> = withContext(dispatcher.ioDispatcher) {
        return@withContext try {
            val response = service.fetchCurrentAndForecastData(
                apiKey = BuildConfig.OPEN_WEATHER_API_KEY,
                latitude = city.latitude!!,
                longitude = city.longitude!!
            ).awaitResponse()
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                DataResult.Success(
                    hashMapOf(
                        WeatherDataType.CURRENT to curWeatherDataMapper.mapObject(body),
                        WeatherDataType.HOURLY to hourWeatherMapper.mapObject(body),
                        WeatherDataType.DAILY to dailyWeatherMapper.mapObject(body)
                    )
                )
            } else {
                DataResult.Failure(KotlinNullPointerException())
            }
        } catch (e: Exception) {
            DataResult.Failure(e)
        }
    }
}
