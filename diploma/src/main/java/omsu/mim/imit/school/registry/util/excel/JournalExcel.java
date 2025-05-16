package omsu.mim.imit.school.registry.util.excel;

import lombok.AllArgsConstructor;
import omsu.mim.imit.school.registry.data.entity.AttendanceEntity;
import omsu.mim.imit.school.registry.data.entity.ChildEntity;
import omsu.mim.imit.school.registry.data.entity.ClassEntity;
import omsu.mim.imit.school.registry.data.entity.GroupEntity;
import omsu.mim.imit.school.registry.data.repository.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

@Component
@AllArgsConstructor
public class JournalExcel extends BaseExcel {

    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;
    private final DirectionRepository directionRepository;
    private final ChildRepository childRepository;
    private final ClassRepository classRepository;
    private final AttendanceRepository attendanceRepository;

    private final AllGroupsExcel allGroupsExcel;
    private final AllDirectionsExcel allDirectionsExcel;
    private final AllChildrenExcel allChildrenExcel;

    {
        SHEET_NAME = "Журнал";

        HEADERS = List.of(
                "Фамилия",
                "Имя",
                "Телефон",
                "Телефон родителя"
        );
    }

    public void fillRows(Workbook workbook, List<UUID> groupIds) {

        var groups = groupRepository.findAllById(groupIds);
        var directions = directionRepository.findAll();
        var children = childRepository.findAll();
        var teachers = teacherRepository.findAll();
        var classes = classRepository.findAll();
        var attendances = attendanceRepository.findAll();

        for(GroupEntity group : groups) {

            var direction = getByIdFromList(directions, group.getDirectionId());
            var teacher = getByIdFromList(teachers, group.getTeacherId());
            var classesByGroup = classes.stream()
                    .filter(classEntity -> classEntity.getGroupId().equals(group.getId()))
                    .toList();

            var sheetTitle = SHEET_NAME + " " + direction.getName() + " " + teacher.getName();
            Sheet sheet = workbook.createSheet(sheetTitle);

            sheet.setDefaultColumnWidth(16);
            sheet.setColumnWidth(8, 2000);

            allGroupsExcel.initHeaders(sheet, allGroupsExcel.HEADERS);

            allGroupsExcel.fillRow(sheet, group, teachers, directions);

            initHeaders(sheet, HEADERS, classes);

            var childrenByGroup = children.stream()
                    .filter(child -> child.getGroupId().equals(group.getId()))
                    .toList();

            for (ChildEntity child : childrenByGroup) {
                fillRow(sheet, child, attendances);
            }

        }
    }

    public void initHeaders(Sheet sheet, List<String> headers, List<ClassEntity> classes) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        headers.forEach(header -> {
            var lastCell = row.getLastCellNum();
            Cell cell = row.createCell(lastCell == -1 ? 0 : lastCell);
            cell.setCellValue(header);
        });

        var sortedByDateClasses = classes.stream()
                .sorted(Comparator.comparing(ClassEntity::getClassDateTime))
                .toList();

        for (ClassEntity classEntity : sortedByDateClasses) {
            var lastCell = row.getLastCellNum();
            Cell cell = row.createCell(lastCell == -1 ? 0 : lastCell);

            Date date = Date.from(classEntity.getClassDateTime().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());

            String strDate = new SimpleDateFormat("dd MMMM", Locale.getDefault()).format(date);

            cell.setCellValue(strDate);
        }
    }

    public void fillRow(Sheet sheet,
                        ChildEntity child,
                        List<AttendanceEntity> attendances) {

        var attendancesByChild = attendances
                .stream()
                .filter(attendance -> attendance.getChildId().equals(child.getId()))
                .map(AttendanceEntity::getIsAttend)
                .map(isAttend -> isAttend == null ? "" : isAttend ? "+" : "-")
                .toList();

        Rows rows = new Rows(
                    child.getSurname(),
                    child.getName(),
                    child.getPhone(),
                    child.getParentPhone(),
                    attendancesByChild
                );

        Row dataRow = sheet.createRow(sheet.getLastRowNum() + 1);

        dataRow.createCell(dataRow.getLastCellNum() + 1)
                .setCellValue(rows.surname);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.name);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.phone);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.parentPhone);
        for (String isAttend : rows.attendances) {
            dataRow.createCell(dataRow.getLastCellNum())
                    .setCellValue(isAttend);
        }
    }

    private record Rows(String surname,
                        String name,
                        String phone,
                        String parentPhone,
                        List<String> attendances){}
}
