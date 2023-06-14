### Detalles de la matrícula
Este endpoint se utiliza para obtener los detalles de la matrícula del presente año escolar.  
Debido a que este endpoint no requiere autenticación, se puede consumir directamente desde el navegador.

```http
POST /student/pensions
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
    "totalDebt": 0.0,
    "pensions": [
      {
        "pensionCod": 0,
        "pensionName": "",
        "pensionAmount": 0.0,
        "pensionDueDate": "",
        "pensionStatus": ""
      }
    ]
  }
}
```