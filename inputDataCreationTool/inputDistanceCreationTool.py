#! /usr/bin/env python
from numpy import random
import os
import argparse

class options():
    def __init__(self):
        parser = argparse.ArgumentParser(description='writes a file consisting of distance measurements with a gaussian distributed erro')
        parser.add_argument('--distance', '-d', dest='distance', required=True, type=float,
        help='True distance that error will be added to')
        parser.add_argument('--distance2', '-d2', dest='distance2', required=False, type=float,
        default=-1.0, help='True distance in the second dimension')
        parser.add_argument('--uncertainty', '-u', dest='uncertainty', required=True, type=float,
        help='value for the random error sigma')
        parser.add_argument('--outputFile', '-o', dest='outputFile', type=str,
        default="distance.txt", help='location that the values will be written')
        parser.add_argument('--measNumber', '-n', dest='measNumber', type=int,
        default=5, help='Number of measurements that will be calculated and written')
        parser.add_argument('--velocity', '-v', dest='velocity', type=float,
        default=0.0, help='input velocity, defaults to 0.0 (stationary object)')
        parser.add_argument('--velocity2', '-v2', dest='velocity2', type=float,
        default=0.0, help='input velocity for second dimension, defaults to 0.0 (stationary object)')
        self.args = parser.parse_args()


def main():
    inputArgs = options()
    with open(inputArgs.args.outputFile, 'w') as outFile:
        mu = 0.0
        sigma = inputArgs.args.uncertainty
        numberOfMeasurements = inputArgs.args.measNumber
        randomErrors = random.normal(mu, sigma, numberOfMeasurements)
        outFile.write("#uncertainty time measurement\n")
        time = 0.0
        deltaTime = 1.0
        velocity = inputArgs.args.velocity
        velocity2 = inputArgs.args.velocity2
        for randomError in randomErrors:
            measurement = inputArgs.args.distance + randomError
            print("measurement: %s" %measurement)
            if(abs(inputArgs.args.distance  + 1.0) > .01):
                measurement2 = inputArgs.args.distance2 + randomError
                outFile.write("%s   %s   %s   %s\n" %( sigma, time, measurement, measurement2))
            else:
                outFile.write("%s   %s   %s\n" %( sigma, time, measurement))

            # update time
            time += deltaTime

            # update position
            inputArgs.args.distance = inputArgs.args.distance + velocity * deltaTime
            if(abs(inputArgs.args.distance  + 1.0) > .01):
                inputArgs.args.distance2 = inputArgs.args.distance2 + velocity2 * deltaTime


if __name__ == "__main__":
    main()
