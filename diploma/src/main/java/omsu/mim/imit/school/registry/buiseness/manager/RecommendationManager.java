package omsu.mim.imit.school.registry.buiseness.manager;

import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.util.HungarianAlgorithmUtil;
import omsu.mim.imit.school.registry.data.entity.AssistantEntity;
import omsu.mim.imit.school.registry.data.entity.TeacherEntity;
import omsu.mim.imit.school.registry.data.repository.AssistantRepository;
import omsu.mim.imit.school.registry.data.repository.TeacherRepository;
import omsu.mim.imit.school.registry.rest.dto.response.AssistantRestResponse;
import omsu.mim.imit.school.registry.rest.dto.response.RecommendationRestResponse;
import omsu.mim.imit.school.registry.rest.dto.response.TeacherRestResponse;
import omsu.mim.imit.school.registry.rest.mapper.AssistantRestResponseMapper;
import omsu.mim.imit.school.registry.rest.mapper.TeacherRestResponseMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationManager {

    private final TeacherRepository teacherRepository;
    private final AssistantRepository assistantRepository;
    private final TeacherRestResponseMapper teacherRestResponseMapper;
    private final AssistantRestResponseMapper assistantRestResponseMapper;

    public RecommendationRestResponse createRecommendation() {
        var teachers = teacherRepository.findAll();
        var assistants = assistantRepository.findAll();
        var matrix = ratingMatrix(teachers, assistants);
        var correspondenceArray = HungarianAlgorithmUtil.kuhnMunkres(matrix);

        return crateResponse(correspondenceArray, teachers, assistants);
    }

    private int[][] ratingMatrix(List<TeacherEntity> teachers, List<AssistantEntity> assistants) {
        var teachersRating = teachers.stream()
                .map(TeacherEntity::getRating)
                .toList();
        var assistantsRating = assistants.stream()
                .map(AssistantEntity::getRating)
                .toList();
        var matrixSize = Math.max(teachersRating.size(), assistantsRating.size());
        var matrix = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                var teacher = signElement(teachersRating, i);
                var assistant = signElement(assistantsRating, j);

                if (teacher == 0 || assistant == 0) {
                    matrix[i][j] = 0;
                    continue;
                }

                matrix[i][j] = teacher + assistant;
            }
        }

        return matrix;
    }

    private <T> int signElement(List<Integer> list, Integer index) {
        int result;

        try {
            result = list.get(index);
        } catch (ArrayIndexOutOfBoundsException exception) {
            result = 0;
        }

        return result;
    }

    private RecommendationRestResponse crateResponse(int[] correspondenceArray, List<TeacherEntity> teachers, List<AssistantEntity> assistants) {
        var teachersSize = teachers.size();
        var assistantsSize = assistants.size();
        var isTeachersMore = teachersSize > assistantsSize;

        var minSize = isTeachersMore ? assistantsSize : teachersSize;
        var recommendationMap = new HashMap<String, String>();

        for (int i = 0; i < minSize; i++) {
            recommendationMap.put(
                    extractInitials(teachers.get(i)),
                    extractInitials(assistants.get(correspondenceArray[i]))
            );
        }



        if (isTeachersMore) {

            return RecommendationRestResponse.<TeacherRestResponse>builder()
                    .recommendationsMap(recommendationMap)
                    .workersWithoutMatch(
                        teachers.stream()
                            .skip(minSize)
                            .map(teacherRestResponseMapper::map)
                            .toList()
                    )
                    .build();
        } else {

            return RecommendationRestResponse.<AssistantRestResponse>builder()
                    .recommendationsMap(recommendationMap)
                    .workersWithoutMatch(
                            assistants.stream()
                                    .skip(minSize)
                                    .map(assistantRestResponseMapper::map)
                                    .toList()
                    )
                    .build();
        }
    }

    private String extractInitials(TeacherEntity teacher) {
        return teacher.getName() + " " + teacher.getSecondName() + " " +  teacher.getSurname();
    }

    private String extractInitials(AssistantEntity assistant) {
        return assistant.getName() + " " + assistant.getSecondName() + " " +  assistant.getSurname();
    }
}
