/** Two dimensional Kalman Filter implementation
 * @author Cory W. Mauer
 * @version 0.1 2020/05/31
 */
import java.util.ArrayList;

public class TwoDimensionalKF extends KalmanFilter {
    private ArrayList<Position> pos_;
    private ArrayList<Velocity> vel_;
    private double posVarience_;
    private double lastPosUpdateTime_;
    private double lastVelUpdateTime_;
    private double KalmanGain_;

    public TwoDimensionalKF()
    {
        pos_ = new ArrayList<Position>(2);
        vel_ = new ArrayList<Velocity>(2);
    }

    /** Method for initializing a Kalman Filter
     * @param Array of measured input positions
     * @param time of measurement
     * @param varience of measurement (sigma^2 uncertainty)
     */
    @Override
    public void initFilter(ArrayList<Position> initPos, double time, double varience)
    {
        lastPosUpdateTime_ = time;
        System.out.println("init pos size" + initPos.size());
        pos_.addAll(initPos);
        if(pos_.size() != 2)
        {
            System.out.println("Error - pos_ size: " + pos_.size() + " Exit");
            System.exit(1);
        }

        for(int i = 0; i < pos_.size(); ++i)
        {
            vel_.add(new Velocity(0.0));
        }
        if(vel_.size() != 2)
        {
            System.out.println("Error - vel_ size: " + vel_.size() + " Exit");
            System.exit(1);
        }
        posVarience_ = varience;

    }

    /** Method for updating a Kalman Filter
     * @param Array of measured input positions
     * @param time of measurement
     * @param varience of measurement (sigma^2 uncertainty)
     */
    @Override
    public void updateFilter(ArrayList<Position> updatePos, double time, double varience)
    {
        // Input error handling
        if(updatePos.size() != 2)
        {
            System.out.println("Error - updatePos size: " + updatePos.size() + " Exit");
            System.exit(1);
        }
        if(varience <= 0)
        {
            System.out.println("Error - varience: " + varience + " Exit");
            System.exit(1);
        }

        // Update Position
        KalmanGain_ = calculateKalmanGain(varience, posVarience_);
        double deltaTime = time - lastPosUpdateTime_;
        for(int i = 0; i < pos_.size(); ++i)
        {
            double estimatePos = pos_.get(i).getPosition() + vel_.get(i).getVelocity() * deltaTime;
            estimatePos = estimatePos + KalmanGain_ * (updatePos.get(i).getPosition() - estimatePos);


            // estimate velocity
            // only update velocity estimate if the delta time is large enough
            if((time - lastVelUpdateTime_) > 0.1)
            {
                System.out.println("predict velocity");
                double estVelocity = (estimatePos -  pos_.get(i).getPosition()) / deltaTime;
                vel_.get(0).setVelocity(estVelocity);
                System.out.println("predict velocity: " + vel_.get(i).getVelocity());
                lastVelUpdateTime_ = time;
            }
            else{
                System.out.println("No velocity predition");
            }
            pos_.get(i).setPosition(estimatePos);
        }
        // update varience
        posVarience_ = (1.0- KalmanGain_) * posVarience_;

        // update position
        lastPosUpdateTime_ = time;

    }

    /** Method for predicting the current position using a Kalman Filter
     * @param desired time of prediction
     */
    @Override
    public ArrayList<Position> predictState(double time)
    {
        ArrayList<Position> predictedPosition = new ArrayList<Position>(1);

        // Assume position at time with velocity
        double deltaTime = time - lastPosUpdateTime_;
        if(pos_.size() != vel_.size())
        {
            System.out.println("Error pos_ size: " + pos_.size() + " and vel_ size: " + vel_.size() +
            " do not match");
        }
        for(int i = 0; i < pos_.size(); ++i)
        {
            double position = pos_.get(i).getPosition() + (vel_.get(i).getVelocity() * deltaTime);
            predictedPosition.add( new Position(position));
        }
        return predictedPosition;
    }

    /** Method for printing the state of the filter
     *
     */
    public void printInfo()
    {
        System.out.println("position Value:" + pos_.get(0).getPosition());
        System.out.println("velocitiy Value:" + vel_.get(0).getVelocity());
        System.out.println("position Value:" + pos_.get(1).getPosition());
        System.out.println("velocitiy Value:" + vel_.get(1).getVelocity());
        System.out.println("lastUpdateTime Value:" + lastPosUpdateTime_);
        System.out.println("KalmanGain_ Value:" + KalmanGain_);
    }
}