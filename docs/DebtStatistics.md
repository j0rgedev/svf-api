# Gráficos de estadísticas del monto de deuda por mes
Este endpoint se encarga de devolver los datos necesarios para generar los gráficos de estadísticas del monto de deuda por mes.

```http
POST /admin/total-debt/{month}
```

`Path Variables`

| Parameter | Type  | Description                                |
|:----------|:------|:-------------------------------------------|
| `month`   | `int` | **Requerido**. Número del mes entre 3 y 12 |


`Header`
```json
{
  "Authorization": "Bearer TU_TOKEN_JWT_AQUÍ"
}
```

`Response`
```json
{
  "status": 200,
  "message": "OK",
  "data": {
    "totalDebt": 0.0,
    "totalPaid": 0.0,
    "totalPending": 0.0
  }
}
```    





