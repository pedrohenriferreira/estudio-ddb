package br.edu.mackenzie.estudiobdd.repository;
import br.edu.mackenzie.estudiobdd.model.Sugestao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import static br.edu.mackenzie.estudiobdd.model.Enums.*;
public interface SugestaoRepository extends JpaRepository<Sugestao,Long> {
    List<Sugestao> findByHistoriaIdOrderByIdAsc(Long historiaId);
    List<Sugestao> findByHistoriaIdAndDecisaoOrderByIdAsc(Long historiaId,Decisao decisao);
    boolean existsByHistoriaIdAndDecisao(Long historiaId,Decisao decisao);
}
