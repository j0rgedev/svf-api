# SVF API

API design for SVF Enrollment System

## API Reference

## Base URL
/api/v1/student/enrollment/

### Login validation
This endpoint is used to validate the student's login credentials.

```http
POST /login/
```

`Body`
```json
{
  "studentCod": "",
  "password": ""
}
```

`Response`
```json
{
  "accessToken": ""
}
```

### Update student password
This endpoint is used to update the student's password.

```http
PUT /updatepassword/
```

`Body`
```json
{
  "studentCod": "",
  "password": ""
}
```


`Response`
```json
{
  "accessToken": ""
}
```

### Validation by SMS
This endpoint is used to verify a student's identity via SMS code.

```http
POST /smsvalidation 
```

`Header`
```json
{
  "Authorization": "Bearer YOUR_ACCESS_TOKEN_HERE"
}
```

`Body`
```json
{
  "smsCode": ""
}
```


`Response`
```json
{
  "message": ""
}
```

### Enrollment Process
This endpoint is used to start the enrollment process for a student.

```http
POST /
```
`Header`
```json
{
  "Authorization": "Bearer YOUR_ACCESS_TOKEN_HERE"
}
```


`Response`
```json
{
  "studentCod": "",
  "names": "",
  "lastNames": "",
  "dni": "",
  "newLevel": "",
  "newGrade": ""
  
}
```

### Enrollment details
This endpoint is used to retrieve the enrollment details.

```http
GET /details
```

`Response`
```json
{
  "schoolYear": "",
  "mainInfo": "",
  "terms": [
      {
          "title": "",
          "body": ""
      },
      {
          "title": "",
          "body": ""
      },
      {
          "title": "",
          "body": ""
      }
  ],
  "amounts": [
      {
          "level": "",
          "cost": ""
      },
      {
          "level": "",
          "cost": ""
      },
      {
          "level": "",
          "cost": ""
      }
  ]
}
```
#### Enrollment confirmation
This endpoint is used to confirm the student's enrollment.

```http
POST /confirmation
```

`Header`
```json
{
  "Authorization": "Bearer YOUR_ACCESS_TOKEN_HERE"
}
```

`Response`
```json
{
  "enrollmentId": ""
}
```
