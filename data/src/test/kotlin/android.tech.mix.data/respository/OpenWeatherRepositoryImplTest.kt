package android.tech.mix.data.respository

import com.google.common.truth.Truth.assertThat
import android.tech.mix.core.testing.CoroutineTest
import android.tech.mix.core.testing.ResponseFileReader
import android.tech.mix.core.testing.utils.DispatcherRule
import android.tech.mix.data.mapper.CurWeatherDataMapper
import android.tech.mix.data.mapper.DailyWeatherMapper
import android.tech.mix.data.mapper.HourWeatherMapper
import android.tech.mix.data.network.OpenWeatherApiService
import android.tech.mix.data.repository.OpenWeatherRepositoryImpl
import android.tech.mix.domain.model.CityInfo
import android.tech.mix.domain.model.DataResult
import android.tech.mix.domain.model.WeatherDataType
import android.tech.mix.domain.repository.WeatherRepository
import android.tech.mix.domain.util.AppCoroutineDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class OpenWeatherRepositoryImplTest : CoroutineTest() {

    private lateinit var mockWebServer: MockWebServer

    private lateinit var weatherRepository: WeatherRepository

    @get:Rule
    val dispatcherRule = DispatcherRule()

    override fun initialize() {
        super.initialize()

        mockWebServer = MockWebServer()
        mockWebServer.start(8080)

        val openWeatherMapApiService: OpenWeatherApiService = Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://127.0.0.1:8080")
            .build()
            .create()

        weatherRepository = OpenWeatherRepositoryImpl(
            openWeatherMapApiService,
            CurWeatherDataMapper(),
            HourWeatherMapper(),
            DailyWeatherMapper(),
            AppCoroutineDispatchers(
                dispatcherRule.dispatcher,
                dispatcherRule.dispatcher,
                dispatcherRule.dispatcher
            )
        )
    }

    override fun cleanup() {
        super.cleanup()
        mockWebServer.shutdown()
    }

    private fun setupMockResponse(response: MockResponse) {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return response
            }
        }
    }

    @Test
    fun `read weather success json response file`() {
        val reader =
            ResponseFileReader(SUCCESS_RESPONSE_STRUCTURE_FILE_NAME)
        assertThat(reader.content).isNotNull()
    }

    @Test
    fun `Search weather information with empty result`() = runTest {

        setupMockResponse(
            MockResponse()
                .setResponseCode(200)
                .setBody(
                    ResponseFileReader(
                        SUCCESS_EMPTY_RESPONSE_STRUCTURE_FILE_NAME
                    ).content
                )
        )

        val availableWeatherInfo = weatherRepository.fetchAllWeatherForecast(
            FAKE_CITY,
            NUMBER_OF_HOURS,
            NUMBER_OF_DAYS
        )

        assertThat(availableWeatherInfo).isInstanceOf(DataResult.Failure::class.java)

        val errorResponse = availableWeatherInfo as DataResult.Failure
        assertThat(errorResponse).isNotNull()
        assertThat(errorResponse.exception).isNotNull()
    }

    @Test
    fun `Search weather information with valid result`(): Unit = runTest {

        setupMockResponse(
            MockResponse()
                .setResponseCode(200)
                .setBody(
                    ResponseFileReader(
                        SUCCESS_RESPONSE_STRUCTURE_FILE_NAME
                    ).content
                )
        )

        val availableWeatherInfo = weatherRepository.fetchAllWeatherForecast(
            FAKE_CITY,
            NUMBER_OF_HOURS,
            NUMBER_OF_DAYS
        )
        assertThat(availableWeatherInfo).isInstanceOf(DataResult.Success::class.java)

        val successResponse = availableWeatherInfo as DataResult.Success
        assertThat(successResponse).isNotNull()
        assertThat(successResponse.data).isNotEmpty()

        val weatherInfo = successResponse.data[WeatherDataType.CURRENT]
        assertThat(weatherInfo).isNotNull()
        assertThat(weatherInfo).isNotEmpty()
        val currentWeather = weatherInfo!![0]
        assertThat(currentWeather).isNotNull()
        assertThat(currentWeather.title).isNotNull()
        assertThat(currentWeather.date).isNotEqualTo(0)
    }

    @Test
    fun `Search weather information with error result`() = runTest {

        setupMockResponse(
            MockResponse()
                .setResponseCode(400)
                .setBody(
                    ResponseFileReader(
                        ERROR_RESPONSE_STRUCTURE_FILE_NAME
                    ).content
                )
        )

        val availableWeatherInfo = weatherRepository.fetchAllWeatherForecast(
            FAKE_CITY,
            NUMBER_OF_HOURS,
            NUMBER_OF_DAYS
        )
        assertThat(availableWeatherInfo).isInstanceOf(DataResult.Failure::class.java)
        val error = availableWeatherInfo as DataResult.Failure
        assertThat(error).isNotNull()
        assertThat(error.exception).isNotNull()
    }

    companion object {
        private const val SUCCESS_RESPONSE_STRUCTURE_FILE_NAME: String = "valid_response_structure.json"
        private const val SUCCESS_EMPTY_RESPONSE_STRUCTURE_FILE_NAME: String = "valid_empty_structure.json"
        private const val ERROR_RESPONSE_STRUCTURE_FILE_NAME: String = "valid_error_structure.json"

        private val FAKE_CITY = CityInfo(
            "London",
            "1",
            1,
            "England",
            longitude = 0.0,
            latitude = 0.0
        )

        private const val NUMBER_OF_HOURS = 48
        private const val NUMBER_OF_DAYS = 7
    }
}