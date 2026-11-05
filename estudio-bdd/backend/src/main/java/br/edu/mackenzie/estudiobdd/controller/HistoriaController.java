package br.edu.mackenzie.estudiobdd.controller;
import br.edu.mackenzie.estudiobdd.dto.ApiDtos.*;
import br.edu.mackenzie.estudiobdd.service.HistoriaService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static br.edu.mackenzie.estudiobdd.model.Enums.*;
@RestController
public class HistoriaController {
    private final HistoriaService service;
    public HistoriaController(HistoriaService service) {this.service=service;}
    @GetMapping("/api/projetos/{projetoId}/historias")
    public List<HistoriaView> listar(@PathVariable Long projetoId,@RequestParam(required=false) Prioridade prioridade) {return service.listar(projetoId,prioridade);}
    @PostMapping("/api/projetos/{projetoId}/historias")
    public ResponseEntity<HistoriaView> criar(@PathVariable Long projetoId,@RequestHeader("X-Usuario-Id") Long usuarioId,
                                             @Valid @RequestBody HistoriaInput input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(projetoId,usuarioId,input));
    }
    @GetMapping("/api/historias/{id}") public HistoriaView buscar(@PathVariable Long id) {return service.buscar(id);}
    @PutMapping("/api/historias/{id}") public HistoriaView editar(@PathVariable Long id,@RequestHeader("X-Usuario-Id") Long usuarioId,
                                                                    @Valid @RequestBody HistoriaInput input) {return service.editar(id,usuarioId,input);}
    @DeleteMapping("/api/historias/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id,@RequestHeader("X-Usuario-Id") Long usuarioId) {service.excluir(id,usuarioId);}
}
