package br.edu.mackenzie.estudiobdd.controller;
import br.edu.mackenzie.estudiobdd.dto.ApiDtos.*;
import br.edu.mackenzie.estudiobdd.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService service;
    public UsuarioController(UsuarioService service) {this.service=service;}
    @GetMapping public List<UsuarioView> listar() {return service.listar();}
    @PostMapping public ResponseEntity<UsuarioView> criar(@Valid @RequestBody UsuarioInput input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(input));
    }
}
