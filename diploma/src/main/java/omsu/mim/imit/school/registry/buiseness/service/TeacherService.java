package omsu.mim.imit.school.registry.buiseness.service;

import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.data.entity.AssistantEntity;
import omsu.mim.imit.school.registry.data.entity.TeacherEntity;
import omsu.mim.imit.school.registry.data.repository.AssistantRepository;
import omsu.mim.imit.school.registry.data.repository.TeacherRepository;
import omsu.mim.imit.school.registry.rest.dto.response.PublicAssistantInfoRestResponse;
import omsu.mim.imit.school.registry.rest.dto.response.PublicTeacherInfoRestResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final AssistantRepository assistantRepository;

    public List<PublicTeacherInfoRestResponse> getPublicTeachers() {
        return teacherRepository.findAll().stream()
                .map(this::toPublic)
                .toList();
    }

    public List<PublicAssistantInfoRestResponse> getPublicAssistants() {
        return assistantRepository.findAll().stream()
                .map(this::toPublic)
                .toList();
    }

    private PublicTeacherInfoRestResponse toPublic(TeacherEntity teacher) {
        return PublicTeacherInfoRestResponse.builder()
                .id(teacher.getId())
                .initial(teacher.getName() + " " + teacher.getSecondName() + " " + teacher.getSurname())
                .build();
    }

    private PublicAssistantInfoRestResponse toPublic(AssistantEntity assistant) {
        return PublicAssistantInfoRestResponse.builder()
                .id(assistant.getId())
                .initial(assistant.getName() + " " + assistant.getSecondName() + " " + assistant.getSurname())
                .build();
    }
}
