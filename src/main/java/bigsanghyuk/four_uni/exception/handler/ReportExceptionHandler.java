package bigsanghyuk.four_uni.exception.handler;

import bigsanghyuk.four_uni.exception.ExceptionMessage;
import bigsanghyuk.four_uni.exception.report.ReportReasonNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ReportExceptionHandler {

    @ExceptionHandler(ReportReasonNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handle(ReportReasonNotFoundException e) {
        final ExceptionMessage message = ExceptionMessage.of(e.getStatus(), e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
}
