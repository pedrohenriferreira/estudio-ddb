package br.edu.mackenzie.estudiobdd.controller;
import br.edu.mackenzie.estudiobdd.dto.ApiDtos.*;
import br.edu.mackenzie.estudiobdd.service.CenarioService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
public class CenarioController {
    private final CenarioService service;
    public CenarioController(CenarioService service) {this.service=service;}
    @GetMapping("/api/historias/{id}/cenarios") public List<CenarioView> listar(@PathVariable Long id) {return service.listar(id);}
    @PostMapping("/api/historias/{id}/cenarios") public ResponseEntity<CenarioView> criar(@PathVariable Long id,
            @RequestHeader("X-Usuario-Id") Long usuarioId,@Valid @RequestBody CenarioInput input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(id,usuarioId,input));
    }
    @GetMapping("/api/cenarios/{id}") public CenarioView buscar(@PathVariable Long id) {return service.buscar(id);}
    @PutMapping("/api/cenarios/{id}") public CenarioView editar(@PathVariable Long id,@RequestHeader("X-Usuario-Id") Long usuarioId,
            @Valid @RequestBody CenarioInput input) {return service.editar(id,usuarioId,input);}
    @DeleteMapping("/api/cenarios/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id,@RequestHeader("X-Usuario-Id") Long usuarioId) {service.excluir(id,usuarioId);}
    @GetMapping("/api/cenarios/{id}/passos") public List<PassoView> listarPassos(@PathVariable Long id) {return service.listarPassos(id);}
    @PostMapping("/api/cenarios/{id}/passos") public ResponseEntity<PassoView> criarPasso(@PathVariable Long id,
            @RequestHeader("X-Usuario-Id") Long usuarioId,@Valid @RequestBody PassoInput input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.adicionarPasso(id,usuarioId,input));
    }
    @PutMapping("/api/passos/{id}") public PassoView editarPasso(@PathVariable Long id,@RequestHeader("X-Usuario-Id") Long usuarioId,
            @Valid @RequestBody PassoInput input) {return service.editarPasso(id,usuarioId,input);}
    @DeleteMapping("/api/passos/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirPasso(@PathVariable Long id,@RequestHeader("X-Usuario-Id") Long usuarioId) {service.excluirPasso(id,usuarioId);}
}
