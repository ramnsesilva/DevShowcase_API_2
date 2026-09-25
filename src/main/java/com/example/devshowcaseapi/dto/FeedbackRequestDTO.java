package com.example.devshowcaseapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FeedbackRequestDTO(
        @NotBlank(message = "O nome do autor é obrigatório")
        String authorName,

        @NotBlank(message = "O comentário é obrigatório")
        String comment,

        @NotNull(message = "A nota é obrigatória")
        @Min(value = 1, message = "A nota mínima é 1")
        @Max(value = 5, message = "A nota máxima é 5")
        Integer rating
) {}