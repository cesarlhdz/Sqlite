package edu.iest.sqliteejemplo.activities

import android.annotation.SuppressLint
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import edu.iest.sqliteejemplo.db.ManejadorBaseDatos
import edu.iest.sqliteejemplo.modelos.Juego
import com.google.android.material.snackbar.Snackbar
import edu.iest.sqliteejemplo.R

class EditarActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var bnGuardar: Button
    private lateinit var etJuego: EditText
    private lateinit var etPrecio: EditText
    private lateinit var spConsola: Spinner
    private val consolas = arrayOf("Xbox", "Nintendo", "Playstation", "MultiPlataforma", "P.C")
    private var consolaSeleccionada: String = ""
    private lateinit var tvJuego: TextView
    var juego: Juego? = null
    var id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar)
        //  setSupportActionBar(toolbar)
        getSupportActionBar()?.title = "Edición"
        getSupportActionBar()?.setHomeButtonEnabled(true);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        inicializarVistas()
        id = intent.getIntExtra("id", 0)
        buscarJuego(id)
        poblarCampos()
    }

    private fun poblarCampos() {
        etJuego.setText(juego?.nombre)
        etPrecio.setText(juego?.precio.toString())
        val position = consolas.indexOf(juego?.consola)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, consolas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spConsola.adapter = adapter
        spConsola.onItemSelectedListener = this
        if (position >= 0) {
            spConsola.setSelection(position)
            consolaSeleccionada = consolas[position]
        }
    }

    private fun inicializarVistas() {
        etJuego = findViewById(R.id.etJuego)
        bnGuardar = findViewById(R.id.bnGuardar)
        etPrecio = findViewById(R.id.etPrecio)
        spConsola = findViewById(R.id.spConsola)
        tvJuego = findViewById(R.id.tvJuego)
        bnGuardar.setOnClickListener {
            var precio_actual: Float
            precio_actual = etPrecio.text.toString().toFloat()
            actualizarJuego(etJuego.text.toString(), precio_actual, consolaSeleccionada)
        }
    }

    val columnaNombreJuego = "nombre"
    val columnaPrecio = "precio"
    val columnaConsola = "consola"

    private fun actualizarJuego(nombreJuego: String, precio: Float, consola: String) {
        if (!TextUtils.isEmpty(consola)) {
            val baseDatos = ManejadorBaseDatos(this)
            val contenido = ContentValues()
            contenido.put("nombre", "zelda")
            contenido.put("precio", 123)
            contenido.put("consola", "nintedo")
            if ( id > 0) {
                val argumentosWhere = arrayOf(id.toString())
                val id_actualizado = baseDatos.actualizar(contenido, "id = ?", argumentosWhere)
                if (id_actualizado > 0) {
                    Snackbar.make(etJuego, "Juego actualizado", Snackbar.LENGTH_LONG).show()
                } else {
                    val alerta = AlertDialog.Builder(this)
                    alerta.setTitle("Atención")
                        .setMessage("No fue posible actualizarlo")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar") { dialog, which ->

                        }
                        .show()
                }
            } else {
                Toast.makeText(this, "no hiciste id", Toast.LENGTH_LONG).show()
            }
            baseDatos.cerrarConexion()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("Range")
    private fun buscarJuego(idJuego: Int) {

        if (idJuego > 0) {
            val baseDatos = ManejadorBaseDatos(this)
            val columnasATraer = arrayOf("id", "nombre", "precio", "consola")
            val condicion = " id = ?"
            val argumentos = arrayOf(idJuego.toString())
            val ordenarPor = "id"
            val cursor = baseDatos.seleccionar(columnasATraer, condicion, argumentos, ordenarPor)

            if (cursor.moveToFirst()) {
                do {
                    val juego_id = cursor.getInt(cursor.getColumnIndex("id"))
                    val nombre = cursor.getString(cursor.getColumnIndex("nombre"))
                    val precio = cursor.getFloat(cursor.getColumnIndex("precio"))
                    val consola = cursor.getString(cursor.getColumnIndex("consola"))
                    juego = Juego(juego_id, nombre, precio, consola)
                } while (cursor.moveToNext())
            }
            baseDatos.cerrarConexion()
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        consolaSeleccionada = consolas[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}