package com.altoros.floatingpoint;

import java.math.BigDecimal;

import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Floating point representations in computers are a trade off between performance and accuracy (resolution and number base).
 * <p>
 * In addition they were designed for a particular domain, to support fast scientific computations where the numbers are
 * very large or very small, but do not need to be 100% accurate (When dealing with interstellar distances you don't
 * worry about accuracy to the Meter).  In this domain the fact that the number base used (base 2) is fundamentally incapable
 * of representing 0.1 (in base 10) is not really a problem, and the limited resolution is also not really a problem (since
 * many numbers can not be represented exactly due to number base) as it just introduces a little more inexactness.
 * <p>
 * However, when the domain is business centric, like finance, the inherent inexactness (of computer floating point numbers)
 * is usually inappropriate (e.g. Currency calculations and representations is especially inappropriate).
 * <p>
 * This class exists to validate that the org.apache.commons.math3.util.Precision.round(double, ...) methods contain an error in
 * their use of Double.toString(...) to populate the BigDecimal (which is used to perform the rounding).  The problem is that
 * Double.toString(...) does its own rounding which then can cause the BigDecimal's rounding to produce the incorrect result.
 */
@SuppressWarnings("unused")
public class PrecisionProblemValidate {
  @Test
  public void test_Precision() {
    for ( int i = 41; i <= 50; i++ ) {
      checkPrecision( "5.72", i );
    }
    for ( int i = 51; i <= 59; i++ ) {
      checkPrecision( "5.72", i, "5.73" );
    }
    for ( int i = 41; i <= 49; i++ ) {
      checkPrecision( "5.73", i );
    }
    for ( int i = 50; i <= 59; i++ ) {
      checkPrecision( "5.73", i, "5.74" );
    }
    checkPrecision( "5.99", 5, "6.0" ); // Not what I would have expected from Double.toString( ... )

    checkPrecision( "200000000000000.05", 44, "2.0000000000000006E14" ); // Not enough bits
  }

  private static void checkPrecision( String pExpected, int pToLoose ) {
    checkPrecision( pExpected, pToLoose, pExpected );
  }

  private static void checkPrecision( String pPrefix, int pToRound, String pExpected ) {
    String zStringOrigDecimal = pPrefix + pToRound;
    double zOrigDouble = Double.parseDouble( zStringOrigDecimal );
    double zNewDouble = Precision.round( zOrigDouble, 2, BigDecimal.ROUND_HALF_EVEN );
    String zStringNewDecimal = Double.toString( zNewDouble );

    if ( !pExpected.equals( zStringNewDecimal ) ) {
      System.out.println( "Precision : " + zStringOrigDecimal + " (" + zOrigDouble + ")" +
                          "       -> : " + zStringNewDecimal + " (" + zNewDouble + ")" );
      Assert.assertEquals( zStringOrigDecimal, pExpected, zStringNewDecimal );
    }
  }
}
