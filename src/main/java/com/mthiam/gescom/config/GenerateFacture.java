/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthiam.gescom.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;



/**
 *
 * @author Mbaye Sokhna THIAM
 */
public class GenerateFacture {
    
    private static Connection getConnection() {
        Connection con = null;
        String url = "jdbc:mysql://localhost:3306/mbaye?serverTimezone=UTC";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, "mbaye", "passer");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Driver class not found. Please add MySQL connector jar in classpath");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Exception occured while getting Database connection");
        }
        return con;
    }
    
    
    public static void showOrdonnance(Map<String, Object> parameters){
        
        Connection connection = null;
        try {

            connection = getConnection(); // opens a jdbc connection

            InputStream sourceFile = GenerateFacture.class.getResourceAsStream("/reports/facture.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(sourceFile);

            // fills compiled report with parameters and a connection
            JasperPrint jasperPrint = JasperFillManager.fillReport (jasperReport,parameters,connection);
            
            JasperViewer.viewReport(jasperPrint, false);

            

        } catch (Exception e) {
            throw new RuntimeException("It's not possible to generate the pdf report.", e);
        } finally {
            // it's your responsibility to close the connection, don't forget it!
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }

    }

    public static void printReport(Map<String, Object> parameters){

        Connection connection = null;
        try {

            connection = getConnection(); // opens a jdbc connection

            InputStream sourceFile = GenerateFacture.class.getResourceAsStream("/reports/facture.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(sourceFile);

            // fills compiled report with parameters and a connection
            JasperPrint jasperPrint = JasperFillManager.fillReport (jasperReport,parameters,connection);

            //JasperViewer.viewReport(jasperPrint, false);
            JasperPrintManager.printReport(jasperPrint,false);



        } catch (Exception e) {
            throw new RuntimeException("It's not possible to generate the pdf report.", e);
        } finally {
            // it's your responsibility to close the connection, don't forget it!
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }

    }
    
}
