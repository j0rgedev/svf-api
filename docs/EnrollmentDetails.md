### Detalles de la matrícula
Este endpoint se utiliza para obtener los detalles de la matrícula del presente año escolar.  
Debido a que este endpoint no requiere autenticación, se puede consumir directamente desde el navegador.

```http
GET /enrollment/details
```

`Response`
```json
{
  "schoolYear": "",
  "mainInfo": "",
  "termDetails": [
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
  "levelCosts": [
      {
          "name": "",
          "amount": ""
      },
      {
          "name": "",
          "amount": ""
      },
      {
          "name": "",
          "amount": ""
      }
  ]
}
```