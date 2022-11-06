package org.aguzman.springcloud.msvc.usuarios.repositories;

import java.util.Optional;

import org.aguzman.springcloud.msvc.usuarios.models.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
	//findBy+atributo de la Entidad
	Optional<Usuario> findByEmail(String email);
	//?1 cambia deacuardo a los parametros que ingresan
	@Query("select u from Usuario u where u.email=?1")
    Optional<Usuario> porEmail(String email);
	
	//existsBy+atributo de la Entidad
	boolean existsByEmail(String email);
	
    @Query(value="call eliminarTabla",nativeQuery=true)
    void truncate();
}
