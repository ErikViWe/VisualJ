package edu.kit.ipd.sdq.visualj.efficiency.measure;

import java.io.InvalidObjectException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

import org.apache.commons.lang3.ObjectUtils;

import edu.kit.ipd.sdq.visualj.util.Misc;
import edu.kit.ipd.sdq.visualj.util.ReflectionUtils;

/**
 * A test run encapsulates an invocation of a given method with given
 * parameters.
 * 
 * <p>
 * An object of this class is serializable if and only if every argument passed
 * to the tested method, as well as the object the method is invoked on, are
 * serializable. If this is not the case,
 * {@link ObjectOutputStream#writeObject(Object)} will throw a
 * {@link NotSerializableException}.
 * </p>
 * 
 * @see EfficiencyTest
 */
public class TestRun implements Serializable {
    
    private static final long serialVersionUID = 4491775315034224521L;
    
    private Object object;
    
    private Object[] args;
    
    private Method method;
    
    private int accuracy;
    
    /**
     * Creates a new (immutable!) {@code TestRun} object with a default
     * {@code accuracy} of {@code 5}.
     * 
     * The {@code TestRun} may be run arbitrarily often by calling the
     * {@link #run()} method.
     * 
     * @param object
     *            the object on which to run the method; can be {@code null} if
     *            the method is static, or if the object on which to invoke the
     *            method should be default-constructed before the invocation.
     * @param method
     *            the method to run.
     * @param args
     *            the arguments to pass to the methods, may be {@code null} if
     *            the method takes no arguments.
     *
     * @throws NullPointerException
     *             if {@code method} is {@code null}
     */
    public TestRun(Object object, Method method, Object[] args) {
        this(object, method, args, 5);
    }
    
    /**
     * Creates a new (immutable!) {@code TestRun} object.
     * 
     * The {@code TestRun} may be run arbitrarily often by calling the
     * {@link #run()} method.
     * 
     * @param object
     *            the object on which to run the method; can be {@code null} if
     *            the method is static, or if the object on which to invoke the
     *            method should be default-constructed before each invocation.
     * @param method
     *            the method to run.
     * @param args
     *            the arguments to pass to the methods, may be {@code null} if
     *            the method takes no arguments.
     * @param accuracy
     *            how many times to invoke {@code method} when {@link #run()} is
     *            called.
     *
     * @throws NullPointerException
     *             if {@code method} is {@code null}
     */
    public TestRun(Object object, Method method, Object[] args, int accuracy) {
        if (method == null) {
            throw new NullPointerException("method");
        }
        
        this.object = object;
        this.method = method;
        this.args = args;
        this.accuracy = accuracy;
    }
    
    /**
     * 
     * @return the method to tun.
     */
    public Method getMethod() {
        return method;
    }
    
    /**
     * 
     * @return how many times to invoke {@code method} when {@link #run()} is
     *         called.
     */
    public int getAccuracy() {
        return accuracy;
    }
    
    /**
     * Sets the accuracy, i.e. how many times to invoke {@code method} when
     * {@link #run()} is called.
     * 
     * @param accuracy
     *            the new accuracy.
     */
    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }
    
    /**
     * Runs the method {@link #getAccuracy()} times.
     * 
     * @return a {@link Result} object representing the results of the time
     *         measurements.
     * 
     * @throws NoDefaultConstructorException
     *             if {@code object == null}, {@code method} is not static, and
     *             {@code object}'s class has no public default constructor.
     * @throws InvocationTargetException
     *             if the default constructor or {@code method} throw an
     *             exception.
     * @throws IllegalArgumentException
     *             if {@code args} does not fit {@code method}.
     * @throws IllegalAccessException
     *             if {@code method} is not public.
     */
    public TestRun.Result run() throws NoDefaultConstructorException, InvocationTargetException, IllegalAccessException,
            IllegalArgumentException {
        long[] runtimes = new long[accuracy];
        long[] steps = new long[accuracy];
        long[] memoryFootprints = new long[accuracy];
        
        final boolean isMethodStatic = ReflectionUtils.isStatic(method);
        final boolean defaultConstruct = !isMethodStatic && object == null;
        
        for (int i = 0; i < accuracy; ++i) {
            Object o = null;
            
            if (!isMethodStatic) {
                if (defaultConstruct) {
                    try {
                        Constructor<?> defaultConstr = method.getDeclaringClass().getDeclaredConstructor();
                        o = defaultConstr.newInstance();
                    } catch (NoSuchMethodException | InstantiationException | IllegalArgumentException e) {
                        throw new NoDefaultConstructorException(
                                "" + method.getDeclaringClass() + " has no default constructor.", e);
                    } catch (SecurityException | IllegalAccessException e) {
                        throw new NoDefaultConstructorException(
                                "" + method.getDeclaringClass() + " has a default constructor, but it is not public.",
                                e);
                    }
                } else {
                    o = ObjectUtils.cloneIfPossible(object);
                }
            }
            
            Object[] clonedArgs = Misc.cloneArray(args);
            
            long startMemory;
            long endMemory;
            
            long startTime;
            long endTime;
            
            StepCounter.reset();
            
            System.runFinalization();
            System.gc();
            
            startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            startTime = System.nanoTime();
            method.invoke(o, clonedArgs);
            endTime = System.nanoTime();
            endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            
            runtimes[i] = endTime - startTime;
            steps[i] = StepCounter.getSteps();
            memoryFootprints[i] = endMemory - startMemory;
        }
        
        return new Result(method.toString(), args, runtimes, steps, memoryFootprints);
    }
    
    @Override
    public String toString() {
        StringJoiner argsStr = new StringJoiner(", ");
        
        for (Object arg : args) {
            argsStr.add("" + arg);
        }
        
        return "object:\r\n\t" + object + "\nmethod: " + method + "\r\naccuracy: " + accuracy + "\r\nargs:" + argsStr;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + accuracy;
        result = prime * result + Arrays.hashCode(args);
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        result = prime * result + ((object == null) ? 0 : object.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TestRun) {
            TestRun tr = (TestRun) obj;
            
            return Objects.equals(object, tr.object) && Arrays.deepEquals(args, tr.args) && method.equals(tr.method)
                    && accuracy == tr.accuracy;
        }
        
        return false;
    }
    
    /**
     * An object of type {@code TestRun.Result} encapsulates the results of all
     * measurements during one call to {@link TestRun#run()}.
     * 
     * <p>
     * An object of this class is serializable if and only if every argument
     * passed to the tested method is serializable. If this is not the case,
     * {@link ObjectOutputStream#writeObject(Object)} will throw a
     * {@link NotSerializableException}.
     * </p>
     */
    public static class Result implements Serializable {
        
        private static final long serialVersionUID = -2841952060677258346L;
        
        private String method;
        
        private Object[] args;
        
        /**
         * Runtimes in nanoseconds.
         */
        private long[] runtimes;
        
        /**
         * Runtimes in steps.
         * 
         * @see StepCounter
         */
        private long[] steps;
        
        /**
         * Memory footprints in bytes.
         */
        private long[] memoryFootprints;
        
        /**
         * Creates a new {@code TestRun.Result}.
         * 
         * @param method
         *            the string representation of the method run.
         * @param args
         *            the arguments passed to the method.
         * @param runtimes
         *            the runtimes (in nanoseconds) of each invocation.
         * @param steps
         *            the runtimes (in calls to {@link StepCounter#step()}) of
         *            each invocation.
         * @param memoryFootprints
         *            the memory footprints (in bytes) of each invocation.
         */
        Result(String method, Object[] args, long[] runtimes, long[] steps, long[] memoryFootprints) {
            this.method = method;
            this.args = args;
            this.runtimes = runtimes;
            this.steps = steps;
            this.memoryFootprints = memoryFootprints;
        }
        
        /**
         * 
         * @return the average runtime in nanoseconds.
         * @see #getMedianRuntime()
         * @see #getRuntimes()
         */
        public double getAverageRuntime() {
            long sum = 0;
            
            for (long element : runtimes) {
                sum += element;
            }
            
            return (double) sum / (double) runtimes.length;
        }
        
        /**
         * 
         * @return the median runtime in nanoseconds.
         * @see #getAverageRuntime()
         * @see #getRuntimes()
         */
        public double getMedianRuntime() {
            final int l = runtimes.length;
            
            if (runtimes.length % 2 == 0) {
                return (runtimes[l / 2 - 1] + runtimes[l / 2]) / 2.0;
            } else {
                return runtimes[l / 2];
            }
        }
        
        /**
         * 
         * @return the runtimes of each invocation in nanoseconds.
         * @see #getMedianRuntime()
         * @see #getAverageRuntime()
         */
        public long[] getRuntimes() {
            return runtimes;
        }
        
        /**
         * 
         * @return the average runtime in calls to {@link StepCounter#step()}.
         * @see #getMedianSteps()
         * @see #getSteps()
         */
        public double getAverageSteps() {
            long sum = 0;
            
            for (long element : steps) {
                sum += element;
            }
            
            return (double) sum / (double) steps.length;
        }
        
        /**
         * 
         * @return the median runtime in calls to {@link StepCounter#step()}.
         * @see #getAverageSteps()
         * @see #getSteps()
         */
        public double getMedianSteps() {
            final int l = steps.length;
            
            if (steps.length % 2 == 0) {
                return (steps[l / 2 - 1] + steps[l / 2]) / 2.0;
            } else {
                return steps[l / 2];
            }
        }
        
        /**
         * 
         * @return the runtimes of each invocation in calls to
         *         {@link StepCounter#step()}.
         * @see #getAverageSteps()
         * @see #getMedianSteps()
         */
        public long[] getSteps() {
            return steps;
        }
        
        /**
         * 
         * @return the average memory footprint in bytes.
         * @see #getMedianMemoryFootprint()
         * @see #getMemoryFootprints()
         */
        public double getAverageMemoryFootprint() {
            long sum = 0;
            
            for (long element : memoryFootprints) {
                sum += element;
            }
            
            return (double) sum / (double) memoryFootprints.length;
        }
        
        /**
         * 
         * @return the median memory footprint in bytes.
         * @see #getAverageMemoryFootprint()
         * @see #getMemoryFootprints()
         */
        public double getMedianMemoryFootprint() {
            final int l = memoryFootprints.length;
            
            if (memoryFootprints.length % 2 == 0) {
                return (memoryFootprints[l / 2 - 1] + memoryFootprints[l / 2]) / 2.0;
            } else {
                return memoryFootprints[l / 2];
            }
        }
        
        /**
         * 
         * @return the memory footprints of each invocation in bytes.
         * @see #getAverageMemoryFootprint()
         * @see #getMedianMemoryFootprint()
         */
        public long[] getMemoryFootprints() {
            return memoryFootprints;
        }
        
        /**
         * 
         * @return a string representation of the method run.
         */
        public String getMethod() {
            return method;
        }
        
        /**
         * 
         * @return how many times the method was run.
         */
        public int getAccuracy() {
            return runtimes.length;
        }
        
        /**
         * 
         * @return the arguments with which the method was run.
         */
        public Object[] getArgs() {
            return args;
        }
        
        @Override
        public String toString() {
            StringJoiner runtimesStr = new StringJoiner(", ", "Runtimes: ", "\r\n");
            StringJoiner stepsStr = new StringJoiner(", ", "Steps: ", "\r\n");
            StringJoiner memStr = new StringJoiner(", ", "Memory: ", "\r\n");
            StringJoiner argsStr = new StringJoiner(", ", "Arguments: ", "\r\n");
            
            for (long runtime : runtimes) {
                runtimesStr.add("" + runtime);
            }
            
            for (long step : steps) {
                stepsStr.add("" + step);
            }
            
            for (long mem : memoryFootprints) {
                stepsStr.add("" + mem);
            }
            
            if (args != null) {
                for (Object arg : args) {
                    argsStr.add("" + arg);
                }
            }
            
            return method + "\r\n" + runtimesStr + stepsStr + memStr + argsStr;
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + Arrays.hashCode(args);
            result = prime * result + ((method == null) ? 0 : method.hashCode());
            result = prime * result + Arrays.hashCode(runtimes);
            result = prime * result + Arrays.hashCode(steps);
            return result;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Result) {
                Result res = (Result) obj;
                return method.equals(res.method) && Arrays.deepEquals(args, res.args)
                        && Arrays.equals(runtimes, res.runtimes) && Arrays.equals(steps, res.steps);
            }
            
            return false;
        }
    }
    
    protected Object writeReplace() throws ObjectStreamException {
        return new SerializedForm(this);
    }
    
    private static class SerializedForm implements Serializable {
        
        private static final long serialVersionUID = -4292140883475866606L;
        
        private Class<?> type;
        
        private Object object;
        
        private Object[] args;
        
        private Class<?>[] argTypes;
        
        private String methodName;
        
        private int accuracy;
        
        private SerializedForm(TestRun testRun) {
            // Method is not serializable, so we just store the method name, and
            // the argument types.
            type = testRun.method.getDeclaringClass();
            argTypes = testRun.method.getParameterTypes();
            
            object = testRun.object;
            args = testRun.args;
            methodName = testRun.method.getName();
            accuracy = testRun.accuracy;
        }
        
        private Object readResolve() throws ObjectStreamException {
            Method method = null;
            try {
                method = ReflectionUtils.getDeclaredOrCompatibleMethod(type, methodName, argTypes);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new InvalidObjectException("Cause: " + e.toString() + "\nMessage: " + e.getMessage());
            }
            
            return new TestRun(object, method, args, accuracy);
        }
    }
}
