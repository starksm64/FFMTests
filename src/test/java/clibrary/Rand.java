package clibrary;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_INT;

public class Rand {
    public static void main(String[] args) throws Throwable {
        Linker linker = Linker.nativeLinker();
        Optional<MemorySegment> optrandMS = linker.defaultLookup().find("rand_r");
        MemorySegment randMS = optrandMS.orElseThrow();
        MethodHandle rand = linker.downcallHandle(randMS, FunctionDescriptor.of(JAVA_INT, ADDRESS));

        int seed = 123456789;
        try(Arena offHeap = Arena.openConfined()) {
            MemorySegment seedMS = offHeap.allocate(JAVA_INT, seed);
            int rtn = (int) rand.invoke(seedMS);
            System.out.printf("rand rtn: %d\n", rtn);
        }
    }
}
