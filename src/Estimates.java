import java.util.ArrayList;

public class Estimates {

    public double countCorrelation(ArrayList<byte[]> input, ArrayList<byte[]> output) {
        byte[] inputArray = new byte[8 * input.size()];
        int counter = 0;
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < 8; j++) {
                inputArray[counter] = input.get(i)[j];
                counter++;
            }
        }

        byte[] outputArray = new byte[8 * output.size()];
        counter = 0;
        for (int i = 0; i < output.size(); i++) {
            for (int j = 0; j < 8; j++) {
                outputArray[counter] = output.get(i)[j];
                counter++;
            }
        }

        return countCorrelation(inputArray, outputArray);
    }

    public double countCorrelation(byte[] input, byte[] output) {
        if(input.length != output.length) {
            System.err.println("input.length != output.length");
        }

        int count = 0;
        int size = input.length;
        for(int i = 0; i < size; i++) {
            int inByte = input[i] & 0xff;
            int outByte = output[i] & 0xff;

            for(int j = 0; j < 8; j++) {
                int tmpIn = (inByte >>> j) & 1;
                int tmpOut = (outByte >>> j) & 1;
                count += (2*tmpIn - 1)*(2*tmpOut - 1);
            }
        }

        double N = size*8;

        return (double)count / N;
    }

    public double estimationOfDistribution( ArrayList<byte[]> output) {
        byte[] outputArray = new byte[8 * output.size()];
        int counter = 0;
        for (int i = 0; i < output.size(); i++) {
            for (int j = 0; j < 8; j++) {
                outputArray[counter] = output.get(i)[j];
                counter++;
            }
        }

        return estimationOfDistribution(outputArray);
    }

    public double estimationOfDistribution(byte[] output) {
        int count = 0;
        int size = output.length;
        for(int i = 0; i < size; i++) {
            int outByte = output[i] & 0xff;

            for(int j = 0; j < 8; j++) {
                int tmpOut = (outByte >>> j) & 1;

                count += tmpOut;
            }
        }
        double N = size*8;

        return (double)count / N;
    }
}