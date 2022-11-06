package org.aguzman.springcloud.msvc.cursos.controllers;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.aguzman.springcloud.msvc.cursos.models.Usuario;
import org.aguzman.springcloud.msvc.cursos.models.entity.Curso;
import org.aguzman.springcloud.msvc.cursos.services.CursoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import feign.FeignException;

@RestController
public class CursoController { 

	Logger logger= Logger.getLogger(CursoController.class.getName());
    @Autowired
    private CursoService service;
    
    @PostConstruct()
    private void init() {
    	if(service.listar().isEmpty()) {
    		
    		Curso curso=new Curso();
    		
    		curso.setNombre("fisica");
    		service.guardar(curso);
    		
    		String pass="1234";
    		Usuario usuario=new Usuario();
    		usuario.setNombre("Juan-Curso");
    		usuario.setPassword(pass);
    		usuario.setEmail(usuario.getNombre()+"@gmail.com");
    		
    		service.crearUsuario(usuario, curso.getId());
    		
    		
    		Usuario usuario2=new Usuario();
    		usuario2.setNombre("Rodri-Curso");
    		usuario2.setPassword(pass);
    		usuario2.setEmail(usuario2.getNombre()+"@gmail.com");
    		
    		service.crearUsuario(usuario2, curso.getId());
    		
    		
    		
    		Curso curso2=new Curso();
    		
    		curso2.setNombre("fisica");
    		service.guardar(curso2);
    		
    		
    		Usuario usuario3=new Usuario();
    		usuario3.setNombre("Juan-Curso-curso");
    		usuario3.setPassword(pass);
    		usuario3.setEmail(usuario3.getNombre()+"@gmail.com");
    		
    		service.crearUsuario(usuario3, curso2.getId());
    		
    		
    		Usuario usuario4=new Usuario();
    		usuario4.setNombre("Rodri-Curso-curso");
    		usuario4.setPassword(pass);
    		usuario4.setEmail(usuario4.getNombre()+"@gmail.com");
    		
    		service.crearUsuario(usuario4, curso2.getId());
    		
    		
    		
    		
  
    		
    		
//    		
//    		
//    		String pass="1234";
//    		Usuario usuario=new Usuario();
//    		usuario.setNombre("Juan");
//    		usuario.setPassword(pass);
//    		usuario.setEmail(usuario.getNombre()+"@gmail.com");
//    		
//    		Usuario usuario2=new Usuario();
//    		usuario2.setNombre("Brian");
//    		usuario2.setPassword(pass);
//    		usuario2.setEmail(usuario2.getNombre()+"@gmail.com");
//    		
//    		Usuario usuario3=new Usuario();
//    		usuario3.setNombre("Luz");
//    		usuario3.setPassword(pass);
//    		usuario3.setEmail(usuario3.getNombre()+"@gmail.com");
//    		
//    		Usuario usuario4=new Usuario();
//    		usuario4.setNombre("Zaira");
//    		usuario4.setPassword(pass);
//    		usuario4.setEmail(usuario4.getNombre()+"@gmail.com");
//    		
//    		Usuario usuario5=new Usuario();
//    		usuario5.setNombre("Seba");
//    		usuario5.setPassword(pass);
//    		usuario5.setEmail(usuario5.getNombre()+"@gmail.com");
    		
    		
    	}
    }
    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Curso> o = service.porId(id);
        if (o.isPresent()) {
            return ResponseEntity.ok(o.get());
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("detalleIdConUsuarios/{id}")
    public ResponseEntity<?> detalleIdConUsuarios(@PathVariable Long id) {
        Optional<Curso> o = service.porIdConUsuarios(id);
        if (o.isPresent()) {
            return ResponseEntity.ok(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        Curso cursoDb = service.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validar(result);
        }
        Optional<Curso> o = service.porId(id);
        if (o.isPresent()) {
            Curso cursoDb = o.get();
            cursoDb.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(cursoDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Curso> o = service.porId(id);
        if (o.isPresent()) {
            service.eliminar(o.get().getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
     /*
      * No hay un flujo lindo de exepciones cuando el id del alumno ya esta regustrado, si se muestra en la consola errores pero refinal postman
      * no se hace rollbakk.Cuidado
      */
    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
		/*
		 * FIXME Se envia un el siguiente usuario como json { "id":2,"nombre":"juan","email":"juan@gmail.com", "password":"1234" } y tiene como
		 * respuesta el siguiente objeto { "id": 2, "nombre": "Brian", "email":"Brian@gmail.com", "password": "1234" }
		 * el cual solamente coinciden el id, validar esto, que el objeto que ingresa sea el selecionado.
		 */
        Optional<Usuario> o;
        try {
            o = service.asignarUsuario(usuario, cursoId);
        }catch(SQLException e){
            	System.out.println(e.getMessage());
        	return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "Error en la base de datos " + e.getMessage()));
   
        }catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No existe el usuario por " +
                            "el id o error en la comunicacion: " + e.getMessage()));
        }catch(Exception e){
        	logger.info(e.getMessage());
        	return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "Error en la base de datos " + e.getMessage()));
        } 
        
        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }
    
	/*
	 * Si funciona bien, no se envia el id, el cual es auto-incremental
	 * request-->{"nombre":"juan", "email":"juan.3@gmail.com", "password":"1234" }
	 * responce-->{ "id": 7,"nombre": "juan", "email": "juan.3@gmail.com", "password": "1234" }
	 * si valida que el correo sea unico del lado del microservicio Usuario
	 */
    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> o;
        try {
            o = service.crearUsuario(usuario, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No se pudo crear el usuario " +
                            "o error en la comunicacion: " + e.getMessage()));
        }
        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }
/*
 * FIXME Ocurre algo similar al agregar un usuario ya creado, solo valida el id, y no los otros campos
 */
    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> o;
        try {
            o = service.eliminarUsuario(usuario, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No existe el usuario por " +
                            "el id o error en la comunicacion: " + e.getMessage()));
        }
        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable Long id){
        service.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }   
       
    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
    
}
