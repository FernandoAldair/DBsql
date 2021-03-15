import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;


public class HemerotecaMain {

    public static void main(String[] args) throws IOException, SQLException, ParseException {
        Menu menu = new Menu();
        Connection conn = null;
        Identity identity;
        int option;
        int intents = 0;
        DBAccessor dbaccessor = new DBAccessor();
        dbaccessor.init();
        while (intents < 3 && conn == null) {
            identity = menu.autenticacio(intents);
            // prova de test
            identity.toString();

            conn = dbaccessor.getConnection(identity);
            intents++;
        }

        option = menu.menuPral();
        while (option > 0 && option < 12) {
            switch (option) {
                case 1:
                    dbaccessor.mostraEquip();
                    break;

                case 2:
                    dbaccessor.mostrarPlayer();
                    break;

                case 3:
                    dbaccessor.crearEquipo();

                case 4:
                    dbaccessor.crearPlayer();
                    break;

                case 5:
                    dbaccessor.crearMatch();
                    break;

                case 6:
                    dbaccessor.jugadorSinEquipo();
                    break;

                case 7:
                    dbaccessor.asignarJugadorAlEquipo();
                    break;

                case 8:

                    break;

                case 9:

                    break;

                case 10:
                    dbaccessor.sortir();
                    break;

                default:
                    System.out.println("Introdueixi una de les opcions anteriors");
                    break;

            }
            option = menu.menuPral();
        }

    }

}
