package com.example.devshowcaseapi.dto;

import com.example.devshowcaseapi.model.Project;
import java.util.Set;
import java.util.stream.Collectors;

public record ProjectResponseDTO(
        Long id,
        String title,
        String description,
        String repositoryUrl,
        Integer upvotes,
        Double averageRating,
        Set<TechnologyResponseDTO> technologies
) {
    public ProjectResponseDTO(Project entity) {
        this(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getRepositoryUrl(),
                entity.getUpvotes(),
                entity.getAverageRating(),
                entity.getTechnologies() != null
                        ? entity.getTechnologies().stream().map(TechnologyResponseDTO::new).collect(Collectors.toSet())
                        : Set.of()
        );
    }
}