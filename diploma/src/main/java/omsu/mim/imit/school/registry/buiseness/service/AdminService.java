package omsu.mim.imit.school.registry.buiseness.service;

import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.mapper.AssistantMapper;
import omsu.mim.imit.school.registry.buiseness.mapper.TeacherMapper;
import omsu.mim.imit.school.registry.data.repository.AssistantRepository;
import omsu.mim.imit.school.registry.data.repository.TeacherRepository;
import omsu.mim.imit.school.registry.rest.dto.request.AssistantRequestDto;
import omsu.mim.imit.school.registry.rest.dto.request.TeacherRequestDto;
import omsu.mim.imit.school.registry.rest.dto.response.AssistantRestResponse;
import omsu.mim.imit.school.registry.rest.dto.response.TeacherRestResponse;
import omsu.mim.imit.school.registry.rest.mapper.AssistantRestResponseMapper;
import omsu.mim.imit.school.registry.rest.mapper.TeacherRestResponseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final TeacherRepository teacherRepository;
    private final AssistantRepository assistantRepository;

    private final TeacherMapper teacherMapper;
    private final AssistantMapper assistantMapper;

    private final TeacherRestResponseMapper teacherRestResponseMapper;
    private final AssistantRestResponseMapper assistantRestResponseMapper;

    public void registerTeacher(TeacherRequestDto request) {
        teacherRepository.save(teacherMapper.map(request));
    }

    public void registerAssistant(AssistantRequestDto request) {
        assistantRepository.save(assistantMapper.map(request));
    }

    public List<TeacherRestResponse> getTeachers() {
        return teacherRestResponseMapper.mapAll(teacherRepository.findAll());
    }

    public List<AssistantRestResponse> getAssistants() {
        return assistantRestResponseMapper.mapAll(assistantRepository.findAll());
    }
}
