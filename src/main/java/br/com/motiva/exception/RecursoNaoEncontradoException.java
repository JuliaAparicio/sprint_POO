package br.com.motiva.exception;

/** Vira HTTP 404 no GlobalExceptionHandler. */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, Long id) {
        super(recurso + " com ID " + id + " não encontrado(a).");
    }
}
