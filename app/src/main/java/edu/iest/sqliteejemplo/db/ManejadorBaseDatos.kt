package edu.iest.sqliteejemplo.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder

class ManejadorBaseDatos {

    val nombreBaseDatos = "MisJuegos"
    val tablaJuegos = "juegos"
    val columnaID = "id"
    val columnaNombreJuego = "nombre"
    val columnaPrecio = "precio"
    val columnaConsola = "consola"

    val versionDB = 1

    val creacionTablaJuegos = "CREATE TABLE IF NOT EXISTS "+tablaJuegos +
            "(  " + columnaID + " INTEGER PRIMARY KEY AUTOINCREMENT," + //nombre columna y tipo de dato
            "  " + columnaNombreJuego + " TEXT NOT NULL," +
            "  " + columnaPrecio + " REAL," +
            "  " + columnaConsola + " TEXT)"

    var misQuerys: SQLiteDatabase

    constructor(contexto: Context){
        val baseDatos = MiDBHelper(contexto)
         misQuerys = baseDatos.writableDatabase
    }

    ///clase para crear o actualizar los tipos de campos de las tablas de la base de datos
    inner class MiDBHelper(contexto: Context): SQLiteOpenHelper(contexto, nombreBaseDatos, null, versionDB){
        override fun onCreate(p0: SQLiteDatabase?) {
           //aqui crearmos nuestras tablas de la db
            if (p0 != null) {
                p0.execSQL(creacionTablaJuegos)
            }//queries de creación
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
          //para realizar migraciones
            p0?.execSQL("DROP TABLE IF EXISTS "+tablaJuegos)
        }
    }

    fun insertar(values: ContentValues): Long{
        return misQuerys.insert(tablaJuegos, null, values)
    }

    fun actualizar(values:ContentValues, clausulaWhere: String, argumentosWhere: Array<String>): Int{
        return misQuerys.update(tablaJuegos,values,clausulaWhere, argumentosWhere )
    }

    fun eliminar( clausulaWhere: String, argumentosWhere: Array<String>): Int {
        return misQuerys.delete(tablaJuegos, clausulaWhere, argumentosWhere)
        //clausula
    }
    fun seleccionar(columnasATraer: Array<String>, condiciones: String, argumentos: Array<String>, ordenarPor: String ): Cursor {
        val groupBy:String? = null
        val having:String? = null
       // val consulta = SQLiteQueryBuilder()
       // consulta.tables = tablaJuegos ASCM DESC
        val cursor =  misQuerys.query(tablaJuegos, columnasATraer,condiciones,argumentos, groupBy, having, ordenarPor)
        return cursor
    }

    fun traerTodos(columnasATraer: Array<String>, ordenarPor: String ): Cursor {
        val groupBy:String? = null
        val having:String? = null
        // val consulta = SQLiteQueryBuilder()
        // consulta.tables = tablaJuegos
        val cursor =  misQuerys.query(tablaJuegos, columnasATraer,null,null, groupBy, having, ordenarPor)
        return cursor
    }

    fun cerrarConexion(){
        misQuerys.close()
    }

}
