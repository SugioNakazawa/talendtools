package jp.gr.java_conf.nkzw.talendtools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Talend コンポーネント一覧 エクセルファイル
 */
public class TlWorkbook {
    private int START_ROW = 0;
    private int HEADER_ROWS = 1;

    private Workbook workbook;
    private Sheet sheet;
    private CellStyle cellstyleHeader;

    public TlWorkbook() {
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet();
        // フォント
        // Font font = workbook.createFont();
        // font.setFontName("Osaka");
        // font.setFontName("HG行書体");
        // ヘッダースタイル
        this.cellstyleHeader = workbook.createCellStyle();
        this.cellstyleHeader.setFillForegroundColor(IndexedColors.YELLOW.index);
        this.cellstyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // this.cellstyleHeader.setFont(font);
    }

    public void outputWorkBook(TlProjct project, String outputDir) throws IOException {
        // ヘッダ編集
        createHeader();
        // ボディ編集
        int i_row = START_ROW + HEADER_ROWS;
        int i_counter = 1;
        Row row = null;
        Cell cell = null;
        for (TlJob job : project.getJobList()) {
            for (TlComponent component : job.getComponentList()) {
                row = sheet.createRow(i_row++);

                cell = row.createCell(0);
                cell.setCellValue(i_counter++);

                cell = row.createCell(1);
                cell.setCellValue(job.getJobName());

                cell = row.createCell(2);
                cell.setCellValue(component.getId());

                cell = row.createCell(3);
                cell.setCellValue(component.getType());

                cell = row.createCell(4);
                cell.setCellValue(component.getExecNum());

                cell = row.createCell(5);
                cell.setCellValue(component.getErapsmsec());
            }
        }
        // 自動セル幅
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }
        // 出力
        FileOutputStream out = new FileOutputStream(outputDir + "/" + project.getProjectName() + ".xlsx");
        workbook.write(out);
        out.close();
    }

    public void createHeader() {
        // ヘッダー編集
        Row row = this.sheet.createRow(START_ROW);

        Cell cell = row.createCell(0);
        cell.setCellValue("#");
        cell.setCellStyle(this.cellstyleHeader);

        cell = row.createCell(1);
        cell.setCellValue("job");
        cell.setCellStyle(this.cellstyleHeader);

        cell = row.createCell(2);
        cell.setCellValue("component_id");
        cell.setCellStyle(this.cellstyleHeader);

        cell = row.createCell(3);
        cell.setCellValue("component_type");
        cell.setCellStyle(this.cellstyleHeader);

        cell = row.createCell(4);
        cell.setCellValue("exec_num");
        cell.setCellStyle(this.cellstyleHeader);

        cell = row.createCell(5);
        cell.setCellValue("eraps_msec");
        cell.setCellStyle(this.cellstyleHeader);
    }

}
