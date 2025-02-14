package omsu.mim.imit.school.registry.util.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ObjectNotFoundException extends RuntimeException{

    private String message;
}
