import enums.Role;
import exceptions.ValidationException;
import interfaces.Logger;
import interfaces.ReadingsService;
import interfaces.UserService;
import logger.LoggerImpl;
import models.User;
import repositories.ReadingsRepositoryImpl;
import repositories.UserRepositoryImpl;
import services.ReadingsServiceImpl;
import services.UserServiceImpl;
import validators.ReadingsValidator;
import validators.UserValidator;

import java.time.DateTimeException;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final UserService userService = new UserServiceImpl(new UserRepositoryImpl(), new UserValidator());
    private static final ReadingsService readingsService = new ReadingsServiceImpl(new ReadingsRepositoryImpl(), new ReadingsValidator());
    private static final Logger logger = LoggerImpl.getInstance();

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            runApplication(scanner);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Запускает приложение и предоставляет пользователю возможность авторизации, регистрации или выхода.
     *
     * @param scanner Объект Scanner для чтения ввода пользователя.
     */
    private static void runApplication(Scanner scanner) {
        logger.info("Приложение запущено");
        while (true) {
            System.out.println("Для авторизации нажмите 1\n" +
                    "Для регистрации нажмите 2\n" +
                    "Если вы желаете выйти, то нажмите 3.");

            String action = scanner.nextLine();
            switch (action) {
                case "1":
                    Optional<User> user = loginUser(scanner);
                    user.ifPresent(currentUser -> redirectToUserPanel(scanner, currentUser));
                    break;
                case "2":
                    registerNewUser(scanner);
                    break;
                case "3":
                    System.out.println("До свидания!");
                    logger.info("Завершение работы приложения");
                    return;
                default:
                    System.out.println("Вы ввели неправильное значение\n");
            }
        }
    }

    /**
     * Аутентифицирует пользователя, запрашивая у него логин и пароль.
     *
     * @param scanner Объект Scanner для чтения ввода пользователя.
     * @return Optional, содержащий пользователя, если он успешно аутентифицирован, или пустой Optional, если аутентификация не удалась.
     */
    private static Optional<User> loginUser(Scanner scanner) {
        System.out.print("Введите ваш логин: ");
        String login = scanner.nextLine();
        System.out.print("Введите ваш пароль: ");
        String password = scanner.nextLine();
        return userService.getUser(login, password);
    }

    /**
     * Регистрирует нового пользователя, запрашивая у него логин и пароль.
     *
     * @param scanner Объект Scanner для чтения ввода пользователя.
     */
    private static void registerNewUser(Scanner scanner) {
        System.out.print("Введите желаемый логин: ");
        String login = scanner.nextLine();
        System.out.print("Введите ваш пароль: ");
        String password = scanner.nextLine();

        try {
            userService.registerUser(login, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Перенаправляет пользователя на панель пользователя или администратора в зависимости от его роли.
     *
     * @param scanner Объект Scanner для чтения ввода пользователя.
     * @param user Пользователь, который должен быть перенаправлен.
     */
    private static void redirectToUserPanel(Scanner scanner, User user) {
        if (user.getRole() == Role.USER) {
            processUsersAction(scanner, user);
        } else if (user.getRole() == Role.ADMIN) {
            redirectToAdminPanel(scanner);
        }
    }

    /**
     * Метод перенаправляет пользователя на панель администратора.
     * Пользователь может выбрать действие: просмотреть данные пользователей или выйти.
     *
     * @param scanner Объект Scanner для чтения ввода пользователя.
     */
    private static void redirectToAdminPanel(Scanner scanner) {
        while (true) {
            System.out.println("Для просмотра показаний пользователей нажмите 1.\n" +
                    "Для выхода нажмите 2.");
            String action = scanner.nextLine();
            switch (action){
                case "1":
                    processAdminActions(scanner);
                    break;
                case "2":
                    return;
                default:
                    System.out.println("Вы ввели неправильное значение.\n");
            }
        }
    }

    /**
     * Метод обрабатывает действия администратора.
     * Администратор вводит логин пользователя, данные которого хочет просмотреть.
     *
     * @param scanner Объект Scanner для чтения ввода администратора.
     */
    private static void processAdminActions(Scanner scanner) {
        System.out.print("Введите логин интересующего вас пользователя: ");
        System.out.println(userService.getLogins());
        String login = scanner.nextLine();
        Optional<User> user = userService.getUser(login);
        user.ifPresent(value -> {
            logger.info("Админ выбрал пользователя: " + user.get().getLogin());
            getAllReadings(user.get());
        });
    }
    /**
     * Обрабатывает действия пользователя, предоставляя ему возможность подачи показаний,
     * просмотра актуальных показаний, просмотра показаний за определенный месяц,
     * просмотра истории подачи показаний, смены пароля аккаунта или выхода.
     *
     * @param scanner Объект Scanner для чтения ввода пользователя.
     * @param user Пользователь, действия которого должны быть обработаны.
     */
    private static void processUsersAction(Scanner scanner, User user) {
        while (true) {
            System.out.println("\n" +
                    "Для подачи показаний нажмите 1\n" +
                    "Для просмотра актуальных показаний нажмите 2\n" +
                    "Для просмотра показаний за определенный месяц нажмите 3\n" +
                    "Для просмотра истории подачи показаний нажмите 4.\n" +
                    "Для смены пароля аккаунта, нажмите 5\n" +
                    "Для того, чтобы выйти, нажмите 6");

            String action = scanner.nextLine();
            switch (action) {
                case "1":
                    addReadings(scanner, user);
                    break;
                case "2":
                    getActualReadings(user);
                    break;
                case "3":
                    getReadingsByMonth(scanner, user);
                    break;
                case "4":
                    getAllReadings(user);
                    break;
                case "5":
                    updateUserPassword(scanner, user);
                    break;
                case "6":
                    logger.info("Пользователь " + user.getLogin() + " вышел.");
                    return;
                default:
                    System.out.println("Вы ввели неправильное значение");
            }
        }
    }

    /**
     * Обновляет пароль пользователя, запрашивая у него старый и новый пароли.
     *
     * @param scanner Объект Scanner для чтения ввода пользователя.
     * @param user Пользователь, который хочет обновить свой пароль.
     */
    private static void updateUserPassword(Scanner scanner, User user) {
        try {
            System.out.print("Введите старый пароль: ");
            String oldPassword = scanner.nextLine();
            System.out.print("Введите новый пароль: ");
            String newPassword = scanner.nextLine();

            userService.changePassword(user, oldPassword, newPassword);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Добавляет показания пользователя, запрашивая у него месяц и показания по отоплению, горячей и холодной воде.
     *
     * @param scanner Объект Scanner для чтения ввода пользователя.
     * @param user Пользователь, который хочет добавить показания.
     */
    private static void addReadings(Scanner scanner, User user) {
        try {
            System.out.print("Укажите порядковый номер месяца: ");
            Month month = Month.of(Integer.parseInt(scanner.nextLine()));

            Map<String, Double> readings = new HashMap<>();

            System.out.print("Показания отопления: ");
            double heating = Double.parseDouble(scanner.nextLine());
            readings.put("heating", heating);

            System.out.print("Показания горячей воды: ");
            double hotWater = Double.parseDouble(scanner.nextLine());
            readings.put("hotWater", hotWater);

            System.out.print("Показания холодной воды: ");
            double coldWater = Double.parseDouble(scanner.nextLine());
            readings.put("coldWater", coldWater);

            // Здесь мы сможем расширить перечень подаваемых показаний

            readingsService.addReadings(user, month, readings);
        } catch (NumberFormatException e) {
            System.out.println("Введены некорректные данные. Пожалуйста, введите числа.");
        } catch (DateTimeException e) {
            System.out.println("Введен некорректный номер месяца. Пожалуйста, введите число от 1 до 12.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Получает и выводит все показания пользователя.
     *
     * @param user Пользователь, для которого нужно получить и вывести показания.
     */
    private static void getAllReadings(User user) {
        readingsService.printAllReadings(user);
    }

    /**
     * Получает и выводит последние показания пользователя.
     * Если показания отсутствуют, выводит сообщение об этом.
     *
     * @param user Пользователь, для которого нужно получить и вывести показания.
     */
    private static void getActualReadings(User user) {
        readingsService.getLastReadings(user).ifPresent(System.out::println);
    }

    /**
     * Получает и выводит показания пользователя за указанный месяц.
     * Если показания отсутствуют, выводит сообщение об этом.
     *
     * @param scanner Объект Scanner для чтения ввода пользователя.
     * @param user Пользователь, для которого нужно получить и вывести показания.
     */
    private static void getReadingsByMonth(Scanner scanner, User user) {
        try {
            System.out.print("Напишите номер месяца, который вас интересует: ");
            Month month = Month.of(Integer.parseInt(scanner.nextLine()));
            readingsService.getReadingsByMonth(user, month).ifPresent(System.out::println);
        } catch (DateTimeException e) {
            System.out.println("Ошибка: введен некорректный номер месяца. Пожалуйста, введите число от 1 до 12.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введены некорректные данные. Пожалуйста, введите числа.");
        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
    }
}
