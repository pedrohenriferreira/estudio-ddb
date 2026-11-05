package br.edu.mackenzie.estudiobdd.controller;
import br.edu.mackenzie.estudiobdd.dto.ApiDtos.*;
import br.edu.mackenzie.estudiobdd.service.ProjetoService;
import br.edu.mackenzie.estudiobdd.service.ResumoService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/projetos")
public class ProjetoController {
    private final ProjetoService service;
    private final ResumoService resumo;
    public ProjetoController(ProjetoService service,ResumoService resumo) {this.service=service;this.resumo=resumo;}
    @GetMapping public List<ProjetoView> listar() {return service.listar();}
    @GetMapping("/{id}") public ProjetoView buscar(@PathVariable Long id) {return service.buscar(id);}
    @PostMapping public ResponseEntity<ProjetoView> criar(@Valid @RequestBody ProjetoInput input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(input));
    }
    @PutMapping("/{id}") public ProjetoView editar(@PathVariable Long id,@Valid @RequestBody ProjetoInput input) {return service.editar(id,input);}
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable Long id) {service.excluir(id);}
    @GetMapping("/{id}/resumo") public ResumoView resumo(@PathVariable Long id) {return resumo.obter(id);}
}
