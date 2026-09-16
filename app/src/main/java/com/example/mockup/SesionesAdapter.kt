package com.example.mockup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SesionesAdapter(
    private var sesiones: List<SesionUso>
) : RecyclerView.Adapter<SesionesAdapter.SesionViewHolder>() {

    class SesionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFecha: TextView = itemView.findViewById(R.id.tv_sesion_fecha)
        val tvHorario: TextView = itemView.findViewById(R.id.tv_sesion_horario)
        val tvOido: TextView = itemView.findViewById(R.id.tv_sesion_oido)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SesionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sesion, parent, false)
        return SesionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SesionViewHolder, position: Int) {
        val sesion = sesiones[position]
        holder.tvFecha.text = sesion.fecha
        holder.tvHorario.text = "Inicio: ${sesion.horaInicio}  •  Fin: ${sesion.horaFin}"
        holder.tvOido.text = sesion.oido
    }

    override fun getItemCount(): Int = sesiones.size

    fun actualizarLista(nuevaLista: List<SesionUso>) {
        sesiones = nuevaLista
        notifyDataSetChanged()
    }
}
