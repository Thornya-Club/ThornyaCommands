package thornyaplugin.thornyaplugin.prefeitura.database;

import thornyaplugin.thornyaplugin.ThornyaCommands;

import java.sql.*;

public class SQLite {

    private final ThornyaCommands pl;
    private String urlVotos = null;

    public SQLite(ThornyaCommands main){
        this.pl = main;
        urlVotos = "jdbc:sqlite:" + pl.getDataFolder() + "/votos.db";
        criarDbVotacao();

    }
    private Connection connectVotos() {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(urlVotos);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    public void criarDbVotacao() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS votos (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	nick string NOT NULL,\n"
                + "	vote string NOT NULL,\n"
                + "	voted integer NOT NULL\n"
                + ");";

        try (Connection conn =  connectVotos();
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                connectVotos().close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }
    public void createVoted(String nick, String vote) {
        String sql = "INSERT INTO votos (nick, vote, voted) VALUES(?, ?, 1)";
        try (Connection conn = this.connectVotos();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nick);
            pstmt.setString(2, vote);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void restartVotes(){
        String sql = "DELETE FROM votos";

        try (Connection conn = this.connectVotos();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public Boolean hasVoted(String nick){
        boolean exist = false;
        String sql = "SELECT COUNT(*) FROM votos WHERE nick = ?";
        try (Connection conn = this.connectVotos();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setString(1, nick);//
            ResultSet rs  = pstmt.executeQuery();
            while(rs.next()){
                if(rs.getInt(1) != 0){
                    exist = true;
                }
            }
            rs.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return exist;
    }
    public int getVotes(String candidato){
        int exist = 0;
        String sql = "SELECT COUNT(*) FROM votos WHERE vote = ?";
        try (Connection conn = this.connectVotos();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){
            pstmt.setString(1, candidato);//
            ResultSet rs  = pstmt.executeQuery();
            while(rs.next()){
                if(rs.getInt(1) != 0){
                    exist = rs.getInt(1);
                }
            }
            rs.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return exist;
    }

}
