package Integral;

public class RecIntegral {
  private double lowerBound;
  private double upperBound;
  private double step;
  private double result;

    public RecIntegral(double lowerBound, double upperBound, double step) {
      this.lowerBound = lowerBound;
      this.upperBound = upperBound;
      this.step = step;
    }

    public void setResult(double result) {
      this.result = result;
    }

    public double getLowerBound () {
      return this.lowerBound;
    }

    public double getUpperBound () {
      return this.upperBound;
    }

    public double getStep() {
      return this.step;
    }

    public double getResult () {
      return this.result;
    }
}
