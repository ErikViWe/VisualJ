package edu.kit.ipd.sdq.visualj.util;

/**
 * Classes that implement this interface provide information about the breakpoints used in the
 * {@code edu.kit.ipd.sdq.visualj.datavis} package.
 */
public interface BreakpointViewer extends BreakpointObservable {
    
    /**
     * Returns the level of the breakpoint with the given id.
     * 
     * @param breakpointId
     *            the breakpoint id
     * @return the level of the breakpoint
     * @throws IndexOutOfBoundsException
     *             if there is no breakpoint with the given id
     */
    int getBreakpointLevel(int breakpointId) throws IndexOutOfBoundsException;
    
    /**
     * Returns the id of the latest added breakpoint or {@code -1} if there is none.
     * 
     * @return the id of the latest added breakpoint or {@code -1} if there is none
     */
    int getLatestBreakpointId();
    
    /**
     * Returns the id of the nearest breakpoint with a level equal or smaller than {@code maxLevel} starting from the
     * breakpoint with the id {@code fromBreakpointId} and taking only newer breakpoints into account.
     * <br>
     * <br>
     * <b>Note:</b> If the breakpoint with the id {@code fromBreakpointId}
     * fulfills the above criteria (its level is equal or smaller than
     * {@code maxLevel}) then {@code fromBreakpointId} is returned.
     * 
     * @param fromBreakpointId
     *            the id from which to start searching
     * @param maxLevel
     *            the maximum level the returned breakpoint should have
     * @return the id of the nearest breakpoint specified as above, or {@code -1} if no such breakpoint exists
     * @throws IndexOutOfBoundsException
     *             if {@code fromBreakpointId} is not a valid breakpoint id
     */
    int getNextBreakpointId(int fromBreakpointId, int maxLevel) throws IndexOutOfBoundsException;
    
    /**
     * Returns the id of the nearest breakpoint with a level equal or smaller than {@code maxLevel} starting from the
     * breakpoint with the id {@code fromBreakpointId} and taking only older breakpoints into account.
     * <br>
     * <br>
     * <b>Note:</b> If the breakpoint with the id {@code fromBreakpointId} fulfills the above criteria (its level is
     * equal or smaller than {@code maxLevel}) then {@code fromBreakpointId} is returned.
     * 
     * @param fromBreakpointId
     *            the id from which to start searching
     * @param maxLevel
     *            the maximum level the returned breakpoint should have
     * @return the id of the nearest breakpoint specified as above, or {@code -1} if no such breakpoint exists
     * @throws IndexOutOfBoundsException
     *             if {@code fromBreakpointId} is not a valid breakpoint id
     */
    int getPrevBreakpointId(int fromBreakpointId, int maxLevel) throws IndexOutOfBoundsException;
}
