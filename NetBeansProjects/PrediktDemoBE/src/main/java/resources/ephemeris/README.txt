The original fortran programs for reading ascii versions of the
JPL planetary ephemeris files had a fixed maximum number of 400
for the number of ephemeris constants used in the integration
and listed in the header.xxx file for the ephemeris.

DE438t uses 576 ephemeris constants, which exceeds the prior
maximum number allowed. The file header.438 includes all 576 constants.

Use header.438t_177 if using an old program with 400 maximum
ephemeris constants. The header.438t_177 file includes a list
of only 177 ephemeris constants, omitting the mass parameters
for 343 individual asteroids modeled in the de438 integration
and higher order lunar gravity moments, that were zero.
Using this header will only omit the asteroid mass parameters
from the binary ephemeris file; position and velocity information
will be unaffected. 

***********************************************************
In addition, this version of DE438 includes Chebyshev polynomial
coefficients fit to the integrated value of TT-TDB evaluated
at the geocenter. Older versions of software will likely ignore
these coefficients.

***********************************************************

The original ephemeris was integrated overs a slightly longer time span
to ensure that data existed before and after each 100 year ascii data block. 
Those few extra days are available in the SPICE binary SPK file, de438t.bsp.
