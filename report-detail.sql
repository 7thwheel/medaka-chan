select substr(datetime,1,10) as date, si.suppliercode, su.name, sum(price * quantity) as amount
from sales sa

inner join saleitems si
on si.saleid = sa.id

inner join suppliers su
on si.suppliercode = su.suppliercode

where date = ?

group by substr(datetime, 1, 10), si.suppliercode;