package br.edu.mackenzie.estudiobdd.service;

import br.edu.mackenzie.estudiobdd.dto.ApiDtos.ResumoView;
import br.edu.mackenzie.estudiobdd.repository.HistoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.EnumMap;
import static br.edu.mackenzie.estudiobdd.model.Enums.*;

@Service
public class ResumoService {
    private final Referencias refs;
    private final HistoriaRepository historias;
    public ResumoService(Referencias refs, HistoriaRepository historias) {this.refs=refs;this.historias=historias;}
    @Transactional(readOnly=true) public ResumoView obter(Long projetoId) {
        refs.projeto(projetoId);
        var lista=historias.findByProjetoIdOrderByIdAsc(projetoId);
        var origens=new EnumMap<OrigemCenario,Long>(OrigemCenario.class);
        var decisoes=new EnumMap<Decisao,Long>(Decisao.class);
        var motivos=new EnumMap<MotivoRejeicao,Long>(MotivoRejeicao.class);
        for(var o:OrigemCenario.values()) origens.put(o,0L);
        for(var d:Decisao.values()) decisoes.put(d,0L);
        for(var m:MotivoRejeicao.values()) motivos.put(m,0L);
        for(var h:lista) {
            for(var c:h.cenarios) origens.merge(c.origem,1L,Long::sum);
            for(var s:h.sugestoes) {
                decisoes.merge(s.decisao,1L,Long::sum);
                if(s.decisao==Decisao.REJEITADA && s.motivoRejeicao!=null) motivos.merge(s.motivoRejeicao,1L,Long::sum);
            }
        }
        return new ResumoView(projetoId,lista.size(),origens,decisoes,motivos);
    }
}
