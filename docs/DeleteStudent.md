# Eliminar un estudiante
Este endpoint se encarga de eliminar un estudiante de la base de datos.

```http
POST /admin/student/delete/{studentCode}
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
    "message": "OK"
}
```    





