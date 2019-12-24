
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class CAST256 {

    private byte[] userKey = new byte[32];
    private int[] keysM = new int[48];
    private int[] keysR = new int[48];
    private int[] workingKey = new int [8];

    private int[] S1 = SBox.S1;
    private int[] S2 = SBox.S2;
    private int[] S3 = SBox.S3;
    private int[] S4 = SBox.S4;

    private int [] Tm = new int [24 * 8];
    private int [] Tr = new int [24 * 8];
    private ReaderWriter forReadwrite;
    private ArrayList<byte[]> resCiphering;

    CAST256(){}

    public void inputKey() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter size of key (in bits): ");
        int keySize = scanner.nextInt();
        boolean correct = false;
        byte [] key = null;

        if(keySize == 128){
            key = new byte[16];
            correct = true;
        }
        if(keySize == 160){
            key = new byte[20];
            correct = true;
        }
        if(keySize == 192){
            key = new byte[24];
            correct = true;
        }
        if(keySize == 224){
            key = new byte[28];
            correct = true;
        }
        if(keySize == 256){
            key = new byte[32];
            correct = true;
        }
        if(!correct){
            throw new Exception("Key size is incorrect. Key size must be 128, 160, 192, 224, 256 bits");
        }
        System.out.println("Enter your key: ");
        for(int i = 0; i < key.length; i++){
            key[i] =  scanner.nextByte();
        }
       /* [] key = { 0x23, 0x42, (byte) 0xbb, (byte) 0x9e, (byte) 0xfa, 0x38, 0x54, 0x2c, 0x0a, (byte) 0xf7, 0x56,
                0x47, (byte) 0xf2, (byte) 0x9f, 0x61, 0x5d };
        System.arraycopy(key, 0, this.userKey, 0, key.length);*/
        keysCreating();
    }

    void keyGenerate(int keySize) throws Exception {

        boolean correct = false;
        byte [] key = null;
        int maxPossibleValue = 100;
        int minPossibleValue = 0;
        if((keySize == 128)||(keySize == 160)||(keySize == 192)||(keySize == 224)||(keySize == 256)){
            correct = true;
        }
        if(!correct){
            throw new Exception("Key size is incorrect. Key size must be 128, 160, 192, 224, 256 bits");
        }
        if(keySize == 128){
            key = new byte[16];
        }
        if(keySize == 160){
            key = new byte[20];
        }
        if(keySize == 192){
            key = new byte[24];
        }
        if(keySize == 224){
            key = new byte[28];
        }
        if(keySize == 256){
            key = new byte[32];
        }
        for(int i = 0; i < key.length; i++){
            key[i] = (byte) ((Math.random()*((maxPossibleValue - minPossibleValue)+1)) +  minPossibleValue);
        }

        System.arraycopy(key, 0, this.userKey, 0, key.length);
    }

    private void keysCreating() {

        int Cm = 0x5a827999;
        int Mm = 0x6ed9eba1;
        int Cr = 19;
        int Mr = 17;

        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 8; j++) {
                Tm[i * 8 + j] = Cm;

                Cm = (Cm + Mm);
                Tr[i * 8 + j] = Cr;
                Cr = (Cr + Mr) & 0x1f;
            }
        }

        byte[] tmpKey = new byte[64];
        int length = userKey.length;
        System.arraycopy(userKey, 0, tmpKey, 0, length);

        for(int i = 0; i < 8; i++){
            workingKey[i] = bytesToBits(tmpKey, i*4);
        }

            W(workingKey);

        /*for(int i = 0; i < 48; i++){
            System.out.println(Integer.toHexString(keysR[i]&0xff) + " " + Integer.toHexString(keysM[i]));
        }*/
    }

    private void W (int [] workingKey){
        for (int i = 0; i < 12; i++)
        {

            int idx = i*2 *8;
            workingKey[6] ^= f1(workingKey[7], Tm[idx ],  Tr[idx ]);
            workingKey[5] ^= f2(workingKey[6], Tm[idx+1], Tr[idx+1]);
            workingKey[4] ^= f3(workingKey[5], Tm[idx+2], Tr[idx+2]);
            workingKey[3] ^= f1(workingKey[4], Tm[idx+3], Tr[idx+3]);
            workingKey[2] ^= f2(workingKey[3], Tm[idx+4], Tr[idx+4]);
            workingKey[1] ^= f3(workingKey[2], Tm[idx+5], Tr[idx+5]);
            workingKey[0] ^= f1(workingKey[1], Tm[idx+6], Tr[idx+6]);
            workingKey[7] ^= f2(workingKey[0], Tm[idx+7], Tr[idx+7]);


            idx = (i*2 + 1)*8;
            workingKey[6] ^= f1(workingKey[7], Tm[idx ],  Tr[idx ]);
            workingKey[5] ^= f2(workingKey[6], Tm[idx+1], Tr[idx+1]);
            workingKey[4] ^= f3(workingKey[5], Tm[idx+2], Tr[idx+2]);
            workingKey[3] ^= f1(workingKey[4], Tm[idx+3], Tr[idx+3]);
            workingKey[2] ^= f2(workingKey[3], Tm[idx+4], Tr[idx+4]);
            workingKey[1] ^= f3(workingKey[2], Tm[idx+5], Tr[idx+5]);
            workingKey[0] ^= f1(workingKey[1], Tm[idx+6], Tr[idx+6]);
            workingKey[7] ^= f2(workingKey[0], Tm[idx+7], Tr[idx+7]);

            keysR[i*4 ]    = workingKey[0] & 0x1f;
            keysR[i*4 + 1] = workingKey[2] & 0x1f;
            keysR[i*4 + 2] = workingKey[4] & 0x1f;
            keysR[i*4 + 3] = workingKey[6] & 0x1f;

            keysM[i*4 ]    = workingKey[7];
            keysM[i*4 + 1] = workingKey[5];
            keysM[i*4 + 2] = workingKey[3];
            keysM[i*4 + 3] = workingKey[1];
        }

    }



    private byte[] encryptBlock (byte[] plaintextBuffer){
        int[] result  = new int[4];
        int beginIdx = 0;
        int writeIdx = 0;
        byte[] ciphertextBuffer = new byte [plaintextBuffer.length];

        int A = bytesToBits(plaintextBuffer, beginIdx);
        int B = bytesToBits(plaintextBuffer, beginIdx + 4);
        int C = bytesToBits(plaintextBuffer, beginIdx + 8);
        int D = bytesToBits(plaintextBuffer, beginIdx + 12);

        encryption(A, B, C, D, result);

        bitsToBytes(result[0], ciphertextBuffer, writeIdx);
        bitsToBytes(result[1], ciphertextBuffer, writeIdx + 4);
        bitsToBytes(result[2], ciphertextBuffer, writeIdx + 8);
        bitsToBytes(result[3], ciphertextBuffer, writeIdx + 12);

        return(ciphertextBuffer);
    }

    private byte[] decryptBlock (byte[] cifertextBuffer){
        int[] result  = new int[4];
        int beginIdx = 0;
        int writeIdx = 0;
        byte[] deciphertextBuffer = new byte [cifertextBuffer.length];

        int A = bytesToBits(cifertextBuffer, beginIdx);
        int B = bytesToBits(cifertextBuffer, beginIdx + 4);
        int C = bytesToBits(cifertextBuffer, beginIdx + 8);
        int D = bytesToBits(cifertextBuffer, beginIdx + 12);

        decryption(A, B, C, D, result);

        bitsToBytes(result[0], deciphertextBuffer, writeIdx);
        bitsToBytes(result[1], deciphertextBuffer, writeIdx + 4);
        bitsToBytes(result[2], deciphertextBuffer, writeIdx + 8);
        bitsToBytes(result[3], deciphertextBuffer, writeIdx + 12);

        return(deciphertextBuffer);
    }

    void startEncryption(String filename) throws IOException {

        ArrayList<byte[]> plaintext;
        ArrayList<byte[]> cifertext = new ArrayList<>();

        forReadwrite = new ReaderWriter();
        plaintext = forReadwrite.read(filename);
        for(int i = 0; i < plaintext.size(); i++){
            cifertext.add(encryptBlock(plaintext.get(i)));
        }
        forReadwrite.write(cifertext, true);
        resCiphering = cifertext;
        Estimates est1 = new Estimates();
        double correlation = est1.countCorrelation(plaintext, cifertext);
        System.out.println("Correlation is " + correlation);
        double distributionOne = est1.estimationOfDistribution(plaintext);
        System.out.println("Disribution one plain is " + distributionOne);
        System.out.println("Disribution zero plain is " + (1 - distributionOne));
        distributionOne = est1.estimationOfDistribution(cifertext);
        System.out.println("Disribution one crypto is " + distributionOne);
        System.out.println("Disribution zero crypto is " + (1 - distributionOne));
    }

    void startDecryption(String filename) throws IOException {

        ArrayList<byte[]> secretext;
        ArrayList<byte[]> restext = new ArrayList<>();
        secretext = resCiphering;
        for(int i = 0; i < secretext.size(); i++){
            restext.add(decryptBlock(secretext.get(i)));
        }
        forReadwrite.write(restext, false);
    }

    private void encryption(int A, int B, int C, int D, int[] result){
        int idx;

        for(int i = 0; i < 6; i++){
            idx = i * 4;
             C ^= f1(D, keysM[idx], keysR[idx]);
             B ^= f2(C, keysM[idx + 1], keysR[idx + 1]);
             A ^= f3(B, keysM[idx + 2], keysR[idx + 2]);
             D ^= f1(A, keysM[idx + 3], keysR[idx + 3]);

        }

        for(int i = 6; i < 12; i++){
            idx = i * 4;
            D ^= f1(A, keysM[idx + 3], keysR[idx + 3]);
            A ^= f3(B, keysM[idx + 2], keysR[idx + 2]);
            B ^= f2(C, keysM[idx + 1], keysR[idx + 1]);
            C ^= f1(D, keysM[idx], keysR[idx]);

        }

        result[0] = A;
        result[1] = B;
        result[2] = C;
        result[3] = D;
    }

    private void decryption(int A, int B, int C, int D, int[] result){
        int idx;

        for(int i = 0; i < 6; i++){
            idx = (11 - i) * 4;
            C ^= f1(D, keysM[idx], keysR[idx]);
            B ^= f2(C, keysM[idx + 1], keysR[idx + 1]);
            A ^= f3(B, keysM[idx + 2], keysR[idx + 2]);
            D ^= f1(A, keysM[idx + 3], keysR[idx + 3]);

        }

        for(int i = 6; i < 12; i++){
            idx = (11 - i) * 4;
            D ^= f1(A, keysM[idx + 3], keysR[idx + 3]);
            A ^= f3(B, keysM[idx + 2], keysR[idx + 2]);
            B ^= f2(C, keysM[idx + 1], keysR[idx + 1]);
            C ^= f1(D, keysM[idx], keysR[idx]);

        }

        result[0] = A;
        result[1] = B;
        result[2] = C;
        result[3] = D;
    }
    private int f1(int I, int m, int r) {
        I = m + I;
        I = I << r | I >>> (32 - r);
        return (((S1[(I >>> 24) & 0xFF]) ^ S2[(I >>> 16) & 0xFF ]) - S3[(I >>> 8) & 0xFF ]) + S4[I & 0xFF];
    }

    private int f2(int I, int m, int r) {
        I = m ^ I;
        I = I << r | I >>> (32 - r);
        return (((S1[(I >>> 24) & 0xFF]) - S2[(I >>> 16) & 0xFF]) + S3[(I >>> 8) & 0xFF]) ^ S4[I & 0xFF];
    }

    private int f3(int I, int m, int r) {
        I = m - I;
        I = I << r | I >>> (32 - r);
        return (((S1[(I >>> 24) & 0xFF]) + S2[(I >>> 16) & 0xFF]) ^ S3[(I >>> 8) & 0xFF]) - S4[I & 0xFF];
    }

    private int bytesToBits(byte[] byteArr, int i){
        return((byteArr[i] & 0xFF) << 24)|((byteArr[i+1] & 0xFF) << 16)|((byteArr[i+2] & 0xFF) << 8)|((byteArr[i+3] & 0xFF));
    }

    private void bitsToBytes(int in, byte[] byteArr, int offset){
        byteArr[offset + 3] = (byte) in;
        byteArr[offset + 2] = (byte)(in >>> 8);
        byteArr[offset + 1] = (byte)(in >>> 16);
        byteArr[offset] = (byte)(in >>> 24);
    }

}


