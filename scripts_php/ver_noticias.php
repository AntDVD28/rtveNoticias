<?php

$conexion = mysqli_connect("localhost","id10127563_admin", "123456", "id10127563_noticias" );

if(!$conexion){

	exit("Error de conexión al servidor");
}

//Hago una consulta de todos los registros de la tabla noticias		
$consulta = "SELECT * FROM noticias";

$resultado = mysqli_query($conexion, $consulta);

//Muestro registros por pantalla
echo "<table border=1>";
echo "<tr><th>Id</th><th>Título</th><th>Enlace</th><th>Fecha</th><th>Leido</th><th>Favorito</th><th>Fuente</th></tr>";

while($rows=mysqli_fetch_array($resultado)){
	echo "<tr>";
	echo "<td>".$rows[0]."</td>";
	echo "<td>".$rows[1]."</td>";
	echo "<td>".$rows[2]."</td>";
	echo "<td>".$rows[3]."</td>";
	echo "<td>".$rows[4]."</td>";
	echo "<td>".$rows[5]."</td>";
	echo "<td>".$rows[6]."</td>";
	echo "</tr>";
}
echo "</table>";

//Cerramos conexiones
$consulta = null;
mysqli_close($conexion);	



?>