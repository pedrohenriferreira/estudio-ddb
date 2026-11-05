package br.edu.mackenzie.estudiobdd.repository;
import br.edu.mackenzie.estudiobdd.model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProjetoRepository extends JpaRepository<Projeto,Long> { boolean existsByNomeIgnoreCase(String nome); }
