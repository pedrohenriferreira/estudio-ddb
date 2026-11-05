package br.edu.mackenzie.estudiobdd.repository;
import br.edu.mackenzie.estudiobdd.model.Cenario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CenarioRepository extends JpaRepository<Cenario,Long> {
    List<Cenario> findByHistoriaIdOrderByPosicaoAsc(Long historiaId);
    boolean existsByHistoriaIdAndTituloIgnoreCase(Long historiaId,String titulo);
    boolean existsByHistoriaIdAndTituloIgnoreCaseAndIdNot(Long historiaId,String titulo,Long id);
}
