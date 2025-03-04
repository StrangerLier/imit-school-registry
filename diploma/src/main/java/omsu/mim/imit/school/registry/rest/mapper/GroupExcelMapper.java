package omsu.mim.imit.school.registry.rest.mapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import omsu.mim.imit.school.registry.data.entity.ChildEntity;
import omsu.mim.imit.school.registry.data.entity.GroupEntity;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GroupExcelMapper {

    private static final String[] ALL_GROUPS_HEADERS = {
            "Направление",
            "Преподаватель",
            "Время",
            "Адрес",
            "Класс",
            "Максимум участников",
            "Записалось участников"
    };

    private static final String[] CHILD_IN_GROUPS_HEADERS = {
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
    };

    private static final String[] ALL_CHILD_HEADERS = ArrayUtils.addAll(
            CHILD_IN_GROUPS_HEADERS,
            "Направление",
            "Время",
            "Адрес",
            "Преподаватель"
    );

    private static final String ALL_GROUPS_SHEET_NAME = "Группы";
    private static final String CHILD_IN_GROUPS_SHEET_NAME = "Группа";
    private static final String ALL_CHILD_SHEET_NAME = "Все школьники";

    public static ByteArrayInputStream allGroupsToExcel(List<GroupEntity> list) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(ALL_GROUPS_SHEET_NAME);

            Row row = sheet.createRow(0);

            initHeaders(ALL_GROUPS_HEADERS, row);

            int rowInd = 1;

            for (GroupEntity c : list) {
                Row dataRow = sheet.createRow(rowInd);
                rowInd++;
                dataRow.createCell(0).setCellValue(c.getDirection());
                dataRow.createCell(1).setCellValue(c.getLecturer());
                dataRow.createCell(2).setCellValue(c.getTime());
                dataRow.createCell(3).setCellValue(c.getAddress());
                dataRow.createCell(4).setCellValue(c.getClassNumber());
                dataRow.createCell(5).setCellValue(c.getListenersAmount());
                dataRow.createCell(6).setCellValue(c.getApprovedListeners());
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.fillInStackTrace();
            System.out.println("EXCEL ERROR");
        }
        return null;
    }

    public static ByteArrayInputStream childInGroupsToExcel(List<ChildEntity> child, Set<GroupEntity> groups) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            for (GroupEntity group : groups) {

                String sheetTitle = CHILD_IN_GROUPS_SHEET_NAME + " " + group.getDirection() + " " + group.getLecturer() + group.getTime();

                Sheet sheet = workbook.createSheet(sheetTitle);

                Row groupHeadersRow = sheet.createRow(0);

                initHeaders(ALL_GROUPS_HEADERS, groupHeadersRow);

                int rowInd = groupHeadersRow.getRowNum() + 1;

                Row dataRow = sheet.createRow(rowInd);
                rowInd++;

                dataRow.createCell(0).setCellValue(group.getDirection());
                dataRow.createCell(1).setCellValue(group.getLecturer());
                dataRow.createCell(2).setCellValue(group.getTime());
                dataRow.createCell(3).setCellValue(group.getAddress());
                dataRow.createCell(4).setCellValue(group.getClassNumber());
                dataRow.createCell(5).setCellValue(group.getListenersAmount());
                dataRow.createCell(6).setCellValue(group.getApprovedListeners());

                Row childHeadersRow = sheet.createRow(rowInd);

                rowInd++;

                initHeaders(CHILD_IN_GROUPS_HEADERS, childHeadersRow);

                for (ChildEntity c : child) {
                    if (!c.getGroupId().equals(group.getId())) {
                        continue;
                    }
                    Row childDataRow = sheet.createRow(rowInd);
                    rowInd++;
                    childDataRow.createCell(0).setCellValue(c.getSurname());
                    childDataRow.createCell(1).setCellValue(c.getName());
                    childDataRow.createCell(2).setCellValue(c.getSecondName());
                    childDataRow.createCell(3).setCellValue(c.getParent());
                    childDataRow.createCell(4).setCellValue(c.getParentPhone());
                    childDataRow.createCell(5).setCellValue(c.getPhone());
                    childDataRow.createCell(6).setCellValue(c.getEmail());
                    childDataRow.createCell(7).setCellValue(c.getSchool());
                    childDataRow.createCell(8).setCellValue(c.getClassNumber());
                    childDataRow.createCell(9).setCellValue(c.getAddress());
                    childDataRow.createCell(10).setCellValue(c.getBirthDate().toString());
                }
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.fillInStackTrace();
            System.out.println("EXCEL ERROR");
        }
        return null;
    }

    public static ByteArrayInputStream allChildToExcel(List<ChildEntity> child, Map<UUID, GroupEntity> groups) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(ALL_CHILD_SHEET_NAME);

            Row row = sheet.createRow(0);

            initHeaders(ALL_CHILD_HEADERS, row);

            int rowInd = 1;

            for (ChildEntity c : child) {
                Row dataRow = sheet.createRow(rowInd);
                rowInd++;
                dataRow.createCell(0).setCellValue(c.getSurname());
                dataRow.createCell(1).setCellValue(c.getName());
                dataRow.createCell(2).setCellValue(c.getSecondName());
                dataRow.createCell(3).setCellValue(c.getParent());
                dataRow.createCell(4).setCellValue(c.getParentPhone());
                dataRow.createCell(5).setCellValue(c.getPhone());
                dataRow.createCell(6).setCellValue(c.getEmail());
                dataRow.createCell(7).setCellValue(c.getSchool());
                dataRow.createCell(8).setCellValue(c.getClassNumber());
                dataRow.createCell(9).setCellValue(c.getAddress());
                dataRow.createCell(10).setCellValue(c.getBirthDate().toString());
                dataRow.createCell(11).setCellValue(groups.get(c.getGroupId()).getDirection());
                dataRow.createCell(12).setCellValue(groups.get(c.getGroupId()).getTime());
                dataRow.createCell(13).setCellValue(groups.get(c.getGroupId()).getAddress());
                dataRow.createCell(14).setCellValue(groups.get(c.getGroupId()).getLecturer());
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.fillInStackTrace();
            System.out.println("EXCEL ERROR");
        }
        return null;
    }

    private static void initHeaders(String[] headers, Row row) {
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
        }
    }
}