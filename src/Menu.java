import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Menu {
    private int option;

    public Menu() {
        super();
    }

    public int menuPral() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        do {

            System.out.println(" \nMENU PRINCIPAL \n");
            System.out.println("1. Mostra equipos ");
            System.out.println("2. Mostra jugadors d'un determinat equip ");
            System.out.println("3. Crea equip ");
            System.out.println("4. Crea Jugador. ");
            System.out.println("5. Crea Partit. ");
            System.out.println("6. Mostra jugadors sense equip.  ");
            System.out.println("7. Assigna  jugador a un equip.");
            System.out.println("8. Desvincula jugador  d'un equip.");
            System.out.println("9. Carrega estadístiques.");
            System.out.println("10. Sortir. ");

            System.out.println("Esculli opció: ");
            try {
                option = Integer.parseInt(br.readLine());
            } catch (NumberFormatException | IOException e) {
                System.out.println("valor no vàlid");
                e.printStackTrace();

            }

        } while (option != 1 && option != 2 && option != 3 && option != 4 && option != 5 && option != 6 && option != 7
                && option != 8 && option != 9 && option != 10);

        return option;
    }

    public Identity autenticacio(int intents) throws IOException {
        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("==============HEMEROTECA====================================");
        System.out.println("============================================================");
        System.out.println("Avís: tens " + (3 - intents) + " intents per loginarte");
        System.out.println("============================================================");
        System.out.println("Inserta nom del usuari: ");
        String usuari = br1.readLine();
        System.out.println("Inserta contrasenya: ");
        String pass = br1.readLine();

        Identity identity = new Identity(usuari, pass);
        return identity;

    }

}