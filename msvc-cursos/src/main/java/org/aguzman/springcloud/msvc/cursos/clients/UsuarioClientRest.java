package org.aguzman.springcloud.msvc.cursos.clients;

import org.aguzman.springcloud.msvc.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/*
 * ver apllication.properties
 * spring.application.name=msvc-usuarios
 * server.port=8001

*/
@FeignClient(name="msvc-usuarios", url="localhost:8001")
public interface UsuarioClientRest {
	/*
	 * El nombre de los metodos no es necesario que sean igual al del controlador de usuario
	 * pero si es necesario que en el endpoint sea el correcto
	 * el BindingResult result, no se agrega ya que es un tema de validacion del metodo una ves entrando el controlador del usuario
	 * aca solo señalamos los endpoint como lo hariamos en postman
	 */
	
    @GetMapping("/{id}")
    Usuario detalle(@PathVariable Long id);

    @PostMapping("/")
    Usuario crear(@RequestBody Usuario usuario);

    @GetMapping("/usuarios-por-curso")
    List<Usuario> obtenerAlumnosPorCurso(@RequestParam Iterable<Long> ids);
   
}
