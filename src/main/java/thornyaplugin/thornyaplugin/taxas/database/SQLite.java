package thornyaplugin.thornyaplugin.taxas.database;

import thornyaplugin.thornyaplugin.ThornyaCommands;

import java.sql.*;

public class SQLite {
    private final ThornyaCommands pl;
    String urlPrefeitura = null;
    String urlClan = null;
    public SQLite(ThornyaCommands main){
        this.pl = main;
        urlPrefeitura = "jdbc:sqlite:" + pl.getDataFolder() + "/TaxasPrefeitura.db";
        urlClan = "jdbc:sqlite:" + pl.getDataFolder() + "/TaxasClan.db";
        criarDbTaxaPrefeitura();
        criarDbTaxaClan();
    }

    private Connection connectPrefeitura() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(urlPrefeitura);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    ////////////////////////////////////////////////////////////////////////////////////////
    public void criarDbTaxaPrefeitura() {
        String sql = "CREATE TABLE IF NOT EXISTS Taxas (valor real NOT NULL);";

        try (Connection conn =  connectPrefeitura();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                connectPrefeitura().close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void criarValorPrefeitura(Float valor) {
        String sql = "INSERT INTO Taxas (valor) VALUES(?)";
        try (Connection conn = this.connectPrefeitura();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setFloat(1, valor);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public Double getValuePrefeitura(){
        Double valor = 0.0;
        String sql = "SELECT valor FROM Taxas";
        try (Connection conn = this.connectPrefeitura();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){
            ResultSet rs  = pstmt.executeQuery();

            while (rs.next()) {
                valor = rs.getDouble("valor");
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return valor;
    }

    public void updateValuePrefeitura(Float value) {
        String sql = "UPDATE Taxas SET valor = ?";

        try (Connection conn = this.connectPrefeitura();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, value);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////

    private Connection connectClan() {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(urlClan);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    ////////////////////////////////////////////////////////////////////////////////////////
    public void criarDbTaxaClan() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS Taxas (valor real NOT NULL);";

        try (Connection conn =  connectClan();
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                connectClan().close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void criarValorClan(Float valor) {
        String sql = "INSERT INTO Taxas (valor) VALUES(?)";
        try (Connection conn = this.connectClan();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setFloat(1, valor);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public Double getValueClan(){
        Double valor = 0.0;
        String sql = "SELECT valor FROM Taxas";
        try (Connection conn = this.connectClan();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){
            ResultSet rs  = pstmt.executeQuery();

            while (rs.next()) {
                valor = rs.getDouble("valor");
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return valor;
    }

    public void updateValueClan(Float value) {
        String sql = "UPDATE Taxas SET valor = ?";

        try (Connection conn = this.connectClan();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, value);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}