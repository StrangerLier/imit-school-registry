package omsu.mim.imit.school.registry.util.excel;

import lombok.AllArgsConstructor;
import omsu.mim.imit.school.registry.data.entity.ChildEntity;
import omsu.mim.imit.school.registry.data.entity.DirectionEntity;
import omsu.mim.imit.school.registry.data.repository.ChildRepository;
import omsu.mim.imit.school.registry.data.repository.DirectionRepository;
import omsu.mim.imit.school.registry.data.repository.GroupRepository;
import omsu.mim.imit.school.registry.data.repository.TeacherRepository;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ChildrenByDirExcel extends BaseExcel {

    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;
    private final DirectionRepository directionRepository;
    private final ChildRepository childRepository;

    private final AllDirectionsExcel allDirectionsExcel;
    private final AllChildrenExcel allChildrenExcel;

    public void fillRows(Workbook workbook, List<UUID> directionsIds) {

        var directions = directionRepository.findAllById(directionsIds);
        var groups = groupRepository.getByDirectionsIds(directionsIds);
        var children = childRepository.getAllByDirIds(directionsIds);
        var teachers = teacherRepository.findAll();

        for(DirectionEntity dir : directions) {
            Sheet sheet = workbook.createSheet(dir.getName());

            sheet.setDefaultColumnWidth(16);
            sheet.setColumnWidth(8, 2000);

            var childrenByDir = children.stream()
                    .filter(child -> {
                        var group = getByIdFromList(groups, child.getGroupId());
                        return group.getDirectionId().equals(dir.getId());
                    })
                    .toList();

            initHeaders(sheet, allDirectionsExcel.HEADERS);

            allDirectionsExcel.fillRow(sheet, dir, childrenByDir.size());

            initHeaders(sheet, allChildrenExcel.HEADERS);

            for (ChildEntity child : childrenByDir) {
                allChildrenExcel.fillRow(sheet, child, groups, teachers, directions);
            }
        }
    }
}
