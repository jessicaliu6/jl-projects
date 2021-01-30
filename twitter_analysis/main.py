# --------------------------------------------
#
# The program prompts the user to input the name of a file containing sentiment keywords and the name of the
# file containing tweets, validates the file, and prints the average happiness level, the total
# number of tweets, and the number of tweets with keywords in sorted order.
#
# 2020 November 17
# Jessica Liu (jliu2722)
#
# --------------------------------------------

# import compute_tweets function and REGIONS from sentiment_analysis.py
from assignment3.sentiment_analysis import compute_tweets, REGIONS

if __name__ == '__main__':

    keywords_file = input("Please enter the name of the file containing the keywords: ") # get keyword file name
    tweets_file = input("Please enter the name of the file containing the tweets: ") # get tweets file name

    results = compute_tweets(tweets_file, keywords_file)

    # print average happiness level, number of keyword tweets, and the number of total tweets
    # for each region in sorted order
    for i in range(len(results)):
        result = results[i]
        region = REGIONS[i]
        print("\nFor the", region, "timezone...")
        print("The average 'happiness level' is:", result[0])
        print("The number of tweets with the keyword are:", result[1])
        print("The number of total tweets are:", result[2])
