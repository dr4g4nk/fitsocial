package core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

public class ResponseDto<Dto, Entity> {
    private Dto result;
    @JsonIgnore
    private Entity entity;
    private boolean success;
    private String message;
    private HttpStatus httpStatus;

    public ResponseDto() {
    }

    public ResponseDto(boolean success) {
        this.success = success;
    }

    public ResponseDto(Dto result) {
        this.result = result;
        this.success = true;
    }

    public ResponseDto(Dto result, Entity entity) {
        this.result = result;
        this.success = true;
        this.entity = entity;
    }

    public ResponseDto(Dto result, Entity entity, boolean success) {
        this.result = result;
        this.success = success;
        this.entity = entity;
    }

    public ResponseDto(Dto result, Entity entity, boolean success, String message) {
        this.result = result;
        this.success = success;
        this.message = message;
        this.entity = entity;
    }

    public ResponseDto(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.success = false;
    }

    public ResponseDto(String message) {
        this.message = message;
        this.success = false;
    }

    public Dto getResult() {
        return result;
    }

    public void setResult(Dto result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonIgnore
    public Entity getEntity() {
        return entity;
    }
}
