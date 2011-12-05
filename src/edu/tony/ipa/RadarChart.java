package edu.tony.ipa;

/**
*
* The MIT License
*
* Copyright (c) 2008 the original author or authors.
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:

* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.

* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/

import static com.googlecode.charts4j.Color.WHITE;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import com.googlecode.charts4j.AxisLabels;
import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.AxisStyle;
import com.googlecode.charts4j.AxisTextAlignment;
import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.Data;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.LineStyle;
import com.googlecode.charts4j.Plots;
import com.googlecode.charts4j.RadarPlot;
import com.googlecode.charts4j.RadialAxisLabels;
import com.googlecode.charts4j.Shape;


/**
*
* @author Julien Chastang (julien.c.chastang at gmail dot com)
*/
public class RadarChart {

   @BeforeClass
   public static void setUpBeforeClass() throws Exception {
       Logger.global.setLevel(Level.ALL);
   }

   /**
    * Testing radar charts
    */
   
   public String getIpaRadr(Integer a1, Integer a2, Integer a3, Integer a4, Integer a5, Integer a6) {
	   // EXAMPLE CODE START
       RadarPlot plot = Plots.newRadarPlot(Data.newData(a1, a2, a3, a4, a5, a6, a1));
       Color plotColor = Color.newColor("990000");
       plot.addShapeMarkers(Shape.SQUARE, plotColor, 12);
       plot.addShapeMarkers(Shape.SQUARE, WHITE, 8);
       plot.setColor(plotColor);
       //plot.setFillAreaColor(Color.newColor("8FA3D6"));
       plot.setLineStyle(LineStyle.newLineStyle(4, 1, 0));
       com.googlecode.charts4j.RadarChart chart = GCharts.newRadarChart(plot);
       chart.setSize(300, 300);
       RadialAxisLabels radialAxisLabels = AxisLabelsFactory.newRadialAxisLabels("食", "衣", "住", "行", "育", "樂");
       radialAxisLabels.setRadialAxisStyle(Color.BLACK, 25);
       chart.addRadialAxisLabels(radialAxisLabels);
       AxisLabels contrentricAxisLabels = AxisLabelsFactory.newNumericAxisLabels(Arrays.asList(0, 33, 66, 100));
       contrentricAxisLabels.setAxisStyle(AxisStyle.newAxisStyle(Color.WHITE, 20, AxisTextAlignment.RIGHT));
       chart.addConcentricAxisLabels(contrentricAxisLabels);
       //chart.setBackgroundFill(Fills.newSolidFill(BLACK));
       String url = chart.toURLString();
       // EXAMPLE CODE END. Use this url string in your web or
       // Internet application.
       Logger.global.info(url);
       return url; 
   }
}