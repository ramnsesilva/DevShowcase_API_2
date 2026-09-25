package com.example.devshowcaseapi.dto;

import com.example.devshowcaseapi.model.Feedback;
import java.time.LocalDateTime;

public record FeedbackResponseDTO(
    Long id,
    String authorName,
    String comment,
    Integer rating,
    LocalDateTime createdAt
) {
    public FeedbackResponseDTO(Feedback entity) {
        this(
            entity.getId(),
            entity.getAuthorName(),
            entity.getComment(),
            entity.getRating(),
            entity.getCreatedAt()
        );
    }
}