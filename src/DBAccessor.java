import javax.sound.midi.Soundbank;
import javax.swing.plaf.nimbus.State;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DBAccessor {
    private String dbname;
    private String host;
    private String port;
    private String user;
    private String passwd;
    private String schema;
    Connection conn = null;

    /**
     * Initializes the class loading the database properties file and assigns
     * values to the instance variables.
     *
     * @throws RuntimeException
     *             Properties file could not be found.
     */
    public void init() {
        Properties prop = new Properties();
        InputStream propStream = this.getClass().getClassLoader().getResourceAsStream("db.properties");

        try {
            prop.load(propStream);
            this.host = prop.getProperty("host");
            this.port = prop.getProperty("port");
            this.dbname = prop.getProperty("dbname");
            this.schema = prop.getProperty("schema");
        } catch (IOException e) {
            String message = "ERROR: db.properties file could not be found";
            System.err.println(message);
            throw new RuntimeException(message, e);
        }
    }

    /**
     * Obtains a {@link Connection} to the database, based on the values of the
     * <code>db.properties</code> file.
     *
     * @return DB connection or null if a problem occurred when trying to
     *         connect.
     */
    public Connection getConnection(Identity identity) {

        // Implement the DB connection
        String url = null;
        try {
            // Loads the driver
            Class.forName("org.postgresql.Driver");

            // Preprara connexió a la base de dades
            StringBuffer sbUrl = new StringBuffer();
            sbUrl.append("jdbc:postgresql:");
            if (host != null && !host.equals("")) {
                sbUrl.append("//").append(host);
                if (port != null && !port.equals("")) {
                    sbUrl.append(":").append(port);
                }
            }
            sbUrl.append("/").append(dbname);
            url = sbUrl.toString();

            // Utilitza connexió a la base de dades
            conn = DriverManager.getConnection(url, identity.getUser(), identity.getPassword());
            conn.setAutoCommit(false);
        } catch (ClassNotFoundException e1) {
            System.err.println("ERROR: Al Carregar el driver JDBC");
            System.err.println(e1.getMessage());
        } catch (SQLException e2) {
            System.err.println("ERROR: No connectat  a la BD " + url);
            System.err.println(e2.getMessage());
        }

        // Sets the search_path
        if (conn != null) {
            Statement statement = null;
            try {
                statement = conn.createStatement();
                statement.executeUpdate("SET search_path TO " + this.schema);
                // missatge de prova: verificació
                System.out.println("OK: connectat a l'esquema " + this.schema + " de la base de dades " + url
                        + " usuari: " + user + " password:" + passwd);
                System.out.println();
                //
            } catch (SQLException e) {
                System.err.println("ERROR: Unable to set search_path");
                System.err.println(e.getMessage());
            } finally {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.err.println("ERROR: Closing statement");
                    System.err.println(e.getMessage());
                }
            }
        }

        return conn;
    }

    public void mostraEquip() throws SQLException, IOException {
        Statement statement = conn.createStatement();
        ResultSet resultSet;
        resultSet = statement.executeQuery("SELECT * FROM team");
        while (resultSet.next()) {
            System.out.println("Name: " + resultSet.getString("name") + "\t" +
                    "type: " + resultSet.getString("type") + "\t" +
                    "country: " + resultSet.getString("country") + "\t" +
                    "city: " + resultSet.getString("city") + "\t" +
                    "court name: " + resultSet.getString("court_name"));
        }
        resultSet.close();
        statement.close();
    }

    public void mostrarPlayer()throws SQLException{
        Statement statement =  conn.createStatement();
        ResultSet resultSet;
        resultSet = statement.executeQuery("SELECT * FROM player");
        while (resultSet.next()) {
            System.out.println("Federation license code: "+resultSet.getString("federation_license_code")+"\t" +
                    "first name: "+resultSet.getString("first_name")+"\t" +
                    "last name: "+resultSet.getString("last_name")+"\t" +
                    "birth date: "+resultSet.getString("birth_date")+"\t" +
                    "gender: "+resultSet.getString("gender")+"\t" +
                    "height: "+resultSet.getString("height")+"\t" +
                    "team name: "+resultSet.getString("team_name")+"\t" +
                    "mvp total: "+resultSet.getString("mvp_total"));
        }
        resultSet.close();
        statement.close();
    }

    public void crearEquipo() throws SQLException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insertar nombre del equipo:");
        String nombre = scanner.nextLine();
        System.out.println("Insertar el tipo 1 o 2(Escoger numero):");
        System.out.println("1- Club");
        System.out.println("2- Nacional");
        int opcion = scanner.nextInt();
        String tipo;
        if (opcion == 1){
            tipo = "Club";
        } else tipo = "Nacional";
        System.out.println("Insertar pais:");
        String pais = scanner.nextLine();
        String ciudad="";
        if (opcion == 2){
            System.out.println("Insertar ciudad:");
            ciudad = scanner.nextLine();
        }
        System.out.println("Insertar cancha del equipo:");
        String court = scanner.nextLine();

        Statement statement = null;
        statement = conn.createStatement();
        statement.executeUpdate("INSERT team VALUES ('"+nombre+"','"+tipo+"','"+pais+"','"+ciudad+"','"+court+"')");

        statement.close();
    }

    public void crearPlayer() throws SQLException, IOException, ParseException {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Inserar Federation license code:");
        String federation = scanner.nextLine();
        System.out.println("Insertar nombre:");
        String nombre = scanner.nextLine();
        System.out.println("Insertar apellidos:");
        String apellidos = scanner.nextLine();
        System.out.println("Insertar fecha de nacimiento(año-mes-dia:0000-00-00):");
        Date date = fecha.parse(scanner.nextLine());
        System.out.println("Insertar genero:");
        String genero = scanner.nextLine();
        System.out.println("Insertar altura:");
        float altura = scanner.nextInt();
        System.out.println("Insertar nombre del equipo:");
        String equipo = scanner.nextLine();
        System.out.println("Insertar total MPV");
        int mvp = scanner.nextInt();

        Statement statement = null;
        statement = conn.createStatement();

        statement.executeUpdate("INSERT INTO player VALUES" +
                " ('"+federation+"','"+nombre+"','"+apellidos+"','"+date+"','"+genero+"','"+altura+"','"+equipo+"','"+mvp+"')");
        statement.close();
    }

    public void crearMatch() throws SQLException, ParseException{
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Insertar equipo local");
        String home_team = scanner.nextLine();
        System.out.println("Insertar equipo visitante");
        String visitor_team = scanner.nextLine();
        System.out.println("Insertar fecha del enfrentamiento (yyyy-MM-dd:0000-00-00");
        Date date = fecha.parse(scanner.nextLine());
        System.out.println("Insertar assistencia");
        Long assistencia = scanner.nextLong();
        System.out.println("Insertar MVP");
        String mvp = scanner.nextLine();
        Statement statement = null;
        statement.executeUpdate("INSERT INTO match VALUES ('"+home_team+"','"+visitor_team+"','"+date+"','"+assistencia+"','"+mvp+"')");
        statement.close();

    }

    public void jugadorSinEquipo() throws SQLException{
        Statement statement = conn.createStatement();
        ResultSet resultSet;

        resultSet = statement.executeQuery("SELECT * FROM player WHERE team_name is null");
        while (resultSet.next()) {
            System.out.println("Federation license code: "+resultSet.getString("federation_license_code")+"\t" +
                    "First name: "+resultSet.getString("first_name")+"\t" +
                    "Last name: "+resultSet.getString("last_name")+"\t" +
                    "birth date: "+resultSet.getString("birth_date")+"\t" +
                    "Gender: "+resultSet.getString("gender")+"\t" +
                    "Gender: "+resultSet.getString("gender")+"\t" +
                    "Height: "+resultSet.getString("height")+"\t" +
                    "Team name: "+resultSet.getString("team_name")+"\t" +
                    "MVP total: "+resultSet.getString("mvp_total"));
        }
        resultSet.close();
        statement.close();
    }

    public void asignarJugadorAlEquipo(){

    }

    public void desvincularJugadorDelEquipo(){

    }

    public void cargarEstadisticas(){

    }





    public void sortir() throws SQLException {
        System.out.println("ADÉU!");
        conn.close();
    }

}