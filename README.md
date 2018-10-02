# precision-problem
The purpose of this repository is to provided code that shows that the "Precision.round" method in the org.apache.commons/commons-math3 libarary (version 3.5) as implemented can generate an incorrect result over and above the basic inaccuracy of Base-2 floating point representations (in this case Doubles).

Background:<br>
Floating point representations in computers are a trade off between performance and accuracy (resolution and number base).  In addition they were designed for a particular domain, to support fast scientific computations where the numbers are very large or very small, but do not need to be 100% accurate (When dealing with interstellar distances you don't worry about accuracy to the Meter).  In this domain the fact that the number base used (base 2) is fundamentally incapable of representing 0.1 (in base 10) is not really a problem, and the limited resolution is also not really a problem (since many numbers can not be represented exactly due to number base) as it just introduces a little more inexactness.

However, when the domain is business centric, like finance, the inherent inexactness (of computer floating point numbers) is usually inappropriate (e.g. Currency calculations and representations is especially inappropriate).

This repository exists to show that the org.apache.commons.math3.util.Precision.round(double, ...) methods contain an error in their use of Double.toString(...) to populate the BigDecimal (which is used to perform the rounding).  The problem is that Double.toString(...) does its own rounding which then can cause the BigDecimal's rounding to produce the incorrect result.
