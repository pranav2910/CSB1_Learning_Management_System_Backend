package com.lms.service.impl;
import com.lms.dto.request.ContentRequest;
import com.lms.dto.response.ContentResponse;
import com.lms.exception.ResourceNotFoundException;
import com.lms.model.Content;
import com.lms.repository.ContentRepository;
import com.lms.repository.CourseRepository;
import com.lms.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {
    private final ContentRepository contentRepository;
    private final CourseRepository courseRepository;

    @Override
    public ContentResponse addContent(String courseId, ContentRequest request) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course", "id", courseId);
        }

        Content content = Content.builder()
                .courseId(courseId)
                .title(request.getTitle())
                .description(request.getDescription())
                .contentType(Content.ContentType.valueOf(request.getContentType()))
                .contentUrl(request.getContentUrl())
                .duration(request.getDuration())
                .order(request.getOrder())
                .isFreePreview(request.getIsFreePreview())
                .questions(mapQuestionRequests(request.getQuestions()))
                .build();

        Content savedContent = contentRepository.save(content);
        return mapToResponse(savedContent);
    }

    @Override
    public List<ContentResponse> getAllContent(String courseId) {
        return contentRepository.findByCourseId(courseId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ContentResponse getContentById(String courseId, String contentId) {
        Content content = contentRepository.findByIdAndCourseId(contentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Content", "id", contentId));
        return mapToResponse(content);
    }

    @Override
    public ContentResponse updateContent(String courseId, String contentId, ContentRequest request) {
        Content content = contentRepository.findByIdAndCourseId(contentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Content", "id", contentId));

        content.setTitle(request.getTitle());
        content.setDescription(request.getDescription());
        content.setContentType(Content.ContentType.valueOf(request.getContentType()));
        content.setContentUrl(request.getContentUrl());
        content.setDuration(request.getDuration());
        content.setOrder(request.getOrder());
        content.setIsFreePreview(request.getIsFreePreview());
        content.setQuestions(mapQuestionRequests(request.getQuestions()));

        Content updatedContent = contentRepository.save(content);
        return mapToResponse(updatedContent);
    }

    @Override
    public void deleteContent(String courseId, String contentId) {
        if (!contentRepository.existsByIdAndCourseId(contentId, courseId)) {
            throw new ResourceNotFoundException("Content", "id", contentId);
        }
        contentRepository.deleteById(contentId);
    }

    private List<Content.Question> mapQuestionRequests(List<ContentRequest.QuestionRequest> questions) {
        if (questions == null) return null;
        
        return questions.stream()
                .map(q -> Content.Question.builder()
                        .questionText(q.getQuestionText())
                        .options(q.getOptions())
                        .correctAnswerIndex(q.getCorrectAnswerIndex())
                        .build())
                .collect(Collectors.toList());
    }

    private ContentResponse mapToResponse(Content content) {
        return ContentResponse.builder()
                .id(content.getId())
                .courseId(content.getCourseId())
                .title(content.getTitle())
                .description(content.getDescription())
                .contentType(content.getContentType().name())
                .contentUrl(content.getContentUrl())
                .duration(content.getDuration())
                .order(content.getOrder())
                .isFreePreview(content.getIsFreePreview())
                .questions(mapQuestionResponses(content.getQuestions()))
                .build();
    }

    private List<ContentResponse.QuestionResponse> mapQuestionResponses(List<Content.Question> questions) {
        if (questions == null) return null;
        
        return questions.stream()
                .map(q -> ContentResponse.QuestionResponse.builder()
                        .questionText(q.getQuestionText())
                        .options(q.getOptions())
                        .correctAnswerIndex(q.getCorrectAnswerIndex())
                        .build())
                .collect(Collectors.toList());
    }
}