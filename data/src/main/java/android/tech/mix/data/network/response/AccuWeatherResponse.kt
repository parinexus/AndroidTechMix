package android.tech.mix.data.network.response

import android.tech.mix.data.network.OpenWeatherApiService
import android.tech.mix.domain.model.CityInfo
import android.tech.mix.domain.model.WeatherForecastInfo
import android.tech.mix.domain.model.WeatherInfo
import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

val IMAGE_NUMBER_FORMATTER: DecimalFormat = DecimalFormat("00")

data class CityData(
    @SerializedName("Key")
    val key: String,
    @SerializedName("EnglishName")
    val name: String,
    @SerializedName("Rank")
    val rank: Int,
    @SerializedName("Country")
    val country: CountryData
) {
    fun toCity(): CityInfo {
        return CityInfo(
            name,
            key,
            rank,
            country.name
        )
    }
}

data class CountryData(
    @SerializedName("ID")
    val id: String,
    @SerializedName("EnglishName")
    val name: String
)

data class HeadlineData(
    @SerializedName("EffectiveDate")
    val effectiveDate: String,
    @SerializedName("Severity")
    val severity: Int,
    @SerializedName("Text")
    val status: String,
    @SerializedName("Category")
    val category: String,
    @SerializedName("Link")
    val link: String,
    @SerializedName("MobileLink")
    val mobileLink: String
)

data class TemperatureValueData(
    @SerializedName("Value")
    val value: Double,
    @SerializedName("UnitType")
    val unitType: Int,
    @SerializedName("Unit")
    val unit: String
)

data class TemperatureData(
    @SerializedName("Minimum")
    val minimum: TemperatureValueData,
    @SerializedName("Maximum")
    val maximum: TemperatureValueData
)

data class WeatherImageData(
    @SerializedName("Icon")
    val icon: Long,
    @SerializedName("IconPhrase")
    val description: String,
    @SerializedName("HasPrecipitation")
    val hasPrecipitation: Boolean,
    @SerializedName("PrecipitationType")
    val precipitationType: String?,
    @SerializedName("PrecipitationIntensity")
    val precipitationIntensity: String?,
    @SerializedName("PrecipitationProbability")
    val precipitationProbability: Double
) {
    fun getIconUrl(): String {
        return String.format(
            OpenWeatherApiService.BASE_IMAGE_URL,
            IMAGE_NUMBER_FORMATTER.format(icon)
        )
    }

    fun getPrecipitationProbabilityText(): String {
        return if (hasPrecipitation) "$precipitationProbability %" else "N/A"
    }
}

data class WeatherData(
    @SerializedName("Date")
    val date: String,
    @SerializedName("EpochDate")
    val dateTimeInMilliseconds: Long,
    @SerializedName("Temperature")
    val temperature: TemperatureData,
    @SerializedName("Day")
    val dayThemeIcon: WeatherImageData,
    @SerializedName("Night")
    val nightThemeIcon: WeatherImageData,
    @SerializedName("Link")
    val link: String,
    @SerializedName("MobileLink")
    val mobileLink: String
) {
    fun toWeatherModel(): WeatherInfo {
        return WeatherInfo(
            dateTimeInMilliseconds,
            dayThemeIcon.getPrecipitationProbabilityText(),
            temperature.minimum.value,
            temperature.maximum.value,
            dayThemeIcon.getIconUrl(),
            nightThemeIcon.getIconUrl(),
            dayThemeIcon.description
        )
    }
}

data class DailyWeatherForecastResponse(
    @SerializedName("Headline")
    val headline: HeadlineData,
    @SerializedName("DailyForecasts")
    val forecastList: List<WeatherData>
) {
    fun toWeatherForecastModel(): WeatherForecastInfo {
        return WeatherForecastInfo(
            headlines = headline.status,
            weatherForecasts = forecastList.map { it.toWeatherModel() }
        )
    }
}

data class HourlyWeatherForecastResponse(
    @SerializedName("DateTime")
    val dateTime: String,
    @SerializedName("EpochDateTime")
    val epocTime: Long,
    @SerializedName("WeatherIcon")
    val weatherIcon: Long,
    @SerializedName("IconPhrase")
    val iconStatus: String,
    @SerializedName("HasPrecipitation")
    val hasPrecipitation: Boolean,
    @SerializedName("PrecipitationType")
    val precipitationType: String,
    @SerializedName("PrecipitationIntensity")
    val precipitationIntensity: String,
    @SerializedName("PrecipitationProbability")
    val precipitationProbability: Double,
    @SerializedName("IsDaylight")
    val isDaylight: Boolean,
    @SerializedName("Temperature")
    val temperature: TemperatureValueData,
    @SerializedName("Link")
    val link: String,
    @SerializedName("MobileLink")
    val mobileLink: String
) {
    private fun getIconUrl(): String {
        return String.format(
            OpenWeatherApiService.BASE_IMAGE_URL,
            IMAGE_NUMBER_FORMATTER.format(weatherIcon)
        )
    }

    private fun getPrecipitationProbabilityText(): String {
        return if (hasPrecipitation) "$precipitationProbability %" else "N/A"
    }

    fun toWeatherModel(): WeatherInfo {
        return WeatherInfo(
            epocTime,
            getPrecipitationProbabilityText(),
            temperature.value,
            temperature.value,
            getIconUrl(),
            getIconUrl(),
            iconStatus
        )
    }
}