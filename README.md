# Project Title

Program for reading a measurement file containing measured distance and measurement uncertainty (1 sigma standard deviation), time, and determining the position using a kalman filter

## Getting Started

compile code
```
cd KalmanFilter
javac KalmanFilterClient.java
```
Run Code
```
# example created with the input Data cration tool using 2 dimensions with a dynamic x component (constant velocity of 5) and stationary y component
java KalmanFilterClient -i ../inputDataCreationTool/2dVelocity5For50.txt
```

### Prerequisites

Java version 1.8

[ASCII-DATA 1.4.0](https://github.com/MitchTalmadge/ASCII-Data)

## Authors

* **Cory W. Mauer**

