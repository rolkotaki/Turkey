
package model;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import database.Database;
import interfaces.ModelInterface;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.util.Date;

/**
 * A ModelPDF osztály segítségével a szezon befejezésekor exportálok egy
 * kimutatást egy PDF fájlba. A helyet a felhasználó választja ki. A megadott
 * helyet létrejön egy új mappa, és ugyanebbe a mappába mentődnek el az Excel
 * fájlok is.
 */
public class ModelPDF implements ModelInterface {
    
    private static String filePath;
    private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 28,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 14,
            Font.BOLD);
    private static Document doc;
    private static ModelDatasForPDF mdPDF;
    
    private static ModelDatasForPDF createDatasForPDF(String path) {
        Database.openConnection();
        ResultSet rs = Database.selectTurkeyArrive();
        ResultSet rs2 = Database.selectWeighing();
        ModelDatasForPDF mdfPDF = null;
        doc = new Document(PageSize.A4);
        try {
            rs.next();
            rs2.next();
            mdfPDF = new ModelDatasForPDF();
            mdfPDF.setSeasonName(SDFDATE.format(Database.selectArriveDate()) + "_" +
                    SDFDATE.format(new Date()));
            mdfPDF.setFile(path + "/szezon_" + mdfPDF.getSeasonName() + "/kitermeles.pdf");
            File f = new File(path + "/szezon_" + mdfPDF.getSeasonName());
            if(!f.exists())
                f.mkdirs();
            mdfPDF.setBuckArriveNum(rs.getInt("buck"));
            mdfPDF.setHenArriveNum(rs.getInt("hen"));
            mdfPDF.setBuckArriveWeight((int)(rs2.getDouble("buckWeight")*rs.getInt("buck")));
            mdfPDF.setHenArriveWeight((int)(rs2.getDouble("henWeight")*rs.getInt("hen")));
            mdfPDF.setBuckSellNum(Database.selectTurkeyNumber(TURKEY[BUCK]));
            mdfPDF.setHenSellNum(Database.selectTurkeyNumber(TURKEY[HEN]));
            mdfPDF.setBuckSellWeight((int)(Database.selectTurkeySellWeight(TURKEY[BUCK])
                    *mdfPDF.getBuckSellNum()));
            mdfPDF.setHenSellWeight((int)(Database.selectTurkeySellWeight(TURKEY[HEN])
                    *mdfPDF.getHenSellNum()));
            mdfPDF.setLoss(mdfPDF.getBuckArriveNum()+mdfPDF.getHenArriveNum()-
                    mdfPDF.getBuckSellNum()-mdfPDF.getHenSellNum());
            mdfPDF.setLossPerCent(DoubleFormat.doubleFormat((100.0*mdfPDF.getLoss())/
                    (mdfPDF.getBuckArriveNum()+mdfPDF.getHenArriveNum())));
            mdfPDF.setAllReceivedFodder(Database.selectAllReceivedFodder()+Database.selectStockStart());
            mdfPDF.setAllWeightGain(mdfPDF.getBuckSellWeight()+mdfPDF.getHenSellWeight()-
                    mdfPDF.getBuckArriveWeight()-mdfPDF.getHenArriveWeight());
            mdfPDF.setCumIndexNumber(DoubleFormat.doubleFormat((mdfPDF.getAllReceivedFodder()*1.0)/
                    mdfPDF.getAllWeightGain()));
        } catch (Exception e) {
            Database.closeConnection();
            e.printStackTrace();
        }
        Database.closeConnection();
        return mdfPDF;
    }
    
    public static void exportToPDF(String path) {
        try {
            mdPDF=createDatasForPDF(path);
            filePath=mdPDF.getFile();
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
            doc.open();
            Paragraph preface = new Paragraph();
            Paragraph title = new Paragraph("Kitermelés", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            preface.add(new Paragraph(" "));
            preface.add(title);
            preface.add(new Paragraph(" "));
            doc.add(preface);

            Paragraph arrive = new Paragraph();
            arrive.add(new Paragraph(" "));
            arrive.add(new Paragraph("Érkezés", smallBold));
            arrive.add(new Paragraph(" "));
            doc.add(arrive);

            Paragraph tblArrive = new Paragraph();
            PdfPTable tableArrive = new PdfPTable(3);

            PdfPCell c1 = new PdfPCell(new Phrase("Pulyka"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c1.setBorderWidth(2);
            tableArrive.addCell(c1);
            c1 = new PdfPCell(new Phrase("Darabszám"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c1.setBorderWidth(2);
            tableArrive.addCell(c1);
            c1 = new PdfPCell(new Phrase("Tömeg (kg)"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c1.setBorderWidth(2);
            tableArrive.addCell(c1);
            tableArrive.setHeaderRows(1);

            tableArrive.addCell("bak");
            tableArrive.addCell(""+mdPDF.getBuckArriveNum());
            tableArrive.addCell(""+mdPDF.getBuckArriveWeight());
            tableArrive.addCell("tojó");
            tableArrive.addCell(""+mdPDF.getHenArriveNum());
            tableArrive.addCell(""+mdPDF.getHenArriveWeight());
            tableArrive.addCell(new Phrase("összesen", smallBold));
            tableArrive.addCell(new Phrase((""+(mdPDF.getBuckArriveNum()+
                    mdPDF.getHenArriveNum())), smallBold));
            tableArrive.addCell(new Phrase((""+(mdPDF.getBuckArriveWeight()+
                    mdPDF.getHenArriveWeight())), smallBold));

            tblArrive.add(tableArrive);
            doc.add(tblArrive);
            
            tblArrive.add(new Paragraph(" "));
            tblArrive.add(new Paragraph(" "));
            tblArrive.add(new Paragraph(" "));
            
            Paragraph sell = new Paragraph();
            sell.add(new Paragraph(" "));
            sell.add(new Paragraph("Leadás", smallBold));
            sell.add(new Paragraph(" "));
            doc.add(sell);
            
            Paragraph tblSell = new Paragraph();
            PdfPTable tableSell = new PdfPTable(3);
            
            PdfPCell c2 = new PdfPCell(new Phrase("Pulyka"));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c2.setBorderWidth(2);
            tableSell.addCell(c2);
            c2 = new PdfPCell(new Phrase("Darabszám"));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c2.setBorderWidth(2);
            tableSell.addCell(c2);
            c2 = new PdfPCell(new Phrase("Tömeg (kg)"));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c2.setBorderWidth(2);
            tableSell.addCell(c2);
            tableSell.setHeaderRows(1);

            tableSell.addCell("bak");
            tableSell.addCell(""+mdPDF.getBuckSellNum());
            tableSell.addCell(""+mdPDF.getBuckSellWeight());
            tableSell.addCell("tojó");
            tableSell.addCell(""+mdPDF.getHenSellNum());
            tableSell.addCell(""+mdPDF.getHenSellWeight());
            tableSell.addCell(new Phrase("összesen", smallBold));
            tableSell.addCell(new Phrase((""+(mdPDF.getBuckSellNum()+mdPDF.getHenSellNum())), smallBold));
            tableSell.addCell(new Phrase((""+(mdPDF.getBuckSellWeight()+mdPDF.getHenSellWeight())), smallBold));

            tblSell.add(tableSell);
            doc.add(tblSell);
            
            tblSell.add(new Paragraph(" "));
            tblSell.add(new Paragraph(" "));
            tblSell.add(new Paragraph(" "));
            
            Paragraph loss = new Paragraph();
            loss.add(new Paragraph(" "));
            loss.add(new Paragraph("Elhullás", smallBold));
            loss.add(new Paragraph(" "));
            doc.add(loss);
            
            Paragraph tblLoss = new Paragraph();
            PdfPTable tableLoss = new PdfPTable(3);
            
            PdfPCell c3 = new PdfPCell(new Phrase("Elhullás (db)"));
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c3.setBorderWidth(2);
            tableLoss.addCell(c3);
            c3 = new PdfPCell(new Phrase("Elhullás (%)"));
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c3.setBorderWidth(2);
            tableLoss.addCell(c3);
            c3 = new PdfPCell(new Phrase("Kitermelés (%)"));
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c3.setBorderWidth(2);
            tableLoss.addCell(c3);
            tableLoss.setHeaderRows(1);

            tableLoss.addCell(""+mdPDF.getLoss());
            tableLoss.addCell(""+mdPDF.getLossPerCent());
            tableLoss.addCell(""+(100-mdPDF.getLossPerCent()));

            tblLoss.add(tableLoss);
            doc.add(tblLoss);
            
            tblLoss.add(new Paragraph(" "));
            tblLoss.add(new Paragraph(" "));
            tblLoss.add(new Paragraph(" "));
            
            Paragraph fodder = new Paragraph();
            fodder.add(new Paragraph(" "));
            fodder.add(new Paragraph("Takarmány", smallBold));
            fodder.add(new Paragraph(" "));
            doc.add(fodder);
            
            Paragraph tblFodder = new Paragraph();
            PdfPTable tableFodder = new PdfPTable(3);
            
            PdfPCell c4 = new PdfPCell(new Phrase("Fogadás (kg)"));
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            c4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c4.setBorderWidth(2);
            tableFodder.addCell(c4);
            c4 = new PdfPCell(new Phrase("Ráhízás (kg)"));
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            c4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c4.setBorderWidth(2);
            tableFodder.addCell(c4);
            c4 = new PdfPCell(new Phrase("Összesített fajlagos\nmutatószám"));
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            c4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c4.setBorderWidth(2);
            tableFodder.addCell(c4);
            tableFodder.setHeaderRows(1);

            tableFodder.addCell(""+mdPDF.getAllReceivedFodder());
            tableFodder.addCell(""+mdPDF.getAllWeightGain());
            tableFodder.addCell(""+mdPDF.getCumIndexNumber());

            tblFodder.add(tableFodder);
            doc.add(tblFodder);

            doc.close();
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }
    }
    
}

/*
 * Forrás: http://www.vogella.com/articles/JavaPDF/article.html
 * http://www.ibm.com/developerworks/library/os-javapdf/index.html#download
 */
