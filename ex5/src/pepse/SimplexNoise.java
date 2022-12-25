package pepse;
import java.util.Random;

/**
 * @author Stefan Gustavson
 */
public class SimplexNoise {

    SimplexNoiseOctave[] octaves;
    private final double[] frequencys;
    private final double[] amplitudes;

    /**
     * recieves a number (eg 128) and calculates what power of 2 it is (eg 2^7)
     * @param largestFeature - the number
     * @param persistence - the persistence in the noise
     * @param seed - the random seed
     */
    public SimplexNoise(int largestFeature,double persistence, int seed) {
        int numberOfOctaves=(int)Math.ceil(Math.log10(largestFeature)/Math.log10(2));
        octaves=new SimplexNoiseOctave[numberOfOctaves];
        frequencys=new double[numberOfOctaves];
        amplitudes=new double[numberOfOctaves];
        Random rnd=new Random(seed);
        for(int i=0;i<numberOfOctaves;i++){
            octaves[i]=new SimplexNoiseOctave(rnd.nextInt());
            frequencys[i] = Math.pow(2,i);
            amplitudes[i] = Math.pow(persistence,octaves.length-i);
        }
    }


    /**
     * calculates the noise
     * @param x - the param to calc the noise at
     * @return - the noise
     */
    public double getNoise(int x) {
        double result=0;
        for(int i=0;i<octaves.length;i++){
            //double frequency = Math.pow(2,i);
            //double amplitude = Math.pow(persistence,octaves.length-i);
            result=result+octaves[i].noise(x/frequencys[i])* amplitudes[i];
        }
        return result;
    }
}