import _multiprocessing
_multiprocessing.sem_unlink = None

import numpy
from scipy.signal import butter
from scipy.signal import filtfilt
from numpy import argmax
from numpy import unique
from numpy import greater
from scipy.signal import argrelextrema


LOWER_CUTOFF = 0.5
UPPER_CUTOFF = 5.0
SAMPLE_RATE = 100


def load_data_from_txt(file_dir):
    txt_file = open(file_dir)
    data = txt_file.readline()
    data = data.split('[')[1]
    data = data.split(']')[0]
    li = data.split(', ')
    waveform = numpy.array(li, dtype=float)
    l = len(waveform)
    time_stamp = numpy.linspace(0, 1 / SAMPLE_RATE * l, l)
    return time_stamp, waveform


def get_cut_loc(filt_d, fs, plot=False):
    """
    - filt_d: signal (N, )
    - HN: num. of frequency multiplication signal
    - fs: sample rate
    - u: self-adaptive parameter
    """
    if len(filt_d) == 0:
        return
    filt_base_wave = butterworth_bandpass_filtrate(4, filt_d, fs, LOWER_CUTOFF, 2)
    # make reference signal
    x_ema = argrelextrema(filt_base_wave, greater)[0]
    LL = int(SAMPLE_RATE / 6)
    for i in range(x_ema.shape[0]):
        xl = max(0, x_ema[i] - LL * 2)
        xr = min(x_ema[i] + LL, len(filt_d))
        while filt_d[x_ema[i]] < max(filt_d[xl: xr]):
            x_ema[i] = argmax(filt_d[xl: xr]) + xl
            xl = max(0, x_ema[i] - LL * 2)
            xr = min(x_ema[i] + LL, len(filt_d))
    return unique(x_ema)


def cut_waveform(wave, cut_loc, start_index=10, n_waves=1):
    filt_waves = []
    i = start_index
    while i + n_waves < len(cut_loc):
        filt_waves.append(wave[cut_loc[i]:cut_loc[i + n_waves]])
        i += n_waves
    return filt_waves


def butterworth_bandpass_filtrate(order, time_series, sample_rate, lower_cutoff, upper_cutoff):
    '''
    IIR Filter: Butterworth Filter
    '''
    b, a = butter(order, Wn=[lower_cutoff / (sample_rate / 2), upper_cutoff / (sample_rate / 2)], btype='bandpass')
    return filtfilt(b, a, time_series)


def examine(ppg_path, lower_rate, upper_rate):

    try:
        upper_band = (60 / int(lower_rate)) * SAMPLE_RATE
        lower_band = (60 / int(upper_rate)) * SAMPLE_RATE
        _, waveform = load_data_from_txt(ppg_path)

        filt_wave = butterworth_bandpass_filtrate(4, waveform, SAMPLE_RATE, LOWER_CUTOFF, UPPER_CUTOFF)

        cut_points = get_cut_loc(filt_wave, SAMPLE_RATE)
        filt_waves = cut_waveform(filt_wave, cut_points, start_index=5, n_waves=1)

        # plt.plot(filt_wave)
        # plt.show()
        valid_waves = 0

        for temp in filt_waves:
            if lower_band < len(temp) < upper_band:
                valid_waves += 1

        return valid_waves
    except Exception:
        # print(Exception.args)
        return 0


def examine_acc(acc_path, lower_band, upper_band):
    try:
        # lower_band = (int)lower_band
        # upper_band = (int)upper_band
        _, waveform = load_data_from_txt(acc_path)
        waveform = waveform[5*SAMPLE_RATE:-5*SAMPLE_RATE]
        filt_wave = butterworth_bandpass_filtrate(4, waveform, SAMPLE_RATE, LOWER_CUTOFF, UPPER_CUTOFF)
        # cut_points = get_cut_loc(filt_wave, SAMPLE_RATE)
        # filt_waves = cut_waveform(filt_wave, cut_points, start_index=5, n_waves=1)
        # plt.plot(filt_wave)
        # plt.show()
        valid_points = 0
        for point in filt_wave:
            if int(lower_band) <= point <= int(upper_band):
                valid_points += 1
        percent=(1-valid_points/float(len(filt_wave)))*100*1000
        return int(percent)
    except Exception:
        #print(Exception.with_traceback())
        return 100

# l = examine('/Users/yixun/Tsinghua-IPSC/ppg.txt', 60, 80)
# print(l)
path="/Users/yixun/decoded/20200916173748_20_213_369/20200916173748_20_213_369_2_60-80_accy_00.txt"
dir = "/Users/yixun/decoded/"

p=examine_acc(path, -10, 10)
print(str(p/1000)+"%")
