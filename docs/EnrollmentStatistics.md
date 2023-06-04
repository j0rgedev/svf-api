# Gráficos de estadísticas de matrícula
Este endpoint se encarga de devolver los datos necesarios para mostrar los gráficos de estadísticas de matrícula en la intranet del administrador.
```htt
POST /admin/dashboard2
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
    "enrolledStudents": [
      {
        "boysCount": "",
        "girlsCount": "",
        "totalCount": ""
      }
    ],
    "enrollmentByYear": {
      "2021": {
        "inicial": 0,
        "primaria": 0,
        "secundaria": 0
      },
      "2022": {
        "inicial": 0,
        "primaria": 0,
        "secundaria": 0
      },
      "2023": {
        "inicial": 0,
        "primaria": 0,
        "secundaria": 0
      }
    },
    "enrollmentByLevelAndGrade": {
      "inicial": {
        "3": 0,
        "4": 0,
        "5": 0
      },
      "primaria": {
        "1": 0,
        "2": 0,
        "3": 0,
        "4": 0,
        "5": 0,
        "6": 0
      },
      "secundaria": {
        "1": 0,
        "2": 0,
        "3": 0,
        "4": 0,
        "5": 0
      }
    }
  }
}
```    





