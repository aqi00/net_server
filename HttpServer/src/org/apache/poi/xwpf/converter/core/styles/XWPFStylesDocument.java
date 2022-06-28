/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.apache.poi.xwpf.converter.core.styles;

import org.apache.poi.xwpf.converter.core.Color;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.xwpf.converter.core.BorderSide;
import org.apache.poi.xwpf.converter.core.ParagraphLineSpacing;
import org.apache.poi.xwpf.converter.core.TableCellBorder;
import org.apache.poi.xwpf.converter.core.TableHeight;
import org.apache.poi.xwpf.converter.core.TableWidth;
import org.apache.poi.xwpf.converter.core.openxmlformats.IOpenXMLFormatsPartProvider;
import org.apache.poi.xwpf.converter.core.styles.paragraph.ParagraphAlignmentValueProvider;
import org.apache.poi.xwpf.converter.core.styles.paragraph.ParagraphBackgroundColorValueProvider;
import org.apache.poi.xwpf.converter.core.styles.paragraph.ParagraphBorderBottomValueProvider;
import org.apache.poi.xwpf.converter.core.styles.paragraph.ParagraphBorderLeftValueProvider;
import org.apache.poi.xwpf.converter.core.styles.paragraph.ParagraphBorderRightValueProvider;
import org.apache.poi.xwpf.converter.core.styles.paragraph.ParagraphBorderTopValueProvider;
import org.apache.poi.xwpf.converter.core.styles.paragraph.ParagraphIndentationFirstLineValueProvider;
import org.apache.poi.xwpf.converter.core.styles.paragraph.ParagraphIndentationHangingValueProvider;
import org.apache.poi.xwpf.converter.core.styles.paragraph.ParagraphIndentationLeftValueProvider;
import org.apache.poi.xwpf.converter.core.styles.paragraph.ParagraphIndentationRightValueProvider;
import org.apache.poi.xwpf.converter.core.styles.paragraph.ParagraphLineSpacingValueProvider;
import org.apache.poi.xwpf.converter.core.styles.paragraph.ParagraphNumPrValueProvider;
import org.apache.poi.xwpf.converter.core.styles.paragraph.ParagraphSpacingAfterValueProvider;
import org.apache.poi.xwpf.converter.core.styles.paragraph.ParagraphSpacingBeforeValueProvider;
import org.apache.poi.xwpf.converter.core.styles.paragraph.ParagraphTabsValueProvider;
import org.apache.poi.xwpf.converter.core.styles.run.RunBackgroundColorValueProvider;
import org.apache.poi.xwpf.converter.core.styles.run.RunFontColorValueProvider;
import org.apache.poi.xwpf.converter.core.styles.run.RunFontFamilyAsciiValueProvider;
import org.apache.poi.xwpf.converter.core.styles.run.RunFontFamilyEastAsiaValueProvider;
import org.apache.poi.xwpf.converter.core.styles.run.RunFontFamilyHAnsiValueProvider;
import org.apache.poi.xwpf.converter.core.styles.run.RunFontSizeValueProvider;
import org.apache.poi.xwpf.converter.core.styles.run.RunFontStyleBoldValueProvider;
import org.apache.poi.xwpf.converter.core.styles.run.RunFontStyleItalicValueProvider;
import org.apache.poi.xwpf.converter.core.styles.run.RunFontStyleStrikeValueProvider;
import org.apache.poi.xwpf.converter.core.styles.run.RunTextHighlightingValueProvider;
import org.apache.poi.xwpf.converter.core.styles.run.RunUnderlineValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.TableAlignmentValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.TableBorderBottomValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.TableBorderInsideHValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.TableBorderInsideVValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.TableBorderLeftValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.TableBorderRightValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.TableBorderTopValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.TableIndentationValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.TableMarginBottomValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.TableMarginLeftValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.TableMarginRightValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.TableMarginTopValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.TableWidthValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.cell.TableCellBackgroundColorValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.cell.TableCellBorderBottomValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.cell.TableCellBorderInsideHValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.cell.TableCellBorderInsideVValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.cell.TableCellBorderLeftValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.cell.TableCellBorderRightValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.cell.TableCellBorderTopValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.cell.TableCellGridSpanValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.cell.TableCellMarginBottomValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.cell.TableCellMarginLeftValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.cell.TableCellMarginRightValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.cell.TableCellMarginTopValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.cell.TableCellNoWrapValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.cell.TableCellTextDirectionValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.cell.TableCellVMergeValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.cell.TableCellVerticalAlignmentValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.cell.TableCellWidthValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.row.TableRowHeaderValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.row.TableRowHeightValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.row.TableRowMarginBottomValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.row.TableRowMarginLeftValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.row.TableRowMarginRightValueProvider;
import org.apache.poi.xwpf.converter.core.styles.table.row.TableRowMarginTopValueProvider;
import org.apache.poi.xwpf.converter.core.utils.DxaUtil;
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRelation;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFSettings;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFont;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrEx;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.FontsDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc.Enum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.SettingsDocument;

public class XWPFStylesDocument
{

    public static final Object EMPTY_VALUE = new Object();

    private static final float DEFAULT_TAB_STOP_POINT = DxaUtil.dxa2points( 720f );

    private final Map<String, CTStyle> stylesByStyleId;

    private CTStyle defaultParagraphStyle;

    private CTStyle defaultTableStyle;

    private final Map<String, Object> values;

    private CTStyle defaultCharacterStyle;

    private CTStyle defaultNumberingStyle;

    private Map<XWPFTable, TableInfo> tableInfos;

    private Float defaultTabStop;

    private CTSettings ctSettings;

    private List<ThemeDocument> themeDocuments;

    private final Map<String, List<String>> fontsAltName;

    private final Map<String, String> fontsToUse;

    private CTStyles styles;

    public XWPFStylesDocument( XWPFDocument document )
        throws XmlException, IOException
    {
        this( document, true );
    }

    public XWPFStylesDocument( XWPFDocument document, boolean lazyInitialization )
        throws XmlException, IOException
    {
        this( document.getStyle(), getFontsDocument( document ), getThemeDocuments( document ),
              getCTSettings( document ), lazyInitialization );
    }

    public XWPFStylesDocument( IOpenXMLFormatsPartProvider provider )
        throws Exception
    {
        this( provider, true );
    }

    public XWPFStylesDocument( IOpenXMLFormatsPartProvider provider, boolean lazyInitialization )
        throws Exception
    {
        this( provider.getStyle(), provider.getFontsDocument(), provider.getThemeDocuments(), provider.getSettings(),
              lazyInitialization );
    }

    public XWPFStylesDocument( CTStyles styles, List<FontsDocument> fontsDocuments, List<ThemeDocument> themeDocuments,
                               CTSettings ctSettings, boolean lazyInitialization )
        throws XmlException, IOException
    {
        this.styles = styles;
        this.stylesByStyleId = new HashMap<String, CTStyle>();
        this.values = new HashMap<String, Object>();
        this.fontsAltName = updateFonts( fontsDocuments );
        this.fontsToUse = new HashMap<String, String>();
        this.themeDocuments = themeDocuments;
        this.ctSettings = ctSettings;
        if ( lazyInitialization )
        {
            initialize();
        }
    }

    private Map<String, List<String>> updateFonts( List<FontsDocument> fontsDocuments )
    {
        Map<String, List<String>> fontsAltName = new HashMap<String, List<String>>();
        // Compute fonts alt name
        // see spec 17.8.3.1 altName (Alternate Names for Font)
        for ( FontsDocument fontsDocument : fontsDocuments )
        {
            CTString altName = null;
            List<CTFont> fonts = fontsDocument.getFonts().getFontList();
            for ( CTFont font : fonts )
            {
                altName = font.getAltName();
                if ( altName != null && StringUtils.isNotEmpty( altName.getVal() ) )
                {
                    List<String> altNames = new ArrayList<String>();
                    // This element specifies a set of alternative names which can be used to locate the font
                    // specified by the parent
                    // element. This set of alternative names is stored in a comma-delimited list, with all
                    // adjacent commas ignored (i.e.
                    // a value of Name A, Name B is equivalent to Name A,,,,,,,,, Name B).
                    String[] names = altName.getVal().split( "," );
                    String name = null;
                    for ( int i = 0; i < names.length; i++ )
                    {
                        name = names[i];
                        if ( StringUtils.isNotEmpty( name ) )
                        {
                            altNames.add( name );
                        }
                    }
                    fontsAltName.put( font.getName(), altNames );
                }
            }
        }
        return fontsAltName;
    }

    protected void initialize()
        throws XmlException, IOException
    {
        List<CTStyle> s = styles.getStyleList();
        for ( CTStyle style : s )
        {
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff.Enum isDefault = style.getDefault();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType.Enum type = style.getType();

            boolean isDefaultStyle =
                ( isDefault != null && isDefault.intValue() == org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff.INT_X_1 );
            if ( isDefaultStyle )
            {
                // default
                if ( type != null )
                {
                    switch ( type.intValue() )
                    {
                        case org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType.INT_CHARACTER:
                            defaultCharacterStyle = style;
                            break;
                        case org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType.INT_NUMBERING:
                            defaultNumberingStyle = style;
                            break;
                        case org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType.INT_PARAGRAPH:
                            defaultParagraphStyle = style;
                            break;
                        case org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType.INT_TABLE:
                            defaultTableStyle = style;
                            break;
                    }
                }

            }
            visitStyle( style, isDefaultStyle );
            stylesByStyleId.put( style.getStyleId(), style );
        }
    }

    protected void visitStyle( CTStyle style, boolean defaultStyle )
    {

    }

    public CTStyle getDefaultParagraphStyle()
    {
        return defaultParagraphStyle;
    }

    public CTStyle getStyle( String styleId )
    {
        return stylesByStyleId.get( styleId );
    }

    public CTDocDefaults getDocDefaults()
    {
        try
        {
            return styles.getDocDefaults();
        }
        catch ( Exception e )
        {
            return null;
        }
    }

    // -------------------- Paragraph

    /**
     * @param docxParagraph
     * @return
     */
    public Float getSpacingBefore( XWPFParagraph docxParagraph )
    {
        return ParagraphSpacingBeforeValueProvider.INSTANCE.getValue( docxParagraph, this );
    }

    public Float getSpacingBefore( CTPPr pPr )
    {
        return ParagraphSpacingBeforeValueProvider.INSTANCE.getValue( pPr );
    }

    public Float getSpacingBefore( CTP paragraph, CTTbl table )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.paragraph.ParagraphSpacingBeforeValueProvider.INSTANCE.getValue( paragraph,
                                                                                                                                         table,
                                                                                                                                         this );
    }

    public Float getSpacingAfter( XWPFParagraph docxParagraph )
    {
        return ParagraphSpacingAfterValueProvider.INSTANCE.getValue( docxParagraph, this );
    }

    public Float getSpacingAfter( CTPPr pPr )
    {
        return ParagraphSpacingAfterValueProvider.INSTANCE.getValue( pPr );
    }

    public Float getSpacingAfter( CTP paragraph, CTTbl table )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.paragraph.ParagraphSpacingAfterValueProvider.INSTANCE.getValue( paragraph,
                                                                                                                                        table,
                                                                                                                                        this );
    }

    public Float getIndentationLeft( XWPFParagraph paragraph )
    {
        return ParagraphIndentationLeftValueProvider.INSTANCE.getValue( paragraph, this );
    }

    public Float getIndentationLeft( CTPPr pPr )
    {
        return ParagraphIndentationLeftValueProvider.INSTANCE.getValue( pPr );
    }

    public Float getIndentationLeft( CTP paragraph, CTTbl table )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.paragraph.ParagraphIndentationLeftValueProvider.INSTANCE.getValue( paragraph,
                                                                                                                                           table,
                                                                                                                                           this );
    }

    public Float getIndentationRight( XWPFParagraph paragraph )
    {
        return ParagraphIndentationRightValueProvider.INSTANCE.getValue( paragraph, this );
    }

    public Float getIndentationRight( CTPPr pPr )
    {
        return ParagraphIndentationRightValueProvider.INSTANCE.getValue( pPr );
    }

    public Float getIndentationRight( CTP paragraph, CTTbl table )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.paragraph.ParagraphIndentationRightValueProvider.INSTANCE.getValue( paragraph,
                                                                                                                                            table,
                                                                                                                                            this );
    }

    public Float getIndentationFirstLine( XWPFParagraph paragraph )
    {
        return ParagraphIndentationFirstLineValueProvider.INSTANCE.getValue( paragraph, this );
    }

    public Float getIndentationFirstLine( CTPPr pPr )
    {
        return ParagraphIndentationFirstLineValueProvider.INSTANCE.getValue( pPr );
    }

    public Float getIndentationFirstLine( CTP paragraph, CTTbl table )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.paragraph.ParagraphIndentationFirstLineValueProvider.INSTANCE.getValue( paragraph,
                                                                                                                                                table,
                                                                                                                                                this );
    }

    public Float getIndentationHanging( XWPFParagraph paragraph )
    {
        return ParagraphIndentationHangingValueProvider.INSTANCE.getValue( paragraph, this );
    }

    public Float getIndentationHanging( CTPPr pPr )
    {
        return ParagraphIndentationHangingValueProvider.INSTANCE.getValue( pPr );
    }

    public Float getIndentationHanging( CTP paragraph, CTTbl table )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.paragraph.ParagraphIndentationHangingValueProvider.INSTANCE.getValue( paragraph,
                                                                                                                                              table,
                                                                                                                                              this );
    }

    public Color getBackgroundColor( XWPFParagraph paragraph )
    {
        return ParagraphBackgroundColorValueProvider.INSTANCE.getValue( paragraph, this );
    }

    public Color getBackgroundColor( CTPPr pPr )
    {
        return ParagraphBackgroundColorValueProvider.INSTANCE.getValue( pPr );
    }

    /**
     * @param paragraph
     * @return
     */
    public ParagraphAlignment getParagraphAlignment( XWPFParagraph paragraph )
    {
        return ParagraphAlignmentValueProvider.INSTANCE.getValue( paragraph, this );
    }

    public ParagraphAlignment getParagraphAlignment( CTPPr pPr )
    {
        return ParagraphAlignmentValueProvider.INSTANCE.getValue( pPr );
    }

    public ParagraphAlignment getParagraphAlignment( CTP paragraph, CTTbl table )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.paragraph.ParagraphAlignmentValueProvider.INSTANCE.getValue( paragraph,
                                                                                                                                     table,
                                                                                                                                     this );
    }

    public CTBorder getBorderTop( XWPFParagraph docxParagraph )
    {
        return ParagraphBorderTopValueProvider.INSTANCE.getValue( docxParagraph, this );
    }

    public CTBorder getBorderTop( CTPPr pPr )
    {
        return ParagraphBorderTopValueProvider.INSTANCE.getValue( pPr );
    }

    public CTBorder getBorderBottom( XWPFParagraph docxParagraph )
    {
        return ParagraphBorderBottomValueProvider.INSTANCE.getValue( docxParagraph, this );
    }

    public CTBorder getBorderBottom( CTPPr pPr )
    {
        return ParagraphBorderBottomValueProvider.INSTANCE.getValue( pPr );
    }

    public CTBorder getBorderLeft( XWPFParagraph docxParagraph )
    {
        return ParagraphBorderLeftValueProvider.INSTANCE.getValue( docxParagraph, this );
    }

    public CTBorder getBorderLeft( CTPPr pPr )
    {
        return ParagraphBorderLeftValueProvider.INSTANCE.getValue( pPr );
    }

    public CTBorder getBorderRight( XWPFParagraph docxParagraph )
    {
        return ParagraphBorderRightValueProvider.INSTANCE.getValue( docxParagraph, this );
    }

    public CTBorder getBorderRight( CTPPr pPr )
    {
        return ParagraphBorderRightValueProvider.INSTANCE.getValue( pPr );
    }

    public CTTabs getParagraphTabs( XWPFParagraph docxParagraph )
    {
        return ParagraphTabsValueProvider.INSTANCE.getValue( docxParagraph, this );
    }

    public CTTabs getParagraphTabs( CTPPr pPr )
    {
        return ParagraphTabsValueProvider.INSTANCE.getValue( pPr );
    }

    public ParagraphLineSpacing getParagraphSpacing( XWPFParagraph docxParagraph )
    {
        return ParagraphLineSpacingValueProvider.INSTANCE.getValue( docxParagraph, this );
    }

    public ParagraphLineSpacing getParagraphSpacing( CTPPr pPr )
    {
        return ParagraphLineSpacingValueProvider.INSTANCE.getValue( pPr );
    }

    public ParagraphLineSpacing getParagraphSpacing( CTP paragraph, CTTbl table )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.paragraph.ParagraphLineSpacingValueProvider.INSTANCE.getValue( paragraph,
                                                                                                                                       table,
                                                                                                                                       this );
    }

    public CTNumPr getParagraphNumPr( XWPFParagraph docxParagraph )
    {
        return ParagraphNumPrValueProvider.INSTANCE.getValue( docxParagraph, this );
    }

    public CTNumPr getParagraphNumPr( CTPPr pPr )
    {
        return ParagraphNumPrValueProvider.INSTANCE.getValue( pPr );
    }

    // -------------------- Run

    /**
     * @param run
     * @return
     */
    public String getFontFamilyAscii( XWPFRun run )
    {
        return RunFontFamilyAsciiValueProvider.INSTANCE.getValue( run, this );
    }

    public String getFontFamilyAscii( CTRPr rPr )
    {
        return RunFontFamilyAsciiValueProvider.INSTANCE.getValue( rPr, this );
    }

    public String getFontFamilyAscii( CTR run, CTP paragraph )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.run.RunFontFamilyAsciiValueProvider.INSTANCE.getValue( run,
                                                                                                                               paragraph,
                                                                                                                               this );
    }

    public String getFontFamilyEastAsia( XWPFRun run )
    {
        return RunFontFamilyEastAsiaValueProvider.INSTANCE.getValue( run, this );
    }

    public String getFontFamilyEastAsia( CTRPr rPr )
    {
        return RunFontFamilyEastAsiaValueProvider.INSTANCE.getValue( rPr, this );
    }

    public String getFontFamilyEastAsia( CTR run, CTP paragraph )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.run.RunFontFamilyEastAsiaValueProvider.INSTANCE.getValue( run,
                                                                                                                                  paragraph,
                                                                                                                                  this );
    }

    public String getFontFamilyHAnsi( XWPFRun run )
    {
        return RunFontFamilyHAnsiValueProvider.INSTANCE.getValue( run, this );
    }

    public String getFontFamilyHAnsi( CTRPr rPr )
    {
        return RunFontFamilyHAnsiValueProvider.INSTANCE.getValue( rPr, this );
    }

    public String getFontFamilyHAnsi( CTR run, CTP paragraph )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.run.RunFontFamilyHAnsiValueProvider.INSTANCE.getValue( run,
                                                                                                                               paragraph,
                                                                                                                               this );
    }

    public Float getFontSize( XWPFRun run )
    {
        return RunFontSizeValueProvider.INSTANCE.getValue( run, this );
    }

    public Float getFontSize( CTR run, CTP paragraph )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.run.RunFontSizeValueProvider.INSTANCE.getValue( run,
                                                                                                                        paragraph,
                                                                                                                        this );
    }

    public Float getFontSize( CTRPr rPr )
    {
        return RunFontSizeValueProvider.INSTANCE.getValue( rPr, this );
    }

    public Boolean getFontStyleBold( XWPFRun run )
    {
        return RunFontStyleBoldValueProvider.INSTANCE.getValue( run, this );
    }

    public Boolean getFontStyleBold( CTRPr rPr )
    {
        return RunFontStyleBoldValueProvider.INSTANCE.getValue( rPr, this );
    }

    public Boolean getFontStyleBold( CTR run, CTP paragraph )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.run.RunFontStyleBoldValueProvider.INSTANCE.getValue( run,
                                                                                                                             paragraph,
                                                                                                                             this );
    }

    public Boolean getFontStyleItalic( XWPFRun run )
    {
        return RunFontStyleItalicValueProvider.INSTANCE.getValue( run, this );
    }

    public Boolean getFontStyleItalic( CTRPr rPr )
    {
        return RunFontStyleItalicValueProvider.INSTANCE.getValue( rPr, this );
    }

    public Boolean getFontStyleItalic( CTR run, CTP paragraph )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.run.RunFontStyleItalicValueProvider.INSTANCE.getValue( run,
                                                                                                                               paragraph,
                                                                                                                               this );
    }

    public Boolean getFontStyleStrike( XWPFRun run )
    {
        return RunFontStyleStrikeValueProvider.INSTANCE.getValue( run, this );
    }

    public Boolean getFontStyleStrike( CTRPr rPr )
    {
        return RunFontStyleStrikeValueProvider.INSTANCE.getValue( rPr, this );
    }

    public Boolean getFontStyleStrike( CTR run, CTP paragraph )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.run.RunFontStyleStrikeValueProvider.INSTANCE.getValue( run,
                                                                                                                               paragraph,
                                                                                                                               this );
    }

    public Color getFontColor( XWPFRun run )
    {
        return RunFontColorValueProvider.INSTANCE.getValue( run, this );
    }

    public Color getFontColor( CTRPr rPr )
    {
        return RunFontColorValueProvider.INSTANCE.getValue( rPr, this );
    }

    public Color getFontColor( CTR run, CTP paragraph )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.run.RunFontColorValueProvider.INSTANCE.getValue( run,
                                                                                                                         paragraph,
                                                                                                                         this );
    }

    public UnderlinePatterns getUnderline( CTRPr rPr )
    {
        return RunUnderlineValueProvider.INSTANCE.getValue( rPr, this );
    }

    public UnderlinePatterns getUnderline( XWPFRun run )
    {
        return RunUnderlineValueProvider.INSTANCE.getValue( run, this );
    }

    public UnderlinePatterns getUnderline( CTR run, CTP paragraph )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.run.RunUnderlineValueProvider.INSTANCE.getValue( run,
                                                                                                                         paragraph,
                                                                                                                         this );
    }

    public Color getBackgroundColor( XWPFRun run )
    {
        return RunBackgroundColorValueProvider.INSTANCE.getValue( run, this );
    }

    public Color getBackgroundColor( CTRPr rPr )
    {
        return RunBackgroundColorValueProvider.INSTANCE.getValue( rPr, this );
    }

    public Color getBackgroundColor( CTR run, CTP paragraph )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.run.RunBackgroundColorValueProvider.INSTANCE.getValue( run,
                                                                                                                               paragraph,
                                                                                                                               this );
    }

    public Color getTextHighlighting( XWPFRun run )
    {
        return RunTextHighlightingValueProvider.INSTANCE.getValue( run, this );
    }

    public Color getTextHighlighting( CTRPr rPr )
    {
        return RunTextHighlightingValueProvider.INSTANCE.getValue( rPr, this );
    }

    public Color getTextHighlighting( CTR run, CTP paragraph )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.run.RunTextHighlightingValueProvider.INSTANCE.getValue( run,
                                                                                                                                paragraph,
                                                                                                                                this );
    }

    // ------------------------ Table

    /**
     * @param table
     * @return
     */
    public TableWidth getTableWidth( XWPFTable table )
    {
        return TableWidthValueProvider.INSTANCE.getValue( table, this );
    }

    public TableWidth getTableWidth( CTTblPr tblPr )
    {
        return TableWidthValueProvider.INSTANCE.getValue( tblPr );
    }

    public TableWidth getTableWidth( CTTblPrBase tblPr )
    {
        return TableWidthValueProvider.INSTANCE.getValue( tblPr );
    }

    public TableWidth getTableWidth( CTTbl table )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.table.TableWidthValueProvider.INSTANCE.getValue( table,
                                                                                                                         this );
    }

    public ParagraphAlignment getTableAlignment( XWPFTable table )
    {
        return TableAlignmentValueProvider.INSTANCE.getValue( table, this );
    }

    public ParagraphAlignment getTableAlignment( CTTblPr tblPr )
    {
        return TableAlignmentValueProvider.INSTANCE.getValue( tblPr );
    }

    public ParagraphAlignment getTableAlignment( CTTblPrBase tblPr )
    {
        return TableAlignmentValueProvider.INSTANCE.getValue( tblPr );
    }

    public ParagraphAlignment getTableAlignment( CTTbl table )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.table.TableAlignmentValueProvider.INSTANCE.getValue( table,
                                                                                                                             this );
    }

    public Float getTableIndentation( XWPFTable table )
    {
        return TableIndentationValueProvider.INSTANCE.getValue( table, this );
    }

    public Float getTableIndentation( CTTblPr tblPr )
    {
        return TableIndentationValueProvider.INSTANCE.getValue( tblPr );
    }

    public Float getTableIndentation( CTTblPrBase tblPr )
    {
        return TableIndentationValueProvider.INSTANCE.getValue( tblPr );
    }

    public Float getTableIndentation( CTTbl table )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.table.TableIndentationValueProvider.INSTANCE.getValue( table,
                                                                                                                               this );
    }

    public TableCellBorder getTableBorderTop( XWPFTable table )
    {
        return TableBorderTopValueProvider.INSTANCE.getValue( table, this );
    }

    public TableCellBorder getTableBorderTop( CTTblPr tblPr )
    {
        return TableBorderTopValueProvider.INSTANCE.getValue( tblPr );
    }

    public TableCellBorder getTableBorderTop( CTTblPrBase tblPr )
    {
        return TableBorderTopValueProvider.INSTANCE.getValue( tblPr );
    }

    public TableCellBorder getTableBorderBottom( XWPFTable table )
    {
        return TableBorderBottomValueProvider.INSTANCE.getValue( table, this );
    }

    public TableCellBorder getTableBorderBottom( CTTblPr tblPr )
    {
        return TableBorderBottomValueProvider.INSTANCE.getValue( tblPr );
    }

    public TableCellBorder getTableBorderBottom( CTTblPrBase tblPr )
    {
        return TableBorderBottomValueProvider.INSTANCE.getValue( tblPr );
    }

    public TableCellBorder getTableBorderLeft( XWPFTable table )
    {
        return TableBorderLeftValueProvider.INSTANCE.getValue( table, this );
    }

    public TableCellBorder getTableBorderLeft( CTTblPr tblPr )
    {
        return TableBorderLeftValueProvider.INSTANCE.getValue( tblPr );
    }

    public TableCellBorder getTableBorderLeft( CTTblPrBase tblPr )
    {
        return TableBorderLeftValueProvider.INSTANCE.getValue( tblPr );
    }

    public TableCellBorder getTableBorderRight( XWPFTable table )
    {
        return TableBorderRightValueProvider.INSTANCE.getValue( table, this );
    }

    public TableCellBorder getTableBorderRight( CTTblPr tblPr )
    {
        return TableBorderRightValueProvider.INSTANCE.getValue( tblPr );
    }

    public TableCellBorder getTableBorderRight( CTTblPrBase tblPr )
    {
        return TableBorderRightValueProvider.INSTANCE.getValue( tblPr );
    }

    public TableCellBorder getTableBorderInsideH( XWPFTable table )
    {
        return TableBorderInsideHValueProvider.INSTANCE.getValue( table, this );
    }

    public TableCellBorder getTableBorderInsideH( CTTblPr tblPr )
    {
        return TableBorderInsideHValueProvider.INSTANCE.getValue( tblPr );
    }

    public TableCellBorder getTableBorderInsideH( CTTblPrBase tblPr )
    {
        return TableBorderInsideHValueProvider.INSTANCE.getValue( tblPr );
    }

    public TableCellBorder getTableBorderInsideV( XWPFTable table )
    {
        return TableBorderInsideVValueProvider.INSTANCE.getValue( table, this );
    }

    public TableCellBorder getTableBorderInsideV( CTTblPr tblPr )
    {
        return TableBorderInsideVValueProvider.INSTANCE.getValue( tblPr );
    }

    public TableCellBorder getTableBorderInsideV( CTTblPrBase tblPr )
    {
        return TableBorderInsideVValueProvider.INSTANCE.getValue( tblPr );
    }

    public Float getTableMarginTop( XWPFTable table )
    {
        return TableMarginTopValueProvider.INSTANCE.getValue( table, this );
    }

    public Float getTableMarginTop( CTTblPrBase tcPr )
    {
        return TableMarginTopValueProvider.INSTANCE.getValue( tcPr );
    }

    public Float getTableMarginBottom( XWPFTable table )
    {
        return TableMarginBottomValueProvider.INSTANCE.getValue( table, this );
    }

    public Float getTableMarginBottom( CTTblPrBase tcPr )
    {
        return TableMarginBottomValueProvider.INSTANCE.getValue( tcPr );
    }

    public Float getTableMarginLeft( XWPFTable table )
    {
        return TableMarginLeftValueProvider.INSTANCE.getValue( table, this );
    }

    public Float getTableMarginLeft( CTTblPrBase tcPr )
    {
        return TableMarginLeftValueProvider.INSTANCE.getValue( tcPr );
    }

    public Float getTableMarginRight( XWPFTable table )
    {
        return TableMarginRightValueProvider.INSTANCE.getValue( table, this );
    }

    public Float getTableMarginRight( CTTblPrBase tcPr )
    {
        return TableMarginRightValueProvider.INSTANCE.getValue( tcPr );
    }

    // ------------------------ Table row

    /**
     * @param row
     * @return
     */
    public TableHeight getTableRowHeight( XWPFTableRow row )
    {
        return TableRowHeightValueProvider.INSTANCE.getValue( row, this );
    }

    /**
     * @param cell
     * @return
     */
    public TableHeight getTableRowHeight( CTTrPr trPr )
    {
        return TableRowHeightValueProvider.INSTANCE.getValue( trPr );
    }

    public Float getTableRowMarginTop( XWPFTableRow row )
    {
        return TableRowMarginTopValueProvider.INSTANCE.getValue( row, this );
    }

    public Float getTableRowMarginTop( CTTblPrEx tblPrEx )
    {
        return TableRowMarginTopValueProvider.INSTANCE.getValue( tblPrEx );
    }

    public Float getTableRowMarginBottom( XWPFTableRow row )
    {
        return TableRowMarginBottomValueProvider.INSTANCE.getValue( row, this );
    }

    public Float getTableRowMarginBottom( CTTblPrEx tblPrEx )
    {
        return TableRowMarginBottomValueProvider.INSTANCE.getValue( tblPrEx );
    }

    public Float getTableRowMarginLeft( XWPFTableRow row )
    {
        return TableRowMarginLeftValueProvider.INSTANCE.getValue( row, this );
    }

    public Float getTableRowMarginLeft( CTTblPrEx tblPrEx )
    {
        return TableRowMarginLeftValueProvider.INSTANCE.getValue( tblPrEx );
    }

    public Float getTableRowMarginRight( XWPFTableRow row )
    {
        return TableRowMarginRightValueProvider.INSTANCE.getValue( row, this );
    }

    public Float getTableRowMarginRight( CTTblPrEx tblPrEx )
    {
        return TableRowMarginRightValueProvider.INSTANCE.getValue( tblPrEx );
    }

    public boolean isTableRowHeader( XWPFTableRow row )
    {
        return TableRowHeaderValueProvider.INSTANCE.getValue( row, this );
    }

    public boolean isTableRowHeader( CTRow row )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.table.row.TableRowHeaderValueProvider.INSTANCE.getValue( row,
                                                                                                                                 this );
    }

    // ------------------------ Table cell

    /**
     * @param cell
     * @return
     */
    public Enum getTableCellVerticalAlignment( XWPFTableCell cell )
    {
        return TableCellVerticalAlignmentValueProvider.INSTANCE.getValue( cell, this );
    }

    public Color getTableCellBackgroundColor( XWPFTableCell cell )
    {
        return TableCellBackgroundColorValueProvider.INSTANCE.getValue( cell, this );
    }

    public Color getTableCellBackgroundColor( CTTcPr tcPr )
    {
        return TableCellBackgroundColorValueProvider.INSTANCE.getValue( tcPr );
    }

    public Color getTableCellBackgroundColor( CTTc cell )
    {
        return org.apache.poi.xwpf.converter.core.openxmlformats.styles.table.cell.TableCellBackgroundColorValueProvider.INSTANCE.getValue( cell,
                                                                                                                                            this );
    }

    public BigInteger getTableCellGridSpan( XWPFTableCell cell )
    {
        return TableCellGridSpanValueProvider.INSTANCE.getValue( cell, this );
    }

    public BigInteger getTableCellGridSpan( CTTcPr tcPr )
    {
        return TableCellGridSpanValueProvider.INSTANCE.getValue( tcPr );
    }

    public TableWidth getTableCellWith( XWPFTableCell cell )
    {
        return TableCellWidthValueProvider.INSTANCE.getValue( cell, this );
    }

    public TableWidth getTableCellWith( CTTcPr tcPr )
    {
        return TableCellWidthValueProvider.INSTANCE.getValue( tcPr );
    }

    public CTTextDirection getTextDirection( XWPFTableCell cell )
    {
        return TableCellTextDirectionValueProvider.INSTANCE.getValue( cell, this );
    }

    public CTTextDirection getTextDirection( CTTcPr tcPr )
    {
        return TableCellTextDirectionValueProvider.INSTANCE.getValue( tcPr );
    }

    public STMerge.Enum getTableCellVMerge( XWPFTableCell cell )
    {
        return TableCellVMergeValueProvider.INSTANCE.getValue( cell, this );
    }

    public STMerge.Enum getTableCellVMerge( CTTcPr tcPr )
    {
        return TableCellVMergeValueProvider.INSTANCE.getValue( tcPr );
    }

    /**
     * Returns the table cell borders with conflicts.
     * 
     * @param cell
     * @param borderSide
     * @return
     * @see http://officeopenxml.com/WPtableCellBorderConflicts.php
     */
    public TableCellBorder getTableCellBorderWithConflicts( XWPFTableCell cell, BorderSide borderSide )
    {
        /**
         * Conflicts between cell borders and table and table-level exception borders If the cell spacing is zero, then
         * there is a conflict. The following rules apply as between cell borders and table and table-level exception
         * (row) borders (Reference: ECMA-376, 3rd Edition (June, 2011), Fundamentals and Markup Language Reference
         * 17.4.40.):
         */

        /**
         * 1) If there is a cell border, then the cell border is displayed.
         */

        /**
         * 2) If there is no cell border but there is a table-level exception border on the row, then that table-level
         * exception border is displayed.
         */

        TableCellBorder border = getTableCellBorder( cell, borderSide );
        if ( border == null )
        {
            XWPFTable table = cell.getTableRow().getTable();
            boolean borderInside = isBorderInside( cell, borderSide );
            if ( borderInside )
            {
                border = getTableCellBorderInside( cell, borderSide );
                if ( border == null )
                {
                    border = getTableBorderInside( table, borderSide );
                }
            }
            if ( border == null && !borderInside )
            {
                /**
                 * 3) If there is no cell or table-level exception border, then the table border is displayed.
                 */
                border = getTableBorder( table, borderSide );
            }
        }
        return border;
    }

    public boolean isBorderInside( XWPFTableCell cell, BorderSide borderSide )
    {
        TableCellInfo cellInfo = getTableCellInfo( cell );
        boolean borderInside = cellInfo.isInside( borderSide );
        return borderInside;
    }

    public TableCellBorder getTableBorder( XWPFTable table, BorderSide borderSide )
    {
        switch ( borderSide )
        {
            case TOP:
                return getTableBorderTop( table );
            case BOTTOM:
                return getTableBorderBottom( table );
            case LEFT:
                return getTableBorderLeft( table );
            case RIGHT:
                return getTableBorderRight( table );
        }
        return null;
    }

    public TableCellBorder getTableBorderInside( XWPFTable table, BorderSide borderSide )
    {
        switch ( borderSide )
        {
            case TOP:
            case BOTTOM:
                return getTableBorderInsideH( table );
            default:
                return getTableBorderInsideV( table );
        }
    }

    public TableCellBorder getTableCellBorderInside( XWPFTableCell cell, BorderSide borderSide )
    {
        switch ( borderSide )
        {
            case TOP:
            case BOTTOM:
                return getTableCellBorderInsideH( cell );
            default:
                return getTableCellBorderInsideV( cell );
        }
    }

    public TableCellBorder getTableCellBorder( XWPFTableCell cell, BorderSide borderSide )
    {
        switch ( borderSide )
        {
            case TOP:
                return getTableCellBorderTop( cell );
            case BOTTOM:
                return getTableCellBorderBottom( cell );
            case LEFT:
                return getTableCellBorderLeft( cell );
            case RIGHT:
                return getTableCellBorderRight( cell );
        }
        return null;
    }

    public TableCellBorder getTableCellBorderTop( XWPFTableCell cell )
    {
        return TableCellBorderTopValueProvider.INSTANCE.getValue( cell, this );
    }

    public TableCellBorder getTableCellBorderTop( CTTcPr tcPr )
    {
        return TableCellBorderTopValueProvider.INSTANCE.getValue( tcPr );
    }

    public TableCellBorder getTableCellBorderBottom( XWPFTableCell cell )
    {
        return TableCellBorderBottomValueProvider.INSTANCE.getValue( cell, this );
    }

    public TableCellBorder getTableCellBorderBottom( CTTcPr tcPr )
    {
        return TableCellBorderBottomValueProvider.INSTANCE.getValue( tcPr );
    }

    public TableCellBorder getTableCellBorderLeft( XWPFTableCell cell )
    {
        return TableCellBorderLeftValueProvider.INSTANCE.getValue( cell, this );
    }

    public TableCellBorder getTableCellBorderLeft( CTTcPr tcPr )
    {
        return TableCellBorderLeftValueProvider.INSTANCE.getValue( tcPr );
    }

    public TableCellBorder getTableCellBorderRight( XWPFTableCell cell )
    {
        return TableCellBorderRightValueProvider.INSTANCE.getValue( cell, this );
    }

    public TableCellBorder getTableCellBorderRight( CTTcPr tcPr )
    {
        return TableCellBorderRightValueProvider.INSTANCE.getValue( tcPr );
    }

    public TableCellBorder getTableCellBorderInsideH( XWPFTableCell cell )
    {
        return TableCellBorderInsideHValueProvider.INSTANCE.getValue( cell, this );
    }

    public TableCellBorder getTableCellBorderInsideH( CTTcPr tcPr )
    {
        return TableCellBorderInsideHValueProvider.INSTANCE.getValue( tcPr );
    }

    public TableCellBorder getTableCellBorderInsideV( XWPFTableCell cell )
    {
        return TableCellBorderInsideVValueProvider.INSTANCE.getValue( cell, this );
    }

    public TableCellBorder getTableCellBorderInsideV( CTTcPr tcPr )
    {
        return TableCellBorderInsideVValueProvider.INSTANCE.getValue( tcPr );
    }

    public Float getTableCellMarginTop( XWPFTableCell cell )
    {
        return TableCellMarginTopValueProvider.INSTANCE.getValue( cell, this );
    }

    public Float getTableCellMarginTop( CTTcPr tcPr )
    {
        return TableCellMarginTopValueProvider.INSTANCE.getValue( tcPr );
    }

    public Float getTableCellMarginBottom( XWPFTableCell cell )
    {
        return TableCellMarginBottomValueProvider.INSTANCE.getValue( cell, this );
    }

    public Float getTableCellMarginBottom( CTTcPr tcPr )
    {
        return TableCellMarginBottomValueProvider.INSTANCE.getValue( tcPr );
    }

    public Float getTableCellMarginLeft( XWPFTableCell cell )
    {
        return TableCellMarginLeftValueProvider.INSTANCE.getValue( cell, this );
    }

    public Float getTableCellMarginLeft( CTTcPr tcPr )
    {
        return TableCellMarginLeftValueProvider.INSTANCE.getValue( tcPr );
    }

    public Float getTableCellMarginRight( XWPFTableCell cell )
    {
        return TableCellMarginRightValueProvider.INSTANCE.getValue( cell, this );
    }

    public Float getTableCellMarginRight( CTTcPr tcPr )
    {
        return TableCellMarginRightValueProvider.INSTANCE.getValue( tcPr );
    }

    public Boolean getTableCellNoWrap( XWPFTableCell cell )
    {
        return TableCellNoWrapValueProvider.INSTANCE.getValue( cell, this );
    }

    public Boolean getTableCellNoWrap( CTTcPr tcPr )
    {
        return TableCellNoWrapValueProvider.INSTANCE.getValue( tcPr );
    }

    public CTStyle getDefaultCharacterStyle()
    {
        return defaultCharacterStyle;
    }

    public CTStyle getDefaultNumberingStyle()
    {
        return defaultNumberingStyle;
    }

    public CTStyle getDefaultTableStyle()
    {
        return defaultTableStyle;
    }

    public CTStyle getStyle( CTString basedOn )
    {
        if ( basedOn == null )
        {
            return null;
        }
        return getStyle( basedOn.getVal() );
    }

    @SuppressWarnings("unchecked")
	public <T> T getValue( String key )
    {
        return (T) values.get( key );
    }

    public <T> void setValue( String key, T value )
    {
        values.put( key, value );
    }

    public TableCellInfo getTableCellInfo( XWPFTableCell cell )
    {
        XWPFTable table = cell.getTableRow().getTable();
        return getTableInfo( table ).getCellInfo( cell );
    }

    public TableInfo getTableInfo( XWPFTable table )
    {
        if ( tableInfos == null )
        {
            tableInfos = new HashMap<XWPFTable, TableInfo>();
        }
        TableInfo tableInfo = tableInfos.get( table );
        if ( tableInfo == null )
        {
            tableInfo = new TableInfo( table, this );
            tableInfos.put( table, tableInfo );
        }
        return tableInfo;
    }

    public CTTblStylePr getTableStyle( String tableStyleID,
                                       org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType.Enum type )
    {
        CTStyle style = getStyle( tableStyleID );
        if ( style == null )
        {
            return null;
        }
        // TODO cache it
        List<CTTblStylePr> tblStylePrs = style.getTblStylePrList();
        for ( CTTblStylePr tblStylePr : tblStylePrs )
        {
            if ( type.equals( tblStylePr.getType() ) )
            {
                return tblStylePr;
            }
        }
        return null;
    }

    /**
     * 17.15.1.25 defaultTabStop (Distance Between Automatic Tab Stops) This element specifies the value which shall be
     * used as the multiplier to generate automatic tab stops in this document. Automatic tab stops refer to the tab
     * stop locations which occur after all custom tab stops in the current paragraph have been surpassed. If this
     * element is omitted, then automatic tab stops should be generated at 720 twentieths of a point (0.5") intervals
     * across the displayed page. [Example: Consider a WordprocessingML document
     * 
     * @return
     */
    public float getDefaultTabStop()
    {
        if ( defaultTabStop == null )
        {
            CTSettings settings = getCTSettings();
            if ( settings != null )
            {
                CTTwipsMeasure value = settings.getDefaultTabStop();
                if ( value != null )
                {
                    if ( !value.isNil() )
                    {
                        this.defaultTabStop = DxaUtil.dxa2points( value.getVal() );
                    }

                }
            }
            if ( defaultTabStop == null )
            {
                this.defaultTabStop = DEFAULT_TAB_STOP_POINT;
            }
        }
        return defaultTabStop;
    }

    public CTSettings getCTSettings()
    {
        return ctSettings;
    }

    private static CTSettings getCTSettings( XWPFDocument document )
    {

        XWPFSettings settings = getSettings( document );
        if ( settings != null )
        {
            try
            {
                InputStream inputStream = settings.getPackagePart().getInputStream();
                return SettingsDocument.Factory.parse( inputStream ).getSettings();
            }
            catch ( Exception e )
            {

            }
        }
        return null;
    }

    private static XWPFSettings getSettings( XWPFDocument document )
    {
        for ( POIXMLDocumentPart p : document.getRelations() )
        {
            String relationshipType = p.getPackagePart().getPackage().getRelationships().getRelationship(0).getRelationshipType();
            if ( relationshipType.equals( XWPFRelation.SETTINGS.getRelation() ) )
            {
                return (XWPFSettings) p;
            }
        }
        return null;
    }

    public List<ThemeDocument> getThemeDocuments()
    {
        return themeDocuments;
    }

    private static List<ThemeDocument> getThemeDocuments( XWPFDocument document )
    {
        List<ThemeDocument> themeDocuments = new ArrayList<ThemeDocument>();

        for ( POIXMLDocumentPart p : document.getRelations() )
        {
            String relationshipType = p.getPackagePart().getPackage().getRelationships().getRelationship(0).getRelationshipType();
            if ( "http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme".equals( relationshipType ) )
            {
                try
                {
                    InputStream inputStream = p.getPackagePart().getInputStream();
                    ThemeDocument theme = ThemeDocument.Factory.parse( inputStream );
                    themeDocuments.add( theme );
                }
                catch ( Throwable e )
                {
                    e.printStackTrace();
                }
            }
        }

        return themeDocuments;
    }

    private static List<FontsDocument> getFontsDocument( XWPFDocument document )
    {

        List<FontsDocument> fontsDocuments = new ArrayList<FontsDocument>();

        for ( POIXMLDocumentPart p : document.getRelations() )
        {
            String relationshipType = p.getPackagePart().getPackage().getRelationships().getRelationship(0).getRelationshipType();
            // "http://schemas.openxmlformats.org/officeDocument/2006/relationships/fontTable"
            if ( XWPFRelation.FONT_TABLE.getRelation().equals( relationshipType ) )
            {
                try
                {
                    InputStream inputStream = p.getPackagePart().getInputStream();
                    FontsDocument fontsDocument = FontsDocument.Factory.parse( inputStream );
                    fontsDocuments.add( fontsDocument );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }

            }
        }
        return fontsDocuments;
    }

    public List<String> getFontsAltName( String fontName )
        throws Exception
    {
        return fontsAltName.get( fontName );
    }

    public String getFontNameToUse( String fontName )
    {
        return fontsToUse.get( fontName );
    }

    public void setFontNameToUse( String fontName, String altFfontName )
    {
        fontsToUse.put( fontName, altFfontName );
    }

}
