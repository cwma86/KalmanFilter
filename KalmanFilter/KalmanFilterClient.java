/** Program to test Kalman filter factory with various dimensions
 * @author Cory W. Mauer
 * @version 0.1 2020/05/23
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import com.mitchtalmadge.asciidata.graph.ASCIIGraph;

public class KalmanFilterClient {
    /**
     * Method to describe input argument options and file parse structure
     */
    public static void help()
    {
        System.out.println("given a text files of distance and uncertainties this methods calculates the filtered position");
        System.out.println("");
        System.out.println("file format:");
        System.out.println("#uncertainty time Position1 ... PositionN");
        System.out.println("0.5 0.0 50.1 20.1");
        System.out.println("0.5 1.0 51.1 20.1");
        System.out.println("...");
        System.out.println("0.5 10.0 55.1 20.1");
        System.out.println("");
        System.out.println("input arguments:");
        System.out.println("-h, --help  help menu");
        System.out.println("-i, --inputFile <path>  input file path");
    }

    /** Method to parse input argument string array
     * @params Array of input arguments
     * @return input file path
     */
    public static String argParse(String[] args)
    {
        int argumentLength = args.length;
        String inputFileArg ="";

        // Check if required inputs were provided
        if(argumentLength == 0)
        {
            help();
            System.out.println("Error input argument required");
            System.exit(1);
        }

        // Read the arguments
        for(int i = 0; i < args.length; ++i)
        {
            switch(args[i].charAt(0))
            {
                case '-':
                    if((args[i].equals("-h")) || (args[i].equals("--help"))  )
                    {
                        help();
                        System.exit(1);
                    }
                    if((args[i].equals("-i")) || (args[i].equals("--inputFile"))  )
                    {
                        // Input file argument, grab the next arg for file input
                        if (i < args.length)
                        {
                            ++i;
                            inputFileArg = args[i];
                        }
                        else
                        {
                            help();
                            System.out.println("Error- not input file provided to input string");
                        }
                    }
                    else
                    {
                        help();
                        System.out.println("Error - unknown input option: " + args[i]);
                        System.exit(1);

                    }
            }
        }
        return inputFileArg;
    }

    /** Program to intake a file of distance measurements and predict true position using a 1D Kalman filter
     *@param input arguments
     */
    public static void main(String[] args)
    {
        // Get the input arguments from the args string
        String inputFileArg = argParse(args);

        // Initialize varience estimate
        double estVarience = 0.0;

        // Initialize measurement distance list
        ArrayList<ArrayList<Position>> inputMeasurements = new  ArrayList<ArrayList<Position>>();

        // Initialize estimated distance list
        ArrayList<ArrayList<Position>> estimatedPosition = new  ArrayList<ArrayList<Position>>();

        KalmanFilter KalmanFltr = null;
        KalmanFilterFactory kfFactory = new KalmanFilterFactory();

        // Parse input file
        File inputFile = new File(inputFileArg);
        if( !inputFile.exists())
        {
            help();
            System.out.println("Error - input file:" + inputFileArg + " does not exist.");
            System.exit(1);

        }
        try {
            Scanner fileScnr = new Scanner(inputFile);
            String line="";
            while (fileScnr.hasNextLine())
            {
                // TODO move the file line parsing to a sperate method
                // Parse line of file
                line = fileScnr.nextLine();
                // check for comment line
                if(line.charAt(0) == '#')
                {
                    continue;
                }
                // Read measurement and uncertainty from line
                Scanner lineScnr = new Scanner(line);

                // Check for uncertainty field
                if(!lineScnr.hasNextDouble())
                {
                    System.out.println("Error - no uncertainty, unexpected file parse format");
                    return;
                }
                double measurmementSigma = lineScnr.nextDouble();
                estVarience = measurmementSigma * measurmementSigma;

                // Check for Time field
                if(!lineScnr.hasNextDouble())
                {
                    System.out.println("Error - no time, unexpected file parse format");
                    return;
                }
                double time = lineScnr.nextDouble();

                // Check for position fields
                ArrayList<Position> measurement = new ArrayList<>();
                while(lineScnr.hasNextDouble())
                {
                    Position pos = new Position(lineScnr.nextDouble());
                    measurement.add(pos);
                }
                // if first pass call the kalman filter factory and init the filter
                if(inputMeasurements.size() == 0)
                {
                    KalmanFltr = kfFactory.getKalman(measurement, time, estVarience);
                }
                else if(KalmanFltr != null)
                {
                    KalmanFltr.updateFilter(measurement, time, estVarience);
                }
                else
                {
                    System.out.println("Error - filter never created");
                    return;
                }
                inputMeasurements.add(measurement);
                estimatedPosition.add(KalmanFltr.predictState(time));
                KalmanFltr.printInfo();

                // TODO are the performance concerns with allocating a new line scanner for every line?
                lineScnr.close();

            }
            fileScnr.close();
        } catch (FileNotFoundException fnfe) {
            help();
            System.out.println("Error file not found exception");
            System.exit(1);
        }



        // Plot measured distance from list vs number of measurements
        // The series should be an array of doubles.
        if(inputMeasurements.size() == 0)
        {
            System.out.println("Error - unable to plot measDistance, array size is 0");
            return;
        }

        // Iterate through each dimension
        for(int i = 0; i < estimatedPosition.get(0).size(); ++i)
        {
            double[] measDistance = new double[inputMeasurements.size()];

            // Plot first position dimension
            int location = 1;
            for (int j = 0; j < inputMeasurements.size(); j++)
            {
                measDistance[j] = inputMeasurements.get(j).get(i).getPosition();
            }
            // Plot measured distance and output the graph.
            System.out.println("Raw Measurements provided by the Sensor for dimension " + (i + 1) + "-D");
            System.out.println("");
            System.out.println(ASCIIGraph.fromSeries(measDistance).withNumRows(15).plot());
        }

        // Plot estimated distance from list
        // The series should be an array of doubles.
        if(estimatedPosition.size() == 0)
        {
            System.out.println("Error - unable to plot estimatedPosition, array size is 0");
            return;
        }

        // Iterate through each dimension
        for(int i = 0; i < estimatedPosition.get(0).size(); ++i)
        {
            double[] estDistance = new double[estimatedPosition.size()];

            // Plot position for the dimension
            for (int j = 0; j < estimatedPosition.size(); j++)
            {
                estDistance[j] = estimatedPosition.get(j).get(i).getPosition();
            }

            // Plot and output the estimated distance graph.
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("Estimated distance from the kalman filter " + (i + 1) + "-D");
            System.out.println(ASCIIGraph.fromSeries(estDistance).withNumRows(15).plot());
        }
    }
}