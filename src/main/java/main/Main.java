package main;

import config.AppConfig;
import javax.swing.SwingUtilities;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import view.LoginForm;

public class Main {

    private static ApplicationContext context;

    public static void main(String[] args) {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (context instanceof ConfigurableApplicationContext) {
                ((ConfigurableApplicationContext) context).close();
            }
        }));

        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
