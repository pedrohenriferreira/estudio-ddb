package br.edu.mackenzie.estudiobdd.config;

import br.edu.mackenzie.estudiobdd.service.RegraException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingRequestHeaderException;
import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErroHandler {
    private ProblemDetail problema(HttpStatus status,String detail) {
        var problem=ProblemDetail.forStatusAndDetail(status,detail);
        problem.setTitle("Erro na requisição");
        problem.setType(URI.create("about:blank"));
        return problem;
    }
    @ExceptionHandler(RegraException.class)
    public ResponseEntity<ProblemDetail> negocio(RegraException ex) {return ResponseEntity.status(ex.status).body(problema(ex.status,ex.getMessage()));}
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> validacao(MethodArgumentNotValidException ex) {
        var mensagem=ex.getBindingResult().getFieldErrors().stream()
                .map(e->e.getField()+": "+e.getDefaultMessage()).distinct().collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(problema(HttpStatus.BAD_REQUEST,mensagem));
    }
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class, MissingRequestHeaderException.class})
    public ResponseEntity<ProblemDetail> parametros(Exception ex) {
        return ResponseEntity.badRequest().body(problema(HttpStatus.BAD_REQUEST,"JSON, parâmetro ou X-Usuario-Id ausente/inválido."));
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> integridade(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problema(HttpStatus.CONFLICT,"Conflito de unicidade ou integridade referencial."));
    }
}
