package com.servlet.office.util;

import java.util.Formatter;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class XSSFHtmlHelper implements HtmlHelper {
    public XSSFHtmlHelper() {
    }

    public void colorStyles(CellStyle style, Formatter out) {
        XSSFCellStyle cs = (XSSFCellStyle)style;
        this.styleColor(out, "background-color", cs.getFillForegroundXSSFColor());
        this.styleColor(out, "text-color", cs.getFont().getXSSFColor());
    }

    private void styleColor(Formatter out, String attr, XSSFColor color) {
        if (color != null && !color.isAuto()) {
            byte[] rgb = color.getRGB();
            if (rgb != null) {
                out.format("  %s: #%02x%02x%02x;%n", attr, rgb[0], rgb[1], rgb[2]);
                byte[] argb = color.getARGB();
                if (argb != null) {
                    out.format("  %s: rgba(0x%02x, 0x%02x, 0x%02x, 0x%02x);%n", attr, argb[3], argb[0], argb[1], argb[2]);
                }
            }
        }
    }
}