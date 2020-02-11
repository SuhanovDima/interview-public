SELECT a.*
FROM accounts a
inner join
  (SELECT
    tr.SOURCE_ID,
    sum(tr.AMOUNT)
  FROM transfers tr
  WHERE tr.TRANSFER_TIME >= '2019-01-01 00:00:00.000'
  GROUP BY tr.SOURCE_ID
  HAVING sum(tr.AMOUNT) > 1000) t ON a.ID = t.SOURCE_ID