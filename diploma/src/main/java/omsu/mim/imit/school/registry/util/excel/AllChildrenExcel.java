package omsu.mim.imit.school.registry.util.excel;

import lombok.AllArgsConstructor;
import omsu.mim.imit.school.registry.data.entity.ChildEntity;
import omsu.mim.imit.school.registry.data.entity.DirectionEntity;
import omsu.mim.imit.school.registry.data.entity.GroupEntity;
import omsu.mim.imit.school.registry.data.entity.TeacherEntity;
import omsu.mim.imit.school.registry.data.repository.ChildRepository;
import omsu.mim.imit.school.registry.data.repository.DirectionRepository;
import omsu.mim.imit.school.registry.data.repository.GroupRepository;
import omsu.mim.imit.school.registry.data.repository.TeacherRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class AllChildrenExcel extends BaseExcel{

    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;
    private final DirectionRepository directionRepository;
    private final ChildRepository childRepository;

    {
        HEADERS = List.of(
                "Фамилия",
                "Имя",
                "Отчество",
                "Родитель",
                "Телефон родителя",
                "Телефон ребенка",
                "Почта ребенка",
                "Школа",
                "Класс",
                "Адрес",
                "Дата рождения",
                "Направление",
                "Время занятий",
                "Адрес занятий",
                "Преподаватель"
        );
        SHEET_NAME = "Учащиеся";
    }

    public void fillRows(Workbook workbook) {
        Sheet sheet = workbook.createSheet(SHEET_NAME);

        sheet.setDefaultColumnWidth(16);
        sheet.setColumnWidth(8, 2000);

        initHeaders(sheet, HEADERS);

        List<GroupEntity> groups = groupRepository.findAll();
        List<TeacherEntity> teachers = teacherRepository.findAll();
        List<DirectionEntity> directions = directionRepository.findAll();

        var children = childRepository.findAll();

        for (ChildEntity child : children) {
            fillRow(sheet, child, groups, teachers, directions);
        }

    }

    void fillRow(Sheet sheet,
                 ChildEntity child,
                 List<GroupEntity> groups,
                 List<TeacherEntity> teachers,
                 List<DirectionEntity> directions) {

        Row dataRow = sheet.createRow(sheet.getLastRowNum() + 1);

        var group = getByIdFromList(groups, child.getGroupId());
        var direction = getByIdFromList(directions, group.getDirectionId());
        var teacher = getByIdFromList(teachers, group.getTeacherId());

        Rows rows = new Rows(
                child.getSurname(),
                child.getName(),
                child.getSecondName(),
                child.getParent(),
                child.getParentPhone(),
                child.getPhone(),
                child.getEmail(),
                child.getSchool(),
                child.getClassNumber(),
                child.getAddress(),
                child.getBirthDate().toString(),
                direction.getName(),
                group.getDayOfWeek() + " " + group.getTime(),
                group.getAddress(),
                teacher.getName()
        );

        dataRow.createCell(dataRow.getLastCellNum() + 1)
                .setCellValue(rows.secondName);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.firstName);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.patronymic);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.parent);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.parentPhone);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.phone);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.email);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.school);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.classNumber);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.address);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.birthday);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.direction);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.classTime);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.classAddress);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.teacher);
    }

    private record Rows(String secondName,
                        String firstName,
                        String patronymic,
                        String parent,
                        String parentPhone,
                        String phone,
                        String email,
                        String school,
                        String classNumber,
                        String address,
                        String birthday,
                        String direction,
                        String classTime,
                        String classAddress,
                        String teacher) {}
}
