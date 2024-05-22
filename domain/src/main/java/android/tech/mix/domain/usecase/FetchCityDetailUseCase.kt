package android.tech.mix.domain.usecase

import android.tech.mix.domain.model.CityInfo
import android.tech.mix.domain.model.DataResult
import android.tech.mix.domain.repository.WeatherRepository

class FetchCityDetailsUseCase(private val weatherRepo: WeatherRepository) {

    suspend operator fun invoke(cityName: String): DataResult<List<CityInfo>> {

        if (cityName.isBlank()) {
            return DataResult.Failure(KotlinNullPointerException("Invalid city name or information"))
        }

        return weatherRepo.fetchCityInfo(cityName)
    }
}
