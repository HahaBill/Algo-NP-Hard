import matplotlib.pyplot as plt
import numpy as np
from numpy.random import default_rng
from math import sqrt, cos, log, pi

# random generator used in this notebook
gen = default_rng()

def checkDistrCont(distr, vol=1000, bins=32, *args):
    samples = []
    for n in range(vol):
        samples.append(distr(*args))

    ys, edges = list(np.histogram(samples, bins=bins))
    xs = np.linspace(min(samples), max(samples), len(ys))
    
    return xs, ys, samples

def checkDistrDisc(distr, *args):
    samples = []
    for i in range(1, 1001):
        samples.append(distr(*args))

    res = [0 for _ in range(1 + max(samples))]
    for n in samples:
        res[n] += 1
    return res