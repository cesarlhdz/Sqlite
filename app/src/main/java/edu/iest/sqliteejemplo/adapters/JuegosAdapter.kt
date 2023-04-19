package edu.iest.sqliteejemplo.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import edu.iest.sqliteejemplo.db.ManejadorBaseDatos
import edu.iest.sqliteejemplo.interfaces.juegosInterface
import edu.iest.sqliteejemplo.modelos.Juego
import edu.iest.sqliteejemplo.R


class JuegosAdapter(contexto: Context, var listadDejuegos: ArrayList<Juego>, juegoInterface: juegosInterface) : BaseAdapter() {

    var contexto: Context? = contexto
    var juegoInterface: juegosInterface? = juegoInterface
    override fun getCount(): Int {
        return listadDejuegos.size
    }

    override fun getItem(p0: Int): Any {
        return listadDejuegos[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        //usar vista reciclado para eficientar
        var convertView: View?= p1
        if(convertView == null){
            convertView = View.inflate(contexto, R.layout.row_juego, null)
        }

        val juego = listadDejuegos[p0]

        val miVista = convertView!!
        val tvTitle: TextView = miVista.findViewById(R.id.etTitle)
        val tvContent: TextView = miVista.findViewById(R.id.tvContent)
        val img01: ImageView = miVista.findViewById(R.id.img01)
        val img02: ImageView = miVista.findViewById(R.id.img02)
        tvTitle.text = juego.nombre
        tvContent.text = juego.consola
        //borrar
        img02.setOnClickListener(){
            //eliminar
            val baseDatos = ManejadorBaseDatos(this.contexto!!)
            val argumentosWhere = arrayOf(juego.id.toString())
            baseDatos.eliminar("id = ? and title = ? ", argumentosWhere)
            juegoInterface?.juegoEliminado()
        }

        img01.setOnClickListener(){
            //Editar
            juegoInterface?.editarJuego(juego)
        }

        return miVista
    }

}
