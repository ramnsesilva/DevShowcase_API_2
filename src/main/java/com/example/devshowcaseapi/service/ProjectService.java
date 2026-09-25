package com.example.devshowcaseapi.service;

import com.example.devshowcaseapi.dto.FeedbackRequestDTO;
import com.example.devshowcaseapi.dto.FeedbackResponseDTO;
import com.example.devshowcaseapi.dto.ProjectRequestDTO;
import com.example.devshowcaseapi.dto.ProjectResponseDTO;
import com.example.devshowcaseapi.exception.ResourceNotFoundException;
import com.example.devshowcaseapi.model.Feedback;
import com.example.devshowcaseapi.model.Project;
import com.example.devshowcaseapi.model.Technology;
import com.example.devshowcaseapi.repository.FeedbackRepository;
import com.example.devshowcaseapi.repository.ProjectRepository;
import com.example.devshowcaseapi.repository.TechnologyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TechnologyRepository technologyRepository;
    private final FeedbackRepository feedbackRepository;

    public ProjectService(ProjectRepository projectRepository,
                          TechnologyRepository technologyRepository,
                          FeedbackRepository feedbackRepository) {
        this.projectRepository = projectRepository;
        this.technologyRepository = technologyRepository;
        this.feedbackRepository = feedbackRepository;
    }

    @Transactional
    public ProjectResponseDTO create(ProjectRequestDTO dto) {
        Project project = new Project();
        project.setTitle(dto.title());
        project.setDescription(dto.description());
        project.setRepositoryUrl(dto.repositoryUrl());

        if (dto.technologyIds() != null && !dto.technologyIds().isEmpty()) {
            List<Technology> foundTechnologies = technologyRepository.findAllById(dto.technologyIds());
            project.setTechnologies(new HashSet<>(foundTechnologies));
        }

        project = projectRepository.save(project);
        return new ProjectResponseDTO(project);
    }

    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> findAll(String technology, Pageable pageable) {
        return projectRepository.findByTechnology(technology, pageable)
                .map(ProjectResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public ProjectResponseDTO findById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado com o id: " + id));
        return new ProjectResponseDTO(project);
    }

    @Transactional
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Projeto não encontrado com o id: " + id);
        }
        projectRepository.deleteById(id);
    }

    @Transactional
    public FeedbackResponseDTO addFeedback(Long projectId, FeedbackRequestDTO dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado com o id: " + id(projectId)));

        Feedback feedback = new Feedback();
        feedback.setAuthorName(dto.authorName().trim());
        feedback.setComment(dto.comment().trim());
        feedback.setRating(dto.rating());
        feedback.setProject(project);

        feedback = feedbackRepository.save(feedback);

        // Recalcular nota média
        List<Feedback> allFeedbacks = feedbackRepository.findByProjectId(projectId);
        double avg = allFeedbacks.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);

        project.setAverageRating(Math.round(avg * 10.0) / 10.0); // Arredonda para 1 casa decimal
        projectRepository.save(project);

        return new FeedbackResponseDTO(feedback);
    }

    @Transactional
    public ProjectResponseDTO upvote(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado com o id: " + id(projectId)));

        project.setUpvotes(project.getUpvotes() + 1);
        project = projectRepository.save(project);
        return new ProjectResponseDTO(project);
    }

    private Long id(Long val) {
        return val;
    }
}