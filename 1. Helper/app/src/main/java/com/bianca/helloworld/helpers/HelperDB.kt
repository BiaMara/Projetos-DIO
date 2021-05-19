package com.bianca.helloworld.helpers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HelperDB(
    context: Context
)  : SQLiteOpenHelper (context, NOME_BANCO, null, VERSAO_ATUAL) {

   companion object {
       private val NOME_BANCO = "contato.db"
       private val VERSAO_ATUAL = 1
   }

    val TABLE_NAME = "contato"
    val COLUMNS_ID = "id"
    val COLUMNS_NOME = "nome"
    val COLUMNS_TELEFONE = "telefone"
    val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    val CREAT_TABLE = "CREATE TABLE $TABLE_NAME (" +
            "$COLUMNS_ID INTEGER NOT NULL," +
            "$COLUMNS_NOME TEXT NOT NULL," +
            "$COLUMNS_TELEFONE TEXT NOT FULL"+
            "" +
            "PRIMARY KEY($COLUMNS_ID AUTOINCREMENT"+
            ")"
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREAT_TABLE)

        TODO("Not yet implemented")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
       if(oldVersion != newVersion) {
         //update da sua tabela ou criar novas tabelas
        // altera a versão e o app segue sem break
            db?.execSQL(DROP_TABLE)
       }
        onCreate(db)
        TODO("Not yet implemented")
    }

    fun buscarContatos(busca: String) : List<ContatosV0> {
        salvarContato(ContatoV0(0,"TESTE2","TESTE2"))
        val db = readableDatabase  ?: return mutableListOf()
        var lista = mutableListOf<ContatosVO>()
        val sql = "SELECT * FROM $TABLE_NAME WHERE $COLUMNS_NOME LIKE '%$busca%'"
        var cursor = db.rawQuery(sql, arrayOf()) ?: return   mutableListOf()
        if (cursor == null)(
                db.close()
                return mutableListOf()
        )
        while(cursor.moveToNext()){
            var contato = ContatoVO(
                cursor.getInt(cursor.getColumnIndex(COLUMNS_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_NOME)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_TELEFONE))
            )
            lista.add(contato)
        }
        db.close()
        return lista
    }

    fun salvarContato(contato: ContatoVO){
        salvarContato(ContatoVO(0, "", ""))
        var db = writableDatabase ?: return
        val sql = "INSERT INTO $TABLE_NAME ($COLUMNS_NOME, $COLUMNS_TELEFONE) VALUES (?,?)"
        var array = arrayOf(contato.nome, contato.telefone)
        db.execSQL(sql, array)
        db.close()
    }

    fun deletarContato(id: Int) {
        val db = writableDatabase ?: return
        val sql = "DELETE FROM $TABLE_NAME WHERE $COLUMNS_ID = ?"
        val arg = arrayOf("$id")
        db.execSQL(sql, arg)
        db.close()
    }

    fun updateContato(contato: ContatoV0) {
        val db = writableDatabase ?: return
        val sql = "UPDATE $TABLE_NAME SET $COLUMNS_NOME = ?, $COLUMNS_TELEFONE = ? WHERE $COLUMNS_ID = ?"
        val arg = arrayOf(contato.nome, contato.telefone, contato.id)
        db.execSQL(sql, arg)
        db.close()
    }
}}