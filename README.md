# SVF API

API design for SVF Enrollment System

## API Reference

## Base URL
/api/v1/student/enrollment/

#### Login validation

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
  "token": ""
}
```

#### Register student

```http
  POST /register/
```

`Body`
```json
{
  "password": ""
}
```


`Response`
```json
{
  "token": ""
}
```

#### Validation by SMS

```http
  POST /validation 
```

`Body`
```json
{
  "token": "",
  "sms": ""
}
```


`Response`
```json
{
  "msg": ""
}
```

#### Enrollment Process

```http
  POST /

```
`Body`
```json
{
  "token": ""
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

#### Enrollment details

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

```http
  POST /confirmation
```

`Body`
```json
{
  "token": ""
}
```

`Response`
```json
{
  "enrollmentId": ""
}
```