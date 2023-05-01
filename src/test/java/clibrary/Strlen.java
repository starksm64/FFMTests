package clibrary;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_LONG;

public class Strlen {
    public static void main(String[] args) throws Throwable {
        Linker linker = Linker.nativeLinker();
        MethodHandle strlen = linker.downcallHandle(
                linker.defaultLookup().find("strlen").get(),
                FunctionDescriptor.of(JAVA_LONG, ADDRESS)
        );

        String testString = "java.specification.version";
        int len = testString.length();
        System.out.printf("Java.length: %d\n", len);
        try(Arena offHeap = Arena.openConfined()) {
            MemorySegment javaStr = offHeap.allocateUtf8String(testString);
            long clen = (long) strlen.invoke(javaStr);
            System.out.printf("C.length: %d\n", clen);
        }
    }
}
