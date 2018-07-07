package ax.math.infinite;

import java.math.BigInteger;

public class IFraction {

    private final BigInteger numerator;
    private final BigInteger denominator;
    private final boolean sign;

    public final static IFraction ZERO = new IFraction(0);
    public final static IFraction ONE = new IFraction(1);
    public final static IFraction TWO = new IFraction(2);
    public final static IFraction FIVE = new IFraction(5);
    public final static IFraction TEN = new IFraction(10);

    private IFraction(BigInteger numerator, BigInteger denominator, boolean checkGCD) {
        sign = IMathUtils.isFractionNegative(numerator, denominator);
        BigInteger gcd = IMathUtils.ONE;
        if (checkGCD)
            gcd = numerator.gcd(denominator);
        this.numerator = numerator.divide(gcd).abs();
        this.denominator = denominator.divide(gcd).abs();
    }

    public IFraction(BigInteger numerator, BigInteger denominator) {
        this(numerator, denominator, true);
    }

    public IFraction(BigInteger numerator) {
        this(numerator, IMathUtils.ONE);
    }

    public IFraction(String numerator) {
        this(new BigInteger(numerator + ""));
    }

    public IFraction(long numerator, long denominator) {
        this(new BigInteger(numerator + ""), new BigInteger(denominator + ""));
    }

    public IFraction(double value) {
        String s = value + "";
        int ex = s.indexOf(".");
        s = s.replace(".", "");
        ex = s.length() - ex;
        BigInteger numerator = new BigInteger(s);
        BigInteger denominator = IMathUtils.TEN.pow(ex);
        sign = IMathUtils.isFractionNegative(numerator, denominator);
        BigInteger gcd = numerator.gcd(denominator);
        this.numerator = numerator.divide(gcd).abs();
        this.denominator = denominator.divide(gcd).abs();
    }

    public BigInteger getNumerator() {
        return numerator;
    }

    public BigInteger getDenominator() {
        return denominator;
    }

    public int getSign() {
        return sign ? -1 : 1;
    }

    public boolean isNegative() {
        return sign;
    }

    public double getValue() {
        return getSign() * numerator.doubleValue() / denominator.doubleValue();
    }

    public IFraction negate() {
        return new IFraction(sign ? numerator : numerator.negate(), denominator, false);
    }

    public IFraction reciprocal() {
        return new IFraction(sign ? denominator.negate() : denominator, numerator, false);
    }

    public IFraction add(BigInteger a) {
        return new IFraction((sign ? numerator.negate() : numerator).add(a.multiply(denominator)), denominator);
    }

    public IFraction subtract(BigInteger a) {
        return add(a.negate());
    }

    public IFraction multiply(BigInteger a) {
        return new IFraction((sign ? numerator.negate() : numerator).multiply(a), denominator);
    }

    public IFraction divide(BigInteger a) {
        return new IFraction(numerator, denominator.multiply(a));
    }

    public IFraction add(IFraction f) {
        BigInteger num = (sign ? numerator.negate() : numerator).multiply(f.denominator);
        num = num.add((f.sign ? f.numerator.negate() : f.numerator).multiply(denominator));
        return new IFraction(num, denominator.multiply(f.denominator));
    }

    public IFraction subtract(IFraction f) {
        return add(f.negate());
    }

    public IFraction multiply(IFraction f) {
        return new IFraction(
                (sign ? numerator.negate() : numerator).multiply((f.sign ? f.numerator.negate() : f.numerator)),
                denominator.multiply(f.denominator));
    }

    public IFraction divide(IFraction f) {
        return multiply(f.reciprocal());
    }

    public IFraction add(double d) {
        return add(new IFraction(d));
    }

    public IFraction subtract(double d) {
        return subtract(new IFraction(d));
    }

    public IFraction multiply(double d) {
        return multiply(new IFraction(d));
    }

    public IFraction divide(double d) {
        return divide(new IFraction(d));
    }

    public IFraction intDivide() {
        return new IFraction((sign ? numerator.negate() : numerator).divide(denominator));
    }

    public IFraction intMod() {
        return new IFraction(numerator.mod(denominator));
    }

    public IFraction intFloor() {
        BigInteger d = numerator.divide(denominator);
        return new IFraction(sign ? d.negate() : d);
    }

    public IFraction intRound() {
        BigInteger d = numerator.divide(denominator);
        BigInteger m = numerator.mod(denominator);
        BigInteger s = denominator.subtract(m);
        BigInteger h = denominator.divide(IMathUtils.TWO);
        if (h.compareTo(s) >= 0) {
            d = d.add(IMathUtils.ONE);
        }
        return new IFraction(sign ? d.negate() : d);
    }

    public IFraction intCeil() {
        if (intMod().equals(IFraction.ZERO)) {
            return intFloor();
        }
        IFraction f = new IFraction((sign ? numerator.negate() : numerator).add(denominator), denominator);
        return f.intFloor();
    }

    public IFraction intPow(IFraction pow) {
        IFraction p = pow.intFloor();
        BigInteger b = p.numerator;
        if (p.sign)
            b = b.negate();
        return intPow(b);
    }

    public IFraction intPow(BigInteger pow) {
        if (pow.compareTo(IMathUtils.MAX_INT_D2) > 0) {
            return intPow(pow.divide(IMathUtils.TWO))
                    .multiply(intPow(pow.divide(IMathUtils.TWO).add(pow.mod(IMathUtils.TWO))));
        }
        return intPow(pow.intValue());
    }

    public IFraction intPow(int pow) {
        int p = Math.abs(pow);
        BigInteger n = (sign ? numerator.negate() : numerator).pow(p);
        BigInteger d = denominator.pow(p);
        if (pow < 0)
            return new IFraction(d, n);
        return new IFraction(n, d);
    }

    /**
     * May not return a precise power if pow-floor(pow) !=0
     * 
     * @return fraction raised to the given power
     */
    public IFraction getPower(IFraction pow) {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IFraction) {
            IFraction f = (IFraction) obj;
            if (!numerator.equals(f.numerator))
                return false;
            if (!denominator.equals(f.denominator))
                return false;
            if (sign ^ f.sign)
                return false;
            return true;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        String s = (getSign() == -1) ? "-" : "";
        boolean nz = numerator.equals(IMathUtils.ZERO);
        boolean dz = denominator.equals(IMathUtils.ZERO);
        if (nz && dz)
            return "U";
        if (dz)
            return s + "I";
        if (nz)
            return "0";
        if (denominator.equals(IMathUtils.ONE))
            return s + numerator + "";
        return s + numerator + "/" + denominator;
    }

}
