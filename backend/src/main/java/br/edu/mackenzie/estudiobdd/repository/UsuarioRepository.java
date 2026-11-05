package br.edu.mackenzie.estudiobdd.repository;
import br.edu.mackenzie.estudiobdd.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UsuarioRepository extends JpaRepository<Usuario,Long> { boolean existsByEmailIgnoreCase(String email); }
