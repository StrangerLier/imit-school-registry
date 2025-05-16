package omsu.mim.imit.school.registry.util.excel;

import omsu.mim.imit.school.registry.data.entity.BaseEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.UUID;

public class BaseExcel {

    List<String> HEADERS;
    String SHEET_NAME;

    public void initHeaders(Sheet sheet, List<String> headers) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        headers.forEach(header -> {
            var lastCell = row.getLastCellNum();
            Cell cell = row.createCell(lastCell == -1 ? 0 : lastCell);
            cell.setCellValue(header);
        });
    }

    public <T extends BaseEntity> T getByIdFromList(List<T> list, UUID id) {
        return list.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
