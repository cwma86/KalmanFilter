/** Factory for creating and intializing the correct Kalman filter
 *  for the input position based on dimensions
 * @author Cory W. Mauer
 * @version 0.1 2020/05/31
 */
import java.util.ArrayList;

public class KalmanFilterFactory {

    /** Kalman filter factory
     * @param initial position
     * @param time
     * @param Varience of measurement
     * @return initialized Kalman filter for input position dimension size
     */
    public KalmanFilter getKalman(ArrayList<Position> initialPos, double time, double estVarience)
    {
        KalmanFilter createdFilter = null;
        if(initialPos.size() == 1)
        {
            createdFilter = new OneDimensionalKF();
            createdFilter.initFilter(initialPos, time, estVarience);
        }
        else if(initialPos.size() == 2)
        {
            createdFilter = new TwoDimensionalKF();
            createdFilter.initFilter(initialPos, time, estVarience);

        }
        else
        {
            System.out.println("Error - unexpected Position size, no KalmanFilter Created");
        }
        return createdFilter;
    }
}