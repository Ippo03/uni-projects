from clock import *

#-------------------- Askisi 1 ----------------------------
class RomanCascadeCounter(CascadeCounter):
    """Metritis CascadeCounter me endei3eis me rwmaikous ari8mous."""
    def __str__(self):
        tens = self.value // 10
        units = self.value % 10
        tens_s = 'L' if tens == 5 else tens*'X'
        if units < 5:
            units_s = units*'I'
        else:
            units_s = 'V' + (units-5)*'I'
        sz = len(tens_s + units_s)
        return '-'*(9-sz) + tens_s + units_s


class RomanClock(Clock):
    """Roloi me endei3eis me rwmaika noumera.

    >>> c = RomanClock(23, 59, 58)
    >>> str(c)
    '----XXIII:---LVIIII:----LVIII'
    >>> c.advance()
    >>> print(c)
    ----XXIII:---LVIIII:---LVIIII
    >>> c.advance()
    >>> print(c)
    ---------:---------:---------
    >>> c.advance()
    >>> print(c)
    ---------:---------:--------I
    >>> c.advance()
    >>> print(c)
    ---------:---------:-------II
    """
    def __init__(self, h, m, s):
        self._h = RomanCascadeCounter(None, 24, h)
        self._m = RomanCascadeCounter(self._h, 60, m)
        self._s = RomanCascadeCounter(self._m, 60, s)
    

#-------------------- Askisi 2 ----------------------------
class DayCounter(CyclicCounter):
    """Metritis hmeras.

    Paradeigmata:
    >>> d = DayCounter()
    >>> str(d)
    'Sunday'
    >>> d.advance()
    >>> print(d)
    Monday
    >>> d2 = DayCounter('Saturday')
    >>> str(d2)
    'Saturday'
    >>> d2.advance()
    >>> str(d2)
    'Sunday'
    >>> d2.advance()
    >>> str(d2)
    'Monday'
    """
    _days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday',\
            'Thursday', 'Friday', 'Saturday']

    def __init__(self, day = 'Sunday'):
        index = self._days.index(day)
        super().__init__(len(self._days), index)
        
    def __str__(self):
        return self._days[int(super().__str__())]


#-------------------- Askisi 3 ----------------------------
class DayClock(Clock):
    """Roloi me endei3h hmeras.

    >>> c = DayClock(23, 59, 58, 'Sunday')
    >>> str(c)
    '23:59:58 Sunday'
    >>> c.advance()
    >>> str(c)
    '23:59:59 Sunday'
    >>> c.advance()
    >>> str(c)
    '00:00:00 Monday'
    >>> c.advance()
    >>> str(c)
    '00:00:01 Monday'

    An paralhf8ei to onoma imeras (teleytaio orisma ston kataskeyasti)
    tote pairnei timi 'Sunday', px.:
    >>> c = DayClock(6, 35, 0)
    >>> print(c)
    06:35:00 Sunday
    """
    def __init__(self, h = 0, m = 0, s = 0, day = 'Sunday'):
        self._d = DayCounter(day)
        self._h = CascadeCounter(None, 24, h)
        self._m = CascadeCounter(self._h, 60, m)
        self._s = CascadeCounter(self._m, 60, s)

    """SYMPLHRWSTE ME TON KWDIKA SAS APO KATW."""
    def __str__(self):
        if (str)(self._h) == (str)(self._m) == (str)(self._s) == '00':
           self._d.advance()
        return '{0}:{1}:{2} {3}'.format(self._h, self._m, self._s, self._d)
