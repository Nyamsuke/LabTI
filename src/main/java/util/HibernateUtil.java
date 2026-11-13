
package util;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class HibernateUtil {

    private static final ClassPathXmlApplicationContext CONTEXT =
            new ClassPathXmlApplicationContext("applicationContext.xml");

    private HibernateUtil() {
    }

    public static <T> T getBean(String name, Class<T> type) {
        return CONTEXT.getBean(name, type);
    }

    public static void shutdown() {
        CONTEXT.close();
    }
}
