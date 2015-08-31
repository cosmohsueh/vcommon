package com.vteam.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Excelpoi {

	private String filename;
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	private int sheetidx;

	public Excelpoi(String filename) throws Exception {
		this(filename, 0);
	}

	public Excelpoi(String filename, int sheetindex) throws Exception {
		InputStream inp = null;
		this.filename = filename;
		this.sheetidx = sheetindex;
		try {
			inp = new FileInputStream(filename);
			workbook = new HSSFWorkbook(inp);
			sheet = workbook.getSheetAt(sheetindex);
		} catch (Exception e) {
			throw new Exception((new StringBuilder("error read excel file:")).append(e).toString());
		} finally {
			if (inp != null) {
				try {
					inp.close();
				} catch (Exception e) {
					throw e;
				}
			}
		}
	}

	public int getCurrentRows() {
		int rows = this.sheet.getLastRowNum() + 1;
		return rows;
	}

	public String getStringValue(int rowidx, int columnidx) throws Exception {
		String value = null;
		Object obj = getValue(rowidx, columnidx);
		if ((obj instanceof String)) {
			value = (String) obj;
		}
		return value;
	}

	public Double getDoubleValue(int rowidx, int columnidx) throws Exception {
		Double value = null;
		Object obj = getValue(rowidx, columnidx);
		if ((obj instanceof Double)) {
			value = (Double) obj;
		}
		return value;
	}

	public Boolean getBooleanValue(int rowidx, int columnidx) throws Exception {
		Boolean value = null;
		Object obj = getValue(rowidx, columnidx);
		if ((obj instanceof Boolean)) {
			value = (Boolean) obj;
		}
		return value;
	}

	public Object getValue(int rowidx, int columnidx) throws Exception {
		Object obj = null;
		try {
			HSSFRow row = this.sheet.getRow(rowidx);
			if (row == null){
				return null;
			}
			HSSFCell cell = row.getCell(columnidx);
			if (cell == null){
				return null;
			}
			int ctype = cell.getCellType();
			if (ctype == 4){
				return new Boolean(cell.getBooleanCellValue());
			}
			if (ctype == 0) {
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					return HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
				}
				return new Double(cell.getNumericCellValue());
			}
			if (ctype == 1) {
				return cell.getRichStringCellValue().getString().trim();
			}
		} catch (Exception e) {
			throw new Exception("error getvalue:" + e);
		}
		return obj;
	}

	public void setValue(int rowidx, int columnidx, Object value)
			throws Exception {
		if (value == null){
			return;
		}
		try {
			HSSFRow row = this.sheet.getRow(rowidx);
			if (row == null) {
				row = this.sheet.createRow(rowidx);
			}
			HSSFCell cell = row.getCell(columnidx);
			if (cell == null) {
				cell = row.createCell(columnidx);
			}
			if ((value instanceof Number)) {
				cell.setCellValue(((Number) value).doubleValue());
			} else if ((value instanceof Date)) {
				cell.setCellValue((Date) value);
			} else if ((value instanceof Boolean)) {
				cell.setCellValue(((Boolean) value).booleanValue());
			} else {
				cell.setCellValue(new HSSFRichTextString(value.toString()));
			}
		} catch (Exception e) {
			throw new Exception("error setvalue:" + e);
		}
	}

	public void insertRow(int startRow, int rows) {
		this.sheet.shiftRows(startRow + 1, this.sheet.getLastRowNum(), rows, true, false);

		for (int i = 0; i < rows; i++) {
			HSSFRow sourceRow = null;
			HSSFRow targetRow = null;

			sourceRow = this.sheet.getRow(startRow);
			targetRow = this.sheet.createRow(++startRow);

			for (int m = sourceRow.getFirstCellNum(); m < sourceRow	.getPhysicalNumberOfCells(); m++) {
				HSSFCell sourceCell = sourceRow.getCell(m);
				HSSFCell targetCell = targetRow.createCell(m);

				targetCell.setCellStyle(sourceCell.getCellStyle());
				targetCell.setCellType(sourceCell.getCellType());
			}
		}
	}

	public void writeto(String filename) throws Exception {
		FileOutputStream fs = null;
		try {
			File f = new File(filename);
			if (f.isFile()) {
				f.createNewFile();
			}
			fs = new FileOutputStream(f);
			workbook.write(fs);
		} catch (Exception e) {
			throw new Exception((new StringBuilder("error write excel file:")).append(e).toString());
		} finally {
			if (fs != null) {
				try {
					fs.close();
				} catch (Exception e) {
					throw e;
				}
			}
		}
	}

	public HSSFWorkbook getWorkbook() {
		return this.workbook;
	}

	public void setWorkbook(HSSFWorkbook workbook) {
		this.workbook = workbook;
	}

	public HSSFSheet getSheet() {
		return this.sheet;
	}

	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getSheetidx() {
		return this.sheetidx;
	}

	public void setSheetidx(int sheetidx) {
		this.sheetidx = sheetidx;
		this.sheet = this.workbook.getSheetAt(sheetidx);
	}
}