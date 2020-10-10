import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static Charset w1251 = Charset.forName("Windows-1251");

    public static byte[] readFile(Path path) throws IOException {
        byte[] fileBytes = Files.readAllBytes(path);
        return fileBytes;
    }

    public static String toBits(byte[] bt) {
        String hiddenBits = "";
        for (byte b : bt) {
            hiddenBits = hiddenBits + String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        }
        System.out.println("\nСкрытая информация в битовом представлении: " + hiddenBits + "\n");
        return hiddenBits;
    }

    public static String addHiddenToText(List<String> lines, String hiddenBits) {
        char[] chars = hiddenBits.toCharArray();
        String res = "";
        for (int j = 0; j < lines.size(); j++) {
            if (j < chars.length) {
                if (chars[j] == '1') {
                    res = res + lines.get(j) + " " + "\n";
                    continue;
                }
            }
            res = res + lines.get(j) + "\n";
        }
        return res;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

        // C:\Projects\project_2_steganorrhaphy\files\hidden_phrase.txt
        System.out.println("\nВведите путь к файлу, в котором находится скрытая информация: ");
        Path pathHidden = Path.of(bf.readLine());
        byte[] hiddenString = readFile(pathHidden);
        String hiddenBits = toBits(hiddenString);

        // C:\Projects\project_2_steganorrhaphy\files\text.txt
        System.out.println("Введите путь к файлу, в котором находится основной текст: ");
        Path pathText = Path.of(bf.readLine());
        List<String> lines = Files.readAllLines(pathText, w1251);

        String result = addHiddenToText(lines, hiddenBits);

        // C:\Projects\project_2_steganorrhaphy\files\result.txt
        System.out.println("\nВведите путь к результирующему файлу, в котором будет находится текст со скрытой информацией: ");
        String pathResult = bf.readLine();
        File fileOutput = new File(pathResult);
        FileOutputStream outStream = new FileOutputStream(fileOutput);
        outStream.write(result.getBytes(w1251));
    }
}
