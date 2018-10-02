package com.altoros.floatingpoint;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.math3.util.Precision;

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
 * This class exists to show that the org.apache.commons.math3.util.Precision.round(double, ...) methods contain an error in
 * their use of Double.toString(...) to populate the BigDecimal (which is used to perform the rounding).  The problem is that
 * Double.toString(...) does its own rounding which then can cause the BigDecimal's rounding to produce the incorrect result.
 */
@SuppressWarnings("unused")
public class PrecisionProblem {
  private static final String INPUT = "10000000000000.0544"; // This number has two Double issues: it can not be represented exactly AND is on the edge of Double's resolution
  private static final String EXPECTED_ROUNDED_VALUE = "10000000000000.05";
  private static final String INCORRECT_ROUNDED_VALUE = "10000000000000.06";

  public static void main( String[] args ) {
    new PrecisionProblem().showProblem();
  }

  private void showProblem() {
    double zDouble_Input_Direct = Double.parseDouble( INPUT );

    BigDecimal zDB_Input_Direct = new BigDecimal( INPUT );
    BigDecimal zDB_Double_Input_Direct = new BigDecimal( zDouble_Input_Direct );
    BigDecimal zDB_toString_Double_Input_Direct = new BigDecimal( Double.toString( zDouble_Input_Direct ) );

    BigDecimal zDB_Rounded_Input_Direct = roundTo2DecimalsHalfEven( zDB_Input_Direct );
    BigDecimal zDB_Rounded_Double_Input_Direct = roundTo2DecimalsHalfEven( zDB_Double_Input_Direct );
    BigDecimal zDB_Rounded_toString_Double_Input_Direct = roundTo2DecimalsHalfEven( zDB_toString_Double_Input_Direct );

    double zPrecision_Rounded_Double_Input_Direct = Precision.round( zDouble_Input_Direct, 2, BigDecimal.ROUND_HALF_EVEN );

    BigDecimal zDB_Precision_Rounded_Double_Direct = new BigDecimal( zPrecision_Rounded_Double_Input_Direct );

    // Results:

    System.out.println( "Input . . . . . . . . . . . . . . . . . : " + INPUT + /* . . . . . . . . . . . . . . . . . . */ s( 104 ) + " Rounded " + EXPECTED_ROUNDED_VALUE + "  (expected)" );
    System.out.println( "BigDecimal of Input . . . . . . . . . . : " + zDB_Input_Direct + /*. . . . . . . . . . . . . */ s( 104 ) + " . . . . " + zDB_Rounded_Input_Direct );
    System.out.println( "BigDecimal of Double_Input. . . . . . . : " + zDB_Double_Input_Direct + /* . . . . . . . . . */ s( 101 ) + " . . . . " + zDB_Rounded_Double_Input_Direct );
    System.out.println( "BigDecimal of toString(Double_Input). . : " + zDB_toString_Double_Input_Direct + /*. . . . . */ s( 105 ) + " . . . . " + zDB_Rounded_toString_Double_Input_Direct + "  (What Precision.round uses)" );
    System.out.println( "Raw Double DecimalFormat. . . . . . . . : " + toDecimalString( zDouble_Input_Direct ) );
    System.out.println( "Raw Double toString . . . . . . . . . . : " + zDouble_Input_Direct );
    System.out.println();
    System.out.println( "BigDecimal of 'Precision.round()'ed . . : " + zDB_Precision_Rounded_Double_Direct );
    System.out.println( "Raw 'Precision.round()'ed DecimalFormat : " + toDecimalString( zPrecision_Rounded_Double_Input_Direct ) );
    System.out.println( "Raw 'Precision.round()'ed toString. . . : " + zPrecision_Rounded_Double_Input_Direct );
  }

  private static StringBuilder s( int pWidthMinus98 ) {
    int zWidth = Math.max( 2, pWidthMinus98 - 98 );
    StringBuilder sb = new StringBuilder( zWidth );
    if ( (zWidth & 1) == 1 ) { // Odd
      sb.append( ' ' );
      zWidth--;
    }
    for ( ; zWidth > 0; zWidth -= 2 ) {
      sb.append( " ." );
    }
    return sb;
  }

  private static BigDecimal roundTo2DecimalsHalfEven( BigDecimal pBigDecimal ) {
    return pBigDecimal.setScale( 2, BigDecimal.ROUND_HALF_EVEN );
  }

  private static String toDecimalString( double pToConvert ) {
    DecimalFormat zFormatter = new DecimalFormat();
    zFormatter.setGroupingUsed( false );
    zFormatter.setMaximumIntegerDigits( Integer.MAX_VALUE );
    zFormatter.setMaximumFractionDigits( Integer.MAX_VALUE );
    return zFormatter.format( pToConvert );
  }
}
