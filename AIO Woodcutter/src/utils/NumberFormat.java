package utils;

public class NumberFormat {
	
	public static String timeFormatDHMS(long time) {
		int sec = (int) (time / 1000), d = sec / 86400, h = sec / 3600 % 24, m = sec / 60 % 60, s = sec % 60;
		return (d < 10 ? "0" + d : d) + ":" + (h < 10 ? "0" + h : h) + ":"
				+ (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
	}
	
	
	public static String runescapeFormat(Integer number) {
		String[] suffix = new String[] { "K", "M", "B", "T" };
		int size = (number.intValue() != 0) ? (int) Math.log10(number) : 0;
		if (size >= 3) {
			while (size % 3 != 0) {
				size = size - 1;
			}
		}
		return (size >= 3) ? +(Math.round((number / Math.pow(10, size)) * 10) / 10d) + suffix[(size / 3) - 1]
				: +number + "";
	}
	
}