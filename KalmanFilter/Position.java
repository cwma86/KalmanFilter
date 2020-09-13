/** Class defining position
 *  @author Cory W. Mauer
 * @version 0.1 2020/05/31
 */
public class Position {
    private double position_;

    /** Constructor
     *
     */
    public Position()
    {
        position_ = 0.0;
    }

    /** Constructor
     * @param initial position
     */
    public Position(double pos)
    {
        position_ = pos;
    }

    /**  Position mutator
     * @param Position
     * */
    public void setPosition(double position)
    {
        position_ = position;
    }

    /**  Position accessor
     * @return Position
     * */
    public double getPosition()
    {
        return position_;
    }



}