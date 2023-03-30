package org.zyfra.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LocalException extends RuntimeException {

    public LocalException(String msg) {
        super(msg);
    }
}
