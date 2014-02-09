select substr(datetime,1,10) as datetime, sum(total) as total
from sales sa

where substr(datetime,1,7) = ?

group by substr(datetime, 1, 10)

order by datetime desc;