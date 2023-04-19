package edu.iest.sqliteejemplo.activities

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import edu.iest.sqliteejemplo.adapters.JuegosAdapter
import edu.iest.sqliteejemplo.db.ManejadorBaseDatos
import edu.iest.sqliteejemplo.interfaces.juegosInterface
import edu.iest.sqliteejemplo.modelos.Juego
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.iest.sqliteejemplo.R

class ListadoActivity : AppCompatActivity(), juegosInterface {

    private lateinit var listView: ListView
    private var listaDeJuegos = ArrayList<Juego>()
    private lateinit var fab: FloatingActionButton
    private val ORDENAR_POR_NOMBRE : String  = "nombre"
    val columnas = arrayOf("id", "nombre","precio", "consola" )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado)
        inicializarVistas()
        asignarEventos()
    }
    override fun onResume() {
        super.onResume()
        traerMisJuegos()
    }
    private fun inicializarVistas(){
        listView = findViewById(R.id.listView)
        fab = findViewById(R.id.fab)
    }

    private fun asignarEventos(){
        fab.setOnClickListener{
            val intent = Intent(this, AgregarActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_listado, menu)
        val searchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val manejador = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(manejador.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                buscarJuego("%" + p0 + "%")
                Toast.makeText(applicationContext, p0, Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if(TextUtils.isEmpty(p0)){
                    this.onQueryTextSubmit("");
                }
                return false
            }


        })
        return super.onCreateOptionsMenu(menu)
    }


    private fun traerMisJuegos() {
        val baseDatos = ManejadorBaseDatos(this)
        val cursor = baseDatos.traerTodos(columnas, ORDENAR_POR_NOMBRE)
        recorrerResultados( cursor)
        baseDatos.cerrarConexion()
    }

    @SuppressLint("Range")
    private fun buscarJuego(nombre: String) {
        val baseDatos = ManejadorBaseDatos(this)
        val camposATraer = arrayOf(nombre)
        val cursor = baseDatos.seleccionar(columnas,"nombre like ?", camposATraer, ORDENAR_POR_NOMBRE)
        recorrerResultados( cursor)
        baseDatos.cerrarConexion()
    }

    @SuppressLint("Range")
    fun recorrerResultados(cursor : Cursor){
        if(listaDeJuegos.size > 0)
            listaDeJuegos.clear()

        if(cursor.moveToFirst()){
            do{
                val juego_id = cursor.getInt(cursor.getColumnIndex("id"))
                val nombre = cursor.getString(cursor.getColumnIndex("nombre"))
                val precio = cursor.getFloat(cursor.getColumnIndex("precio"))
                val consola = cursor.getString(cursor.getColumnIndex("consola"))
                val juego: Juego
                juego = Juego(juego_id, nombre, precio, consola)
                listaDeJuegos.add(juego)
            }while(cursor.moveToNext())
        }
        val adapter: JuegosAdapter = JuegosAdapter(this, listaDeJuegos,this)
        listView.adapter = adapter

    }

    override fun juegoEliminado() {
        Log.d("PRUEBAS", "juegoEliminado")
        traerMisJuegos()
    }

    override fun editarJuego(juego: Juego) {
        Log.d("PRUEBAS", "editar Juego "+juego.id)
        val intent = Intent(this, EditarActivity::class.java)
        intent.putExtra("id",juego.id)
        intent.putExtra("nombre",juego.nombre)
        intent.putExtra("consola",juego.consola)
        startActivity(intent)
    }


}