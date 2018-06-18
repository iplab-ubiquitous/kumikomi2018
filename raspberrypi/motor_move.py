import RPi.GPIO as GPIO
import time
import sys
import ta7291

if __name__ == "__main__":
        d = ta7291.ta7291(18, 24, 25)
        d.drive(100)
        time.sleep(10)
        d.brake()
        time.sleep(1)

        d.cleanup()