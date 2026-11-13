package main;

import config.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import javax.swing.SwingUtilities;
import view.LoginForm;

public class Main {
    
    private static ApplicationContext context;

    public static void main(String[] args) {
        context = new AnnotationConfigApplicationContext(AppConfig.class);

        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }

    public static ApplicationContext getContext() {
        return context;
    }
}