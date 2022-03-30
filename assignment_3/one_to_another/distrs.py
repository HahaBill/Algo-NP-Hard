import matplotlib.pyplot as plt
from numpy.random import Generator

def samplePoisson(rate=1):
    scale = 1 / rate
    count = 0
    sigma = 0.0

    while sigma < rate:
        x = Generator.exponential(scale=scale)
        sigma += x
        count += 1

    return count

def checkPoisson(rate=1):
    samples = []
    for i in range(1, 1001):
        samples.append(samplePoisson(rate))
    samples.sort()
    res = [0 for _ in range(1 + max(samples))]
    for n in samples:
        res[n] += 1
    return res

ys = range(1, 1001)
xs = checkPoisson()

plt.plot(xs, ys, color='purple')
#plt.title(f"Cantor distribution on a sample volume of 1000")
plt.savefig("poisson.png")
plt.show()