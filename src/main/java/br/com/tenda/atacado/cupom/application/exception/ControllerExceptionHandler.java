package br.com.tenda.atacado.cupom.application.exception;

import br.com.tenda.atacado.cupom.application.output.StandardErrorOutput;
import br.com.tenda.atacado.cupom.core.exception.DomainException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardErrorOutput> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
        StandardErrorOutput err = new StandardErrorOutput(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(), "Não encontrado", e.getMessage(), request.getRequestURI());
        log.error("PRECONDITION_FAILED: {} => {}", err.getError(), err.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardErrorOutput> entityNotFound(EntityNotFoundException e, HttpServletRequest request) {
        StandardErrorOutput err = new StandardErrorOutput(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(), "Não encontrado", e.getMessage(), request.getRequestURI());
        log.error("PRECONDITION_FAILED: {} => {}", err.getError(), err.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(ServiceBusinessException.class)
    public ResponseEntity<StandardErrorOutput> serviceBusinessException(ServiceBusinessException e, HttpServletRequest request) {
        StandardErrorOutput err = new StandardErrorOutput(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "Houve um problema ao solicitar a operação", e.getMessage(), request.getRequestURI());
        log.error("PRECONDITION_FAILED: {} => {}", err.getError(), err.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<StandardErrorOutput> persistenceException(PersistenceException e, HttpServletRequest request) {
        StandardErrorOutput err = new StandardErrorOutput(System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro ao persistir dados", e.getMessage(), request.getRequestURI());
        log.error("PERSISTENCE_ERROR: {} => {}", err.getError(), err.getMessage(), e.getCause());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<StandardErrorOutput> domainException(DomainException e, HttpServletRequest request) {
        StandardErrorOutput err = new StandardErrorOutput(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "Houve um erro de domínio ao solicitar a operação", e.getMessage(), request.getRequestURI());
        log.error("DOMAIN_EXCEPTION: {} => {}", err.getError(), err.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<StandardErrorOutput> contraintViolationException(TransactionSystemException e, HttpServletRequest request) {
        StandardErrorOutput err = new StandardErrorOutput(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "Violação de integridade de dados",
                "Não foi possível realizar a transação: "+e.getMessage(),
                request.getContextPath()
        );
        log.error("BAD REQUEST: {} => {}", err.getError(), err.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(RollbackException.class)
    public ResponseEntity<StandardErrorOutput> handleNotValidException(RollbackException ex, HttpServletRequest request){
        StandardErrorOutput err = new StandardErrorOutput(
                System.currentTimeMillis(),
                HttpStatus.NOT_ACCEPTABLE.value(),
                "Violação de integridade de dados",
                "Não foi possível realizar a transação",
                request.getContextPath()
        );
        log.error("BAD REQUEST: {} => {}", err.getError(), err.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(err);

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardErrorOutput> dataIntegrityException(DataIntegrityViolationException e, HttpServletRequest request) {
        StandardErrorOutput err = new StandardErrorOutput(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "Violação de integridade de dados",
                "Não foi possível realizar a transação: "+e.getMessage(),
                request.getContextPath()
        );
        log.error("BAD REQUEST: {} => {}", err.getError(), err.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> accessNotAuthorizedHandle(AccessDeniedException ex, HttpServletRequest request) {
        StandardErrorOutput err = new StandardErrorOutput(
                System.currentTimeMillis(),
                HttpStatus.FORBIDDEN.value(),
                "Não autorizado",
                "Acesso negado", request.getRequestURI());
        log.error("ACCESS NOT AUTHORIZED: {} => {}", err.getError(), err.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardErrorOutput> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        StandardErrorOutput err = new StandardErrorOutput(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                errorMessage.isEmpty() ? "Dados inválidos" : errorMessage,
                request.getRequestURI()
        );
        log.error("VALIDATION_ERROR: {} => {}", err.getError(), err.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }
}
