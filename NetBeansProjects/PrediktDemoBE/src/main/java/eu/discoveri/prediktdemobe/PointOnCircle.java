/*
 * Calculates a point on a zodiac circle.
 */
package eu.discoveri.prediktdemobe;

import eu.discoveri.predikt.exception.DegreeMinSecException;
import javafx.geometry.Point2D;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class PointOnCircle
{
  /**
   * Calculates a point on a zodiac circle.
   * http://www.onlinemathe.de/forum/Kreis-Punkte-auf-der-Linie-Berechnen
   * https://upload.wikimedia.org/wikipedia/commons/8/82/Sinus_en_cosinus.png
   *
   * @param radius The radius of the circle.
   * @param degree of the point on the circle.
   * @param offsetFromRadius
   * @param traditionalDirection Calculate point starting (degree = 0) from ascendent and moving counter-clockwise. Defaults to true.
   * If set to false, the calculation starts from descendent and moves counter-clockwise.
   * @returns {{x: *, y: *}}
   */
    public static Point2D getPointOnCircle( double radius, DegreeMinSec dms, double offsetFromRadius, boolean traditionalDirection )
    {
        return getPointOnCircle( radius, DegreeMinSec.convertDegreeMinutesSecondsToFloat(dms), offsetFromRadius, traditionalDirection );
    }
    
    public static Point2D getPointOnCircle( double radius, DegreeMinSec dms )
    {
        return getPointOnCircle( radius, DegreeMinSec.convertDegreeMinutesSecondsToFloat(dms), 0.0, false );
    }
    
    public static Point2D getPointOnCircle( double radius, double degree )
    {
        return getPointOnCircle( radius, degree, 0.0, false );
    }
    
    public static Point2D getPointOnCircle( double radius, double degree, double offsetFromRadius, boolean traditionalDirection )
    {
        double x = 0.0d, y = 0.0d;
        double xCenterPoint = 0.0d, yCenterPoint = 0.0d;
        double degreeNormalized = degree * (Math.PI / 180.0);

        double xNormalized = Math.cos(degreeNormalized);
        double yNormalized = Math.sin(degreeNormalized);

        if( offsetFromRadius != 0.0d )
        {
          x = xCenterPoint + (radius - offsetFromRadius) * xNormalized;
          y = yCenterPoint + (radius - offsetFromRadius) * yNormalized;
        } else {
          x = xCenterPoint + radius * xNormalized;
          y = yCenterPoint + radius * yNormalized;
        }

        // Eh?
        boolean ySvgInverse = true;
        if( ySvgInverse )
        {
          y = -y;
        }

        if( traditionalDirection )
        {
          x = -x;
          y = -y;
        }

    return new Point2D(x,y);
    }

    public static double getOppositeDegree( double degree )
    {
        if (degree < 180.)
        {
            return degree + 180.;
        }
        else
        {
            return degree - 180.;
        }
    }

    public static double getRandomInt( double min, double max )
    {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }

    public static double getRandomArbitrary( double min, double max )
    {
        return Math.random() * (max - min) + min;
    }

    public static boolean isEachDegree( double interval, double degree )
    {
        return degree % interval == 0 && degree > 0;
    }
}


class DegreeMinSec
{
    private float degrees, minutes, seconds;

    public DegreeMinSec(float degrees, float minutes, float seconds)
            throws DegreeMinSecException
    {
        if( degrees < 0.0 || degrees > 360.0 )
        {
            throw new DegreeMinSecException("Degree must be between 0 and 360.");
        }
        this.degrees = degrees;
        if( minutes < 0.0 || minutes > 60.0 )
        {
            throw new DegreeMinSecException("Degree must be between 0 and 60.");
        }
        this.minutes = minutes;
        if( seconds < 0.0 || seconds > 60.0 )
        {
            throw new DegreeMinSecException("Degree must be between 0 and 60.");
        }
        this.seconds = seconds;
    }

    /**
     * Get components
     * @return 
     */
    public float getDegrees() { return degrees; }

    public float getMinutes() { return minutes; }

    public float getSeconds() { return seconds; }
    

    /*
     * Degrees Minutes Seconds to decimal degrees
     */
    public static float convertDegreeMinutesSecondsToFloat( DegreeMinSec dms )
    {
        float minutes = dms.getMinutes() * 1.f/60.f;
        float seconds = dms.getSeconds() * 1.f/3600.f;

        return dms.getDegrees() + minutes + seconds;
    }
}