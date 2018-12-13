import de.hhu.stups.btypes.BSet;
import de.hhu.stups.btypes.BInteger;
import de.hhu.stups.btypes.BBoolean;

public class RangeCardBig2 {






    private BInteger counter;
    private BSet set;
    private BInteger result;

    public RangeCardBig2() {
        counter = (BInteger) new BInteger(0);
        set = (BSet) BSet.range(new BInteger(1),new BInteger(25000));
        result = (BInteger) new BInteger(0);
    }

    public void simulate() {
        while((counter.less(new BInteger(10000))).booleanValue()) {
            result = (BInteger) set.card();
            counter = (BInteger) counter.plus(new BInteger(1));
        }
    }

    public static void main(String[] args) {
        RangeCardBig2 exec = new RangeCardBig2();
        long start = System.nanoTime();
        exec.simulate();
        long end = System.nanoTime();
        System.out.println(exec.getClass().toString() + " Execution: " + (end - start));
    }

}
