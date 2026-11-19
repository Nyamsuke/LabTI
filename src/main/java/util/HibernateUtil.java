package util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Helper untuk konfigurasi Spring versi XML (applicationContext.xml).
 * Jalur yang dipakai Main saat ini adalah AppConfig berbasis annotation,
 * sehingga kelas ini disediakan sebagai alternatif/pendukung saja.
 */
public final class HibernateUtil {

    private static final ClassPathXmlApplicationContext CONTEXT
            = new ClassPathXmlApplicationContext("applicationContext.xml");

    private HibernateUtil() {
    }

    public static ApplicationContext getContext() {
        return CONTEXT;
    }

    public static <T> T getBean(Class<T> type) {
        return CONTEXT.getBean(type);
    }

    public static <T> T getBean(String name, Class<T> type) {
        return CONTEXT.getBean(name, type);
    }

    public static void shutdown() {
        CONTEXT.close();
    }
}
