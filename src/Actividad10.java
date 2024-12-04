import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Actividad10 {
    private static final String LOG_FILE = "password_validation_log.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExecutorService executorService = Executors.newCachedThreadPool();

        try {
            Files.writeString(Path.of(LOG_FILE), "", StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error al inicializar el archivo de registro: " + e.getMessage());
            return;
        }

        System.out.println("Ingrese una contraseña para validar (o 'exit' para salir):");
        String input;

        while (!(input = scanner.nextLine()).equalsIgnoreCase("exit")) {
            final String password = input;
            executorService.execute(() -> {
                String result = isValidPassword(password)
                        ? "válida"
                        : "no válida";
                String logEntry = "Contraseña: \"" + password + "\", Resultado: " + result + "\n";
                System.out.println("La contraseña \"" + password + "\" es " + result + ".");
                writeToLog(logEntry);
            });
            System.out.println("Ingrese otra contraseña para validar (o 'exit' para salir):");
        }

        executorService.shutdown();
        System.out.println("Validación de contraseñas finalizada. Revisa el archivo: " + LOG_FILE);
    }

    private static boolean isValidPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*\\d.*") &&
                password.chars().filter(Character::isUpperCase).count() >= 2 &&
                password.chars().filter(Character::isLowerCase).count() >= 3 &&
                password.matches(".*[^a-zA-Z0-9].*");
    }

    private static void writeToLog(String logEntry) {
        try {
            Files.writeString(Path.of(LOG_FILE), logEntry, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de registro: " + e.getMessage());
        }
    }
}