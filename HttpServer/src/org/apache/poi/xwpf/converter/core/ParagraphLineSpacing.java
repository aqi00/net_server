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
package org.apache.poi.xwpf.converter.core;

//         w:spacing w:after="200" w:line="276" w:lineRule="auto" /> 
public class ParagraphLineSpacing
{

    private final Float spaceBefore;

    private final Float spaceAfter;

    private final Float leading;

    private final Float multipleLeading;

    public ParagraphLineSpacing( Float spaceBefore, Float spaceAfter, Float leading, Float multipleLeading )
    {
        this.spaceBefore = spaceBefore;
        this.spaceAfter = spaceAfter;
        this.leading = leading;
        this.multipleLeading = multipleLeading;
    }

    public Float getSpaceBefore()
    {
        return spaceBefore;
    }

    public Float getSpaceAfter()
    {
        return spaceAfter;
    }

    public Float getLeading()
    {
        return leading;
    }

    public Float getMultipleLeading()
    {
        return multipleLeading;
    }

}
