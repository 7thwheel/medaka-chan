SELECT i.ItemCode, i.Name, i.Price, s.Name, b.Name FROM Item i

INNER JOIN Bumon b ON
i.BumonCode = b.BumonCode

INNER JOIN Suppliers s ON
i.SupplierCode = s.SupplierCode

ORDER BY s.SupplierCode, b.BumonCode
;