package br.edu.mackenzie.estudiobdd.service;

import org.springframework.http.HttpStatus;

public class RegraException extends RuntimeException {
    public final HttpStatus status;
    public RegraException(HttpStatus status,String mensagem) { super(mensagem);this.status=status; }
    public static RegraException invalido(String msg) {return new RegraException(HttpStatus.UNPROCESSABLE_ENTITY,msg);}
    public static RegraException naoEncontrado(String nome) {return new RegraException(HttpStatus.NOT_FOUND,nome+" não encontrado(a).");}
    public static RegraException conflito(String msg) {return new RegraException(HttpStatus.CONFLICT,msg);}
    public static RegraException proibido(String msg) {return new RegraException(HttpStatus.FORBIDDEN,msg);}
}
