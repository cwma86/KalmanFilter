/** Kalman filter base class
* @author Cory W. Mauer
* @version 0.1 2020/05/31
*/
import java.util.ArrayList;

public abstract class KalmanFilter {

    /** Method for initializing a Kalman Filter
     * @param Array of measured input positions
     * @param time of measurement
     * @param varience of measurement (sigma^2 uncertainty)
     */
    abstract public void initFilter(ArrayList<Position> initPos, double time, double varience);

    /** Method for updating a Kalman Filter
     * @param Array of measured input positions
     * @param time of measurement
     * @param varience of measurement (sigma^2 uncertainty)
     */
    abstract public void updateFilter(ArrayList<Position> updatePos, double time, double varience);

    /** Method for predicting the current position using a Kalman Filter
     * @param desired time of prediction
     */
    abstract public ArrayList<Position> predictState(double time);

    /** Method calculating a kalman filter gain
     * @param new measurement varience
     * @param previous estimated varience
     */
    protected static double calculateKalmanGain(final double newVarience, final double previousVarienceEst)
    {
        return previousVarienceEst / (previousVarienceEst + newVarience);
    }

    /** Method for printing the state of the filter
     *
     */
    abstract public void printInfo();

}