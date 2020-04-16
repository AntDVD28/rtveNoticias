package com.example.rtvenoticias;

public class Noticia {

    private Integer id;
    private String titulo;
    private String enlace;
    private String fecha;
    private Integer leido;
    private Integer favorito;
    //En la variable entera fuente estamos guardando el identificador del recurso (icono)
    private Integer fuente;


    //Constructores

    //Constructor utilizado en EliminarNoticia y ListadoNoticias
    public Noticia(){

    }


    //Constructor utilizado en FichaNoticia
    public Noticia(Integer id, String titulo, String enlace, String fecha, Integer leido, Integer favorito, Integer fuente){

        this.id = id;
        this.titulo = titulo;
        this.enlace = enlace;
        this.fecha = fecha;
        this.leido = leido;
        this.favorito = favorito;
        this.fuente = fuente;
    }


    //Métodos getters
    public Integer getId(){
        return this.id;
    }

    public String getTitulo(){

        //Hago una copia de titulo, trabajaré con ella por si necesitara posteriormente su valor original
        String titulo_nuevo = this.titulo;

        //Recortamos el titulo a un máximo de 28 caracteres en el caso que lo supere
        if(titulo_nuevo.length()>25){

            titulo_nuevo = titulo_nuevo.substring(0,25)+"...";

        }
        return titulo_nuevo;
    }

    public String getEnlace(){
        return this.enlace;
    }

    public String getFecha(){
        return this.fecha;
    }

    public Integer getLeido(){
        return this.leido;
    }

    public Integer getFavorito(){
        return this.favorito;
    }

    public Integer getFuente(){
        return this.fuente;
    }

    //Métodos setters
    public void setId(Integer id){
        this.id = id;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public void setEnlace(String enlace){
        this.enlace = enlace;
    }

    public void setFecha(String fecha){
        this.fecha = fecha;
    }

    public void setLeido(Integer leido){
        this.leido = leido;
    }

    public void setFavorito(Integer favorito){
        this.favorito = favorito;
    }

    public void setFuente(Integer fuente){
        this.fuente = fuente;
    }

    //Método auxiliar que utilizamos para mostrar por pantalla el contenido del objeto
    public String toString(){

        return "ID: " + this.id + "- Titulo: " + this.titulo + "- Fuente: " + this.fuente;
    }


}
