# Búsqueda de alumnos
Este endpoint se encarga de buscar a los estudiantes registrados en el sistema.

```http
POST /admin/students/search
```
`Header`
```json
{
    "Authorization": "Bearer TU_TOKEN_JWT_AQUÍ"
}
```

`Params`
```json
{
    "query": "{valor de búsqueda}"
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





