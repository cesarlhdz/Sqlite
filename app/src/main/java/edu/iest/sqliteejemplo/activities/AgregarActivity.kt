package edu.iest.sqliteejemplo.activities

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import edu.iest.sqliteejemplo.db.ManejadorBaseDatos
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import edu.iest.sqliteejemplo.R

class AgregarActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private  lateinit var fabAgregar: FloatingActionButton
    private  lateinit var etJuego: EditText
    private  lateinit var etPrecio: EditText
    private  lateinit var spConsola: Spinner
    private val consolas = arrayOf("Xbox", "Nintendo", "Playstation", "MultiPlataforma", "P.C")
    private var consolaSeleccionada: String = ""
    private  lateinit var tvJuego: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)
        inicializarVistas()

        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, consolas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spConsola.adapter = adapter
        spConsola.onItemSelectedListener = this
        fabAgregar.setOnClickListener{
            insertarJuego( etJuego.text.toString(),  etPrecio.text.toString().toFloat(),consolaSeleccionada)
        }
    }

    val columnaID = "id"
    val columnaNombreJuego = "nombre"
    val columnaPrecio = "precio"
    val columnaConsola = "consola"
    var id: Int = 0
    private fun insertarJuego(nombreJuego: String, precio: Float, consola: String){
       if(!TextUtils.isEmpty(consola)) {
           val baseDatos = ManejadorBaseDatos(this)
           //  val columnas = arrayOf(columnaID, columnaNombreJuego, columnaPrecio, columnaConsola)
           val contenido = ContentValues()
           contenido.put(columnaNombreJuego, nombreJuego)
           contenido.put(columnaPrecio, precio)
           contenido.put(columnaConsola, consola)
           //guardar imagen
            id = baseDatos.insertar(contenido).toInt()
           if (id > 0) {
               Toast.makeText(this, "juego " + nombreJuego + " agregado", Toast.LENGTH_LONG).show()
               finish()
           } else
               Toast.makeText(this, "Ups no se pudo guardar el juego", Toast.LENGTH_LONG).show()
           baseDatos.cerrarConexion()
       }else{
           Snackbar.make(tvJuego,"Favor seleccionar una consola", 0).show()
       }
    }

    private fun inicializarVistas(){
        etJuego = findViewById(R.id.etJuego)
        fabAgregar = findViewById(R.id.fabAgregar)
        etPrecio = findViewById(R.id.etPrecio)
        spConsola = findViewById(R.id.spConsola)
        tvJuego = findViewById(R.id.tvJuego)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
        consolaSeleccionada = consolas[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }



}