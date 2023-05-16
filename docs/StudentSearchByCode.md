# Búsqueda de alumnos por su código
Este endpoint se encarga de buscar un alumno por su código.

```http
POST /admin/students/search/{studentCode}
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
    "data": [
        {
            "studentCode": "",
            "fullName": "",
            "dni": "",
            "birthday": "",
            "direction": "",
            "phone": "",
            "level": "",
            "grade": "",
            "isEnrolled": "",
            "representativesCode": ""
        }
    ]
}
```    





