/*
 * DE438 version.
 */
package eu.discoveri.predikt.ephemeris;

import eu.discoveri.predikt.utils.Constants;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Header:
 * 
 * Data file:
 * First two words are section number and data sectio.
 * The first two double precision words in each data record contain:
 *       Julian date of earliest data in record.
 *       Julian date of latest data in record.
 * Format:
 *    1  1042
 *   0.2433264500000000D+07    0.2433296500000000D+07    0.4416951468071251D+08
 *   0.8080851859133677D+07   -0.7484298527403557D+06 ...
 *  :
 *    2  1042
 *   0.2433296500000000D+07    0.2433328500000000D+07   -0.2689229696311430D+08
 *  -0.1692656323708943D+08    0.9281544239931520D+06 ...
 *  :
 * 
 * There are three Cartesian components (x, y, z), for each of the items #1-11; 
 * there are two components for the 12th item, nutations : d(psi) and d(epsilon);
 * there are three components for the 13th item, librations : phi, theta, psi;
 * there are three components for the 14th item, mantle omega_x,omega_y,omega_z;
 * there is one component for the 17th item, TT-TDB.
 * 
 * Planetary positions are stored in units of kilometers (TDB-compatible).
 * Nutations and librations are stored in units of radians.  The mantle angular
 * velocities are stored in radians/day. TT-TDB is stored in seconds.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class DE438 extends DEheader
{
    /**
     * Array with the names and starting dates of each file with Chebychev coefficients.
     */
//    private double[] startfiledates = { 2287184.5, 2323696.5, 2360208.5,
//                    2396752.5, 2433264.5, 2469776.5, 2506320.5, 2542832.5, 2579376.5,
//                    2615888.5, 2652400.5, 2688976.5 };
//    private final String[] filenames = { "ascp01550.438t", "ascp01650.438t",
//                    "ascp01750.438t", "ascp01850.438t", "ascp01950.438t", "ascp02050.438t",
//                    "ascp02150.438t", "ascp02250.438t", "ascp02350.438t", "ascp02450.438t",
//                    "ascp02550.438t" };
//    private final FileData[] fd = { new FileData("ascp01550.438t",2287184.5,2323696.5),
//                                    new FileData("ascp01650.438t",2323696.5,2360208.5),
//                                    new FileData("ascp01750.438t",2360208.5,2396752.5),
//                                    new FileData("ascp01850.438t",2396752.5,2433264.5),
//                                    new FileData("ascp01950.438t",2433264.5,2469776.5),
//                                    new FileData("ascp02050.438t",2469776.5,2506320.5),
//                                    new FileData("ascp02150.438t",2506320.5,2542832.5),
//                                    new FileData("ascp02250.438t",2542832.5,2579376.5),
//                                    new FileData("ascp02350.438t",2579376.5,2615888.5),
//                                    new FileData("ascp02450.438t",2615888.5,2652400.5),
//                                    new FileData("ascp02550.438t",2652400.5,2688976.5) };
    private final FileData[] fd = { new FileData("ascp01950.438t",2433264.5,2469776.5) };

    /**
     * Constructor which sets the main parameters of the ephemeris.
     */
    public DE438()
    {
        /**
         * Numbers per interval.<br>
         * NCOEFF-2. NCOEFF is from file header.424.
         */
        numbersperinterval = 1016;

        /** GROUP 1010 from file header.438 */
        denomber = 438;

        /* GROUP 1030 from file header.438 */
        /**
         * Start epoch for all ephemeris in Julian Days. (21Dec1549)
         */
        startepoch = 2287184.50;
        /**
         * End epoch for all ephemeris in Julian Days. (25Jan2650)
         */
        finalepoch = 2688976.50;
        /**
         * Ephemerides files are broken into intervals of length
         * "interval duration", in [days].
         */
        intervalduration = 32;

        /* GROUP 1040 - 1041 from file header.438 */
        /**
         * Speed of light in [km/sec].
         */
        clight = 0.299792458000000000e+06;
        /**
         * Length of an A.U., in [km].
         */
        au = 0.149597870700000000e+09;
        /**
         * Earth - Moon mass ratio.
         */
        emrat = 0.813005690741906200e+02;
        /**
         * Mass parameter for Mercury GM in [AU^3/day^2].
         */
        gm1 = 0.491248045036476000e-10;
        /**
         * Mass parameter for Venus GM in [AU^3/day^2].
         */
        gm2 = 0.724345233264412000e-09;
        /**
         * Mass parameter for Earth-Moon barycenter GM in [AU^3/day^2].
         */
        gmb = 0.899701139019987100e-09;
        /**
         * Mass parameter for Mars GM in [AU^3/day^2].
         */
        gm4 = 0.954954869555077000e-10;
        /**
         * Mass parameter for Jupiter GM in [AU^3/day^2].
         */
        gm5 = 0.282534584083387000e-06;
        /**
         * Mass parameter for Saturn GM in [AU^3/day^2].
         */
        gm6 = 0.282534584083387000e-06;
        /**
         * Mass parameter for Uranus GM in [AU^3/day^2].
         */
        gm7 = 0.129202482578296000e-07;
        /**
         * Mass parameter for Neptune GM in [AU^3/day^2].
         */
        gm8 = 0.152435734788511000e-07;
        /**
         * Mass parameter for Pluto GM in [AU^3/day^2].
         */
        gm9 = 0.217844105197418000e-11;
        /**
         * Mass parameter for Sun GM in [AU^3/day^2].
         */
        gms = 0.295912208285591100e-03;
        /**
         * A new instance of variable for Chebyshev polynomials coefficients
         * with dimension (number of intervals * numbers_per_interval+1).
         */
        ephemeriscoefficients = new double[1500000];
    }

    /**
     * Method to read the DE438 ASCII ephemeris file corresponding to 'jultime'.
     * The start and final dates of the ephemeris file are set, as are the
     * Chebyshev polynomials coefficients for Mercury, Venus, Earth-Moon, Mars,
     * Jupiter, Saturn, Uranus, Neptune, Pluto, Geocentric Moon, Sun, nutations
     * and lunar librations.
     * 
     * @param jultime Julian date for calculation.
     * @return true if all reading procedures is OK and 'jultime' is in properly
     *         interval.
     */
    @Override
    protected boolean readEphCoeff( double jultime )
    {
        int records = 0, idx = 0;
        String filename = "", fileline= "";
        boolean result = false;

        // Doesn't compute!
        if( (jultime < this.startepoch) || (jultime >= this.finalepoch) )
        {
            return result;
        }
        
        if( (jultime < this.ephemerisdates[1]) || (jultime >= this.ephemerisdates[2]) )
        {
            try
            {
                /* Select the proper ephemeris file */
                for( int i = 0; i < fd.length - 1; i++ )
                {
                    if( (jultime >= fd[i].getStartdate()) && (jultime < fd[i].getEnddate()) )
                    {
                        filename = fd[i].getFilename();
                        records = (int) (fd[i].getEnddate() - fd[i].getStartdate()) / intervalduration;
                    }
                }
                
                String infile = Constants.RESOURCEPATH+"/ephemeris/" + filename;
                InputStream is = new FileInputStream(infile);
                
                /* Read each record in the file */
                try( BufferedReader file = new BufferedReader(new InputStreamReader(is)) )
                {
                    /* Read each record in the file */
                    for( int j = 1; j <= records; j++ )
                    {
                        /* read line 1 and ignore */
                        file.readLine();

                        /* Now read real number data */
                        do
                        {
                            fileline = file.readLine().trim().replaceAll("D","e").replaceAll(" +",":");
                            String[] arrOfVals = fileline.split(":");
                            
                            for( String v: arrOfVals )
                            {
                                ephemeriscoefficients[idx++] = Double.parseDouble(v);
                            }
                        } while( fileline.indexOf('e') != -1 && fileline.indexOf('.') != -1 );
                    }
                }
                result = true;
            }
            catch( IOException | StringIndexOutOfBoundsException | NumberFormatException ex )
            {
                System.out.println( "ERR!! File: "+filename+ ", Error = " +ex.toString() );
                System.out.println( "   Fileline: " +fileline);
                System.exit(-1);
            }
        } else {
            result = true;
        }
        
        return result;
    }
}


/*
 * File name and date
 */
class FileData
{
    private final String  filename;
    private final double  startdate, enddate;

    public FileData( String filename, double startdate, double enddate )
    {
        this.startdate = startdate;
        this.enddate = enddate;
        this.filename = filename;
    }

    public double getStartdate() { return startdate; }
    public double getEnddate() { return enddate; }
    public String getFilename() { return filename; }
}
