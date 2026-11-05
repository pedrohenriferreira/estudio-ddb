package br.edu.mackenzie.estudiobdd.repository;
import br.edu.mackenzie.estudiobdd.model.HistoriaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import static br.edu.mackenzie.estudiobdd.model.Enums.*;
public interface HistoriaRepository extends JpaRepository<HistoriaUsuario,Long> {
    List<HistoriaUsuario> findByProjetoIdOrderByIdAsc(Long projetoId);
    List<HistoriaUsuario> findByProjetoIdAndPrioridadeOrderByIdAsc(Long projetoId, Prioridade prioridade);
    boolean existsByProjetoIdAndTituloIgnoreCase(Long projetoId,String titulo);
    boolean existsByProjetoIdAndTituloIgnoreCaseAndIdNot(Long projetoId,String titulo,Long id);
}
