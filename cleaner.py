#!/usr/bin/env python
# coding: utf-8

# In[1]:


import numpy as np
import pandas as pd
import datetime
import re
import sys
import os


# In[2]:
# Little thing to redefine sys.stdin as a raw stream not a BufferedReader or TextIOWrapper
#sys.stdin = os.fdopen(sys.stdin.fileno(), 'rb',0)
print('reading...', file=sys.stderr, flush=True)
input_fd = open(sys.stdin.fileno(), encoding='UTF-8', errors='replace')
raw = pd.read_csv(input_fd,
                  parse_dates=['Timestamp']
                 )


# In[3]:


# raw


# In[4]:

print('converting...', file=sys.stderr, flush=True)


# convert timestamp from US/Pacific to US/Eastern ==> add 3 hours
raw.Timestamp = raw.Timestamp.apply(lambda ts: ts.tz_localize('US/Pacific').tz_convert('US/Eastern'))


# In[5]:


def convert_interval(intv_str):
    """Assume format is <HHH:MIN:SEC.MSEC>. 
    It's easier to use a regex to parse this which allows a colon or period to separate fields.
    If data was not well formed this could cause errors.
    This converts to a decimalized <seconds.microseconds>
    """
    # check to see if we've already converted it e.g. we're re-running this
    if isinstance(intv_str, float):
        return intv_str
    _i = re.compile('[:.]').split(intv_str)
    _i = list(map(lambda x: int(x), _i))
    _dt = datetime.timedelta(hours=_i[0], minutes=_i[1], seconds=_i[2], milliseconds=_i[3])
    return _dt.total_seconds() + _dt.microseconds/1e6


# In[6]:


# Convert string interval into a seconds.microseconds object
raw.FooDuration = raw.FooDuration.apply(convert_interval)
raw.BarDuration = raw.BarDuration.apply(convert_interval)


# In[7]:


# calculate new value for TotalDuration as it is filled with garbage
raw.TotalDuration = raw.BarDuration + raw.FooDuration


# In[8]:


# wrap x in an int so we can re-run this without errors (idempotent)
raw.ZIP = raw.ZIP.apply(lambda x: f"{int(x):05d}")


# In[9]:


# convert names to uppercase
raw['FullName'] = raw['FullName'].apply(str.upper)


# In[10]:


# raw


# In[11]:


# raw.dtypes


# ## write file to stdout

# In[12]:

print(f'writing {raw.shape[0]} records....', file=sys.stderr, flush=True)

output_fd = open(sys.stdout.fileno(), 'w', encoding='UTF-8')
raw.to_csv(output_fd, index=False, date_format='%FT%H:%M:%S%z')
output_fd.close()

