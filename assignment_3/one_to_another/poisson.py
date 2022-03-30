import matplotlib.pyplot as plt
import numpy as np
from numpy.random import default_rng
from math import sqrt, cos, log, pi

def samplePoisson(rate=1):
    rate = sqrt(rate)
    scale = 1 / rate
    count = 0
    sigma = 0.0

    while sigma < rate:
        x = gen.exponential(scale=scale)
        sigma += x
        count += 1

    return count

rate = 1
ys = checkDistr(samplePoisson, rate)
xs = range(0, len(ys))

plt.plot(xs, ys, color='purple')
plt.title(f"Poisson distribution on a sample volume of 1000 with rate = {rate}")
#plt.savefig("poisson.png")
plt.show()