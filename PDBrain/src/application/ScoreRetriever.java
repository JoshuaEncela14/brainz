package application;

import java.sql.*;

public class ScoreRetriever {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/brainzmcq_mysql";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        // Example of retrieving scores
        retrieveScores(1); // Example user id
    }

    public static void retrieveScores(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String query = "SELECT * FROM score WHERE UserId = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int scoreId = rs.getInt("scoreId");
                int enScore = rs.getInt("en_score");
                int mathScore = rs.getInt("math_score");
                int sciScore = rs.getInt("sci_score");
                int overallScore = rs.getInt("overall_score");
                int stageId = rs.getInt("stageId");

                System.out.println("Score ID: " + scoreId);
                System.out.println("English Score: " + enScore);
                System.out.println("Math Score: " + mathScore);
                System.out.println("Science Score: " + sciScore);
                System.out.println("Overall Score: " + overallScore);
                System.out.println("Stage ID: " + stageId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
