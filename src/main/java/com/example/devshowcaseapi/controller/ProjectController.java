package com.example.devshowcaseapi.controller;

import com.example.devshowcaseapi.dto.FeedbackRequestDTO;
import com.example.devshowcaseapi.dto.FeedbackResponseDTO;
import com.example.devshowcaseapi.dto.ProjectRequestDTO;
import com.example.devshowcaseapi.dto.ProjectResponseDTO;
import com.example.devshowcaseapi.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "projects", description = "Endpoints para gestão de projetos da plataforma")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo projeto com validações")
    public ResponseEntity<ProjectResponseDTO> create(@Valid @RequestBody ProjectRequestDTO dto) {
        ProjectResponseDTO created = projectService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Lista projetos com paginação e filtro opcional por tecnologia")
    public ResponseEntity<Page<ProjectResponseDTO>> findAll(
            @RequestParam(required = false) String technology,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ProjectResponseDTO> page = projectService.findAll(technology, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca projeto por ID")
    public ResponseEntity<ProjectResponseDTO> findById(@PathVariable Long id) {
        ProjectResponseDTO project = projectService.findById(id);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um projeto por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/feedbacks")
    @Operation(summary = "Cadastra nota (1 a 5) e comentário, recalculando a média do projeto")
    public ResponseEntity<FeedbackResponseDTO> addFeedback(
            @PathVariable Long id,
            @Valid @RequestBody FeedbackRequestDTO dto
    ) {
        FeedbackResponseDTO feedback = projectService.addFeedback(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(feedback);
    }

    @PutMapping("/{id}/upvote")
    @Operation(summary = "Incrementa as curtidas/estrelas do projeto")
    public ResponseEntity<ProjectResponseDTO> upvote(@PathVariable Long id) {
        ProjectResponseDTO updated = projectService.upvote(id);
        return ResponseEntity.ok(updated);
    }
}