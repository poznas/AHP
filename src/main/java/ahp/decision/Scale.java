package ahp.decision;

public class Scale {

    private static final int scale = 1;
    private static final double[] satty = new double[]{
            1,2,3,4,5,6,7,8,9
    };
    private static final double[] balanced = new double[]{
            1,1.22,1.5,1.86,2.33,3,4,5.67,9
    };
    public static double[] values;
    static {
        if ( scale == 1 ){
            values = satty;
        }else{
            values = balanced;
        }
    }
}
