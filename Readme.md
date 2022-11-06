Empaquetar el jar de un proyecto.!
ante cualquier cambio se tiene que generar el .jar

.\mvnw.cmd clean package

es guarda en target
para ejecutarlo desde consola podemos lanzar el .jar como
 java -jar nombre.jar
 
 por ejemplo luego de ejecutar ".\mvnw.cmd clean package"  vamos a generar un jar , que vamos a poder encointrar en target
  
 java -jar msvc-usuarios-0.0.1-SNAPSHOT.jar
 
 Ya con este jar podemos ejecutarno en cualquier ambiente, solo con instalar el jdk es suficiente.
 


Ventajas de usar docker
 
tema que soluciona Docker es el tema de versiones

y el ambiente que tenemos en nuestra máquina.

Desarrollo con el ambiente que tenemos en producción y también con el ambiente que podría tener nuestro

equipo de trabajo, los compañeros.

Compartir nuestro proyecto se hace mucho más transparente, mucho más fácil, claro, porque hay.
 
 
 
 ejecutando la imagen del docker, se romper ya que en nuestro contenero no se encuentra el localhost para conectar a la base de datos
 para eso se modifica el applicatation.properties , en lugar de localhost ponemos host.docker.internal:3306
 
 Pero cuando volvemos a empaquetar con 
 
 .\mvnw.cmd clean package
  se va a romper porque no encuentra la direcion que introduciomos en el aplication
 porlo cual la solucion es empaquetar sin realizar las pruebas unitarios, es decir como
 
 .\mvnw.cmd clean package -DskipTests
 
 
 
 
 
 