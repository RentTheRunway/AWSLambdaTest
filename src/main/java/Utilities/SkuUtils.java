package Utilities;

public class SkuUtils {
    public static String getStyle(String sku) {
        if (sku == null) {
            return null;
        } else if (sku.isEmpty()) {
            return "";
        }
        return sku.split("_")[0];
    }

    public static String getSize(String sku) {
        if (sku == null) {
            return null;
        } else if (sku.isEmpty()) {
            return "";
        }
        return sku.split("_")[1];
    }
}

