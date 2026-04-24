package astrobankapp.utils;

import astrobankapp.exception.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ClienteFormularioValidacion {
    static Scanner sc = new Scanner(System.in);

    public static String validateString(String prompt) throws InputMismatchException {

        while(true) {


            System.out.println(prompt);
            String value = sc.nextLine().trim();
            if(!value.isEmpty()){
                return value;
            }
            sc.nextLine();
            throw new InputMismatchException("Error al ingresar el valor , el campo no debe estar vacio");
        }

    }

    public static double validateDouble(String prompt) {

        while(true){
            try{

                System.out.println(prompt);
                double value = sc.nextDouble();
                sc.nextLine();
                return value;

            }catch (InputMismatchException e){
                System.out.println("Error al ingresar el valor, este debe ser un numero decimal");
                sc.nextLine();
            }
        }
    }

    public static int validateInt(String prompt) {

        while(true){
            try{

                System.out.println(prompt);
                int value = sc.nextInt();
                sc.nextLine();
                return value;

            }catch (InputMismatchException e){
                System.out.println("Error al ingresar el valor, este debe ser un numero entero");
                sc.nextLine();
            }
        }
    }

    public static void validateCustomerForm(
            String identification,
            String name,
            String user,
            String phone,
            String email,
            String password,
            String confirmPassword) {

        if (name == null || name.trim().isEmpty()) {
            throw new FieldRequiredException("Nombre"); // Name is required
        }
        if (user == null || user.trim().isEmpty()) {
            throw new FieldRequiredException("Usuario"); // Name is required
        }
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidEmailException(email); // Invalid email format
        }
        if (phone == null || !phone.matches("^\\d{10}$")) {
            throw new InvalidPhoneException(phone); // Phone number must be 10 digits
        }
        if (identification == null || identification.trim().isEmpty()) {
            throw new FieldRequiredException("Identificaion"); // identificacion is required
        }
        // 1. Validar que la contraseña no esté vacía
        if (password == null || password.isEmpty()) {
            throw new FieldRequiredException("Contraseña");
        }

        // 2. Validar longitud mínima (ejemplo: 6 caracteres)
        if (password.length() < 6) {
            throw new ValidationException("La contraseña debe tener al menos 6 caracteres.");
        }

        // 3. Validar coincidencia (Confirmación)
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException();
        }

        // All validations passed
    }

    public static void validateLoginForm(String user, String password) {
        if (user == null || user.isEmpty()) throw new FieldRequiredException("Usuario");
        if (password == null || password.isEmpty()) throw new FieldRequiredException("Contraseña");
    }

}
