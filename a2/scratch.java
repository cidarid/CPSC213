public class scratch {
    public static void main(String[] args) {
        int i = 0xDDABCABC;
        System.out.printf("%x", i <<< 8);
    }

    public static String convert(int i) {
        return String.format("%16s", Integer.toBinaryString(i & 0xff).replace(" ", "0"));
    }
}
