package me.dags.noise;

/**
 * @author dags <dags@dags.me>
 *
 * Source modules are expected to return values between -1.0 and 1.0
 */
public interface Source extends Module {

    default float minValue() {
        return -1F;
    }

    default float maxValue() {
        return 1F;
    }

    Builder toBuilder();
}
