package com.example.mockup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SesionesAdapter(
    private val sesiones: List<SesionUso>
) : RecyclerView.Adapter<SesionesAdapter.SesionViewHolder>() {

    class SesionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tv_session_date)
        val tvTime: TextView = itemView.findViewById(R.id.tv_session_time)
        val tvEar: TextView = itemView.findViewById(R.id.tv_session_ear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SesionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sesion, parent, false)
        return SesionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SesionViewHolder, position: Int) {
        val sesion = sesiones[position]
        holder.tvDate.text = sesion.fecha
        holder.tvTime.text = holder.itemView.context.getString(
            R.string.history_session_time,
            sesion.horaInicio,
            sesion.horaFin
        )
        holder.tvEar.text = sesion.oido
    }

    override fun getItemCount(): Int = sesiones.size
}
