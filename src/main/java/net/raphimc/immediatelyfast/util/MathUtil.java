package net.raphimc.immediatelyfast.util;

public final class MathUtil {

    private static final int DATA_BASE_UNIT = 1024;
    private static final String[] DATA_UNITS = new String[]{"KiB", "MiB", "GiB", "TiB", "PiB", "EiB"};

    private MathUtil() {
    }

    public static String formatBytes(long bytes) {
        final boolean negative = bytes < 0;
        bytes = Math.abs(bytes);
        if (bytes < DATA_BASE_UNIT) {
            return bytes + " B";
        }

        final int exponent = (int) (Math.log(bytes) / Math.log(DATA_BASE_UNIT));
        return (negative ? "-" : "") + String.format("%.1f ", bytes / Math.pow(DATA_BASE_UNIT, exponent)) + DATA_UNITS[exponent - 1];
    }

}

