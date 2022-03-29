import matplotlib.pyplot as plt
import numpy as np

mu, sigma = 0, 0.1  # mean and standard deviation
n = 10000
s = np.random.normal(mu, sigma, n)
count, bins, ignored = plt.hist(s, 50, density=True)
plt.plot(bins, 1/(sigma * np.sqrt(2 * np.pi)) *
         np.exp(- (bins - mu)**2 / (2 * sigma**2)),
         linewidth=2, color='red')

plt.title(
    f"Normal Distribution (\u03BC={mu}, \u03C3={sigma}, n={n})")
plt.savefig('normal_dist_' + str(mu) + '_' +
            str(sigma) + '_' + str(n) + '.png')
plt.show()
