package com.alibaba.ttl.threadpool.agent;


import com.alibaba.ttl.threadpool.agent.transformlet.TtlExecutorTransformlet;
import com.alibaba.ttl.threadpool.agent.transformlet.TtlForkJoinTransformlet;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.ttl.threadpool.agent.internal.logging.Logger;


/**
 * TTL Java Agent.
 *
 * @author Jerry Lee (oldratlee at gmail dot com)
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/instrument/package-summary.html">The mechanism for instrumentation</a>
 * @since 0.9.0
 */
public final class TtlAgent {

    private TtlAgent() {
        throw new InstantiationError("Must not instantiate this class");
    }

    public static void premain(String agentArgs, Instrumentation inst) throws Exception {
        Logger.setLoggerImplType(getLogImplTypeFromAgentArgs(agentArgs));

        final Logger logger = Logger.getLogger(TtlAgent.class);

        logger.info("[TtlAgent.premain] begin, agentArgs: " + agentArgs + ", Instrumentation: " + inst);

        @SuppressWarnings("unchecked")
        ClassFileTransformer transformer = new TtlTransformer(TtlExecutorTransformlet.class, TtlForkJoinTransformlet.class);
        inst.addTransformer(transformer, true);
        logger.info("[TtlAgent.premain] addTransformer " + transformer.getClass() + " success");

        logger.info("[TtlAgent.premain] end");

    }

    private static String getLogImplTypeFromAgentArgs(String agentArgs) {
        final Map<String, String> kv = splitCommaColonStringToKV(agentArgs);
        // FIXME DELETE
        System.out.println(agentArgs + "-->  " + kv);
        return kv.get("ttl.logger");
    }

    static Map<String, String> splitCommaColonStringToKV(String commaColonString) {
        Map<String, String> ret = new HashMap<String, String>();
        if (commaColonString == null || commaColonString.trim().length() == 0) return ret;

        final String[] splitKvArray = commaColonString.trim().split("\\s*,\\s*");
        for (String kvString : splitKvArray) {
            final String[] kv = kvString.trim().split("\\s*:\\s*");
            if (kv.length == 0) continue;

            if (kv.length == 1) ret.put(kv[0], "");
            else {
                ret.put(kv[0], kv[1]);
            }
        }

        return ret;
    }
}
