package com.example.joaopaulo.domoticaapp.data;

import android.provider.BaseColumns;

class DataBaseContract implements BaseColumns {

    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "BomFilme.db";
    static final String TABLE_COMODO = "comodo";
    static final String COLUMN_CODIGO = "codigo";
    static final String COLUMN_NOME = "nome";
    static final String COLUMN_TIPO = "tipo";
    static final String COLUMN_IMAGEM = "imagem";
    static final String TABLE_DISPOSITIVO = "dispositivo";
    static final String COLUMN_COMODO = "comodo";
    static final String COLUMN_COMANDO = "comando";
    static final String COLUMN_COMANDO_ON = "comandoon";
    static final String COLUMN_COMANDO_OFF = "comandooff";
    static final String COLUMN_COMANDO_VAR = "comandovar";

    private DataBaseContract() {}

    static String retornaCriacaoTabelaComodo() {

        return "CREATE TABLE " + TABLE_COMODO + "( " +
                _ID  + " INTEGER PRIMARY KEY, " +
                COLUMN_CODIGO + " TEXT, " +
                COLUMN_NOME + " TEXT, " +
                COLUMN_TIPO + " TEXT, " +
                COLUMN_IMAGEM + " INT )";
    }

    static String retornaDropTabelaComodo() {

        return "DROP TABLE IF EXISTS " + TABLE_COMODO;
    }

    static String retornaCriacaoTabelaDispositivo() {

        return "CREATE TABLE " + TABLE_DISPOSITIVO + "( " +
                _ID  + " INTEGER PRIMARY KEY, " +
                COLUMN_COMODO + " INT, " +
                COLUMN_CODIGO + " TEXT, " +
                COLUMN_NOME + " TEXT, " +
                COLUMN_TIPO + " TEXT, " +
                COLUMN_COMANDO + " TEXT, " +
                COLUMN_COMANDO_ON + " TEXT, " +
                COLUMN_COMANDO_OFF + " TEXT, " +
                COLUMN_COMANDO_VAR + " TEXT )";
    }

    static String retornaDropTabelaDispositivo() {

        return "DROP TABLE IF EXISTS " + TABLE_DISPOSITIVO;
    }

    static String[] retornaCamposSelecao() {

        return new String[] {_ID};/*, COLUMN_TITULO, COLUMN_LANCAMENTO, COLUMN_SINOPSE,
                COLUMN_IMAGEM_POSTER, COLUMN_IMAGEM_BACK, COLUMN_ACESSO};*/
    }

    static String retornaOrdenacaoConsulta() {

        return COLUMN_COMODO + " DESC";
    }
}
