<?php

	$conexion = mysqli_connect("localhost","id10127563_admin", "123456", "id10127563_noticias" );

	if(!$conexion){

		exit("Error de conexión al servidor");
	}	


		
	//Capturo los datos enviados por POST
	$titulo = $_POST["titulo"];
	$enlace = $_POST["enlace"];
	$fecha = $_POST["fecha"];
	$leido = $_POST["leido"];
	$favorito = $_POST["favorito"];
	$fuente = $_POST["fuente"];

	
	$consulta = "INSERT INTO noticias (titulo, enlace, fecha, leido, favorito, fuente) values ('".$titulo."','".$enlace."','".$fecha."',".$leido.",".$favorito.",".$fuente.")";


	$resultado = mysqli_query($conexion, $consulta);

	$num = mysqli_affected_rows($conexion);

	if($num>0){

		echo "Registro insertado correctamente";

				
	}else {

		echo "Error en el registro";
	}
	
	//Cerramos conexiones
	$consulta = null;
	mysqli_close($conexion);



?>