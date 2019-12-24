import java.io.*;
import java.util.ArrayList;

public class ReaderWriter {
    private ArrayList<byte[]> plaintext = new ArrayList<>();
    private int countOfBlock;
    private int additionBits;
    private byte[] buffer;
    private boolean isImg;

    ReaderWriter(){
        countOfBlock = 0;
        additionBits = 0;
        isImg = false;
    }


    public ArrayList<byte[]> read (String filename) throws IOException {

        if(filename.contains(".jpg")){
            isImg = true;
        }
        FileInputStream fin = new FileInputStream(filename);
        buffer = new byte[fin.available()];
        fin.read(buffer, 0, fin.available());
        fin.close();

        int blockSize = 16;
        countOfBlock = buffer.length / blockSize;
        additionBits = buffer.length % blockSize;

        byte[] tmp = new byte[16];
        for(int i = 0; i < countOfBlock; i++){
            tmp = new byte[16];
            for(int j = 0; j < blockSize; j++){
                tmp[j] = buffer[j + i * blockSize];
            }
            plaintext.add(i, tmp);
        }

        if (buffer.length % blockSize != 0){
            tmp = new byte[16];
            for(int i = blockSize * countOfBlock; i < buffer.length; i++) {
                tmp[i % blockSize] = buffer[i];
            }

            for(int i = buffer.length % blockSize; i < tmp.length; i++){
                tmp[i] = 0;
            }
        }
        plaintext.add(tmp);
        return plaintext;
    }

    public void write(ArrayList<byte[]> writeData, boolean param) throws IOException {

        String filename = "";
        int blockSize = 16;
        if (isImg) {
            if(param){
                filename = "Encrypted.jpg";
            }
            if(!param){
                filename = "Decrypted.jpg";
            }
            File file = new File(filename);
            byte[] img;
            if(param) {
                img = new byte[blockSize * writeData.size() - additionBits + 20];
            }
            else
            {
                img = new byte[blockSize * writeData.size() - additionBits + 10];
            }

            int counter = 0;
            for(int i = 0; i < writeData.size(); i++){
                for(int j = 0; j < blockSize; j++){
                    if(counter == blockSize*writeData.size() - additionBits){
                        break;
                    }
                    img[counter] = writeData.get(i)[j];
                    counter++;
                }
            }

            FileOutputStream stream = new FileOutputStream(file);
            stream.write(img);
            stream.close();

        }
        else {
            if (param) {
                filename = "Encrypted.txt";

            }
            if (!param) {
                filename = "Decrypted.txt";
            }
            File file = new File(filename);

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            for (int i = 0; i < writeData.size(); i++) {
                for (int j = 0; j < writeData.get(i).length; j++) {
                    if (i == writeData.size() - 1 & j == additionBits) {
                        break;
                    }
                    out.print((char) (writeData.get(i)[j] & 0xFF));
                }
            }

            out.close();
        }
    }

}



