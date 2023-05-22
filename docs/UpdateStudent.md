# Actualizar estudiante
Este endpoint se encarga de actualizar la información de un estudiante.

```http
PUT /admin/student/update/{studentCode}
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
    "newDirection": "",
    "newLevel": "",
    "newGrade": 0
}

```

`Response`
```json
{
    "status": 200,
    "message": "OK"
}
```    





