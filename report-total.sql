select substr(datetime,1,10) as d, sum(total)
from sales sa

group by substr(datetime, 1, 10)

order by d desc;