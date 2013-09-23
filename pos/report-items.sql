select sa.datetime,si.*, price*quantity as total from sales sa

inner join saleitems si
on sa.id = si.saleid

where substr(sa.datetime,1,10) = ?

order by id;
