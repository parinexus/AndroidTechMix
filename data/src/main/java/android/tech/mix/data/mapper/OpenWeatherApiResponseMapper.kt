package android.tech.mix.data.mapper

import android.tech.mix.data.network.response.OpenWeatherApiResponse
import android.tech.mix.domain.mapper.Transformer
import android.tech.mix.domain.model.Notification
import android.tech.mix.domain.model.WeatherInfo
import javax.inject.Inject


/**
 * Mapper class responsible for converting the response from the OpenWeatherMap API to a list of Weather objects.
 */
class OpenWeatherApiResponseMapper @Inject constructor() :
    Transformer<OpenWeatherApiResponse, List<WeatherInfo>> {

    /**
     * Converts the OpenWeatherMap API response to a list of Weather objects.
     * @param fromObject The response from the OpenWeatherMap API.
     * @return A list of Weather objects converted from the API response.
     */
    override fun mapObject(fromObject: OpenWeatherApiResponse): List<WeatherInfo> {

        val currentWeather = fromObject.currentWeather

        return listOf(
            WeatherInfo(
                date = currentWeather.date,
                probability = "${currentWeather.pop * 100} %",
                lowTemperature = currentWeather.temperature,
                highTemperature = currentWeather.temperature,
                dayIcon = currentWeather.weatherImages[0].getDayThemeImageUrl(),
                nightIcon = currentWeather.weatherImages[0].getNightThemeImageUrl(),
                description = currentWeather.weatherImages[0].description,
                title = currentWeather.weatherImages[0].main,
                feelsLikeTemperature = currentWeather.feelsLikeTemperature,
                alerts = fromObject.alerts?.map {
                    Notification(
                        sender = it.senderName ?: "",
                        startTime = it.start ?: 0,
                        endTime = it.end ?: 0,
                        eventType = it.event ?: "",
                        details = it.description,
                        tagsList = it.tags,
                    )
                },
                visibility = currentWeather.visibility.toDouble(),
                dewPoint = currentWeather.dewPoint,
                uvIndex = currentWeather.uvIndex,
                pressure = currentWeather.pressure.toDouble(),
                humidity = currentWeather.humidity,
                windSpeed = currentWeather.windSpeed,
                precipitation = currentWeather.rain?.get("1h")?.asDouble ?: 0.0
            )
        )
    }
}