package util;

import com.terraforged.cereal.Cereal;
import com.terraforged.cereal.spec.Context;
import com.terraforged.cereal.value.DataValue;
import com.terraforged.noise.Module;
import com.terraforged.noise.Source;
import org.junit.Assert;

public class SpecTester {
    public static void test(Module input) {
        DataValue inputData = encode(input);
        Module output = decode(inputData);
        DataValue outputData = encode(output);

        if (input.equals(output)) {
            return;
        }

        print(inputData, outputData);

        Assert.assertEquals(input, output);
    }

    protected static Module n1() {
        return Source.perlin(1234, 50, 3);
    }

    protected static Module n2() {
        return Source.simplex(5678, 100, 5);
    }

    protected static Module n3() {
        return Source.billow(91011, 200, 2);
    }

    protected static DataValue encode(Module module) {
        return Cereal.serialize(module);
    }

    protected static Module decode(DataValue value) {
        return Cereal.deserialize(value.asObj(), Module.class, Context.NONE);
    }

    protected static void print(Object a, Object b) {
        System.err.println("\nFAIL:");
        System.err.println(" " + a);
        System.err.println(" " + b);
    }

    public static class Result {
        private final Module module;
        private final DataValue data;

        public Result(Module module) {
            this(module, encode(module));
        }

        public Result(Module module, DataValue data) {
            this.module = module;
            this.data = data;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Result result = (Result) o;

            return module.equals(result.module);
        }

        @Override
        public int hashCode() {
            return module.hashCode();
        }

        @Override
        public String toString() {
            return "Result{" +
                    "data=" + data +
                    '}';
        }
    }
}
