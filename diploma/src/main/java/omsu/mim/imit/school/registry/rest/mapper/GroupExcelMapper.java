package omsu.mim.imit.school.registry.rest.mapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import omsu.mim.imit.school.registry.util.excel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupExcelMapper {

    @Autowired
    private AllGroupsExcel allGroupsExcel;
    @Autowired
    private ChildInGroupsExcel childInGroupsExcel;
    @Autowired
    private AllChildrenExcel allChildrenExcel;
    @Autowired
    private ChildrenByDirExcel childrenByDirExcel;
    @Autowired
    private JournalExcel journalExcel;

    public ByteArrayInputStream allGroupsToExcel() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            allGroupsExcel.fillRows(workbook);
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.fillInStackTrace();
            System.out.println("EXCEL ERROR");
        }
        return null;
    }

    public ByteArrayInputStream childInGroupsToExcel(List<UUID> groupsIds) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            childInGroupsExcel.fillRows(workbook, groupsIds);
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.fillInStackTrace();
            System.out.println("EXCEL ERROR");
        }
        return null;
    }

    public ByteArrayInputStream allChildToExcel() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            allChildrenExcel.fillRows(workbook);
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.fillInStackTrace();
            System.out.println("EXCEL ERROR");
        }
        return null;
    }

    public ByteArrayInputStream childByDirToExcel(List<UUID> directionsIds) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            childrenByDirExcel.fillRows(workbook, directionsIds);
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.fillInStackTrace();
            System.out.println("EXCEL ERROR");
        }
        return null;
    }

    public ByteArrayInputStream journalToExcel(List<UUID> groupIds) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            journalExcel.fillRows(workbook, groupIds);
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.fillInStackTrace();
            System.out.println("EXCEL ERROR");
        }
        return null;
    }
}