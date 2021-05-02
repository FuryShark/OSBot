package utils;

public class NumberFormat {

	public static String timeFormatHMS(long time) {
		StringBuilder t = new StringBuilder();
		long total_secs = time / 1000L;
		long total_mins = total_secs / 60L;
		long total_hrs = total_mins / 60L;
		int secs = (int) total_secs % 60;
		int mins = (int) total_mins % 60;
		int hrs = (int) total_hrs % 24;
		if (hrs < 10) {
			t.append("0");
		}
		t.append(hrs).append(":");
		if (mins < 10) {
			t.append("0");
		}
		t.append(mins).append(":");
		if (secs < 10) {
			t.append("0");
		}
		t.append(secs);
		return t.toString();
	}

	public static String timeFormatDHMS(long time) {
		int sec = (int) (time / 1000), d = sec / 86400, h = sec / 3600 % 24, m = sec / 60 % 60, s = sec % 60;
		return (d < 10 ? "0" + d : d) + ":" + (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":"
				+ (s < 10 ? "0" + s : s);
	}

	public static String runescapeFormat(Long number) {
		String[] suffix = new String[] { "K", "M", "B", "T" };
		long size = (number.longValue() != 0) ? (long) Math.log10(number) : 0;
		if (size >= 3) {
			while (size % 3 != 0) {
				size = size - 1;
			}
		}
		return (size >= 3) ? +(Math.round((number / Math.pow(10, size)) * 10) / 10d) + suffix[(int) ((size / 3) - 1)]
				: +number + "";
	}
}
