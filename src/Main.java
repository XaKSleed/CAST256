import java.util.Scanner;

public class Main {

    public static void main(String[] args) {


        String filename = "C:\\Text.txt";
        String filename2 = "C:\\Users\\Эльдо\\IdeaProjects\\CAST256\\Encrypted.txt";
        String filename3 = "pic_crypt.jpg";
        try {
             CAST256 key1 = new CAST256();
             key1.inputKey();
             key1.startEncryption(filename3);
             key1.startDecryption(filename2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
