import matplotlib.pyplot as plt
import numpy as np
from numpy.random import default_rng
from math import sqrt, cos, log


def sampleChi(deg=1):
    return sum(gen.normal(size=deg) ** 2)
vol = 10000

xs, ys, ss = checkDistrCont(sampleChi, vol, 50, 10)


plt.plot(xs, ys, color='purple')