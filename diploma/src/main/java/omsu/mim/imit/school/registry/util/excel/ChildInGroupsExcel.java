package omsu.mim.imit.school.registry.util.excel;

import lombok.AllArgsConstructor;
import omsu.mim.imit.school.registry.data.entity.ChildEntity;
import omsu.mim.imit.school.registry.data.entity.GroupEntity;
import omsu.mim.imit.school.registry.data.repository.DirectionRepository;
import omsu.mim.imit.school.registry.data.repository.GroupRepository;
import omsu.mim.imit.school.registry.data.repository.TeacherRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ChildInGroupsExcel extends BaseExcel {

    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;
    private final DirectionRepository directionRepository;

    private final AllGroupsExcel allGroupsExcel;

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
                "Дата рождения"
        );
    }

    public void fillRows(Workbook workbook, List<UUID> groupIds) {
        var groups = groupRepository.findAllById(groupIds)
                .stream()
                .collect(Collectors.toUnmodifiableSet());
        var children = groupRepository.getChildInGroups(groupIds);
        var teachers = teacherRepository.findAll();
        var directions = directionRepository.findAll();

        for (GroupEntity group : groups) {
            String sheetTitle = getByIdFromList(directions, group.getDirectionId()).getName() + " "
                    + getByIdFromList(teachers, group.getTeacherId()).getName() + " "
                    + group.getDayOfWeek() + " "
                    + group.getTime().replace(":", ".");

            Sheet sheet = workbook.createSheet(sheetTitle);
            sheet.setDefaultColumnWidth(16);

            initHeaders(sheet, allGroupsExcel.HEADERS);

            allGroupsExcel.fillRow(sheet, group, teachers, directions);

            initHeaders(sheet, HEADERS);

            for (ChildEntity child : children) {
                fillRow(sheet, child);
            }
        }
    }

    public void fillRow(Sheet sheet, ChildEntity child){
        Row dataRow = sheet.createRow(sheet.getLastRowNum() + 1);

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
                child.getBirthDate().toString()
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
                        String birthday) {}

}
