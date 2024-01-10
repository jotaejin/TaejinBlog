package com.tjlog.demo.exception;

/**
 * status -> 404
 */
public class PostNotFound extends TjlogException{

    private static final String MESSAGE = "존재하지 않는 글입니다";

    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
