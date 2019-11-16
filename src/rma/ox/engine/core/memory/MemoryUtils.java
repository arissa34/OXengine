package rma.ox.engine.core.memory;

public class MemoryUtils {

    public static final String TAG = MemoryUtils.class.getSimpleName();

    public enum OctetUnit{
        O(1), KO(1000), MO(1000000), GO(1000000000);

        public int unit;
        OctetUnit(int unit) {
            this.unit = unit;
        }

        public long convert(long bytes){
            return bytes / unit;
        }
    }

    /**
     *
     *  SYSTEM MEMORY
     *
     *  picture : https://i.stack.imgur.com/GjuwM.png
     *
     *
     *                          Runtime.maxMemory()
     *  <-------------------------------------------------------------->
     *           Runtime.totalMemory()
     *  <-------------------------------------->
     *                       Runtime.freeMemory()
     *                      <------------------->
     *  ----------------------------------------------------------------
     *  |   Used memory     |   Free memory     |   Unlocated memory   |
     *  |                   |                   |                      |
     *  ----------------------------------------------------------------
     **/

    // Total allocated memory, is the total allocated space reserved for the java process
    // Caution : this is not the total memory, the JVM may grow the heap for new allocations.
    public static long getAllocatedTotal() { return getAllocatedTotal(null, false);}
    public static long getAllocatedTotal(OctetUnit unit, boolean log) {
        long totalMemAllocated = Runtime.getRuntime().totalMemory();
        if (log && unit != null){
            System.out.println("==> Total memory allocated : "+ unit.convert(totalMemAllocated) + " "+unit.name().toLowerCase());
        }
        return totalMemAllocated;
    }

    // Current allocated free memory
    // Caution this is not the total free available memory:
    public static long getAllocatedFreeMemory() { return getAllocatedFreeMemory(null, false);}
    public static long getAllocatedFreeMemory(OctetUnit unit, boolean log) {
        long freeMem = Runtime.getRuntime().freeMemory();
        if (log && unit != null){
            System.out.println("==> Allocated free memory available : "+ unit.convert(freeMem) + " "+unit.name().toLowerCase());
        }
        return freeMem;
    }

    // Total designated memory
    // Caution : You can never allocate more memory than this, unless you use native code.
    public static long getMaxMenory() { return getMaxMenory(null, false);}
    public static long getMaxMenory(OctetUnit unit, boolean log){
        long maxMem = Runtime.getRuntime().maxMemory();
        if (log && unit != null){
            System.out.println("==> Total designated memory : "+ unit.convert(maxMem) + " "+unit.name().toLowerCase());
        }
        return maxMem;
    }

    // Unallocated memory : amount of space the process' heap can grow.
    public static long getUnlocatedMenory() { return getUnlocatedMenory(null, false);}
    public static long getUnlocatedMenory(OctetUnit unit, boolean log) {
        long memUnallocated = getMaxMenory() - getAllocatedTotal();
        if(log && unit != null){
            System.out.println("==> Unallocated memory : "+ unit.convert(memUnallocated) + " "+unit.name().toLowerCase());
        }
        return memUnallocated;
    }

    // Total free memory
    public static long getTotalFreeMemory(){ return getTotalFreeMemory(null, false);};
    public static long getTotalFreeMemory(OctetUnit unit, boolean log){
        long totalFreeMem = getAllocatedFreeMemory() + getUnlocatedMenory();
        if (log && unit != null){
            System.out.println("==> Total free memory available : "+ unit.convert(totalFreeMem ) + " "+unit.name().toLowerCase());
        }
        return totalFreeMem;
    }

    // Used memory
    public static long getUsedMenory() { return getUsedMenory(null, false);}
    public static long getUsedMenory(OctetUnit unit, boolean log){
        long usedMem = getAllocatedTotal() - getAllocatedFreeMemory();
        if(log && unit != null){
            System.out.println("==> Used memory : "+ unit.convert(usedMem ) + " "+unit.name().toLowerCase());
        }
        return Runtime.getRuntime().maxMemory();
    }

    public static void getCurrentMemoryStats(OctetUnit unit) {
        System.out.println("========> Memory Stat <========");
        getMaxMenory(unit, true);
        getAllocatedTotal(unit, true);
        getUsedMenory(unit, true);
        getTotalFreeMemory(unit, true);
        getAllocatedFreeMemory(unit, true);
        getUnlocatedMenory(unit, true);
        System.out.println("==============================");
    }
}
