package edu.kit.ipd.sdq.visualj.efficiency.measure;

import java.io.InvalidObjectException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.exception.CloneFailedException;

import edu.kit.ipd.sdq.visualj.util.Misc;
import edu.kit.ipd.sdq.visualj.util.ReflectionUtils;

/**
 * A EfficiencyTest contains methods for measuring the time and space efficiency
 * of a given method with multiple inputs.
 * 
 * <p>
 * An object of this class is serializable if and only if every argument passed
 * to the tested method (i.e. every object in {@code argsList} or every object
 * returned by the Generator), as well as the object the method is invoked on,
 * are serializable. If this is not the case,
 * {@link ObjectOutputStream#writeObject(Object)} will throw a
 * {@link NotSerializableException}.
 * </p>
 * 
 * @see TestRun
 */
public class EfficiencyTest implements Serializable {
    
    private static final long serialVersionUID = -5701377923803925908L;
    
    private Object object;
    
    private Method method;
    
    private ArgumentTupleGenerator parameterGenerator;
    
    private int accuracy;
    
    /**
     * Creates a new {@code EfficiencyTest}.
     * 
     * @param object
     *            the object on which to run the method; can be {@code null} if
     *            the method is static, or if the object should be
     *            default-constructed before every invocation.
     * @param method
     *            the method to test.
     * @param argsList
     *            an array of arrays containing the arguments to pass to the
     *            method.
     * @param accuracy
     *            how many times to run the method on each input.
     * 
     * @throws IllegalArgumentException
     *             if {@code object} does not fit the method.
     * @throws NullPointerException
     *             if {@code method}, or {@code argsList} are {@code null}.
     */
    public EfficiencyTest(Object object, Method method, Object[][] argsList, int accuracy)
            throws IllegalArgumentException {
        this(object, method, new DefaultGenerator(argsList), accuracy);
    }
    
    /**
     * Creates a new {@code EfficiencyTest}.
     * 
     * @param object
     *            the object on which to run the method; can be {@code null} if
     *            the method is static, or if the object should be
     *            default-constructed before every invocation.
     * @param method
     *            the method to test.
     * @param argsList
     *            an array of arrays containing the arguments to pass to the
     *            method.
     * @param parameterGenerator
     *            a generator whose {@link ArgumentTupleGenerator#yield()}
     *            method returns input arguments for the method.
     * @param accuracy
     *            how many times to run the method on each input.
     * 
     * @throws IllegalArgumentException
     *             if {@code object} does not fit the method.
     * @throws NullPointerException
     *             if {@code method}, or {@code parameterGenerator} are
     *             {@code null}.
     */
    public EfficiencyTest(Object object, Method method, ArgumentTupleGenerator parameterGenerator, int accuracy)
            throws IllegalArgumentException {
        if (method == null) {
            throw new NullPointerException("method");
        } else if (parameterGenerator == null) {
            throw new NullPointerException("parameterGenerator");
        }
        
        this.object = object;
        this.method = method;
        
        if (parameterGenerator instanceof DefaultGenerator) {
            this.parameterGenerator = ((DefaultGenerator) parameterGenerator).clone();
        } else {
            try {
                this.parameterGenerator = ObjectUtils.cloneIfPossible(parameterGenerator);
            } catch (CloneFailedException e) {
                this.parameterGenerator = parameterGenerator;
            }
        }
        
        this.accuracy = accuracy;
    }
    
    /**
     * Runs the tests.
     * 
     * @return the results of every {@link TestRun}.
     * 
     * @throws NoDefaultConstructorException
     *             if {@code object == null}, the method is not static, and
     *             {@code object}'s class has no public default constructor.
     * @throws InvocationTargetException
     *             if the default constructor or the method throw an exception.
     * @throws IllegalArgumentException
     *             if the method cannot be invoked on the specified object with
     *             the specified arguments.
     * @throws IllegalAccessException
     *             if the method is not public.
     */
    public TestRun.Result[] run() throws NoDefaultConstructorException, InvocationTargetException,
            IllegalAccessException, IllegalArgumentException {
        List<TestRun.Result> resList = new LinkedList<>();
        
        // We save every argument tuple from the generator in argsList,
        // and later instantiate a new generator using this argsList.
        // This makes sure that:
        // (a) this EfficiencyTest can be run again.
        // (b) this EfficiencyTest can be properly serialized.
        Deque<Object[]> argsList = new LinkedList<>();
        
        // Run tests.
        argsList.addLast(parameterGenerator.yield());
        while (argsList.peekLast() != null) {
            resList.add(new TestRun(object, method, argsList.peekLast(), accuracy).run());
            argsList.addLast(parameterGenerator.yield());
        }
        
        // Save argument tuples.
        parameterGenerator = new DefaultGenerator(argsList.toArray(new Object[0][]));
        
        return resList.toArray(new TestRun.Result[0]);
    }
    
    @Override
    public String toString() {
        return "Object: " + object + "\r\n" + "Method: " + method + "\r\n" + "accuracy: " + accuracy;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + accuracy;
        result = prime * result + Objects.hashCode(method);
        result = prime * result + Objects.hashCode(object);
        result = prime * result + ((parameterGenerator == null) ? 0 : parameterGenerator.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EfficiencyTest) {
            EfficiencyTest et = (EfficiencyTest) obj;
            
            return Objects.equals(object, et.object) && Objects.equals(parameterGenerator, et.parameterGenerator)
                    && Objects.equals(method, et.method) && accuracy == et.accuracy;
        }
        
        return false;
    }
    
    /**
     * An {@code ArgumentTupleGenerator} may be used to generate the arguments
     * with which to invoke the method you want to test lazily.
     * 
     * Its {@link #yield()} method always returns the next argument tuple with
     * which to invoke the method.
     */
    @FunctionalInterface
    public static interface ArgumentTupleGenerator {
        
        /**
         * 
         * @return the next argument tuple with which to invoke the method, or
         *         {@code null} if there is no further argument tuple available.
         */
        Object[] yield();
    }
    
    /**
     * The {@code DefaultGenerator} is used by the
     * {@link EfficiencyTest#EfficiencyTest(Object, Method, Object[][], int)}
     * constructor.
     * 
     * Its yield method simply always returns the next tuple in the array.
     */
    private static class DefaultGenerator implements ArgumentTupleGenerator, Cloneable {
        
        private Object[][] arguments;
        private int currIndex = 0;
        
        /**
         * Creates a new {@code DefaultVisualizer}.
         * 
         * @param arguments
         *            the two-dimensional array array from which to
         *            {@link #yield()}.
         * 
         * @throws NullPointerException
         *             if {@code arguments} is {@code null}.
         */
        private DefaultGenerator(Object[][] arguments) {
            if (arguments == null) {
                throw new NullPointerException("arguments");
            }
            
            this.arguments = arguments;
        }
        
        @Override
        public Object[] yield() {
            if (currIndex >= arguments.length) {
                return null;
            }
            
            Object[] result = arguments[currIndex++];
            
            // To be constistent with TestRun and the reflection library,
            // we allow null if the method takes no arguments.
            //
            // In this case, we return an empty array instead, so the run method
            // does not think
            // there are no further tuples available.
            return result == null ? new Object[]{} : result;
        }
        
        @Override
        public DefaultGenerator clone() {
            Object[][] newArgs = new Object[arguments.length][];
            
            for (int i = 0; i < arguments.length; ++i) {
                newArgs[i] = Misc.cloneArray(arguments[i]);
            }
            
            return new DefaultGenerator(newArgs);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof DefaultGenerator) {
                return Arrays.deepEquals(arguments, ((DefaultGenerator) obj).arguments);
            }
            
            return false;
        }
        
        @Override
        public int hashCode() {
            return arguments.hashCode();
        }
    }
    
    protected Object writeReplace() throws ObjectStreamException {
        return new SerializedForm(this);
    }
    
    private static class SerializedForm implements Serializable {
        
        private static final long serialVersionUID = -6011078336512719036L;
        
        private Class<?> type;
        
        private Object object;
        
        private Object[][] argsList;
        
        private String methodName;
        
        private int accuracy;
        
        private SerializedForm(EfficiencyTest efficiencyTest) {
            object = efficiencyTest.object;
            type = efficiencyTest.method.getDeclaringClass();
            methodName = efficiencyTest.method.getName();
            accuracy = efficiencyTest.accuracy;
            
            if (efficiencyTest.parameterGenerator instanceof DefaultGenerator) {
                // This is a bit faster than the else block...
                argsList = ((DefaultGenerator) efficiencyTest.parameterGenerator).arguments;
            } else {
                // If the parameterGenerator is not a default generator,
                // we have to get every argument tuple seperately.
                Set<Object[]> argsListSet = new TreeSet<>();
                
                Object[] args = efficiencyTest.parameterGenerator.yield();
                while (args != null) {
                    argsListSet.add(args);
                    args = efficiencyTest.parameterGenerator.yield();
                }
                
                argsList = argsListSet.toArray(new Object[0][0]);
            }
        }
        
        private Object readResolve() throws ObjectStreamException {
            final int numberOfArgs;
            
            if (argsList == null || argsList.length == 0 || argsList[0] == null) {
                numberOfArgs = 0;
            } else {
                numberOfArgs = argsList[0].length;
            }
            
            Class<?>[] argTypes = new Class<?>[numberOfArgs];
            
            for (int i = 0; i < numberOfArgs; ++i) {
                argTypes[i] = argsList[0][i].getClass();
            }
            
            Method method = null;
            
            try {
                method = ReflectionUtils.getDeclaredOrCompatibleMethod(type, methodName, argTypes);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new InvalidObjectException("Cause: " + e.toString() + "\nMessage: " + e.getMessage());
            }
            
            return new EfficiencyTest(object, method, argsList, accuracy);
        }
    }
}
