/** Velocity class
 * @author Cory W. Mauer
 * @version 0.1 2020/05/31
 */
public class Velocity {
    private double velocity_;

    /** Constructor
     *
     */
    public Velocity()
    {
        velocity_ = 0.0;
    }

    /** Constructor
     * @param initial velocity
     */
    public Velocity(double vel)
    {
        velocity_ = vel;
    }

    /**  velocity mutator
     * @param velocity
     * */
    public void setVelocity(double velocity)
    {
        velocity_ = velocity;
    }
    /**  velocity accessor
     * @return velocity
     * */
    public double getVelocity()
    {
        return velocity_;
    }
    /**  calculate Velocity and store locally
     * @param initial position
     * @param final positiopn
     * @param time delta
     * */
    public void calculateVelocity(double initialPos, double finalPos, double deltaTime)
    {
        velocity_ = (finalPos - initialPos) / deltaTime;
    }
}