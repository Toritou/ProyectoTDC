package com.example.araucaweathers

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private val serverUrl = "http://54.236.89.141:8081/sensor/data/latest"

    // Referencias a los TextViews en el diseño
    private lateinit var textTitle: TextView
    private lateinit var textSubtitle: TextView
    private lateinit var textAirQualityIndex: TextView
    private lateinit var textStatus: TextView
    private lateinit var textDescription: TextView
    private lateinit var textPm25: TextView
    private lateinit var textPm10: TextView
    private lateinit var textPm100: TextView
    private lateinit var textPm25Env: TextView
    private lateinit var textPm10Env: TextView
    private lateinit var textPm100Env: TextView
    private lateinit var btnUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enlazar los elementos del diseño
        textTitle = findViewById(R.id.text_title)
        textSubtitle = findViewById(R.id.text_subtitle)
        textAirQualityIndex = findViewById(R.id.text_air_quality_index)
        textStatus = findViewById(R.id.text_status)
        textDescription = findViewById(R.id.text_description)
        textPm25 = findViewById(R.id.text_pm25)
        textPm10 = findViewById(R.id.text_pm10)
        textPm100 = findViewById(R.id.text_pm100)
        textPm25Env = findViewById(R.id.text_pm25_env)
        textPm10Env = findViewById(R.id.text_pm10_env)
        textPm100Env = findViewById(R.id.text_pm100_env)
        btnUpdate = findViewById(R.id.btn_update)

        // Cargar datos al iniciar la actividad
        loadSensorData()

        // Acción del botón para actualizar los datos
        btnUpdate.setOnClickListener {
            loadSensorData()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadSensorData() {
        val request = Request.Builder().url(serverUrl).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error al cargar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseBody = response.body?.string()
                Log.d("ServerResponse", responseBody ?: "Respuesta vacía")

                if (responseBody.isNullOrEmpty()) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Respuesta vacía del servidor", Toast.LENGTH_SHORT).show()
                    }
                    return
                }

                try {
                    val jsonData = JSONObject(responseBody)
                    runOnUiThread {
                        try {
                            // Extraer datos del JSON
                            val timestamp = jsonData.optString("timestamp", "--")
                            val pm25 = jsonData.optDouble("pm25_standard", -1.0)
                            val pm10 = jsonData.optDouble("pm10_standard", -1.0)
                            val pm100 = jsonData.optDouble("pm100_standard", -1.0)
                            val pm25Env = jsonData.optDouble("pm25_env", -1.0)
                            val pm10Env = jsonData.optDouble("pm10_env", -1.0)
                            val pm100Env = jsonData.optDouble("pm100_env", -1.0)

                            // Validación de datos
                            if (pm25 == -1.0 || pm10 == -1.0 || pm100 == -1.0) {
                                Toast.makeText(this@MainActivity, "Datos incompletos del servidor", Toast.LENGTH_SHORT).show()
                                return@runOnUiThread
                            }

                            // Actualizar UI
                            textSubtitle.text = "Publicado a las $timestamp"
                            val airQualityIndex = (pm25 + pm10 + pm100).toInt() / 3
                            textAirQualityIndex.text = airQualityIndex.toString()

                            when {
                                airQualityIndex <= 50 -> {
                                    textStatus.text = "Buena"
                                    textStatus.setTextColor(getColor(R.color.green))
                                    textDescription.text = "La calidad del aire es buena. ¡Un día perfecto para salir a caminar!"
                                }
                                airQualityIndex in 51..100 -> {
                                    textStatus.text = "Moderada"
                                    textStatus.setTextColor(getColor(R.color.yellow))
                                    textDescription.text = "La calidad del aire es moderada. Puede haber molestias leves."
                                }
                                else -> {
                                    textStatus.text = "Mala"
                                    textStatus.setTextColor(getColor(R.color.red))
                                    textDescription.text = "La calidad del aire es mala. Limita las actividades al aire libre."
                                }
                            }

                            textPm25.text = String.format("%.1f", pm25)
                            textPm10.text = String.format("%.1f", pm10)
                            textPm100.text = String.format("%.1f", pm100)
                            textPm25Env.text = String.format("%.1f", pm25Env)
                            textPm10Env.text = String.format("%.1f", pm10Env)
                            textPm100Env.text = String.format("%.1f", pm100Env)
                        } catch (e: Exception) {
                            Toast.makeText(this@MainActivity, "Error al procesar los datos", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Error al interpretar la respuesta JSON: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
