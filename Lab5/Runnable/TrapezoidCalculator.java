package Runnable;

public class TrapezoidCalculator implements Runnable {
    private double a;
    private double b;
    private double eps;
    private double result;

    public TrapezoidCalculator(double a, double b, double eps) {
        this.a = a;
        this.b = b;
        this.eps = eps;
    }

    @Override
    public void run() {
        result = trapezoidMethod(a, b, eps);
    }

    private double trapezoidMethod(double a, double b, double eps) {
        double sum = 0.0;
        double currentX = a;

        while (currentX < b) {
            double nextX = (currentX + eps < b) ? currentX + eps : b;
            sum += (f(currentX) + f(nextX)) * (nextX - currentX) / 2;
            currentX = nextX;
        }

        return sum;
    }

    private double f(double x) {
        return 1 / Math.log(x);
    }

    public double getResult() {
        return result;
    }
}
