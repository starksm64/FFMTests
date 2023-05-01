package clibrary;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_INT;

public class Printf {
    public static void main(String[] args) throws Throwable {
        Linker linker = Linker.nativeLinker();
        Optional<MemorySegment> optPrintfMS = linker.defaultLookup().find("printf");
        MemorySegment printfMS = optPrintfMS.orElseThrow();
        MethodHandle printf = linker.downcallHandle(printfMS, FunctionDescriptor.of(JAVA_INT, ADDRESS, JAVA_INT));

        int version = Integer.getInteger("java.specification.version");
        System.out.printf("Java version: %d\n", version);
        try(Arena offHeap = Arena.openConfined()) {
            MemorySegment formatStr = offHeap.allocateUtf8String("Java FFM call to printf %d\n");
            int rtn = (int) printf.invoke(formatStr, version);
            System.out.printf("cprintf rtn: %d\n", rtn);
        }
    }
}
