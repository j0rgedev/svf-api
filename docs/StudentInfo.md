### Información del estudiante
Este endpoint es usado para obtener la información del estudiante.

```http
POST /enrollment/student-info
```
`Header`
```json
{
  "Authorization": "Bearer TU_TOKEN_JWT_AQUÍ"
}
```

`Response (alumno no matrículado)`
```json
{
  "studentCode": "",
  "names": "",
  "lastNames": "",
  "dni": "",
  "newLevel": "",
  "newGrade": ""
}
```

`Response (alumno matrículado)`
```json
{
  "studentCode": "",
  "names": "",
  "lastNames": "",
  "enrollmentCode": ""
}
```