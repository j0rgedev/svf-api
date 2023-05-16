# Lista de estudiantes
Este endpoint se encarga de listar todos los estudiantes registrados en el sistema.

```http
POST /admin/students
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
            "birthday": "",
            "isEnrolled": ""
        }
    ]
}
```