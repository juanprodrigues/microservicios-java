package org.aguzman.springcloud.msvc.usuarios.controllers;

import org.aguzman.springcloud.msvc.usuarios.models.entity.Usuario;
import org.aguzman.springcloud.msvc.usuarios.repositories.UsuarioRepository;
import org.aguzman.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService service;
    
    @PostConstruct
    private void init() {
    	
    	if(service.listar().isEmpty()) {
    		
    		List<Usuario>usuarios=new ArrayList<>();
    	
    		String pass="1234";
    		Usuario usuario=new Usuario();
    		usuario.setNombre("Juan");
    		usuario.setPassword(pass);
    		usuario.setEmail(usuario.getNombre()+"@gmail.com");
    		usuarios.add(usuario);
    		
    		Usuario usuario2=new Usuario();
    		usuario2.setNombre("Brian");
    		usuario2.setPassword(pass);
    		usuario2.setEmail(usuario2.getNombre()+"@gmail.com");
    		usuarios.add(usuario2);
    		
    		Usuario usuario3=new Usuario();
    		usuario3.setNombre("Luz");
    		usuario3.setPassword(pass);
    		usuario3.setEmail(usuario3.getNombre()+"@gmail.com");
    		usuarios.add(usuario3);
    		
    		Usuario usuario4=new Usuario();
    		usuario4.setNombre("Zaira");
    		usuario4.setPassword(pass);
    		usuario4.setEmail(usuario4.getNombre()+"@gmail.com");
    		usuarios.add(usuario4);
    		
    		Usuario usuario5=new Usuario();
    		usuario5.setNombre("Seba");
    		usuario5.setPassword(pass);
    		usuario5.setEmail(usuario5.getNombre()+"@gmail.com");
    		usuarios.add(usuario5);
    		
    		for (Usuario usuarioGuardar : usuarios) {
    			service.guardar(usuarioGuardar);
			}
    	}
    		
    	}
    
    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = service.porId(id);
        if (usuarioOptional.isPresent()) {
            return ResponseEntity.ok(usuarioOptional.get());
        }
        return ResponseEntity.notFound().build();
    }
    //@Valid de javax.validation.Valid seguido de esta validacion se tiene que colocar el BindingResult result
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario,BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }

        if (!usuario.getEmail().isEmpty() && service.existePorEmail(usuario.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Collections
                            .singletonMap("mensaje", "Ya existe un usuario con ese correo electronico!"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@RequestBody Usuario usuario, @PathVariable Long id) {
        Optional<Usuario> o = service.porId(id);
        if (o.isPresent()) {
            Usuario usuarioDb = o.get();
            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuarioDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Usuario> o = service.porId(id);
        if (o.isPresent()) {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam(value="ids",required=true) List<Long> ids){
        return ResponseEntity.ok(service.listarPorIds(ids));
    	//return ResponseEntity.ok(ids);
    }
    
    private ResponseEntity<?> validar(BindingResult result) {
    	// FIXME sfeds 
    	Map<String,String> errorres=new HashMap<>();
    	result.getFieldErrors().forEach(err->{
    		errorres.put(err.getField(),"El campo"+err.getField()+" "+err.getDefaultMessage());
    	});
    	return ResponseEntity.badRequest().body(errorres);
    }
}
