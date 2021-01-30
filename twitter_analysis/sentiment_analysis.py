# --------------------------------------------
#
# The program determines the average happiness level, number of keyword tweets,
# and the total number of tweets for each region.
#
# 2020 November 17
# Jessica Liu (jliu2722)
#
# --------------------------------------------

import string

REGIONS = ['Eastern', 'Central', 'Mountain', 'Pacific']


def compute_tweet(tweet, keywords, scores):
    """
    Calculate happiness score for the tweet and update scores
    :param tweet: text in each tweet
    :param keywords: dict mapping from keyword to value
    :param scores: dict mapping from region to score
    """

    region, text = tweet

    score = scores[region] # score for this region
    has_keywords = False # when a keyword exists in a tweet, this will change to True

    # step 1. split text to words
    words = text.split()

    # step 2. loop word for each words
    for word in words:
        # 2.1 strip punctuation
        word = word.strip(string.punctuation)

        # 2.2 to lower case
        word = word.lower()

        # 2.3 exact match sentiment keywords, add score to total
        if word in keywords:
            value = keywords[word]
            score['total_happiness_score'] += value
            has_keywords = True # if a keyword exists in the tweet

    score['count_tweets'] += 1

    # if a keyword exists in the tweet, increase the count of keywords tweets
    if has_keywords:
        score['count_keyword_tweets'] += 1

def get_region(latitude, longitude):
    """
    Compute region based on latitude and longitude.
    If the latitude-longitude of a tweet is outside of all these regions, it is to be skipped;
    if a tweet is on the border between regions, then choose the first matched region.
    :param latitude: latitude of tweet
    :param longitude: longitude of tweet
    :return: None or region name - one of Eastern, Central, Mountain, Pacific
    """
    longitudes = [
        -67.444574,    # p1/2
        -87.518395,    # p3/4
        -101.998892,   # p5/6
        -115.236428,   # p7/8
        -125.242264,   # p9/10
    ]
    latitudes = [
        49.189787,     # p1/3/5/7/9
        24.660845      # p2/4/6/8/10
    ]

    # if latitude of tweet is outside of range, return none
    if latitude > latitudes[0] or latitude < latitudes[1]:
        return None

    # determine the region of the tweet based on longitude
    for i in range(len(longitudes)-1):
        if longitude <= longitudes[i] and longitude >= longitudes[i + 1]:
            return REGIONS[i]

    return None


def extract_tweet_param(line: str):
    """
    Extract latitude, longitude, and text from tweet line
    :param line: a tweet line
    :return: (latitude, longitude, text)
    """
    # format: [latitude, longitude] value date time text
    # example [41.298669629999997, -81.915329330000006] 6 2011-08-28 19:02:36 Work needs to fly by ... I'm so excited to see Spy Kids 4 with then love of my life ... ARREIC

    # find and extract latitude
    i = line.find(',')
    latitude = float(line[1:i])

    # find and extract longitude
    j = line.find(']', i)
    longitude = float(line[i+2:j])

    # find and extract text
    i = line.find(' ', j+2)
    text = line[i:]
    date_time_len = 21 # len(" 2011-08-28 19:02:36 ")
    text = line[i + date_time_len:]

    return latitude, longitude, text

def read_tweets_file(tweets_file):
    """
    Read tweets files and return list of tuples (region, text)
    :param tweets_file: the tweets data file name
    :return:
        A list of tuples, each tuple have region and text.
        If a tweets is not in regions defined, the tweet will be ignored
    """
    # step 1. open the file
    f = open(tweets_file, "r", encoding="utf-8")

    tweets = []  # this will be a list of tuples, each tuple have region
    # step 2. loop for each line
    for line in f:
        line = line.strip()
        if len(line) > 0:
            # step 2.1 get the latitude, longitude, and text from each line
            latitude, longitude, text = extract_tweet_param(line)

            # step 2.2 get region
            region = get_region(latitude, longitude)
            if region:  # when not None, it is a regular region
                tweets.append((region, text))

    return tweets


def read_keywords_file(keywords_file):
    """
    Read keywords file, create a mapping from keyword to value
    :param keywords_file: the keywords file name
    :return: a dict mapping from keyword to value
    """
    # step 1. open the file
    f = open(keywords_file, "r", encoding="utf-8")

    keywords = {} # this will be a dict mapping from keyword to value
    # step 2. loop for each line
    for line in f:
        # step 2.1 get keyword, value from each line
        # format: keyword, value
        # example: amazing,10
        line = line.strip()
        # add keyword and value to keywords dictionary if line is not blank
        if len(line) > 0:
            words = line.split(",")
            keywords[words[0].strip()] = int(words[1])

    return keywords


def compute_tweets(tweets_file, keywords_file):
    """
    Compute happiness score for all regions
    :param tweets_file: the tweets data file name
    :param keywords_file: the keywords file name
    :return:
        A list of tuples in order of Eastern, Central, Mountain, Pacific.
        Each tuple contain three values: (average, count_of_keyword_tweets, count_of_tweets)
        If there is an exception from a file name that does not exist, then an empty list should be returned.
    """
    try:
        tweets = read_tweets_file(tweets_file)
        keywords = read_keywords_file(keywords_file)
    except Exception as ex:
        # if file not exist or fail to read, return an empty list
        print(ex)
        return []

    scores = {}
    # initialize scores dictionary
    for region in REGIONS:
        scores[region] = {
            'total_happiness_score':  0,
            'count_tweets': 0,
            'count_keyword_tweets': 0
        }

    # determine scores for each tweet
    for tweet in tweets:
        compute_tweet(tweet, keywords, scores)

    results = []
    # calculate average happiness score for each region and add to results list
    for region in REGIONS:
        score = scores[region]
        if score['count_keyword_tweets'] > 0:
            average = score['total_happiness_score'] / score['count_keyword_tweets']
        else:
            average = 0  # no keywords exist for the region
        # append region analyzed result tuple to final results
        results.append((average, score['count_keyword_tweets'], score['count_tweets']))

    return results

