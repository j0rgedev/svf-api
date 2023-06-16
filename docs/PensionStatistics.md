# Gráficos de estadísticas de pensiones
Este endpoint se encarga de devolver los datos necesarios para mostrar los gráficos de estadísticas de pensiones en la intranet del administrador.

```http
POST /admin/pension-dashboard
```

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
    "monthPensionsAmount": [
      {
        "month": "ene",
        "amount": 0
      },
      {
        "month": "feb",
        "amount": 0
      },
      {
        "month": "mar",
        "amount": 0
      },
      {
        "month": "abr",
        "amount": 0
      },
      {
        "month": "may",
        "amount": 0
      },
      {
        "month": "jun",
        "amount": 0
      },
      {
        "month": "jul",
        "amount": 0
      },
      {
        "month": "ago",
        "amount": 0
      },
      {
        "month": "sep",
        "amount": 0
      },
      {
        "month": "oct",
        "amount": 0
      },
      {
        "month": "nov",
        "amount": 0
      },
      {
        "month": "dic",
        "amount": 0
      }
    ],
    "studentsPaymentStatus": {
      "totalStudentsWithPayment": 0,
      "totalStudentsWithoutPayment": 0
    }
  }
}
```    





