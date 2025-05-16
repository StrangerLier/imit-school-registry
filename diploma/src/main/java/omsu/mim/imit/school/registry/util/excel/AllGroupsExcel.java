package omsu.mim.imit.school.registry.util.excel;

import lombok.AllArgsConstructor;
import omsu.mim.imit.school.registry.data.entity.DirectionEntity;
import omsu.mim.imit.school.registry.data.entity.GroupEntity;
import omsu.mim.imit.school.registry.data.entity.TeacherEntity;
import omsu.mim.imit.school.registry.data.repository.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class AllGroupsExcel extends BaseExcel {

    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;
    private final DirectionRepository directionRepository;

    {
        HEADERS = List.of(
                "Направление",
                "Преподаватель",
                "Время",
                "Адрес",
                "Класс",
                "Максимум участников",
                "Записалось участников"
        );
        SHEET_NAME = "Группы";
    }

    public void fillRows(Workbook workbook) {
        Sheet sheet = workbook.createSheet(SHEET_NAME);
        sheet.setDefaultColumnWidth(16);

        initHeaders(sheet, HEADERS);

        List<GroupEntity> groups = groupRepository.findAll();
        List<TeacherEntity> teachers = teacherRepository.findAll();
        List<DirectionEntity> directions = directionRepository.findAll();

        for (GroupEntity group : groups) {
            fillRow(sheet, group, teachers, directions);
        }
    }

    void fillRow(Sheet sheet,
                                GroupEntity group,
                                List<TeacherEntity> teachers,
                                List<DirectionEntity> directions) {
        Row dataRow = sheet.createRow(sheet.getLastRowNum() + 1);

        Rows rows = new Rows(
                getByIdFromList(directions, group.getDirectionId()).getName(),
                getByIdFromList(teachers, group.getTeacherId()).getName(),
                group.getDayOfWeek() + " " + group.getTime(),
                group.getAddress(),
                group.getClassNumber(),
                group.getListenersAmount(),
                group.getApprovedListeners()
        );

        dataRow.createCell(dataRow.getLastCellNum() + 1)
                .setCellValue(rows.direction);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.teacher);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.time);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.address);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.classNumber);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.listenersAmount);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.approvedListeners);
    }

    private record Rows(String direction,
                        String teacher,
                        String time,
                        String address,
                        String classNumber,
                        Integer listenersAmount,
                        Integer approvedListeners) { }

}
