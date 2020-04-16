package com.example.rtvenoticias;

import android.provider.BaseColumns;

public class EstructuraBD {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private EstructuraBD() {}


    //Definimos nombre de la tabla y columnas
    public static final String TABLE_NAME = "noticias";
    public static final String COLUMN_1_NAME = "id";
    public static final String COLUMN_2_NAME = "titulo";
    public static final String COLUMN_3_NAME = "enlace";
    public static final String COLUMN_4_NAME = "fecha";
    public static final String COLUMN_5_NAME = "leido";
    public static final String COLUMN_6_NAME = "favorita";
    public static final String COLUMN_7_NAME = "fuente";


    protected static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    EstructuraBD.COLUMN_1_NAME + " INTEGER PRIMARY KEY," +
                    EstructuraBD.COLUMN_2_NAME + " TEXT," +
                    EstructuraBD.COLUMN_3_NAME + " TEXT," +
                    EstructuraBD.COLUMN_4_NAME + " TEXT," +
                    EstructuraBD.COLUMN_5_NAME + " INTEGER," +
                    EstructuraBD.COLUMN_6_NAME + " INTEGER," +
                    EstructuraBD.COLUMN_7_NAME + " INTEGER" +
                    ")";

    protected static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + EstructuraBD.TABLE_NAME;

}
