package l1j.server.server.utils;

public class PrintUtil {
    private static int status = 1;
    public static void println(Object object) {
        if (status == 0) { return; }
        System.out.println(object);
    }
}
