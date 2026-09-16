package com.example.mockup

import android.util.Log
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/** Datos del clima que se muestran en Home. */
data class WeatherData(
    val temperatura: Double,
    val humedad: Int,
    val codigoClima: Int,
    val viento: Double
)

/**
 * Petición GET a Open-Meteo para el clima actual de Neiva, Huila.
 *
 * obtenerClima() hace trabajo de red, así que solo debe llamarse desde un hilo
 * secundario (HomeActivity lo hace con un Thread).
 */
object WeatherApi {

    private const val TAG = "WeatherApi"

    // Neiva, Huila, Colombia (ubicación fija por ahora)
    private const val URL_CLIMA =
        "https://api.open-meteo.com/v1/forecast" +
            "?latitude=2.9273&longitude=-75.2819" +
            "&current=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m"

    /** Devuelve el clima actual, o null si falla la conexión o la lectura del JSON. */
    fun obtenerClima(): WeatherData? {
        var conexion: HttpURLConnection? = null
        return try {
            val url = URL(URL_CLIMA)
            conexion = url.openConnection() as HttpURLConnection
            conexion.requestMethod = "GET"
            conexion.connectTimeout = 8000
            conexion.readTimeout = 8000

            if (conexion.responseCode != 200) {
                Log.e(TAG, "Open-Meteo respondió ${conexion.responseCode}")
                return null
            }

            val json = conexion.inputStream.bufferedReader().use { it.readText() }
            val current = JSONObject(json).getJSONObject("current")

            WeatherData(
                temperatura = current.getDouble("temperature_2m"),
                humedad = current.getInt("relative_humidity_2m"),
                codigoClima = current.getInt("weather_code"),
                viento = current.getDouble("wind_speed_10m")
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error al consultar el clima", e)
            null
        } finally {
            conexion?.disconnect()
        }
    }

    /** Convierte el weather_code (escala WMO) en un texto sencillo para el usuario. */
    fun descripcionClima(codigo: Int): Int = when (codigo) {
        0 -> R.string.weather_clear
        1, 2 -> R.string.weather_partly_cloudy
        3 -> R.string.weather_overcast
        45, 48 -> R.string.weather_fog
        51, 53, 55 -> R.string.weather_drizzle
        61, 63, 65 -> R.string.weather_rain
        66, 67 -> R.string.weather_freezing_rain
        71, 73, 75, 77 -> R.string.weather_snow
        80, 81, 82 -> R.string.weather_showers
        85, 86 -> R.string.weather_snow_showers
        95 -> R.string.weather_thunderstorm
        96, 99 -> R.string.weather_thunderstorm_hail
        else -> R.string.weather_unknown
    }
}
