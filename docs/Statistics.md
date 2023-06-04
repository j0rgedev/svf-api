# Gráficos principales
Este endpoint se encarga de devolver los gráficos principales de la página de administrador.
```htt
POST /admin/dashboard1
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
    "lastFiveEnrolledStudents": [
      {
        "code": "",
        "fullName": "",
        "level": ""
      }
    ],
    "enrollmentInformation": {
      "totalStudents": 0,
      "enrolled": 0,
      "notEnrolled": 0
    }
  }
}
```    





