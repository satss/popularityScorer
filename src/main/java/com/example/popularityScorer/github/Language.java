package com.example.popularityScorer.github;

public enum Language {
    Java,
    Kotlin,
    Python,
    Cpp,
    Csharp,
    PHP,
    C,
    Ruby,
    Rust,
    Scala,
    Swift,
    Groovy,
    Go,
    JavaScript,
    TypeScript,
    Uncategorized;

    static public Language fromString(String language) {
        if(language == null) {
            return Language.Uncategorized;
        }
        return switch (language.toLowerCase()) {
            case "java" -> Language.Java;
            case "kotlin" -> Language.Kotlin;
            case "python" -> Language.Python;
            case "c++" -> Language.Cpp;
            case "c#" -> Language.Csharp;
            case "groovy" -> Language.Groovy;
            case "php" -> Language.PHP;
            case "c" -> Language.C;
            case "rust" -> Language.Rust;
            case "scala" -> Language.Scala;
            case "swift" -> Language.Swift;
            case "javascript" -> Language.JavaScript;
            case "typescript" -> Language.TypeScript;
            case "go", "golang" -> Language.Go;
            case "ruby" -> Language.Ruby;
            default -> Language.Uncategorized;
        };
    }


}
