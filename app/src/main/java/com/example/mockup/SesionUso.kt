package com.example.mockup

import org.json.JSONArray
import org.json.JSONObject

data class SesionUso(
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val oido: String
) {
    fun toJson(): JSONObject {
        val obj = JSONObject()
        obj.put("fecha", fecha)
        obj.put("horaInicio", horaInicio)
        obj.put("horaFin", horaFin)
        obj.put("oido", oido)
        return obj
    }

    companion object {
        fun fromJson(obj: JSONObject): SesionUso {
            return SesionUso(
                fecha = obj.optString("fecha"),
                horaInicio = obj.optString("horaInicio"),
                horaFin = obj.optString("horaFin"),
                oido = obj.optString("oido")
            )
        }

        fun listToJson(sesiones: List<SesionUso>): JSONArray {
            val array = JSONArray()
            sesiones.forEach { array.put(it.toJson()) }
            return array
        }

        fun listFromJson(array: JSONArray): List<SesionUso> {
            val lista = mutableListOf<SesionUso>()
            for (i in 0 until array.length()) {
                lista.add(fromJson(array.getJSONObject(i)))
            }
            return lista
        }
    }
}
