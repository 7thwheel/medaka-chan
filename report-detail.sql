select substr(datetime,1,10) as d, si.suppliercode, su.name, sum(price * quantity)
from sales sa

inner join saleitems si
on si.saleid = sa.id

inner join suppliers su
on si.suppliercode = su.suppliercode

where d = ?

group by substr(datetime, 1, 10), si.suppliercode;