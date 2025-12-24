package com.algoviz.codeexecution.dto;

public enum Language {
    JAVA("java", ".java", "javac", "java"),
    CPP("cpp", ".cpp", "g++", "./a.out"),
    PYTHON("python", ".py", "python3", "python3");

    private final String name;
    private final String extension;
    private final String compiler;
    private final String executor;

    Language(String name, String extension, String compiler, String executor) {
        this.name = name;
        this.extension = extension;
        this.compiler = compiler;
        this.executor = executor;
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public String getCompiler() {
        return compiler;
    }

    public String getExecutor() {
        return executor;
    }

    public boolean needsCompilation() {
        return this == JAVA || this == CPP;
    }
}
