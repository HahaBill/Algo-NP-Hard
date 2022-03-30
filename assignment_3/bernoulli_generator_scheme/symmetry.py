import math
from statsmodels.distributions.empirical_distribution import ECDF
import numpy as np
import matplotlib.pyplot as plt
from scipy.stats import bernoulli

# run for 31 iterations


def sampleCantor():
    x = 0
    for (n, x_n) in enumerate(bernoulli.rvs(1/2, size=31), start=1):
        x += x_n / (3 ** n)

    return 2 * x


# plot on a range from 0 to 1
def checkCantor():
    samples = []
    for _ in range(1000):
        samples.append(sampleCantor())

    return samples


def binary_to_ternary(x):
    y = 0
    x = round(2**52 * x)

    for i in range(1, 52):
        y = y + 2*(x % 2)
        y = y/3
        x = math.floor(x/2)
    return y


# print(binary_to_ternary(0.9))

# n = 1000

# uniform_sample = np.random.uniform(0, 1, n)
# cantor_mapped = np.array([binary_to_ternary(i) for i in uniform_sample])

# x = np.sort(cantor_mapped)
# y = np.arange(1, n+1)/n

# plt.plot(x, y, color='purple')
# plt.title(f"Cantor distribution on a sample volume of 1000")
# plt.savefig('cantor_distribution.png')

# plt.show()

n = 1000
s = checkCantor()
cantor_mapped = np.array([binary_to_ternary(i) for i in s])
x = np.sort(cantor_mapped)
y = np.arange(1, n+1)/n

# plt.plot(x, y, color='purple')
# plt.title(f"Cantor distribution on a sample volume of 1000")

plt.plot(s, y, color='black')
plt.show()
