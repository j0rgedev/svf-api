# Gráficos principales
Este endpoint se encarga de devolver los gráficos principales de la página de administrador.
```htt
POST /admin/general-dashboard
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
    "paidPensionsCount": [
      {
        "month": "",
        "count": 0
      }
    ],
    "lastEnrolledStudents": [
      {
        "studentCod": "",
        "fullName": "",
        "level": ""
      }
    ],
    "enrollmentCount": {
      "totalStudents": 0,
      "enrolled": 0,
      "notEnrolled": 0
    }
  }
}
```    





