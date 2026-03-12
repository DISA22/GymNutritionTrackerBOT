package bot;

public class CommandNotFoundException extends RuntimeException {
    public CommandNotFoundException() {
        super("Это не команда или такой команды не существует. Введите /start , чтобы получить информацию");
    }
}
