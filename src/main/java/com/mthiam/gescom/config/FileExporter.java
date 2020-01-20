package com.mthiam.gescom.config;

import com.mthiam.gescom.models.ArticleExport;
import com.mthiam.gescom.models.EntreeExport;
import com.mthiam.gescom.models.VenteExport;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public abstract class FileExporter {

    public static void exportArticleToExcel(List<ArticleExport> dataList,String pageName,String exportName) throws IOException, ParserConfigurationException, SAXException, URISyntaxException {

        String[] columns = {"Code article", "Référence", "Désignation", "Prix de Base","Catégorie","Quantité restante","Quantité minimum"};
        // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        /* CreationHelper helps us create instances of various things like DataFormat,
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet(pageName);

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);


        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

//        // Create Cell Style for formatting Date
//        CellStyle dateCellStyle = workbook.createCellStyle();
//        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        // Create Other rows and cells with employees data
        int rowNum = 1;
        for(ArticleExport articleExport: dataList) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(articleExport.getCodearticle());

            row.createCell(1)
                    .setCellValue(articleExport.getReference());

            row.createCell(2)
                    .setCellValue(articleExport.getDesignation());

//            Cell dateOfBirthCell = row.createCell(2);
//            dateOfBirthCell.setCellValue(employee.getDateOfBirth());
//            dateOfBirthCell.setCellStyle(dateCellStyle);

            row.createCell(3)
                    .setCellValue(articleExport.getPrixBase());

            row.createCell(4)
                    .setCellValue(articleExport.getCategorie());

            row.createCell(5)
                    .setCellValue(articleExport.getQteEnStock());

            row.createCell(6)
                    .setCellValue(articleExport.getQteAlerte());
        }

        // Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(AppConfiguration.getEntrepriseDirectory()+"/"+exportName+".xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();

    }

    public static void exportArticleFinStockToExcel(List<ArticleExport> dataList,String pageName,String exportName) throws IOException, ParserConfigurationException, SAXException, URISyntaxException {

        String[] columns = {"Code article", "Référence", "Désignation", "Prix de Base","Catégorie","Quantité restante","Quantité minimum"};
        // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        /* CreationHelper helps us create instances of various things like DataFormat,
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet(pageName);

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);


        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

//        // Create Cell Style for formatting Date
//        CellStyle dateCellStyle = workbook.createCellStyle();
//        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        // Create Other rows and cells with employees data
        int rowNum = 1;
        for(ArticleExport articleExport: dataList) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(articleExport.getCodearticle());

            row.createCell(1)
                    .setCellValue(articleExport.getReference());

            row.createCell(2)
                    .setCellValue(articleExport.getDesignation());

//            Cell dateOfBirthCell = row.createCell(2);
//            dateOfBirthCell.setCellValue(employee.getDateOfBirth());
//            dateOfBirthCell.setCellStyle(dateCellStyle);

            row.createCell(3)
                    .setCellValue(articleExport.getPrixBase());

            row.createCell(4)
                    .setCellValue(articleExport.getCategorie());

            row.createCell(5)
                    .setCellValue(articleExport.getQteEnStock());

            row.createCell(6)
                    .setCellValue(articleExport.getQteAlerte());
        }

        // Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(AppConfiguration.getEntrepriseDirectory()+"/"+exportName+".xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();

    }

    public static void exportEntreesToExcel(List<EntreeExport> dataList, String pageName, String exportName) throws IOException, ParserConfigurationException, SAXException, URISyntaxException {

        String[] columns = {"Numero entrée", "Fournisseur", "Numéro facture", "Date","Montant","Enregistré par"};
        // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        /* CreationHelper helps us create instances of various things like DataFormat,
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet(pageName);

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);


        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

//        // Create Cell Style for formatting Date
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        // Create Other rows and cells with employees data
        int rowNum = 1;
        for(EntreeExport e: dataList) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(e.getNumeroEntree());

            row.createCell(1)
                    .setCellValue(e.getFournisseur());

            row.createCell(2)
                    .setCellValue(e.getNumeroFacture());

            Cell dateOfBirthCell = row.createCell(3);
            dateOfBirthCell.setCellValue(e.getEntreeDate());
            dateOfBirthCell.setCellStyle(dateCellStyle);



            row.createCell(4)
                    .setCellValue(e.getMontantEntree());

            row.createCell(5)
                    .setCellValue(e.getUtilisateur());


        }

        // Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(AppConfiguration.getEntrepriseDirectory()+"/"+exportName+".xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();

    }


    public static void exportVentesToExcel(List<VenteExport> dataList, String pageName, String exportName) throws IOException, ParserConfigurationException, SAXException, URISyntaxException {

        String[] columns = {"Numero vente", "Client", "Date","Montant total","Montant versé","Montant restant","Enregistré par"};
        // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        /* CreationHelper helps us create instances of various things like DataFormat,
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet(pageName);

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);


        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

//        // Create Cell Style for formatting Date
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        // Create Other rows and cells with employees data
        int rowNum = 1;
        for(VenteExport v: dataList) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(v.getNumeroVente());

            row.createCell(1)
                    .setCellValue(v.getClient());



            Cell dateOfBirthCell = row.createCell(2);
            dateOfBirthCell.setCellValue(v.getVenteDate());
            dateOfBirthCell.setCellStyle(dateCellStyle);



            row.createCell(3)
                    .setCellValue(v.getMontantVente());

            row.createCell(4)
                    .setCellValue(v.getRestant());

            row.createCell(5)
                    .setCellValue(v.getMontantVente()  - v.getRestant());

            row.createCell(6)
                    .setCellValue(v.getUtilisateur());


        }

        // Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(AppConfiguration.getEntrepriseDirectory()+"/"+exportName+".xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();

    }
}
