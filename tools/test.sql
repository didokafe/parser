-- Write MySQL query to find IPs that mode more than a certain number of requests for a given time period
select e.* from log_entry e where e.date >= '2017-01-01.15:00:00' AND e.date <= '2017-01-01.16:00:00' GROUP BY e.ip HAVING count(e.ip) > 200;

-- Write MySQL query to find requests made by a given IP
select * from log_entry e where e.ip = '192.168.234.82';