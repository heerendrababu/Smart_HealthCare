package com.HealthCareDemo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.HealthCareDemo.model.Report;

public class ReportDao {
	private static String dbUrl = "jdbc:mysql://localhost:3306/practiceproject";
	private static String dbUsername = "root";
	private static String dbPassword = "Babu@123";
	
	   private static Connection cn = null;
	    private static PreparedStatement ps = null;

	// Method to save report to the database
	public static boolean saveReport(Report report) {
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        cn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
	        String sql = "INSERT INTO Report (patientName, patientEmail, bloodGroup, description) VALUES (?, ?, ?, ?)";
            ps = cn.prepareStatement(sql);
            ps.setString(1, report.getPatientName());
            ps.setString(2, report.getPatientEmail());
            ps.setString(3, report.getBloodGroup());
            ps.setString(4, report.getDescription());
            int rows = ps.executeUpdate();
            return rows > 0; // Return true if the report was saved
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("Class Not Found Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (cn != null) cn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }}