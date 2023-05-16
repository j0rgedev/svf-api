#### Proceso de la matrícula
Este endpoint se encarga de procesar la matrícula del alumno.

```http
POST /enrollment/process
```

`Header`
```json
{
    "Authorization": "Bearer TU_TOKEN_JWT_AQUÍ"
}
```

`Body`
```json
{
    "totalAmount": "",
    "date": "",
    "levelCode": "",
    "paymentCode": ""
}
```

`Response`
```json
{
  "enrollmentCode": ""
}
```