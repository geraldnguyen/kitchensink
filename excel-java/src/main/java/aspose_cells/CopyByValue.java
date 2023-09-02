package aspose_cells;

import com.aspose.cells.CopyOptions;
import com.aspose.cells.Worksheet;

public class CopyByValue {
    public void copy(Worksheet from, Worksheet to) throws Exception {
        from.getWorkbook().calculateFormula();
        var copyOption = new CopyOptions();
        copyOption.setCopyInvalidFormulasAsValues(true);

        to.copy(from, copyOption);
        to.getCells().removeFormulas();
    }
}
