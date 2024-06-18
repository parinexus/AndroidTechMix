package android.tech.mix.data.mapper

import android.tech.mix.data.network.response.OpenWeatherApiResponse
import android.tech.mix.domain.mapper.Transformer
import android.tech.mix.domain.model.WeatherInfo
import javax.inject.Inject

class DailyWeatherMapper @Inject constructor() :
    Transformer<OpenWeatherApiResponse, List<WeatherInfo>> {
    override fun mapObject(response: OpenWeatherApiResponse): List<WeatherInfo> {
        return response.dailyWeather?.map {
            WeatherInfo(
                date = it.date,
                probability = "${it.pop * 100} %",
                lowTemperature = it.temperature.min ?: 0.0,
                highTemperature = it.temperature.max ?: 0.0,
                dayIcon = it.weatherImages[0].getDayThemeImageUrl(),
                nightIcon = it.weatherImages[0].getNightThemeImageUrl(),
                description = it.weatherImages[0].description,
                title = it.weatherImages[0].main,
                feelsLikeTemperature = it.feelsLikeTemperature.max ?: 0.0
            )
        } ?: emptyList()
    }
}
