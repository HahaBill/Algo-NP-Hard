import numpy as np
import matplotlib.pyplot as plt
import argparse
from scipy.stats import binom
from scipy.stats import nbinom
from scipy.stats import bernoulli
import math


class BernoulliGenerator:

    def __init__(self, args):
        self.n_trials = args.n_trials
        self.probability = args.probability
        self.r = args.r

    def binomialDistribution(self):
        n = self.n_trials
        p = self.probability
        x = np.arange(0, n + 1)

        def factorial(n):
            x = 1
            for i in range(1, n+1):
                x *= i
            return x

        def combination(n, k):
            return factorial(n)/(factorial(k)*factorial(n-k))

        def binompmf(k, n, p):
            return combination(n, k)*(p**k)*((1-p)**(n-k))

        r = list(range(n + 1))
        dist = [binompmf(k, n, p) for k in r]

        print(dist)

        plt.bar(r, dist)
        # #plt.plot(x, binompmf(x, n, p), color='blue')
        plt.show()

        # # binomial_pmf = binom.pmf(x, n, p)
        # # print(binomial_pmf)
        # # print(sum(binomial_pmf))
        # # plt.plot(x, binomial_pmf, color='blue')

        # # dist = [binom.pmf(r, n, p) for r in x]
        # # plt.bar(x, dist)
        # plt.xlabel('Number of Success')
        # plt.ylabel('Probability ')

        # plt.title(
        #     f"Binomial Distribution (n={n}, p={p})")
        # # plt.savefig('binom' + str(n) + "_" + str(p) + '.png')
        # plt.show()

    def negativeBinomialDistribution(self):
        n = self.n_trials
        p = self.probability
        x = np.arange(0, n + 1)
        r = self.r

        def factorial(n):
            x = 1
            for i in range(1, n+1):
                x *= i
            return x

        def combination(n, k):
            return factorial(n)/(factorial(k)*factorial(n-k))

        def binompmf(k, n, p):
            return combination(n, k)*(p**k)*((1-p)**(n-k))

        r = list(range(n + 1))
        dist = [binompmf(k, n, p) for k in r]

        plt.bar(r, dist)
        plt.plot(x, binompmf(x, n, p), color='blue')
        plt.show()

        # nbinomial_pmf = nbinom.pmf(x, r, p)
        # plt.plot(x, nbinomial_pmf, color='orange')

        # dist = [nbinom.pmf(i, r, p) for i in x]
        # plt.bar(x, dist, color='orange')
        # plt.xlabel('Number of Success')
        # plt.ylabel('Probability ')

        plt.title(
            f"Negative Binomial Distribution (n={n}, p={p}, r={r})")
        # plt.savefig('negativebinom' + "_" + str(n) +
        #             "_" + str(p) + "_" + str(r) + '.png')
        plt.show()

    def gameOfFlippingCoin(self):
        p = 0.5
        N = 1000
        result = []

        S_n = 0
        for n in range(N):
            value_rv = bernoulli.rvs(0.5, size=1)[0]
            if value_rv == 0:
                value_rv = -1
            S_n += value_rv
            X_n = (1/N) * S_n
            # print(X_n)
            result.append(X_n)

        print(result)

        r = list(range(N))
        plt.bar(r, result)
        plt.savefig('gameFlippingCoin_2.png')
        plt.show()

        # nbinomial_pmf = nbinom.pmf(x, r, p)
        # plt.plot(x, nbinomial_pmf, color='orange')

        # dist = [nbinom.pmf(i, r, p) for i in x]
        # plt.bar(x, dist, color='orange')
        # plt.xlabel('Number of Success')
        # plt.ylabel('Probability ')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='''
    Script to create generators
    ''')
    parser.add_argument('--n_trials', type=int, default=30, required=False,
                        help='input "n" trials')
    parser.add_argument('--probability', type=int, default=0.8, required=False,
                        help='input probability')
    parser.add_argument('--r', type=int, default=1000, required=False,
                        help='input "r" successes/failures')

    args = parser.parse_args()

    bernoulli_scheme_generator = BernoulliGenerator(args)
    # bernoulli_scheme_generator.binomialDistribution()
    # bernoulli_scheme_generator.negativeBinomialDistribution()
    bernoulli_scheme_generator.gameOfFlippingCoin()
