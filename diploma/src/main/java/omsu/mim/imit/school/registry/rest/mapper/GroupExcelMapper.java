package omsu.mim.imit.school.registry.rest.mapper;

import omsu.mim.imit.school.registry.data.entity.GroupEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class GroupExcelMapper {

    public static String[] HEADERS = {
            "id",
            "address",
            "time",
            "lecturer"
    };

    public static String SHEET_NAME = "sheetName";

    public static ByteArrayInputStream dataToExcel(List<GroupEntity> list) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try{
            Sheet sheet = workbook.createSheet(SHEET_NAME);

            Row row = sheet.createRow(0);

            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(HEADERS[i]);
            }

            int rowInd = 1;

            for(GroupEntity c : list) {
                Row dataRow = sheet.createRow(rowInd);
                rowInd++;

                dataRow.createCell(0).setCellValue(c.getId().toString());
                dataRow.createCell(1).setCellValue(c.getAddress());
                dataRow.createCell(2).setCellValue(c.getTime());
                dataRow.createCell(3).setCellValue(c.getLecturer());

            }

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("EXCEL ERROR");
        } finally {
            workbook.close();
            out.close();
        }

        return null;

    }

}
