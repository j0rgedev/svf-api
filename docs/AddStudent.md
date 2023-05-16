# Agregar Estudiante
Este endpoint se encarga de agregar un nuevo estudiante a la base de datos.

```http
POST /admin/student/add
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
    "studentInformation": {
        "names": "",
        "lastNames": "",
        "dni": "",
        "birthday": "",
        "phoneNumber": "",
        "currentLevel": "",
        "currentGrade": ""
    },
    "representativeInformation": {
        "names": "",
        "lastNames": "",
        "dni": "",
        "birthday": "",
        "phoneNumber": "",
        "direction": "",
        "email": "",
        "relationship": ""
    }
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
            "defaultPassword": ""
        }
    ]
}
```    





