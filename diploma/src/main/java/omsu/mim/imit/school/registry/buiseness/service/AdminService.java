package omsu.mim.imit.school.registry.buiseness.service;

import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.mapper.AssistantMapper;
import omsu.mim.imit.school.registry.buiseness.mapper.DirectionMapper;
import omsu.mim.imit.school.registry.buiseness.mapper.TeacherMapper;
import omsu.mim.imit.school.registry.data.entity.xml.HolidayEntity;
import omsu.mim.imit.school.registry.data.repository.AssistantRepository;
import omsu.mim.imit.school.registry.data.repository.DirectionRepository;
import omsu.mim.imit.school.registry.data.repository.HolidayRepository;
import omsu.mim.imit.school.registry.data.repository.TeacherRepository;
import omsu.mim.imit.school.registry.rest.dto.request.AssistantRequestDto;
import omsu.mim.imit.school.registry.rest.dto.request.DirectionRequestDto;
import omsu.mim.imit.school.registry.rest.dto.request.TeacherRequestDto;
import omsu.mim.imit.school.registry.rest.dto.response.AssistantRestResponse;
import omsu.mim.imit.school.registry.rest.dto.response.DirectionRestResponse;
import omsu.mim.imit.school.registry.rest.dto.response.TeacherRestResponse;
import omsu.mim.imit.school.registry.rest.mapper.AssistantRestResponseMapper;
import omsu.mim.imit.school.registry.rest.mapper.DirectionRestResponseMapper;
import omsu.mim.imit.school.registry.rest.mapper.TeacherRestResponseMapper;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final TeacherRestResponseMapper teacherRestResponseMapper;

    private final AssistantRepository assistantRepository;
    private final AssistantMapper assistantMapper;
    private final AssistantRestResponseMapper assistantRestResponseMapper;

    private final DirectionRepository directionRepository;
    private final DirectionMapper directionMapper;
    private final DirectionRestResponseMapper directionRestResponseMapper;

    private final HolidayRepository holidayRepository;

    public void registerTeacher(TeacherRequestDto request) {
        teacherRepository.save(teacherMapper.map(request));
    }

    public void registerAssistant(AssistantRequestDto request) {
        assistantRepository.save(assistantMapper.map(request));
    }

    public void addDirection(DirectionRequestDto request) {
        directionRepository.save(directionMapper.map(request));
    }

    public List<TeacherRestResponse> getTeachers() {
        return teacherRestResponseMapper.mapAll(teacherRepository.findAll());
    }

    public List<AssistantRestResponse> getAssistants() {
        return assistantRestResponseMapper.mapAll(assistantRepository.findAll());
    }

    public List<DirectionRestResponse> getDirections() {
        return directionRestResponseMapper.mapAll(directionRepository.findAll());
    }

    public void addHoliday(String[] holidays) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        var holidaysToSave = Arrays.stream(holidays)
                .map(holidayStr -> {
                    try {
                        LocalDate holidayDate = simpleDateFormat.parse(holidayStr)
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

                        return new HolidayEntity(UUID.randomUUID(), holidayDate);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        holidayRepository.saveAll(holidaysToSave);
    }

    public void removeHolidays(String[] holidaysIds) {
        var holidaysToRemove = Arrays.stream(holidaysIds)
                .map(UUID::fromString)
                .toList();
        holidayRepository.deleteAllById(holidaysToRemove);
    }
}
