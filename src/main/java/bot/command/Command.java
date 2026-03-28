package bot.command;

import domain.User;

import java.util.List;

public interface Command{

    List<String> execute(User user, String... args);
}
