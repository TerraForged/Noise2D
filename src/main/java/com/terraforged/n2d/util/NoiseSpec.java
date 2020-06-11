package com.terraforged.n2d.util;

import com.terraforged.cereal.spec.Context;
import com.terraforged.cereal.spec.DataSpec;
import com.terraforged.cereal.spec.DataSpecs;
import com.terraforged.cereal.value.DataObject;
import com.terraforged.cereal.value.DataValue;
import com.terraforged.n2d.combiner.Add;
import com.terraforged.n2d.combiner.Max;
import com.terraforged.n2d.combiner.Min;
import com.terraforged.n2d.combiner.Multiply;
import com.terraforged.n2d.combiner.Sub;
import com.terraforged.n2d.domain.AddWarp;
import com.terraforged.n2d.domain.CacheWarp;
import com.terraforged.n2d.domain.CompoundWarp;
import com.terraforged.n2d.domain.CumulativeWarp;
import com.terraforged.n2d.domain.DirectionWarp;
import com.terraforged.n2d.domain.DomainWarp;
import com.terraforged.n2d.func.Interpolation;
import com.terraforged.n2d.func.MidPointCurve;
import com.terraforged.n2d.func.SCurve;
import com.terraforged.n2d.modifier.Abs;
import com.terraforged.n2d.modifier.AdvancedTerrace;
import com.terraforged.n2d.modifier.Alpha;
import com.terraforged.n2d.modifier.Bias;
import com.terraforged.n2d.modifier.Boost;
import com.terraforged.n2d.modifier.Cache;
import com.terraforged.n2d.modifier.Clamp;
import com.terraforged.n2d.modifier.Curve;
import com.terraforged.n2d.modifier.Freq;
import com.terraforged.n2d.modifier.Grad;
import com.terraforged.n2d.modifier.Invert;
import com.terraforged.n2d.modifier.Map;
import com.terraforged.n2d.modifier.Modulate;
import com.terraforged.n2d.modifier.Power;
import com.terraforged.n2d.modifier.PowerCurve;
import com.terraforged.n2d.modifier.Scale;
import com.terraforged.n2d.modifier.Steps;
import com.terraforged.n2d.modifier.Terrace;
import com.terraforged.n2d.modifier.Threshold;
import com.terraforged.n2d.modifier.VariableCurve;
import com.terraforged.n2d.modifier.Warp;
import com.terraforged.n2d.selector.Base;
import com.terraforged.n2d.selector.Blend;
import com.terraforged.n2d.selector.MultiBlend;
import com.terraforged.n2d.selector.Select;
import com.terraforged.n2d.selector.VariableBlend;
import com.terraforged.n2d.source.Constant;
import com.terraforged.n2d.source.FastBillow;
import com.terraforged.n2d.source.FastCell;
import com.terraforged.n2d.source.FastCellEdge;
import com.terraforged.n2d.source.FastCubic;
import com.terraforged.n2d.source.FastPerlin;
import com.terraforged.n2d.source.FastRidge;
import com.terraforged.n2d.source.FastSimplex;
import com.terraforged.n2d.source.FastSin;
import com.terraforged.n2d.source.Line;
import com.terraforged.n2d.source.Rand;

public class NoiseSpec {

    static {
        // sources
        DataSpecs.register(Constant.spec());
        DataSpecs.register(FastBillow.spec());
        DataSpecs.register(FastCell.spec());
        DataSpecs.register(FastCellEdge.spec());
        DataSpecs.register(FastCubic.spec());
        DataSpecs.register(FastPerlin.spec());
        DataSpecs.register(FastRidge.spec());
        DataSpecs.register(FastSimplex.spec());
        DataSpecs.register(FastSin.spec());
        DataSpecs.register(Line.spec());
        DataSpecs.register(Rand.spec());

        // combiners
        DataSpecs.register(Add.spec());
        DataSpecs.register(Max.spec());
        DataSpecs.register(Min.spec());
        DataSpecs.register(Multiply.spec());
        DataSpecs.register(Sub.spec());

        // modifiers
        DataSpecs.register(Abs.spec());
        DataSpecs.register(AdvancedTerrace.spec());
        DataSpecs.register(Alpha.spec());
        DataSpecs.register(Bias.spec());
        DataSpecs.register(Boost.spec());
        DataSpecs.register(Cache.spec());
        DataSpecs.register(Clamp.spec());
        DataSpecs.register(Curve.spec());
        DataSpecs.register(Freq.spec());
        DataSpecs.register(Grad.spec());
        DataSpecs.register(Invert.spec());
        DataSpecs.register(Map.spec());
        DataSpecs.register(Modulate.spec());
        DataSpecs.register(Power.spec());
        DataSpecs.register(PowerCurve.spec());
        DataSpecs.register(Scale.spec());
        DataSpecs.register(Steps.spec());
        DataSpecs.register(Terrace.spec());
        DataSpecs.register(Threshold.spec());
        DataSpecs.register(VariableCurve.spec());
        DataSpecs.register(Warp.spec());

        // selectors
        DataSpecs.register(Base.spec());
        DataSpecs.register(Blend.spec());
        DataSpecs.register(MultiBlend.spec());
        DataSpecs.register(Select.spec());
        DataSpecs.register(VariableBlend.spec());

        // curves
        DataSpecs.register(Interpolation.spec());
        DataSpecs.register(MidPointCurve.spec());
        DataSpecs.register(SCurve.spec());

        // warps
        DataSpecs.register(CacheWarp.spec());
        DataSpecs.register(AddWarp.spec());
        DataSpecs.register(CompoundWarp.spec());
        DataSpecs.register(CumulativeWarp.spec());
        DataSpecs.register(DirectionWarp.spec());
        DataSpecs.register(DomainWarp.spec());

        // utils
        DataSpecs.register(Vec2f.spec());
        DataSpecs.register(Vec2i.spec());
    }

    public static void init() {}

    public static int getSeed(DataObject data, DataSpec<?> spec, Context context) {
        DataValue value = context.getData().get("seed");
        if (value.isNonNull()) {
            context.getData().add("seed", value.inc(1));
            return value.asInt();
        }
        return spec.get("seed", data, DataValue::asInt);
    }
}
