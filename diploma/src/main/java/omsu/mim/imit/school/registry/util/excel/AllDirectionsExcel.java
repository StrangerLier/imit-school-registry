package omsu.mim.imit.school.registry.util.excel;

import lombok.AllArgsConstructor;
import omsu.mim.imit.school.registry.data.entity.*;
import omsu.mim.imit.school.registry.data.repository.ChildRepository;
import omsu.mim.imit.school.registry.data.repository.DirectionRepository;
import omsu.mim.imit.school.registry.data.repository.GroupRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class AllDirectionsExcel extends BaseExcel {

    private final GroupRepository groupRepository;
    private final DirectionRepository directionRepository;
    private final ChildRepository childRepository;

    {
        HEADERS = List.of(
                "Направление",
                "Всего записано"
        );
        SHEET_NAME = "Направления";
    }

    public void fillRows(Workbook workbook) {
        Sheet sheet = workbook.createSheet(SHEET_NAME);

        sheet.setDefaultColumnWidth(16);
        sheet.setColumnWidth(8, 2000);

        initHeaders(sheet, HEADERS);

        var directions = directionRepository.findAll();
        var groups = groupRepository.findAll();
        var children = childRepository.findAll();

        long childByDirCount;

        for (DirectionEntity direction : directions) {
            childByDirCount = countChildrenByDir(direction, children, groups);
            fillRow(sheet, direction, childByDirCount);
        }

    }

    void fillRow(Sheet sheet,
                 DirectionEntity direction,
                 long childByDirCount) {

        Row dataRow = sheet.createRow(sheet.getLastRowNum() + 1);

        Rows rows = new Rows(
                direction.getName(),
                childByDirCount
        );

        dataRow.createCell(dataRow.getLastCellNum() + 1)
                .setCellValue(rows.name);
        dataRow.createCell(dataRow.getLastCellNum())
                .setCellValue(rows.listenersAmount);
    }

    public long countChildrenByDir(DirectionEntity direction, List<ChildEntity> children, List<GroupEntity> groups) {
        return children.stream()
                .filter(child -> {
                    var group = getByIdFromList(groups, child.getGroupId());
                    return group.getDirectionId().equals(direction.getId());
                })
                .count();
    }

    private record Rows(String name,
                        long listenersAmount) {}

}
