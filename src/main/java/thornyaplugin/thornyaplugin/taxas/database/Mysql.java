package thornyaplugin.thornyaplugin.taxas.database;
import thornyaplugin.thornyaplugin.ThornyaCommands;

import java.sql.*;

public class Mysql {

    private final ThornyaCommands pl;

    public Connection conn;
    private String host, database, username, password, ssl;
    private int port;

    public Mysql(ThornyaCommands main){
        this.pl = main;
        loadMysql();
        createDatabase();
    }
    private Double getPlayerBalanceBank(String username){
        loadMysql();
        Double valor = 0.0;
        try {
            PreparedStatement st = getConnect().prepareStatement("SELECT money FROM meb_accounts WHERE player_name = ?");
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                valor = rs.getDouble("money");
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valor;
    }
    public void updatePlayer(String UUID, String player_name, String cargo, String tag, Double money,
                             Float kdr, Integer deaths, Integer civilian_kills,
                             Integer neutral_kills, Integer rival_kills, Boolean player_clan_leader,
                             Boolean player_clan_trusted, Boolean player_friendly_fire, Float player_exp,
                             Integer player_exp_lvl, Boolean player_operator, Integer player_gamemode){
        loadMysql();
        try {
            PreparedStatement st = getConnect().prepareStatement("REPLACE INTO `thornya_player`(`player_uuid`, `player_nome`, `player_tag`, `player_clan_tag`, `player_money_banco`, `player_money_balance`, `player_kdr`, `player_deaths`, `player_civilian_kills`, `player_neutral_kills`, `player_rival_kills`, `player_leader`, `player_trusted`, `player_friendly_fire`, `player_exp`, `player_exp_lvl`, `player_operator`, `player_gamemode`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            st.setString(1, UUID);
            st.setString(2, player_name);
            st.setString(3, cargo);
            st.setString(4, tag);
            st.setDouble(5, money);
            st.setDouble(6, this.getPlayerBalanceBank(player_name));
            st.setFloat(7, kdr);
            st.setInt(8, deaths);
            st.setInt(9, civilian_kills);
            st.setInt(10, neutral_kills);
            st.setInt(11, rival_kills);
            int player_leaderI = player_clan_leader?1:0;
            st.setInt(12, player_leaderI);
            int player_trustedI = player_clan_trusted?1:0;
            st.setInt(13, player_trustedI);
            int player_friendly_fireI = player_friendly_fire?1:0;
            st.setInt(14, player_friendly_fireI);
            st.setFloat(15, player_exp);
            st.setInt(16, player_exp_lvl);
            int player_operatorI = player_operator?1:0;
            st.setInt(17, player_operatorI);
            st.setInt(18, player_gamemode);

            st.executeUpdate();
            st.close();
            closeConnect(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateNoClanPlayer(String UUID, String player_name, String cargo, Double money,
                                   Float player_exp, Integer player_exp_lvl, Boolean player_operator, Integer player_gamemode){
        loadMysql();
        try {
            PreparedStatement st = getConnect().prepareStatement("REPLACE INTO `thornya_player`(`player_uuid`, `player_nome`, `player_tag`, `player_money_banco`, `player_money_balance`, `player_exp`, `player_exp_lvl`, `player_operator`, `player_gamemode`) VALUES (?,?,?,?,?,?,?,?,?)");
            st.setString(1, UUID);
            st.setString(2, player_name);
            st.setString(3, cargo);
            st.setDouble(4, money);
            st.setDouble(5, this.getPlayerBalanceBank(player_name));
            st.setFloat(6, player_exp);
            st.setInt(7, player_exp_lvl);
            int player_operatorI = player_operator?1:0;
            st.setInt(8, player_operatorI);
            st.setInt(9, player_gamemode);

            st.executeUpdate();
            st.close();
            closeConnect(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateValorClan(Double valor) {
        loadMysql();
        try {
            PreparedStatement st = getConnect().prepareStatement("UPDATE taxas SET valor = ? WHERE ID = 0");
            st.setDouble(1, valor);
            st.executeUpdate();
            st.close();
            closeConnect(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateValorPrefeitura(Double valor) {
        loadMysql();
        try {
            PreparedStatement st = getConnect().prepareStatement("UPDATE taxas SET valor = ? WHERE ID = 1");
            st.setDouble(1, valor);
            st.executeUpdate();
            st.close();
            closeConnect(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String getPrefeito() {
        loadMysql();
        String valor = "";
        try {
            PreparedStatement st = getConnect().prepareStatement("SELECT data FROM config WHERE id = 1");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String[] valorC = rs.getString("data").split(":");
                valor = valorC[1];
            }
            st.close();
            closeConnect(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valor;
    }
    public String getTagClan() {
        loadMysql();
        String[] valorC = null;
        try {
            PreparedStatement st = getConnect().prepareStatement("SELECT data FROM config WHERE id = 2");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                valorC = rs.getString("data").split(":");
            }
            st.close();
            closeConnect(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert valorC != null;
        return valorC[1];
    }
    public Double getValorClan() {
        loadMysql();
        Double valor = 0.0;
        try {
            PreparedStatement st = getConnect().prepareStatement("SELECT valor FROM taxas WHERE ID = 0");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                valor = rs.getDouble("valor");
            }
            st.close();
            closeConnect(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valor;
    }
    public Double getValorPrefeitura() {
        loadMysql();
        Double valor = 0.0;
        try {
            PreparedStatement st = getConnect().prepareStatement("SELECT valor FROM taxas WHERE ID = 1");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                valor = rs.getDouble("valor");
            }
            st.close();
            closeConnect(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valor;
    }
    public void createDatabase() {
        loadMysql();
        try {
            PreparedStatement st = getConnect().prepareStatement("CREATE DATABASE IF NOT EXISTS taxas");
            st.close();
            closeConnect(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadMysql() {
        if (pl.config.getFile("configuration").getBoolean("mysql.enable")) {
            ssl = "false";
            host = pl.config.getFile("configuration").getString("mysql.host");
            database = pl.config.getFile("configuration").getString("mysql.database");
            username = pl.config.getFile("configuration").getString("mysql.username");
            port = pl.config.getFile("configuration").getInt("mysql.port");
            password = pl.config.getFile("configuration").getString("mysql.password");
            try {
                if (getConnect() != null && !getConnect().isClosed()) {
                    return;
                }
                Class.forName("java.sql.Driver");
                setConnect(DriverManager.getConnection("jdbc:mysql://" +
                        this.host + ":" + this.port + "/" + this.database +
                        "?autoReconnect=true&useSSL=" + this.ssl, this.username, this.password));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnect() {
        return conn;
    }

    public void setConnect(Connection connect) {
        this.conn = connect;
    }

    public void closeConnect(Connection connect) throws SQLException {
        connect.close();
    }

}