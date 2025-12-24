package com.algoviz.codeexecution.dto;

public enum Verdict {
    ACCEPTED("Accepted"),
    WRONG_ANSWER("Wrong Answer"),
    TIME_LIMIT_EXCEEDED("Time Limit Exceeded"),
    MEMORY_LIMIT_EXCEEDED("Memory Limit Exceeded"),
    RUNTIME_ERROR("Runtime Error"),
    COMPILATION_ERROR("Compilation Error"),
    PRESENTATION_ERROR("Presentation Error"),
    INTERNAL_ERROR("Internal Error");

    private final String description;

    Verdict(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
