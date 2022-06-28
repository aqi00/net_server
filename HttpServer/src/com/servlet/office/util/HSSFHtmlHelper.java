package com.servlet.office.util;

import java.util.Formatter;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.CellStyle;

public class HSSFHtmlHelper implements HtmlHelper {
    private final HSSFWorkbook wb;
    private final HSSFPalette colors;
    private static final HSSFColor HSSF_AUTO;

    public HSSFHtmlHelper(HSSFWorkbook wb) {
        this.wb = wb;
        this.colors = wb.getCustomPalette();
    }

    public void colorStyles(CellStyle style, Formatter out) {
        HSSFCellStyle cs = (HSSFCellStyle) style;
        out.format("  /* fill pattern = %d */%n", cs.getFillPattern().getCode());
        this.styleColor(out, "background-color", cs.getFillForegroundColor());
        this.styleColor(out, "color", cs.getFont(this.wb).getColor());
        this.styleColor(out, "border-left-color", cs.getLeftBorderColor());
        this.styleColor(out, "border-right-color", cs.getRightBorderColor());
        this.styleColor(out, "border-top-color", cs.getTopBorderColor());
        this.styleColor(out, "border-bottom-color", cs.getBottomBorderColor());
    }

    private void styleColor(Formatter out, String attr, short index) {
        HSSFColor color = this.colors.getColor(index);
        if (index != HSSF_AUTO.getIndex() && color != null) {
            short[] rgb = color.getTriplet();
            out.format("  %s: #%02x%02x%02x; /* index = %d */%n", attr, rgb[0], rgb[1], rgb[2], index);
        } else {
            out.format("  /* %s: index = %d */%n", attr, index);
        }

    }

    static {
        HSSF_AUTO = HSSFColorPredefined.AUTOMATIC.getColor();
    }
}