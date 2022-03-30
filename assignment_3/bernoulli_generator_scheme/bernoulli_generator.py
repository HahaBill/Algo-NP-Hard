import numpy as np
import matplotlib.pyplot as plt
import argparse
import scipy
from scipy.stats import binom
from scipy.stats import nbinom
from scipy.stats import bernoulli
import math


def binDistr(n, p):
    return sum(bernoulli.rvs(p, size=n))


def sampleBinomial(n, p=0.5, trials=1):
    if trials == 1:
        return binDistr(n, p)
    res = []
    for _ in range(trials):
        res.append(binDistr(n, p))
    return res


def sampleNBinomial(r, p=0.5, trials=1):
    res = []
    for _ in range(trials):
        fails = 0
        successes = 0
        while fails < r:
            x = bernoulli.rvs(p)
            successes += x
            fails += 1 - x
        res.append(successes)

    if trials > 1:
        res
    elif trials < 1:
        print("OKAY")
        return None
    else:
        return res[0]


class BernoulliGenerator:
    def __init__(self, args):
        self.n_trials = args.n_trials
        self.probability = args.probability
        self.r = args.r

    def gameOfFlippingCoin(self):
        p = 0.5
        N = 1000
        result = []

        S_n = 0
        for n in range(N+1):
            value_rv = bernoulli.rvs(0.5, size=1)[0]
            if value_rv == 0:
                value_rv = -1
            S_n += value_rv

            X_n = (1/math.sqrt(N)) * S_n

            # print(X_n)
            result.append(X_n)

        # print(result)

        r = list(range(n+1))
        print(r)
        print(len(result))
        plt.bar(r, result)
        plt.savefig('gameFlippingCoin_3.png')
        plt.show()

        # nbinomial_pmf = nbinom.pmf(x, r, p)
        # plt.plot(x, nbinomial_pmf, color='orange')

        # dist = [nbinom.pmf(i, r, p) for i in x]
        # plt.bar(x, dist, color='orange')
        # plt.xlabel('Number of Success')
        # plt.ylabel('Probability ')


# 1.b
def check_lln(n, p):
    S = sampleBinomial(n, p, trials=5000)
    ks = [0 for _ in range(n + 1)]
    for s in S:
        ks[s] += 1

    freqs = [count / 5000 for count in ks]

    return freqs


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
    # bernoulli_scheme_generator.sampleBinomial()

    # N = 1000
    # P = 0.5

    # n = 10000
    # r = 30

    # binomSample = sampleBinomial(N, P, n)
    # # negativeBinomSample = sampleNBinomial(r, P, n)

    # x = np.random.binomial(N, P, n)
    # count, bins, ignored = plt.hist(
    #     x, bins="auto", density=True, color='orange')
    # plt.plot(bins, scipy.special.comb(N, bins) * (P ** bins)
    #          * ((1 - P) ** (N - bins)), linewidth=2, color='red')
    # plt.title(f"Binomial distribution (p={P}, n={n})")
    # plt.savefig('binomial' + str(P) + '_' +
    #             str(n) + '.png')
    # plt.show()

    # s = np.random.negative_binomial(r, P, n)
    # #x = np.random.binomial(N, P, n)
    # count, bins, ignored = plt.hist(
    #     s, bins="auto", density=True, color='green')

    # plt.plot(bins, scipy.special.comb(bins + r - 1, bins) * (P ** r)
    #          * ((1 - P) ** (bins)), linewidth=2, color='blue')

    # plt.title(f"Negative Binomial distribution (p={P}, r={r})")
    # plt.savefig('negative_binomial_lln' + str(P) + '_' +
    #             str(r) + '.png')

    # plt.show()

    P = 0.8

    s = check_lln(5000, P)
    x = range(0, len(s))
    plt.plot(x, s, color='black')

    plt.title(f"Law of Large numbers for frequencies (p={P}, n={5000})")
    plt.savefig('lln_frequencies' + str(P) + '_' +
                str(5000) + '.png')

    plt.show()

    # bernoulli_scheme_generator.negativeBinomialDistribution()
    # bernoulli_scheme_generator.gameOfFlippingCoin()
