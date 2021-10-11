package core;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MethodReferenceTest {
    interface ThreeParam1 {
        LocalDateTime apply(int one, int two, int three);
    }

    interface ThreeParam2 {
        LocalDateTime apply(LocalDate one, int two, int three);
    }

    interface ThreeParam3 {
        LocalDate apply(int one, int two, int three);
    }

    /**
     * object::instanceMethod method reference will invoke the referenced method on
     * the said object with compatible arguments
     */
    @Nested
    class Object_InstanceMethod {
        private String noParam(Supplier<String> supplier) {
            return supplier.get();
        }
        private String oneParam(Function<String, String> function) {
            return function.apply("1");
        }
        private String twoParam(BiFunction<Integer, Integer, String> function) {
            return function.apply(1, 2);
        }
        private LocalDateTime threeParam(ThreeParam1 function) {
            return function.apply(1, 2, 3);
        }

        @Test
        void invoke_the_method_on_the_owning_object() {
            var aList = List.of("a", "b", "c");
            assertEquals(List.of("hello a", "hello b", "hello c"), aList.stream().map("hello "::concat).collect(Collectors.toList()));
        }

        @Test
        void invoke_with_compatible_arguments() {
            // like a Supplier
            assertEquals("a", noParam("a "::trim));
            // like a Function or Consumer
            assertEquals("a1", oneParam("a"::concat));
            // like a BiFunction or BiConsumer
            assertEquals("1", twoParam("0123"::substring));
            // or any compatible Functional Interface
            assertEquals(
                LocalDateTime.of(2021, 10, 11, 1, 2, 3),
                threeParam(LocalDate.of(2021, 10, 11)::atTime));
        }
    }

    /**
     * Class::instanceMethod method reference will invoke the referenced method
     * on an instance of the Class
     */
    @Nested
    class Class_InstanceMethod {
        private String noParam(Supplier<String> supplier) {
            return supplier.get();
        }
        private String oneParam(Function<String, String> function) {
            return function.apply("1   ");
        }
        private String twoParam(BiFunction<String, String, String> function) {
            return function.apply("1", "2");
        }
        private LocalDateTime threeParam(ThreeParam2 function) {
            return function.apply(LocalDate.of(2021, 10, 11), 2, 3);
        }

        @Test
        void invoke_the_method_on_the_class_instance() {
            var aList = List.of("a", "b", "c");
            assertEquals(
                aList.stream().map(a -> a.toUpperCase()).collect(Collectors.toList()),
                aList.stream().map(String::toUpperCase).collect(Collectors.toList())
            );
        }

        @Test
        void invoke_with_compatible_arguments() {   // as long as the 1st param is a class's instance
            // like a Supplier                                  // Constructor is neither static nor instance
            assertEquals("", noParam(String::new));     // including here just to complete 0, 1, 2, 3 params
            // like a Function or Consumer
            assertEquals("1", oneParam(String::trim));
            // like a BiFunction or BiConsumer
            assertEquals("12", twoParam(String::concat));
            // or any compatible Functional Interface
            assertEquals(
                LocalDateTime.of(2021, 10, 11, 2, 3),
                threeParam(LocalDate::atTime));
        }
    }

    /**
     * Class::staticMethod method reference will invoke the referenced static method on
     * the declared class with compatible arguments
     */
    @Nested
    class Class_StaticMethod {
        private String noParam(Supplier<String> supplier) {
            return supplier.get();
        }
        private String oneParam(Function<String, String> function) {
            return function.apply("1");
        }
        private String twoParam(BiFunction<String, String, String> function) {
            return function.apply("%s", "1");
        }
        private LocalDate threeParam(ThreeParam3 function) {
            return function.apply(1, 2, 3);
        }

        @Test
        void invoke_the_static_method_on_the_class() {
            var aList = List.of(1, 2.0, true);
            assertEquals(
                List.of("1", "2.0", "true"),
                aList.stream().map(String::valueOf).collect(Collectors.toList())
            );
        }

        @Test
        void invoke_with_compatible_arguments() {
            // like a Supplier
            assertEquals(System.lineSeparator(), noParam(System::lineSeparator));
            // like a Function or Consumer
            assertEquals("1", oneParam(String::valueOf));
            // like a BiFunction or BiConsumer
            assertEquals("1", twoParam(String::format));
            // or any compatible Functional Interface
            assertEquals(
                LocalDate.of(1, 2, 3),
                threeParam(LocalDate::of));
        }
    }
}
