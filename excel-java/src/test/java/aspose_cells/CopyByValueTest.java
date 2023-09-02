package aspose_cells;

import com.aspose.cells.CellsFactory;
import com.aspose.cells.Style;
import com.aspose.cells.Workbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CopyByValueTest {
    private CopyByValue action;

    @BeforeEach
    void setup() {
        action = new CopyByValue();
    }

    void saveWorkbook(Workbook wb, Path file) throws Exception {
        wb.save(file.toAbsolutePath().toString());
    }

    @Test
    void copyStaticValues() throws Exception {

        // arrange: source
        var sourceWB = new Workbook();
        var sourceWS = sourceWB.getWorksheets().add("source");
        sourceWS.getCells().get("A1").putValue("A1");
        saveWorkbook(sourceWB, Files.createTempFile("source", ".xlsx"));

        // arrange: target
        var targetWB = new Workbook();
        var targetWS = targetWB.getWorksheets().add("target");

        // act
        action.copy(sourceWS, targetWS);
        saveWorkbook(targetWB, Files.createTempFile("target", ".xlsx"));

        // assert
        assertEquals("A1", targetWS.getCells().get("A1").getStringValue());
    }

    @Test
    void copyFormat() throws Exception {

        // arrange: source
        var sourceWB = new Workbook();
        var sourceWS = sourceWB.getWorksheets().add("source");

        var numberStyle = new CellsFactory().createStyle();
        numberStyle.setCustom("#,##0.000");
        sourceWS.getCells().get("A1").putValue(111111.222222222222222);
        sourceWS.getCells().get("A1").setStyle(numberStyle);
        saveWorkbook(sourceWB, Files.createTempFile("source", ".xlsx"));

        // arrange: target
        var targetWB = new Workbook();
        var targetWS = targetWB.getWorksheets().add("target");

        // act
        action.copy(sourceWS, targetWS);
        saveWorkbook(targetWB, Files.createTempFile("target", ".xlsx"));

        // assert
        assertEquals("111,111.222", targetWS.getCells().get("A1").getStringValue());
    }

    @Test
    void copyInvalidFormulasAsValues() throws Exception {

        // arrange: source
        var sourceWB = new Workbook();
        var aWS = sourceWB.getWorksheets().add("aWS");
        aWS.getCells().get("A2").putValue(2);
        var sourceWS = sourceWB.getWorksheets().add("source");
        sourceWS.getCells().get("A1").putValue(1);
        sourceWS.getCells().get("A3").setFormula("=A1+aWS!A2");
        saveWorkbook(sourceWB, Files.createTempFile("source", ".xlsx"));

        // arrange: target
        var targetWB = new Workbook();
        var targetWS = targetWB.getWorksheets().add("target");

        // act
        action.copy(sourceWS, targetWS);
        saveWorkbook(targetWB, Files.createTempFile("target", ".xlsx"));

        // assert
        assertEquals(3, sourceWS.getCells().get("A3").getIntValue());
        assertEquals("=A1+aWS!A2", sourceWS.getCells().get("A3").getFormula());

        assertEquals(3, targetWS.getCells().get("A3").getIntValue());
        assertNull(targetWS.getCells().get("A3").getFormula());
    }

    @Test
    void copyValidFormulaAsValue() throws Exception {

        // arrange: source
        var sourceWB = new Workbook();
        var sourceWS = sourceWB.getWorksheets().add("source");
        sourceWS.getCells().get("A1").putValue(1);
        sourceWS.getCells().get("A2").putValue(2);
        sourceWS.getCells().get("A3").setFormula("=A1+A2");
        saveWorkbook(sourceWB, Files.createTempFile("source", ".xlsx"));

        // arrange: target
        var targetFile = Files.createTempFile("target", ".xlsx");
        var targetWB = new Workbook();
        var targetWS = targetWB.getWorksheets().add("target");

        // act
        action.copy(sourceWS, targetWS);
        saveWorkbook(targetWB, Files.createTempFile("target", ".xlsx"));

        // assert
        assertEquals(3, sourceWS.getCells().get("A3").getIntValue());
        assertEquals("=A1+A2", sourceWS.getCells().get("A3").getFormula());

        assertEquals(3, targetWS.getCells().get("A3").getIntValue());
        assertNull(targetWS.getCells().get("A3").getFormula());
    }
}